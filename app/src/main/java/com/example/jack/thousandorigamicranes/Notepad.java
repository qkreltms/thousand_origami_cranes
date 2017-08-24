package com.example.jack.thousandorigamicranes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//TODO : 키보드 위로 에디트 텍스터 이동
public class Notepad extends AppCompatActivity implements TextWatcher{
    private EditText mMemo;
    private MyDatabaseHelper helper;
    private SQLiteDatabase db;
    private String mMemoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        mMemo = (EditText) findViewById(R.id.edt_memo);
        mMemo.addTextChangedListener(this);
        hideActionBar();

    }

    public void hideActionBar() {
        getSupportActionBar().hide();

    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy MM dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date); //TODO : 월 화수목금토일 넣기
    }

    public boolean checkDataIsNull() {
        if (mMemoData != null && mMemoData.length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (! checkDataIsNull()) {
            insertDataIntoDB(mMemoData);
        }
    }

    public void insertDataIntoDB(String text) {
        helper = new MyDatabaseHelper(getApplicationContext());
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("date", getDate());
        values.put("text", text);
        db.insert(helper.getDatabaseName(), null, values);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mMemoData = String.valueOf(charSequence);
        //TODO save data
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
