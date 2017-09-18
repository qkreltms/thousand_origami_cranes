package com.example.jack.thousandorigamicranes.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.example.jack.thousandorigamicranes.MainActivity;
import com.example.jack.thousandorigamicranes.R;
import com.example.jack.thousandorigamicranes.data.CounterDBHelper;
import com.example.jack.thousandorigamicranes.data.NoteDBHelper;

import java.util.Random;

/**
 * Created by jack on 2017-08-31.
 */

public class BroadcastForNotification extends BroadcastReceiver {

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
        String[] noteListData = selectDB(context);

        if (noteListData != null) {
            String text = noteListData[0];
            String date = noteListData[1];
            builder.setSmallIcon(R.drawable.bottle2)
                    .setContentTitle(date)
                    .setContentText(text)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.bottle3); //TODO 사진 바꾸기
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notificationManager.notify(1, builder.build());
            }
        }
    }

    public String[] selectDB(Context context) {
        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
        SQLiteDatabase db = noteDBHelper.getReadableDatabase();
        CounterDBHelper counterDBHelper = new CounterDBHelper(context);
        Cursor c;

        int counterDBCounter = counterDBHelper.getCounter();
        if (counterDBCounter == 0) {
            Log.i("countDB", "출력할 데이터 없음");
            return null;
        }
        int random = new Random().nextInt(counterDBCounter) + 1;
        String selectNumberRandomly = Integer.toString((random));
        String sql = "SELECT * FROM " + noteDBHelper.getDatabaseName() + " WHERE " + noteDBHelper.getIdFieldName() + "=" + selectNumberRandomly;
        c = db.rawQuery(sql, null);
        String text = "";
        String date = "";
        if (c != null && c.getCount() != 0) {
            c.moveToFirst();
            int dateIndex = c.getColumnIndex("date");
            int textIndex = c.getColumnIndex("text");
            text = c.getString(textIndex);
            date = c.getString(dateIndex);
            c.close();
        }
        return new String[]{text, date};
    }
}
