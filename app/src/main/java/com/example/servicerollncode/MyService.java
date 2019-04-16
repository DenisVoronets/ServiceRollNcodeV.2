package com.example.servicerollncode;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {

    private Thread thread;

    public static final String TIME = "TIME";
    public static final String COUNT = "COUNT";
    public static final String ACTION_MY_SERVICE = "com.example.servicerollncode";

    public MyService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss ");
        String formateDate = dateFormat.format(new Date());
        Intent intent = new Intent();
        intent.setAction(TIME);
        intent.putExtra("currentTime", (Serializable) formateDate);
        sendBroadcast(intent);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myCount();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        thread.interrupt();
        super.onDestroy();
    }



    public void myCount() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int n = -1;
                while (true) {
                    ++n;
                    StringBuilder msgStr = new StringBuilder("Текущий показатель: ");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("s", Locale.getDefault());
                    try {
                        msgStr.append(simpleDateFormat.format(simpleDateFormat.parse(String.valueOf(n))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setAction(COUNT);
                    intent.putExtra("count", (Serializable) msgStr);
                    sendBroadcast(intent);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });
        thread.start();
    }


}
