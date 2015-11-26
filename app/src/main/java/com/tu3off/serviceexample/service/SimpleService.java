package com.tu3off.serviceexample.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.tu3off.serviceexample.core.ApplicationBridge;
import com.tu3off.serviceexample.ui.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleService extends Service {

    public static final int TASK_COUNTER = 1;

    private static final String EXTRA_BUNDLE = SimpleService.class.getName() + ".extraBundle";
    private static final String EXTRA_TAG = SimpleService.class.getName() + ".extraTag";
    private static final String EXTRA_TASK_NUMBER = SimpleService.class.getName() + ".numberOfTask";

    private ExecutorService executorService;
    private ApplicationBridge applicationBridge;

    public static void start(Context context, int numberOfTask, String tag) {
        Intent intent = new Intent(context, SimpleService.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAG, tag);
        bundle.putInt(EXTRA_TASK_NUMBER, numberOfTask);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newSingleThreadExecutor();
        applicationBridge = (ApplicationBridge) getApplicationContext();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
        switch (bundle.getInt(EXTRA_TASK_NUMBER)) {
            case TASK_COUNTER:
                String tag = bundle.getString(EXTRA_TAG);
                executorService.execute(new CounterCommand(tag));
                return START_NOT_STICKY;
            default:
                throw new IllegalArgumentException();
        }
    }

    private class CounterCommand implements Runnable {

        private final String tag;

        public CounterCommand(String tag) {
            this.tag = tag;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                applicationBridge.getRestoreManager().setValue(tag + " command i-count:" + i);
                LocalBroadcastManager.getInstance(SimpleService.this).sendBroadcast(
                        new Intent(MainActivity.UI_ACTION_BROADCAST_RECEIVER));
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
