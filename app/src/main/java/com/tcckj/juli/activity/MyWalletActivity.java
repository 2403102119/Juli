package com.tcckj.juli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.NetUtil;

import okhttp3.Call;

/**
 * 钱包界面
 */
public class MyWalletActivity extends BaseActivity {
    RelativeLayout rl_integral_wallet,rl_recommend_wallet,rl_dynamic_wallet,rl_static_wallet,
            rl_frozen_wallet;

    private Bean.Wallet wallet;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_my_wallet);

        title.setText("钱包");
        rightTxt.setText("提现记录");
        rightTxt.setVisibility(View.VISIBLE);

        rl_integral_wallet = findView(R.id.rl_integral_wallet);
        rl_recommend_wallet = findView(R.id.rl_recommend_wallet);
        rl_dynamic_wallet = findView(R.id.rl_dynamic_wallet);
        rl_static_wallet = findView(R.id.rl_static_wallet);
        rl_frozen_wallet = findView(R.id.rl_frozen_wallet);
    }

    @Override
    protected void initListener() {
        rl_integral_wallet.setOnClickListener(this);
        rl_recommend_wallet.setOnClickListener(this);
        rl_dynamic_wallet.setOnClickListener(this);
        rl_static_wallet.setOnClickListener(this);
        rl_frozen_wallet.setOnClickListener(this);
        back.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getWalletMoney(App.token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_integral_wallet:           //积分钱包
                Intent intent1 = new Intent(MyWalletActivity.this, WalletDetailActivity.class);
                intent1.putExtra("wallet_type", "integral");
                if (null != wallet) {
                    intent1.putExtra("can_release", wallet.jifenOne);
                    intent1.putExtra("releasing", wallet.jifenTwo);
                    intent1.putExtra("can_withdraw", wallet.jifenThree);
                }
                startActivity(intent1);
                break;
            case R.id.rl_recommend_wallet:          //推荐钱包
                Intent intent2 = new Intent(MyWalletActivity.this, WalletDetailActivity.class);
                intent2.putExtra("wallet_type", "recommend");
                if (null != wallet) {
                    intent2.putExtra("can_release", wallet.tuijianOne);
                    intent2.putExtra("releasing", wallet.tuijianTwo);
                    intent2.putExtra("can_withdraw", wallet.tuijianThree);
                }
                startActivity(intent2);
                break;
            case R.id.rl_dynamic_wallet:            //动态钱包
                Intent intent3 = new Intent(MyWalletActivity.this, WalletDetailActivity.class);
                intent3.putExtra("wallet_type", "dynamic");
                if (null != wallet) {
                    intent3.putExtra("can_release", wallet.dongtaiOne);
                    intent3.putExtra("releasing", wallet.dongtaiTwo);
                    intent3.putExtra("can_withdraw", wallet.dongtaiThree);
                }
                startActivity(intent3);
                break;
            case R.id.rl_static_wallet:              //静态钱包
                Intent intent4 = new Intent(MyWalletActivity.this, WalletDetailActivity.class);
                intent4.putExtra("wallet_type", "static");
                if (null != wallet) {
                    intent4.putExtra("can_release", wallet.jingtaiOne);
                    intent4.putExtra("releasing", wallet.jingtaiTwo);
                    intent4.putExtra("can_withdraw", wallet.jingtaiThree);
                }
                startActivity(intent4);
                break;
            case R.id.rl_frozen_wallet:                //冻结钱包
//                Intent intent5 = new Intent(MyWalletActivity.this, FrozenWalletActivity.class);
                Intent intent5 = new Intent(MyWalletActivity.this, WalletDetailActivity.class);
                intent5.putExtra("wallet_type", "frozen");
                if (null != wallet) {
                    intent5.putExtra("can_release", wallet.dongjieOne);
                    intent5.putExtra("releasing", wallet.dongjieTwo);
                    intent5.putExtra("can_withdraw", wallet.dongjieThree);
                }
                startActivity(intent5);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.rightTxt:
                startActivity(new Intent(MyWalletActivity.this, WithdrawRecordActivity.class));
                break;
        }
    }


    /**
     * App24 > 获取钱包信息
     */
    private void getWalletMoney(final String token) {
        if (NetUtil.isNetWorking(MyWalletActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getWalletMoneyData(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.WalletAll data = new Gson().fromJson(result, Bean.WalletAll.class);
                            if (data.status == 1){
                                wallet = data.model;
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
