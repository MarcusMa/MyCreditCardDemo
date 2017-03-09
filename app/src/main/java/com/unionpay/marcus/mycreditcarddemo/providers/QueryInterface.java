package com.unionpay.marcus.mycreditcarddemo.providers;

/**
 * Created by marcus on 17/3/9.
 */

public interface QueryInterface {
    boolean isSessionValid();
    String getRecentBill();
    String getBonus();
    String getLimit();
}
