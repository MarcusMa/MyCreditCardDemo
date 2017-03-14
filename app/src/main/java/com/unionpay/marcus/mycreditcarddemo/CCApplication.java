package com.unionpay.marcus.mycreditcarddemo;

import android.app.Application;
import android.content.Context;
import android.webkit.CookieSyncManager;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.unionpay.marcus.mycreditcarddemo.manager.SharedPreferenceHelper;

/**
 * Created by marcus on 17/3/10.
 */

public class CCApplication extends Application {
    SharedPreferenceHelper sharedperferencehelper;
    public static Context mContext;
    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
        CookieSyncManager.createInstance(getApplicationContext());
        mContext = getApplicationContext();
        sharedperferencehelper = SharedPreferenceHelper.getInstance(getApplicationContext());
    }
}
