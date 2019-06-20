package com.tcckj.juli.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.adapter.FirstFriendsAdapter;
import com.tcckj.juli.adapter.SecondFriendsAdapter;
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
 * 我的端口
 */
public class MyPortActivity extends BaseActivity {
    NiceRecyclerView nev_my_port;
    SecondFriendsAdapter adapter;
    TextView tv_my_port_name;

    List<Map<String, Object>> list = new ArrayList<>();

    private int index = 0, num = 15;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_my_port);

        title.setText("我的端口");
        nev_my_port = findView(R.id.nev_my_port);
        tv_my_port_name = findView(R.id.tv_my_port_name);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        adapter = new SecondFriendsAdapter(MyPortActivity.this, list);
        nev_my_port.setAdapter(adapter);
        adapter.setOnItemClickListener(new SecondFriendsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {

            }
        });

        applyPort(App.token, String.valueOf(index), String.valueOf(num));
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
     * App31 > 我的端口数据显示
     */
    private void applyPort(final String token, final String index, final String num) {
        if (NetUtil.isNetWorking(MyPortActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.myPortData(token, index, num, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            if (!mySmart.isLoading()){
                                list.clear();
                            }

                            Bean.MyPortAll data = new Gson().fromJson(result, Bean.MyPortAll.class);
                            if (data.status == 1){
                                tv_my_port_name.setText("我的端口：" + data.portName);

                                List<Bean.FirstFriends> firstFriendsList = data.list;
                                for (int i = 0; i < firstFriendsList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("oid",firstFriendsList.get(i).oid);
                                    map.put("account",firstFriendsList.get(i).account);
                                    map.put("name",firstFriendsList.get(i).name);
                                    map.put("recommend_number",firstFriendsList.get(i).recommend_number);
                                    map.put("registDate",firstFriendsList.get(i).registDate);
                                    map.put("money",firstFriendsList.get(i).money);

                                    list.add(map);
                                }
                                adapter.notifyDataSetChanged();

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
