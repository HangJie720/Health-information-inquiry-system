package com.ijustyce.fastandroiddev;

import android.app.Application;

/**
 * Created by yangchun on 16/4/28.  全局的Application
 */
public class IApplication extends Application {

    private static Application app;
    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
    }

    public static void init(Application application){

        app = application;
    }

    public static Application getInstance(){

        return app;
    }
}
