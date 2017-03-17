package com.unionpay.marcus.mycreditcarddemo.providers.bankcomm;

import android.os.AsyncTask;

import com.unionpay.marcus.mycreditcarddemo.basic.QueryCallBack;
import com.unionpay.marcus.mycreditcarddemo.manager.HttpRequestManager;

import static com.unionpay.marcus.mycreditcarddemo.providers.bankcomm.QueryBankCommImpl.ACTION_BANKCOMM_BILLING_DETIAL;
import static com.unionpay.marcus.mycreditcarddemo.providers.bankcomm.QueryBankCommImpl.BANKCOMM_BASIC_URL;

/**
 * Created by marcus on 17/3/17.
 */

public class BankCommQueryTask extends AsyncTask<String,Integer,String> {
    private String currentActionName;
    private QueryCallBack callBack;
    private HttpRequestManager httpRequestManager;
    public BankCommQueryTask(String action, QueryCallBack callBack){
        this.currentActionName = action;
        this.callBack = callBack;
        httpRequestManager = HttpRequestManager.getInstance();
    }

    @Override
    protected String doInBackground(String... params) {
        String requestUrl = null;
        switch (currentActionName){
            case ACTION_BANKCOMM_BILLING_DETIAL:
                requestUrl = BANKCOMM_BASIC_URL + currentActionName ;
                break;
            default:
                break;
        }
        if (null != requestUrl)
            return httpRequestManager.sendRequest(requestUrl,"GET",null);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(null!=s)
            callBack.onResult(currentActionName,s);
        else{
            callBack.onError(currentActionName,s);
        }
    }
}
