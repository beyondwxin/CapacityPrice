# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\androidSdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn javax.swing.**
-keep public class javax.swing.**{*;}

-dontwarn javax.rmi.**
-keep public class javax.rmi.**{*;}

-dontwarn org.zeroturnaround.**
-keep public class org.zeroturnaround.**{*;}

-dontwarn org.jaxen.**
-keep public class org.jaxen.**{*;}

-dontwarn com.sun.org.apache.**
-keep public class com.sun.org.apache.**{*;}

-dontwarn org.jdom.**
-keep public class org.jdom.**{*;}

-dontwarn javax.servlet.**
-keep public class javax.servlet.**{*;}

-dontwarn javax.el.**
-keep public class javax.el.**{*;}

-dontwarn org.python.**
-keep public class org.python.**{*;}

-dontwarn java.beans.**
-keep public class java.beans.**{*;}

-dontwarn org.apache.**
-keep public class org.apache.**{*;}

-dontwarn org.w3c.**
-keep public class org.w3c.**{*;}

-dontwarn org.mozilla.**
-keep public class org.mozilla.**{*;}

-dontwarn java.lang.**
-keep public class java.lang.**{*;}

-dontwarn org.dom4j.**
-keep public class org.dom4j.**{*;}

-dontwarn java.util.**
-keep public class java.util.**{*;}

-dontwarn org.slf4j.**
-keep public class org.slf4j.**{*;}

-dontwarn java.nio.file.**
-keep public class java.nio.file.**{*;}

-dontwarn org.codehaus.**
-keep public class org.codehaus.**{*;}

-dontwarn com.alibaba.fastjson.**
-keep public class com.alibaba.fastjson.**{*;}

-dontwarn net.sf.json.**
-keep public class et.sf.json.**{*;}

-dontwarn freemarker.debug.**
-keep public class freemarker.debug.**{*;}

-dontwarn freemarker.ext.**
-keep public class freemarker.ext.**{*;}

-dontwarn freemarker.template.**
-keep public class freemarker.template.**{*;}

-dontwarn freemarker.core.**
-keep public class freemarker.core.**{*;}


-dontwarn com.squareup.okhttp.**
-keep public class com.squareup.okhttp.**{*;}

-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

## 避免影响升级功能，需要keep住support包的类
-keep class android.support.**{*;}

-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

-keep   class com.amap.api.services.**{*;}

-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}


-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#保持http下的所有类不被混淆(以防反射到实体类中属性部分为空情况，这里混淆整个http包下)
-keep class com.king.capacityprice.http.*{*;}
-keep class com.king.capacityprice.view.pojo.**{*;}