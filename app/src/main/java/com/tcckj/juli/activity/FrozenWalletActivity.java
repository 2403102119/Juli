package com.tcckj.juli.activity;

import android.os.Bundle;

import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;

/**
 * 冻结钱包
 */
public class FrozenWalletActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_frozen_wallet);

        title.setText("冻结钱包");
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
    }
}
