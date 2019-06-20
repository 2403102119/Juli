package com.tcckj.juli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.adapter.HuntRecordAdapter;
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
 * 合约记录界面
 */
public class ContractRecordActivity extends BaseActivity {
    List<Map<String, Object>> list = new ArrayList<>();
    HuntRecordAdapter adapter;
    NiceRecyclerView nrv_contract_record;

    private int index = 0,num = 15;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_contrect_record);

        title.setText("合约记录");
        nrv_contract_record = findView(R.id.nrv_contract_record);

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        adapter = new HuntRecordAdapter(ContractRecordActivity.this, list);
        nrv_contract_record.setAdapter(adapter);
        adapter.setOnItemClickListener(new HuntRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Intent intent = new Intent(ContractRecordActivity.this, ContractDetailActivity.class);
                intent.putExtra("oid", (String) list.get(position).get("oid"));
                startActivity(intent);
            }
        });

        getOrderList(App.token, String.valueOf(index), String.valueOf(num));

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index = 0;
                getOrderList(App.token, String.valueOf(index), String.valueOf(num));
            }
        });

        mySmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index += num;
                getOrderList(App.token, String.valueOf(index), String.valueOf(num));
            }
        });
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
     * App06 > 获取合约记录
     */
    private void getOrderList(final String token, final String index, final String num) {
        if (NetUtil.isNetWorking(ContractRecordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getOrderListData(token, index, num, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            if (!mySmart.isLoading()){
                                list.clear();
                            }

                            Bean.ContractListAll data = new Gson().fromJson(result, Bean.ContractListAll.class);
                            if (data.status == 1){
                                List<Bean.Contract> orderList = data.orderList;
                                for (int i = 0; i < orderList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("oid", orderList.get(i).oid);
                                    map.put("date", orderList.get(i).date);
                                    map.put("frozenDate", orderList.get(i).frozenDate);
                                    map.put("money", orderList.get(i).money);
                                    map.put("number", orderList.get(i).number);
                                    map.put("releaseDate", orderList.get(i).releaseDate);
                                    map.put("name", orderList.get(i).commodity.name);

                                    list.add(map);
                                }

                                adapter.notifyDataSetChanged();
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
