package com.king.capacityprice.view.pojo;

/**
 * Created by king on 2016/12/7.
 * 版本更新model
 */

public class UpdateInfo {
    private String version;//服务器最新版本
    private String url_server;//服务器地址
    private String apk_url;//apk下载地址
    private String description;//提示信息

    public String getUrl_server() {
        return url_server;
    }

    public void setUrl_server(String url_server) {
        this.url_server = url_server;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}