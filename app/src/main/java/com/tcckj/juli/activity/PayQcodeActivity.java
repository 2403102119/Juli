package com.tcckj.juli.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.FileUtils;
import com.tcckj.juli.util.ImageLoadUtil;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.RGBLuminanceSource;
import com.tcckj.juli.util.SPUtil;
import com.tcckj.juli.util.StringUtil;
import com.tcckj.juli.util.UriUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import okhttp3.Call;

/*
付款二维码界面
 */
public class PayQcodeActivity extends BaseActivity {
    ImageView img_pay_qcode,img_pay_upload_picture;
    TextView tv_upload_sure,tv_long_press_tip;

    PopupWindow pop,rechargePop;
    LinearLayout ll_popup,ll_recharge_popup,ll_pay_qcode;

    String url;

    String photoImgliu;

    private static final int SAVE_SUCCESS = 0;//保存图片成功
    private static final int SAVE_FAILURE = 1;//保存图片失败
    private static final int SAVE_BEGIN = 2;//开始保存图片
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_BEGIN:
                    toast("开始保存图片...");
                    img_pay_upload_picture.setClickable(false);
                    break;
                case SAVE_SUCCESS:
                    toast("图片保存成功,请到相册查找");
                    img_pay_upload_picture.setClickable(true);
                    break;
                case SAVE_FAILURE:
                    toast("图片保存失败,请稍后再试...");
                    img_pay_upload_picture.setClickable(true);
                    break;
            }
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_pay_qcode);

        img_pay_qcode = findView(R.id.img_pay_qcode);
        img_pay_upload_picture = findView(R.id.img_pay_upload_picture);
        tv_upload_sure = findView(R.id.tv_upload_sure);
        tv_long_press_tip = findView(R.id.tv_long_press_tip);
        ll_pay_qcode = findView(R.id.ll_pay_qcode);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        tv_upload_sure.setOnClickListener(this);
        img_pay_qcode.setOnClickListener(this);

        img_pay_upload_picture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showRechargePopupWindow();
                ll_recharge_popup.startAnimation(AnimationUtils.loadAnimation(
                        PayQcodeActivity.this, R.anim.activity_translate_in));
                rechargePop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            if ("qcode".equals(getIntent().getStringExtra("show_type"))){
                title.setText("扫码付款");
//                img_pay_qcode.setImageResource(R.mipmap.pay_qcode);
                img_pay_qcode.setClickable(false);
                tv_upload_sure.setVisibility(View.GONE);

                getPhoto(App.token);
            }
            if ("upload".equals(getIntent().getStringExtra("show_type"))){
                title.setText("上传充值图片");
                img_pay_qcode.setImageResource(R.mipmap.add_picture_image);
                img_pay_qcode.setScaleType(ImageView.ScaleType.CENTER_CROP);
                img_pay_qcode.setClickable(true);
                tv_upload_sure.setVisibility(View.VISIBLE);
            }
            if ("jd_qcode".equals(getIntent().getStringExtra("show_type"))){
                title.setText("京东支付二维码");
                ll_pay_qcode.setBackgroundResource(R.mipmap.jingdong_bg);
                img_pay_qcode.setImageResource(R.mipmap.pay_qcode);
                img_pay_qcode.setVisibility(View.GONE);
                img_pay_upload_picture.setVisibility(View.VISIBLE);
                tv_upload_sure.setVisibility(View.GONE);
//                tv_long_press_tip.setVisibility(View.VISIBLE);

                url = UriUtil.ip + getIntent().getStringExtra("img_url");
                ImageLoadUtil.showImage(PayQcodeActivity.this, url, img_pay_upload_picture);
            }
            if ("alipay_qcode".equals(getIntent().getStringExtra("show_type"))){
                title.setText("支付宝二维码");
                ll_pay_qcode.setBackgroundResource(R.mipmap.alipay_bg);
                img_pay_qcode.setImageResource(R.mipmap.pay_qcode);
                img_pay_qcode.setVisibility(View.GONE);
                img_pay_upload_picture.setVisibility(View.VISIBLE);
                tv_upload_sure.setVisibility(View.GONE);
//                tv_long_press_tip.setVisibility(View.VISIBLE);

                url = UriUtil.ip + getIntent().getStringExtra("img_url");
                ImageLoadUtil.showImage(PayQcodeActivity.this, url, img_pay_upload_picture);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_upload_sure:
                if (App.islogin){
                    if (StringUtil.isSpace(photoImgliu)){
                        toast("请选择上传图片");
                    }else {
                        if (System.currentTimeMillis() - App.lastUploadTime > 120000){
                            uploadPhoto(App.token, photoImgliu, "jpeg");
                        }else {
                            toast("两分钟内不可多次上传图片");
                        }
                    }
                }else {
                    startActivity(new Intent(PayQcodeActivity.this, LoginActivity.class));
                }
                break;
            case R.id.img_pay_qcode:
                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        PayQcodeActivity.this, R.anim.activity_translate_in));
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void saveCurrentImage() {
        //获取当前屏幕的大小
        int width = getWindow().getDecorView().getRootView().getWidth();
        int height = getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //找到当前页面的根布局
        View view = getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片,创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        temBitmap = view.getDrawingCache();
        SimpleDateFormat df = new SimpleDateFormat("yyyymmddhhmmss");
        final String time = df.format(new Date());
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen", time + ".png");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                temBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen/" + time + ".png";
                    final Result result = parseQRcodeBitmap(path);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (null != result) {
                                Intent intent = new Intent(PayQcodeActivity.this, WebShowActivity.class);
                                intent.putExtra("url", result.toString());
                                startActivity(intent);
                            } else {
                                Toast.makeText(PayQcodeActivity.this, "无法识别", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }).start();
            //禁用DrawingCahce否则会影响性能 ,而且不禁止会导致每次截图到保存的是第一次截图缓存的位图
            view.setDrawingCacheEnabled(false);
        }
    }
    //解析二维码图片,返回结果封装在Result对象中
    private Result parseQRcodeBitmap(String bitmapPath) {
        //解析转换类型UTF-8
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        //获取到待解析的图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        options.inSampleSize = options.outHeight / 400;
        if (options.inSampleSize <= 0) {
            options.inSampleSize = 1; //防止其值小于或等于0
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        //新建一个RGBLuminanceSource对象，将bitmap图片传给此对象
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);
        //将图片转换成二进制图片
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        //初始化解析对象
        QRCodeReader reader = new QRCodeReader();
        //开始解析
        Result result = null;
        try {
            result = reader.decode(binaryBitmap, hints);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }


    /**
     * 保存二维码到本地相册
     */
    private void saveImageToPhotos(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
            return;
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        mHandler.obtainMessage(SAVE_SUCCESS).sendToTarget();
    }

    /**
     * 初始化窗口
     */
    private void showRechargePopupWindow() {
        rechargePop = new PopupWindow(PayQcodeActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_recharge_popupwindows,
                null);
        ll_recharge_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        rechargePop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        rechargePop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        rechargePop.setBackgroundDrawable(new BitmapDrawable());
        rechargePop.setFocusable(true);
//        rechargePop.setOutsideTouchable(true);
        rechargePop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_yikoujia);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_tuangou);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_jingpai);
       /* bt1.setText("您确认要退出账号么？");
        bt1.setTextColor(getResources().getColor(R.color.hintColor));
        bt1.setTextSize(15);
        bt2.setText("退出");
        bt2.setTextColor(getResources().getColor(R.color.text_red));
        bt2.setTextSize(20);
        bt3.setText("取消");
        bt3.setTextColor(getResources().getColor(R.color.nomalText));
        bt3.setTextSize(20);*/

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechargePop.dismiss();
                ll_recharge_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FileUtils.saveBitmap(FileUtils.saveBitmapFromView(img_pay_upload_picture),"juli",PayQcodeActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.obtainMessage(SAVE_BEGIN).sendToTarget();
                        Bitmap bp = FileUtils.returnBitMap(url);
                        saveImageToPhotos(PayQcodeActivity.this, bp);
//                        FileUtils.saveBitmap(FileUtils.returnBitMap(url),"juli",PayQcodeActivity.this);
                    }
                }).start();

                rechargePop.dismiss();
                ll_recharge_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentImage();
                rechargePop.dismiss();
                ll_recharge_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rechargePop.dismiss();
                ll_recharge_popup.clearAnimation();
            }
        });
    }


    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    /**
     * 初始化窗口
     */
    private void showPopupWindow() {
        pop = new PopupWindow(PayQcodeActivity.this);
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
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {   //调用系统相机

                Acp.getInstance(getApplicationContext()).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)//, Manifest.permission.READ_PHONE_STATE、, Manifest.permission.SEND_SMS
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                try {
                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(camera, TAKE_PICTURE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    toast("已授权调用相机，请重新操作");
                                }
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                toast(permissions.toString() + "权限拒绝");
                            }
                        });

                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {   //调用系统相册
                Acp.getInstance(getApplicationContext()).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)//, Manifest.permission.READ_PHONE_STATE、, Manifest.permission.SEND_SMS
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                try {
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                    startActivityForResult(intent, RESULT_LOAD_IMAGE);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    toast("已授权访问本机文件，请重新操作");
                                }
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                toast(permissions.toString() + "权限拒绝");
                            }
                        });

                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)

        {

            case TAKE_PICTURE:

                if (resultCode == Activity.RESULT_OK && null != data) {
                    Bundle bundle = data.getExtras();

                    Bitmap mimp = (Bitmap) bundle.get("data");
//                    mimp = Bimp.createFramedPhoto(480, 480, mimp, 0);
//                    ZQ_private_head.setImageBitmap(mimp);
                    img_pay_qcode.setImageBitmap(mimp);

                    try {
                        //把位图转化为字符流
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        mimp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        baos.close();
                        byte[] buffer = baos.toByteArray();
                        photoImgliu = Base64.encodeToString(buffer, 0, buffer.length, Base64.NO_WRAP);
//                        userHeadUndo(App.token, cameralImgliu, "JPEG");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                break;
            case RESULT_LOAD_IMAGE:
                if (resultCode == RESULT_OK && null != data) {
//                    mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                    Log.e("6666", ":" + mSelectPath);
                    Bitmap bitmap;

                    Log.d("1111", "onActivityResult:相册 " + data.getData().toString());
                    ContentResolver resolver = getContentResolver();

                    try {
                        InputStream inputStream = resolver.openInputStream(data.getData());

                        bitmap = BitmapFactory.decodeStream(inputStream);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        baos.close();
                        byte[] buffer = baos.toByteArray();
                        photoImgliu = Base64.encodeToString(buffer, 0, buffer.length, Base64.NO_WRAP);

                        img_pay_qcode.setImageBitmap(bitmap);
//                        userHeadUndo(App.token, photoImgliu, "JPEG");

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * App35 > 上传充值图片接口
     */
    private void uploadPhoto(final String token, final String headImage, final String PicType) {
        if (NetUtil.isNetWorking(PayQcodeActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.uploadPhotoData(token, headImage, PicType, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                toast("上传成功");

                                SPUtil.saveData(PayQcodeActivity.this, "lastUploadTime", System.currentTimeMillis());

                                App.lastUploadTime = System.currentTimeMillis();

                                finish();
                            }else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            finishRefresh();
                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }


    /**
     * App13 > 绑定微信
     */
    private void getPhoto(final String token) {
        if (NetUtil.isNetWorking(PayQcodeActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getPhotoData(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.GetQCode data = new Gson().fromJson(result, Bean.GetQCode.class);
                            if (data.status == 1){
                                ImageLoadUtil.showImage(PayQcodeActivity.this, UriUtil.ip + data.photo, img_pay_qcode);
                            }else {
                                toast(data.message);
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
