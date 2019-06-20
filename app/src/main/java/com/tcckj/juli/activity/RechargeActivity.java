package com.tcckj.juli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
 * 充值界面
 */
public class RechargeActivity extends BaseActivity {
    LinearLayout ll_recharge_zhifubao,ll_recharge_weixin,ll_recharge_yinhangka,
            ll_recharge_jingdong, ll_recharge_wangyin,ll_recharge_kuaijie,ll_recharge_xianxia,
            ll_recharge_saoma,ll_recharge_upload_picture;
    CheckBox cb_charge_zhifubao,cb_charge_weixin,cb_charge_yinhangka,cb_charge_jingdong,
            cb_charge_wangyin,cb_charge_kuaijie,cb_charge_xianxia;
    TextView tv_recharge_sure;
    EditText et_recharge_input_money;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_recharge);

        title.setText("充值");

        ll_recharge_zhifubao = findView(R.id.ll_recharge_zhifubao);
        ll_recharge_weixin = findView(R.id.ll_recharge_weixin);
        ll_recharge_yinhangka = findView(R.id.ll_recharge_yinhangka);
        ll_recharge_jingdong = findView(R.id.ll_recharge_jingdong);
        ll_recharge_wangyin = findView(R.id.ll_recharge_wangyin);
        ll_recharge_kuaijie = findView(R.id.ll_recharge_kuaijie);
        ll_recharge_xianxia = findView(R.id.ll_recharge_xianxia);
        ll_recharge_saoma = findView(R.id.ll_recharge_saoma);
        ll_recharge_upload_picture = findView(R.id.ll_recharge_upload_picture);
        cb_charge_zhifubao = findView(R.id.cb_charge_zhifubao);
        cb_charge_weixin = findView(R.id.cb_charge_weixin);
        cb_charge_yinhangka = findView(R.id.cb_charge_yinhangka);
        cb_charge_jingdong = findView(R.id.cb_charge_jingdong);
        cb_charge_wangyin = findView(R.id.cb_charge_wangyin);
        cb_charge_kuaijie = findView(R.id.cb_charge_kuaijie);
        cb_charge_xianxia = findView(R.id.cb_charge_xianxia);
        tv_recharge_sure = findView(R.id.tv_recharge_sure);
        et_recharge_input_money = findView(R.id.et_recharge_input_money);

        cb_charge_kuaijie.setChecked(true);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);

        ll_recharge_zhifubao.setOnClickListener(this);
        ll_recharge_weixin.setOnClickListener(this);
        ll_recharge_yinhangka.setOnClickListener(this);
        ll_recharge_jingdong.setOnClickListener(this);
        ll_recharge_wangyin.setOnClickListener(this);
        ll_recharge_kuaijie.setOnClickListener(this);
        ll_recharge_xianxia.setOnClickListener(this);
        ll_recharge_saoma.setOnClickListener(this);
        ll_recharge_upload_picture.setOnClickListener(this);
        cb_charge_zhifubao.setOnClickListener(this);
        cb_charge_weixin.setOnClickListener(this);
        cb_charge_yinhangka.setOnClickListener(this);
        cb_charge_jingdong.setOnClickListener(this);
        cb_charge_wangyin.setOnClickListener(this);
        cb_charge_kuaijie.setOnClickListener(this);
        cb_charge_xianxia.setOnClickListener(this);
        tv_recharge_sure.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.ll_recharge_zhifubao:
            case R.id.cb_charge_zhifubao:
                cb_charge_zhifubao.setChecked(true);
                cb_charge_weixin.setChecked(false);
                cb_charge_yinhangka.setChecked(false);
                cb_charge_jingdong.setChecked(false);
                cb_charge_wangyin.setChecked(false);
                cb_charge_kuaijie.setChecked(false);
                cb_charge_xianxia.setChecked(false);
                break;
            case R.id.ll_recharge_weixin:
            case R.id.cb_charge_weixin:
                cb_charge_zhifubao.setChecked(false);
                cb_charge_weixin.setChecked(true);
                cb_charge_yinhangka.setChecked(false);
                cb_charge_jingdong.setChecked(false);
                cb_charge_wangyin.setChecked(false);
                cb_charge_kuaijie.setChecked(false);
                cb_charge_xianxia.setChecked(false);
                break;
            case R.id.ll_recharge_yinhangka:
            case R.id.cb_charge_yinhangka:
                cb_charge_zhifubao.setChecked(false);
                cb_charge_weixin.setChecked(false);
                cb_charge_yinhangka.setChecked(true);
                cb_charge_jingdong.setChecked(false);
                cb_charge_wangyin.setChecked(false);
                cb_charge_kuaijie.setChecked(false);
                cb_charge_xianxia.setChecked(false);
                break;
            case R.id.ll_recharge_jingdong:
            case R.id.cb_charge_jingdong:
                cb_charge_zhifubao.setChecked(false);
                cb_charge_weixin.setChecked(false);
                cb_charge_yinhangka.setChecked(false);
                cb_charge_jingdong.setChecked(true);
                cb_charge_wangyin.setChecked(false);
                cb_charge_kuaijie.setChecked(false);
                cb_charge_xianxia.setChecked(false);
                break;
            case R.id.ll_recharge_wangyin:
            case R.id.cb_charge_wangyin:
                cb_charge_zhifubao.setChecked(false);
                cb_charge_weixin.setChecked(false);
                cb_charge_yinhangka.setChecked(false);
                cb_charge_jingdong.setChecked(false);
                cb_charge_wangyin.setChecked(true);
                cb_charge_kuaijie.setChecked(false);
                cb_charge_xianxia.setChecked(false);
                break;
            case R.id.ll_recharge_kuaijie:
            case R.id.cb_charge_kuaijie:
                cb_charge_zhifubao.setChecked(false);
                cb_charge_weixin.setChecked(false);
                cb_charge_yinhangka.setChecked(false);
                cb_charge_jingdong.setChecked(false);
                cb_charge_wangyin.setChecked(false);
                cb_charge_kuaijie.setChecked(true);
                cb_charge_xianxia.setChecked(false);
                break;
            case R.id.ll_recharge_xianxia:
            case R.id.cb_charge_xianxia:
                cb_charge_zhifubao.setChecked(false);
                cb_charge_weixin.setChecked(false);
                cb_charge_yinhangka.setChecked(false);
                cb_charge_jingdong.setChecked(false);
                cb_charge_wangyin.setChecked(false);
                cb_charge_kuaijie.setChecked(false);
                cb_charge_xianxia.setChecked(true);
                break;
            case R.id.ll_recharge_saoma:
                Intent intent1 = new Intent(RechargeActivity.this, PayQcodeActivity.class);
                intent1.putExtra("show_type", "qcode");
                startActivity(intent1);
                break;
            case R.id.ll_recharge_upload_picture:
                Intent intent2 = new Intent(RechargeActivity.this, PayQcodeActivity.class);
                intent2.putExtra("show_type", "upload");
                startActivity(intent2);
                break;
            case R.id.tv_recharge_sure:
                String moneyStr = et_recharge_input_money.getText().toString();
                if (StringUtil.isSpace(moneyStr)){
                    toast("请输入充值金额");
                    break;
                }

                tv_recharge_sure.setClickable(false);

                if (cb_charge_kuaijie.isChecked()){
                    appPay(App.token, moneyStr, "0");
                }else if(cb_charge_wangyin.isChecked()) {
                    appPay(App.token, moneyStr, "1");
                }else if (cb_charge_jingdong.isChecked()) {
                    appPay(App.token, moneyStr, "2");
                }else if (cb_charge_zhifubao.isChecked()) {
                    appPay(App.token, moneyStr, "3");
                }else {
                    toast("功能尚未开放");
                    tv_recharge_sure.setClickable(true);
                }

                break;
        }
    }


    /**
     * App27 > 金额充值
     */
    private void appPay(final String token, final String money, final String rechargeType) {
        if (NetUtil.isNetWorking(RechargeActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.appPayData(token, money, rechargeType, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            tv_recharge_sure.setClickable(true);

                            Bean.FastPayResultAll data = new Gson().fromJson(result, Bean.FastPayResultAll.class);
                            if (data.status == 1){
                                if (cb_charge_kuaijie.isChecked()||cb_charge_wangyin.isChecked()) {
                                    Intent intent = new Intent(RechargeActivity.this, WebShowActivity.class);
                                    intent.putExtra("url", data.url);
                                    startActivity(intent);
                                }else if (cb_charge_jingdong.isChecked()){
                                    Intent intent = new Intent(RechargeActivity.this, PayQcodeActivity.class);
                                    intent.putExtra("show_type", "jd_qcode");
                                    intent.putExtra("img_url", data.url);
                                    startActivity(intent);
                                }else if (cb_charge_zhifubao.isChecked()){
                                    Intent intent = new Intent(RechargeActivity.this, PayQcodeActivity.class);
                                    intent.putExtra("show_type", "alipay_qcode");
                                    intent.putExtra("img_url", data.url);
                                    startActivity(intent);
                                }
                            }else {
                                toast("获取失败，请重新提交订单");
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
