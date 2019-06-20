package com.tcckj.juli.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.adapter.WithdrawRecordAdapter;
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

/*
提现记录界面
 */
public class WithdrawRecordActivity extends BaseActivity {
    NiceRecyclerView nrv_withdraw_record;
    WithdrawRecordAdapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();

    private int index = 0, num = 15;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_withdraw_record);

        title.setText("提现记录");
        nrv_withdraw_record = findView(R.id.nrv_withdraw_record);

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        adapter = new WithdrawRecordAdapter(list, WithdrawRecordActivity.this);
        nrv_withdraw_record.setAdapter(adapter);
        adapter.setOnItemClickListener(new WithdrawRecordAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {

            }

            @Override
            public void OnDeleteListener(int position) {
                if(App.islogin) {
                    deletePutForward(App.token, (String) list.get(position).get("oid"));
                }else {
                    startActivity(new Intent(WithdrawRecordActivity.this, LoginActivity.class));
                }
            }
        });

        if (App.islogin) {
            getPutForwardList(App.token, String.valueOf(index), String.valueOf(num));
        }else {
            startActivity(new Intent(WithdrawRecordActivity.this, LoginActivity.class));
        }

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index = 0;
                getPutForwardList(App.token, String.valueOf(index), String.valueOf(num));
            }
        });

        mySmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index += num;
                getPutForwardList(App.token, String.valueOf(index), String.valueOf(num));
            }
        });
    }


    /**
     * App42 > 获取提现记录
     */
    private void getPutForwardList(final String token, final String index, final String num) {
        if (NetUtil.isNetWorking(WithdrawRecordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getPutForwardListData(token, index, num, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.WithdrawRecordAll data = new Gson().fromJson(result, Bean.WithdrawRecordAll.class);
                            if (data.status == 1){
                                if (!mySmart.isLoading()){
                                    list.clear();
                                }

                                List<Bean.WithdrawRecord> withdrawRecordList = data.presentList;
                                for (int i = 0; i < withdrawRecordList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("back", withdrawRecordList.get(i).back);
                                    map.put("backName", withdrawRecordList.get(i).backName);
                                    map.put("state", withdrawRecordList.get(i).state);
                                    map.put("date", withdrawRecordList.get(i).date);
                                    map.put("money", withdrawRecordList.get(i).money);
                                    map.put("oid", withdrawRecordList.get(i).oid);
                                    map.put("status", withdrawRecordList.get(i).status);
                                    map.put("type", withdrawRecordList.get(i).type);

                                    list.add(map);
                                }

                                adapter.notifyDataSetChanged();
                                finishRefresh();
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


    /**
     * App43 > 删除提现记录
     */
    private void deletePutForward(final String token, final String oid) {
        if (NetUtil.isNetWorking(WithdrawRecordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.deletePutForwardData(token, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                toast(data.message);
                                getPutForwardList(App.token, String.valueOf(index), String.valueOf(num));
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
