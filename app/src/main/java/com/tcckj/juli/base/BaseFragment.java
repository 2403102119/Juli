package com.tcckj.juli.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tcckj.juli.R;
import com.tcckj.juli.thread.HttpInterface;

/**
 * 首页的Fragment
 * <p>
 * 首页
 */
public class BaseFragment extends Fragment implements View.OnClickListener {
    protected HttpInterface httpInterface;

    protected RelativeLayout top;
    protected LinearLayout back;
    private TextView backText;
    public TextView title;
    private LinearLayout rightBtnLayer;
    protected ImageView imgSetting;
    private ImageView imgShouCang;
    private ImageView imgShare;
    public TextView rightTxt;
    public SmartRefreshLayout mySmart;
    private FrameLayout container;
    private View rootView;
    protected ImageView img_back;

    protected Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity=getActivity();
        httpInterface = new HttpInterface(activity);
        rootView = inflater.inflate(R.layout.activity_base, container, false);

        top = findView(R.id.top);
        back = findView(R.id.back);
        backText = findView(R.id.backText);
        title = findView(R.id.title);
        rightBtnLayer = findView(R.id.rightBtnLayer);
        imgSetting = findView(R.id.imgSetting);
        imgShouCang = findView(R.id.imgShouCang);
        imgShare = findView(R.id.imgShare);
        rightTxt = findView(R.id.rightTxt);
        mySmart = findView(R.id.mySmart);
        img_back = findView(R.id.img_back);
        mySmart.setEnableAutoLoadmore(false);
        this.container = findView(R.id.container);
        back.setOnClickListener(this);

        top.setVisibility(View.GONE);

        initView(rootView);
        initListener();
        initData();

        return rootView;
    }


    protected void initView(View view) {

    }

    protected void initData() {

    }

    protected void initListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                activity.finish();
                break;
        }
    }

    protected void setContainer(int layoutId){
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutId,container,true);
    }

    protected void setTopTitle(String title){
        this.title.setText(title);
    }

    /**
     * 弹吐司
     */
    public void toast(Object message) {
        if (getActivity()!=null) {
            Toast toast = Toast.makeText(getActivity(), message + "", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void finishRefresh(){
        mySmart.finishRefresh();
        mySmart.finishLoadmore();
    }

    /**
     * findViewById
     */
    public <T extends View> T findView(int resId) {
        return (T) this.rootView.findViewById(resId);
    }
}







