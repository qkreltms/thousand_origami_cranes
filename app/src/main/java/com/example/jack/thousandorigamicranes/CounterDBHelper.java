package com.example.jack.thousandorigamicranes;

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
    private static final String DATE = "date";
    private static final String TEXT = "text";
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + "(" + ID + " INTEGER PRIMARY KEY, " + DATE + " DATETIME, " + TEXT + " TEXT);";

    public CounterDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        Log.w(com.example.jack.thousandorigamicranes.MyDatabaseHelper.class.getName(), "table " + DATABASE_NAME + " has created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(com.example.jack.thousandorigamicranes.MyDatabaseHelper.class.getName(),
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

    public String getTextFieldName() {
        return TEXT;
    }
}
