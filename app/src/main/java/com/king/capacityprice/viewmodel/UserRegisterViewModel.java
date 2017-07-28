package com.king.capacityprice.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.king.capacityprice.R;
import com.king.capacityprice.base.BaseApplication;
import com.king.capacityprice.base.ModelCallBack;
import com.king.capacityprice.base.SMSCallback;
import com.king.capacityprice.base.SMSManager;
import com.king.capacityprice.http.HttpCallBack;
import com.king.capacityprice.http.RequestCenter;
import com.king.capacityprice.model.request.UserRegister;
import com.king.capacityprice.tinker.SampleApplicationLike;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.SharedPreferencesUtil;
import com.king.capacityprice.utils.ToastUtil;
import com.king.capacityprice.utils.dialogUtils.DialogManager;

import org.json.JSONObject;

/**
 * Created by king on 2017/3/21.
 * 注册modelview
 */
public class UserRegisterViewModel implements ModelCallBack {
    private Context mContext;

    public UserRegisterViewModel(Context context) {
        super();
        mContext = context;
    }

    /**
     * mob-号码检测
     *
     * @param mModel
     */
    public void verifyMun(final UserRegister mModel) {
        SMSManager.getInstance().verifyNum(SampleApplicationLike.getInstance().getApplication(), "86", mModel.getPhone(), new SMSCallback() {
            @Override
            public void success() {
                Toast.makeText(SampleApplicationLike.getInstance().getApplication(), "验证码已发送", Toast.LENGTH_SHORT).show();
                mModel.setClick(false);
            }

            @Override
            public void error(Throwable throwable) {
                try {
                    mModel.setClick(true);
                    int status = 0;
                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (status == 468) {
                        Toast.makeText(SampleApplicationLike.getInstance().getApplication(), mContext.getString(R.string.toast_codeError), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(SampleApplicationLike.getInstance().getApplication(), des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * mob-验证码检测
     *
     * @param mModel
     */
    public void verifyCode(final UserRegister mModel, final UserRegisterViewModel mViewModel, final HttpCallBack callBack) {
        SMSManager.getInstance().verifyCode(SampleApplicationLike.getInstance().getApplication(), "86", mModel.getPhone(), mModel.getYzm(), new SMSCallback() {
            @Override
            public void success() {
                DialogManager.getInstance().showProgressDialog(mContext, "正在注册");
                mViewModel.doRegister(mModel, callBack);
            }

            @Override
            public void error(Throwable throwable) {
                try {
                    int status = 0;
                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (status == 468) {
                        Toast.makeText(SampleApplicationLike.getInstance().getApplication(), mContext.getString(R.string.toast_codeError), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(SampleApplicationLike.getInstance().getApplication(), des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }


    /**
     * @param userRegister
     */
    public void doRegister(UserRegister userRegister, HttpCallBack callBack) {
        if (!TextUtils.equals(userRegister.getPassword(), userRegister.getConfirmPassword())) {
            DialogManager.getInstance().dissMissDialog();
            ToastUtil.getInstance().toastInCenter(mContext, "两次密码不一致");
            return;
        }
//        JPushInterface.setAliasAndTags(BaseApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.getStringValue(mContext, CommonValues.REGISTRATION_ID, ""), null);
        RequestCenter.requestRegister(userRegister, callBack);
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
