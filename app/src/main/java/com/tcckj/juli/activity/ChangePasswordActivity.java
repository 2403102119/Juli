package com.tcckj.juli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
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
 * 修改登录/支付密码
 */
public class ChangePasswordActivity extends BaseActivity {
    LinearLayout ll_change_password_verification,ll_original_password;
    TextView tv_my_password_change,tv_get_verification;
    EditText et_original_password,et_new_password,et_new_password_again,et_verification_code;

    int password_type = 0;  //0:登录密码        1：支付密码
    private CountDownTimer timer;
    private boolean isShowOldPassword;          //是否显示原密码

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_change_password);

        ll_change_password_verification = findView(R.id.ll_change_password_verification);
        ll_original_password = findView(R.id.ll_original_password);
        tv_my_password_change = findView(R.id.tv_my_password_change);
        tv_get_verification = findView(R.id.tv_get_verification);
        et_original_password = findView(R.id.et_original_password);
        et_new_password = findView(R.id.et_new_password);
        et_new_password_again = findView(R.id.et_new_password_again);
        et_verification_code = findView(R.id.et_verification_code);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        tv_my_password_change.setOnClickListener(this);
        tv_get_verification.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            if ("login".equals(getIntent().getStringExtra("password_type"))){
                title.setText("修改登录密码");
                ll_change_password_verification.setVisibility(View.GONE);
                ll_original_password.setVisibility(View.VISIBLE );
                password_type = 0;
            }else if ("pay".equals(getIntent().getStringExtra("password_type"))){
                title.setText("支付密码");
                password_type = 1;
                getPayPassword(App.token);
                rightTxt.setVisibility(View.VISIBLE);
                rightTxt.setText("忘记密码");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_my_password_change:
                String original_password = et_original_password.getText().toString().trim();
                String new_password = et_new_password.getText().toString().trim();
                String new_password_again = et_new_password_again.getText().toString().trim();
                String verification_code = et_verification_code.getText().toString().trim();
                if (isShowOldPassword) {
                    if (StringUtil.isSpace(original_password)) {
                        toast("请输入原密码");
                        break;
                    }
                }
                if (StringUtil.isSpace(new_password)){
                    toast("请输入新密码");
                    break;
                }
                if (!new_password.equals(new_password_again)){
                    toast("两次输入的新密码不相同");
                    break;
                }
                switch (password_type){
                    case 0:
                        editPassword(App.token, original_password, new_password, new_password_again);
                        break;
                    case 1:
                        if (StringUtil.isSpace(verification_code)){
                            toast("请输入验证码");
                            break;
                        }
                        editPayPassword(App.token, original_password, new_password, new_password_again, verification_code);
                        break;
                }
                break;
            case R.id.tv_get_verification:
                sendMsg(App.personalInfo.phone);
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
            case R.id.rightTxt:
                Intent intent = new Intent(ChangePasswordActivity.this, ForgetPasswordActivity.class);
                intent.putExtra("password_type", "pay");
                startActivity(intent);
                break;
        }
    }


    /**
     * App23 > 用户修改密码
     */
    private void editPassword(final String token, final String oldPassword, final String newPasswordOne, final String newPasswordTwo) {
        if (NetUtil.isNetWorking(ChangePasswordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.editPasswordData(token, oldPassword, newPasswordOne, newPasswordTwo, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                toast(data.message);
                                et_original_password.setText("");
                                et_new_password.setText("");
                                et_new_password_again.setText("");
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
     * App25 > 用户修改支付密码
     */
    private void getPayPassword(final String token) {
        if (NetUtil.isNetWorking(ChangePasswordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getPayPasswordData(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.GetPayPassword data = new Gson().fromJson(result, Bean.GetPayPassword.class);
                            if (data.status == 1){
                                if (StringUtil.isSpace(data.payPassword)){
                                    ll_original_password.setVisibility(View.GONE);
                                    isShowOldPassword = false;
                                }else {
                                    ll_original_password.setVisibility(View.VISIBLE);
//                                    oldPassword = data.payPassword;
                                    isShowOldPassword = true;
                                }
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
     * App01 > 发送手机验证码
     */
    private void sendMsg(final String account) {
        if (NetUtil.isNetWorking(ChangePasswordActivity.this)){
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
     * App26 > 用户修改支付密码
     */
    private void editPayPassword(final String token, final String oldPassword, final String newPasswordOne, final String newPasswordTwo, final String code) {
        if (NetUtil.isNetWorking(ChangePasswordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.editPayPasswordData(token, oldPassword, newPasswordOne, newPasswordTwo, code, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                toast(data.message);
                                et_original_password.setText("");
                                et_new_password.setText("");
                                et_new_password_again.setText("");
                                et_verification_code.setText("");
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


}
