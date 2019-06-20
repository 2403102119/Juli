package com.tcckj.juli.view;


import com.tcckj.juli.entity.AddressBean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */

public interface PickAddressInterface {
    abstract void onOkClick(String mProvinceName, String mCityName, String mCountyName, String mCurrentName, List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> beans);

    abstract void onCancelClick();
}
