package com.unionpay.marcus.mycreditcarddemo.providers.cmbchina;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.unionpay.marcus.mycreditcarddemo.R;
import com.unionpay.marcus.mycreditcarddemo.basic.QueryCallBack;
import com.unionpay.marcus.mycreditcarddemo.manager.DataEngine;
import com.unionpay.marcus.mycreditcarddemo.manager.HttpRequestManager;
import com.unionpay.marcus.mycreditcarddemo.manager.SharedPreferenceHelper;
import com.unionpay.marcus.mycreditcarddemo.tools.RSAUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.QueryCmbChinaImpl.CMBCHINA_BASIC_URL;
import static com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.QueryCmbChinaImpl.CMBCHINA_LOGIN;
import static com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.QueryCmbChinaImpl.CMBCHINA_PUBLIC_KEY;
import static com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.QueryCmbChinaImpl.CMBCHINA_SESSION_SESS;
import static com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.QueryCmbChinaImpl.CMBCHINA_VALID_CODE;

public class CmbChinaLoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String  TAG = "CmbChinaLoginActivity";
    private final int MSG_CMBCHINA_DO_ALL_REQUEST = 31;
    private final int MSG_CMBCHINA_DO_LOGIN = 32;
    private final int MSG_CMBCHINA_SESSION_SESS = 33;
    private final int MSG_CMBCHINA_REQ_VALID_CODE = 34;
    private final int MSG_UPDATE_VALID_CODE_IMAGE = 35;
    private final int MSG_LOGIN_SUCCESS = 36;

    public static final String CMBCHINA_SHARED_PERFERENCE_KEY_USER_SESSION_ID = "userSessionId";

    private BootstrapButton loginBtn;
    private BootstrapEditText mCardNum,mPassword,mValidCode;
    private ImageView valideCodeImage;

    private String uuId;
    private String enUserId,enPassword;
    private HttpRequestManager httpRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmb_china_login);
        /** init view **/
        mCardNum = (BootstrapEditText) findViewById(R.id.user_card_num);
        mPassword = (BootstrapEditText) findViewById(R.id.user_password);
        mValidCode = (BootstrapEditText) findViewById(R.id.user_valid_code);
        valideCodeImage = (ImageView) findViewById(R.id.image_valid_code);

        valideCodeImage.setOnClickListener(this);

        loginBtn = (BootstrapButton) findViewById(R.id.loginCmbChinaBtn);
        loginBtn.setOnClickListener(this);

        httpRequestManager = HttpRequestManager.getInstance();
        updateValidCodeAction();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginCmbChinaBtn){
            try {
                RSAUtils.loadPublicKey(CMBCHINA_PUBLIC_KEY);
                enUserId = RSAUtils.encryptWithRSA(mCardNum.getText().toString());
                enPassword = RSAUtils.encryptWithRSA(mPassword.getText().toString());
                handler.sendEmptyMessage(MSG_CMBCHINA_SESSION_SESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // setResult(RESULT_OK);
            // finish();
        }else if (v.getId() == R.id.image_valid_code){
            updateValidCodeAction();
        }
    }

    private void updateValidCodeAction(){
        CmbChinaQueryTask task = new CmbChinaQueryTask(queryCallBack);
        task.execute(QueryCmbChinaImpl.CMBCHINA_VALID_CODE);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_UPDATE_VALID_CODE_IMAGE:
                    Bitmap bitmap = scaleBitmap(DataEngine.getInstance().getValidCodeBitmap(),3.0f);
                    valideCodeImage.setImageBitmap(bitmap);
                    break;
                case MSG_CMBCHINA_SESSION_SESS:
                    CmbChinaQueryTask task4 = new CmbChinaQueryTask(queryCallBack);
                    task4.execute(CMBCHINA_SESSION_SESS,enUserId);
                    break;
                case MSG_CMBCHINA_DO_LOGIN:
                    CmbChinaQueryTask task5 = new CmbChinaQueryTask(queryCallBack);
                    task5.execute(CMBCHINA_LOGIN,enPassword,enUserId,mValidCode.getText().toString(),uuId);
                    break;
                case MSG_LOGIN_SUCCESS:
                    setResult(RESULT_OK);
                    finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private QueryCallBack queryCallBack = new QueryCallBack() {
        @Override
        public void onSuccess(String actionName, String result) {
            Log.i(TAG, "onPostExecute(Result result) called");
            if (actionName == CMBCHINA_VALID_CODE) {
                handler.sendEmptyMessage(MSG_UPDATE_VALID_CODE_IMAGE);
            }
            else {
                try {
                    JSONObject json = new JSONObject(result);
                    String respCode = json.optString("respCode");
                    if ("1000".equalsIgnoreCase(respCode)) {
                        Message msg = Message.obtain();
                        switch ( actionName) {
                            case CMBCHINA_SESSION_SESS:
                                uuId = json.getString("uuid");
                                msg.what = MSG_CMBCHINA_DO_LOGIN;
                                break;
                            case CMBCHINA_LOGIN:
                                // save sessionId
                                String userSessionId = json.getString("userSessionId");
                                saveUserSessionId(userSessionId);
                                msg.what = MSG_LOGIN_SUCCESS;
                            default:
                                break;
                        }
                        handler.sendMessage(msg);
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onError(String actionName, String result) {

        }
    };


    public String getUserSessionId(){
        SharedPreferenceHelper.getInstance(getApplicationContext()).getString(CMBCHINA_SHARED_PERFERENCE_KEY_USER_SESSION_ID);
        return null;
    }

    public void saveUserSessionId(String userSessionId){
        // save to cookie manager
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(CMBCHINA_BASIC_URL, "userSessionId=" +userSessionId);
        CookieSyncManager.getInstance().sync();
        // save to sharedpreference
        SharedPreferenceHelper.getInstance(getApplicationContext()).saveString(CMBCHINA_SHARED_PERFERENCE_KEY_USER_SESSION_ID,userSessionId);
    }

    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }
}
