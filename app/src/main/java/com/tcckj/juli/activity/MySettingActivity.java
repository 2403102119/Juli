package com.tcckj.juli.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcckj.juli.App;
import com.tcckj.juli.MainActivity;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.CacheUtil;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.SPUtil;
import com.tcckj.juli.util.Tools;

import okhttp3.Call;

/**
 * 账户设置界面
 */
public class MySettingActivity extends BaseActivity {
    LinearLayout ll_change_login_password,ll_change_pay_password,ll_about_juli,ll_check_update;
    TextView tv_unlogin,tv_cache_size;
    CheckBox cb_audio_control;

    PopupWindow pop;
    LinearLayout ll_popup,ll_clear_cache;

    private String newVersion = "1";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_my_setting);

        title.setText("账户设置");
        ll_change_login_password = findView(R.id.ll_change_login_password);
        ll_change_pay_password = findView(R.id.ll_change_pay_password);
        ll_about_juli = findView(R.id.ll_about_juli);
        ll_clear_cache = findView(R.id.ll_clear_cache);
        ll_check_update = findView(R.id.ll_check_update);
        tv_unlogin = findView(R.id.tv_unlogin);
        tv_cache_size = findView(R.id.tv_cache_size);
        cb_audio_control = findView(R.id.cb_audio_control);

        if (App.isAudioOpen){
            cb_audio_control.setChecked(true);
        }else {
            cb_audio_control.setChecked(false);
        }
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        ll_change_login_password.setOnClickListener(this);
        ll_change_pay_password.setOnClickListener(this);
        ll_about_juli.setOnClickListener(this);
        ll_clear_cache.setOnClickListener(this);
        ll_check_update.setOnClickListener(this);
        tv_unlogin.setOnClickListener(this);
        cb_audio_control.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        try {
            tv_cache_size.setText(CacheUtil.getTotalCacheSize(MySettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.ll_change_login_password:
                Intent intent1 = new Intent(MySettingActivity.this, ChangePasswordActivity.class);
                intent1.putExtra("password_type", "login");
                startActivity(intent1);
                break;
            case R.id.ll_change_pay_password:
                Intent intent2 = new Intent(MySettingActivity.this, ChangePasswordActivity.class);
                intent2.putExtra("password_type", "pay");
                startActivity(intent2);
                break;
            case R.id.ll_about_juli:
                startActivity(new Intent(MySettingActivity.this, AboutJuliActivity.class));
                break;
            case R.id.tv_unlogin:
                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        MySettingActivity.this, R.anim.activity_translate_in));
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.cb_audio_control:
                if (App.isAudioOpen){
                    cb_audio_control.setChecked(false);
                    App.isAudioOpen = false;
                    SPUtil.saveData(MySettingActivity.this, "isAudioOpen", false);
                }else {
                    cb_audio_control.setChecked(true);
                    App.isAudioOpen = true;
                    SPUtil.saveData(MySettingActivity.this, "isAudioOpen", true);
                }
                break;
            case R.id.ll_clear_cache:
                CacheUtil.clearAllCache(MySettingActivity.this);
                toast("清理成功");
                try {
                    tv_cache_size.setText(CacheUtil.getTotalCacheSize(MySettingActivity.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_check_update:
                getNewVersion();
                break;
        }
    }


    // 获取更新版本号
    private void getVersion(final int vision) {
//         {"data":{"content":"其他bug修复。","id":"2","api_key":"android",
//         // "version":"2.1"},"msg":"获取成功","status":1}
        String data = "";
        //网络请求获取当前版本号和下载链接
        //实际操作是从服务器获取
        //demo写死了

        String newversion = newVersion;//更新新的版本号

        Log.i("11111111", "getVersion: " + newversion);
        Log.i("11111111", "getVersion: " + getApplicationContext().getPackageName() + ".provider");

        String content = "确定去更新吗？";
       /* String content = "\n" +
                "就不告诉你我们更新了什么-。-\n" +
                "\n" +
                "----------万能的分割线-----------\n" +
                "\n" +
                "(ㄒoㄒ) 被老板打了一顿，还是来告诉你吧：\n" +

                "1.下架商品误买了？恩。。。我搞了点小动作就不会出现了\n" +
                "2.侧边栏、弹框优化 —— 这个你自己去探索吧，总得留点悬念嘛-。-\n";//更新内容*/
        String url = "http://openbox.mobilem.360.cn/index/d/sid/3429345";//安装包下载地址

        double newversioncode = Double
                .parseDouble(newversion);
        int cc = (int) (newversioncode);

        System.out.println(newversion + "v" + vision + ",,"
                + cc);
        if (cc != vision) {
            if (vision < cc) {
                System.out.println(newversion + "v"
                        + vision);
                // 版本号不同
                ShowDialog(vision, newversion, content, url);


            }
        }
    }


    /**
     * 升级系统
     *
     * @param content
     * @param url
     */
    private void ShowDialog(int vision, String newversion, String content,
                            final String url) {

        new android.app.AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("https://fir.im/ugm7");
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    /**
     * 初始化窗口
     */
    private void showPopupWindow() {
        pop = new PopupWindow(MySettingActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
                null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_yikoujia);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_tuangou);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_jingpai);
        bt1.setText("您确认要退出账号么？");
        bt1.setTextColor(getResources().getColor(R.color.hintColor));
        bt1.setTextSize(15);
        bt2.setText("退出");
        bt2.setTextColor(getResources().getColor(R.color.text_red));
        bt2.setTextSize(20);
        bt3.setText("取消");
        bt3.setTextColor(getResources().getColor(R.color.nomalText));
        bt3.setTextSize(20);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.saveData(MySettingActivity.this,"islogin",false);
                SPUtil.saveBean2Sp(MySettingActivity.this, new Bean.PersonalInfo(), "personal", "personalInfo");
                App.islogin = false;
                pop.dismiss();
                ll_popup.clearAnimation();
                startActivity(new Intent(MySettingActivity.this, LoginActivity.class));
                App.finishRealAllActivity();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }


    /*
   获取最新版本号
    */
    private void getNewVersion(){
        if (NetUtil.isNetWorking(MySettingActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.updateTypeData("1", new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.VersionCode data = new Gson().fromJson(result, Bean.VersionCode.class);
                            if (1 == data.status){
                                newVersion = data.type;


                                // 获取本版本号，是否更新
                                int vision = Tools.getVersion(MySettingActivity.this);
                                getVersion(vision);
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
