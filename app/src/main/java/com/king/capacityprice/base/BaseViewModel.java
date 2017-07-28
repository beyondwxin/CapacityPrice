package com.king.capacityprice.base;


import com.king.capacityprice.http.HttpCallBack;
import com.king.capacityprice.http.ZCResponse;

/**
 * Created by king on 2016/8/29.
 */
public class BaseViewModel implements HttpCallBack {
    protected ModelCallBack callback;

    @Override
    public boolean onSuccess(ZCResponse response, String requestUrl, String method) {
        return false;
    }

    @Override
    public boolean onFail(Throwable error, String requestUrl, String method) {
        return false;
    }

//
//    @Override
//    public boolean httpCallBackPreFilter(String result, String method) {
//        return false;
//    }
}
