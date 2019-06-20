package com.tcckj.juli.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.activity.CalculatePowerStrategyActivity;
import com.tcckj.juli.activity.ContractRecordActivity;
import com.tcckj.juli.activity.InviteFriendsActivity;
import com.tcckj.juli.activity.LoginActivity;
import com.tcckj.juli.activity.MyWalletActivity;
import com.tcckj.juli.activity.WebShowActivity;
import com.tcckj.juli.base.BaseFragment;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.ImageLoadUtil;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.StringUtil;
import com.tcckj.juli.util.UriUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 寻宝界面
 */
public class TreasureHuntFragment extends BaseFragment {

    ImageView img_hunt_my_property, img_hunt_invite_friends,img_hunt_promote,img_get_detail,
            img_orb_show,img_box_bg;
    TextView tv_hunt_get_more, tv_hunt_suanli_show,tv_auto_hunt_tip,tv_box_top,tv_box_bottom,
            tv_box_bottom_true,tv_hunt_contract_tip;
    TextView tv_hunt_name1,tv_hunt_time1,tv_hunt_cost1,tv_hunt_name2,tv_hunt_time2,tv_hunt_cost2;
    View view_hunt_contract;
    LinearLayout ll_hunt_contract_title,ll_hunt_contract1,ll_hunt_contract2;
    //    VerticalMarqueeView tv_hunt_broadcast;
    TextView tv_hunt_broadcast;
    CheckBox cb_auto_hunt;

    List<Map<String, Object>> list = new ArrayList<>();

    private String moneyState = "0";                       //自动挖宝   0：关闭      1：开启
    private String dayState = "0";                       //自动挖宝   0：关闭      1：开启
    private String url = "";                                //广告链接

    private int imgStaticId = 0;                          //图片静态资源id
    private int imgDynamicId = 0;                          //图片动态资源id

    private int imgBoxOpen = 0;                            //宝箱打开资源id
    private int imgBoxClose = 0;                            //宝箱未打开资源id

    private int audioId = 0;                                 //播放的音频id

    private boolean isBoxShow = false;                      //是否显示宝箱，如果不是，显示挖宝
    private boolean isBoxOpen = false;                      //宝箱是否打开
    private String boxOid = "";                               //宝箱id

    MediaPlayer mMediaPlayer;

    @Override
    protected void initView(View view) {
        setContainer(R.layout.fragment_treasure_hunt);

        img_hunt_my_property = (ImageView) view.findViewById(R.id.img_hunt_my_property);        //我的资产
        img_hunt_invite_friends = (ImageView) view.findViewById(R.id.img_hunt_invite_friends); //邀请好友
        img_hunt_promote = (ImageView) view.findViewById(R.id.img_hunt_promote);                //提升算力
        img_get_detail = (ImageView) view.findViewById(R.id.img_get_detail);                     //了解详情

        tv_hunt_get_more = (TextView) view.findViewById(R.id.tv_hunt_get_more);                  //查看更多（合约记录）
        tv_hunt_suanli_show = (TextView) view.findViewById(R.id.tv_hunt_suanli_show);           //算力展示
//        tv_hunt_broadcast = (VerticalMarqueeView) view.findViewById(R.id.tv_hunt_broadcast);               //广播展示
        tv_hunt_broadcast = findView(R.id.tv_hunt_broadcast);
        tv_auto_hunt_tip = (TextView) view.findViewById(R.id.tv_auto_hunt_tip);                 //挖宝中
        tv_box_top = (TextView) view.findViewById(R.id.tv_box_top);                                 //宝箱顶部文字
        tv_box_bottom = (TextView) view.findViewById(R.id.tv_box_bottom);                          //宝箱/挖宝底部文字
        tv_box_bottom_true = (TextView) view.findViewById(R.id.tv_box_bottom_true);               //宝箱/挖宝底部文字

        cb_auto_hunt = (CheckBox) view.findViewById(R.id.cb_auto_hunt);                            //自动挖宝
        img_orb_show = (ImageView) view.findViewById(R.id.img_orb_show);                            //自动挖宝动画
        img_box_bg = (ImageView) view.findViewById(R.id.img_box_bg);                                //宝箱背景

        tv_hunt_name1 = findView(R.id.tv_hunt_name1);
        tv_hunt_time1 = findView(R.id.tv_hunt_time1);
        tv_hunt_cost1 = findView(R.id.tv_hunt_cost1);
        tv_hunt_name2 = findView(R.id.tv_hunt_name2);
        tv_hunt_time2 = findView(R.id.tv_hunt_time2);
        tv_hunt_cost2 = findView(R.id.tv_hunt_cost2);
        tv_hunt_contract_tip = findView(R.id.tv_hunt_contract_tip);
        view_hunt_contract = findView(R.id.view_hunt_contract);
        ll_hunt_contract1 = findView(R.id.ll_hunt_contract1);
        ll_hunt_contract2 = findView(R.id.ll_hunt_contract2);
        ll_hunt_contract_title = findView(R.id.ll_hunt_contract_title);

//        Glide.with(getActivity()).load(imgDynamicId).into(img_box_bg);

//        img_box_bg.setVisibility(View.GONE);

//        tv_hunt_broadcast.setMovementMethod(new ScrollingMovementMethod());
//        tv_hunt_broadcast.setText("余币宝产品于4月10号上线，本期计划已结束，请期待更多");

        mySmart.setEnableRefresh(true);
    }

    @Override
    protected void initListener() {
        img_hunt_my_property.setOnClickListener(this);
        img_hunt_invite_friends.setOnClickListener(this);
        img_hunt_promote.setOnClickListener(this);
        img_get_detail.setOnClickListener(this);
        tv_hunt_get_more.setOnClickListener(this);
        cb_auto_hunt.setOnClickListener(this);
        img_orb_show.setOnClickListener(this);
        img_box_bg.setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        MediaPlayer mMediaPlayer;
//        mMediaPlayer=MediaPlayer.create(getActivity(), R.raw.box);
//        mMediaPlayer.start();

        setAudioId();

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                homePage(App.token);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_hunt_my_property:
                startActivity(new Intent(getActivity(), MyWalletActivity.class));               //我的钱包
                break;
            case R.id.img_hunt_invite_friends:
                startActivity(new Intent(getActivity(), InviteFriendsActivity.class));          //邀请好友
                break;
            case R.id.tv_hunt_get_more:
                startActivity(new Intent(getActivity(), ContractRecordActivity.class));         //合约记录
                break;
            case R.id.img_hunt_promote:
                startActivity(new Intent(getActivity(), CalculatePowerStrategyActivity.class)); //算力攻略
                break;
            case R.id.img_get_detail:
                if (StringUtil.isSpace(url)){
                    break;
                }
                Intent intent = new Intent(getActivity(), WebShowActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);                //了解详情
                break;
            case R.id.cb_auto_hunt:
//                if ("0".equals(moneyState)){
//                    toast("购买合约后才能开启");
//                    cb_auto_hunt.setChecked(false);
//
//                    break;
//                }

//                tv_box_top.setVisibility(View.GONE);
//                img_box_bg.setVisibility(View.GONE);
//                tv_box_bottom_true.setVisibility(View.GONE);
//                img_orb_show.setVisibility(View.VISIBLE);
//                tv_box_bottom.setVisibility(View.VISIBLE);
//
//                isBoxShow = false;

                setAudioId();

                cb_auto_hunt.setEnabled(false);
                img_orb_show.setClickable(false);
                switch (moneyState){
                    case "0":
                        switchState(App.token, "1");
                        break;
                    case "1":
                        switch (dayState){
                            case "1":
                                switchState(App.token, "1");
                                break;
                            case "0":
                                switchState(App.token, "0");
                                break;
                        }
                        break;
                }
                break;
            case R.id.img_orb_show:
                Log.i("111111111", "onClick: " + moneyState);
//                cb_auto_hunt.setEnabled(false);
                img_orb_show.setClickable(false);
                switch (moneyState) {
                    case "0":
                        switchDayState(App.token, "1");
//                        switchState(App.token, "1");
                        break;
                    case "1":
                        toast("已经在挖宝了");
//                        cb_auto_hunt.setEnabled(true);
                        img_orb_show.setClickable(true);
                        break;
                }
                break;
            case R.id.img_box_bg:
                if (isBoxOpen){
                    homePage(App.token);
                }else {
                    openBox(App.token, boxOid);
                }
                break;
        }
    }

    private void setAudioId(){
        if (App.islogin){
            switch (App.personalInfo.memberLevl){
                case "4":
                    imgStaticId = R.mipmap.tieqiao_static;
                    imgDynamicId = R.mipmap.tieqiao_dynamic;
                    audioId = R.raw.tieqiao;
                    break;
                case "3":
                    imgStaticId = R.mipmap.tiegao_static;
                    imgDynamicId = R.mipmap.tiegao_dynamic;
                    audioId = R.raw.tiegao;
                    break;
                case "2":
                    imgStaticId = R.mipmap.dianzuan_static;
                    imgDynamicId = R.mipmap.dianzuan_dynamic;
                    audioId = R.raw.dianzuan;
                    break;
                case "1":
                    imgStaticId = R.mipmap.wajueji_static;
                    imgDynamicId = R.mipmap.wajueji_dynamic;
                    audioId = R.raw.wajueji;
                    break;
            }
        }
    }

    private int sp2px(Context context, int sp){
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5f);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mMediaPlayer && App.isAudioOpen && 0 != audioId){
            mMediaPlayer.pause();
        }

//        mMediaPlayer = MediaPlayer.create(getActivity(), audioId);
//        mMediaPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        setAudioId();

        if (null != img_orb_show && getActivity()!=null&&!getActivity().isDestroyed()) {
            Glide.with(getActivity()).load(imgStaticId).into(img_orb_show);
        }

        if (App.islogin){
            homePage(App.token);
        }else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mMediaPlayer){
            mMediaPlayer.reset();
        }
//        tv_hunt_broadcast.stopScroll();
    }

    /**
     * App05 > 首页数据
     */
    private void homePage(final String token) {
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.homePageData(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            if (!mySmart.isLoading()){
                                list.clear();
                            }

                            Bean.TreasureHuntAll data = new Gson().fromJson(result, Bean.TreasureHuntAll.class);
                            if (data.status == 1){
                                tv_hunt_suanli_show.setText("当前挖宝算力    " + (int)data.model.forceVlue);
                                moneyState = data.model.moneyState;
                                dayState = data.model.dayState;

                                if (null != mMediaPlayer){
                                    mMediaPlayer.reset();
                                }

                                switch (data.model.orderList.size()){
                                    case 0:
                                        ll_hunt_contract1.setVisibility(View.GONE);
                                        ll_hunt_contract2.setVisibility(View.GONE);
                                        ll_hunt_contract_title.setVisibility(View.GONE);
                                        tv_hunt_contract_tip.setVisibility(View.VISIBLE);
                                        view_hunt_contract.setVisibility(View.GONE);
                                        tv_hunt_get_more.setVisibility(View.GONE);
                                        break;
                                    case 1:
                                        ll_hunt_contract1.setVisibility(View.VISIBLE);
                                        ll_hunt_contract2.setVisibility(View.GONE);
                                        ll_hunt_contract_title.setVisibility(View.VISIBLE);
                                        tv_hunt_contract_tip.setVisibility(View.GONE);
                                        view_hunt_contract.setVisibility(View.GONE);
                                        tv_hunt_get_more.setVisibility(View.VISIBLE);

                                        String dateStr = data.model.orderList.get(0).date;
                                        String date[] = dateStr.split(" ");

                                        tv_hunt_name1.setText(data.model.orderList.get(0).commodity.name);
                                        tv_hunt_time1.setText(date[0] + "\n" + date[1]);
                                        tv_hunt_cost1.setText(StringUtil.doubleToString(data.model.orderList.get(0).money));
                                        break;
                                    case 2:
                                        ll_hunt_contract1.setVisibility(View.VISIBLE);
                                        ll_hunt_contract2.setVisibility(View.VISIBLE);
                                        ll_hunt_contract_title.setVisibility(View.VISIBLE);
                                        tv_hunt_contract_tip.setVisibility(View.GONE);
                                        view_hunt_contract.setVisibility(View.VISIBLE);
                                        tv_hunt_get_more.setVisibility(View.VISIBLE);

                                        String dateStr1 = data.model.orderList.get(0).date;
                                        String date1[] = dateStr1.split(" ");

                                        tv_hunt_name1.setText(data.model.orderList.get(0).commodity.name);
                                        tv_hunt_time1.setText(date1[0] + "\n" + date1[1]);
                                        tv_hunt_cost1.setText(StringUtil.doubleToString(data.model.orderList.get(0).money));

                                        String dateStr2 = data.model.orderList.get(1).date;
                                        String date2[] = dateStr2.split(" ");

                                        tv_hunt_name2.setText(data.model.orderList.get(1).commodity.name);
                                        tv_hunt_time2.setText(date2[0] + "\n" + date2[1]);
                                        tv_hunt_cost2.setText(StringUtil.doubleToString(data.model.orderList.get(1).money));

                                        break;
                                }

                                if (data.model.userCaseList.size() > 0){
                                    for (int i = 0; i < data.model.userCaseList.size(); i++) {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("oid",data.model.userCaseList.get(i).oid);
                                        map.put("name",data.model.userCaseList.get(i).name);
                                        map.put("number",data.model.userCaseList.get(i).number);
                                        map.put("state",data.model.userCaseList.get(i).state);
                                        map.put("type",data.model.userCaseList.get(i).type);

                                        list.add(map);
                                    }

                                    for (Map<String, Object> map:list
                                            ){
                                        if ("0".equals(map.get("state")) && null != getActivity()){
                                            isBoxOpen = false;

                                            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.box);

                                            switch ((String)map.get("name")){
                                                case "金":
                                                    imgBoxOpen = R.mipmap.jin_open;
                                                    imgBoxClose = R.mipmap.jin_close;
                                                    break;
                                                case "银":
                                                    imgBoxOpen = R.mipmap.yin_open;
                                                    imgBoxClose = R.mipmap.yin_close;
                                                    break;
                                                case "铜":
                                                    imgBoxOpen = R.mipmap.tong_open;
                                                    imgBoxClose = R.mipmap.tong_close;
                                                    break;
                                            }
                                            switch ((String)map.get("type")){
                                                case "0":
                                                    tv_box_top.setText("恭喜你获得了注册宝箱！");
                                                    break;
                                                case "1":
                                                    tv_box_top.setText("恭喜你获得了推荐宝箱！");
                                                    break;
                                                case "2":
                                                    tv_box_top.setText("恭喜你获得了收益宝箱！");
                                                    break;
                                                case "3":
                                                    tv_box_top.setText("恭喜你获得了寻宝宝箱！");
                                                    break;
                                            }
                                            tv_box_bottom_true.setText("点击打开宝箱");

                                            boxOid = (String) map.get("oid");

                                            if (getActivity()!=null&&!getActivity().isDestroyed()) {
                                                Glide.with(getActivity()).load(imgBoxClose).into(img_box_bg);
                                            }

                                            tv_box_bottom_true.setTextSize(16);
                                            tv_box_bottom_true.setTextColor(getActivity().getResources().getColor(R.color.yellows));
                                            tv_box_top.setVisibility(View.VISIBLE);
                                            tv_box_bottom.setVisibility(View.GONE);
                                            tv_box_bottom_true.setVisibility(View.VISIBLE);
                                            img_box_bg.setVisibility(View.VISIBLE);
                                            img_orb_show.setVisibility(View.GONE);
                                            isBoxShow = true;
                                            break;
                                        }
                                    }

                                    if (!isBoxShow){
                                        if (0 != audioId && null != getActivity()) {
                                            mMediaPlayer = MediaPlayer.create(getActivity(), audioId);
                                            switch (data.model.moneyState) {
                                                case "0":
                                                    Glide.with(getActivity()).load(imgStaticId).into(img_orb_show);
                                                    tv_box_bottom.setText("正在休息中...");

                                                    cb_auto_hunt.setChecked(false);
                                                    tv_auto_hunt_tip.setVisibility(View.GONE);
                                                    break;
                                                case "1":
                                                    Glide.with(getActivity()).load(imgDynamicId).into(img_orb_show);
                                                    if (App.isAudioOpen) {
                                                        mMediaPlayer.start();
                                                        mMediaPlayer.setLooping(true);
                                                    } else {
                                                        if (null != mMediaPlayer) {
//                                                        mMediaPlayer.release();
                                                        }
                                                    }
                                                    tv_box_bottom.setText("正在挖宝中...");
                                                    switch (data.model.dayState){
                                                        case "0":
                                                            cb_auto_hunt.setChecked(true);
                                                            tv_auto_hunt_tip.setVisibility(View.VISIBLE);
                                                            break;
                                                        case "1":
                                                            cb_auto_hunt.setChecked(false);
                                                            tv_auto_hunt_tip.setVisibility(View.GONE);
                                                            break;
                                                    }
                                                    break;
                                            }
                                        }

                                        tv_box_top.setVisibility(View.GONE);
                                        tv_box_bottom.setVisibility(View.VISIBLE);
                                        tv_box_bottom_true.setVisibility(View.GONE);
                                        img_box_bg.setVisibility(View.GONE);
                                        img_orb_show.setVisibility(View.VISIBLE);

                                        isBoxShow = false;
                                    }
                                }else {
                                    if (0 != audioId && null != getActivity()) {
                                        mMediaPlayer = MediaPlayer.create(getActivity(), audioId);
                                        /*switch (data.model.moneyState) {
                                            case "0":
                                                Glide.with(getActivity()).load(imgStaticId).into(img_orb_show);
                                                tv_box_bottom.setText("正在休息中...");
                                                break;
                                            case "1":
                                                if ("0".equals(data.model.dayState)) {
                                                    Glide.with(getActivity()).load(imgDynamicId).into(img_orb_show);
                                                    if (App.isAudioOpen) {
                                                        mMediaPlayer.start();
                                                        mMediaPlayer.setLooping(true);
                                                    } else {
                                                        if (null != mMediaPlayer) {
//                                                        mMediaPlayer.release();
                                                        }
                                                    }
                                                    tv_box_bottom.setText("正在挖宝中...");
                                                }else {
                                                    Glide.with(getActivity()).load(imgStaticId).into(img_orb_show);
                                                    tv_box_bottom.setText("正在休息中...");
                                                }
                                                break;
                                        }*/

                                        switch (data.model.moneyState) {
                                            case "0":
                                                Glide.with(getActivity()).load(imgStaticId).into(img_orb_show);
                                                tv_box_bottom.setText("正在休息中...");

                                                cb_auto_hunt.setChecked(false);
                                                tv_auto_hunt_tip.setVisibility(View.GONE);
                                                break;
                                            case "1":
                                                Glide.with(getActivity()).load(imgDynamicId).into(img_orb_show);
                                                if (App.isAudioOpen) {
                                                    mMediaPlayer.start();
                                                    mMediaPlayer.setLooping(true);
                                                } else {
                                                    if (null != mMediaPlayer) {
//                                                        mMediaPlayer.release();
                                                    }
                                                }
                                                tv_box_bottom.setText("正在挖宝中...");
                                                switch (data.model.dayState){
                                                    case "0":
                                                        cb_auto_hunt.setChecked(true);
                                                        tv_auto_hunt_tip.setVisibility(View.VISIBLE);
                                                        break;
                                                    case "1":
                                                        cb_auto_hunt.setChecked(false);
                                                        tv_auto_hunt_tip.setVisibility(View.GONE);
                                                        break;
                                                }
                                                break;
                                        }


                                    }
                                    tv_box_top.setVisibility(View.GONE);
                                    tv_box_bottom.setVisibility(View.VISIBLE);
                                    tv_box_bottom_true.setVisibility(View.GONE);
                                    img_box_bg.setVisibility(View.GONE);
                                    img_orb_show.setVisibility(View.VISIBLE);

                                    isBoxShow = false;
                                }

                                StringBuffer noticeStr = new StringBuffer();
                                List<String> notice_list = new ArrayList<>();
                                List<Bean.BroadcastNotice> broadcastNoticeList = data.model.noticeList;
                                for (int i = 0; i < broadcastNoticeList.size(); i++) {
                                    noticeStr.append(broadcastNoticeList.get(i).title);
                                    noticeStr.append("                                                                              ");
                                    notice_list.add(broadcastNoticeList.get(i).title);
                                }

                                List<Bean.PictureNotice> pictureNoticeList = data.model.notice;

                                ImageLoadUtil.showImage(getActivity(), UriUtil.ip+pictureNoticeList.get(0).photo, img_get_detail, R.mipmap.advertising);
                                url = pictureNoticeList.get(0).url;

                                tv_hunt_broadcast.setText(noticeStr);
                                tv_hunt_broadcast.setFocusable(true);
                                tv_hunt_broadcast.setFocusableInTouchMode(true);
                                tv_hunt_broadcast.requestFocus();

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



    /**
     * App07 > 开启或者关闭自动挖矿
     */
    private void switchState(final String token, final String type) {
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.switchStateData(token, type, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            cb_auto_hunt.setEnabled(true);
                            img_orb_show.setClickable(true);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                switch (moneyState){
                                    case "1":
                                        dayState = "0";
                                        /*cb_auto_hunt.setChecked(true);
                                        tv_auto_hunt_tip.setVisibility(View.VISIBLE);
                                        Glide.with(getActivity()).load(imgDynamicId).into(img_orb_show);
                                        tv_box_bottom.setText("正在挖宝中。。");
                                        if (App.isAudioOpen && !isBoxShow) {
                                                mMediaPlayer = MediaPlayer.create(getActivity(),audioId);
                                                mMediaPlayer.start();
                                                mMediaPlayer.setLooping(true);
                                        }*/
                                        break;
                                    case "0":
                                        dayState = "1";
//                                        cb_auto_hunt.setChecked(false);
                                        /*tv_auto_hunt_tip.setVisibility(View.GONE);
                                        Glide.with(getActivity()).load(imgStaticId).into(img_orb_show);
                                        tv_box_bottom.setText("正在休息中。。");
                                        if (App.isAudioOpen  && !isBoxShow){
                                            if (null != mMediaPlayer){
                                                mMediaPlayer.reset();
                                            }
                                        }*/
                                        break;
                                }
                                homePage(App.token);
                            }else {
                                switch (moneyState) {
                                    case "1":
                                    switch (dayState) {
                                        case "1":
                                            cb_auto_hunt.setChecked(false);
                                            break;
                                        case "0":
                                            cb_auto_hunt.setChecked(true);
                                            break;
                                    }
                                    break;
                                    case "0":
                                        cb_auto_hunt.setChecked(false);
                                        break;
                                }
                                Log.i("1111111", "onSuccess: "+ dayState);
                                toast(data.message);
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
     * App16 > 打开箱子
     */
    private void openBox(final String token, final String oid) {
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.openBoxData(token, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            cb_auto_hunt.setEnabled(true);
                            img_orb_show.setClickable(true);

                            Bean.OpenBox data = new Gson().fromJson(result, Bean.OpenBox.class);
                            if (data.status == 1){
                                Glide.with(getActivity()).load(imgBoxOpen).into(img_box_bg);
//                                Glide.with(getActivity()).load(R.mipmap.light_open).into(img_box_bg);

                                cb_auto_hunt.setEnabled(false);
                                img_orb_show.setClickable(false);

                                if (App.isAudioOpen) {
                                    mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.box);
                                    mMediaPlayer.start();
                                }else {
//                                    if (null != mMediaPlayer){
//                                        mMediaPlayer.reset();
//                                    }
                                }

                                tv_box_top.setText("恭喜您获得积分");
                                tv_box_bottom_true.setText("+"+ StringUtil.doubleToString(data.number) +"积分");
                                tv_box_bottom_true.setTextSize(20);
                                tv_box_bottom_true.setTextColor(getActivity().getResources().getColor(R.color.text_blue2));

                                isBoxOpen = true;

                                cb_auto_hunt.setEnabled(true);
                                img_orb_show.setClickable(true);

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
     * App38 > 一天开启或者关闭自动挖矿
     */
    private void switchDayState(final String token, final String type) {
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.switchDayStateData(token, type, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                homePage(App.token);
                            }else {
                                toast(data.message);
                            }
                            img_orb_show.setClickable(true);
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
