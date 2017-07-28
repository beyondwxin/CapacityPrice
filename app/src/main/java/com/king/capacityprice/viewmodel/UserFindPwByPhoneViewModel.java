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
import com.king.capacityprice.model.request.UserFindPwByPhone;
import com.king.capacityprice.model.request.UserRegister;
import com.king.capacityprice.tinker.SampleApplicationLike;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.LogUtil;
import com.king.capacityprice.utils.SharedPreferencesUtil;
import com.king.capacityprice.utils.ToastUtil;
import com.king.capacityprice.utils.dialogUtils.DialogManager;

import org.json.JSONObject;

/**
 * Created by king on 2017/3/21.
 * 找回密码--手机号modelview
 */
public class UserFindPwByPhoneViewModel implements ModelCallBack {
    private Context mContext;

    public UserFindPwByPhoneViewModel(Context context) {
        super();
        mContext = context;
    }

    /**
     * 保存，校验手机号
     *
     * @param findPwModel
     */
    public void verifyPhone(UserFindPwByPhone findPwModel, HttpCallBack callBack) {
        RequestCenter.forgetPwByPhone(findPwModel, callBack);
    }


    /**
     * @param findPwByPhone
     */
    public void checkPw(UserFindPwByPhone findPwByPhone) {
        if (!TextUtils.equals(findPwByPhone.getPassword(), findPwByPhone.getConfirmPassword())) {
            DialogManager.getInstance().dissMissDialog();
            ToastUtil.getInstance().toastInCenter(mContext, "两次密码不一致");
            return;
        }

    }

    /**
     * 确认修改
     *
     * @param findPwModel
     */
    public void doModify(UserFindPwByPhone findPwModel, HttpCallBack callBack) {
        RequestCenter.modifyByPhone(findPwModel, callBack);
    }

    /**
     * mob-号码检测
     *
     * @param mModel
     */
    public void verifyMun(final UserFindPwByPhone mModel) {
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
    public void verifyCode(final UserFindPwByPhone mModel) {
        SMSManager.getInstance().verifyCode(SampleApplicationLike.getInstance().getApplication(), "86", mModel.getPhone(), mModel.getYzm(), new SMSCallback() {
            @Override
            public void success() {
                mModel.setStatus(true);
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
