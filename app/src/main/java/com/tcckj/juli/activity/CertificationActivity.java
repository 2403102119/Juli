package com.tcckj.juli.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.StringUtil;

import okhttp3.Call;

/**
 * 实名认证界面
 */
public class CertificationActivity extends BaseActivity {
    TextView tv_certification_bind;
    EditText et_certification_name,et_certification_id_number,et_certification_bank_card;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_certification);

        title.setText("实名认证");

        tv_certification_bind = findView(R.id.tv_certification_bind);
        et_certification_name = findView(R.id.et_certification_name);
        et_certification_id_number = findView(R.id.et_certification_id_number);
        et_certification_bank_card = findView(R.id.et_certification_bank_card);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        tv_certification_bind.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_certification_bind:
                String nameStr = et_certification_name.getText().toString();
                String idNumber = et_certification_id_number.getText().toString();
                String bankStr = et_certification_bank_card.getText().toString();
                if (StringUtil.isSpace(nameStr)){
                    toast("请输入真实姓名");
                    break;
                }
                if (StringUtil.isSpace(idNumber)){
                    toast("请输入身份证号");
                    break;
                }
                if (StringUtil.isSpace(bankStr)){
                    toast("请输入银行卡号");
                    break;
                }
                if (!StringUtil.is18ByteIdCardComplex(idNumber)){
                    toast("请输入正确的身份证号");
                    break;
                }
                if (!StringUtil.checkBankCard(bankStr)){
                    toast("请输入正确的银行卡号");
                    break;
                }

                editReal(App.token,nameStr,idNumber,bankStr);

                break;
        }
    }



    /**
     * WeChat14 > 实名认证
     */
    private void editReal(final String token, final String backRealName, final String realId, final String backId) {
        if (NetUtil.isNetWorking(CertificationActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.editRealData(token, backRealName, realId, backId, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                toast("认证成功");
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
