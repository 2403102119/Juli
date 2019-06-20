package com.tcckj.juli.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;

public class WebShow2Activity extends BaseActivity {
    WebView web_show;
    private String url = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_web_show2);

        web_show = findView(R.id.web_show);

        web_show.getSettings().setJavaScriptEnabled(true);
        web_show.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            url = getIntent().getStringExtra("url");
            web_show.loadUrl(url);
        }
    }
}
