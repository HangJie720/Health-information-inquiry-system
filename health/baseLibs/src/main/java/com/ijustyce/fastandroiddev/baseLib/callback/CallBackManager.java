package com.ijustyce.fastandroiddev.baseLib.callback;

import android.app.Activity;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yc on 16-2-7. 回调管理
 */
public class CallBackManager {

    private static List<ActivityLifeCall> activityLifeCall;

    public static void onCreate(Activity activity){
        if (activityLifeCall == null || activityLifeCall.isEmpty()) return;
        for (ActivityLifeCall tmp : activityLifeCall){
            if (tmp != null) tmp.onCreate(activity);
        }
    }

    public static void dispatchTouchEvent(MotionEvent event, Activity activity) {
        if (activityLifeCall == null || activityLifeCall.isEmpty()) return;
        for (ActivityLifeCall tmp : activityLifeCall){
            if (tmp != null) tmp.dispatchTouchEvent(event, activity);
        }
    }

    public static void onResume(Activity activity){
        if (activityLifeCall == null || activityLifeCall.isEmpty()) return;
        for (ActivityLifeCall tmp : activityLifeCall){
            if (tmp != null) tmp.onResume(activity);
        }
    }

    public static void onStop(Activity activity){
        if (activityLifeCall == null || activityLifeCall.isEmpty()) return;
        for (ActivityLifeCall tmp : activityLifeCall){
            if (tmp != null) tmp.onStop(activity);
        }
    }

    public static void onDestroy(Activity activity){
        if (activityLifeCall == null || activityLifeCall.isEmpty()) return;
        for (ActivityLifeCall tmp : activityLifeCall){
            if (tmp != null) tmp.onDestroy(activity);
        }
    }

    public static void onPause(Activity activity){
        if (activityLifeCall == null || activityLifeCall.isEmpty()) return;
        for (ActivityLifeCall tmp : activityLifeCall){
            if (tmp != null) tmp.onPause(activity);
        }
    }

    static {
        activityLifeCall = new ArrayList<>();
    }

    public static void addActivityLifeCall(ActivityLifeCall lifeCall){

        if (activityLifeCall!= null){
            activityLifeCall.add(lifeCall);
        }
    }

    /**
     * this function will remove in next version, please use addActivityLifeCall
     */
    @Deprecated
    public static void setActivityLifeCall(ActivityLifeCall lifeCall){

        addActivityLifeCall(lifeCall);
    }
}