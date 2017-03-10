package com.unionpay.marcus.mycreditcarddemo;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * Created by marcus on 17/3/10.
 */

public class CCApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
