package com.unionpay.marcus.mycreditcarddemo.ui;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.unionpay.marcus.mycreditcarddemo.R;
import com.unionpay.marcus.mycreditcarddemo.basic.CreditCard;
import com.unionpay.marcus.mycreditcarddemo.basic.CreditCardConstants;
import com.unionpay.marcus.mycreditcarddemo.manager.CreditCardsManager;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private CreditCardsManager creditCardsManager;
    private ListView mCardList;
    private ListAdapter mCardListAdapter;
    private LayoutInflater inflater;
    private static final int REQ_CODE_FOR_ADD_BANK_ACCOUNT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        inflater = getLayoutInflater();
        /** init view object **/
        mCardList = (ListView) findViewById(R.id.cardList);
        init();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,AccountAdditionActivity.class);
                ((MainActivity) mContext).startActivityForResult(intent,REQ_CODE_FOR_ADD_BANK_ACCOUNT);
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
            }
        });
    }

    private void init(){
        creditCardsManager = CreditCardsManager.getInstance();
        mCardListAdapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return creditCardsManager.getCount();
            }

            @Override
            public Object getItem(int position) {
                return creditCardsManager.getItem(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                CreditCard card =  (CreditCard) getItem(position);
                if(null == card){
                    return null;
                }

                View item = inflater.inflate(R.layout.card_fragment, null);
                ImageView bankLogo = (ImageView)item.findViewById(R.id.bank_logo);
                TextView bankName = (TextView) item.findViewById(R.id.bank_name);
                TextView cardNumber = (TextView) item.findViewById(R.id.card_number);
                TextView recentBill = (TextView)item.findViewById(R.id.recentBill);
                TextView bonus = (TextView)item.findViewById(R.id.bonus);
                TextView letfLimit = (TextView) item.findViewById(R.id.leftLimit);
                TextView totalLimit = (TextView) item.findViewById(R.id.totalLimit);
                BootstrapButton needLoginBtn = (BootstrapButton) item.findViewById(R.id.needLoginBtn);
                LinearLayout cardDetialContainer = (LinearLayout) item.findViewById(R.id.layout_card_detial_container);
                LinearLayout billDetialContainer = (LinearLayout) item.findViewById(R.id.layout_bill_detial_container);
                RelativeLayout needLoginContrainer = (RelativeLayout) item.findViewById(R.id.layout_need_login_container);

                switch (card.getBankLabel()){
                    case CreditCardConstants.BANK_LABEL_FOR_CMBCHINA:
                        bankLogo.setImageResource(R.mipmap.cmbchina_logo);
                        bankName.setText("招商银行");
                        break;
                    case CreditCardConstants.BANK_LABLE_FOR_BANKCOMM:
                        bankLogo.setImageResource(R.mipmap.bankcomm_logo);
                        bankName.setText("交通银行");
                        break;
                    default:
                        bankLogo.setImageResource(R.mipmap.ic_launcher);
                        bankName.setText("未知");
                        break;
                }
                cardNumber.setText(card.getCardNumber());

                if(card.isSessionValid()){
                    // cookies 有效
                    needLoginContrainer.setVisibility(View.GONE);
                    cardDetialContainer.setVisibility(View.VISIBLE);
                    billDetialContainer.setVisibility(View.VISIBLE);
                    recentBill.setText(String.format("%.2f",card.getRecentBill()));
                    bonus.setText(String.format("%d",card.getBonus()));
                    letfLimit.setText(String.format("%.2f",card.getLeftLimit()));
                    totalLimit.setText(String.format("%.2f",card.getLeftLimit()));
                }
                else{
                    // cookies 无效
                    needLoginContrainer.setVisibility(View.VISIBLE);
                    cardDetialContainer.setVisibility(View.GONE);
                    billDetialContainer.setVisibility(View.GONE);
                    needLoginBtn.setTag(card);
                    needLoginBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.getId() == R.id.needLoginBtn){
                                CreditCard card = (CreditCard) v.getTag();
                                Snackbar.make(v, card.getCardNumber() , Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    });
                }
                return item;
            }

        };
        mCardList.setAdapter(mCardListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
