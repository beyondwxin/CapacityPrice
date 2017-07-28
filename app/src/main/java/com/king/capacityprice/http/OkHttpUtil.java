package com.king.capacityprice.http;

import android.app.Activity;
import android.os.Looper;

import com.king.capacityprice.R;
import com.king.capacityprice.base.BaseApplication;
import com.king.capacityprice.tinker.SampleApplicationLike;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.LogUtil;
import com.king.capacityprice.utils.ToastUtil;
import com.king.capacityprice.utils.dialogUtils.DialogManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class OkHttpUtil {

    private String TAG = "OkHttpUtil";
    private static OkHttpUtil okHttpUtil;
    public final static long CONNECT_TIMEOUT = 10000L;
    public final static long READ_TIMEOUT = 10000L;


    public static OkHttpUtil getInstance() {
        if (okHttpUtil == null) {
            synchronized (OkHttpUtil.class) {
                okHttpUtil = new OkHttpUtil();
            }
        }
        return okHttpUtil;
    }

    /**
     * 后台访问
     * fileMap为空表示上传单纯的参数，反之上传文件(附参数)
     *
     * @param zCRequest
     * @param fileMap
     * @param callBack
     */
    public void okHttpPost(final ZCRequest zCRequest, final Map<String, File> fileMap, final HttpCallBack callBack) {
        if (!SampleApplicationLike.getInstance().isNetworkAvailable((Activity) callBack)) {
            ToastUtil.getInstance().toastInCenter(SampleApplicationLike.getInstance().getApplication(), R.string.toast_netadvice);
            DialogManager.getInstance().dissMissDialog();
            return;
        }
        String url = Url.BASEURL + zCRequest.readMethod();
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Map<String, Object> header = zCRequest.getHeader();
        Map<String, Object> params = zCRequest.getRequest().getParamsMap();
        // 若只上传参数
        if (fileMap == null) {
            FormBody.Builder formBody = new FormBody.Builder();
            for (Map.Entry entry : header.entrySet()) {
                formBody.add(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
            for (Map.Entry entry : params.entrySet()) {
                formBody.add(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
            RequestBody body = formBody.build();
            request = new Request.Builder().url(url).post(body).tag(this).build();
        } else {
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            if (params != null) {
                // map 里面是请求中所需要的 key 和 value
                for (Map.Entry entry : params.entrySet()) {
                    requestBody.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }
            }
            for (String key : fileMap.keySet()) {
                //userid+字段名称用base64编码+.jpg
                File file = fileMap.get(key);
                requestBody.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
//                String filename = new String(Base64.encode((SUserInfo.getUserInfoInstance().getId() + key).getBytes(), Base64.DEFAULT)) + ".jpg";
//                RequestBody body = RequestBody.create(MediaType.parse("image/*"), fileMap.get(key));
//                requestBody.addFormDataPart(key, filename, body);
            }
            RequestBody body = requestBody.build();
            request = new Request.Builder().url(url).post(body).tag(this).build();
        }
        client.newBuilder().readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS).connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("onFailure：", "url:" + zCRequest.readUrl() + "--method:" + zCRequest.readMethod() + "--result:" + call.toString());
                Looper.prepare();
                callBack.onFail(e, zCRequest.readUrl(), zCRequest.readMethod());
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    LogUtil.e("onResponse：", "url:" + zCRequest.readUrl() + "--method:" + zCRequest.readMethod() + "--result:" + str);

                    ZCResponse zCResponse = null;
                    try {
                        zCResponse = MyJSON.parseObject(str, ZCResponse.class);
                        if (httpStatusFilter(zCResponse)) {
                            Looper.prepare();
                            callBack.onSuccess(zCResponse, zCRequest.readUrl(), zCRequest.readMethod());
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            LogUtil.e("serverException", "服务器异常！");
                            callBack.onFail(new Exception(), zCRequest.readUrl(), zCRequest.readMethod());
                            Looper.loop();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        DialogManager.getInstance().dissMissDialog();
                        LogUtil.e("dataException:", "url:" + zCRequest.readUrl() + "--method:" + zCRequest.readMethod() + "--exception:" + e.getMessage());
                    }
                } else {
                    Looper.prepare();
                    callBack.onFail(new Exception(), zCRequest.readUrl(), zCRequest.readMethod());
                    Looper.loop();
                    LogUtil.e("responseException：", "url:" + zCRequest.readUrl() + "--method:" + zCRequest.readMethod() + "--result:" + call.toString());
                }
            }
        });
    }

    /**
     * 状态过滤
     *
     * @param response
     * @return
     */
    public static boolean httpStatusFilter(ZCResponse response) {
        return response.getStatus().equals(CommonValues.SUCCESS_STATUS);
    }
}
