package com.tcckj.juli.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.adapter.TransactionRecordAdapter;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 交易记录
 */
public class TransactionRecordActivity extends BaseActivity {
    RadioButton rb_transaction_record_income,rb_transaction_record_expenditure;
    List<Map<String, Object>> list = new ArrayList<>();

    NiceRecyclerView nrv_transaction_record;

    TransactionRecordAdapter adapter;
    SmartRefreshLayout smart;

    private String type = "0";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_transation_record);

        title.setText("交易记录");

        rb_transaction_record_income = findView(R.id.rb_transaction_record_income);
        rb_transaction_record_expenditure = findView(R.id.rb_transaction_record_expenditure);
        nrv_transaction_record = findView(R.id.nrv_transaction_record);
        smart = findView(R.id.smart);

        rb_transaction_record_income.setTextColor(getResources().getColor(R.color.text_blue2));
        rb_transaction_record_income.setChecked(true);

        smart.setEnableRefresh(true);
    }

    @Override
    protected void initListener() {
        rb_transaction_record_income.setOnClickListener(this);
        rb_transaction_record_expenditure.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        adapter = new TransactionRecordAdapter(TransactionRecordActivity.this, list);
        nrv_transaction_record.setAdapter(adapter);
        adapter.setOnItemClickListener(new TransactionRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

            }
        });

        getTreasureList(App.token, type);

        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getTreasureList(App.token, type);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.rb_transaction_record_income:
                rb_transaction_record_income.setTextColor(getResources().getColor(R.color.text_blue2));
                rb_transaction_record_expenditure.setTextColor(getResources().getColor(R.color.gray));
                type = "0";
                getTreasureList(App.token, type);
                break;
            case R.id.rb_transaction_record_expenditure:
                rb_transaction_record_expenditure.setTextColor(getResources().getColor(R.color.text_blue2));
                rb_transaction_record_income.setTextColor(getResources().getColor(R.color.gray));
                type = "1";
                getTreasureList(App.token, type);
                break;
        }
    }



    /**
     * App20 > 获取交易记录
     */
    private void getTreasureList(final String token, final String type) {
        if (NetUtil.isNetWorking(TransactionRecordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getTreasureListData(token, type, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.TreasureListAll data = new Gson().fromJson(result, Bean.TreasureListAll.class);
                            if (data.status == 1){
                                if (!mySmart.isLoading()){
                                    list.clear();
                                }

                                List<Bean.Treasure> treasureList = data.recordList;
                                for (int i = 0; i < treasureList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("oid", treasureList.get(i).oid);
                                    map.put("value", treasureList.get(i).value);
                                    map.put("date", treasureList.get(i).date);
                                    map.put("name", treasureList.get(i).name);
                                    map.put("type", treasureList.get(i).type);

                                    list.add(map);
                                }

                                adapter.notifyDataSetChanged();
                                smart.finishRefresh();
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            smart.finishRefresh();
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
