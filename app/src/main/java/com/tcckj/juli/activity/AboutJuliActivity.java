package com.tcckj.juli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
 * 关于聚力界面
 */
public class AboutJuliActivity extends BaseActivity {
    TextView tv_about_juli;
    ImageView img_coin;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_about_juli);

        title.setText("关于");

        tv_about_juli = findView(R.id.tv_about_juli);
        img_coin = findView(R.id.img_coin);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (App.islogin) {
            applyPort(App.token);
        }else {
            startActivity(new Intent(AboutJuliActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }


    /**
     * App44 > 获取关于我们得信息
     */
    private void applyPort(final String token) {
        if (NetUtil.isNetWorking(AboutJuliActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getSetUpData(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.AboutJuli data = new Gson().fromJson(result, Bean.AboutJuli.class);
                            if (data.status == 1){
                                tv_about_juli.setText(data.photo);
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
