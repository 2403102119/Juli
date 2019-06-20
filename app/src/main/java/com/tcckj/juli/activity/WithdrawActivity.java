package com.tcckj.juli.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
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
 * 提现界面
 */
public class WithdrawActivity extends BaseActivity {
    LinearLayout ll_withdraw_zhifubao,ll_withdraw_weixin,ll_withdraw_yinhangka;
    CheckBox cb_withdraw_zhifubao,cb_withdraw_weixin,cb_withdraw_yinhangka;
    TextView tv_withdraw_tip,tv_withdraw_sure,tv_withdraw_asset,et_withdraw_money,tv_withdraw_all,
            tv_withdraw_rate;

    private int type,factor;
    private double minAsset,allAsset;
    private String certificationState;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_withdraw);

        title.setText("提现");
        ll_withdraw_zhifubao = findView(R.id.ll_withdraw_zhifubao);
        ll_withdraw_weixin = findView(R.id.ll_withdraw_weixin);
        ll_withdraw_yinhangka = findView(R.id.ll_withdraw_yinhangka);
        cb_withdraw_zhifubao = findView(R.id.cb_withdraw_zhifubao);
        cb_withdraw_weixin = findView(R.id.cb_withdraw_weixin);
        cb_withdraw_yinhangka = findView(R.id.cb_withdraw_yinhangka);
        tv_withdraw_tip = findView(R.id.tv_withdraw_tip);
        tv_withdraw_sure = findView(R.id.tv_withdraw_sure);
        tv_withdraw_asset = findView(R.id.tv_withdraw_asset);
        tv_withdraw_all = findView(R.id.tv_withdraw_all);
        tv_withdraw_rate = findView(R.id.tv_withdraw_rate);
        et_withdraw_money = findView(R.id.et_withdraw_money);

        cb_withdraw_yinhangka.setChecked(true);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);

        ll_withdraw_zhifubao.setOnClickListener(this);
        ll_withdraw_weixin.setOnClickListener(this);
        ll_withdraw_yinhangka.setOnClickListener(this);
        cb_withdraw_zhifubao.setOnClickListener(this);
        cb_withdraw_weixin.setOnClickListener(this);
        cb_withdraw_yinhangka.setOnClickListener(this);
        tv_withdraw_sure.setOnClickListener(this);
        tv_withdraw_all.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            type = getIntent().getIntExtra("type", 6);
        }

        getPutForward(App.token, type+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.ll_withdraw_zhifubao:
            case R.id.cb_withdraw_zhifubao:
                cb_withdraw_zhifubao.setChecked(true);
                cb_withdraw_weixin.setChecked(false);
                cb_withdraw_yinhangka.setChecked(false);
                break;
            case R.id.ll_withdraw_weixin:
            case R.id.cb_withdraw_weixin:
                cb_withdraw_zhifubao.setChecked(false);
                cb_withdraw_weixin.setChecked(true);
                cb_withdraw_yinhangka.setChecked(false);
                break;
            case R.id.ll_withdraw_yinhangka:
            case R.id.cb_withdraw_yinhangka:
                cb_withdraw_zhifubao.setChecked(false);
                cb_withdraw_weixin.setChecked(false);
                cb_withdraw_yinhangka.setChecked(true);
                break;
            case R.id.tv_withdraw_sure:
                String moneyStr = et_withdraw_money.getText().toString();
//                String msg1="";//
                if (StringUtil.isSpace(moneyStr)){
//                    msg1="请输入提现金额";
                    toast("请输入提现金额");
                    break;
                }

                double money = Double.valueOf(moneyStr);
                if (money < minAsset){
                    toast("提现金额不可小于最小金额");
                    break;
                }

                if (money%factor > 0){
                    toast("提现金额必须要被倍数整除");
                    break;
                }

                if (cb_withdraw_zhifubao.isChecked() || cb_withdraw_weixin.isChecked()){
                    toast("功能尚未开放");
                    break;
                }

                switch (certificationState){
                    case "0":
                    case "3":
                        toast("请先实名认证");
                        break;
                    case "1":
                        toast("实名认证中");
                        break;
                    case "2":
                        initDialog(moneyStr);
                        break;
                }

                break;
            case R.id.tv_withdraw_all:
                et_withdraw_money.setText(StringUtil.doubleToString(allAsset));
                /*if (allAsset < minAsset){
                    toast("提现金额不可小于最小金额");
                    break;
                }

                if (allAsset%factor > 0){
                    toast("提现金额必须要被倍数整除");
                    break;
                }

                if (cb_withdraw_zhifubao.isChecked() || cb_withdraw_weixin.isChecked()){
                    toast("功能尚未开放");
                    break;
                }
                switch (certificationState){
                    case "1":
                    case "3":
                        toast("请先实名认证");
                        break;
                    case "0":
                        toast("实名认证中");
                        break;
                    case "2":
                        putForward(App.token, allAsset+"", type+"");
                        break;
                }*/
                break;
        }
    }


    /*
    提现输入弹窗
     */
    private void initDialog(final String moneyStr) {
        final Dialog dialog = new Dialog(WithdrawActivity.this, R.style.Dialog);
        View view = LayoutInflater.from(WithdrawActivity.this).inflate(R.layout.dialog_mine_change_name, null);
        TextView tv_dialog_change_name_left = (TextView) view.findViewById(R.id.tv_dialog_change_name_left);
        TextView tv_dialog_change_name_right = (TextView) view.findViewById(R.id.tv_dialog_change_name_right);
        TextView tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        TextView tv_dialog_content = (TextView) view.findViewById(R.id.tv_dialog_content);
        final EditText et_dialog_input_name = (EditText) view.findViewById(R.id.et_dialog_input_name);

        tv_dialog_title.setText("支付密码");
        et_dialog_input_name.setHint("请输入支付密码");

        tv_dialog_change_name_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_dialog_change_name_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payPassword = et_dialog_input_name.getText().toString().trim();
                if (StringUtil.isSpace(payPassword)){
                    toast("请输入支付密码");
                    return;
                }

                judgePayPassword(App.token, payPassword, moneyStr);
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        dialog.show();
    }


    /**
     * App36 > 提现接口
     */
    private void putForward(final String token, final String money, final String type) {
        if (NetUtil.isNetWorking(WithdrawActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.putForwardData(token, money, type, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                toast("提交成功，等待审核。。。");
                                finish();
                            }else {
                                toast(data.message);
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
     * App37 > 获取提现信息接口
     */
    private void getPutForward(final String token, final String type) {
        if (NetUtil.isNetWorking(WithdrawActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getPutForwardData(token, type, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.WithdrawMsg data = new Gson().fromJson(result, Bean.WithdrawMsg.class);
                            if (data.status == 1){
                                tv_withdraw_tip.setText("最小金额:" + data.putBase +"   倍数:" + data.putMultiple);
                                tv_withdraw_asset.setText("我的资产总数 " + StringUtil.doubleToString(data.money));
                                tv_withdraw_rate.setText("手续费:" + data.chargeMoney);
                                certificationState = data.realState;

                                minAsset = data.putBase;
                                factor = (int)data.putMultiple;
                                allAsset = data.money;
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
     * App41 > 支付密码验证
     */
    private void judgePayPassword(final String token, final String payPassword, final String moneyStr) {
        if (NetUtil.isNetWorking(WithdrawActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.judgePayPasswordData(token, payPassword, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.PayPassword data = new Gson().fromJson(result, Bean.PayPassword.class);
                            if (data.status == 1){
                                if ("1".equals(data.type)){
                                    putForward(App.token, moneyStr, type+"");
                                }else {
                                    toast("您尚未设置支付密码");
                                }
                            }else {
                                toast("请输入正确的支付密码");
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
