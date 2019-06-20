package com.tcckj.juli.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.tcckj.juli.App;
import com.tcckj.juli.R;
import com.tcckj.juli.adapter.AddressAdapter;
import com.tcckj.juli.adapter.PortAdapter;
import com.tcckj.juli.base.BaseActivity;
import com.tcckj.juli.entity.AddressBean;
import com.tcckj.juli.entity.Bean;
import com.tcckj.juli.thread.MApiResultCallback;
import com.tcckj.juli.thread.ThreadPoolManager;
import com.tcckj.juli.util.DataHelper;
import com.tcckj.juli.util.GetJsonDataUtil;
import com.tcckj.juli.util.NetUtil;
import com.tcckj.juli.util.SPUtil;
import com.tcckj.juli.view.MyPickAddressDialog;
import com.tcckj.juli.view.MyPickAddressInterface;
import com.tcckj.juli.view.NiceRecyclerView;
import com.tcckj.juli.view.PickAddressDialog;
import com.tcckj.juli.view.PickAddressInterface;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 申请端口界面
 */
public class ApplyPortActivity extends BaseActivity {
    private List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> streetChildsBeans = new ArrayList<>();
    private List<AddressBean> addressBeen;

    private List<Map<String, Object>> list = new ArrayList<>();
    private ArrayList<String> addressList = new ArrayList<>();

    NiceRecyclerView nrv_port_list,nrv_address_list;
    PortAdapter adapter;
    AddressAdapter addressAdapter;

    TextView tv_choose_port;

    private boolean isApply;

    //省、市、区、街道
    private String province = "";
    private String city = "";
    private String district = "";
    private String portString = "";

    private String oid = "";

//    private Dialog dialog;

    TextView tv_dialog_choose_province,tv_dialog_choose_city,tv_dialog_choose_district,
            tv_apply_port_sure;

    private int index = 0,num = 15;


    /*
    地址选择器相关
     */
    private ArrayList<AddressBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private boolean isLoaded = false;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

//                        Toast.makeText(JsonDataActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(JsonDataActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
//                    showPickerView();
                    isLoaded = true;
                    tv_choose_port.setClickable(true);
                    break;

                case MSG_LOAD_FAILED:
//                    Toast.makeText(JsonDataActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_apply_port);

//        title.setText("申请端口");
        img_back.setImageResource(R.mipmap.delete_icon);

        tv_choose_port = findView(R.id.tv_choose_port);
        tv_apply_port_sure = findView(R.id.tv_apply_port_sure);
        nrv_port_list = findView(R.id.nrv_port_list);
        nrv_address_list = findView(R.id.nrv_address_list);

//        initDialog();
    }

   /* private void initDialog() {
        dialog = new Dialog(ApplyPortActivity.this, R.style.Dialog);
        View view = LayoutInflater.from(ApplyPortActivity.this).inflate(R.layout.dialog_choose_place,null);
        tv_dialog_choose_province = (TextView) view.findViewById(R.id.tv_dialog_choose_province);
        tv_dialog_choose_city = (TextView) view.findViewById(R.id.tv_dialog_choose_city);
        tv_dialog_choose_district = (TextView) view.findViewById(R.id.tv_dialog_choose_district);

        tv_dialog_choose_province.setOnClickListener(this);
        tv_dialog_choose_city.setOnClickListener(this);
        tv_dialog_choose_district.setOnClickListener(this);

        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(view);
    }*/

    @Override
    protected void initListener() {
        tv_choose_port.setOnClickListener(this);
        img_back.setOnClickListener(this);
        tv_apply_port_sure.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            isApply = getIntent().getBooleanExtra("isApply", false);
            if (isApply){
                title.setText("申请端口");
                tv_apply_port_sure.setVisibility(View.GONE);
            }else {
                title.setText("加入端口");
            }
        }

        adapter = new PortAdapter(ApplyPortActivity.this, list);
        nrv_port_list.setAdapter(adapter);
        adapter.setOnItemClickListener(new PortAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
//                list.clear();
                oid = (String) list.get(position).get("oid");

                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", list.get(i).get("name"));
                    map.put("oid", list.get(i).get("oid"));
                    map.put("date", list.get(i).get("date"));
                    map.put("number", list.get(i).get("number"));
                    map.put("portLevel", list.get(i).get("portLevel"));
                    if (position == i){
                        map.put("isClick", true);
                    }else {
                        map.put("isClick", false);
                    }
                    list.set(i, map);
                }
                adapter.notifyDataSetChanged();
            }
        });

        addressAdapter = new AddressAdapter(ApplyPortActivity.this, addressList);
        nrv_address_list.setAdapter(addressAdapter);
        addressAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                portString = province + "," + city + "," + district;
                nrv_address_list.setVisibility(View.GONE);
                nrv_port_list.setVisibility(View.VISIBLE);
                if (isApply) {
                    applyPort(App.token, portString, String.valueOf(position + 1));
                    /*switch (position) {
                        case 0:
                            applyPort(App.token, province, String.valueOf(position + 1));
                            break;
                        case 1:
                            applyPort(App.token, province + "," + city, String.valueOf(position + 1));
                            break;
                        case 2:
                            applyPort(App.token, portString, String.valueOf(position + 1));
                            break;
                        case 3:
                            applyPort(App.token, portString, String.valueOf(position + 1));
                            break;
                    }*/
                }else {
                    switch (position) {
                        case 0:
                            findPortList(App.token, province, String.valueOf(position + 1), String.valueOf(index), String.valueOf(num));
                            break;
                        case 1:
                            findPortList(App.token, province + "," + city, String.valueOf(position + 1), String.valueOf(index), String.valueOf(num));
                            break;
                        case 2:
                            findPortList(App.token, portString, String.valueOf(position + 1), String.valueOf(index), String.valueOf(num));
                            break;
                        case 3:
                            findPortList(App.token, portString, String.valueOf(position + 1), String.valueOf(index), String.valueOf(num));
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_choose_port:

                mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                if (isLoaded) {
//                    showPickerView();
                } else {
//                    Toast.makeText(ApplyPortActivity.this, "Please waiting until the data is parsed", Toast.LENGTH_SHORT).show();
                    tv_choose_port.setClickable(false);
                }


                /*
                 * 先设置不可点击
                 *//*
                tv_choose_port.setEnabled(false);
                PickAddressDialog.showPickAddressDialog(ApplyPortActivity.this, "1", streetChildsBeans);
                addressBeen = DataHelper.getAddress(this);
                PickAddressDialog.setData(addressBeen);
                *//*
                 * 数据加载完成设置可点击
                 *//*
                tv_choose_port.setEnabled(true);
                PickAddressDialog.setOnTopClicklistener(new PickAddressInterface() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onOkClick(String mProvinceName, String mCityName, String mCountyName, String mCurrentName, List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> beans) {

//                        tv_dialog_choose_province.setText(mProvinceName);
//                        tv_dialog_choose_city.setText(mCityName);
//                        tv_dialog_choose_district.setText(mCountyName);
//                        dialog.show();

//                        streetChildsBeans = beans;

                        province = mProvinceName;
                        city = mCityName;
                        district = mCountyName;

                        addressList.clear();
                        addressList.add(province);
                        addressList.add(city);
                        addressList.add(district);
                        addressList.add("经理级");

                        nrv_port_list.setVisibility(View.GONE);
                        nrv_address_list.setVisibility(View.VISIBLE);
                        addressAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });*/
                break;
            case R.id.img_back:
                finish();
                break;
            /*case R.id.tv_dialog_choose_province:
                portString = province;
                findPortList(App.token, portString, String.valueOf(index), String.valueOf(num));
                dialog.dismiss();
                break;
            case R.id.tv_dialog_choose_city:
                portString = province + "," + city;
                findPortList(App.token, portString, String.valueOf(index), String.valueOf(num));
                dialog.dismiss();
                break;
            case R.id.tv_dialog_choose_district:
                portString = province + "," + city + "," + district;
                findPortList(App.token, portString, String.valueOf(index), String.valueOf(num));
                dialog.dismiss();
                break;*/
            case R.id.tv_apply_port_sure:
                    joinPort(App.token, oid);
                break;
        }
    }


//    public void showPickerView() {// 弹出选择器
//
//        TimePickerBuilder timePickerBuilder =
//
//        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                //返回的分别是三个级别的选中位置
//                String tx = options1Items.get(options1).getPickerViewText() +
//                        options2Items.get(options1).get(options2) +
//                        options3Items.get(options1).get(options2).get(options3);
//
//                province = options1Items.get(options1).getPickerViewText();
//                city = options2Items.get(options1).get(options2);
//                district = options3Items.get(options1).get(options2).get(options3);
//
//                addressList.clear();
//                addressList.add(province);
//                addressList.add(city);
//                addressList.add(district);
//                addressList.add("经理级");
//
//                nrv_port_list.setVisibility(View.GONE);
//                nrv_address_list.setVisibility(View.VISIBLE);
//                addressAdapter.notifyDataSetChanged();
////
//            }
//        })
//
//                .setTitleText("")
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                .setContentTextSize(20)
//                .build();
//
//        /*pvOptions.setPicker(options1Items);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
////        pvOptions.on
//        pvOptions.show();
//    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "address.json");//获取assets目录下的json文件数据

        ArrayList<AddressBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getChilds().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getChilds().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getChilds().get(c).getName() == null
                        || jsonBean.get(i).getChilds().get(c).getChilds().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int j = 0; j < jsonBean.get(i).getChilds().get(c).getChilds().size(); j++) {
                        City_AreaList.add(jsonBean.get(i).getChilds().get(c).getChilds().get(j).getName());
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<AddressBean> parseData(String result) {//Gson 解析
        ArrayList<AddressBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                AddressBean entity = gson.fromJson(data.optJSONObject(i).toString(), AddressBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }



    /**
     * App28 > 获取端口列表
     */
    private void findPortList(final String token, final String code, final String level, final String index, final String num) {
        if (NetUtil.isNetWorking(ApplyPortActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.findPortListData(token, code, level, index, num, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            if (!mySmart.isLoading()){
                                list.clear();
                            }

                            Bean.PortAll data = new Gson().fromJson(result, Bean.PortAll.class);
                            if (data.status == 1){
                                List<Bean.Port> portList = data.list;
                                for (int i = 0; i < portList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", portList.get(i).name);
                                    map.put("oid", portList.get(i).oid);
                                    map.put("date", portList.get(i).date);
                                    map.put("number", portList.get(i).number);
                                    map.put("portLevel", portList.get(i).portLevel);
                                    map.put("isClick", false);

                                    list.add(map);
                                }
                                adapter.notifyDataSetChanged();
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
     * App30 > 申请加入端口
     */
    private void joinPort(final String token, final String oid) {
        if (NetUtil.isNetWorking(ApplyPortActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.joinPortData(token, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            if (!mySmart.isLoading()){
                                list.clear();
                            }

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                              toast("申请加入成功，等待审核...");
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
     * App29 > 申请端口
     */
    private void applyPort(final String token, final String code, final String level) {
        if (NetUtil.isNetWorking(ApplyPortActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.applyPortData(token, code, level, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            if (!mySmart.isLoading()){
                                list.clear();
                            }

                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (data.status == 1){
                                toast(data.message);
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


}
