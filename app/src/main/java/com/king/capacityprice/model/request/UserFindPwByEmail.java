package com.king.capacityprice.model.request;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


import java.io.Serializable;

import com.king.capacityprice.BR;


/**
 * Created by king on 2017/3/21.
 * 通过邮箱找回密码Model
 */
public class UserFindPwByEmail extends BaseObservable implements Serializable {
    private Integer id;
    private String email;
    private String avCode;
    private String password;
    private String confirmPassword;
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
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getAvCode() {
        return avCode;
    }

    public void setAvCode(String avCode) {
        this.avCode = avCode;
        notifyPropertyChanged(BR.avCode);
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
}
