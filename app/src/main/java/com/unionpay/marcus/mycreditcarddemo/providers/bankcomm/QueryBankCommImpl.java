package com.unionpay.marcus.mycreditcarddemo.providers.bankcomm;

import android.util.Log;

import com.unionpay.marcus.mycreditcarddemo.AppConfig;
import com.unionpay.marcus.mycreditcarddemo.basic.CreditCard;
import com.unionpay.marcus.mycreditcarddemo.basic.InitCallBack;
import com.unionpay.marcus.mycreditcarddemo.manager.HttpRequestManager;
import com.unionpay.marcus.mycreditcarddemo.providers.QueryInterface;
import com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.QueryCmbChinaImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by marcus on 17/3/9.
 */

public class QueryBankCommImpl implements QueryInterface {

    public static final String LOGIN_URL = "https://creditcardapp.bankcomm.com/idm/sso/login.html";
    public static final String BANKCOMM_BASIC_URL = "https://creditcardapp.bankcomm.com/";
    public static final String ACTION_BANKCOMM_BILLING_DETIAL = "member/member/service/billing/detail.html";
    public static final String ACTION_BANKCOMM_BILLING_INFO_QRY = "member/member/service/billing/billingInfoQry.html";
    public static final String ACTION_BANKCOMM_POINT_INFO_QRY = "member/member/service/billing/pointInfoQry.html";
    public static final String ACTION_HOME_INDEX_ITEM = "member/member/home/index_item.html";
    public static final String ACTION_BANKCOMM_LIMIT_QRY = "member/member/limit/info.json";

    public static final String KEEP_LIVE_URL = BANKCOMM_BASIC_URL + ACTION_BANKCOMM_BILLING_DETIAL;

    private static Map<String, String> resultMap = new HashMap<>();

    @Override
    public boolean isSessionValid() {
        if(AppConfig.isMock){
            return true;
        }
        // TODO
        return false;
    }

    @Override
    public String getRecentBill() {
        if(AppConfig.isMock){
            return String.format("%.2f",Math.random()*1000);
        }
        // TODO
        return null;
    }

    @Override
    public String getBonus() {
        if(AppConfig.isMock){
            return "123234";
        }
        // TODO
        return null;
    }

    @Override
    public String getLimit() {
        if(AppConfig.isMock){
            return String.format("%.2f",Math.random()*1000);
        }
        // TODO
        return null;
    }

    @Override
    public void initData(CreditCard tmpCard, InitCallBack initCallBack) throws InterruptedException, JSONException {
        CountDownLatch latch = new CountDownLatch(3);//两个工人的协作
        String cardNo = tmpCard.getCardNumber();
        QueryBankCommImpl.Worker worker1 = new QueryBankCommImpl.Worker(ACTION_BANKCOMM_LIMIT_QRY, cardNo, latch);
        QueryBankCommImpl.Worker worker2 = new QueryBankCommImpl.Worker(ACTION_BANKCOMM_POINT_INFO_QRY, cardNo, latch);
        QueryBankCommImpl.Worker worker3 = new QueryBankCommImpl.Worker(ACTION_BANKCOMM_BILLING_INFO_QRY, cardNo, latch);
        worker1.start();
        worker2.start();
        worker3.start();
        latch.await();//等待所有工人完成工作


        // 信用额度
        String result = resultMap.get(ACTION_BANKCOMM_LIMIT_QRY);

        try{
            JSONObject jsonObject = new JSONObject(result);
            String totalLimit = jsonObject.optString("crdLmt");
            String leftLimit  = jsonObject.optString("avacrdlmt");
            tmpCard.setLeftLimit(Float.valueOf(leftLimit));
            tmpCard.setTotalLimit(Float.valueOf(totalLimit));
        }
        catch (Exception e){
            tmpCard.setSessionValid(false);
        }



        // Document doc = Jsoup.parse(result);


        // 积分
        result = resultMap.get(ACTION_BANKCOMM_POINT_INFO_QRY);
        if(null == result || result.contains("登录部分")){
            tmpCard.setSessionValid(false);
        }
        else{
            tmpCard.setSessionValid(true);
            Document doc = Jsoup.parse(result);
            Element element = doc.select("li").first();
            Log.d("test","element value" + element.text());
            Pattern pattern1 = Pattern.compile("[0-9]+");
            Matcher matcher = pattern1.matcher(element.text());
            String pointMsg = "-1";
            while (matcher.find()) {
                Log.d("test"," point value :" + String.valueOf(matcher.groupCount()));
                pointMsg = matcher.group();
                break;
            }
            tmpCard.setBonus(Integer.valueOf(pointMsg));
        }

        // 账单
        result = resultMap.get(ACTION_BANKCOMM_BILLING_INFO_QRY);
        if(null == result || result.contains("登录部分")){
            tmpCard.setSessionValid(false);
        }
        else{
            tmpCard.setSessionValid(true);
            Document doc = Jsoup.parse(result);
            Element element = doc.select("li.mt16").first();
            Log.d("test","element value" + element.text());
            Pattern pattern1 = Pattern.compile("\\-?[0-9]+.[0-9]+");
            Matcher matcher = pattern1.matcher(element.text());
            String pointMsg = "0.0";
            while (matcher.find()) {
                Log.d("test"," point value :" + String.valueOf(matcher.groupCount()));
                pointMsg = matcher.group();
                break;
            }
            tmpCard.setRecentBill(Float.valueOf(pointMsg));
        }

        initCallBack.onFinished();
    }

    static class Worker extends Thread {
        String currentActionName;
        String cardNo;
        CountDownLatch latch;

        public Worker(String actionName,String cardNo, CountDownLatch latch) {
            this.currentActionName = actionName;
            this.latch = latch;
            this.cardNo = cardNo;
        }

        public void run() {
            String result = doWork();//工作了
            resultMap.put(currentActionName, result);
            latch.countDown();//工人完成工作，计数器减一
        }

        private String doWork() {
            JSONObject obj = new JSONObject();
            String requestUrl = BANKCOMM_BASIC_URL + currentActionName;
            String method = "GET";
            String encodeCardNo = null;
            try {
                encodeCardNo = URLEncoder.encode(cardNo,"utf-8");
                switch (currentActionName){
                    case ACTION_BANKCOMM_LIMIT_QRY:

                        requestUrl = requestUrl +"?cardNo=" + encodeCardNo;
                        break;
                    case ACTION_BANKCOMM_POINT_INFO_QRY:
                        requestUrl = requestUrl +"?cardNo=" + encodeCardNo;
                        break;
                    case ACTION_BANKCOMM_BILLING_INFO_QRY:
                        requestUrl = requestUrl +"?cardNo=" + encodeCardNo;
                        break;
                    default:break;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return HttpRequestManager.getInstance().sendRequest(requestUrl, method, obj);
        }
    }
}
