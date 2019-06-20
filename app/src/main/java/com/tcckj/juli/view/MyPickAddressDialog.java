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

public class MyPickAddressDialog {

    public void showPickAddressDialog(final Activity ctx) {
        Dialog dialog = new Dialog(ctx, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(ctx).inflate(R.layout.address, null);

        initView(view, dialog);//实例化控件

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

    public void showPickAddressDialog(final Activity ctx, int def) {
        Dialog dialog = new Dialog(ctx, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(ctx).inflate(R.layout.address, null);

        initView(view, dialog);//实例化控件

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
    private static String mCurrentProviceName, mCurrentCityName, mCurrentDistrictName, mCurrentStreetName;
    private static MyPickAddressInterface pickAddressInterface;
    private List<String> firstList;
    private List<String> secondList;
    private List<String> thirdList;
    private List<String> fourthList = new ArrayList<>();

    private void initView(View view, final Dialog dialog) {
        cancel = (TextView) view.findViewById(R.id.box_cancel);
        ok = (TextView) view.findViewById(R.id.box_ok);
        mProvincePicker = (WheelView) view.findViewById(R.id.province);
        mCityPicker = (WheelView) view.findViewById(R.id.city);
        mCountyPicker = (WheelView) view.findViewById(R.id.county);
        mStreetPicker = (WheelView) view.findViewById(R.id.street);

            mProvincePicker.setVisibility(View.VISIBLE);
            mCityPicker.setVisibility(View.VISIBLE);
            mCountyPicker.setVisibility(View.VISIBLE);
            mStreetPicker.setVisibility(View.GONE);


        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String provinceText = firstList.get(id);
                if (!mCurrentProviceName.equals(provinceText)) {
                    mCurrentProviceName = provinceText;

                    secondList.clear();
                    for (int i = 0; i < 20; i++) {
                        secondList.add("市a"+i);
                    }

                    ArrayList<String> mCityData = new ArrayList<>();
                    for (int i = 0; i < secondList.size(); i++) {
                        mCityData.add(secondList.get(i));
                    }
                    mCityPicker.resetData(mCityData);
                    Log.i("1111111111", "endSelect: " + provinceText + "-------:" + mCityData.size());
                    mCityPicker.setDefault(0);
                    if (mCityData.size() > 0) {
                        mCurrentCityName = mCityData.get(0);
                    }

                    /*if (secondList.size() > 0) {

                        ArrayList<String> mCountyData = new ArrayList<>();
                        for (int i = 0; i < thirdList.size(); i++) {
                            mCountyData.add(thirdList.get(i));
                        }
                        mCountyPicker.resetData(mCountyData);
                        mCountyPicker.setDefault(0);
                        mCurrentDistrictName = mCountyData.get(0);

                        ArrayList<String> mStreetData = new ArrayList<>();
                        for (int i = 0; i < fourthList.size(); i++) {
                            mStreetData.add(fourthList.get(i));
                        }
                        mStreetPicker.resetData(mStreetData);
                        mStreetPicker.setDefault(0);
                        mCurrentStreetName = mStreetData.get(0);
                    }*/
                }
            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        mCityPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {

                thirdList.clear();
                for (int i = 0; i < 20; i++) {
                    thirdList.add("县a"+i);
                }

                ArrayList<String> mCountyData = new ArrayList<>();
                for (int i = 0; i < thirdList.size(); i++) {
                    mCountyData.add(thirdList.get(i));
                }
                mCountyPicker.resetData(mCountyData);
                mCountyPicker.setDefault(0);
                if (mCountyData.size() > 0) {
                    mCurrentDistrictName = mCountyData.get(0);
                }

                /*if (thirdList.size() > 0) {
                    ArrayList<String> mStreetData = new ArrayList<>();
                    for (int i = 0; i < fourthList.size(); i++) {
                        mStreetData.add(fourthList.get(i));
                    }
                    mStreetPicker.resetData(mStreetData);
                    mStreetPicker.setDefault(0);
                    mCurrentStreetName = mStreetData.get(0);
                }*/
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mCountyPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String currentname = thirdList.get(id);
                if (!mCurrentDistrictName.equals(currentname)) {
                    mCurrentDistrictName = currentname;

                    ArrayList<String> mStreetData = new ArrayList<>();
                    for (int i = 0; i < fourthList.size(); i++) {
                        mStreetData.add(fourthList.get(i));
                    }
                    /*mStreetPicker.resetData(mStreetData);
                    mStreetPicker.setDefault(0);
                    mCurrentStreetName = mStreetData.get(0);*/

                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mStreetPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                mCurrentStreetName = fourthList.get(id);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickAddressInterface != null) {
                        pickAddressInterface.onOkClick(firstList.get(mProvincePicker.getSelected())
                                , mCityPicker.getSelectedText()
                                , mCountyPicker.getSelectedText(), "", fourthList);
                        dialog.dismiss();
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

    public static void setOnTopClicklistener(MyPickAddressInterface pickAddress) {
        pickAddressInterface = pickAddress;
    }

    public void setFirstData(ArrayList<String> list){
        firstList = list;
        mProvincePicker.setData(list);
        mProvincePicker.setDefault(0);
        mCurrentProviceName = firstList.get(0);
    }

    public void setSecondData(ArrayList<String> list){
        secondList = list;
        mCityPicker.setData(list);
        mCityPicker.setDefault(0);
        mCurrentCityName = secondList.get(0);
    }

    public void setThirdData(ArrayList<String> list){
        thirdList = list;
        mCountyPicker.setData(list);
        mCountyPicker.setDefault(0);
        mCurrentDistrictName = thirdList.get(0);
    }

    /*public void setData(List<String> beans) {
        firstList = beans;
        secondList.clear();

        for (int i = 0; i < firstList.size(); i++) {
            mProvinceDatas.add(firstList.get(i).getName());
        }
        mProvincePicker.setData(mProvinceDatas);
        mProvincePicker.setDefault(0);
        mCurrentProviceName = mProvinceDatas.get(0);

        secondList.addAll(firstList.get(0).getChilds());
        ArrayList<String> mCityData = new ArrayList<>();
        for (int i = 0; i < secondList.size(); i++) {
            mCityData.add(secondList.get(i).getName());
        }
        mCityPicker.setData(mCityData);
        mCityPicker.setDefault(0);
        mCurrentCityName = mCityData.get(0);

        thirdList = secondList.get(0).getChilds();
        ArrayList<String> mDistrictData = new ArrayList<>();
        for (int i = 0; i < thirdList.size(); i++) {
            mDistrictData.add(thirdList.get(i).getName());
        }
        mCountyPicker.setData(mDistrictData);
        mCountyPicker.setDefault(0);
        mCurrentDistrictName = mDistrictData.get(0);

        fourthList = thirdList.get(0).getChilds();
        ArrayList<String> mStreetData = new ArrayList<>();
        for (int i = 0; i < fourthList.size(); i++) {
            mStreetData.add(fourthList.get(i).getName());
        }
        mStreetPicker.setData(mStreetData);
        mStreetPicker.setDefault(0);
        mCurrentStreetName = mStreetData.get(0);
    }*/

    public void onDistory() {
        firstList.clear();
        secondList.clear();
        thirdList.clear();
        fourthList.clear();
        mProvincePicker.resetData(new ArrayList<String>());
        mCountyPicker.resetData(new ArrayList<String>());
        mStreetPicker.resetData(new ArrayList<String>());
        mCountyPicker.resetData(new ArrayList<String>());
    }
}
