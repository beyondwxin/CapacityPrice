package com.king.capacityprice.model.request;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.king.capacityprice.BR;

import java.io.Serializable;


/**
 * Created by king on 2016/89/18.
 * 登录Model
 * <p>
 * "id": 19,
 * "description": null,
 * "status": null,
 * "password": "111",
 * "mpVerificationCodeSendTime": 1490837575000,
 * "mpNum": "13521301642",
 * "email": "king782515516@163.com",
 * "createUserId": null,
 * "createTime": null,
 * "updateUserId": null,
 * "updateTime": null,
 * "mpVerificationCode": "1111",
 * "registerTime": 1490837575000,
 * "lastLoginTime": null
 */
public class UserInfo extends BaseObservable implements Serializable {
    private Integer id;
    private String mpNum;
    private String password;
    private String description;
    private String status;
    private String mpVerificationCodeSendTime;
    private String email;
    private String createUserId;
    private String updateTime;
    private String createTime;
    private String updateUserId;
    private String mpVerificationCode;
    private String registerTime;
    private String lastLoginTime;


    @Bindable
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getMpNum() {
        return mpNum;
    }

    public void setMpNum(String mpNum) {
        this.mpNum = mpNum;
        notifyPropertyChanged(BR.mpNum);
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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public String getMpVerificationCodeSendTime() {
        return mpVerificationCodeSendTime;
    }

    public void setMpVerificationCodeSendTime(String mpVerificationCodeSendTime) {
        this.mpVerificationCodeSendTime = mpVerificationCodeSendTime;
        notifyPropertyChanged(BR.mpVerificationCodeSendTime);
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
    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
        notifyPropertyChanged(BR.createUserId);
    }

    @Bindable
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        notifyPropertyChanged(BR.updateTime);
    }

    @Bindable
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
        notifyPropertyChanged(BR.createTime);
    }

    @Bindable
    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
        notifyPropertyChanged(BR.updateUserId);
    }

    @Bindable
    public String getMpVerificationCode() {
        return mpVerificationCode;
    }

    public void setMpVerificationCode(String mpVerificationCode) {
        this.mpVerificationCode = mpVerificationCode;
        notifyPropertyChanged(BR.mpVerificationCode);
    }

    @Bindable
    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
        notifyPropertyChanged(BR.registerTime);
    }

    @Bindable
    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        notifyPropertyChanged(BR.lastLoginTime);
    }
}
