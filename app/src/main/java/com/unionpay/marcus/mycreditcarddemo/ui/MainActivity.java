package com.unionpay.marcus.mycreditcarddemo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.unionpay.marcus.mycreditcarddemo.basic.InitCallBack;
import com.unionpay.marcus.mycreditcarddemo.manager.CreditCardsManager;
import com.unionpay.marcus.mycreditcarddemo.providers.bankcomm.QueryBankCommImpl;
import com.unionpay.marcus.mycreditcarddemo.providers.cmbchina.QueryCmbChinaImpl;

import static com.unionpay.marcus.mycreditcarddemo.basic.CreditCardConstants.BANK_LABEL_FOR_CMBCHINA;
import static com.unionpay.marcus.mycreditcarddemo.basic.CreditCardConstants.BANK_LABLE_FOR_BANKCOMM;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private CreditCardsManager creditCardsManager;
    private ListView mCardList;
    private ListAdapter mCardListAdapter;
    private LayoutInflater inflater;
    private FloatingActionButton mFab;
    private static final int REQ_CODE_FOR_ADD_BANK_ACCOUNT = 1;
    private LinearLayout mMainContainer, mLodingContainer;
    private TextView loadingTtitle;

    private static final int MSG_INIT_DATA_START = 1001;
    private static final int MSG_INIT_DATE_END = 1002;
    private static final int MSG_INTI_SINGLE_CARD_END = 1003;
    private static final int MSG_REFRESH_LIST = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        inflater = getLayoutInflater();
        /** initView view object **/
        mMainContainer = (LinearLayout) findViewById(R.id.content_main);
        mLodingContainer = (LinearLayout) findViewById(R.id.loading_page);
        loadingTtitle = (TextView) findViewById(R.id.loading_title);
        mCardList = (ListView) findViewById(R.id.cardList);
        // initView();
        creditCardsManager = CreditCardsManager.getInstance();
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AccountAdditionActivity.class);
                ((MainActivity) mContext).startActivityForResult(intent, REQ_CODE_FOR_ADD_BANK_ACCOUNT);
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
            }
        });
        handler.sendEmptyMessage(MSG_INIT_DATA_START);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("message", String.valueOf(msg.what));
            switch (msg.what) {
                case MSG_INTI_SINGLE_CARD_END:
                    CreditCardsManager.getInstance().initEndCount++;
                    if (CreditCardsManager.getInstance().initEndCount == CreditCardsManager.getInstance().getCount())
                        handler.sendEmptyMessage(MSG_INIT_DATE_END);
                    break;
                case MSG_INIT_DATA_START:
                    CreditCardsManager.getInstance().init();
                    CreditCardsManager.getInstance().initEndCount = 0;
                    mLodingContainer.setVisibility(View.VISIBLE);
                    loadingTtitle.setText("加载中...");
                    mFab.setVisibility(View.GONE);

                    if (creditCardsManager.getCount() == 0) {
                        mFab.setVisibility(View.VISIBLE);
                        loadingTtitle.setText("暂时没有任何信用卡");
                    }

                    for (int i = 0; i < creditCardsManager.getCount(); i++) {
                        final CreditCard tmpCard = creditCardsManager.getItem(i);
                        if (null != tmpCard) {
                            try {
                                tmpCard.initData(new InitCallBack() {
                                    @Override
                                    public void onFinished() {
                                        handler.sendEmptyMessage(MSG_INTI_SINGLE_CARD_END);
                                    }
                                });
                            } catch (Exception e) {
                                handler.sendEmptyMessage(MSG_INTI_SINGLE_CARD_END);
                            }
                        }
                    }

                    break;
                case MSG_INIT_DATE_END:
                    mLodingContainer.setVisibility(View.GONE);
                    mFab.setVisibility(View.VISIBLE);
                    initView();
                    break;
                case MSG_REFRESH_LIST:
                    creditCardsManager = CreditCardsManager.getInstance();
                    if (creditCardsManager.getCount() > 0) {
                        mLodingContainer.setVisibility(View.GONE);
                        mFab.setVisibility(View.VISIBLE);
                        initView();
                    }
                    break;
                default:
                    break;

            }
            super.handleMessage(msg);
        }
    };


    private void initView() {
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
                CreditCard card = (CreditCard) getItem(position);
                if (null == card) {
                    return null;
                }

                View item = inflater.inflate(R.layout.card_fragment, null);
                ImageView bankLogo = (ImageView) item.findViewById(R.id.bank_logo);
                TextView bankName = (TextView) item.findViewById(R.id.bank_name);
                TextView cardNumber = (TextView) item.findViewById(R.id.card_number);
                TextView recentBill = (TextView) item.findViewById(R.id.recentBill);
                TextView bonus = (TextView) item.findViewById(R.id.bonus);
                TextView letfLimit = (TextView) item.findViewById(R.id.leftLimit);
                TextView totalLimit = (TextView) item.findViewById(R.id.totalLimit);
                BootstrapButton needLoginBtn = (BootstrapButton) item.findViewById(R.id.needLoginBtn);
                LinearLayout cardDetialContainer = (LinearLayout) item.findViewById(R.id.layout_card_detial_container);
                LinearLayout billDetialContainer = (LinearLayout) item.findViewById(R.id.layout_bill_detial_container);
                RelativeLayout needLoginContrainer = (RelativeLayout) item.findViewById(R.id.layout_need_login_container);

                switch (card.getBankLabel()) {
                    case BANK_LABEL_FOR_CMBCHINA:
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
                cardNumber.setText(card.getFormatedCardNumber());

                if (card.isSessionValid()) {
                    // cookies 有效
                    needLoginContrainer.setVisibility(View.GONE);
                    cardDetialContainer.setVisibility(View.VISIBLE);
                    billDetialContainer.setVisibility(View.VISIBLE);
                    recentBill.setText(String.format("%.2f", card.getRecentBill()));
                    bonus.setText(card.getFormatedBonus());
                    letfLimit.setText(String.format("%.2f", card.getLeftLimit()));
                    totalLimit.setText(String.format("%.2f", card.getTotalLimit()));
                } else {
                    // cookies 无效
                    needLoginContrainer.setVisibility(View.VISIBLE);
                    cardDetialContainer.setVisibility(View.GONE);
                    billDetialContainer.setVisibility(View.GONE);
                    needLoginBtn.setTag(card);
                    needLoginBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.getId() == R.id.needLoginBtn) {
                                CreditCard card = (CreditCard) v.getTag();
                                Snackbar.make(v, card.getCardNumber(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    });
                }
                return item;
            }

        };
        mCardList.setAdapter(mCardListAdapter);
        // mCardListAdapter.();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_FOR_ADD_BANK_ACCOUNT && resultCode == RESULT_OK) {
            // refresh list
            //
            handler.sendEmptyMessage(MSG_INIT_DATA_START);

            // handler.sendEmptyMessage(MSG_INIT_DATA_START);
            Snackbar.make(mMainContainer, "need refresh list", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}
