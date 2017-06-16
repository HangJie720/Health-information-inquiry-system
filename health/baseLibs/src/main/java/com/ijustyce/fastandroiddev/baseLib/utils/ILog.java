package com.ijustyce.fastandroiddev.baseLib.utils;

import android.util.Log;

import com.ijustyce.fastandroiddev.baseLib.BuildConfig;

/**
 * Created by yc on 16-2-6. 再次封装的LogCat类，实现在release版本不打印logcat
 */
public class ILog {

    private static boolean showLog = BuildConfig.DEBUG;  //  是否显示，用于release版本不打印log
    private static String tag = "FastAndroidDev";   //  默认的

    public static void i(String tag, String msg) {

        if (showLog) {
            Log.i(tag, msg);
        }
    }

    public static void i(String msg) {

        if (showLog) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {

        if (showLog) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {

        if (showLog) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {

        if (showLog) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg) {

        if (showLog) {
            Log.e(tag, msg);
        }
    }

    public static boolean isShowLog() {
        return showLog;
    }

    public static void setShowLog(boolean showLog) {
        ILog.showLog = showLog;
    }
}
