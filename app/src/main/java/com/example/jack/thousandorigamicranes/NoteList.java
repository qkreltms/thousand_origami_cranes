package com.example.jack.thousandorigamicranes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteList extends AppCompatActivity {
    private Adapter mAdapter;
    private ArrayList<ListViewItem> mList;
    private RecyclerView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        hideActionBar();
        initAndSetListView();
        initAndAddNotesIntoList();
        initAndSetAdapter(mListView);
    }

    public ArrayList<ListViewItem> getNotesFromDB() {//TODO : 이름에 List추가하기
        MyDatabaseHelper helper = new MyDatabaseHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(helper.getDatabaseName(), null, null, null, null, null, null);
        int dateIndex = c.getColumnIndex("date");
        int textIndex = c.getColumnIndex("text");

        ArrayList<ListViewItem> list = new ArrayList<>();
        c.moveToFirst();
        //만약 적힌 노트가 아무것도 없다면 하나 추가해줌 현재날짜, write something 문자
        if (c.getCount() == 0) {
            list.add(addItem(0, null, getDate()));
            list.add(addItem(1, getResources().getString(R.string.write_something), null));
        } //TODO : 한번적으면 자동으로 사라지게
        String date = c.getString(dateIndex);
        String prevDate = date;//젤 처음 행의 날짜받음
        list.add(addItem(0, null, getDate())); //현재날짜 추가해줌
        do {
            date = c.getString(dateIndex);
            String text = c.getString(textIndex);
            if (prevDate.equals(date)) {
                list.add(addItem(1, text, null));
            } else {
                list.add(addItem(0, null, date));
                list.add(addItem(1, text, null));
                prevDate = date;
            } //TODO : check 날짜가 달라지는지 확인

            Log.i("데이터베이스 내용", "날짜:" + date + "memo:" + text);
        } while (c.moveToNext());
        c.close();

        return list;
    }

    public ListViewItem addItem(int type, @Nullable String memo, @Nullable String date) { //type2
        ListViewItem item = new ListViewItem();
        item.setMemo(memo);
        item.setDate(date);
        item.setType(type); //TODO : ENUM 으로 관리

        return item;
    }

//    public ListViewItem addItem(int type, String memo) { //type1
//        ListViewItem item = new ListViewItem();
//        item.setMemo(memo);
//        item.setType(type); //TODO : ENUM 으로 관리
//
//        return item;
//    }
//
//    public ListViewItem addItem(int type) {
//        ListViewItem item = new ListViewItem();
//        item.setDate(getDate());
//        item.setType(type); //TODO : ENUM 으로 관리
//
//        return item;
//    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy MM dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date); //TODO : +월 화수목금토일 넣기
    }

    public void initAndAddNotesIntoList() {
        mList = new ArrayList<>();
        mList = getNotesFromDB();
    }

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        mListView.setLayoutManager(layoutManager);
    }

    public void initAndSetListView() {
        mListView = (RecyclerView) findViewById(R.id.list_show_memo);
        setLayoutManager(new LinearLayoutManager(this)); //레이아웃 메니저를 local로 하는게 좋을까? 파라메터로 받는게 좋을까
    }

    public void initAndSetAdapter(RecyclerView recyclerView) {
        mAdapter = new Adapter(mList);
        recyclerView.setAdapter(mAdapter);
    }

    public void hideActionBar() {
        getSupportActionBar().hide();
    }
}
