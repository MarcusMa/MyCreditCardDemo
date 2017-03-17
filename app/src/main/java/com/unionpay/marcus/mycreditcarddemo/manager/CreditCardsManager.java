package com.unionpay.marcus.mycreditcarddemo.manager;

import android.content.Context;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.unionpay.marcus.mycreditcarddemo.basic.CreditCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcus on 17/3/9.
 */

public class CreditCardsManager {
    private static CreditCardsManager instance;
    private SharedPreferenceHelper sharedperferencehelper;
    private List<CreditCard> cards = new ArrayList<>();
    public int initEndCount;

    public static CreditCardsManager getInstance() {
        if (null == instance) {
            instance = new CreditCardsManager();
        }
        return instance;
    }

    private CreditCardsManager() {
        // TODO init
        sharedperferencehelper = SharedPreferenceHelper.getInstance(null);
        init();
    }

    public void init() {
        if (null != cards && cards.size() > 0) {
            cards.clear();
        }
        JSONArray cardjsonarray = null;
        if(!TextUtils.isEmpty(DataEngine.getInstance().getSharedPreferenceCache())){
            try {
                cardjsonarray = new JSONArray(DataEngine.getInstance().getSharedPreferenceCache());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            cardjsonarray = sharedperferencehelper.getLocalCreditCardList();
        }

        if (null != cardjsonarray && cardjsonarray.length() > 0) {
            int len = cardjsonarray.length();
            for (int i = len; i > 0; i--) {
                JSONObject object = cardjsonarray.optJSONObject(len - i);
                CreditCard card = CreditCard.instacneCreditCardByJsonObject(object);
                if (null != card) {
                    cards.add(card);
                }
            }
        }
    }

    public int getCount() {
        if (null == cards) {
            return 0;
        } else {
            return cards.size();
        }
    }

    public CreditCard getItem(int position) {
        if (null == cards || position >= cards.size() || position < 0) {
            return null;
        } else
            return cards.get(position);
    }
}
