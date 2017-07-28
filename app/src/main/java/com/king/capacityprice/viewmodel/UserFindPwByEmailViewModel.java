package com.king.capacityprice.viewmodel;

import android.content.Context;
import android.text.TextUtils;

import com.king.capacityprice.R;
import com.king.capacityprice.base.ModelCallBack;
import com.king.capacityprice.http.HttpCallBack;
import com.king.capacityprice.http.RequestCenter;
import com.king.capacityprice.model.request.UserFindPwByEmail;
import com.king.capacityprice.model.request.UserRegister;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.LogUtil;
import com.king.capacityprice.utils.SharedPreferencesUtil;
import com.king.capacityprice.utils.ToastUtil;
import com.king.capacityprice.utils.dialogUtils.DialogManager;

/**
 * Created by king on 2017/3/21.
 * 找回密码--邮箱modelview
 */
public class UserFindPwByEmailViewModel implements ModelCallBack {
    private Context mContext;

    public UserFindPwByEmailViewModel(Context context) {
        super();
        mContext = context;
    }

    /**
     * 验证填入信息
     *
     * @param result
     * @param email
     * @param mModel
     */
    public void check(String result, String email, UserFindPwByEmail mModel) {
        if (!TextUtils.equals(result, mModel.getAvCode())) {
            ToastUtil.getInstance().toastInCenter(mContext, "请输入正确的激活码");
            return;
        }
        if (!TextUtils.equals(mModel.getEmail(), email)) {
            ToastUtil.getInstance().toastInCenter(mContext, "邮箱地址错误");
            return;
        }
        mModel.setStatus(true);
    }

    /**
     * 获取邮箱激活码
     *
     * @param findPwByEmail
     */
    public void getAVCode(UserFindPwByEmail findPwByEmail, HttpCallBack callBack) {
        RequestCenter.forgetPwByEmailGetAVCode(findPwByEmail, callBack);
    }

    /**
     * 确认修改
     *
     * @param findPwByEmail
     */
    public void doModify(UserFindPwByEmail findPwByEmail, HttpCallBack callBack) {
        RequestCenter.modifyByEmail(findPwByEmail, callBack);
    }

    /**
     * 处理业务逻辑
     *
     * @param model model实体
     */
    @Override
    public void onResultCallback(Object model) {
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
