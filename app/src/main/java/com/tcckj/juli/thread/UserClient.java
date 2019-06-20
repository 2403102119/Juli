package com.tcckj.juli.thread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tcckj.juli.App;
import com.tcckj.juli.MainActivity;
import com.tcckj.juli.R;
import com.tcckj.juli.activity.LoginActivity;
import com.tcckj.juli.util.MUIToast;
import com.tcckj.juli.util.SPUtil;
import com.tcckj.juli.util.ZipUtils;
import com.tcckj.juli.view.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * kylin on 2017/6/26.
 */

public class UserClient {

    public static final int SHOW_LOADING = 0x10;
    public static final int DIMISS_LOADING = 0x11;

    private Class<?> mClass;
    private HashMap<String, String> params;
    private HashMap<String, String> headers;
    private List<Pair<String, File>> filePairList;

    private String urlLink;

    private int responseCode;
    private String message;

    private String response = "";

    public String httpurl;

    //private Context context;


    final Handler mDeliver = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_LOADING:
                    if (loadingDialog != null) {
                        loadingDialog.show();
                    }
                    break;
                case DIMISS_LOADING:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    break;
            }
        }
    };
    private File file;
    private Response fileResponse;
    private LoadingDialog loadingDialog;

    public enum RequestMethod {
        POST, POSTFILE, GET, GETFILE, GETTASKFILE
    }

    public UserClient(String url, Class<?> mClass) {
        this.urlLink = url;
        params = new HashMap<>();
        headers = new HashMap<>();
        filePairList = new ArrayList<>();
        this.mClass = mClass;
    }

    public UserClient(String url) {
        this.urlLink = url;
        params = new HashMap<>();
        headers = new HashMap<>();
        filePairList = new ArrayList<>();
        this.mClass = mClass;
    }

    public void AddParam(String name, String value) {
        params.put(name, value);
    }

    public void AddHeader(String name, String value) {
        headers.put(name, value);
    }

    public void addFiles(Pair<String, File> file) {
        filePairList.add(file);
    }

    /**
     * 同步调用，无结果回调。调用方不能再主线程中，需开子线程。
     *
     * @param method
     * @throws IOException
     */
    public void executeWithOkhttp(RequestMethod method) throws IOException,
            SocketTimeoutException {
        switch (method) {
            case GET:
                Response responseGet = OkHttpUtils.get().url(urlLink).headers(headers).params(params)
                        .build().execute();
                if (responseGet.isSuccessful()) {
                    this.response = getStringFromInputStream(responseGet.body()
                            .byteStream());
                }
                break;
            case POST:
                Response responsePost = OkHttpUtils.post().url(urlLink).headers(headers).params(params).build().execute();
                if (responsePost.isSuccessful()) {
                    this.response = getStringFromInputStream(responsePost.body()
                            .byteStream());
                }
                break;
            case POSTFILE:
                PostFormBuilder formBuilder = OkHttpUtils.post();
                if (filePairList.size() > 0) {
                    for (int i = 0; i < filePairList.size(); i++) {
                        Pair<String, File> filePair = filePairList.get(i);
                        formBuilder.addFile(filePair.first, filePair.second.getName(), filePair.second);
                    }
                }

                Response responsePostFile = formBuilder.url(urlLink).headers(headers).params(params).build().execute();
                if (responsePostFile.isSuccessful()) {
                    this.response = getStringFromInputStream(responsePostFile
                            .body().byteStream());
                }
            case GETFILE:
                Response responseGetFile = OkHttpUtils.post().url(urlLink).headers(headers).params(params).build().execute();

                if (responseGetFile.isSuccessful()) {
                    String fileOutPath = getFileFromInputStream(responseGetFile.body().byteStream());
                    if (fileOutPath != null) {
                        File file = new File(fileOutPath);
                        this.file = file;
                    }
                }
                break;
            case GETTASKFILE:
                Response responseGetTaskFile = OkHttpUtils.post().url(urlLink).headers(headers).params(params).build().execute();

                if (responseGetTaskFile.isSuccessful()) {
                    this.fileResponse = responseGetTaskFile;
                }
                break;
            default:
                break;
        }

    }


    private String getFileFromInputStream(InputStream byteStream) {
        File file = null;
        String fileName = urlLink.substring(urlLink.lastIndexOf("/") + 1);
        String outPath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String dirPath = Environment.getExternalStorageDirectory() + "/zywx/";
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            try {
                String filePath = dirPath + fileName;
                file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                OutputStream os = new FileOutputStream(file);
                int len = 0;
                byte[] bys = new byte[1024];
                while ((len = byteStream.read(bys)) != -1) {
                    os.write(bys, 0, len);
                }
                os.close();
                byteStream.close();
                outPath = ZipUtils.getInstance().unZipFiles(filePath, dirPath);
            } catch (IOException e) {
//                MLog.e("--RestClient--", "e:", e);
                e.printStackTrace();
            }
        }
        return outPath;

    }

    private boolean show = true;

    public void executePost(final MApiResultCallback callback, final LoadingDialog loadingDialog, final Context context) {

        //this.context = context;
//        params.put("token", App.token + "");
        Log.e("params", params + "");
        show = true;
        if (loadingDialog != null) {
            mDeliver.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (show) {
                        loadingDialog.show();
                    }
                    //mDeliver.sendEmptyMessageDelayed(SHOW_LOADING,500);
                }
            }, 500);
        }


        /*
            这里去掉了啊啊啊啊啊啊啊啊啊啊


    //OkHttpUtils.getInstance().cancelTag(urlLink);


         */

        //OkHttpUtils.getInstance().cancelTag(urlLink);
        Log.e("urlLink",urlLink+"");
        OkHttpUtils.post().url(urlLink).params(params).headers(headers).tag(urlLink)
                .build().connTimeOut(60 * 1000).readTimeOut(60 * 1000).writeTimeOut(60 * 1000)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        show = false;
                        Log.e("resposeData", response.toString());
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            mDeliver.post(new Runnable() {
                                @Override
                                public void run() {
                                    //mDeliver.removeMessages(SHOW_LOADING);
                                    loadingDialog.dismiss();
                                }
                            });
                        }

                        if (checkUserLoginState(response)) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.has("status")) {
                                    int status = (int) jsonObject.optInt("status", -1);
                                    Log.e("status", status + "");
                                    if (status != 9) {
                                        callback.onSuccess(response);
                                    } else{
                                        sendRedEnvelopes((Activity) context, "", "", "", "");
                                    }
                                } else {
                                    callback.onFail(response);
                                }
                            } catch (JSONException e) {
                                callback.onFail(response);
                                e.printStackTrace();
                            }
                        } else {
                            callback.onFail(HandleResponseCode.RESPONSE_NOTLOGIN);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception exception) {
                        exception.printStackTrace();
                        show = false;
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            mDeliver.post(new Runnable() {
                                @Override
                                public void run() {
                                    //mDeliver.removeMessages(SHOW_LOADING);
                                    loadingDialog.dismiss();
                                }
                            });
                        }
                        mDeliver.post(new Runnable() {
                            @Override
                            public void run() {
                                MUIToast.toast(context, R.string.connect_failed_toast);
                            }
                        });
                        callback.onError(call, exception);
                    }
                });
    }

    /**
     * 需要根据执行结果继续下一步操作的执行本方法
     *
     * @param requestMethod
     * @param callback      结果回调
     */
    public void executeWithOkHttpCallback(RequestMethod requestMethod, final MApiResultCallback callback, final LoadingDialog loadingDialog) {

        //params.put("token", App.token + "");

        switch (requestMethod) {
            case GET:
                OkHttpUtils.get().url(urlLink).headers(headers).params(params)
                        .build().connTimeOut(1 * 60 * 1000).readTimeOut(1 * 60 * 1000).writeTimeOut(1 * 60 * 1000).execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        if (checkUserLoginState(response)) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.e("json", response.toString());
                                if (jsonObject.has("status")) {
                                    int status = jsonObject.optInt("status", -1);
                                    if (status == 1) {
                                        callback.onSuccess(response);
                                    } else if (status == 9) {
                                        callback.onTokenError(response);


                                    } else {
                                        callback.onFail(response);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            callback.onFail(HandleResponseCode.RESPONSE_NOTLOGIN);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception exception) {
                        callback.onError(call, exception);
                    }
                });
                break;
            case POST:
                OkHttpUtils.post().url(urlLink).params(params).headers(headers)
                        .build().connTimeOut(60 * 1000).readTimeOut(60 * 1000).writeTimeOut(60 * 1000)
                        .execute(new StringCallback() {
                            @Override
                            public void onResponse(String response) {

                                Log.e("resposeData", response.toString());

                                if (checkUserLoginState(response)) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(
                                                response);
                                        if (jsonObject.has("status")) {
                                            int status = (int) jsonObject.optInt("status", -1);
                                            Log.e("status", status + "");
                                            if (status == 1) {
                                                callback.onSuccess(response);
                                            } else if (status == 9) {
                                                callback.onTokenError(response);
                                            } else {
                                                callback.onFail(response);
                                            }
                                        } else {
                                            callback.onFail(response);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    callback.onFail(HandleResponseCode.RESPONSE_NOTLOGIN);
                                }
                            }

                            @Override
                            public void onError(Call call, Exception exception) {
                                exception.printStackTrace();
                                callback.onError(call, exception);
                            }
                        });
                break;
            case POSTFILE:
                PostFormBuilder formBuilder = OkHttpUtils.post();
                if (filePairList.size() > 0) {
                    for (int i = 0; i < filePairList.size(); i++) {
                        Pair<String, File> filePair = filePairList.get(i);
                        formBuilder.addFile(filePair.first, filePair.second.getName(), filePair.second);
                    }
                }
                formBuilder
                        .url(urlLink)
                        .params(params)
                        .headers(headers)
                        .build().connTimeOut(60 * 1000).readTimeOut(60 * 1000).writeTimeOut(60 * 1000).execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
//                        MLog.d("ResetClient", "Fileresponse:" + response.toString());
                        if (checkUserLoginState(response)) {
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        response);
                                if (jsonObject.has("status")) {
                                    int status = jsonObject.optInt("status", -1);
                                    if (status == 1) {
                                        callback.onSuccess(response);
                                    } else if (status == 9) {
                                        callback.onTokenError(response);
                                    } else {
                                        callback.onFail(response);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            callback.onFail(HandleResponseCode.RESPONSE_NOTLOGIN);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception exception) {
//                        MLog.d("ResetClient", "response:" + response.toString());
                        callback.onError(call, exception);
                    }

                    @Override
                    public void inProgress(float f) {
                        callback.inProgress(f);
                    }
                });

                break;
            default:
                break;
        }

    }

    public static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        is.close();
        String state = bos.toString();
        bos.close();
        return state;
    }


    public String getHttpurl() {
        return httpurl;
    }

    public void setHttpurl(String httpurl) {
        this.httpurl = httpurl;
    }

    public String getResponse() {
        if (checkUserLoginState(response)) {
            return response;
        }
        return "";
    }

    private boolean checkUserLoginState(String response) {
//        MLog.i("--response--", "" + response);
        if (!TextUtils.isEmpty(response) && response.indexOf("DOCTYPE") > -1) {
//			AppContext.getInstance().autoLogin();
            return false;
        }
        return true;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setUrl(String url) {
        this.urlLink = url;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Response getFileResponse() {
        return fileResponse;
    }

    public void setFileResponse(Response fileResponse) {
        this.fileResponse = fileResponse;
    }


    /**
     * 其他设备登录提示
     */
    public void sendRedEnvelopes(final Activity ctx, String title, String content, String left, String right) {
        LayoutInflater inflater = ctx.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_token_error, null);
        TextView dialog_content = (TextView) view.findViewById(R.id.tv_content);
        TextView dialog_left = (TextView) view.findViewById(R.id.tv_left);
        TextView dialog_right = (TextView) view.findViewById(R.id.tv_right);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true); //设置点击对话框外关闭对话框
        dialog_content.setText("异常登录");
        dialog_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                SPUtil.saveData(ctx, "token", "");
                SPUtil.saveData(ctx,"islogin",false);
                App.token="";
                App.islogin=false;
                Intent intent = new Intent();

                intent.setClass(ctx, LoginActivity.class);
                intent.putExtra("activity", "main");
                ctx.startActivity(intent);

//                //关闭adapter所在activity
//                if(Activity.class.isInstance(ctx))
//                {
//                    //关闭adapter所在activity
//                    Activity activity = (Activity)ctx;
//                    activity.finish();
//                }
            }
        });
        dialog.show();
    }
}