package com.ijustyce.school.http;

import android.content.Context;

import com.ijustyce.fastandroiddev.net.HttpListener;
import com.ijustyce.fastandroiddev.net.HttpParams;
import com.ijustyce.fastandroiddev.net.INetWork;
import com.ijustyce.school.constant.HttpConstant;

/**
 * Created by yc on 16-3-17.    用户账号相关接口
 */
public class HttpAccount {

    public static boolean login(String tag, String userId, String pw, Context context, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.LOGIN)
                .add("email", userId).add("pw", pw);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean register(String tag, String email, String pw, String name,
                                   int isTeacher, Context context, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.REGISTER)
                .add("email", email).add("pw", pw).add("name", name).add("isTeacher", isTeacher);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean upHead(String tag, String head, Context context, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.UP_HEAD).add("head", head);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean getAllKeCheng(String tag, Context context, int pageNo, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.GET_KECHENG).add("pageNo", pageNo);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean searchKeCheng(String tag, String key, Context context, int pageNo, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.SEARCH_KECHENG).add("pageNo", pageNo).add("key", key);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean newKeCheng(String day, String time, String address, String name,
                                     String tag, int index, Context context, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.NEW_KECHENG);
        params.add("name", name).add("address", address).add("day", day)
                .add("time", time).add("index", index);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean deleteKeCheng(String id, String tag, Context context, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.DELETE_KECHENG).add("id", id);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean getKeBiao(String tag, String day, int pageNo, Context context, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.GET_KEBIAO).add("day", day).add("pageNo", pageNo);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean newKeBiao(String tag, String id, Context context, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.NEW_KEBIAO).add("id", id);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean deleteKeBiao(String tag, String id, Context context, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.DELETE_KEBIAO).add("id", id);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean newBiJi(String tag, String courseId, String desc, String value,
                                  int fujian, Context context, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.NEW_BIJI);
        params.add("id", courseId).add("desc", desc).add("info", value).add("fujian", fujian);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean getBiJi(String tag, String courseId, int pageNo, Context context, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.GET_BIJI).add("id", courseId).add("pageNo", pageNo);
        return INetWork.sendGet(context, params, listener);
    }

    public static boolean deleteBiJi(String tag, String courseId, Context context, HttpListener listener){

        HttpParams params = HttpParams.create(tag, HttpConstant.DELETE_BIJI).add("id", courseId);
        return INetWork.sendGet(context, params, listener);
    }
}
