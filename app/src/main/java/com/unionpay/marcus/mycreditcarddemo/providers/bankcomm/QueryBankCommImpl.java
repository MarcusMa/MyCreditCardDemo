package com.unionpay.marcus.mycreditcarddemo.providers.bankcomm;

import com.unionpay.marcus.mycreditcarddemo.AppConfig;
import com.unionpay.marcus.mycreditcarddemo.providers.QueryInterface;

/**
 * Created by marcus on 17/3/9.
 */

public class QueryBankCommImpl implements QueryInterface {
    @Override
    public boolean isSessionValid() {
        if(AppConfig.isMock){
            return true;
        }
        // TODO
        return false;
    }

    @Override
    public String getRecentBill() {
        if(AppConfig.isMock){
            return String.format("%.2f",Math.random()*1000);
        }
        // TODO
        return null;
    }

    @Override
    public String getBonus() {
        if(AppConfig.isMock){
            return "123234";
        }
        // TODO
        return null;
    }

    @Override
    public String getLimit() {
        if(AppConfig.isMock){
            return String.format("%.2f",Math.random()*1000);
        }
        // TODO
        return null;
    }
}
