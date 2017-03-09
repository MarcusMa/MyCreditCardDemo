package com.unionpay.marcus.mycreditcarddemo.basic;

import com.unionpay.marcus.mycreditcarddemo.providers.QueryInterface;
import com.unionpay.marcus.mycreditcarddemo.providers.bankcomm.QueryBankCommImpl;
import com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.QueryCmbChinaImpl;

import org.json.JSONObject;

/**
 * Created by marcus on 17/3/9.
 */

public class CreditCard implements CreditCardInterface{

    private int bankLabel;
    private String cardNumber;
    private int bonus;
    private float recentBill;
    private float totalLimit;
    private float leftLimit;
    private QueryInterface queryInterface;

    public static final CreditCard instacneCreditCardByJsonObject(JSONObject object){
        if (null!=object && object.has(CreditCardConstants.KEY_CREDIT_CARD_BANK_TYPE)
                && object.has(CreditCardConstants.KEY_CREDIT_CARD_NUMBER)){
            int bankLabel = object.optInt(CreditCardConstants.KEY_CREDIT_CARD_BANK_TYPE);
            String cardNumber = object.optString(CreditCardConstants.KEY_CREDIT_CARD_NUMBER);
            return new CreditCard(bankLabel,cardNumber);
        }
        else{
            return null;
        }
    }

    private CreditCard(int bankLabel,String cardNumber){
        setBankLabel(bankLabel);
        setCardNumber(cardNumber);
        switch (bankLabel){
            case CreditCardConstants.BANK_LABEL_FOR_CMBCHINA:
                queryInterface = new QueryCmbChinaImpl();
                break;
            case CreditCardConstants.BANK_LABLE_FOR_BANKCOMM:
                queryInterface = new QueryBankCommImpl();
                break;
            default:
                break;
        }
    }

    public int getBankLabel() {
        return bankLabel;
    }

    public void setBankLabel(int bankLabel) {
        this.bankLabel = bankLabel;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public void setRecentBill(float recentBill) {
        this.recentBill = recentBill;
    }

    public float getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(float totalLimit) {
        this.totalLimit = totalLimit;
    }

    public float getLeftLimit() {
        return leftLimit;
    }

    public void setLeftLimit(float leftLimit) {
        this.leftLimit = leftLimit;
    }


    @Override
    public boolean isSessionValid() {
        return false;
    }

    @Override
    public String queryRecentBill() {
        return null;
    }

    @Override
    public String queryBonus() {
        return null;
    }

    @Override
    public String queryLimit() {
        return null;
    }
}
