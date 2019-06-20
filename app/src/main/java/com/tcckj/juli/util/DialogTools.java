package com.tcckj.juli.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tcckj.juli.R;


/**
 * kylin on 2018/1/17.
 */

public class DialogTools {
    private Dialog mLoadingDialog;
    private Context context;

    public DialogTools(Context context) {
        this.context=context;
    }

    public DialogTools() {}

    public void showLoading(String content) {

        View view= LayoutInflater.from(context).inflate(R.layout.loading_dialog, null);
        TextView loadingText = (TextView)view.findViewById(R.id.text);
        loadingText.setText(content);
        mLoadingDialog = new Dialog(context, R.style.Dialog);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(view,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
    }

    public void showLoading(Context context, String content) {

        View view= LayoutInflater.from(context).inflate(R.layout.loading_dialog, null);
        TextView loadingText = (TextView)view.findViewById(R.id.text);
        loadingText.setText(content);
        mLoadingDialog = new Dialog(context, R.style.Dialog);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(view,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();

    }

    public void dismissLoading(){
        if (mLoadingDialog != null){
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }


    private String codeStr;     //验证码字符串
    /**
     * 初始化弹窗
     */
    public Dialog vetificationDialog() {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_verification_show, null);
        final ImageView img_verification_show = (ImageView) view.findViewById(R.id.img_verification_show);
        TextView tv_dialog_verification_left = (TextView) view.findViewById(R.id.tv_dialog_verification_left);
        TextView tv_dialog_verification_right = (TextView) view.findViewById(R.id.tv_dialog_verification_right);
        final EditText et_dialog_input_verification = (EditText) view.findViewById(R.id.et_dialog_input_verification);

        final CodeUtils codeUtils = CodeUtils.getInstance();
        codeStr = codeUtils.createCode();
        img_verification_show.setImageBitmap(codeUtils.createBitmap(codeStr));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        img_verification_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeStr = codeUtils.createCode();
                img_verification_show.setImageBitmap(codeUtils.createBitmap(codeStr));
            }
        });
        tv_dialog_verification_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_dialog_verification_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeString = et_dialog_input_verification.getText().toString();
                if (StringUtil.isSpace(codeString)){
                    Toast.makeText(context,"请输入验证码",Toast.LENGTH_SHORT).show();
                    isVerification.isVerification(false);
                    return;
                }
                if (!codeStr.equalsIgnoreCase(codeString)){
                    Toast.makeText(context,"验证码不正确，请重新输入",Toast.LENGTH_SHORT).show();
                    isVerification.isVerification(false);
                    return;
                }
//                Toast.makeText(context,"验证成功",Toast.LENGTH_SHORT).show();
                isVerification.isVerification(true);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    IsVerification isVerification;

    public void setIsVerification(IsVerification isVerification) {
        this.isVerification = isVerification;
    }

    public interface IsVerification{
        void isVerification(boolean b);
    }
}
