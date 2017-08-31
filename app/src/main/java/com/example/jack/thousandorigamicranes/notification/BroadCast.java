package com.example.jack.thousandorigamicranes.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.jack.thousandorigamicranes.MainActivity;
import com.example.jack.thousandorigamicranes.R;

/**
 * Created by jack on 2017-08-31.
 */

public class BroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification.Builder builder;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, "API 8.0 only");
        } else {
            builder = new Notification.Builder(context);
        }
        builder.setSmallIcon(R.drawable.bottle2)
                .setTicker("ticker")
                .setContentTitle("title")
                .setContentText("content")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setNumber(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationManager.notify(1, builder.build());
        }
    }
}
