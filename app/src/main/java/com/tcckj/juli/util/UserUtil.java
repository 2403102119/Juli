package com.tcckj.juli.util;//package com.tckkj.duorou.util;
//
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.tckkj.duorou.App;
//import com.tckkj.duorou.R;
//import com.tckkj.duorou.base.BaseActivity;
//import com.tckkj.duorou.entity.Bean;
//
//import okhttp3.Call;
//
///**
// * kylin on 2018/1/31.
// */
//
//public class UserUtil {
//    private BaseActivity activity;
//    private HttpInterface httpInterface;
//
//    public UserUtil(BaseActivity activity, HttpInterface httpInterface){
//        this.activity=activity;
//        this.httpInterface=httpInterface;
//    }
//
//    //获得单个用户信息
//    public void getUserInformation(final String token,final boolean isFinish){
//        if (NetUtil.isNetWorking(activity)) {
//            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
//                @Override
//                public void run() {
//
//                    httpInterface.getUser(token, new MApiResultCallback() {
//                        @Override
//                        public void onSuccess(String result) {
//                            Log.e("onSuccess", result);
//                            Bean.LoginInfo info=new Gson().fromJson(result, Bean.LoginInfo.class);
//                            if (info!=null){
//                                App.login=info.model;
//                            }
//                            if (isFinish){
//                                activity.finish();
//                            }
//                        }
//
//                        @Override
//                        public void onFail(String response) {
//                            Log.e("获取.异常", response);
//                            if (isFinish){
//                                activity.finish();
//                            }
//                        }
//
//                        @Override
//                        public void onError(Call call, Exception exception) {
//                            Log.e("onError", call + "-----" + exception);
//                            if (isFinish){
//                                activity.finish();
//                            }
//                        }
//
//                        @Override
//                        public void onTokenError(String response) {
//                            Log.e("onTokenError", response);
//                            if (isFinish){
//                                activity.finish();
//                            }
//                        }
//                    });
//                }
//            });
//        } else {
//            activity.toast(R.string.system_busy);
//        }
//    }
//}
