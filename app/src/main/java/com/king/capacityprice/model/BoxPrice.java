package com.king.capacityprice.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.king.capacityprice.BR;

import java.io.Serializable;


/**
 * Created by king on 2017/3/21.
 * 各箱号对应的价格 20 40 35
 */
public class BoxPrice extends BaseObservable implements Serializable {
    private String price20;
    private String price40;
    private String price35;

    @Bindable
    public String getPrice20() {
        return price20;
    }

    public void setPrice20(String price20) {
        this.price20 = price20;
        notifyPropertyChanged(BR.price20);

    }

    @Bindable
    public String getPrice40() {
        return price40;
    }

    public void setPrice40(String price40) {
        this.price40 = price40;
        notifyPropertyChanged(BR.price40);
    }

    @Bindable
    public String getPrice35() {
        return price35;
    }

    public void setPrice35(String price35) {
        this.price35 = price35;
        notifyPropertyChanged(BR.price35);
    }
}
