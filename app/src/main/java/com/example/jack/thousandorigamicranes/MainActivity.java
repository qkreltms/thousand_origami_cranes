package com.example.jack.thousandorigamicranes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.jack.thousandorigamicranes.data.CounterDBHelper;
import com.example.jack.thousandorigamicranes.notification.Alarm;

public class MainActivity extends AppCompatActivity {
    ImageButton bottle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottle = (ImageButton) findViewById(R.id.bottle);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setBottleImage(this);
        new Alarm(getApplicationContext()).start();
        hideActionBar();
    }

    public void setBottleImage(Context context) { //TODO : 여기 리펙토링 할것
        int count = countNote(context);
        if (count < 10) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bottle.setImageDrawable(getResources().getDrawable(R.drawable.glass_bottle, null));
            } else {
                bottle.setImageDrawable(getResources().getDrawable(R.drawable.glass_bottle));
            }
        } else if (count >= 10 && count < 15) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bottle.setImageDrawable(getResources().getDrawable(R.drawable.bottle2, null));
            } else {
                bottle.setImageDrawable(getResources().getDrawable(R.drawable.bottle2));
            }
        } else if (count >= 15) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bottle.setImageDrawable(getResources().getDrawable(R.drawable.bottle3, null));
            } else {
                bottle.setImageDrawable(getResources().getDrawable(R.drawable.bottle3));
            }
        }
    }

    public void hideActionBar() {
        getSupportActionBar().hide();
    }

    public void onShowNoteList(View view) {
        Intent intent = new Intent(this, NoteList.class);
        startActivity(intent);
    }

    public void onAddNote(View view) {
        Intent intent = new Intent(this, Notepad.class);
        startActivity(intent);
    }

    public int countNote(Context context) {
        CounterDBHelper counterDBHelper = new CounterDBHelper(context);
        selectDB(counterDBHelper);
        return counterDBHelper.getCounter();
    }

    public void selectDB(CounterDBHelper counterDBHelper) {
        SQLiteDatabase db = counterDBHelper.getReadableDatabase();
        String sql = "SELECT " + counterDBHelper.getCounterFieldName() + " FROM " + counterDBHelper.getDatabaseName();
        Cursor c = db.rawQuery(sql, null);
        int counterIndex = c.getColumnIndex(counterDBHelper.getCounterFieldName());
        int counter;
        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                counter = Integer.valueOf(c.getString(counterIndex));
                counterDBHelper.setCounter(counter);
            }
        }
        c.close();
    }
}
