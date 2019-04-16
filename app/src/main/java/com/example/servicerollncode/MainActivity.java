package com.example.servicerollncode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.servicerollncode.MyService.ACTION_MY_SERVICE;
import static com.example.servicerollncode.MyService.COUNT;
import static com.example.servicerollncode.MyService.TIME;

public class MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "MySettings";
    public static final String APP_PREFERENCES_COUNT = "MyCount";
    public static final String APP_PREFERENCES_TIME = "MyTime";


    private TextView viewTime;
    private TextView viewCount;
    private Button buttonOn;
    private Button buttonOff;

    MyReceiver receiver;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewTime = findViewById(R.id.viewTime);
        viewCount = findViewById(R.id.viewCount);
        buttonOn = findViewById(R.id.buttonOn);
        buttonOff = findViewById(R.id.buttonOff);
        sharedPreferences = getSharedPreferences(APP_PREFERENCES,0);

        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TIME);
        intentFilter.addAction(COUNT);
        registerReceiver(receiver, intentFilter);
        if (sharedPreferences.contains(APP_PREFERENCES_TIME)) {
            viewTime.setText(sharedPreferences.getString(APP_PREFERENCES_TIME, " "));
        }
        if (sharedPreferences.contains(APP_PREFERENCES_COUNT)) {
            viewCount.setText(sharedPreferences.getString(APP_PREFERENCES_COUNT, " "));
        }

        buttonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, MyService.class));
            }
        });

        buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, MyService.class));
                String string = viewCount.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(APP_PREFERENCES_COUNT, string);
                editor.apply();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TIME)) {
                viewTime.setText(intent.getStringExtra("currentTime"));
                String string2 = viewTime.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(APP_PREFERENCES_TIME, string2);
                editor.apply();
            } else if (intent.getAction().equals(COUNT)) {
                viewCount.setText(intent.getStringExtra("count"));
            }

        }
    }
}
