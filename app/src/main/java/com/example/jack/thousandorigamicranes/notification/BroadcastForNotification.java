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
        String text = noteListData[0]; //TODO : 리펙토링
        String date = noteListData[1];
        if (text.equals("") && date.equals("")) {}
        else {
            builder.setSmallIcon(R.drawable.bottle2) //TODO : 노트내용 출력 단, 없을경우 예외처리
                    .setContentTitle(date)
                    .setContentText(text)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.bottle3);
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
        String selectNumberRandomly = Integer.toString((new Random().nextInt(counterDBHelper.getCounter()) + 1));
        //TODO : db 카운터 값이 0일때 예외 처리 try catch
        Log.i("랜덤으로 고른것 테스트", selectNumberRandomly);
        Log.i("db 카운터 테스트", Integer.toString(counterDBHelper.getCounter()));
        String sql = "SELECT * FROM " + noteDBHelper.getDatabaseName() + " WHERE " + noteDBHelper.getIdFieldName() + "=" + selectNumberRandomly;
        //TODO : 1개 밖에없을때 테스트, 아무것도 없을 때
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
