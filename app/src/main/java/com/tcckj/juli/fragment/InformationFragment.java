package com.tcckj.juli.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.activity.UnfreezeWalletActivity;
import com.tcckj.juli.activity.WebShowActivity;
import com.tcckj.juli.adapter.CalculatePowerRankingAdapter;
import com.tcckj.juli.base.BaseFragment;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.StringUtil;
import com.tcckj.juli.util.UriUtil;
import com.tcckj.juli.view.NiceRecyclerView;
import com.tcckj.juli.view.SetBanner;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 资讯界面
 */
public class InformationFragment extends BaseFragment {
    RelativeLayout rl_information;
    NiceRecyclerView nrv_calculate_power_record;
    CalculatePowerRankingAdapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();
    List<String> listBanner = new ArrayList<>();
    List<Map<String, Object>> listBannerAll = new ArrayList<>();
    TextView tv_information_title,tv_information_content,tv_information_date;

    Banner banner;

    @Override
    protected void initView(View view) {
        setContainer(R.layout.fragment_information);

        rl_information = (RelativeLayout) view.findViewById(R.id.rl_information);
        nrv_calculate_power_record = (NiceRecyclerView) view.findViewById(R.id.nrv_calculate_power_record);
        banner = (Banner) view.findViewById(R.id.banner);

        tv_information_title = findView(R.id.tv_information_title);
        tv_information_content = findView(R.id.tv_information_content);
        tv_information_date = findView(R.id.tv_information_date);

        mySmart.setEnableRefresh(true);
    }

    @Override
    protected void initListener() {
        rl_information.setOnClickListener(this);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (StringUtil.isSpace((String) listBannerAll.get(position).get("url"))){
                    return;
                }
                Intent intent = new Intent(getActivity(), WebShowActivity.class);
                intent.putExtra("url", (String) listBannerAll.get(position).get("url"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        adapter = new CalculatePowerRankingAdapter(getActivity(), list);
        nrv_calculate_power_record.setAdapter(adapter);

        getForceList(App.token);

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getForceList(App.token);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_information:
//                startActivity(new Intent(getActivity(), UnfreezeWalletActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        SetBanner.context=null;
        super.onDestroy();
    }



    /**
     * App19 > 获取算力排行
     */
    private void getForceList(final String token) {
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getForceListData(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.InfomationAll data = new Gson().fromJson(result, Bean.InfomationAll.class);
                            if (data.status == 1){
                                if (!mySmart.isLoading()){
                                    list.clear();
                                    listBanner.clear();
                                    listBannerAll.clear();
                                }

                                List<Bean.UserInfo> userInfoList = data.userList;
                                if (userInfoList.size() <= 10) {
                                    for (int i = 0; i < userInfoList.size(); i++) {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("oid", userInfoList.get(i).oid);
                                        map.put("account", userInfoList.get(i).account);
                                        map.put("forceVlue", userInfoList.get(i).forceVlue);
                                        map.put("name", userInfoList.get(i).name);
                                        map.put("recommend_number", userInfoList.get(i).recommend_number);

                                        list.add(map);
                                    }
                                }else {
                                    for (int i = 0; i < 10; i++) {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("oid", userInfoList.get(i).oid);
                                        map.put("account", userInfoList.get(i).account);
                                        map.put("forceVlue", userInfoList.get(i).forceVlue);
                                        map.put("name", userInfoList.get(i).name);
                                        map.put("recommend_number", userInfoList.get(i).recommend_number);

                                        list.add(map);
                                    }
                                }

                                List<Bean.NoticeInfo> noticeInfoList = data.noticeList;
                                if (noticeInfoList.size() > 0) {
                                    tv_information_title.setText(noticeInfoList.get(0).title);
                                    tv_information_content.setText(noticeInfoList.get(0).content);

                                    String dateStr = noticeInfoList.get(0).date;

                                    String[] dateNum = dateStr.split(" ");

                                    String[] date = dateNum[0].split("-");
                                    String[] time = dateNum[1].split(":");

                                    tv_information_date.setText(date[1]+"-"+date[2]+" "+ time[0]+":"+time[1]);
                                }

                                List<Bean.BannerInfo> bannerInfoList = data.bannerList;
                                for (int i = 0; i < bannerInfoList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("content", bannerInfoList.get(i).content);
                                    map.put("date", bannerInfoList.get(i).date);
                                    map.put("oid", bannerInfoList.get(i).oid);
                                    map.put("photo", bannerInfoList.get(i).photo);
                                    map.put("url", bannerInfoList.get(i).url);

                                    listBanner.add(UriUtil.ip + bannerInfoList.get(i).photo);
                                    listBannerAll.add(map);
                                }
                                SetBanner.startBanner(getActivity(),banner,listBanner);

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
