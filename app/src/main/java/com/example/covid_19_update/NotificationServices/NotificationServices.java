package com.example.covid_19_update.NotificationServices;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.covid_19_update.R;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationServices extends Service {

    String title, message;

    private Timer mTimer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTimer = new Timer();
//        mTimer.schedule(timerTask, 3000, 3 * 1000);
        mTimer.schedule(timerTask, 1800, 1800 * 1000);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
//            title = intent.getStringExtra("title");
//            message = intent.getStringExtra("message");

        } catch (Exception e){
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try{
            mTimer.cancel();
            timerTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("com.example.covid_19_update");
        intent.putExtra("kini", "kana");
        sendBroadcast(intent);
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            notifyMe();
        }
    };

    public void notifyMe() {

        Log.d("notify", "notifying");

        NotiServices notiServices = new NotiServices(this);
        notiServices.createNotification();

    }

    public void compare() {

    }

}
