package com.king.capacityprice.base;

/**
 * Created by 伍新 on 2016/8/29.
 */
public interface ModelCallBack<T> {
    void onResultCallback(T model);

    void onFailCallback(T err);

    void onMultipleResultCallBack(String method, T... model);
}
