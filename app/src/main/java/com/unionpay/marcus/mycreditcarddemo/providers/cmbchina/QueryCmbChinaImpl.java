package com.unionpay.marcus.mycreditcarddemo.providers.cmbchina;

import com.unionpay.marcus.mycreditcarddemo.AppConfig;
import com.unionpay.marcus.mycreditcarddemo.providers.QueryInterface;

/**
 * Created by marcus on 17/3/9.
 */

public class QueryCmbChinaImpl implements QueryInterface {
    @Override
    public boolean isSessionValid() {
        if(AppConfig.isMock){
            return false;
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
            return "143434";
        }
        return null;
    }

    @Override
    public String getLimit() {
        if(AppConfig.isMock){
            return String.format("%.2f",Math.random()*1000);
        }
        return null;
    }
}
