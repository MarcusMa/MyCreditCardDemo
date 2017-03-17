package com.unionpay.marcus.mycreditcarddemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.unionpay.marcus.mycreditcarddemo.manager.HttpRequestManager;
import com.unionpay.marcus.mycreditcarddemo.providers.bankcomm.QueryBankCommImpl;
import com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.QueryCmbChinaImpl;

import java.util.Timer;
import java.util.TimerTask;

public class KeepLiveService extends Service {
    private static final String TAG = "KeepLiveService";
    HttpRequestManager httpRequestManager = HttpRequestManager.getInstance();
    private boolean[] bankTypes = new boolean[2];

    private TimerTask task;
    private Timer timer;
    public KeepLiveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bankTypes = intent.getBooleanArrayExtra("bankTypes");

        if(null == task){
            task = new TimerTask() {
                @Override
                public void run() {
                    Log.e(TAG,"*************** KEEP LIVE *********************");
                    if(bankTypes[0]){
                        Log.d(TAG,"cmbchina is keep live");
                        httpRequestManager.sendRequest(QueryCmbChinaImpl.KEEP_LIVE_URL,"GET",null);
                    }
                    if (bankTypes[1]){
                        Log.d(TAG,"bankcomm is keep live");
                        httpRequestManager.sendRequest(QueryBankCommImpl.KEEP_LIVE_URL,"GET",null);
                    }
                }
            };
        }
        if(null == timer){
            timer = new Timer();
            timer.schedule(task,1000,60000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"********************** onDestroy ******************");
        if(null != timer){
            timer.cancel();
            timer = null;
        }
        Intent service = new Intent(this, KeepLiveService.class);
        service.putExtra("bankTypes",bankTypes);
        this.startService(service);
        super.onDestroy();
    }
}
