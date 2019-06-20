package com.tcckj.juli.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.ImageLoadUtil;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.SPUtil;
import com.tcckj.juli.util.StringUtil;
import com.tcckj.juli.util.UriUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;

/**
 * 个人资料界面
 */
public class PersonalInformationActivity extends BaseActivity {
    TextView tv_alipay_show,tv_wechat_show,tv_personal_phone;
    ImageView img_to_bind_alipay,img_to_bind_wechat,img_personal_back,img_personal_head;

    PopupWindow pop;
    LinearLayout ll_popup;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_personal_information);

        top.setVisibility(View.GONE);

        tv_alipay_show = findView(R.id.tv_alipay_show);
        tv_wechat_show = findView(R.id.tv_wechat_show);
        tv_personal_phone = findView(R.id.tv_personal_phone);
        img_to_bind_alipay = findView(R.id.img_to_bind_alipay);
        img_to_bind_wechat = findView(R.id.img_to_bind_wechat);
        img_personal_back = findView(R.id.img_personal_back);
        img_personal_head = findView(R.id.img_personal_head);

        tv_personal_phone.setText(App.personalInfo.phone);
        if (StringUtil.isSpace(App.personalInfo.alipay)) {
            tv_alipay_show.setText("未绑定");
        }else {
            tv_alipay_show.setText(App.personalInfo.alipay);
        }

        if (StringUtil.isSpace(App.personalInfo.wechat)){
            tv_wechat_show.setText("未绑定");
        }else {
            tv_wechat_show.setText(App.personalInfo.wechat);
        }

//        ImageLoadUtil.showImage(PersonalInformationActivity.this,"http://p7.sinaimg.cn/3224680670/180/01161397632885",img_personal_head);
        if (!StringUtil.isSpace(App.personalInfo.headPhoto)){
            ImageLoadUtil.showImage(PersonalInformationActivity.this, UriUtil.ip + App.personalInfo.headPhoto, img_personal_head, R.mipmap.def_head);
        }

        mySmart.setEnableRefresh(true);
    }

    @Override
    protected void initListener() {
        tv_alipay_show.setOnClickListener(this);
        tv_wechat_show.setOnClickListener(this);
        img_to_bind_alipay.setOnClickListener(this);
        img_to_bind_wechat.setOnClickListener(this);
        img_personal_back.setOnClickListener(this);
//        img_personal_head.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getInfo(App.token);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfo(App.token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_alipay_show:
                if (StringUtil.isSpace(App.personalInfo.alipay)){       //绑定支付宝
                    Intent intent2 = new Intent(PersonalInformationActivity.this, BindAlipayActivity.class);
                    intent2.putExtra("bind_type", "bind_ali");
                    startActivity(intent2);
                }else {                                                 //换绑支付宝
                    Intent intent1 = new Intent(PersonalInformationActivity.this, MyAlipayActivity.class);
                    intent1.putExtra("bind_type", "ali");
                    startActivity(intent1);
                }
                break;
            case R.id.img_to_bind_alipay:
                if (StringUtil.isSpace(App.personalInfo.alipay)){       //绑定支付宝
                    Intent intent2 = new Intent(PersonalInformationActivity.this, BindAlipayActivity.class);
                    intent2.putExtra("bind_type", "bind_ali");
                    startActivity(intent2);
                }else {                                                 //换绑支付宝
                    Intent intent1 = new Intent(PersonalInformationActivity.this, MyAlipayActivity.class);
                    intent1.putExtra("bind_type", "ali");
                    startActivity(intent1);
                }
                break;
            case R.id.tv_wechat_show:
                if (StringUtil.isSpace(App.personalInfo.wechat)){       //绑定微信
                    Intent intent4 = new Intent(PersonalInformationActivity.this, MyAlipayActivity.class);
                    intent4.putExtra("bind_type", "bind_wx");
                    startActivity(intent4);
                }else {                                                  //换绑微信
                    Intent intent3 = new Intent(PersonalInformationActivity.this, MyAlipayActivity.class);
                    intent3.putExtra("bind_type", "wx");
                    startActivity(intent3);
                }
                break;
            case R.id.img_to_bind_wechat:
                if (StringUtil.isSpace(App.personalInfo.wechat)){       //绑定微信
                    Intent intent4 = new Intent(PersonalInformationActivity.this, MyAlipayActivity.class);
                    intent4.putExtra("bind_type", "bind_wx");
                    startActivity(intent4);
                }else {                                                  //换绑微信
                    Intent intent3 = new Intent(PersonalInformationActivity.this, MyAlipayActivity.class);
                    intent3.putExtra("bind_type", "wx");
                    startActivity(intent3);
                }
                break;
            case R.id.img_personal_back:
                finish();
                break;
            case R.id.img_personal_head:
                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        PersonalInformationActivity.this, R.anim.activity_translate_in));
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    /**
     * 初始化窗口
     */
    private void showPopupWindow() {
        pop = new PopupWindow(PersonalInformationActivity.this);
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
                    img_personal_head.setImageBitmap(mimp);

                    try {
                        //把位图转化为字符流
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        mimp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        baos.close();
                        byte[] buffer = baos.toByteArray();
                        String cameralImgliu = Base64.encodeToString(buffer, 0, buffer.length, Base64.NO_WRAP);
                        userHeadUndo(App.token, cameralImgliu, "JPEG");

//                        cameralImgliu[i] = Base64.encodeToString(buffer, 0, buffer.length, Base64.NO_WRAP);
//                        Log.e("666", "camearlLiu" + i + ":" + cameralImgliu[i]);

                       /* if (App.islogin) {
                            submitImage(App.token, cameralImgliu[i], "JPEG");
                        } else {
                            startActivity(new Intent(PrivateInformationActivity.this, LoginGuideActivity.class));
                        }*/

//                        i++;

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
                        String photoImgliu = Base64.encodeToString(buffer, 0, buffer.length, Base64.NO_WRAP);

                        img_personal_head.setImageBitmap(bitmap);
                        userHeadUndo(App.token, photoImgliu, "JPEG");

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*try {
                        for (String p : mSelectPath) {
                            Log.e("55555", p.toString());
                            bitmap = Bimp.revitionImageSize(p.toString());
//                            bitmap = Bimp.createFramedPhoto(480, 480, bitmap, 0);
//                            ZQ_private_head.setImageBitmap(bitmap);
                            //把图片数量添加进集合,方便删除，统计数量

                            //把位图转化为字符流
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                            baos.close();
                            byte[] buffer = baos.toByteArray();
                            String[] photoImgliu = new String[1024 * 1024];
                            photoImgliu[i] = Base64.encodeToString(buffer, 0, buffer.length, Base64.NO_WRAP);
                            Log.e("666", "photoLiu" + i + ":" + photoImgliu[i]);

                          *//*  if (App.islogin) {
                                submitImage(App.token, photoImgliu[i], "JPEG");
                            } else {
                                startActivity(new Intent(PrivateInformationActivity.this, LoginGuideActivity.class));
                            }

                            i++;*//*
                        }*/

                   /* } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
                break;
            default:
                break;
        }
    }



    /**
     * App10 > 获取用户信息
     */
    private void getInfo(final String token) {
        if (NetUtil.isNetWorking(PersonalInformationActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getInfoData(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.PersonalInfoAll data = new Gson().fromJson(result, Bean.PersonalInfoAll.class);
                            if (data.status == 1){
                                SPUtil.saveBean2Sp(PersonalInformationActivity.this, data.model, "personal","personalInfo");

                                App.personalInfo = data.model;

                                tv_personal_phone.setText(App.personalInfo.phone);
                                if (StringUtil.isSpace(App.personalInfo.alipay)) {
                                    tv_alipay_show.setText("未绑定");
                                }else {
                                    tv_alipay_show.setText(App.personalInfo.alipay);
                                }

                                if (StringUtil.isSpace(App.personalInfo.wechat)){
                                    tv_wechat_show.setText("未绑定");
                                }else {
                                    tv_wechat_show.setText(App.personalInfo.wechat);
                                }
//                                if (!StringUtil.isSpace(App.personalInfo.headPhoto)){
//                                    ImageLoadUtil.showImage(PersonalInformationActivity.this, UriUtil.ip + App.personalInfo.headPhoto, img_personal_head);
//                                }
                            }
                            finishRefresh();
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
     * App09 > 修改头像
     */
    private void userHeadUndo(final String token, final String headImage, final String PicType) {
        if (NetUtil.isNetWorking(PersonalInformationActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.userHeadUndoData(token, headImage, PicType, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.UploadHead data = new Gson().fromJson(result, Bean.UploadHead.class);
                            if (data.status == 1){
                                getInfo(App.token);
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


}
