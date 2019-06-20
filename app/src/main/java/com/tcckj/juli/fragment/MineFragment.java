package com.tcckj.juli.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import com.tcckj.juli.activity.ApplyPortActivity;
import com.tcckj.juli.activity.CertificationActivity;
import com.tcckj.juli.activity.FirstFriendsActivity;
import com.tcckj.juli.activity.MyPortActivity;
import com.tcckj.juli.activity.MySettingActivity;
import com.tcckj.juli.activity.MyWalletActivity;
import com.tcckj.juli.activity.PersonalInformationActivity;
import com.tcckj.juli.activity.RechargeActivity;
import com.tcckj.juli.activity.TransactionRecordActivity;
import com.tcckj.juli.base.BaseFragment;
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

import static android.app.Activity.RESULT_OK;

/**
 * 我的界面
 */
public class MineFragment extends BaseFragment {
    ImageView img_mine_change_name,img_mine_head;
    RelativeLayout rl_mine_personal_information,rl_mine_certification,rl_transaction_record,
            rl_mine_friends,rl_mine_port,rl_mine_wallet,rl_mine_corn,rl_mine_setting,rl_mine_recharge;
    TextView tv_dialog_change_name_left,tv_dialog_change_name_right,tv_dialog_title,
            tv_dialog_content,tv_mine_choose_port,tv_personal_name,tv_mine_phone,tv_mine_member;
    EditText et_dialog_input_name;

    Dialog dialog;
    boolean isChangeNameDialog = true;      //true：修改昵称     false：选择端口

    PopupWindow pop;
    LinearLayout ll_popup;

    @Override
    protected void initView(View view) {
        setContainer(R.layout.fragment_mine);

        img_mine_change_name = findView(R.id.img_mine_change_name);
        img_mine_head = findView(R.id.img_mine_head);

        rl_mine_personal_information = findView(R.id.rl_mine_personal_information);
        rl_mine_certification = findView(R.id.rl_mine_certification);
        rl_transaction_record = findView(R.id.rl_transaction_record);
        rl_mine_friends = findView(R.id.rl_mine_friends);
        rl_mine_port = findView(R.id.rl_mine_port);
        rl_mine_wallet = findView(R.id.rl_mine_wallet);
        rl_mine_corn = findView(R.id.rl_mine_corn);
        rl_mine_setting = findView(R.id.rl_mine_setting);
        rl_mine_recharge = findView(R.id.rl_mine_recharge);

        tv_mine_choose_port = findView(R.id.tv_mine_choose_port);
        tv_personal_name = findView(R.id.tv_personal_name);
        tv_mine_phone = findView(R.id.tv_mine_phone);
        tv_mine_member = findView(R.id.tv_mine_member);

        if (App.islogin) {
            tv_personal_name.setText(App.personalInfo.name);
            tv_mine_phone.setText(App.personalInfo.phone);
            if (!StringUtil.isSpace(App.personalInfo.headPhoto)){
                ImageLoadUtil.showImage(getActivity(), UriUtil.ip + App.personalInfo.headPhoto, img_mine_head, R.mipmap.def_head);
            }

            switch (App.personalInfo.portState){
                case "0":
                    tv_mine_choose_port.setText("选择端口");
                    break;
                case "1":
                    tv_mine_choose_port.setText("更换端口");
                    break;
                case "2":
                    tv_mine_choose_port.setText("申请端口");
                    break;
            }
        }

//        ImageLoadUtil.showImage(getActivity(),"https://p18.ssl.qhimgs3.com/dr/240_240_/t01729c9aaab412cb9c.gif?t=1521081553",img_mine_head);
        initDialog();

        mySmart.setEnableRefresh(true);
    }

    @Override
    protected void initListener() {
        img_mine_change_name.setOnClickListener(this);
        img_mine_head.setOnClickListener(this);
        rl_mine_personal_information.setOnClickListener(this);
        rl_mine_certification.setOnClickListener(this);
        rl_transaction_record.setOnClickListener(this);
        rl_mine_friends.setOnClickListener(this);
        rl_mine_port.setOnClickListener(this);
        rl_mine_wallet.setOnClickListener(this);
        rl_mine_corn.setOnClickListener(this);
        rl_mine_setting.setOnClickListener(this);
        rl_mine_recharge.setOnClickListener(this);

        tv_mine_choose_port.setOnClickListener(this);
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

    private void initDialog() {
        dialog = new Dialog(getActivity(), R.style.Dialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_mine_change_name, null);
        tv_dialog_change_name_left = (TextView) view.findViewById(R.id.tv_dialog_change_name_left);
        tv_dialog_change_name_right = (TextView) view.findViewById(R.id.tv_dialog_change_name_right);
        tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        tv_dialog_content = (TextView) view.findViewById(R.id.tv_dialog_content);
        et_dialog_input_name = (EditText) view.findViewById(R.id.et_dialog_input_name);
        tv_dialog_change_name_left.setOnClickListener(this);
        tv_dialog_change_name_right.setOnClickListener(this);

        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_mine_change_name:
                initDialog();

                isChangeNameDialog = true;
                tv_dialog_change_name_right.setText("确定");
                tv_dialog_content.setVisibility(View.GONE);
                et_dialog_input_name.setVisibility(View.VISIBLE);
                et_dialog_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});   //设置最大12个字符

                dialog.show();
                break;
            case R.id.tv_dialog_change_name_left:
                if (isChangeNameDialog) {
                    dialog.dismiss();
                }else {                                             //加入端口界面
                    switch (App.personalInfo.portState){
                        case "0":
                            Intent intent1 = new Intent(getActivity(), ApplyPortActivity.class);
                            intent1.putExtra("isApply", false);
                            startActivity(intent1);
                            dialog.dismiss();
                            break;
                        case "1":
                            Intent intent2 = new Intent(getActivity(), ApplyPortActivity.class);
                            intent2.putExtra("isApply", false);
                            startActivity(intent2);
                            dialog.dismiss();
                            break;
                        case "2":
                            dialog.dismiss();
                            break;
                    }
                }
                break;
            case R.id.tv_dialog_change_name_right:
                if (isChangeNameDialog){                        //修改昵称
                    String nameStr = et_dialog_input_name.getText().toString().trim();
                    if (StringUtil.isSpace(nameStr)){
                        toast("请输入昵称");
                        break;
                    }
                    editName(App.token,nameStr);
                    dialog.dismiss();
                }else {                                             //申请端口界面
                    Intent intent = new Intent(getActivity(), ApplyPortActivity.class);
                    intent.putExtra("isApply", true);
                    startActivity(intent);
                    dialog.dismiss();
                }
                break;
            case R.id.rl_mine_personal_information:             //个人资料界面
                startActivity(new Intent(getActivity(), PersonalInformationActivity.class));
                break;
            case R.id.rl_mine_certification:                    //实名认证界面
                startActivity(new Intent(getActivity(), CertificationActivity.class));
                break;
            case R.id.rl_transaction_record:                    //交易记录
                startActivity(new Intent(getActivity(), TransactionRecordActivity.class));
                break;
            case R.id.rl_mine_friends:                          //我的好友
                startActivity(new Intent(getActivity(), FirstFriendsActivity.class));
                break;
            case R.id.rl_mine_port:                             //我的端口
                if("0".equals(App.personalInfo.portState)) {
                    toast("尚未拥有端口");
                }else {
                    startActivity(new Intent(getActivity(), MyPortActivity.class));
                }
                break;
            case R.id.rl_mine_wallet:                           //我的钱包
                startActivity(new Intent(getActivity(), MyWalletActivity.class));
                break;
            case R.id.rl_mine_recharge:                         //充值界面
                startActivity(new Intent(getActivity(), RechargeActivity.class));
                break;
            case R.id.rl_mine_corn:                             //币
                final Dialog tipDialog = new Dialog(getActivity(),R.style.Dialog);
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_tip_no_open, null);
                TextView tv_dialog_sure = (TextView) view.findViewById(R.id.tv_dialog_sure);
                tv_dialog_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipDialog.dismiss();
                    }
                });
                tipDialog.setCanceledOnTouchOutside(false);
                tipDialog.setContentView(view);
                tipDialog.show();
                break;
            case R.id.rl_mine_setting:                          //设置
                startActivity(new Intent(getActivity(), MySettingActivity.class));
                break;
            case R.id.tv_mine_choose_port:                      //选择端口
                //未选择端口，显示dialog提醒选择端口
                isChangeNameDialog = false;
                tv_dialog_title.setText("我的端口");
                switch (App.personalInfo.portState){
                    case "0":
                        tv_dialog_change_name_left.setText("加入端口");
                        tv_dialog_change_name_right.setText("申请端口");
                        tv_dialog_content.setText("暂时没有端口，请先申请加入端口");
                        break;
                    case "1":
                        tv_dialog_change_name_left.setText("更换端口");
                        tv_dialog_change_name_right.setText("申请端口");
                        tv_dialog_content.setText("更换或申请端口");
                        break;
                    case "2":
                        tv_dialog_change_name_left.setText("取消");
                        tv_dialog_change_name_right.setText("申请端口");
                        tv_dialog_content.setText("更换或申请端口");
                        break;
                }
                tv_dialog_content.setVisibility(View.VISIBLE);
                et_dialog_input_name.setVisibility(View.GONE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.img_mine_head:
                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), R.anim.activity_translate_in));
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.islogin) {
            tv_personal_name.setText(App.personalInfo.name);
            tv_mine_phone.setText(App.personalInfo.phone);
            if (!StringUtil.isSpace(App.personalInfo.headPhoto)){
                ImageLoadUtil.showImage(getActivity(), UriUtil.ip + App.personalInfo.headPhoto, img_mine_head, R.mipmap.def_head);
            }
        }
    }


    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    /**
     * 初始化窗口
     */
    private void showPopupWindow() {
        pop = new PopupWindow(getActivity());
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

                Acp.getInstance(getActivity().getApplicationContext()).request(new AcpOptions.Builder()
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
                Acp.getInstance(getActivity().getApplicationContext()).request(new AcpOptions.Builder()
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)

        {

            case TAKE_PICTURE:

                if (resultCode == RESULT_OK && null != data) {
                    Bundle bundle = data.getExtras();

                    Bitmap mimp = (Bitmap) bundle.get("data");
//                    img_mine_head.setImageBitmap(mimp);

                    try {
                        //把位图转化为字符流
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        mimp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        baos.close();
                        byte[] buffer = baos.toByteArray();
                        String cameralImgliu = Base64.encodeToString(buffer, 0, buffer.length, Base64.NO_WRAP);
                        userHeadUndo(App.token, cameralImgliu, "jpeg");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                break;
            case RESULT_LOAD_IMAGE:
                if (resultCode == RESULT_OK && null != data) {
                    Bitmap bitmap;

                    Log.d("1111", "onActivityResult:相册 " + data.getData().toString());
                    ContentResolver resolver = getActivity().getContentResolver();

                    try {
                        InputStream inputStream = resolver.openInputStream(data.getData());

                        bitmap = BitmapFactory.decodeStream(inputStream);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        baos.close();
                        byte[] buffer = baos.toByteArray();
                        String photoImgliu = Base64.encodeToString(buffer, 0, buffer.length, Base64.NO_WRAP);

//                        img_mine_head.setImageBitmap(bitmap);
                        userHeadUndo(App.token, photoImgliu, "jpeg");

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
     * App11 > 修改昵称
     */
    private void editName(final String token, final String name) {
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.editNameData(token, name, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                getInfo(App.token);
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



    /**
     * App10 > 获取用户信息
     */
    private void getInfo(final String token) {
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getInfoData(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.PersonalInfoAll data = new Gson().fromJson(result, Bean.PersonalInfoAll.class);
                            if (data.status == 1){
                                SPUtil.saveBean2Sp(getActivity(), data.model, "personal","personalInfo");

                                App.personalInfo = data.model;

                                tv_personal_name.setText(App.personalInfo.name);
                                tv_mine_member.setText(App.personalInfo.memberName);
                                ImageLoadUtil.showImage(getActivity(),UriUtil.ip+App.personalInfo.headPhoto, img_mine_head, R.mipmap.def_head);
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
        if (NetUtil.isNetWorking(getActivity())){
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
