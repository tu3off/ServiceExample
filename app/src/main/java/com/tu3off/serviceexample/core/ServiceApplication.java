package com.tu3off.serviceexample.core;

import android.app.Application;

import com.tu3off.serviceexample.restore.RestoreManager;

public class ServiceApplication extends Application implements ApplicationBridge {

    private RestoreManager restoreManager;

    @Override
    public void onCreate() {
        super.onCreate();
        restoreManager = new RestoreManager();
    }

    @Override
    public RestoreManager getRestoreManager() {
        return restoreManager;
    }
}
