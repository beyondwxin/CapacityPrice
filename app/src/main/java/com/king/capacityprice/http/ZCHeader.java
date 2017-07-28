package com.king.capacityprice.http;


import com.king.capacityprice.base.BaseApplication;
import com.king.capacityprice.tinker.SampleApplicationLike;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.DeviceInfo;
import com.king.capacityprice.utils.SharedPreferencesUtil;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by admin on 2016/3/21.
 */
public class ZCHeader extends HashMap implements Serializable {

    public ZCHeader() {
        this.put("version", CommonValues.APP_VERSION);
        this.put("registrationId", SharedPreferencesUtil.getStringValue(SampleApplicationLike.getInstance().getApplication(), CommonValues.REGISTRATION_ID, ""));
        this.put("token", SharedPreferencesUtil.getStringValue(SampleApplicationLike.getInstance().getApplication(), CommonValues.TOKENCODE, ""));
        this.put("device", DeviceInfo.getManufacturer());
        this.put("platform", "android");
    }


}
