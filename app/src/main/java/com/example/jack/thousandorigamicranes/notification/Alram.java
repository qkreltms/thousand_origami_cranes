package com.example.jack.thousandorigamicranes.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

/**
 * Created by jack on 2017-08-31.
 */

public class Alram {
    private Context mContext;

    public Alram(Context context) {
        mContext = context;
    }

    public void start() {
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, BroadCast.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        long triggerTime = SystemClock.elapsedRealtime() + 1000;
        long repeatTime = 1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, repeatTime, sender);
    }

}
