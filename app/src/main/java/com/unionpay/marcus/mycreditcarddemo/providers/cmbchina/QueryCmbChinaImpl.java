package com.unionpay.marcus.mycreditcarddemo.providers.cmbchina;

import com.unionpay.marcus.mycreditcarddemo.AppConfig;
import com.unionpay.marcus.mycreditcarddemo.basic.CreditCard;
import com.unionpay.marcus.mycreditcarddemo.basic.InitCallBack;
import com.unionpay.marcus.mycreditcarddemo.manager.HttpRequestManager;
import com.unionpay.marcus.mycreditcarddemo.manager.SharedPreferenceHelper;
import com.unionpay.marcus.mycreditcarddemo.providers.QueryInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.CmbChinaLoginActivity.CMBCHINA_SHARED_PERFERENCE_KEY_USER_SESSION_ID;

/**
 * Created by marcus on 17/3/9.
 */

public class QueryCmbChinaImpl implements QueryInterface {

    public String cmbchina_url = "https://xyk.cmbchina.com/login";
    public static final String CMBCHINA_BASIC_URL = "https://xyk.cmbchina.com";
    public static final String CMBCHINA_GET_TOTAL_BILL = "getTotalBill.do";
    public static final String ACTION_CMBCHINA_QUERY_LIMIT_BY_SESSION = "queryLimitBySession.do";
    public static final String ACTION_CMBCHINA_QUERY_BONUS_BY_SESSION = "queryBonusBySession.do";
    public static final String ACTION_CMBCHINA_QUERY_BONUS ="getBonus.do";
    public static final String ACTION_CMBCHINA_QUERY_CARD_LIMIT = "queryCardLimit.do";
    public static final String ACTION_CMBCHINA_SESSION_SESS = "session.sess";
    public static final String ACTION_CMBCHINA_LOGIN = "login.do";
    public static final String ACTION_CMBCHINA_VALID_CODE = "captcha.code";

    public static final String CMBCHINA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDd844tvJK4okLS0w3YUlgplte6cGFK7+6hDWfyxu99iLJEFnTW5AikqLpvn+E+oioZ5DiGjGhLxqPI45iGzDdJBWx8bNWkvmT/gAfTC/k0/6ZbgbycrLtxHKToldVS5e4UX+GcqFd+79la/pWLttdG9T/3wRE1KVmh36RuN32vWwIDAQAB";

    private static Map<String, String> resultMap = new HashMap<>();

    @Override
    public boolean isSessionValid() {
        return false;
    }

    @Override
    public String getRecentBill() {
        if (AppConfig.isMock) {
            return String.format("%.2f", Math.random() * 1000);
        }
        // TODO
        return null;
    }

    @Override
    public String getBonus() {
        if (AppConfig.isMock) {
            return "143434";
        }
        return null;
    }

    @Override
    public String getLimit() {
        if (AppConfig.isMock) {
            return String.format("%.2f", Math.random() * 1000);
        }
        return null;
    }

    public void initData(InitCallBack callBack) {

    }

    public static String getUserSessionId() {
        return SharedPreferenceHelper.getInstance(null).getString(CMBCHINA_SHARED_PERFERENCE_KEY_USER_SESSION_ID);
    }

    @Override
    public void initData(final CreditCard tmpCard, InitCallBack initCallBack) throws InterruptedException, JSONException {
        CountDownLatch latch = new CountDownLatch(3);//两个工人的协作

        Worker worker1 = new Worker(ACTION_CMBCHINA_QUERY_LIMIT_BY_SESSION, latch);
        Worker worker2 = new Worker(ACTION_CMBCHINA_QUERY_BONUS, latch);
        Worker worker3 = new Worker(CMBCHINA_GET_TOTAL_BILL, latch);
        worker1.start();
        worker2.start();
        worker3.start();
        latch.await();//等待所有工人完成工作

        String result = resultMap.get(ACTION_CMBCHINA_QUERY_LIMIT_BY_SESSION);

        // 信用额度
        JSONObject json = new JSONObject(result);
        String respCode = json.optString("respCode");
        if ("1000".equalsIgnoreCase(respCode)) {
            tmpCard.setSessionValid(true);
            String creditLimit = json.optString("creditLimit");
            String availLimit = json.optString("availLimit");
            tmpCard.setLeftLimit(Float.parseFloat(availLimit));
            tmpCard.setTotalLimit(Float.parseFloat(creditLimit));
        }

        // 积分
        result = resultMap.get(ACTION_CMBCHINA_QUERY_BONUS);

        json = new JSONObject(result);
        respCode = json.optString("respCode");
        if ("1000".equalsIgnoreCase(respCode)) {
            tmpCard.setSessionValid(true);
            String point = json.getJSONObject("data").optString("availPoint");
            tmpCard.setBonus(Integer.valueOf(point));
        }
        // 账单
        result = resultMap.get(CMBCHINA_GET_TOTAL_BILL);

        json = new JSONObject(result);
        respCode = json.optString("respCode");
        if ("1000".equalsIgnoreCase(respCode)) {
            tmpCard.setSessionValid(true);
            String point = json.optString("unpayAmountRMB");
            tmpCard.setRecentBill(Float.valueOf(point));
        }

        initCallBack.onFinished();
    }

    static class Worker extends Thread {
        String currentActionName;
        CountDownLatch latch;

        public Worker(String actionName, CountDownLatch latch) {
            this.currentActionName = actionName;
            this.latch = latch;
        }

        public void run() {
            String result = doWork();//工作了
            QueryCmbChinaImpl.resultMap.put(currentActionName, result);
            latch.countDown();//工人完成工作，计数器减一

        }

        private String doWork() {
            JSONObject obj = new JSONObject();
            String requestUrl = QueryCmbChinaImpl.CMBCHINA_BASIC_URL + "/" + currentActionName;
            String method = "POST";
            try {
                obj.put("actionName", currentActionName);
                obj.put("userSessionId", QueryCmbChinaImpl.getUserSessionId());
                obj.put("x_caller", ".0");
                obj.put("x_mchannel", "webapp");
                obj.put("x_traceid", "webapp");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return HttpRequestManager.getInstance().sendRequest(requestUrl, method, obj);
        }
    }
}
