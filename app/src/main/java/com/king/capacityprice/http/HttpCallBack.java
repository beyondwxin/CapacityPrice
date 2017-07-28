package com.king.capacityprice.http;

/**
 * Http请求回调接口
 */
public interface HttpCallBack {
    boolean onSuccess(ZCResponse response, String requestUrl, String method);

    boolean onFail(Throwable error, String requestUrl, String method);

}