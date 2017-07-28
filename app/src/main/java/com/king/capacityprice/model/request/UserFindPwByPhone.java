package com.king.capacityprice.model.request;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.king.capacityprice.BR;

import java.io.Serializable;

/**
 * Created by king on 2017/3/21.
 * 通过手机号找回密码Model
 */
public class UserFindPwByPhone extends BaseObservable implements Serializable {
    private Integer id;
    private String phone;
    private String yzm;
    private String password;
    private String getAuthCode;
    private String confirmPassword;
    private boolean isClick; //获取验证码可点击状态

    private boolean status = false;//步骤状态。。提交和确认修改

    @Bindable
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getYzm() {
        return yzm;
    }

    public void setYzm(String yzm) {
        this.yzm = yzm;
        notifyPropertyChanged(BR.yzm);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getGetAuthCode() {
        return getAuthCode;
    }

    public void setGetAuthCode(String getAuthCode) {
        this.getAuthCode = getAuthCode;
        notifyPropertyChanged(BR.getAuthCode);
    }

    @Bindable
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        notifyPropertyChanged(BR.confirmPassword);
    }

    @Bindable
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
        notifyPropertyChanged(BR.click);
    }
}
