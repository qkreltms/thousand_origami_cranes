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

    //TODO : update 및 delete 기능 추가하기
//    public void delete (String name) {
//        db = helper.getWritableDatabase();
//        db.delete("student", "name=?", new String[]{name});
//        Log.i("db", name + "정상적으로 삭제 되었습니다.");
//    }

//    public void update (String name, int age) {
//        db = helper.getWritableDatabase(); //db 객체를 얻어온다. 쓰기가능
//
//        ContentValues values = new ContentValues();
//        values.put("age", age);    //age 값을 수정
//        db.update("student", values, "name=?", new String[]{name});
//        /*
//         * new String[] {name} 이런 간략화 형태가 자바에서 가능하다
//         * 당연하지만, 별도로 String[] asdf = {name} 후 사용하는 것도 동일한 결과가 나온다.
//         */
//
//        /*
//         * public int update (String table,
//         * ContentValues values, String whereClause, String[] whereArgs)
//         */
//    }

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
