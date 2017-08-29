package com.example.jack.thousandorigamicranes;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class NoteList extends AppCompatActivity {
    private static Adapter mAdapter;
    private static ArrayList<ListViewItem> mList;
    private RecyclerView mListView;
    static MyDatabaseHelper helper;
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        helper = new MyDatabaseHelper(getApplicationContext());
        db = helper.getReadableDatabase();
        mList = new ArrayList<>();
        mList = initList();
        mAdapter = new Adapter(mList);
        mListView = (RecyclerView) findViewById(R.id.list_show_memo);

    }

    @Override
    protected void onStart() {
        super.onStart();

        updateAdapter();
        hideActionBar();
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static ArrayList<ListViewItem> initList() {
        ArrayList<ListViewItem> temp = selectAllFromDB();
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
                }
            }
        }
        return list;
    }

    public static ListViewItem addItem(int id, @Nullable String memo, @Nullable String date, int type) {
        return  new ListViewItem(id, memo, date, type);
    }

    public static ArrayList<ListViewItem> selectAllFromDB() {
        Cursor c = db.query(helper.getDatabaseName(), null, null, null, null, null, null);
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
                    case R.id.menu_delete: deleteDB(position);
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

    public static void updateAdapter() {
        mList.clear();
        mList.addAll(initList());
        mAdapter.notifyDataSetChanged();
    }
    public static void deleteDB(int position) {
        db = helper.getWritableDatabase();
        String id = Integer.toString(mList.get(position).getId());
        db.delete(helper.getDatabaseName(), helper.getIdFieldName() + "=" + id, null);
        Log.i(helper.getDatabaseName(), mList.get(position).getId() + " : " + mList.get(position).getMemo() + "정상적으로 삭제 되었습니다.");
    }

    public static void updateDB (int position, String memo) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(helper.getTextFieldName(), memo);    //age 값을 수정
        db.update(helper.getDatabaseName(), values, helper.getIdFieldName() + "=" + mList.get(position).getId(), null);
        Log.i(helper.getDatabaseName(), mList.get(position).getId() + "정상적으로 업데이트 되었습니다.");
    }
}
