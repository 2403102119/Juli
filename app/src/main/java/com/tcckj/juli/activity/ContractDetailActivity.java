package com.tcckj.juli.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.StringUtil;

import okhttp3.Call;

/**
 * 合约详情界面
 */
public class ContractDetailActivity extends BaseActivity {
    Dialog dialog,rateDialog;

    ImageView img_month_rate;
    TextView tv_release_principal,tv_contract_percent,tv_contract_detail_name,tv_contract_detail_date,
            tv_contract_detail_cost,tv_contract_detail_rate,tv_contract_detail_state,tv_dialog_content,
            tv_contract_detail_type;

    private String oid;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_contract_detail);

        title.setText("合约详情");
        img_month_rate = findView(R.id.img_month_rate);
        tv_release_principal = findView(R.id.tv_release_principal);
        tv_contract_percent = findView(R.id.tv_contract_percent);
        tv_contract_detail_name = findView(R.id.tv_contract_detail_name);
        tv_contract_detail_date = findView(R.id.tv_contract_detail_date);
        tv_contract_detail_cost = findView(R.id.tv_contract_detail_cost);
        tv_contract_detail_rate = findView(R.id.tv_contract_detail_rate);
        tv_contract_detail_state = findView(R.id.tv_contract_detail_state);
        tv_contract_detail_type = findView(R.id.tv_contract_detail_type);

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(false);

        initDialog();
    }

    private void initDialog() {
        dialog = new Dialog(ContractDetailActivity.this, R.style.Dialog);
        View view = LayoutInflater.from(ContractDetailActivity.this).inflate(R.layout.dialog_tip_no_open, null);
        tv_dialog_content = (TextView) view.findViewById(R.id.tv_dialog_content);
        TextView tv_dialog_sure = (TextView) view.findViewById(R.id.tv_dialog_sure);
        tv_dialog_content.setText("无法释放本金");
        tv_dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        rateDialog = new Dialog(this,R.style.Dialog);
        View rateView = getLayoutInflater().inflate(R.layout.dialog_yield_rate_detail, null);
        TextView tv_yield_rate_cancel = (TextView) rateView.findViewById(R.id.tv_yield_rate_cancel);
        tv_yield_rate_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateDialog.dismiss();
            }
        });
        rateDialog.setCanceledOnTouchOutside(false);
        rateDialog.setContentView(rateView);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        img_month_rate.setOnClickListener(this);
        tv_release_principal.setOnClickListener(this);

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getOrderDetail(App.token, oid);
            }
        });
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            oid = getIntent().getStringExtra("oid");

            getOrderDetail(App.token, oid);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.img_month_rate:
                rateDialog.show();
                break;
            case R.id.tv_release_principal:
                releasePrincipal(App.token, oid);
                break;
        }
    }



    /**
     * App22 > 释放本金
     */
    private void releasePrincipal(final String token, final String oid) {
        if (NetUtil.isNetWorking(ContractDetailActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.releasePrincipalData(token, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
//                                startActivity(new Intent(ContractDetailActivity.this, ReleaseDetailActivity.class));
                                toast(data.message);
                                getOrderDetail(App.token, oid);
                            }else {
                                tv_dialog_content.setText(data.message);
                                dialog.show();
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
     * App33 > 获取合约详情
     */
    private void getOrderDetail(final String token, final String oid) {
        if (NetUtil.isNetWorking(ContractDetailActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getOrderDetailData(token, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.ContractDetail data = new Gson().fromJson(result, Bean.ContractDetail.class);
                            if (data.status == 1){
                                if ("0".equals(data.state)){
                                    tv_contract_detail_state.setText("进行中");
                                    tv_release_principal.setBackgroundColor(getResources().getColor(R.color.text_blue2));
                                    tv_release_principal.setClickable(true);
                                }else if ("1".equals(data.state)){
                                    tv_contract_detail_state.setText("已完成");
                                    tv_release_principal.setBackgroundColor(getResources().getColor(R.color.hintColor));
                                    tv_release_principal.setClickable(false);
                                }

                                if ("0".equals(data.type)){
                                    tv_release_principal.setBackgroundColor(getResources().getColor(R.color.hintColor));
                                    tv_release_principal.setClickable(false);
                                    tv_contract_detail_type.setText("常规模式");
                                }else {
                                    tv_contract_detail_type.setText("余额宝模式");
//                                    tv_release_principal.setBackgroundColor(getResources().getColor(R.color.text_blue2));
//                                    tv_release_principal.setClickable(true);
                                }

                                tv_contract_percent.setText("+"+ StringUtil.doubleToString(data.averageRate) + "%");
                                tv_contract_detail_date.setText(data.date);
                                tv_contract_detail_cost.setText(StringUtil.doubleToString(data.principal));
                                tv_contract_detail_rate.setText(StringUtil.doubleToString(data.money));
                                tv_contract_detail_name.setText(data.name);

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
