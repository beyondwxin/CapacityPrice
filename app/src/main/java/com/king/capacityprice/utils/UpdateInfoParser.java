package com.king.capacityprice.utils;

import android.util.Xml;


import com.king.capacityprice.view.pojo.UpdateInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * Created by king on 2016/12/7.
 */

public class UpdateInfoParser {
    public static UpdateInfo getUpdataInfo(InputStream is) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");
        int type = parser.getEventType();
        UpdateInfo info = new UpdateInfo();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("version".equals(parser.getName())) {
                        info.setVersion(parser.nextText());
                    } else if ("apk_url".equals(parser.getName())) {
                        info.setApk_url(parser.nextText());
                    } else if ("description".equals(parser.getName())) {
                        info.setDescription(parser.nextText());
                    }
                    break;
            }
            type = parser.next();
        }
        return info;
    }
}