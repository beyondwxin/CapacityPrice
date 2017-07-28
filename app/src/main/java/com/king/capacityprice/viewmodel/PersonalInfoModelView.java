package com.king.capacityprice.viewmodel;

import com.king.capacityprice.base.ModelCallBack;
import com.king.capacityprice.http.HttpCallBack;
import com.king.capacityprice.http.RequestCenter;

/**
 * Created by king on 2016/11/23.
 * 个人资料modelView
 */
public class PersonalInfoModelView implements ModelCallBack {
    private static PersonalInfoModelView mInstance;

    private PersonalInfoModelView() {

    }

    public static PersonalInfoModelView getmInstance() {
        if (mInstance == null) {
            synchronized (PersonalInfoModelView.class) {
                mInstance = new PersonalInfoModelView();
            }
        }
        return mInstance;
    }

//    /**
//     * 获取个人资料
//     *
//     * @param phone
//     * @param callBack
//     */
//    public void getPersonalInfo(String phone, HttpCallBack callBack) {
//        RequestCenter.getPersonalInfo(phone, callBack);
//    }

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

    }

    @Override
    public void onMultipleResultCallBack(String method, Object[] model) {

    }
}
