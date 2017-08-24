package com.example.jack.thousandorigamicranes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NoteList extends AppCompatActivity {
    private Adapter mAdapter;
    private ArrayList<ListViewItem> mList;
    private RecyclerView mListView;
    private ArrayList<Integer> mIdList;
    MyDatabaseHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        helper = new MyDatabaseHelper(getApplicationContext());
        db = helper.getReadableDatabase();

        hideActionBar();
        initAndSetListView();
        initAndAddNotesIntoList();
        initAndSetAdapter(mListView);

    }

    public ArrayList<ListViewItem> getNoteListFromDB() {
        mIdList = new ArrayList<Integer>();
        Cursor c = db.query(helper.getDatabaseName(), null, null, null, null, null, null);
        int dateIndex = c.getColumnIndex("date");
        int textIndex = c.getColumnIndex("text");
        int idIndex = c.getColumnIndex("id");

        ArrayList<ListViewItem> list = new ArrayList<>();
        c.moveToLast();
        //만약 적힌 노트가 아무것도 없다면 하나 추가해줌 현재날짜, write something 문자
        if (c.getCount() == 0) {
            list.add(addItem(0, null, getDate()));
            list.add(addItem(1, getResources().getString(R.string.write_something), null));
        }
        String now = getDate();
        String prevDate = "";
        do {
            String dateInDB = c.getString(dateIndex);
            String textInDB = c.getString(textIndex);
            int idInDB = Integer.valueOf(c.getString(idIndex));
            if (prevDate.equals(dateInDB)) {
                list.add(addItem(1, textInDB, null));
            } else {
                list.add(addItem(0, null, dateInDB));
                list.add(addItem(1, textInDB, null));
                prevDate = dateInDB;
            }
            mIdList.add(idIndex);
            Log.i("데이터베이스 내용", "날짜:" + dateInDB + "memo:" + textInDB);
        } while (c.moveToPrevious());
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

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy MM dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date); //TODO : +월 화수목금토일 넣기
    }

    public void initAndAddNotesIntoList() {
        mList = new ArrayList<>();
        mList = getNoteListFromDB();
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

    public static void showMenu (View view, final int position) //TODO : 이름 바꾸기
    {
        PopupMenu menu = new PopupMenu (view.getContext(), view);
        menu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener ()
        {
            @Override
            public boolean onMenuItemClick (MenuItem item)
            {
                int id = item.getItemId();
                switch (id)
                {//TODO delete or update를 여기서
                    case R.id.menu_delete: break;
                    case R.id.menu_update: break;
                }
                return true;
            }
        });
        menu.inflate (R.menu.popup_menu);
        menu.show();
    }

//    public void deleteMemo (ArrayList<ListViewItem> list, int position) {
//        db = helper.getWritableDatabase();
//        list.get(position);
//        //TODO : 유니크 아이디를 찾는다
//        db.delete("student", "name=?", new String[]{name});
//        Log.i("db", name + "정상적으로 삭제 되었습니다.");
//    }
//
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
}
