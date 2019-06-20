package com.tcckj.juli.activity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.adapter.ContactRecordAdapter;
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
 * 算力记录界面
 */
public class CalculatePowerRecordActivity extends BaseActivity {
    NiceRecyclerView nrv_calculate_power_record;
    ContactRecordAdapter adapter;

    List<Map<String, Object>> list = new ArrayList<>();
    private int index = 0, num = 15;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_calculate_power_record);

        title.setText("算力记录");
        nrv_calculate_power_record = findView(R.id.nrv_calculate_power_record);

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        adapter = new ContactRecordAdapter(CalculatePowerRecordActivity.this, list);
        nrv_calculate_power_record.setAdapter(adapter);

        getForceVlueList(App.token, String.valueOf(index), String.valueOf(num));

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index = 0;
                getForceVlueList(App.token, String.valueOf(index), String.valueOf(num));
            }
        });

        mySmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index += num;
                getForceVlueList(App.token, String.valueOf(index), String.valueOf(num));
            }
        });
    }



    /**
     * App11 > 修改昵称
     */
    private void getForceVlueList(final String token, final String index, final String num) {
        if (NetUtil.isNetWorking(CalculatePowerRecordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getForceVlueListData(token, index, num,  new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            if (!mySmart.isLoading()){
                                list.clear();
                            }

                            Bean.CalculateListAll data = new Gson().fromJson(result, Bean.CalculateListAll.class);
                            if (data.status == 1){
                                List<Bean.Calculate> calculateList = data.orderList;
                                for (int i = 0; i < calculateList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("oid", calculateList.get(i).oid);
                                    map.put("value", calculateList.get(i).value);
                                    map.put("date", calculateList.get(i).date);
                                    map.put("describe", calculateList.get(i).describe);
                                    map.put("type", calculateList.get(i).type);

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
                            finish();
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
