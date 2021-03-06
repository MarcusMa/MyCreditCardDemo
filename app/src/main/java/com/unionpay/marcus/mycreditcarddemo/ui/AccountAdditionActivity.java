package com.unionpay.marcus.mycreditcarddemo.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.unionpay.marcus.mycreditcarddemo.R;
import com.unionpay.marcus.mycreditcarddemo.providers.bankcomm.BankCommLoginActivity;
import com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.CmbChinaLoginActivity;

public class AccountAdditionActivity extends AppCompatActivity implements View.OnClickListener{
    private BootstrapButton cmbChinaBtn,bankCommBtn;
    private Context mContext;
    public static final int REQ_CMBCHINA_ACCOUNT = 1001;
    public static final int REQ_BANKCOMM_ACCOUNT = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_addition);
        cmbChinaBtn = (BootstrapButton) findViewById(R.id.addCmbChinaBtn);
        bankCommBtn = (BootstrapButton) findViewById(R.id.addBankCommBtn);
        cmbChinaBtn.setOnClickListener(this);
        bankCommBtn.setOnClickListener(this);
        mContext = this;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addBankCommBtn){
            Intent intent = new Intent(mContext, BankCommLoginActivity.class);
            ((AccountAdditionActivity) mContext).startActivityForResult(intent, REQ_BANKCOMM_ACCOUNT);
        }
        else if(v.getId() == R.id.addCmbChinaBtn){
            Intent intent = new Intent(mContext, CmbChinaLoginActivity.class);
            ((AccountAdditionActivity) mContext).startActivityForResult(intent, REQ_CMBCHINA_ACCOUNT);
        }
        else
        {
            // do nothing
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            setResult(RESULT_OK,data);
            finish();
        }
    }
}

