package com.tcckj.juli.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.DialogTools;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.StringUtil;

import okhttp3.Call;

/**
 * 矿机详情界面
 */
public class OreMachineDetailsActivity extends BaseActivity {
    ImageView img_ore_machine;
    TextView tv_ore_machine_percent,tv_commodity_buy_average,tv_commodity_yield_average,
            tv_commodity_surplus_number, tv_commodity_product_feature,tv_commodity_detail_content,
            tv_ore_machine_buy_now,tv_dialog_change_name_left,tv_dialog_change_name_right,
            tv_dialog_title,tv_dialog_content,tv_commodity_product_type,tv_commodity_user_level;
    EditText et_dialog_input_name;

    Dialog dialog,inputDialog;

    private double minMoney,maxMoney;
    private String oid;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_ore_machine_details);

        title.setText("迷你矿机");

        img_ore_machine = findView(R.id.img_ore_machine);
        tv_ore_machine_percent = findView(R.id.tv_ore_machine_percent);
        tv_commodity_buy_average = findView(R.id.tv_commodity_buy_average);
        tv_commodity_yield_average = findView(R.id.tv_commodity_yield_average);
        tv_commodity_surplus_number = findView(R.id.tv_commodity_surplus_number);
        tv_commodity_product_feature = findView(R.id.tv_commodity_product_feature);
        tv_commodity_detail_content = findView(R.id.tv_commodity_detail_content);
        tv_ore_machine_buy_now = findView(R.id.tv_ore_machine_buy_now);
        tv_commodity_product_type = findView(R.id.tv_commodity_product_type);
        tv_commodity_user_level = findView(R.id.tv_commodity_user_level);

        initDialog();
        initInputDialog();

        mySmart.setEnableRefresh(true);
    }

    @Override
    protected void initListener() {
        img_ore_machine.setOnClickListener(this);
        tv_ore_machine_buy_now.setOnClickListener(this);
        tv_dialog_change_name_left.setOnClickListener(this);
        tv_dialog_change_name_right.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            oid = getIntent().getStringExtra("oid");
            title.setText(getIntent().getStringExtra("name"));

            getCommodity(App.token, oid);

            mySmart.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshLayout) {
                    getCommodity(App.token, oid);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.img_ore_machine:
                dialog.show();
                break;
            case R.id.tv_ore_machine_buy_now:
                initInputDialog();
                inputDialog.show();
                break;
            case R.id.tv_dialog_change_name_left:
                inputDialog.dismiss();
                break;
            case R.id.tv_dialog_change_name_right:
                String moneyStr = et_dialog_input_name.getText().toString().trim();
                if (StringUtil.isSpace(moneyStr)){
                    toast("请输入购买金额");
                }else {
                    double money = Double.valueOf(moneyStr);
                    if (money < minMoney || money > maxMoney){
                        toast("购买金额应在购买范围内");
                    }else {
                        getPurchase(App.token, money+"", oid);
                        inputDialog.dismiss();
                    }
                }
                break;
        }
    }

    private void initDialog() {
        dialog = new Dialog(this,R.style.Dialog);
        View view = getLayoutInflater().inflate(R.layout.dialog_yield_rate_detail, null);
        TextView tv_yield_rate_cancel = (TextView) view.findViewById(R.id.tv_yield_rate_cancel);
        tv_yield_rate_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(view);
    }

    private void initInputDialog() {
        inputDialog = new Dialog(OreMachineDetailsActivity.this, R.style.Dialog);
        View view = LayoutInflater.from(OreMachineDetailsActivity.this).inflate(R.layout.dialog_mine_change_name, null);
        tv_dialog_change_name_left = (TextView) view.findViewById(R.id.tv_dialog_change_name_left);
        tv_dialog_change_name_right = (TextView) view.findViewById(R.id.tv_dialog_change_name_right);
        tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        tv_dialog_content = (TextView) view.findViewById(R.id.tv_dialog_content);
        et_dialog_input_name = (EditText) view.findViewById(R.id.et_dialog_input_name);
        tv_dialog_change_name_left.setOnClickListener(this);
        tv_dialog_change_name_right.setOnClickListener(this);

        tv_dialog_title.setText("购买金额");
        et_dialog_input_name.setHint("请输入购买金额");

        et_dialog_input_name.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        inputDialog.setCanceledOnTouchOutside(false);
        inputDialog.setContentView(view);
    }

    /**
     * App18 > 获取商品详情
     */
    private void getCommodity(final String token, final String oid) {
        if (NetUtil.isNetWorking(OreMachineDetailsActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getCommodityData(token, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.CommodityDetailAll data = new Gson().fromJson(result, Bean.CommodityDetailAll.class);
                            if (data.status == 1){
                                tv_ore_machine_percent.setText("+"+StringUtil.doubleToString(data.model.averageRate) + "%");
                                tv_commodity_buy_average.setText(StringUtil.doubleToString(data.model.minMoney) + "~" + StringUtil.doubleToString(data.model.maxMoney));
                                tv_commodity_yield_average.setText(StringUtil.doubleToString(data.model.receptionMinRate) + "%~" + StringUtil.doubleToString(data.model.receptionMaxRate) + "%");
                                tv_commodity_product_feature.setText(data.model.characteristic);
                                tv_commodity_detail_content.setText(data.model.content);
                                tv_commodity_surplus_number.setText(data.model.number + "");
                                tv_commodity_user_level.setText(data.model.member);

                                if ("0".equals(data.model.type)){
                                    tv_commodity_product_type.setText("常规模式");
                                }else if ("1".equals(data.model.type)){
                                    tv_commodity_product_type.setText("余额宝模式");
                                }

                                minMoney = data.model.minMoney;
                                maxMoney = data.model.maxMoney;
                            }
                            finishRefresh();
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
     * App21 > 购买合约
     */
    private void getPurchase(final String token, final String money, final String oid) {
        if (NetUtil.isNetWorking(OreMachineDetailsActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getPurchaseData(token, money, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                toast(data.message);
                                getCommodity(App.token, oid);
                            }else {
                                toast(data.message);
                            }
                            finishRefresh();
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
