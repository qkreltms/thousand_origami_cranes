package com.example.jack.thousandorigamicranes.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by jack on 2017-08-31.
 */

public class Alarm {
    private Context mContext;
    public Alarm(Context context) {
        mContext = context;
    }

    public void start() {
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, BroadcastForNotification.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long triggerTime = calendar.getTimeInMillis();
        long repeatTime = AlarmManager.INTERVAL_DAY;
        //과거 시간일 경우 하루 더해줌으로써 노티피케이션 안나오게
        if (Calendar.getInstance().getTimeInMillis() - triggerTime > 0) {
            triggerTime = triggerTime + repeatTime;
        }
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, repeatTime, sender);
    }

}
