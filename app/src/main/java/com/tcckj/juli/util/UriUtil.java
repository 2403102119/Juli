package com.tcckj.juli.util;

/**
 * kylin on 2017/12/12.
 */

public class UriUtil {
//    public static final String ip = "http://192.168.1.169:8080/Payment/";
    public static final String ip = "http://103.230.243.230/Payment/";

//    public static final String ip = "http://192.168.1.254:8080/Payment/";
//    public static final String ip = "http://192.168.1.195:8080/Payment/";

    public static final String send_msg = UriUtil.ip + "action/LoginAction/sendMsg";//App01 > 发送手机验证码
    public static final String user_register = UriUtil.ip + "action/LoginAction/userRegister";//App02 > 用户注册
    public static final String user_login = UriUtil.ip + "action/LoginAction/userLogin";//App03 > 用户登录
    public static final String msg_login = UriUtil.ip + "action/LoginAction/appMsgLogin";//App04 > 快捷登录
    public static final String home_page = UriUtil.ip + "action/LoginAction/homePage";//App05 > 首页数据
    public static final String get_order_list = UriUtil.ip + "action/LoginAction/getOrderList";//App06 > 获取合约记录
    public static final String switch_state = UriUtil.ip + "action/LoginAction/switchState";//App07 > 开启或者关闭自动挖矿
    public static final String get_force_vlue_list = UriUtil.ip + "action/LoginAction/getForceVlueList";//App08 > 获取算力记录
    public static final String user_head_undo = UriUtil.ip + "action/LoginAction/userHeadUndo";//App09 > 修改头像
    public static final String get_info = UriUtil.ip + "action/LoginAction/getInfo";//App10 > 获取用户信息
    public static final String edit_name = UriUtil.ip + "action/LoginAction/editNickName";//App11 > 修改昵称
    public static final String edit_alipay = UriUtil.ip + "action/LoginAction/editAlipay";//App12 > 绑定支付宝
    public static final String edit_wechat = UriUtil.ip + "action/LoginAction/editWechat";//App13 > 绑定微信
    public static final String edit_real = UriUtil.ip + "action/LoginAction/editReal";//WeChat14 > 实名认证
    public static final String get_friends_list = UriUtil.ip + "action/LoginAction/getFriendsList";//App15 > 获取好友记录
    public static final String open_box = UriUtil.ip + "action/CommodityAction/switchState";//App16 > 打开箱子
    public static final String get_commodity_list = UriUtil.ip + "action/CommodityAction/getCommodityList";//App17 > 获取商品记录
    public static final String get_commodity = UriUtil.ip + "action/CommodityAction/getCommodity";//App18 > 获取商品详情
    public static final String get_force_list = UriUtil.ip + "action/CommodityAction/getForceList";//App19 > 获取算力排行
    public static final String get_treasure_list = UriUtil.ip + "action/CommodityAction/getRecordList";//App20 > 获取交易记录
    public static final String get_purchase = UriUtil.ip + "action/CommodityAction/getPurchase";//App21 > 购买合约
    public static final String release_principal = UriUtil.ip + "action/CommodityAction/releasePrincipal";//App22 > 释放本金
    public static final String edit_password = UriUtil.ip + "action/LoginAction/appEditPassword";//App23 > 用户修改密码
    public static final String get_wallet_money = UriUtil.ip + "action/LoginAction/getWalletMoney";//App24 > 获取钱包信息
    public static final String get_pay_password = UriUtil.ip + "action/LoginAction/appGetPayPassword";//App25 > 用户修改支付密码
    public static final String edit_pay_password = UriUtil.ip + "action/LoginAction/appEditPayPassword";//App26 > 用户修改支付密码
    public static final String app_pay = UriUtil.ip + "action/CommodityAction/appPay";//App27 > 金额充值
    public static final String find_port_list = UriUtil.ip + "action/LoginAction/findPortList";//App28 > 获取端口列表
    public static final String apply_port = UriUtil.ip + "action/LoginAction/applyPort";//App29 > 申请端口
    public static final String join_port = UriUtil.ip + "action/LoginAction/appJsonPort";//App30 > 申请加入端口
    public static final String my_port = UriUtil.ip + "action/LoginAction/myPortData";//App31 > 我的端口数据显示
    public static final String find_list = UriUtil.ip + "action/LoginAction/findList";//App32 > 分享页面数据
    public static final String get_order_detail = UriUtil.ip + "action/LoginAction/getOrderDetil";//App33 > 获取合约详情
    public static final String release_wallet = UriUtil.ip + "action/LoginAction/releaseWallet";//App34 > 释放接口
    public static final String upload_photo = UriUtil.ip + "action/LoginAction/uploadPhoto";//App35 > 上传充值图片接口
    public static final String put_forward = UriUtil.ip + "action/LoginAction/putForward";//App36 > 提现接口
    public static final String get_put_forward = UriUtil.ip + "action/LoginAction/getPutForward";//App37 > 获取提现信息接口
    public static final String switch_day_state = UriUtil.ip + "action/LoginAction/switchDayState";//App38 > 一天开启或者关闭自动挖矿
    public static final String get_agreement = UriUtil.ip + "action/LoginAction/getXieYi";//App39 > 获取协议
    public static final String update_type = UriUtil.ip + "action/CommodityAction/updateType";//App39 > 获取协议
    public static final String get_photo = UriUtil.ip + "action/CommodityAction/getPhoto";//App40 > 获取充值图片
    public static final String judge_pay_password = UriUtil.ip + "action/LoginAction/getPhoto";//App41 > 支付密码验证
    public static final String get_put_forward_list = UriUtil.ip + "action/CommodityAction/getPutForwardList";//App42 > 获取提现记录
    public static final String delete_put_forward = UriUtil.ip + "action/CommodityAction/deletePutForward";//App43 > 删除提现记录
    public static final String get_set_up = UriUtil.ip + "action/CommodityAction/getSetUp";//App44 > 获取关于我们得信息
    public static final String change_login_password = UriUtil.ip + "action/LoginAction/appCodeEditLoginPassword";//App45 > 验证码修改登录密码
    public static final String change_pay_password = UriUtil.ip + "action/LoginAction/appCodeEditPayPassword";//App46 > 验证码修改支付密码
    public static final String get_getQRcode = UriUtil.ip + "action/basicQRcodeAction/getQRcode";//生成二维码action > 生成二维码
}
