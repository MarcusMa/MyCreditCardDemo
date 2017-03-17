package com.unionpay.marcus.mycreditcarddemo.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unionpay.marcus.mycreditcarddemo.basic.CreditCardConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcus on 17/3/15.
 */

public class SQLManager extends SQLiteOpenHelper {
    private static SQLManager instance;

    private static final String DATABASE_NAME = "my_credit_cards";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME ="table_cards";
    private SQLiteDatabase database;

    public static SQLManager getInstance(Context mContext) {
        if (null == instance){
            instance = new SQLManager(mContext);
        }
        return instance;
    }

    private SQLManager(Context mContext){
        super(mContext,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = " CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + "( "
                + "_id integer primary key autoincrement "
                + CreditCardConstants.KEY_CREDIT_CARD_BANK_TYPE + " integer, "
                + CreditCardConstants.KEY_CREDIT_CARD_NUMBER + " text, "
                + CreditCardConstants.KEY_CREDIT_CARD_USER_NAME + " text "
                +" ) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveCreditCard(JSONArray jsonArray){
        if(null != jsonArray){
            checkDataBase();
            for( int i = 0;i<jsonArray.length();i++){
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String cardNo = obj.optString(CreditCardConstants.KEY_CREDIT_CARD_NUMBER);
                    String sql = " SELECT * FROM "
                            + TABLE_NAME
                            + " WHERE " + CreditCardConstants.KEY_CREDIT_CARD_NUMBER + " = ?";
                    // database.rawQuery(sql,new String[]{})
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    }

    public void queryByCardNumber(String cardNo){

    }

    private void checkDataBase(){
        if(database == null){
            database = getWritableDatabase();
        }
    }

}
