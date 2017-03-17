package com.unionpay.marcus.mycreditcarddemo.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.unionpay.marcus.mycreditcarddemo.AppConfig;
import com.unionpay.marcus.mycreditcarddemo.basic.CreditCardConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.unionpay.marcus.mycreditcarddemo.basic.CreditCardConstants.KEY_CREDIT_CARDS;

/**
 * Created by marcus on 17/3/9.
 */

public class SharedPreferenceHelper {
    private static SharedPreferenceHelper instance;
    private static final String SHARED_PREFERENCE_KEY = "MyCreditCards";
    private SharedPreferences sharedPreferences;

    public static SharedPreferenceHelper getInstance(Context context) {
        if (null == instance) {
            instance = new SharedPreferenceHelper(context);
        }
        return instance;
    }

    private SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
    }

    public JSONArray getLocalCreditCardList() {
        String saveString = sharedPreferences.getString(KEY_CREDIT_CARDS, null);
        if (null != saveString && !saveString.isEmpty()) {
            try {
                JSONArray array = new JSONArray(saveString);
                return array;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void saveCreditCard(JSONArray array) {
        try {
            JSONArray localArray = getLocalCreditCardList();
            if (null != localArray) {
                for (int i = 0; i < array.length(); i++) {
                    boolean isSame = false;
                    JSONObject objI = array.getJSONObject(i);

                    for (int j = 0; j < localArray.length(); j++) {
                        JSONObject objJ = localArray.getJSONObject(j);
                        String localCardNum = objI.optString(CreditCardConstants.KEY_CREDIT_CARD_NUMBER);
                        if (localCardNum != null && localCardNum.equalsIgnoreCase(objJ.optString(CreditCardConstants.KEY_CREDIT_CARD_NUMBER))) {
                            isSame = true;
                            break;
                        }
                    }

                    if (!isSame) {
                        localArray.put(objI);
                    }
                }
            } else {
                localArray = array;
            }
            String saveString = localArray.toString();
            // DataEngine.getInstance().setSharedPreferenceCache(saveString);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_CREDIT_CARDS, saveString);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(key, value);
        editor.commit();//提交修改
    }
}
