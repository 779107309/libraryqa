package com.example.arcfacedemo;

import android.app.Application;

import com.example.arcfacedemo.face.FaceDB;

/**
 * Created by Administrator on 2019/1/18.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FaceDB mFaceDB = new FaceDB();

    }
}
