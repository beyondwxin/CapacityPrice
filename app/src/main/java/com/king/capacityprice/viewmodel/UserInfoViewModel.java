package com.king.capacityprice.viewmodel;

import android.content.Context;
import android.text.TextUtils;

import com.king.capacityprice.R;
import com.king.capacityprice.base.ModelCallBack;
import com.king.capacityprice.http.HttpCallBack;
import com.king.capacityprice.http.RequestCenter;
import com.king.capacityprice.model.request.UserInfo;
import com.king.capacityprice.model.request.UserRegister;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.SharedPreferencesUtil;
import com.king.capacityprice.utils.ToastUtil;
import com.king.capacityprice.utils.dialogUtils.DialogManager;

/**
 * Created by king on 2017/3/21.
 * 登录modelview
 */
public class UserInfoViewModel implements ModelCallBack {
    private Context mContext;

    public UserInfoViewModel(Context context) {
        super();
        mContext = context;
    }

    /**
     * @param userInfo
     */
    public void doLogin(UserInfo userInfo, HttpCallBack callBack) {
        //绑定设备标识
        if (TextUtils.isEmpty(userInfo.getMpNum()) || TextUtils.isEmpty(userInfo.getPassword())) {
            DialogManager.getInstance().dissMissDialog();
            ToastUtil.getInstance().toastInCenter(mContext, "请输入手机号或密码");
            return;
        }
//        JPushInterface.setAliasAndTags(BaseApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.getStringValue(mContext, CommonValues.REGISTRATION_ID, ""), null);
        RequestCenter.login(userInfo, callBack);
    }

    /**
     * 处理业务逻辑
     *
     * @param model model实体
     */
    @Override
    public void onResultCallback(Object model) {
        UserInfo userInfo = (UserInfo) model;
        SharedPreferencesUtil.saveObject(mContext, CommonValues.USERINFO, userInfo);
    }

    @Override
    public void onFailCallback(Object err) {
        DialogManager.getInstance().dissMissDialog();
        ToastUtil.getInstance().toastInCenter(mContext, R.string.serverException);
    }

    @Override
    public void onMultipleResultCallBack(String method, Object[] model) {

    }

}
