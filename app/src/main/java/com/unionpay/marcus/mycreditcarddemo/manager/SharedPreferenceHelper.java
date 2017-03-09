package com.unionpay.marcus.mycreditcarddemo.manager;

import com.unionpay.marcus.mycreditcarddemo.AppConfig;
import com.unionpay.marcus.mycreditcarddemo.basic.BankConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcus on 17/3/9.
 */

public class SharedPreferenceHelper {
    private static SharedPreferenceHelper instance;
    public static SharedPreferenceHelper getInstance() {
        if(null == instance){
            instance = new SharedPreferenceHelper();
        }
        return instance ;
    }

    private SharedPreferenceHelper(){
        //TODO init shareperferencehelper
    }
    
    public static final JSONArray getLocalCreditCardList(){
        if (AppConfig.isMock){
            try {
                JSONObject obj = new JSONObject();
                obj.put(BankConstants.KEY_CREDIT_CARD_BANK_TYPE,BankConstants.BANK_LABEL_FOR_CMBBANK);
                obj.put(BankConstants.KEY_CREDIT_CARD_NUMBER,"6222 **** **** 1234");

                JSONObject obj2 = new JSONObject();
                obj2.put(BankConstants.KEY_CREDIT_CARD_BANK_TYPE,BankConstants.BANK_LABLE_FOR_BANKCOMM);
                obj2.put(BankConstants.KEY_CREDIT_CARD_NUMBER,"6222 **** **** 4079");

                JSONArray array = new JSONArray();
                array.put(obj);
                array.put(obj2);
                return array;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        else{
            //FIXME donot use the mock data
            return  null;
        }
    }

    public static void saveCreditCard(){
        //TODO save a card info to sharepreference
    }
    
}