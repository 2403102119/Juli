package com.tcckj.juli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
 * 我的支付宝/微信
 */
public class MyAlipayActivity extends BaseActivity {
    TextView tv_my_pay_name,tv_my_pay_change,tv_my_pay_show;
    EditText et_my_pay_input;

    private int isAli = 0;       //0:我的支付宝    1:我的微信    2：绑定微信

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_my_alipay);

        title.setText("我的支付宝");

        tv_my_pay_name = findView(R.id.tv_my_pay_name);
        tv_my_pay_change = findView(R.id.tv_my_pay_change);
        tv_my_pay_show = findView(R.id.tv_my_pay_show);

        et_my_pay_input = findView(R.id.et_my_pay_input);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        tv_my_pay_change.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            if ("wx".equals(getIntent().getStringExtra("bind_type"))){
                isAli = 1;
                tv_my_pay_name.setText("微信");
                tv_my_pay_change.setText("更换微信");
                title.setText("我的微信");
                tv_my_pay_show.setText(App.personalInfo.wechat);
            }else if ("bind_wx".equals(getIntent().getStringExtra("bind_type"))){
                isAli = 2;
                tv_my_pay_name.setText("微信");
                tv_my_pay_change.setText("确定绑定");
                title.setText("绑定微信");
                tv_my_pay_show.setVisibility(View.GONE);
                et_my_pay_input.setVisibility(View.VISIBLE);
            }else {
                tv_my_pay_show.setText(App.personalInfo.alipay);
            }
        }
    }

    private static final int CHANGE_INFO = 11111;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_my_pay_change:
                switch (isAli){
                    case 0:             //更换支付宝
                        Intent intent1 = new Intent(MyAlipayActivity.this, BindAlipayActivity.class);
                        intent1.putExtra("bind_type","change_ali");
                        startActivityForResult(intent1, CHANGE_INFO);
                        break;
                    case 1:             //更换微信
                        Intent intent2 = new Intent(MyAlipayActivity.this, BindAlipayActivity.class);
                        intent2.putExtra("bind_type","change_wx");
                        startActivityForResult(intent2, CHANGE_INFO);
                        break;
                    case 2:             //绑定微信
                        String wxStr = et_my_pay_input.getText().toString().trim();
                        if (StringUtil.isSpace(wxStr)){
                            toast("请输入您的微信号");
                            break;
                        }
                        editWechat(App.token, wxStr);
                        break;
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_INFO && resultCode == 22222){
            finish();
        }
    }



    /**
     * App13 > 绑定微信
     */
    private void editWechat(final String token, final String wechat) {
        if (NetUtil.isNetWorking(MyAlipayActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.editWechatData(token, wechat, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                toast(data.message);
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
