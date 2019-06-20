package com.tcckj.juli.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络连接工具类
 */
public class NetUtil {
    /**
     * 是否连接网络
     *
     * @param context
     * @return
     */
    public static boolean isNetWorking(Context context) {
        if (context==null)
            return false;
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }


}
