package com.tcckj.juli.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.activity.CalculatePowerRecordActivity;
import com.tcckj.juli.activity.ContractRecordActivity;
import com.tcckj.juli.activity.OreMachineDetailsActivity;
import com.tcckj.juli.adapter.MarketAdapter;
import com.tcckj.juli.base.BaseFragment;
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
 * 市场界面
 */
public class MarketFragment extends BaseFragment {
    List<Map<String, Object>> list = new ArrayList<>();
    NiceRecyclerView nrv_market;

    MarketAdapter adapter;

    private int index = 0, num = 15;

    @Override
    protected void initView(View view) {
        setContainer(R.layout.fragment_market);

        top.setVisibility(View.VISIBLE);
        back.setVisibility(View.GONE);
        rightTxt.setVisibility(View.VISIBLE);
        title.setText("市场");
        rightTxt.setText("记录");

        nrv_market = findView(R.id.nrv_market);

        mySmart.setEnableRefresh(true);
    }

    @Override
    protected void initData() {
        adapter = new MarketAdapter(getActivity(), list);
        nrv_market.setAdapter(adapter);
        adapter.setOnItemClickListener(new MarketAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {
                Intent intent = new Intent(getActivity(), OreMachineDetailsActivity.class);
                intent.putExtra("oid", (String) list.get(position).get("oid"));
                intent.putExtra("name", (String) list.get(position).get("name"));
                startActivity(intent);
            }
        });

        getCommodityList(App.token, String.valueOf(index), String.valueOf(num));

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index = 0;
                getCommodityList(App.token, String.valueOf(index), String.valueOf(num));
            }
        });

        mySmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index += num;
                getCommodityList(App.token, String.valueOf(index), String.valueOf(num));
            }
        });
    }

    @Override
    protected void initListener() {
        rightTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightTxt:
                startActivity(new Intent(getActivity(), ContractRecordActivity.class));
                break;
        }
    }



    /**
     * App17 > 获取商品记录
     */
    private void getCommodityList(final String token, final String index, final String num) {
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getCommodityListData(token, index, num, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            if (!mySmart.isLoading()){
                                list.clear();
                            }

                            Bean.CommodityListAll data = new Gson().fromJson(result, Bean.CommodityListAll.class);
                            if (data.status == 1){
                                List<Bean.Commodity> commodityList = data.commodityList;
                                for (int i = 0; i < commodityList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("averageRate", commodityList.get(i).averageRate);
                                    map.put("frozenDate", commodityList.get(i).frozenDate);
                                    map.put("name", commodityList.get(i).name);
                                    map.put("number", commodityList.get(i).number);
                                    map.put("oid", commodityList.get(i).oid);
                                    map.put("photo", commodityList.get(i).photo);
                                    map.put("releaseDate", commodityList.get(i).releaseDate);

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
