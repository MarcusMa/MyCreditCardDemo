package com.unionpay.marcus.mycreditcarddemo.providers.bankcomm;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.unionpay.marcus.mycreditcarddemo.R;
import com.unionpay.marcus.mycreditcarddemo.basic.CreditCardConstants;
import com.unionpay.marcus.mycreditcarddemo.basic.QueryCallBack;
import com.unionpay.marcus.mycreditcarddemo.manager.SharedPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.unionpay.marcus.mycreditcarddemo.providers.bankcomm.QueryBankCommImpl.ACTION_BANKCOMM_BILLING_DETIAL;

public class BankCommLoginActivity extends AppCompatActivity {

    private static final String TAG = "BankCommLoginActivity";

    private static final int MSG_LOGIN_SUCCESS = 1;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_comm_login);
        mWebView = (WebView) findViewById(R.id.bank_comm_webview);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("test", "shouldOverrideUrlLoading: " + url);
                handler.sendEmptyMessage(MSG_LOGIN_SUCCESS);
                // handler.sendEmptyMessage(MSG_HIDE_WEB_VIEW);
                // handler.sendEmptyMessageDelayed(MSG_BANKCOMM_DO_BILLING_DETIAL_REQUEST,1000);
                // handler.sendEmptyMessage(MSG_CMBCHINA_DO_ALL_REQUEST); // for cmbchina
                //return false;
                // return super.shouldOverrideUrlLoading(view, cmbchina_url);
                return super.shouldOverrideUrlLoading(view,url);
            }


            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.contains("main.html"))
                {
                    Log.e("test", "shouldInterceptRequest: " + url);
                    // handler.sendEmptyMessage(MSG_HIDE_WEB_VIEW);
                    // handler.sendEmptyMessage(MSG_START_KEEP_LIVE_SERVICE);
                    // handler.sendEmptyMessage(MSG_CMBCHINA_DO_ALL_REQUEST); for cmbchina
                    // handler.sendEmptyMessage(MSG_BANKCOMM_DO_BILLING_DETIAL_REQUEST);
                    // handler.sendEmptyMessage(MSG_BANKCOMM_DO_ALL_REQUEST);
                }

                return super.shouldInterceptRequest(view, url);
            }
        });

        mWebView.loadUrl(QueryBankCommImpl.LOGIN_URL);

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_LOGIN_SUCCESS:
                    mWebView.setVisibility(View.GONE);
                    BankCommQueryTask task = new BankCommQueryTask(ACTION_BANKCOMM_BILLING_DETIAL, new QueryCallBack() {
                        @Override
                        public void onResult(String actionName, String result) {
                            if (ACTION_BANKCOMM_BILLING_DETIAL.equalsIgnoreCase(actionName)){
                                Document doc = Jsoup.parse(result);

                                Element element = doc.select("span.name").first();
                                String userName = element.text();
                                Log.d(TAG,"userName:" + userName);

                                JSONArray ourArray = new JSONArray();
                                Element select = doc.select("select#mycard").first();
                                Elements options = select.children();
                                for(int i=0;i<options.size();i++){
                                    Element tmpOption = options.get(i);
                                    Log.d(TAG,"CardNo:" + tmpOption.attr("value"));

                                    JSONObject newObj = new JSONObject();
                                    try {
                                        newObj.put(CreditCardConstants.KEY_CREDIT_CARD_USER_NAME,userName);
                                        newObj.put(CreditCardConstants.KEY_CREDIT_CARD_BANK_TYPE,CreditCardConstants.BANK_LABLE_FOR_BANKCOMM);
                                        newObj.put(CreditCardConstants.KEY_CREDIT_CARD_NUMBER,tmpOption.attr("value"));
                                        ourArray.put(newObj);

                                        if(ourArray.length()>0){
                                            SharedPreferenceHelper.getInstance(getApplicationContext()).saveCreditCard(ourArray);
                                        }

                                        setResult(RESULT_OK);
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }

                        @Override
                        public void onError(String actionName, String result) {

                        }
                    });
                    task.execute();
                    // setResult(RESULT_OK);
                    // finish();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

}
