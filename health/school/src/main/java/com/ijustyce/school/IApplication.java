package com.ijustyce.school;

import android.app.Application;
import android.content.SharedPreferences;

import com.ijustyce.fastandroiddev.baseLib.callback.CallBackManager;
import com.ijustyce.fastandroiddev.baseLib.utils.IJson;
import com.ijustyce.fastandroiddev.baseLib.utils.RegularUtils;
import com.ijustyce.fastandroiddev.net.HttpParams;
import com.ijustyce.fastandroiddev.umenglib.ActivityLifeTongJi;
import com.ijustyce.school.model.UserInfo;

/**
 * Created by yc on 16-3-18.    Application 类
 */
public class IApplication extends Application {

    private static UserInfo.DataEntity userInfo;
    @Override
    public void onCreate() {
        super.onCreate();

        CallBackManager.setActivityLifeCall(new ActivityLifeTongJi());

        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        String result = shared.getString("userInfo", null);
        if (result != null){
            UserInfo tmp = IJson.fromJson(result, UserInfo.class);

            if (tmp != null) {
                setUserInfo(tmp);
            }
        }
    }

    public static boolean isTeacher(){

        return isLogin() && userInfo.getTeacher().equals("1");
    }

    public static boolean isLogin(){

        return userInfo != null && userInfo.getPw() != null && userInfo.getEmail() != null;
    }

    public static UserInfo.DataEntity getUserInfo() {
        return userInfo;
    }

    public static String getHead(){

        if (userInfo == null || userInfo.getHead() == null){
            return null;
        }
        String head = userInfo.getHead();
        if (RegularUtils.isImage(head)){
            return head;
        }
        return null;
    }

    public static void setUserInfo(UserInfo userInfo) {

        HttpParams.addCommon("email", userInfo == null ? null : userInfo.getUserId());
        HttpParams.addCommon("pw", userInfo == null ? null : userInfo.getPw());
        HttpParams.addCommon("pageSize", 10);
        IApplication.userInfo = userInfo == null ? null : userInfo.getUserData();
    }
}
