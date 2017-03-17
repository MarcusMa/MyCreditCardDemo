package com.unionpay.marcus.mycreditcarddemo.basic;

import com.unionpay.marcus.mycreditcarddemo.providers.QueryInterface;
import com.unionpay.marcus.mycreditcarddemo.providers.bankcomm.QueryBankCommImpl;
import com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.QueryCmbChinaImpl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcus on 17/3/9.
 */

public class CreditCard {

    private int bankLabel;
    private String cardNumber;
    private String userName;
    private int bonus;
    private float recentBill;
    private float totalLimit;
    private float leftLimit;
    private boolean isSessionValid;
    private QueryInterface queryInterface;

    public static final CreditCard instacneCreditCardByJsonObject(JSONObject object){
        if (null!=object && object.has(CreditCardConstants.KEY_CREDIT_CARD_BANK_TYPE)
                && object.has(CreditCardConstants.KEY_CREDIT_CARD_NUMBER)){
            int bankLabel = object.optInt(CreditCardConstants.KEY_CREDIT_CARD_BANK_TYPE);
            String cardNumber = object.optString(CreditCardConstants.KEY_CREDIT_CARD_NUMBER);
            String userName = object.optString(CreditCardConstants.KEY_CREDIT_CARD_USER_NAME);
            return new CreditCard(bankLabel,cardNumber,userName);
        }
        else{
            return null;
        }
    }

    private CreditCard(int bankLabel,String cardNumber,String userName){
        setBankLabel(bankLabel);
        setCardNumber(cardNumber);
        setUserName(userName);
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
        initOther();  // init other info using queryInterface
    }

    private void initOther(){
        if(null!=queryInterface && queryInterface.isSessionValid()){
            setSessionValid(true);
            setBonus(Integer.valueOf(queryInterface.getBonus()));
            setRecentBill(Float.valueOf(queryInterface.getRecentBill()));
            setTotalLimit(Float.valueOf(queryInterface.getLimit()));
            setLeftLimit(Float.valueOf(queryInterface.getLimit()));
        }
        else{
            setSessionValid(false);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSessionValid() {
        return isSessionValid;
    }

    public void setSessionValid(boolean sessionValid) {
        isSessionValid = sessionValid;
    }

    public int getBonus() {
        return bonus;
    }

    public float getRecentBill() {
        return recentBill;
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

    public String getFormatedCardNumber(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(userName);
        buffer.append(" [尾号 ");
        buffer.append(cardNumber.substring(cardNumber.length()-4,cardNumber.length()));
        buffer.append(" ]");
        return buffer.toString();
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

    public String getFormatedBonus(){
        if(bonus<0){
            return "无";
        }
        else{
            return String.format("%d", bonus);
        }
    }

    public void initData(InitCallBack initCallBack){
        try {
            queryInterface.initData(this,initCallBack);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
