package com.tcckj.juli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcckj.juli.App;
import com.tcckj.juli.MainActivity;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.DESUtils;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.SPUtil;
import com.tcckj.juli.util.StringUtil;

import okhttp3.Call;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

    TextView login; //登录
    TextView tv_login_to_register;      //注册
    RelativeLayout rl_login_to_register;//注册
    TextView tv_login_verification;     //验证码登录
    TextView tv_login_forget;             //忘记密码
    ImageView img_login_show_password;  //显示密码（可见/不可见）

    CheckBox cb_login_remember;

    EditText et_login_phone,et_login_password;

    private String login_phone = ""; //手机号
    private String login_password = ""; //密码
    private static final String TAG = "获取.成功";

    private boolean isShowPassword = false;

    private String activityName = "";

    /*
    给用户设置别名
     */
    private static final int MSG_SET_ALIAS = 1001;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_login_number);
        top.setVisibility(View.GONE);

        login = findView(R.id.login);
        tv_login_to_register = findView(R.id.tv_login_to_register);
        tv_login_verification = findView(R.id.tv_login_verification);
        tv_login_forget = findView(R.id.tv_login_forget);
        rl_login_to_register = findView(R.id.rl_login_to_register);

        et_login_phone = findView(R.id.et_login_phone);
        et_login_password = findView(R.id.et_login_password);
        img_login_show_password = findView(R.id.img_login_show_password);
        cb_login_remember = findView(R.id.cb_login_remember);

        et_login_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);//设置密码不可见

        if (App.islogin){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        if (!App.isFirstLogin){
//            String account[] = DESUtils.readPassword(LoginActivity.this);
            et_login_phone.setText(App.lastAccount);

//            et_login_phone.setText(account[0]);
            if (App.isRememberPassword){
                cb_login_remember.setChecked(true);
//                et_login_password.setText(account[1]);
                et_login_password.setText(App.lastPassword);
            }
        }
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        login.setOnClickListener(this);
        rl_login_to_register.setOnClickListener(this);
        tv_login_verification.setOnClickListener(this);
        tv_login_forget.setOnClickListener(this);
        img_login_show_password.setOnClickListener(this);

        et_login_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isSpace(s+"")||!StringUtil.isPhone(s + "")){
                    et_login_password.setText("");
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            activityName = getIntent().getStringExtra("activity");

            if ("main".equals(getIntent().getStringExtra("activity"))){

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.login:
                login_phone = et_login_phone.getText().toString().trim();
                login_password = et_login_password.getText().toString().trim();
                if (StringUtil.isSpace(login_phone)) {
                    toast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhone(login_phone)) {
                    toast("请输入正确的手机号");
                    return;
                }
                if (StringUtil.isSpace(login_password)) {
                    toast("请输入密码");
                    return;
                }
                if (StringUtil.isGB2312(login_password)){
                    toast("密码中不能包含汉字");
                    return;
                }
                userLogin(login_password,login_phone);
                break;
            case R.id.rl_login_to_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));          //注册
                break;
            case R.id.tv_login_verification:
                startActivity(new Intent(LoginActivity.this, VerificationLoginActivity.class)); //验证码登录
                break;
            case R.id.img_login_show_password:
                String passwordStr = et_login_password.getText().toString();
                if (isShowPassword){
                    isShowPassword = false;
                    et_login_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);//设置密码不可见，如果只设置TYPE_TEXT_VARIATION_PASSWORD则无效
                    img_login_show_password.setImageResource(R.mipmap.eye_icon);
                }else {
                    isShowPassword = true;
                    et_login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);         //设置密码可见
                    img_login_show_password.setImageResource(R.mipmap.eye_open_icon);
                }
                et_login_password.setSelection(passwordStr.length());
                break;
            case R.id.tv_login_forget:
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                intent.putExtra("password_type", "login");
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ("main".equals(activityName)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    /**
     * App03 > 用户登录
     */
    private void userLogin(final String password, final String account) {
        if (NetUtil.isNetWorking(LoginActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.userLoginData(password, account, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.PersonalInfoAll data = new Gson().fromJson(result, Bean.PersonalInfoAll.class);
                            if (data.status == 1){
                                toast(data.message);

                                if (App.isFirstLogin){
                                    SPUtil.saveData(LoginActivity.this, "isFirstLogin", false);

                                    App.isFirstLogin = false;
                                }

//                                DESUtils.savePassword(LoginActivity.this, account, password, true);
                                SPUtil.saveData(LoginActivity.this, "lastAccount", account);
                                App.lastAccount = account;

                                if (cb_login_remember.isChecked()) {
                                    SPUtil.saveData(LoginActivity.this, "lastPassword", password);
                                    SPUtil.saveData(LoginActivity.this, "isRememberPassword", true);

                                    App.lastPassword = password;
                                    App.isRememberPassword = true;
                                }else {
                                    SPUtil.saveData(LoginActivity.this, "lastPassword", "");
                                    SPUtil.saveData(LoginActivity.this, "isRememberPassword", false);

                                    App.lastPassword = "";
                                    App.isRememberPassword = false;
                                }

                                SPUtil.saveBean2Sp(LoginActivity.this, data.model, "personal","personalInfo");
                                SPUtil.saveData(LoginActivity.this, "islogin", true);
                                SPUtil.saveData(LoginActivity.this, "token", data.model.token);

                                App.personalInfo = data.model;
                                App.islogin = true;
                                App.token = data.model.token;

                                App.finishAllActivity();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

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