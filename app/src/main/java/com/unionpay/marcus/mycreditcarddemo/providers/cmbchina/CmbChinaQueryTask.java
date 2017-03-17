package com.unionpay.marcus.mycreditcarddemo.providers.cmbchina;

import android.os.AsyncTask;
import android.util.Log;

import com.unionpay.marcus.mycreditcarddemo.basic.QueryCallBack;
import com.unionpay.marcus.mycreditcarddemo.manager.HttpRequestManager;

import org.json.JSONObject;


/**
 * Created by marcus on 17/3/10.
 */

public class CmbChinaQueryTask extends AsyncTask<String,Integer,String>{

    private static final String TAG = "CmbChinaQueryTask";
    private String currentActionName;
    private HttpRequestManager httpRequestManager;
    private QueryCallBack queryCallBack;

    public CmbChinaQueryTask(QueryCallBack callBack){
        httpRequestManager = HttpRequestManager.getInstance();
        queryCallBack = callBack;
    }

    //onPreExecute方法用于在执行后台任务前做一些UI操作
    @Override
    protected void onPreExecute() {
        Log.i(TAG, "CmbChinaQueryTask onPreExecute() called");
    }

    //doInBackground方法内部执行后台任务,不可在此方法内修改UI
    @Override
    protected String doInBackground(String... params) {
        Log.i(TAG, "doInBackground(Params... params) called");
        JSONObject obj = new JSONObject();
        currentActionName = params[0];
        String requestUrl = QueryCmbChinaImpl.CMBCHINA_BASIC_URL + "/" + currentActionName;
        String method = "POST";
        try {
            obj.put("actionName",currentActionName);
            if(QueryCmbChinaImpl.ACTION_CMBCHINA_SESSION_SESS.equalsIgnoreCase(currentActionName)){
                obj.put("idNo",params[1]);
                obj.put("idType","0");
                obj.put("x_caller",".0");
                obj.put("x_mchannel","webapp");
                obj.put("x_traceid","webapp");
            }else if(QueryCmbChinaImpl.ACTION_CMBCHINA_LOGIN.equalsIgnoreCase(currentActionName)){
                obj.put("encodedStr","");
                obj.put("loginType","0");
                obj.put("pin",params[1]);
                obj.put("realId",params[2]);
                obj.put("type","0");
                obj.put("valicode",params[3]);
                obj.put("x_caller","webapp");
                obj.put("x_mchannel","webapp");
                obj.put("x_traceid",params[4]);
            }
            else if(QueryCmbChinaImpl.ACTION_CMBCHINA_VALID_CODE.equalsIgnoreCase(currentActionName)){
                method = "GET";
            }
            else{
                obj.put("userSessionId", QueryCmbChinaImpl.getUserSessionId());
                obj.put("x_caller", ".0");
                obj.put("x_mchannel", "webapp");
                obj.put("x_traceid", "webapp");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return httpRequestManager.sendRequest(requestUrl,method,obj);
    }

    //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
    @Override
    protected void onPostExecute(String result) {
        queryCallBack.onResult(currentActionName,result);
    }
}
