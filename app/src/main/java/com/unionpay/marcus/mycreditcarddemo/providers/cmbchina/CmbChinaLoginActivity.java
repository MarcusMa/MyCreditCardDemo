package com.unionpay.marcus.mycreditcarddemo.providers.cmbchina;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.unionpay.marcus.mycreditcarddemo.R;

public class CmbChinaLoginActivity extends AppCompatActivity {
    private BootstrapButton loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmb_china_login);
        loginBtn = (BootstrapButton) findViewById(R.id.loginCmbChinaBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.loginCmbChinaBtn){
                    //TODO
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

    }
}
