package com.unionpay.marcus.mycreditcarddemo.manager;

import android.graphics.Bitmap;

/**
 * Created by marcus on 17/3/10.
 */

public class DataEngine {
    private static DataEngine instance;
    private Bitmap validCodeBitmap = null;
    private String sharedPreferenceCache;
    public static DataEngine getInstance() {
        if(null == instance){
            instance = new DataEngine();
        }
        return instance;
    }

    private DataEngine(){

    }

    public String getSharedPreferenceCache() {
        return sharedPreferenceCache;
    }

    public void setSharedPreferenceCache(String sharedPreferenceCache) {
        this.sharedPreferenceCache = sharedPreferenceCache;
    }

    public Bitmap getValidCodeBitmap() {
        return validCodeBitmap;
    }

    public void setValidCodeBitmap(Bitmap validCodeBitmap) {
        this.validCodeBitmap = validCodeBitmap;
    }
}
