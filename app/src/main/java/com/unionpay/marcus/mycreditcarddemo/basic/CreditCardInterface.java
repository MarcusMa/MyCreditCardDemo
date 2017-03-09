package com.unionpay.marcus.mycreditcarddemo.basic;

/**
 * Created by marcus on 17/3/9.
 */

public interface CreditCardInterface {
    boolean isSessionValid();
    String queryRecentBill();
    String queryBonus();
    String queryLimit();
}
