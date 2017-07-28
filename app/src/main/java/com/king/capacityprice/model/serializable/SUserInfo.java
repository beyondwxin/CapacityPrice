package com.king.capacityprice.model.serializable;


import com.king.capacityprice.base.BaseApplication;
import com.king.capacityprice.model.request.UserInfo;
import com.king.capacityprice.model.request.UserRegister;
import com.king.capacityprice.tinker.SampleApplicationLike;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.SharedPreferencesUtil;

import java.io.Serializable;

/**
 * 序列化用户信息
 * Created by king on 2016/10/9.
 */
public class SUserInfo implements Serializable {
    private static UserInfo userInfo;

    public static UserInfo getUserInfoInstance() {
        userInfo = (UserInfo) SharedPreferencesUtil.readObject(SampleApplicationLike.getInstance().getApplication(), CommonValues.USERINFO);
        return userInfo;
    }
}
