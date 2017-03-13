package com.unionpay.marcus.mycreditcarddemo.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.unionpay.marcus.mycreditcarddemo.tools.IOUtils;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

import static com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.QueryCmbChinaImpl.CMBCHINA_VALID_CODE;

/**
 * Created by marcus on 17/3/10.
 */

public class HttpRequestManager {
    private static final String TAG = "HttpRequestManager";
    private static HttpRequestManager instance;;

    public static HttpRequestManager getInstance() {
        if(null == instance){
            instance = new HttpRequestManager();
        }
        return instance;
    }

    private HttpRequestManager(){

    }

    public String sendRequest(String url, String method, JSONObject params){
        try {
            Log.e(TAG, "url: " + url);
            Log.e(TAG, "method: " + method );
            if (null != params){
                Iterator<String> it = params.keys();
                while(it.hasNext()){
                    String key = it.next();
                    Log.e(TAG, "params: key ->" + key + " value ->"+params.getString(key));
                }
            }

            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{myX509TrustManager}, null);
            URL mReqUrl = new URL(url);
            URLConnection urlConnection = mReqUrl.openConnection();
            // HttpURLConnection conn = (HttpURLConnection) urlConnection;
            HttpsURLConnection conn = (HttpsURLConnection)  urlConnection;

            //设置套接工厂
            conn.setSSLSocketFactory(sslcontext.getSocketFactory());
            // set request Method
            conn.setRequestMethod(method);

            // set request CooKie
            String cookies = CookieManager.getInstance().getCookie(url);
            Log.d(TAG,"Request Cookie :" + cookies);
            conn.setRequestProperty("Cookie",cookies);

            if(method.equalsIgnoreCase("post")){
                // set request Body
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(params.toString());
                dos.flush();
                dos.close();
            }

            Log.d(TAG,">>>>> Response >>>>>");

            // according "Set-Cookie" field to add cookies into CookieManager
            Map<String, List<String>> headFields = conn.getHeaderFields();
            List<String> cookieList = headFields.get("Set-Cookie");
            if( null != cookieList){
                CookieManager cookieManager = CookieManager.getInstance();
                for(String cookie: cookieList) {
                    Log.e(TAG,"Response > Set-Cookie : "+ cookie);
                    cookieManager.setCookie(url, cookie);
                }
                // CookieSyncManager.getInstance().sync();
            }

            // set WebResourceResponse to return
            String charset = conn.getContentEncoding() != null ? conn.getContentEncoding() : Charset.defaultCharset().displayName();
            String mime = conn.getContentType();
            InputStream inputStream = conn.getInputStream();

            /** for cmbchina to get the valid code **/
            if (url.contains(CMBCHINA_VALID_CODE)){
                Bitmap codePic = BitmapFactory.decodeStream(inputStream);
                DataEngine.getInstance().setValidCodeBitmap(codePic);
            }

            byte[] pageContents = IOUtils.readFully(inputStream);

            // convert the contents and return
            Map<String,String> tmpMap = new HashMap<>();
            for(Map.Entry<String, List<String>> entry: headFields.entrySet()){
                String str = "";
                for( String tampering : entry.getValue()){
                    str = str + tampering;
                }
                tmpMap.put(entry.getKey(),str);
            }

            // convert the contents and return
            String strContents = new String(pageContents, "UTF-8");

            Log.d(TAG," WebResourceResponse >");
            Log.d(TAG, " Mime : " + mime +" Charset : " + charset +
                    " Response Code : " + conn.getResponseCode() +
                    " Phase Reason : " + "OK" +
                    " Header : " + tmpMap.toString() +
                    " Content : " + strContents);
            return strContents;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TrustManager myX509TrustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.d("X509TrustManager" , "checkClientTrusted()");
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.d("X509TrustManager" , "checkServerTrusted()");
            X509Certificate certificate = chain[0];
            X500Principal issuerPrincipal = certificate.getIssuerX500Principal();
            Log.d("X509TrustManager" , "issuer name :" + issuerPrincipal.getName());
            X500Principal subjectPrincipal = certificate.getSubjectX500Principal();
            Log.d("X509TrustManager" , "subject name :" + subjectPrincipal.getName());

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            Log.d("X509TrustManager" , "getAcceptedIssuers()");
            return null;
        }
    };
}
