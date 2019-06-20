package com.tcckj.juli.entity;

import java.io.Serializable;
import java.util.List;

public class Bean {
    public String message;
    public int status;


    /**
     * 获取验证码的结果
     */
    public class SendMsgResult{
        public String yzm;
        public int status;
        public String message;
    }


    /**
     * 个人信息实体类  一级
     */
    public class PersonalInfoAll{
        public PersonalInfo model;
        public int status;
        public String message;
    }


    /*
    个人信息实体类
     */
    public static class PersonalInfo implements Serializable{
        public String name;
        public String headPhoto;
        public String portLevel;
        public String portID;
        public String member;
        public String portNumber;
        public String alipay;
        public String wechat;
        public String realName;
        public String phone;
        public String token;
        public String oid;
        public String real_status;
        public String portState;
        public String moneyState;
        public String memberName;
        public String memberLevl;
        public int recommend_number;
        public double forceVlue;
    }


    /*
    首页数据实体类     一级
     */
    public class TreasureHuntAll{
        public TreasureHunt model;
        public int status;
        public String message;
    }


    /*
    首页数据实体类
     */
    public class TreasureHunt{
        public double forceVlue;   //算力
        public String moneyState;   //自动挖矿状态0关闭1开启
        public String dayState;   //自动挖矿状态0关闭1开启
        public List<UserCase> userCaseList;   //用户箱子列表
        public List<BroadcastNotice> noticeList;   //公告
        public List<PictureNotice> notice;   //图片广告
        public List<Contract> orderList;    //合约记录
    }


    /*
    用户箱子实体类
     */
    public class UserCase{
        public String oid;
        public String name;
        public String type;
        public String state;
        public double number;
    }


    /*
    广播公告实体类
     */
    public class BroadcastNotice{
        public String oid;
        public String title;
    }


    /*
    图片公告实体类
     */
    public class PictureNotice{
        public String oid;
        public String title;
        public String photo;
        public String url;
    }


    /*
    一级好友信息实体类     一级
     */
    public class FirstFriendsListAll{
        public List<FirstFriends> userList;
        public double sum;
        public int status;
        public String message;
    }


    /*
    一级好友信息实体类
     */
    public class FirstFriends{
        public String oid;
        public String registDate;
        public int money;
        public int recommend_number;
        public String name;
        public String account;
    }


    /*
    资讯返回实体类     一级
   */
    public class InfomationAll{
        public List<UserInfo> userList;
        public List<BannerInfo> bannerList;
        public List<NoticeInfo> noticeList;
        public int status;
        public String message;
    }


    /*
    算力排行用户信息实体类
     */
    public class UserInfo{
        public String oid;
        public String account;
        public String name;
        public int recommend_number;
        public int forceVlue;
    }


    /*
    轮播图实体类
     */
    public class BannerInfo{
        public String oid;
        public String date;
        public String photo;
        public String content;
        public String url;
    }


    /*
    资讯消息实体类
     */
    public class NoticeInfo{
        public String oid;
        public String date;
        public String photo;
        public String content;
        public String title;
    }


    /*
    合约记录实体类     一级
     */
    public class ContractListAll{
        public List<Contract> orderList;
        public int status;
        public String message;
    }


    /*
    合约记录实体类
     */
    public class Contract implements Serializable{
        public String oid;
        public String date;
        public int number;
        public int oneDay;
        public int twoDay;
        public int frozenDate;
        public int releaseDate;
        public double money;
        public ContractCommodity commodity;
    }


    /*
    合约商品实体类
     */
    public class ContractCommodity{
        public String name;
        public int number;
        public int frozenDate;
        public int releaseDate;
    }


    /*
      算力记录实体类     一级
       */
    public class CalculateListAll{
        public List<Calculate> orderList;
        public int status;
        public String message;
    }


    /*
    算力记录实体类
     */
    public class Calculate{
        public String oid;
        public String type;
        public String describe;
        public String date;
        public int value;
    }


    /*
     交易记录实体类     一级
      */
    public class TreasureListAll{
        public List<Treasure> recordList;
        public int status;
        public String message;
    }


    /*
    交易记录实体类
     */
    public class Treasure{
        public String oid;
        public String type;
        public String name;
        public String date;
        public double value;
    }


    /*
    商品记录实体类     一级
     */
    public class CommodityListAll{
        public List<Commodity> commodityList;
        public int status;
        public String message;
    }


    /*
   商品记录实体类
    */
    public class Commodity{
        public String oid;
        public String photo;
        public String name;
        public double averageRate;
        public int number;
        public int frozenDate;
        public int releaseDate;
    }


    /*
   商品详情实体类     一级
    */
    public class CommodityDetailAll{
        public CommodityDetail model;
        public int status;
        public String message;
    }


    /*
  商品详情实体类
   */
    public class CommodityDetail{
        public String oid;
        public String photo;
        public String type;
        public String characteristic;
        public String content;
        public String member;
        public double minMoney;
        public double maxMoney;
        public double receptionMinRate;
        public double receptionMaxRate;
        public int number;
        public double averageRate;
    }


    /*
    二维码返回的实体类
     */
    public class QCode{
        public int status;
        public String ewmPath;
        public String zcUrl;
        public String yqm;
        public String message;
    }


    /*
    上传头像返回的实体类
     */
    public class UploadHead {
        public int status;
        public String message;
        public String Head;
    }


    /*
    钱包的实体类      一级
     */
    public class WalletAll{
        public int status;
        public String message;
        public Wallet model;
    }


    /*
    钱包的实体类
     */
    public class Wallet{
        public String oid;
        public double jifenOne;
        public double jifenTwo;
        public double jifenThree;
        public double tuijianOne;
        public double tuijianTwo;
        public double tuijianThree;
        public double dongtaiOne;
        public double dongtaiTwo;
        public double dongtaiThree;
        public double jingtaiOne;
        public double jingtaiTwo;
        public double jingtaiThree;
        public double dongjieOne;
        public double dongjieTwo;
        public double dongjieThree;
    }


    /*
    打开箱子的结果返回实体类
     */
    public class OpenBox {
        public int status;
        public String message;
        public double number;
    }


    /*
    获取支付密码返回的实体类
     */
    public class GetPayPassword {
        public int status;
        public String message;
        public String payPassword;
    }


    /*
    好友总信息的返回实体类
     */
    public class FriendsMsg {
        public int status;
        public String message;
        public int oneSize;
        public int twoSize;
        public double oneMoney;
        public double twoMoney;
    }


    /*
    端口列表的实体类    一级
     */
    public class PortAll{
        public int status;
        public String message;
        public List<Port> list;
    }


    /*
    端口的实体类
     */
    public class Port{
        public String name;
        public String oid;
        public String portLevel;
        public String number;
        public String date;
        public PortUser user;
    }


    /*
    端口用户实体类
     */
    public class PortUser{
        public String oid;
        public int recommend_number;
    }


    /*
    合约详情实体类
     */
    public class ContractDetail{
        public String state;
        public String type;
        public String oid;
        public String date;
        public String name;
        public double money;
        public double averageRate;
        public double principal;
        public double releaseDate;
        public int status;
        public String message;
    }


    /*
    提现信息实体类
     */
    public class WithdrawMsg{
        public int putBase;
        public int putMultiple;
        public double money;
        public String chargeMoney;
        public String realState;
        public int status;
        public String message;
    }


    /*
    我的端口数据实体类
     */
    public class MyPortAll{
        public int status;
        public String message;
        public String portName;
        public List<FirstFriends> list;
    }


    /*
    快捷支付结果返回实体类
     */
    public class FastPayResultAll{
        public int status;
        public String message;
        public String url;
    }


    /*
    版本号返回的实体类
     */
    public class VersionCode{
        public String type;
        public int status;
        public String message;
    }


    /*
    获取充值二维码
     */
    public class GetQCode{
        public String photo;
        public int status;
        public String message;
    }


    /*
    关于聚力的返回信息实体类
     */
    public class AboutJuli{
        public String photo;
        public int status;
        public String message;
    }


    /*
    支付密码验证的返回信息实体类
     */
    public class PayPassword{
        public String type;
        public int status;
        public String message;
    }


    /*
    提现记录返回的实体类   一级
     */
    public class WithdrawRecordAll{
        public List<WithdrawRecord> presentList;
        public int status;
        public String message;
    }


    /*
    提现记录的实体类
     */
    public class WithdrawRecord{
        public String oid;
        public String state;
        public String type;
        public double money;
        public String status;
        public String date;
        public String back;
        public String backName;
    }


}