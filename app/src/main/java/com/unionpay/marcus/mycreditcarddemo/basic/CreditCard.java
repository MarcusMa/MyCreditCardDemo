package com.unionpay.marcus.mycreditcarddemo.basic;

import org.json.JSONObject;

/**
 * Created by marcus on 17/3/9.
 */

public class CreditCard {

    private int bankLabel;
    private String cardNumber;
    private int bonus;
    private float recentBill;
    private float totalLimit;
    private float leftLimit;

    public static final CreditCard instacneCreditCardByJsonObject(JSONObject object){
        if (null!=object && object.has(BankConstants.KEY_CREDIT_CARD_BANK_TYPE)
                && object.has(BankConstants.KEY_CREDIT_CARD_NUMBER)){
            int bankLabel = object.optInt(BankConstants.KEY_CREDIT_CARD_BANK_TYPE);
            String cardNumber = object.optString(BankConstants.KEY_CREDIT_CARD_NUMBER);
            return new CreditCard(bankLabel,cardNumber);
        }
        else{
            return null;
        }
    }

    private CreditCard(int bankLabel,String cardNumber){
        setBankLabel(bankLabel);
        setCardNumber(cardNumber);
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

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public float getRecentBill() {
        return recentBill;
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


}
