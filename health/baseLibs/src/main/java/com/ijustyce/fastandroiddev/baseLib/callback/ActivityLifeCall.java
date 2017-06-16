package com.ijustyce.fastandroiddev.baseLib.callback;

import android.app.Activity;
import android.view.MotionEvent;

/**
 * Created by yc on 16-2-7. Activity    生命周期调用
 */
public interface ActivityLifeCall {

    void onResume(Activity activity);
    void onPause(Activity activity);
    void onCreate(Activity activity);
    void onStop(Activity activity);
    void onDestroy(Activity activity);
    void dispatchTouchEvent(MotionEvent event, Activity activity);
}
