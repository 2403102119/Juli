package com.tcckj.juli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tcckj.juli.App;
import com.tcckj.juli.MainActivity;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.util.StringUtil;

/**
 * 算力攻略界面
 */
public class CalculatePowerStrategyActivity extends BaseActivity {
    TextView tv_raise_earnings_quickly,tv_calculate_show;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_calculate_power_strategy);

        title.setText("算力攻略");
        imgShare.setVisibility(View.VISIBLE);
        imgShare.setImageResource(R.mipmap.time_icon);

        tv_raise_earnings_quickly = findView(R.id.tv_raise_earnings_quickly);
        tv_calculate_show = findView(R.id.tv_calculate_show);

        if (App.islogin) {
            tv_calculate_show.setText(StringUtil.doubleToString(App.personalInfo.forceVlue));
        }else {
            startActivity(new Intent(CalculatePowerStrategyActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        imgShare.setOnClickListener(this);

        tv_raise_earnings_quickly.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.imgShare:
                startActivity(new Intent(CalculatePowerStrategyActivity.this, CalculatePowerRecordActivity.class));
                break;
            case R.id.tv_raise_earnings_quickly:
                Intent intent = new Intent(CalculatePowerStrategyActivity.this, MainActivity.class);
                intent.putExtra("stepTo", 1);
                startActivity(intent);
                finish();
                break;
        }
    }
}
