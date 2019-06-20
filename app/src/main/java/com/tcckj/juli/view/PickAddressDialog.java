package com.tcckj.juli.view;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tcckj.juli.R;
import com.tcckj.juli.entity.AddressBean;

import java.util.ArrayList;
import java.util.List;


/**
 * kylin on 2018/1/5.
 */

public class PickAddressDialog {

    public static void showPickAddressDialog(final Activity ctx, String state, List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> streetChildsBeans) {
        Dialog dialog = new Dialog(ctx, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(ctx).inflate(R.layout.address, null);

        initView(view, dialog, state,0, streetChildsBeans);//实例化控件

        dialog.setContentView(view);//将布局设置给Dialog
        Window dialogWindow = dialog.getWindow();//获取当前Activity所在的窗体
        dialogWindow.setGravity(Gravity.BOTTOM);//设置Dialog从窗体底部弹出

        WindowManager windowManager = ctx.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public static void showPickAddressDialog(final Activity ctx, String state,int def, List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> streetChildsBeans) {
        Dialog dialog = new Dialog(ctx, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(ctx).inflate(R.layout.address, null);

        initView(view, dialog, state,def, streetChildsBeans);//实例化控件

        dialog.setContentView(view);//将布局设置给Dialog
        Window dialogWindow = dialog.getWindow();//获取当前Activity所在的窗体
        dialogWindow.setGravity(Gravity.BOTTOM);//设置Dialog从窗体底部弹出

        WindowManager windowManager = ctx.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


    private static TextView cancel, ok;
    private static WheelView mProvincePicker, mCityPicker, mCountyPicker, mStreetPicker;
    private static ArrayList<String> mProvinceDatas = new ArrayList<>();
    private static String mCurrentProviceName, mCurrentCityName, mCurrentDistrictName, mCurrentStreetName;
    private static PickAddressInterface pickAddressInterface;
    private static List<AddressBean> addressBeanList;
    private static List<AddressBean.CityChildsBean> cityChildsBeans = new ArrayList<>();
    private static List<AddressBean.CityChildsBean.CountyChildsBean> countyChildsBeans = new ArrayList<>();
    private static List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> streetChildsBeans = new ArrayList<>();

    private static void initView(View view, final Dialog dialog, final String state,final int def, List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> streetData) {
        cancel = (TextView) view.findViewById(R.id.box_cancel);
        ok = (TextView) view.findViewById(R.id.box_ok);
        mProvincePicker = (WheelView) view.findViewById(R.id.province);
        mCityPicker = (WheelView) view.findViewById(R.id.city);
        mCountyPicker = (WheelView) view.findViewById(R.id.county);
        mStreetPicker = (WheelView) view.findViewById(R.id.street);

        if (state.equals("1")) {
            mProvincePicker.setVisibility(View.VISIBLE);
            mCityPicker.setVisibility(View.VISIBLE);
            mCountyPicker.setVisibility(View.VISIBLE);
            mStreetPicker.setVisibility(View.GONE);
        } else {
            mProvincePicker.setVisibility(View.GONE);
            mCityPicker.setVisibility(View.GONE);
            mCountyPicker.setVisibility(View.GONE);
            mStreetPicker.setVisibility(View.VISIBLE);
            streetChildsBeans = streetData;
            ArrayList<String> streetDatas = new ArrayList<>();
            for (int i = 0; i < streetChildsBeans.size(); i++) {
                streetDatas.add(streetChildsBeans.get(i).getName());
            }
            mStreetPicker.resetData(streetDatas);
            if (def>0){
                mStreetPicker.setDefault(def-1);
            }else {
                mStreetPicker.setDefault(0);
            }
            mCurrentStreetName = streetDatas.get(0);
        }


        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String provinceText = mProvinceDatas.get(id);
                if (!mCurrentProviceName.equals(provinceText)) {
                    mCurrentProviceName = provinceText;
                    ArrayList<String> mCityData = new ArrayList<>();
                    cityChildsBeans = addressBeanList.get(id).getChilds();
                    Log.i("111111", "endSelect: " + id);
                    Log.i("111111", "endSelect: " + cityChildsBeans.get(0).getName());
                    for (int i = 0; i < cityChildsBeans.size(); i++) {
                        mCityData.add(cityChildsBeans.get(i).getName());
                    }
                    mCityPicker.resetData(mCityData);
                    mCityPicker.setDefault(0);
                    if (mCityData.size() > 0) {
                        mCurrentCityName = mCityData.get(0);
                    }

                    if (cityChildsBeans.size() > 0) {

                        ArrayList<String> mCountyData = new ArrayList<>();
                        countyChildsBeans = cityChildsBeans.get(0).getChilds();
                        for (int i = 0; i < countyChildsBeans.size(); i++) {
                            mCountyData.add(countyChildsBeans.get(i).getName());
                        }
                        mCountyPicker.resetData(mCountyData);
                        mCountyPicker.setDefault(0);
                        mCurrentDistrictName = mCountyData.get(0);

                        ArrayList<String> mStreetData = new ArrayList<>();
                        streetChildsBeans = countyChildsBeans.get(0).getChilds();
                        for (int i = 0; i < streetChildsBeans.size(); i++) {
                            mStreetData.add(streetChildsBeans.get(i).getName());
                        }
                        mStreetPicker.resetData(mStreetData);
                        mStreetPicker.setDefault(0);
                        mCurrentStreetName = mStreetData.get(0);
                    }
                }
            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        mCityPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                ArrayList<String> mCountyData = new ArrayList<>();
                countyChildsBeans = cityChildsBeans.get(id).getChilds();
                for (int i = 0; i < countyChildsBeans.size(); i++) {
                    mCountyData.add(countyChildsBeans.get(i).getName());
                }
                mCountyPicker.resetData(mCountyData);
                mCountyPicker.setDefault(0);
                if (mCountyData.size() > 0) {
                    mCurrentDistrictName = mCountyData.get(0);
                }

                if (countyChildsBeans.size() > 0) {
                    streetChildsBeans = countyChildsBeans.get(0).getChilds();
                    ArrayList<String> mStreetData = new ArrayList<>();
                    for (int i = 0; i < streetChildsBeans.size(); i++) {
                        mStreetData.add(streetChildsBeans.get(i).getName());
                    }
                    mStreetPicker.resetData(mStreetData);
                    mStreetPicker.setDefault(0);
                    mCurrentStreetName = mStreetData.get(0);
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mCountyPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String currentname = countyChildsBeans.get(id).getName();
                if (!mCurrentDistrictName.equals(currentname)) {
                    mCurrentDistrictName = currentname;

                    streetChildsBeans = countyChildsBeans.get(id).getChilds();
                    ArrayList<String> mStreetData = new ArrayList<>();
                    for (int i = 0; i < streetChildsBeans.size(); i++) {
                        mStreetData.add(streetChildsBeans.get(i).getName());
                    }
                    mStreetPicker.resetData(mStreetData);
                    mStreetPicker.setDefault(0);
                    mCurrentStreetName = mStreetData.get(0);

                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mStreetPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                mCurrentStreetName = streetChildsBeans.get(id).getName();
            }

            @Override
            public void selecting(int id, String text) {

            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickAddressInterface != null) {
                    if (state.equals("1")) {
                        pickAddressInterface.onOkClick(mProvinceDatas.get(mProvincePicker.getSelected())
                                , mCityPicker.getSelectedText()
                                , mCountyPicker.getSelectedText(), "", streetChildsBeans);
                        dialog.dismiss();
                    } else {
                        pickAddressInterface.onOkClick("", "", "", mCurrentStreetName, streetChildsBeans);
                        dialog.dismiss();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickAddressInterface != null) {
                    pickAddressInterface.onCancelClick();
                    dialog.dismiss();
                }
            }
        });
    }

    public static void setOnTopClicklistener(PickAddressInterface pickAddress) {
        pickAddressInterface = pickAddress;
    }

    public static void setData(List<AddressBean> beans) {
        addressBeanList = beans;
        cityChildsBeans.clear();

        for (int i = 0; i < addressBeanList.size(); i++) {
            mProvinceDatas.add(addressBeanList.get(i).getName());
        }
        mProvincePicker.setData(mProvinceDatas);
        mProvincePicker.setDefault(0);
        mCurrentProviceName = mProvinceDatas.get(0);

        cityChildsBeans.addAll(addressBeanList.get(0).getChilds());
        ArrayList<String> mCityData = new ArrayList<>();
        for (int i = 0; i < cityChildsBeans.size(); i++) {
            mCityData.add(cityChildsBeans.get(i).getName());
        }
        mCityPicker.setData(mCityData);
        mCityPicker.setDefault(0);
        mCurrentCityName = mCityData.get(0);

        countyChildsBeans = cityChildsBeans.get(0).getChilds();
        ArrayList<String> mDistrictData = new ArrayList<>();
        for (int i = 0; i < countyChildsBeans.size(); i++) {
            mDistrictData.add(countyChildsBeans.get(i).getName());
        }
        mCountyPicker.setData(mDistrictData);
        mCountyPicker.setDefault(0);
        mCurrentDistrictName = mDistrictData.get(0);

        streetChildsBeans = countyChildsBeans.get(0).getChilds();
        ArrayList<String> mStreetData = new ArrayList<>();
        for (int i = 0; i < streetChildsBeans.size(); i++) {
            mStreetData.add(streetChildsBeans.get(i).getName());
        }
        mStreetPicker.setData(mStreetData);
        mStreetPicker.setDefault(0);
        mCurrentStreetName = mStreetData.get(0);
    }

    public void onDestory() {
        addressBeanList.clear();
        cityChildsBeans.clear();
        countyChildsBeans.clear();
        streetChildsBeans.clear();
        mProvincePicker.resetData(new ArrayList<String>());
        mCountyPicker.resetData(new ArrayList<String>());
        mStreetPicker.resetData(new ArrayList<String>());
        mCountyPicker.resetData(new ArrayList<String>());
    }
}
