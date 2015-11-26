package com.tu3off.serviceexample.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import com.tu3off.serviceexample.R;
import com.tu3off.serviceexample.core.ApplicationBridge;
import com.tu3off.serviceexample.service.SimpleService;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String UI_ACTION_BROADCAST_RECEIVER = MainActivity.class.getName()
            + "actionUI";

    private ApplicationBridge applicationBridge;
    private UIBroadcastReceiver uiBroadcastReceiver;
    private TextView tvServiceCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uiBroadcastReceiver = new UIBroadcastReceiver();
        applicationBridge = (ApplicationBridge) getApplicationContext();
        tvServiceCount = (TextView) findViewById(R.id.tvServiceCount);
        findViewById(R.id.btStartService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleService.start(MainActivity.this, SimpleService.TASK_COUNTER,
                        TAG);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                uiBroadcastReceiver, uiBroadcastReceiver.getIntentFilter());
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                uiBroadcastReceiver);
    }

    private class UIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            tvServiceCount.setText(applicationBridge.getRestoreManager().getValue());
        }

        public IntentFilter getIntentFilter() {
            return new IntentFilter(UI_ACTION_BROADCAST_RECEIVER);
        }
    }
}
