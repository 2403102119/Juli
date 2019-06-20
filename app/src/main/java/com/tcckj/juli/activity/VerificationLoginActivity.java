package com.tcckj.juli.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcckj.juli.App;
import com.tcckj.juli.MainActivity;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.CodeUtils;
import com.tcckj.juli.util.DialogTools;
import com.tcckj.juli.util.DialogUtils;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.SPUtil;
import com.tcckj.juli.util.StringUtil;

import okhttp3.Call;

/**
 * 验证码登录
 */
public class VerificationLoginActivity extends BaseActivity {
    TextView tv_login_password,tv_register_verification_show,tv_verification_login;
    EditText et_verification_login_phone,et_verification_code;

    CountDownTimer timer;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_verification_login);

        top.setVisibility(View.GONE);

        tv_login_password = findView(R.id.tv_login_password);
        tv_register_verification_show = findView(R.id.tv_register_verification_show);
        tv_verification_login = findView(R.id.tv_verification_login);
        et_verification_login_phone = findView(R.id.et_verification_login_phone);
        et_verification_code = findView(R.id.et_verification_code);
    }

    @Override
    protected void initListener() {
        tv_login_password.setOnClickListener(this);
        tv_register_verification_show.setOnClickListener(this);
        tv_verification_login.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login_password:
                startActivity(new Intent(VerificationLoginActivity.this, LoginActivity.class));
                break;
            case R.id.tv_register_verification_show:
                final String phoneNumStr = et_verification_login_phone.getText().toString().trim();
                if (StringUtil.isSpace(phoneNumStr)){
                    toast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhone(phoneNumStr)){
                    toast("请输入正确的手机号");
                    break;
                }

                DialogTools dialogUtils = new DialogTools(VerificationLoginActivity.this);
                Dialog dialog = dialogUtils.vetificationDialog();
                dialogUtils.setIsVerification(new DialogTools.IsVerification() {
                    @Override
                    public void isVerification(boolean b) {
                        if (b){
                            sendMsg(phoneNumStr);
                            if (null != timer){
                                timer.cancel();
                            }
                            timer = new CountDownTimer(60000,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    tv_register_verification_show.setText(millisUntilFinished/1000+"");
                                    tv_register_verification_show.setClickable(false);
                                }

                                @Override
                                public void onFinish() {
                                    tv_register_verification_show.setClickable(true);
                                    tv_register_verification_show.setText("获取验证码");
                                }
                            }.start();
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.tv_verification_login:
                String phoneStr = et_verification_login_phone.getText().toString().trim();
                String codeStr = et_verification_code.getText().toString().trim();
                if (StringUtil.isSpace(phoneStr)){
                    toast("请输入手机号");
                    break;
                }
                if (StringUtil.isSpace(codeStr)){
                    toast("请输入验证码");
                    break;
                }
                if (!StringUtil.isPhone(phoneStr)){
                    toast("请输入正确的手机号");
                    break;
                }

                msgLogin(codeStr, phoneStr);

                break;
        }
    }


    /**
     * App01 > 发送手机验证码
     */
    private void sendMsg(final String account) {
        if (NetUtil.isNetWorking(VerificationLoginActivity.this)){
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
     * App04 > 快捷登录
     */
    private void msgLogin(final String code, final String account) {
        if (NetUtil.isNetWorking(VerificationLoginActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.msgLoginData(code, account, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.PersonalInfoAll data = new Gson().fromJson(result, Bean.PersonalInfoAll.class);
                            if (data.status == 1){
                                toast(data.message);

                                SPUtil.saveBean2Sp(VerificationLoginActivity.this, data.model, "personal","personalInfo");
                                SPUtil.saveData(VerificationLoginActivity.this, "islogin", true);
                                SPUtil.saveData(VerificationLoginActivity.this, "token", data.model.token);

                                App.personalInfo = data.model;
                                App.islogin = true;
                                App.token = data.model.token;

                                App.finishAllActivity();
                                startActivity(new Intent(VerificationLoginActivity.this, MainActivity.class));

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
