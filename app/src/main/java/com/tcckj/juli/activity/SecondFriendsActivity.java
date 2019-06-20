package com.tcckj.juli.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.adapter.SecondFriendsAdapter;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.StringUtil;
import com.tcckj.juli.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 二级好友界面
 */
public class SecondFriendsActivity extends BaseActivity {
    NiceRecyclerView nrv_second_friends;
    SecondFriendsAdapter adapter;
    ImageView img_second_friend_back;
    SmartRefreshLayout smart;
    TextView tv_second_all_cost;

    List<Map<String, Object>> list = new ArrayList<>();
    private String oid;

    private int index = 0,num = 15;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_second_friends);

        top.setVisibility(View.GONE);
        nrv_second_friends = findView(R.id.nrv_second_friends);
        img_second_friend_back = findView(R.id.img_second_friend_back);
        tv_second_all_cost = findView(R.id.tv_second_all_cost);
        smart = findView(R.id.smart);

        smart.setEnableRefresh(true);
        smart.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {
        img_second_friend_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            oid = getIntent().getStringExtra("oid");
        }

        adapter = new SecondFriendsAdapter(SecondFriendsActivity.this, list);
        nrv_second_friends.setAdapter(adapter);
        adapter.setOnItemClickListener(new SecondFriendsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {

            }
        });

        getFriendsList(App.token, String.valueOf(index), String.valueOf(num), oid);

        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index = 0;
                getFriendsList(App.token, String.valueOf(index), String.valueOf(num), oid);
            }
        });

        smart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index += num;
                getFriendsList(App.token, String.valueOf(index), String.valueOf(num), oid);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_second_friend_back:
                finish();
                break;
        }
    }


    /**
     * App15 > 获取好友记录
     */
    private void getFriendsList(final String token, final String index, final String num, final String oid) {
        if (NetUtil.isNetWorking(SecondFriendsActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getFriendsListData(token, index, num, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            if (!smart.isLoading()){
                                list.clear();
                            }

                            Bean.FirstFriendsListAll data = new Gson().fromJson(result, Bean.FirstFriendsListAll.class);
                            if (data.status == 1){
                                tv_second_all_cost.setText("团队总金额：  " + StringUtil.doubleToString(data.sum));

                                List<Bean.FirstFriends> firstFriendsList = data.userList;
                                for (int i = 0; i < firstFriendsList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("account", firstFriendsList.get(i).account);
                                    map.put("money", firstFriendsList.get(i).money);
                                    map.put("name", firstFriendsList.get(i).name);
                                    map.put("oid", firstFriendsList.get(i).oid);
                                    map.put("recommend_number", firstFriendsList.get(i).recommend_number);
                                    map.put("registDate", firstFriendsList.get(i).registDate);

                                    list.add(map);
                                }

                                adapter.notifyDataSetChanged();
                            }
                            smart.finishRefresh();
                            smart.finishLoadmore();
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            smart.finishRefresh();
                            smart.finishLoadmore();
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
