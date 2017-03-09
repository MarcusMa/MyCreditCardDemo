package com.unionpay.marcus.mycreditcarddemo.providers.cmbchina;

import com.unionpay.marcus.mycreditcarddemo.providers.QueryInterface;

/**
 * Created by marcus on 17/3/9.
 */

public class QueryCmbChinaImpl implements QueryInterface {
    @Override
    public boolean isSessionValid() {
        return false;
    }

    @Override
    public String getRecentBill() {
        return null;
    }

    @Override
    public String getBonus() {
        return null;
    }

    @Override
    public String getLimit() {
        return null;
    }
}
