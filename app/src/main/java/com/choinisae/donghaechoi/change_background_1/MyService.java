package com.choinisae.donghaechoi.change_background_1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.choinisae.donghaechoi.change_background_1.receiver.MyReceiver;

/**
 * Created by donghaechoi on 2016. 3. 16..
 */
public class MyService extends Service {


    private String TAG = "TAG";

    @Override
    public void onCreate() {
        super.onCreate();
        unregisterRestartAlarm();
        MyReceiver myReceiver = new MyReceiver();
        IntentFilter off = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(myReceiver, off);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        registerRestartAlarm();
    }

    void registerRestartAlarm() {
        Log.d(TAG, "registerRestartAlarm");
        Intent intent = new Intent(MyService.this, MyService.class);
        intent.setAction(RestartService.ACTION_RESTART_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(
                MyService.this, 0, intent, 0);
        long firstTime = SystemClock.elapsedRealtime();  // 현재 시간
        firstTime += 10000; // 10초 후에 알람이벤트 발생
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE); // 알람 서비스 등록
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                10000, sender); // 알람 반복 주기
    }

    void unregisterRestartAlarm() {
        Log.d(TAG, "unregisterRestartAlarm");
        Intent intent = new Intent(MyService.this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(
                MyService.this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }


}

