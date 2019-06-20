package com.tcckj.juli.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcckj.juli.App;
import com.tcckj.juli.MainActivity;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.DialogTools;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.SPUtil;
import com.tcckj.juli.util.StringUtil;

import okhttp3.Call;

/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity {
    TextView tv_register_verification_show,register,tv_read_agreement;
    EditText et_register_phone,et_register_invitation_code,et_register_password,et_register_verification;

    CheckBox cb_register_agreement;

    CountDownTimer timer;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_register);

        top.setVisibility(View.GONE);
        tv_register_verification_show = findView(R.id.tv_register_verification_show);
        tv_read_agreement = findView(R.id.tv_read_agreement);
        register = findView(R.id.register);

        cb_register_agreement = findView(R.id.cb_register_agreement);
        et_register_phone = findView(R.id.et_register_phone);
        et_register_invitation_code = findView(R.id.et_register_invitation_code);
        et_register_password = findView(R.id.et_register_password);
        et_register_verification = findView(R.id.et_register_verification);

        cb_register_agreement.setChecked(true);
    }

    @Override
    protected void initListener() {
        tv_register_verification_show.setOnClickListener(this);
        register.setOnClickListener(this);
        tv_read_agreement.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_read_agreement:
                getAgreement();
                break;
            case R.id.tv_register_verification_show:
                final String phoneStr = et_register_phone.getText().toString().trim();
                if (StringUtil.isSpace(phoneStr)){
                    toast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhone(phoneStr)){
                    toast("请输入正确的手机号");
                    break;
                }
                DialogTools dialogUtils = new DialogTools(RegisterActivity.this);
                Dialog dialog = dialogUtils.vetificationDialog();
                dialogUtils.setIsVerification(new DialogTools.IsVerification() {
                    @Override
                    public void isVerification(boolean b) {
                        if (b){
                            sendMsg(phoneStr);                  //获取验证码
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
            case R.id.register:
                String phoneNumStr = et_register_phone.getText().toString().trim();
                String invitationCodeStr = et_register_invitation_code.getText().toString().trim();
                String passwordStr = et_register_password.getText().toString().trim();
                String verificationStr = et_register_verification.getText().toString().trim();

                if (StringUtil.isSpace(phoneNumStr)){
                    toast("请输入手机号");
                    break;
                }
                if (StringUtil.isSpace(passwordStr)){
                    toast("请输入密码");
                    break;
                }
                if (StringUtil.isSpace(verificationStr)){
                    toast("请输入验证码");
                    break;
                }
                if (!StringUtil.isPhone(phoneNumStr)){
                    toast("请输入正确的手机号");
                    break;
                }
                if (passwordStr.length() < 6 || passwordStr.length() > 20){
                    toast("密码长度应为6~20位");
                    break;
                }
                if (!cb_register_agreement.isChecked()){
                    toast("阅读并同意用户手册后方可注册");
                    break;
                }

                userRegister(verificationStr,invitationCodeStr,passwordStr,phoneNumStr);

                break;
        }
    }


    /**
     * App01 > 发送手机验证码
     */
    private void sendMsg(final String account) {
        if (NetUtil.isNetWorking(RegisterActivity.this)){
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
     * App02 > 用户注册
     */
    private void userRegister(final String code, final String invitationCode, final String password, final String account) {
        if (NetUtil.isNetWorking(RegisterActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.userRegisterData(code, invitationCode, password, account, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                toast("注册成功");

                                SPUtil.saveData(RegisterActivity.this, "islogin", true);
                                App.islogin = true;

                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
     * App39 > 获取协议
     */
    private void getAgreement() {
        if (NetUtil.isNetWorking(RegisterActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getAgreementData(new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.FastPayResultAll data = new Gson().fromJson(result, Bean.FastPayResultAll.class);
                            if (data.status == 1){
                                Intent intent = new Intent(RegisterActivity.this, WebShowActivity.class);
                                intent.putExtra("url", data.url);
                                startActivity(intent);
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
