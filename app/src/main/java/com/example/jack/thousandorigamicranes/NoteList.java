package com.example.jack.thousandorigamicranes;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.jack.thousandorigamicranes.data.CounterDBHelper;
import com.example.jack.thousandorigamicranes.data.ListViewItem;
import com.example.jack.thousandorigamicranes.data.MemoAsyncTaskLoader;
import com.example.jack.thousandorigamicranes.data.NoteDBHelper;

import java.util.ArrayList;
import java.util.Collection;

public class NoteList extends AppCompatActivity {
    private static Adapter mAdapter;
    private RecyclerView mListView;
    static NoteDBHelper noteDBHelper;
    static SQLiteDatabase db;
    static CounterDBHelper counterDBHelper;
    static Context context;
    private static ArrayList<ListViewItem> mList;

    //TODO : 에이싱크 테스크에서 리스트 불러오기 , 불러오기 이전에 종료할시 예외 처리
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        context = getApplicationContext();
        counterDBHelper = new CounterDBHelper(context);
        noteDBHelper = new NoteDBHelper(context);
        db = noteDBHelper.getReadableDatabase();
        mList = new ArrayList<>();
        mAdapter = new Adapter(mList);
        mListView = (RecyclerView) findViewById(R.id.list_show_memo);
    }

    @Override
    protected void onStart() {
        super.onStart();

//      db.execSQL("DELETE FROM Memo");
        updateAdapter();
        hideActionBar();
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void hideActionBar() {
        getSupportActionBar().hide();
    }

    public static void showPopUpMenu(final View view, final int position)
    {
        PopupMenu menu = new PopupMenu (view.getContext(), view);
        menu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener ()
        {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onMenuItemClick (MenuItem item)
            {
                int id = item.getItemId();
                switch (id)
                {
                    case R.id.menu_delete: deleteNoteAndSubCounter(position);
                        updateAdapter();
                        break;
                    case R.id.menu_update:
                        Intent intent = new Intent(view.getContext(), Notepad.class);
                        intent.putExtra("update", new String[] {mList.get(position).getMemo(), Integer.toString(position)});
                        view.getContext().startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        menu.inflate (R.menu.popup_menu);
        menu.show();
    }

    public static void deleteNoteAndSubCounter(int position) {
        db = noteDBHelper.getWritableDatabase();
        String id = Integer.toString(mList.get(position).getId());
        db.delete(noteDBHelper.getDatabaseName(), noteDBHelper.getIdFieldName() + "=" + id, null);
        Log.i(noteDBHelper.getDatabaseName(), mList.get(position).getId() + " : " + mList.get(position).getMemo() + "정상적으로 삭제 되었습니다.");

        subCountNote();
    }

    public static void subCountNote() {
        selectCounterDB();
        counterDBHelper.subCounter();
        insertIntoCounterDB();
    }

    public static void updateAdapter() {
        mList.clear();
        mList.addAll(initList());
        mAdapter.notifyDataSetChanged();
    }

    public static ArrayList<ListViewItem> initList() {
        ArrayList<ListViewItem> temp = selectDB();
        ArrayList<ListViewItem> list = new ArrayList<>();
        int tempSize = temp.size()-1;

        if (tempSize >= 0) {
            String preDate = temp.get(tempSize).getDate();

            list.add(addItem(temp.get(tempSize).getId(), null, temp.get(tempSize).getDate(), 0));
            for (int i = tempSize; i >= 0; i--) {
                if (preDate.equals(temp.get(i).getDate())) {
                    list.add(addItem(temp.get(i).getId(), temp.get(i).getMemo(), null, 1));
                } else {
                    list.add(addItem(temp.get(i).getId(), null, temp.get(i).getDate(), 0));
                    list.add(addItem(temp.get(i).getId(), temp.get(i).getMemo(), null, 1));
                    preDate = temp.get(i).getDate();
                }
            }
        }
        return list;
    }

    public static ListViewItem addItem(int id, @Nullable String memo, @Nullable String date, int type) {
        return  new ListViewItem(id, memo, date, type);
    }

    public static ArrayList<ListViewItem> selectDB() {
        Cursor c = db.query(noteDBHelper.getDatabaseName(), null, null, null, null, null, null);
        int dateIndex = c.getColumnIndex("date");
        int textIndex = c.getColumnIndex("text");
        int idIndex = c.getColumnIndex("id");
        ArrayList<ListViewItem> list = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                list.add(new ListViewItem(Integer.valueOf(c.getString(idIndex)), c.getString(textIndex), c.getString(dateIndex)));
            } while (c.moveToNext());
        }
        c.close();

        return list;
    }

    public static void selectCounterDB() {
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

    public static void insertIntoCounterDB() {
        CounterDBHelper helper = new CounterDBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(helper.getCounterFieldName(), counterDBHelper.getCounter());
        db.insert(helper.getDatabaseName(), null, values);
    }

    public static void updateDB (int position, String memo) {
        db = noteDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(noteDBHelper.getTextFieldName(), memo);
        db.update(noteDBHelper.getDatabaseName(), values, noteDBHelper.getIdFieldName() + "=" + mList.get(position).getId(), null);
        Log.i(noteDBHelper.getDatabaseName(), mList.get(position).getId() + "정상적으로 업데이트 되었습니다.");
    }
}
