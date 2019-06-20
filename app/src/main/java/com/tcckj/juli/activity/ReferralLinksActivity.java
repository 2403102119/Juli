package com.tcckj.juli.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.ImageLoadUtil;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.StringUtil;
import com.tcckj.juli.util.UriUtil;

import okhttp3.Call;

/**
 * 推广链接界面
 */
public class ReferralLinksActivity extends BaseActivity {
    ImageView img_links_back,img_my_code;
    TextView tv_my_code,tv_invite_link;

    private String linkUrl;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_referral_links);

        top.setVisibility(View.GONE);
        img_links_back = findView(R.id.img_links_back);
        img_my_code = findView(R.id.img_my_code);
        tv_my_code = findView(R.id.tv_my_code);
        tv_invite_link = findView(R.id.tv_invite_link);
    }

    @Override
    protected void initListener() {
        img_links_back.setOnClickListener(this);
        tv_invite_link.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getQRcode(App.token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_links_back:
                finish();
                break;
            case R.id.tv_invite_link:
                if (StringUtil.isSpace(linkUrl)) {
                    toast("未获取到邀请链接");
                }else {
                    ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Label", linkUrl);
                    mClipboardManager.setPrimaryClip(clipData);
                    toast("已复制");
                }
                break;
        }
    }



    /**
     * App16 > 打开箱子
     */
    private void getQRcode(final String token) {
        if (NetUtil.isNetWorking(ReferralLinksActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getQRcodeData(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.QCode data = new Gson().fromJson(result, Bean.QCode.class);
                            if (data.status == 1){
                                tv_my_code.setText(data.yqm);
                                linkUrl = data.zcUrl;
                                ImageLoadUtil.showImage(ReferralLinksActivity.this, UriUtil.ip + data.ewmPath, img_my_code);
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
