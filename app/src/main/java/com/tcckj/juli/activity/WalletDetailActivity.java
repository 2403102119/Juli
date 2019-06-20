package com.tcckj.juli.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.StringUtil;

import okhttp3.Call;

/**
 * 钱包详情界面
 */
public class WalletDetailActivity extends BaseActivity {
    TextView tv_wallet_withdraw,tv_wallet_release;
    TextView tv_dialog_change_name_left,tv_dialog_change_name_right,tv_dialog_title,
            tv_dialog_content,tv_can_release_money,tv_releasing_money,tv_can_withdraw_money;
    EditText et_dialog_input_name;

    Dialog dialog;
    private double canReleaseMoney, releasingMoney, canWithdrawMoney;
    private int walletType = 6;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_wallet_detail);

        tv_wallet_withdraw = findView(R.id.tv_wallet_withdraw);
        tv_wallet_release = findView(R.id.tv_wallet_release);
        tv_can_release_money = findView(R.id.tv_can_release_money);
        tv_releasing_money = findView(R.id.tv_releasing_money);
        tv_can_withdraw_money = findView(R.id.tv_can_withdraw_money);

        initDialog();

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(false);
    }

    @Override
    protected void initListener() {
        tv_wallet_withdraw.setOnClickListener(this);
        tv_wallet_release.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            if ("integral".equals(getIntent().getStringExtra("wallet_type"))){
                title.setText("积分钱包");
                walletType = 0;
            }else if ("recommend".equals(getIntent().getStringExtra("wallet_type"))){
                title.setText("推荐钱包");
                walletType = 1;
            }else if ("dynamic".equals(getIntent().getStringExtra("wallet_type"))){
                title.setText("动态钱包");
                walletType = 2;
            }else if ("static".equals(getIntent().getStringExtra("wallet_type"))){
                title.setText("静态钱包");
                walletType = 3;
            }else if ("frozen".equals(getIntent().getStringExtra("wallet_type"))){
                title.setText("冻结钱包");
                walletType = 4;
            }

//            canReleaseMoney = getIntent().getDoubleExtra("can_release", 0);
//            releasingMoney = getIntent().getDoubleExtra("releasing", 0);
//            canWithdrawMoney = getIntent().getDoubleExtra("can_withdraw", 0);

            tv_can_release_money.setText(StringUtil.doubleToString(canReleaseMoney));
            tv_releasing_money.setText(StringUtil.doubleToString(releasingMoney));
            tv_can_withdraw_money.setText(StringUtil.doubleToString(canWithdrawMoney));
        }

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getWalletMoney(App.token);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWalletMoney(App.token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_wallet_withdraw:       //提现
                Intent intent = new Intent(WalletDetailActivity.this, WithdrawActivity.class);
                intent.putExtra("type", walletType);
                startActivity(intent);
                break;
            case R.id.tv_wallet_release:
                initDialog();
                dialog.show();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_dialog_change_name_left:
                dialog.dismiss();
                break;
            case R.id.tv_dialog_change_name_right:
                String releaseNum = et_dialog_input_name.getText().toString().trim();
                if (StringUtil.isSpace(releaseNum)){
                    toast("请输入释放金额");
                    break;
                }

                double releaseMoney = Double.valueOf(releaseNum);
                if (releaseMoney > canReleaseMoney){
                    Log.i("1111111111", "onClick: " + releaseMoney + "--------" + canReleaseMoney);
                    toast("释放金额不可大于可释放金额");
                    break;
                }else if(releaseMoney < 1) {
                    toast("释放金额不可小于1");
                }else {
                    releaseWallet(App.token, releaseNum, walletType+"");
                }

                dialog.dismiss();
                break;
        }
    }

    private void initDialog() {
        dialog = new Dialog(WalletDetailActivity.this, R.style.Dialog);
        View view = LayoutInflater.from(WalletDetailActivity.this).inflate(R.layout.dialog_mine_change_name, null);
        tv_dialog_change_name_left = (TextView) view.findViewById(R.id.tv_dialog_change_name_left);
        tv_dialog_change_name_right = (TextView) view.findViewById(R.id.tv_dialog_change_name_right);
        tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        tv_dialog_content = (TextView) view.findViewById(R.id.tv_dialog_content);
        et_dialog_input_name = (EditText) view.findViewById(R.id.et_dialog_input_name);
        tv_dialog_change_name_left.setOnClickListener(this);
        tv_dialog_change_name_right.setOnClickListener(this);

        tv_dialog_title.setText("释放金额");
        et_dialog_input_name.setHint("请输入释放金额");

        et_dialog_input_name.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
    }

    /**
     * App34 > 释放接口
     */
    private void releaseWallet(final String token, final String money, final String type) {
        if (NetUtil.isNetWorking(WalletDetailActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.releaseWalletData(token, money, type, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            toast(data.message);
                            if (data.status == 1){
                                getWalletMoney(App.token);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            finishRefresh();
                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }


    /**
     * App24 > 获取钱包信息
     */
    private void getWalletMoney(final String token) {
        if (NetUtil.isNetWorking(WalletDetailActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getWalletMoneyData(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.WalletAll data = new Gson().fromJson(result, Bean.WalletAll.class);
                            if (data.status == 1){
                                Bean.Wallet wallet = data.model;

                                switch (walletType){
                                    case 0:
                                        tv_can_release_money.setText(StringUtil.doubleToString(wallet.jifenOne));
                                        tv_releasing_money.setText(StringUtil.doubleToString(wallet.jifenTwo));
                                        tv_can_withdraw_money.setText(StringUtil.doubleToString(wallet.jifenThree));

                                        canReleaseMoney = wallet.jifenOne;
                                        releasingMoney = wallet.jifenTwo;
                                        canWithdrawMoney = wallet.jifenThree;

                                        break;
                                    case 1:
                                        tv_can_release_money.setText(StringUtil.doubleToString(wallet.tuijianOne));
                                        tv_releasing_money.setText(StringUtil.doubleToString(wallet.tuijianTwo));
                                        tv_can_withdraw_money.setText(StringUtil.doubleToString(wallet.tuijianThree));

                                        canReleaseMoney = wallet.tuijianOne;
                                        releasingMoney = wallet.tuijianTwo;
                                        canWithdrawMoney = wallet.tuijianThree;

                                        break;
                                    case 2:
                                        tv_can_release_money.setText(StringUtil.doubleToString(wallet.dongtaiOne));
                                        tv_releasing_money.setText(StringUtil.doubleToString(wallet.dongtaiTwo));
                                        tv_can_withdraw_money.setText(StringUtil.doubleToString(wallet.dongtaiThree));

                                        canReleaseMoney = wallet.dongtaiOne;
                                        releasingMoney = wallet.dongtaiTwo;
                                        canWithdrawMoney = wallet.dongtaiThree;

                                        break;
                                    case 3:
                                        tv_can_release_money.setText(StringUtil.doubleToString(wallet.jingtaiOne));
                                        tv_releasing_money.setText(StringUtil.doubleToString(wallet.jingtaiTwo));
                                        tv_can_withdraw_money.setText(StringUtil.doubleToString(wallet.jingtaiThree));

                                        canReleaseMoney = wallet.jingtaiOne;
                                        releasingMoney = wallet.jingtaiTwo;
                                        canWithdrawMoney = wallet.jingtaiThree;

                                        break;
                                    case 4:
                                        tv_can_release_money.setText(StringUtil.doubleToString(wallet.dongjieOne));
                                        tv_releasing_money.setText(StringUtil.doubleToString(wallet.dongjieTwo));
                                        tv_can_withdraw_money.setText(StringUtil.doubleToString(wallet.dongjieThree));

                                        canReleaseMoney = wallet.dongjieOne;
                                        releasingMoney = wallet.dongjieTwo;
                                        canWithdrawMoney = wallet.dongjieThree;

                                        break;
                                }

                                finishRefresh();
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            finishRefresh();
                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }


}
