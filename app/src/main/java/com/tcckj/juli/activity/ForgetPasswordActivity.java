package com.tcckj.juli.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
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

/*
忘记密码界面
 */
public class ForgetPasswordActivity extends BaseActivity {
    private EditText et_user_phone,et_new_password,et_new_password_again,et_verification_code;
    TextView tv_get_verification, tv_my_password_change;
    private CountDownTimer timer;
    private boolean isLogin = true;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_forget_password);

        et_user_phone = findView(R.id.et_user_phone);
        et_new_password = findView(R.id.et_new_password);
        et_new_password_again = findView(R.id.et_new_password_again);
        et_verification_code = findView(R.id.et_verification_code);
        tv_get_verification = findView(R.id.tv_get_verification);
        tv_my_password_change = findView(R.id.tv_my_password_change);
    }

    @Override
    protected void initListener() {
        tv_get_verification.setOnClickListener(this);
        tv_my_password_change.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()) {
            if ("login".equals(getIntent().getStringExtra("password_type"))){
                isLogin = true;
                title.setText("重置登录密码");
            }
            if ("pay".equals(getIntent().getStringExtra("password_type"))){
                isLogin = false;
                title.setText("重置支付密码");
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_get_verification:
                String phoneStr = et_user_phone.getText().toString().trim();

                if (StringUtil.isSpace(phoneStr)){
                    toast("请输入手机号");
                    break;
                }
                if (!StringUtil.isPhone(phoneStr)){
                    toast("请输入正确的手机号");
                    break;
                }

                sendMsg(phoneStr);
                if (null != timer){
                    timer.cancel();
                }
                timer = new CountDownTimer(60000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tv_get_verification.setText(millisUntilFinished/1000+"");
                        tv_get_verification.setClickable(false);
                    }

                    @Override
                    public void onFinish() {
                        tv_get_verification.setClickable(true);
                        tv_get_verification.setText("获取验证码");
                    }
                }.start();
                break;
            case R.id.tv_my_password_change:
                String phone = et_user_phone.getText().toString().trim();
                String password = et_new_password.getText().toString().trim();
                String passwordAgain = et_new_password_again.getText().toString().trim();
                String codeStr = et_verification_code.getText().toString().trim();

                if (StringUtil.isSpace(phone)){
                    toast("请输入手机号");
                    break;
                }
                if (StringUtil.isSpace(password)){
                    toast("请输入密码");
                    break;
                }
                if (StringUtil.isSpace(passwordAgain)){
                    toast("请再次输入密码");
                    break;
                }
                if (StringUtil.isSpace(codeStr)){
                    toast("请输入验证码");
                    break;
                }
                if (!password.equals(passwordAgain)){
                    toast("两次输入的密码不相同");
                    break;
                }
                if (!StringUtil.isPhone(phone)){
                    toast("请输入正确的手机号");
                    break;
                }
                if (isLogin) {
                    changeLoginPassword(codeStr, phone, password, passwordAgain);
                }else {
                    changePayPassword(codeStr, phone, password, passwordAgain);
                }
                break;
        }
    }

    /**
     * App01 > 发送手机验证码
     */
    private void sendMsg(final String account) {
        if (NetUtil.isNetWorking(ForgetPasswordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.sendMsgData(account, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.SendMsgResult data = new Gson().fromJson(result, Bean.SendMsgResult.class);
                            if (data.status == 1){

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
     * App45 > 验证码修改登录密码
     */
    private void changeLoginPassword(final String code, final String account, final String newPasswordOne, final String newPasswordTwo) {
        if (NetUtil.isNetWorking(ForgetPasswordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.changeLoginPasswordData(code, account, newPasswordOne, newPasswordTwo, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                et_user_phone.setText("");
                                et_new_password.setText("");
                                et_new_password_again.setText("");
                                et_verification_code.setText("");
                                toast(data.message);
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
     * App46 > 验证码修改支付密码
     */
    private void changePayPassword(final String code, final String account, final String newPasswordOne, final String newPasswordTwo) {
        if (NetUtil.isNetWorking(ForgetPasswordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.changePayPasswordData(code, account, newPasswordOne, newPasswordTwo, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                et_user_phone.setText("");
                                et_new_password.setText("");
                                et_new_password_again.setText("");
                                et_verification_code.setText("");
                                toast(data.message);
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
