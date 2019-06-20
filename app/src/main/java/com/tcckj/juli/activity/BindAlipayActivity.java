package com.tcckj.juli.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.SPUtil;
import com.tcckj.juli.util.StringUtil;

import okhttp3.Call;

/**
 * 绑定支付宝界面
 */
public class BindAlipayActivity extends BaseActivity {
    RelativeLayout rl_my_pay_show1,rl_my_pay_show3;
    TextView tv_my_pay_sure,tv_my_pay_name,tv_bind_alipay_show,tv_bind_account_tip;
    EditText et_my_pay_input_accout,et_my_pay_input_name;

    private int activity_user = 0;  //0:绑定支付宝   1：更换支付宝     2：更换微信

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_bind_alipay);

        title.setText("绑定支付宝");
        rl_my_pay_show1 = findView(R.id.rl_my_pay_show1);
        rl_my_pay_show3 = findView(R.id.rl_my_pay_show3);

        et_my_pay_input_accout = findView(R.id.et_my_pay_input_accout);
        et_my_pay_input_name = findView(R.id.et_my_pay_input_name);

        tv_my_pay_sure = findView(R.id.tv_my_pay_sure);
        tv_my_pay_name = findView(R.id.tv_my_pay_name);
        tv_bind_alipay_show = findView(R.id.tv_bind_alipay_show);
        tv_bind_account_tip = findView(R.id.tv_bind_account_tip);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        tv_my_pay_sure.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            if ("change_ali".equals(getIntent().getStringExtra("bind_type"))){
                activity_user = 1;
                rl_my_pay_show1.setVisibility(View.VISIBLE);
                tv_bind_alipay_show.setText(App.personalInfo.alipay);
                title.setText("更换支付宝");
                tv_my_pay_sure.setText("确认更换");
            }else if ("change_wx".equals(getIntent().getStringExtra("bind_type"))){
                activity_user = 2;
                rl_my_pay_show1.setVisibility(View.VISIBLE);
                tv_bind_alipay_show.setText(App.personalInfo.wechat);
                rl_my_pay_show3.setVisibility(View.GONE);
                et_my_pay_input_accout.setHint(R.string.input_wx_accout);
                tv_bind_account_tip.setText("当前微信账号");
                title.setText("更换微信");
                tv_my_pay_name.setText("微信");
                tv_my_pay_sure.setText("确认更换");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_my_pay_sure:
                String accoutStr = et_my_pay_input_accout.getText().toString().trim();
                String nameStr = et_my_pay_input_name.getText().toString().trim();
                if (StringUtil.isSpace(accoutStr)){
                    toast("请输入账号");
                    break;
                }
                switch (activity_user){
                    case 0:
                        if (StringUtil.isSpace(nameStr)){
                            toast("请输入真实姓名");
                            break;
                        }
                        editAlipay(App.token, nameStr, accoutStr);
                        break;
                    case 1:
                        if (StringUtil.isSpace(nameStr)){
                            toast("请输入真实姓名");
                            break;
                        }
                        editAlipay(App.token, nameStr, accoutStr, 22222);
                        break;
                    case 2:
                        editWechat(App.token, accoutStr);
                        break;
                }
                break;
        }
    }



    /**
     * App12 > 绑定支付宝
     */
    private void editAlipay(final String token, final String realName, final String alipay) {
        if (NetUtil.isNetWorking(BindAlipayActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.editAlipayData(token, realName, alipay, new MApiResultCallback() {
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



    /**
     * App12 > 绑定支付宝(换绑)
     */
    private void editAlipay(final String token, final String realName, final String alipay, final int code) {
        if (NetUtil.isNetWorking(BindAlipayActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.editAlipayData(token, realName, alipay, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                setResult(code);
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



    /**
     * App13 > 绑定微信(换绑)
     */
    private void editWechat(final String token, final String wechat) {
        if (NetUtil.isNetWorking(BindAlipayActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.editWechatData(token, wechat, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                setResult(22222);
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
