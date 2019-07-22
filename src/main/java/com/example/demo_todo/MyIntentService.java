package com.example.demo_todo;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Date;

public class MyIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;
    DatabaseHelper databaseHelper;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    MainActivity mainActivity;
    public MyIntentService(){
        super("MyIntentService");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        databaseHelper=new DatabaseHelper(getBaseContext());
        mainActivity=new MainActivity();
        sharedPrefrenceConfig=new SharedPrefrenceConfig(getBaseContext());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Date current_time= Calendar.getInstance().getTime();
        int curr_hours=current_time.getHours();
        int curr_mins=current_time.getMinutes();
        if (mainActivity.hour_u<=curr_hours && databaseHelper.getActiveTaskCount()>0 && mainActivity.min_u<=curr_mins ) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setContentTitle("Goal Today");
            builder.setContentText("Hey "+sharedPrefrenceConfig.readName()+","+databaseHelper.getActiveTaskCount()+" tasks are pending");
            builder.setSmallIcon(R.mipmap.app_icon);
            Intent notifyIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            Notification notificationCompat = builder.build();
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(NOTIFICATION_ID, notificationCompat);
        }
    }

}
