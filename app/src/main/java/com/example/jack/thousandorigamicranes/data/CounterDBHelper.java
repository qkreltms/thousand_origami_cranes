package com.example.jack.thousandorigamicranes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jack on 2017-08-29.
 */

public class CounterDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CounterDB";
    private static final int DATABASE_VERSION = 1;
    private static final String ID = "id";
    private static final String COUNTER = "counter";
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + "(" + ID + " INTEGER PRIMARY KEY, " + COUNTER + " INTEGER);";
    private static int counter = 0;

    public CounterDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        Log.w(CounterDBHelper.class.getName(), "table " + DATABASE_NAME + " has created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(CounterDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(database);
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public String getIdFieldName() {
        return ID;
    }

    public String getCounterFieldName() {
        return COUNTER;
    }

    public void addCounter() {
        counter++;
        Log.i("카운터 횟수 추가", Integer.toString(counter));
    }

    public void subCounter() {
        counter--;
        Log.i("카운터 횟수 삭제", Integer.toString(counter));
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int num) {
        counter = num;
    }
}
