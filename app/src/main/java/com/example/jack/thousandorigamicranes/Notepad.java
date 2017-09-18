package com.example.jack.thousandorigamicranes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.jack.thousandorigamicranes.data.CounterDBHelper;
import com.example.jack.thousandorigamicranes.data.NoteDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notepad extends AppCompatActivity implements TextWatcher {
    private EditText mMemo;
    private String mMemoData;
    private CounterDBHelper counterDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        mMemo = (EditText) findViewById(R.id.edt_memo);
        mMemo.addTextChangedListener(this);
        counterDBHelper = new CounterDBHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        SQLiteDatabase db = counterDBHelper.getWritableDatabase();
//        db.execSQL("DELETE FROM CounterDB");
        setTextIfUpdate();
        hideActionBar();
    }

    public void setTextIfUpdate() {
        if (getIntent().hasExtra("update")) {
            setTextAtNotepad();
        }
    }

    public void setTextAtNotepad() {
        String memo = getIntent().getStringArrayExtra("update")[0];
        if (memo.length() > 0) {
            mMemo.setText(memo);
        }
    }

    public void hideActionBar() {
        getSupportActionBar().hide();
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
        SimpleDateFormat dayOfTheWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date) + " " + dayOfTheWeekFormat.format(date);
    }

    public boolean checkDataIsNull() {
        return (mMemoData == null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (! checkDataIsNull()) {
            if (! isUpdate()) {
                insertAndCountNote(mMemoData);
            } else {
                int position = Integer.parseInt(getIntent().getStringArrayExtra("update")[1]);
                NoteList.updateDB(position, mMemoData);
            }
        }
    }

    public boolean isUpdate() {
        return getIntent().hasExtra("update");
    }

    public void countNote() {
        selectCounterDB();
        counterDBHelper.addCounter();
        insertIntoCounterDB();
    }

    public void insertAndCountNote(String text) {
        NoteDBHelper helper = new NoteDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(helper.getDateFieldName(), getDate()); //TODO : Object로 넣는게 좋은지 확인
        values.put(helper.getTextFieldName(), text);
        db.insert(helper.getDatabaseName(), null, values);

        countNote();
    }

    public void selectCounterDB() {
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

    public void insertIntoCounterDB() {
        CounterDBHelper helper = new CounterDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(helper.getCounterFieldName(), counterDBHelper.getCounter());
        db.insert(helper.getDatabaseName(), null, values);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mMemoData = String.valueOf(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
