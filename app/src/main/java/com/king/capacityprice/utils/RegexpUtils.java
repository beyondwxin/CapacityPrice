package com.king.capacityprice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by king on 2017/3/29.
 * 正则工具类
 */

public final class RegexpUtils {
    private RegexpUtils() {
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static final boolean isEmail(String email) {
        String str = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

}