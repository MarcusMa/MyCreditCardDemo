package com.unionpay.marcus.mycreditcarddemo.providers;

import com.unionpay.marcus.mycreditcarddemo.basic.CreditCard;
import com.unionpay.marcus.mycreditcarddemo.basic.InitCallBack;

import org.json.JSONException;

/**
 * Created by marcus on 17/3/9.
 */

public interface QueryInterface {
    boolean isSessionValid();
    String getRecentBill();
    String getBonus();
    String getLimit();
    void initData(final CreditCard tmpCard, InitCallBack callBack) throws InterruptedException, JSONException;
}
