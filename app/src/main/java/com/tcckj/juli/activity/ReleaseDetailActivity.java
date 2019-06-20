package com.tcckj.juli.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;

/**
 * 释放详情界面
 */
public class ReleaseDetailActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_release_detail);

        title.setText("释放详情");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
