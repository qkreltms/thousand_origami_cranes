package com.example.jack.thousandorigamicranes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Memo";
    private static final int DATABASE_VERSION = 2;
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String TEXT = "text";
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "+DATABASE_NAME +  "("+ID+" INTEGER PRIMARY KEY, "+DATE+" DATETIME, "+TEXT+" TEXT);";

    public NoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        Log.w(NoteDBHelper.class.getName(), "table " + DATABASE_NAME + " has created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(NoteDBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(database);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public String getIdFieldName() {
        return ID;
    }

    public String getDateFieldName() {
        return DATE;
    }

    public String getTextFieldName() {
        return TEXT;
    }
}