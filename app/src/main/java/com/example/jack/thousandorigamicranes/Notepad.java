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

public class Notepad extends AppCompatActivity implements TextWatcher {
    private EditText mMemo;
    private String mMemoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        mMemo = (EditText) findViewById(R.id.edt_memo);
        mMemo.addTextChangedListener(this);

        setTextIfUpdate();
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

    @Override
    protected void onStart() {
        super.onStart();

        hideActionBar();
    }

    public void hideActionBar() {
        getSupportActionBar().hide(); //TODO : theme 바꾸면 된다는데 자세히 알아보기
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy MM dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date); //TODO : 월 화수목금토일 넣기
    }

    public boolean checkDataIsNull() {
        return (mMemoData != null && mMemoData.length() > 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!checkDataIsNull()) {
            if (!isUpdate()) {
                insertDataIntoDB(mMemoData);
            } else {
                int position = Integer.parseInt(getIntent().getStringArrayExtra("update")[1]);
                NoteList.updateDB(position, mMemoData);
            }
        }
    }

    public boolean isUpdate() {
        return getIntent().hasExtra("update");
    }

    public void insertDataIntoDB(String text) {
        MyDatabaseHelper helper = new MyDatabaseHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

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
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
