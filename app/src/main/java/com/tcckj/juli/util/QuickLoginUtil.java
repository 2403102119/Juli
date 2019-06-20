package com.tcckj.juli.util;

import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.thread.HttpInterface;

/**
 * kylin on 2018/1/31.
 */

public class QuickLoginUtil {
    private BaseActivity activity;
    private HttpInterface httpInterface;
    private boolean isBind;
    private String token;


    /*
    给用户设置别名
     */
    private static final String TAG = "获取.成功";
    private static final int MSG_SET_ALIAS = 1001;
   /* private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(activity.getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    SPUtil.saveData(activity,"isBindAlias",true);

                    App.isBindAlias = true;
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                case 2008:
                    logs = "没有安装应用";
                    Log.i(TAG, logs);
//                    activity.toast("没有安装应用");
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
//            activity.toast(logs);
        }
    };

*/
    public QuickLoginUtil(BaseActivity activity, HttpInterface httpInterface, boolean isBind, String token){
        this.activity=activity;
        this.httpInterface=httpInterface;
        this.isBind = isBind;
        this.token = token;
    }
/*

    public void wx() {
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, authListener);
    }

    public void qq() {
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.QQ, authListener);
    }

    private UMAuthListener authListener=new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Log.e(share_media.getName(),"map="+map);
            final String no=map.get("openid");
            Log.e("no","--------------");


            Log.e("no",no);

            Log.e("no","--------------");


            //Log.e("n00000000000000",no);

            if (isBind){
                if ("qq".equals(share_media.getName())){
                    Log.e("no","-------qq-------");
                    qqBind(token, no);
                }else {
                    wxBind(token, no);
                }
            }else {
                if ("qq".equals(share_media.getName())){
                    Log.e("no","-------qq-------");
                    qqLogin(no);
                }else {
                    wxLogin(no);
                }
            }

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.e(share_media.getName(),"i="+i+",throwable="+throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.e(share_media.getName(),"i="+i);
        }
    };
*/


/*
    *//*
    duorou83-绑定qq号
     *//*
    public void qqBind(final String token, final String no) {
        httpInterface.qqBind(token, no, new MApiResultCallback() {
            @Override
            public void onSuccess(final String result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            int status=jsonObject.optInt("status");
                            String message=jsonObject.optString("message");
                            if (status==1){
                                MUIToast.show(activity,jsonObject.optString("message"));
                                activity.finish();
                            }
                            else {
                                MUIToast.show(activity,jsonObject.optString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFail(String response) {
                Log.e("onFail", response);
            }

            @Override
            public void onError(Call call, Exception exception) {
                Log.e("onError", exception.getMessage()+"");
            }

            public void onTokenError(String response) {

            }
        });
    }



    *//*
    duorou82-绑定微信号
     *//*
    public void wxBind(final String token, final String no) {
        httpInterface.wxBind(token, no, new MApiResultCallback() {
            @Override
            public void onSuccess(final String result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            int status=jsonObject.optInt("status");
                            String message=jsonObject.optString("message");
                            if (status==1){
                                MUIToast.show(activity,jsonObject.optString("message"));
                                activity.finish();
                            }
                            else {
                                MUIToast.show(activity,jsonObject.optString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFail(String response) {
                Log.e("onFail", response);
            }

            @Override
            public void onError(Call call, Exception exception) {
                Log.e("onError", exception.getMessage()+"");
            }

            public void onTokenError(String response) {

            }
        });
    }



    public void wxLogin(final String no) {
        httpInterface.wxLogin(no, new MApiResultCallback() {
            @Override
            public void onSuccess(final String result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            int status=jsonObject.optInt("status");
                            String phoneBind=jsonObject.optString("wechatBind");
                            String token=jsonObject.optString("token");
                            String phone=jsonObject.optString("phone");
                            String oid=jsonObject.optString("oid");
                            if (status==1){
                                if ("1".equals(phoneBind)){
                                    SPUtil.saveData(activity,"token",token);
                                    SPUtil.saveData(activity,"phone",phone);
                                    SPUtil.saveData(activity,"islogin",true);
                                    SPUtil.saveData(activity,"memberId",oid);

                                    // 调用 Handler 来异步设置别名
                                    mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, oid));

                                    App.phone=phone;
                                    App.token=token;
                                    App.islogin=true;
                                    App.login.oid = oid;
                                    activity.finish();
                                }
                                else {
                                    Intent intent =new Intent(activity,BindPhoneActivity.class);
                                    intent.putExtra("wechatOpenid",no);
                                    intent.putExtra("flag",0);
                                    intent.putExtra("bindtype","bindphone");
                                    activity.startActivityForResult(intent,666);
                                }
                            }
                            else {
                                MUIToast.show(activity,jsonObject.optString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFail(String response) {
                Log.e("onFail", response);
            }

            @Override
            public void onError(Call call, Exception exception) {
                Log.e("onError", exception.getMessage()+"");
            }

            public void onTokenError(String response) {

            }
        });
    }

    public void qqLogin(final String no) {
        httpInterface.qqLogin(no, new MApiResultCallback() {
            @Override
            public void onSuccess(final String result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            int status=jsonObject.optInt("status");
                            String phoneBind=jsonObject.optString("qqBind");
                            String token=jsonObject.optString("token");
                            String phone=jsonObject.optString("phone");
                            String oid=jsonObject.optString("oid");
                            if (status==1){
                                if ("1".equals(phoneBind)){
                                    SPUtil.saveData(activity,"token",token);
                                    SPUtil.saveData(activity,"phone",phone);
                                    SPUtil.saveData(activity,"islogin",true);
                                    SPUtil.saveData(activity,"memberId",oid);

                                    // 调用 Handler 来异步设置别名
                                    mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, oid));
                                    App.phone=phone;
                                    App.token=token;
                                    App.islogin=true;
                                    App.login.oid = oid;
                                    activity.finish();
                                }
                                else {
                                    Intent intent =new Intent(activity,BindPhoneActivity.class);
                                    intent.putExtra("wechatOpenid",no);
                                    intent.putExtra("flag",1);
                                    intent.putExtra("bindtype","bindphone");
                                    activity.startActivityForResult(intent,666);
                                }
                            }
                            else {
                                MUIToast.show(activity,jsonObject.optString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFail(String response) {
                Log.e("onFail", response);
            }

            @Override
            public void onError(Call call, Exception exception) {
                Log.e("onError", exception.getMessage()+"");
            }

            public void onTokenError(String response) {

            }
        });
    }*/
}
