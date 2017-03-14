package com.unionpay.marcus.mycreditcarddemo.basic;

/**
 * Created by marcus on 17/3/10.
 */

public interface QueryCallBack {
    void onResult(String actionName, String result);
    void onError(String actionName,String result);
}
