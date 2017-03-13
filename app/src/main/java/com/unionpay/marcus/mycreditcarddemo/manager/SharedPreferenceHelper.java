package com.unionpay.marcus.mycreditcarddemo.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.unionpay.marcus.mycreditcarddemo.AppConfig;
import com.unionpay.marcus.mycreditcarddemo.basic.CreditCardConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcus on 17/3/9.
 */

public class SharedPreferenceHelper {
    private static SharedPreferenceHelper instance;
    private static final String SHARED_PREFERENCE_KEY = "MyCreditCards";
    private SharedPreferences sharedPreferences;
    public static SharedPreferenceHelper getInstance(Context context) {
        if(null == instance){
            instance = new SharedPreferenceHelper(context);
        }
        return instance ;
    }

    private SharedPreferenceHelper(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
    }

    public static final JSONArray getLocalCreditCardList(){
        if (AppConfig.isMock){
            try {
                JSONObject obj = new JSONObject();
                obj.put(CreditCardConstants.KEY_CREDIT_CARD_BANK_TYPE, CreditCardConstants.BANK_LABEL_FOR_CMBCHINA);
                obj.put(CreditCardConstants.KEY_CREDIT_CARD_NUMBER,"6222 **** **** 1234");

                JSONObject obj2 = new JSONObject();
                obj2.put(CreditCardConstants.KEY_CREDIT_CARD_BANK_TYPE, CreditCardConstants.BANK_LABLE_FOR_BANKCOMM);
                obj2.put(CreditCardConstants.KEY_CREDIT_CARD_NUMBER,"6222 **** **** 4079");

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

    public String getString(String key){
        return sharedPreferences.getString(key, "");
    }

    public void saveString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(key,value);
        editor.commit();//提交修改
    }
}
