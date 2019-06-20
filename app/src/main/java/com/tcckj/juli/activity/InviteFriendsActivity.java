package com.tcckj.juli.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
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
 * 邀请好友界面
 */
public class InviteFriendsActivity extends BaseActivity {
    TextView tv_referral_links,tv_first_friends_number,tv_first_friends_jifen,tv_second_friends_number,
            tv_second_friends_jifen;

    RelativeLayout rl_to_first_friend,rl_to_second_friend;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_invite_friends);

        title.setText("邀请好友");

        tv_referral_links = findView(R.id.tv_referral_links);
        tv_first_friends_number = findView(R.id.tv_first_friends_number);
        tv_first_friends_jifen = findView(R.id.tv_first_friends_jifen);
        tv_second_friends_number = findView(R.id.tv_second_friends_number);
        tv_second_friends_jifen = findView(R.id.tv_second_friends_jifen);
        rl_to_first_friend = findView(R.id.rl_to_first_friend);
        rl_to_second_friend = findView(R.id.rl_to_second_friend);
    }

    @Override
    protected void initListener() {

        back.setOnClickListener(this);
        tv_referral_links.setOnClickListener(this);
//        rl_to_first_friend.setOnClickListener(this);
//        rl_to_second_friend.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        findList(App.token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_referral_links:
                startActivity(new Intent(InviteFriendsActivity.this, ReferralLinksActivity.class));
                break;
            case R.id.rl_to_first_friend:
                startActivity(new Intent(InviteFriendsActivity.this, FirstFriendsActivity.class));
                break;
//            case R.id.rl_to_second_friend:
//                startActivity(new Intent(InviteFriendsActivity.this, SecondFriendsActivity.class));
//                break;
        }
    }


    /**
     * App32 > 分享页面数据
     */
    private void findList(final String token) {
        if (NetUtil.isNetWorking(InviteFriendsActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.findListData(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.FriendsMsg data = new Gson().fromJson(result, Bean.FriendsMsg.class);
                            if (data.status == 1){
                                tv_first_friends_number.setText(data.oneSize+"");
                                tv_first_friends_jifen.setText(StringUtil.doubleToString(data.oneMoney));
                                tv_second_friends_number.setText(data.twoSize+"");
                                tv_second_friends_jifen.setText(StringUtil.doubleToString(data.twoMoney));
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


}
