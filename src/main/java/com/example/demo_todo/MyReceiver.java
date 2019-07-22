package com.example.demo_todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myintent=new Intent(context,MyIntentService.class);
        context.startService(myintent);
    }
}
