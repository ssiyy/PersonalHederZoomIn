package com.example.siy.myapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by Siy on 2019/02/19.
 *
 * @author Siy
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getInstance() {
        return context;
    }
}
