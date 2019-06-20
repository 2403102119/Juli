package com.tcckj.juli.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tcckj.juli.MainActivity;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;

public class StartActivity extends BaseActivity {

    private Handler handler = new Handler();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_start);

        top.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        },2000);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
