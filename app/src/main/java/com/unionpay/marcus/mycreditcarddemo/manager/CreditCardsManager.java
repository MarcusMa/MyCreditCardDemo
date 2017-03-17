package com.unionpay.marcus.mycreditcarddemo.manager;

import android.content.Context;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;

import com.unionpay.marcus.mycreditcarddemo.basic.CreditCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

import static com.unionpay.marcus.mycreditcarddemo.basic.CreditCardConstants.KEY_CREDIT_CARDS;

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
        JSONArray cardjsonarray  = sharedperferencehelper.getLocalCreditCardList();
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

    public void clear(){
        cards.clear();
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        cookieManager.removeAllCookie();
        sharedperferencehelper.saveString(KEY_CREDIT_CARDS,"");
    }
}
