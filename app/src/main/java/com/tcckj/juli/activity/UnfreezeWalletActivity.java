package com.tcckj.juli.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;

/**
 * 解冻钱包界面
 */
public class UnfreezeWalletActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_unfreeze);

        title.setText("解冻钱包");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
