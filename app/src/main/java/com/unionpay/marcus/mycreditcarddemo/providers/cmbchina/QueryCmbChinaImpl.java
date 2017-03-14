package com.unionpay.marcus.mycreditcarddemo.providers.cmbchina;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.unionpay.marcus.mycreditcarddemo.AppConfig;
import com.unionpay.marcus.mycreditcarddemo.manager.SharedPreferenceHelper;
import com.unionpay.marcus.mycreditcarddemo.providers.QueryInterface;

/**
 * Created by marcus on 17/3/9.
 */

public class QueryCmbChinaImpl implements QueryInterface {

    public String cmbchina_url = "https://xyk.cmbchina.com/login";
    public static final String CMBCHINA_BASIC_URL = "https://xyk.cmbchina.com";
    public static final String CMBCHINA_GET_TOTAL_BILL = "getTotalBill.do";
    public static final String CMBCHINA_QUERY_LIMIT_BY_SESSION = "queryLimitBySession.do";
    public static final String CMBCHINA_QUERY_BONUS_BY_SESSION = "queryBonusBySession.do";
    public static final String CMBCHINA_QUERY_CARD_LIMIT = "queryCardLimit.do";
    public static final String CMBCHINA_SESSION_SESS = "session.sess";
    public static final String CMBCHINA_LOGIN = "login.do";
    public static final String CMBCHINA_VALID_CODE = "captcha.code";

    public static final String CMBCHINA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDd844tvJK4okLS0w3YUlgplte6cGFK7+6hDWfyxu99iLJEFnTW5AikqLpvn+E+oioZ5DiGjGhLxqPI45iGzDdJBWx8bNWkvmT/gAfTC/k0/6ZbgbycrLtxHKToldVS5e4UX+GcqFd+79la/pWLttdG9T/3wRE1KVmh36RuN32vWwIDAQAB";

    @Override
    public boolean isSessionValid() {
        if(AppConfig.isMock){
            return false;
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
            return "143434";
        }
        return null;
    }

    @Override
    public String getLimit() {
        if(AppConfig.isMock){
            return String.format("%.2f",Math.random()*1000);
        }
        return null;
    }

    public static String getUserSessionId(){
        return "";
    }
}
