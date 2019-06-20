package com.tcckj.juli;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.util.SPUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * kylin on 2017/12/12.
 */

public class App extends Application {
    public static Map<String, Long> map;

    public static int quality= 100;    //默认图片质量
    public static String token = "";

    public static boolean islogin=false;    //是否登录
    public static boolean isFirstLogin = true;     //是否是首次登录
    public static boolean isRememberPassword;       //是否记住密码
    public static boolean isAudioOpen;              //音频是否开启

    public static String lastAccount;       //上次登录账号
    public static String lastPassword;      //上次登录密码
    public static long lastUploadTime;    //上次上传支付图片的时间

    public static Bean.PersonalInfo personalInfo;        //用户的个人信息
    /**
     * 维护Activity 的list
     */
    private static List<Activity> mActivitys = Collections.synchronizedList(new LinkedList<Activity>());

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityListener();

        islogin = (boolean) SPUtil.getData(this,"islogin",false);
        isFirstLogin = (boolean) SPUtil.getData(this,"isFirstLogin", true);
        isRememberPassword = (boolean) SPUtil.getData(this,"isRememberPassword", false);
        isAudioOpen = (boolean) SPUtil.getData(this,"isAudioOpen", true);
        lastAccount = (String) SPUtil.getData(this,"lastAccount", "");
        lastPassword = (String) SPUtil.getData(this,"lastPassword", "");
        personalInfo = (Bean.PersonalInfo) SPUtil.getBeanFromSp(this,"personal","personalInfo");
        token = (String) SPUtil.getData(this,"token", "");
        lastUploadTime = (long) SPUtil.getData(this,"lastUploadTime", 0l);

        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "5b04e023a40fa3728d00001b", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        //开启ShareSDK debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.gray, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        /*PlatformConfig.setWeixin("wxef582ac741b222a3","d4db62de24d27748ae6a34030fe3d74a");
        PlatformConfig.setQQZone("1106701003","3YJoucb85kutzgnQ");*/
    }

    public static boolean isInstall(Context context,String pkg) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
//                Log.e("pn","pn="+pn);
                if (pn.equalsIgnoreCase(pkg)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (!(activity.getClass().equals(MainActivity.class)))
                activity.finish();
        }
        mActivitys.clear();
    }

    /**
     * 结束所有Activity
     */
    public static void finishRealAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
                activity.finish();
        }
        mActivitys.clear();
    }

    /**
     * 得到MainActivity
     */
    public static MainActivity getMainActivity() {
        if (mActivitys == null) {
            return null;
        }
        for (Activity activity : mActivitys) {
            if ((activity.getClass().equals(MainActivity.class)))
                return (MainActivity)activity;
        }
        return null;
    }

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
    }


    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    /*
                     *  监听到 Activity创建事件 将该 Activity 加入list,MainActivity主页面不加入
                     */
//                    if (!(activity.getClass().equals(MainActivity.class)))
                    pushActivity(activity);

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (null==mActivitys&&mActivitys.isEmpty()){
                        return;
                    }
                    if (mActivitys.contains(activity)){
                        /*
                         *  监听到 Activity销毁事件 将该Activity 从list中移除
                         */
                        popActivity(activity);
                    }
                }
            });
        }
    }
}
