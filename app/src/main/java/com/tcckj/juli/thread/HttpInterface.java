package com.tcckj.juli.thread;

import android.content.Context;
import android.util.Log;

import com.tcckj.juli.util.UriUtil;
import com.tcckj.juli.view.LoadingDialog;

/**
 * kylin on 2017/12/12.
 */

public class HttpInterface {
    private Context context;
    public LoadingDialog loadingDialog;

    public HttpInterface(Context context) {
        this.context = context;
        if (loadingDialog == null) {
            Log.e("httpInterface", "new LoadingDialog");
            loadingDialog = new LoadingDialog(context);
            //loadingDialog.setHint("playing。。。");
        }
    }

    /**
     * 获取验证码
     *//*
    public void sendYzm(String phone, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.sendYzm);
        try {
            userClient.AddParam("model.phone", phone);  //手机号
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * App01 > 发送手机验证码
     */
    public void sendMsgData(String account, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.send_msg);
        try {
            userClient.AddParam("user.account", account);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App02 > 用户注册
     */
    public void userRegisterData(String code, String invitationCode, String password, String account, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.user_register);
        try {
            userClient.AddParam("code", code);
            userClient.AddParam("invitationCode", invitationCode);
            userClient.AddParam("user.password", password);
            userClient.AddParam("user.account", account);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    /**
     * App03 > 用户登录
     */
    public void userLoginData(String password, String account, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.user_login);
        try {
            userClient.AddParam("user.password", password);
            userClient.AddParam("user.account", account);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App04 > 快捷登录
     */
    public void msgLoginData(String code, String account, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.msg_login);
        try {
            userClient.AddParam("code", code);
            userClient.AddParam("user.account", account);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App05 > 首页数据
     */
    public void homePageData(String token, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.home_page);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App06 > 获取合约记录
     */
    public void getOrderListData(String token, String index, String num, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_order_list);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App07 > 开启或者关闭自动挖矿
     */
    public void switchStateData(String token, String type, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.switch_state);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("type", type);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App08 > 获取算力记录
     */
    public void getForceVlueListData(String token, String index, String num, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_force_vlue_list);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App09 > 修改头像
     */
    public void userHeadUndoData(String token, String headImage, String PicType, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.user_head_undo);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("headImage", headImage);
            userClient.AddParam("PicType", PicType);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }




    /**
     * App10 > 获取用户信息
     */
    public void getInfoData(String token, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_info);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    /**
     * App11 > 修改昵称
     */
    public void editNameData(String token, String name, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.edit_name);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("user.name", name);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App12 > 绑定支付宝
     */
    public void editAlipayData(String token, String realName, String alipay, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.edit_alipay);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("user.realName", realName);
            userClient.AddParam("user.alipay", alipay);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App13 > 绑定微信
     */
    public void editWechatData(String token, String wechat, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.edit_wechat);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("user.wechat", wechat);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * WeChat14 > 实名认证
     */
    public void editRealData(String token, String backRealName, String realId, String backId, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.edit_real);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("user.backRealName", backRealName);
            userClient.AddParam("user.realId", realId);
            userClient.AddParam("user.backId", backId);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App15 > 获取好友记录
     */
    public void getFriendsListData(String token, String index, String num, String oid, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_friends_list);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);
            userClient.AddParam("user.oid", oid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App16 > 打开箱子
     */
    public void openBoxData(String token, String oid, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.open_box);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("oid", oid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App17 > 获取商品记录
     */
    public void getCommodityListData(String token, String index, String num, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_commodity_list);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    /**
     * App18 > 获取商品详情
     */
    public void getCommodityData(String token, String oid, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_commodity);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("oid", oid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    /**
     * App19 > 获取算力排行
     */
    public void getForceListData(String token, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_force_list);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App20 > 获取交易记录
     */
    public void getTreasureListData(String token, String type, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_treasure_list);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("type", type);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App21 > 购买合约
     */
    public void getPurchaseData(String token, String money, String oid, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_purchase);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("money", money);
            userClient.AddParam("oid", oid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App22 > 释放本金
     */
    public void releasePrincipalData(String token, String oid, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.release_principal);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("oid", oid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App23 > 用户修改密码
     */
    public void editPasswordData(String token, String oldPassword, String newPasswordOne, String newPasswordTwo, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.edit_password);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("oldPassword", oldPassword);
            userClient.AddParam("newPasswordOne", newPasswordOne);
            userClient.AddParam("newPasswordTwo", newPasswordTwo);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App24 > 获取钱包信息
     */
    public void getWalletMoneyData(String token, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_wallet_money);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App25 > 用户修改支付密码
     */
    public void getPayPasswordData(String token, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_pay_password);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App26 > 用户修改支付密码
     */
    public void editPayPasswordData(String token, String oldPassword, String newPasswordOne, String newPasswordTwo, String code, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.edit_pay_password);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("oldPassword", oldPassword);
            userClient.AddParam("newPasswordOne", newPasswordOne);
            userClient.AddParam("newPasswordTwo", newPasswordTwo);
            userClient.AddParam("code", code);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App27 > 金额充值
     */
    public void appPayData(String token, String money, String rechargeType, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.app_pay);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("money", money);
            userClient.AddParam("rechargeType", rechargeType);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App28 > 获取端口列表
     */
    public void findPortListData(String token, String code, String level, String index, String num, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.find_port_list);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("code", code);
            userClient.AddParam("level", level);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App29 > 申请端口
     */
    public void applyPortData(String token, String code, String level, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.apply_port);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("code", code);
            userClient.AddParam("level", level);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App30 > 申请加入端口
     */
    public void joinPortData(String token, String oid, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.join_port);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("oid", oid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App31 > 我的端口数据显示
     */
    public void myPortData(String token, String index, String num, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.my_port);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App32 > 分享页面数据
     */
    public void findListData(String token, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.find_list);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App33 > 获取合约详情
     */
    public void getOrderDetailData(String token, String oid, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_order_detail);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("oid", oid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App34 > 释放接口
     */
    public void releaseWalletData(String token, String money, String type, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.release_wallet);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("money", money);
            userClient.AddParam("type", type);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 生成二维码action > 生成二维码
     */
    public void getQRcodeData(String token, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_getQRcode);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App35 > 上传充值图片接口
     */
    public void uploadPhotoData(String token, String headImage, String PicType, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.upload_photo);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("headImage", headImage);
            userClient.AddParam("PicType", PicType);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App36 > 提现接口
     */
    public void putForwardData(String token, String money, String type, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.put_forward);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("money", money);
            userClient.AddParam("type", type);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App37 > 获取提现信息接口
     */
    public void getPutForwardData(String token, String type, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_put_forward);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("type", type);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App38 > 一天开启或者关闭自动挖矿
     */
    public void switchDayStateData(String token, String type, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.switch_day_state);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("type", type);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App39 > 获取协议
     */
    public void getAgreementData(MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_agreement);
        try {
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App39 > 获取协议
     */
    public void updateTypeData(String type, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.update_type);
        try {
            userClient.AddParam("type", type);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App40 > 获取充值图片
     */
    public void getPhotoData(String token, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_photo);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App41 > 支付密码验证
     */
    public void judgePayPasswordData(String token, String payPassword, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.judge_pay_password);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("payPassword", payPassword);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App42 > 获取提现记录
     */
    public void getPutForwardListData(String token, String index, String num, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_put_forward_list);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App43 > 删除提现记录
     */
    public void deletePutForwardData(String token, String oid, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.delete_put_forward);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("oid", oid);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App44 > 获取关于我们得信息
     */
    public void getSetUpData(String token, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.get_set_up);
        try {
            userClient.AddParam("token", token);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App45 > 验证码修改登录密码
     */
    public void changeLoginPasswordData(String code, String account, String newPasswordOne, String newPasswordTwo, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.change_login_password);
        try {
            userClient.AddParam("code", code);
            userClient.AddParam("user.account", account);
            userClient.AddParam("newPasswordOne", newPasswordOne);
            userClient.AddParam("newPasswordTwo", newPasswordTwo);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * App46 > 验证码修改支付密码
     */
    public void changePayPasswordData(String code, String account, String newPasswordOne, String newPasswordTwo, MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.change_pay_password);
        try {
            userClient.AddParam("code", code);
            userClient.AddParam("user.account", account);
            userClient.AddParam("newPasswordOne", newPasswordOne);
            userClient.AddParam("newPasswordTwo", newPasswordTwo);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
