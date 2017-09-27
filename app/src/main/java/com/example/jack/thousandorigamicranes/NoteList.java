package com.example.jack.thousandorigamicranes;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
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

import com.example.jack.thousandorigamicranes.data.CounterDBHelper;
import com.example.jack.thousandorigamicranes.data.ListViewItem;
import com.example.jack.thousandorigamicranes.data.NoteDBHelper;

import java.util.ArrayList;

public class NoteList extends AppCompatActivity {
    private static Adapter mAdapter;
    private RecyclerView mListView;
    private static NoteDBHelper sNoteDBHelper;
    private static SQLiteDatabase sSqLiteDatabase;
    private static CounterDBHelper sCounterDBHelper;
    private static ArrayList<ListViewItem> mArrayList;
    private AsyncTask mAsyncTask;

    //TODO : 사진추가기능
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        setNotibarColor();
        Context mContext = getApplicationContext();
        mAsyncTask = new AsyncTask(mContext);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAsyncTask.execute();
    }

    private class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        private Context context;

        private AsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sCounterDBHelper = new CounterDBHelper(context);
            sNoteDBHelper = new NoteDBHelper(context);
            sSqLiteDatabase = sNoteDBHelper.getReadableDatabase();
            mArrayList = new ArrayList<>();
            mAdapter = new Adapter(context, mArrayList);
            mListView = (RecyclerView) findViewById(R.id.list_show_memo);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            updateAdapter();
            mListView.setAdapter(mAdapter);
            mListView.setLayoutManager(new LinearLayoutManager(context));
            //메모디비 내용 지우기
            //sSqLiteDatabase.execSQL("DELETE FROM Memo");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public void setNotibarColor() {
        //TODO : deprecated 된거 사용 ㄴㄴ
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorCyan));
        }
    }

    public static void showPopUpMenu(final View view, final int position) //TODO : 체크리스트 뜨도록 업그레이드
    {
        PopupMenu menu = new PopupMenu(view.getContext(), view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_delete:
                        deleteNoteAndSubCounter(view.getContext(), position);
                        updateAdapter();
                        break;
                    case R.id.menu_update:
                        Intent intent = new Intent(view.getContext(), Notepad.class);
                        intent.putExtra("update", new String[]{mArrayList.get(position).getMemo(), Integer.toString(position)});
                        view.getContext().startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        menu.inflate(R.menu.popup_menu);
        menu.show();
    }

    public static void deleteNoteAndSubCounter(Context context, int position) {
        sSqLiteDatabase = sNoteDBHelper.getWritableDatabase();
        String id = Integer.toString(mArrayList.get(position).getId());
        sSqLiteDatabase.delete(sNoteDBHelper.getDatabaseName(), sNoteDBHelper.getIdFieldName() + "=" + id, null);
        Log.i(sNoteDBHelper.getDatabaseName(), mArrayList.get(position).getId() + " : " + mArrayList.get(position).getMemo() + "정상적으로 삭제 되었습니다.");

        subCountNote(context);
    }

    public static void subCountNote(Context context) {
        selectCounterDB();
        sCounterDBHelper.subCounter();
        insertIntoCounterDB(context);
    }

    public static void updateAdapter() {
        mArrayList.clear();
        mArrayList.addAll(initList());
        mAdapter.notifyDataSetChanged();
    }

    public static ArrayList<ListViewItem> initList() {
        ArrayList<ListViewItem> temp = selectNoteDB();
        ArrayList<ListViewItem> list = new ArrayList<>();
        int tempSize = temp.size() - 1;

        if (tempSize >= 0) {
            String preDate = temp.get(tempSize).getDate();
            //날짜만 추가
            list.add(new ListViewItem(temp.get(tempSize).getId(), null, temp.get(tempSize).getDate(), 0, null));
            for (int i = tempSize; i >= 0; i--) {
                int id = temp.get(i).getId();
                String memo = temp.get(i).getMemo();
                String uri = temp.get(i).getUri();
                String date = temp.get(i).getDate();

                if (preDate.equals(date)) {
                    if (uri == null) {
                        list.add(new ListViewItem(id, memo, null, 1, null));
                    } else {
                        list.add(new ListViewItem(id, memo, null, 2, uri));
                    }
                } else {
                    list.add(new ListViewItem(id, null, date, 0, null));
                    if (uri == null) {
                        list.add(new ListViewItem(id, memo, null, 1, null));
                    } else {
                        list.add(new ListViewItem(id, memo, null, 2, uri));
                    }
                }
                preDate = temp.get(i).getDate();
            }
        }
        return list;
    }

    public static ArrayList<ListViewItem> selectNoteDB() {
        Cursor c = sSqLiteDatabase.query(sNoteDBHelper.getDatabaseName(), null, null, null, null, null, null);
        int dateIndex = c.getColumnIndex(sNoteDBHelper.getDateFieldName());
        int textIndex = c.getColumnIndex(sNoteDBHelper.getTextFieldName());
        int idIndex = c.getColumnIndex(sNoteDBHelper.getIdFieldName());
        int uriIndex = c.getColumnIndex(sNoteDBHelper.getURIFieldName());
        ArrayList<ListViewItem> list = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                list.add(new ListViewItem(Integer.valueOf(c.getString(idIndex)), c.getString(textIndex), c.getString(dateIndex), c.getString(uriIndex)));
            } while (c.moveToNext());
        }
        c.close();

        return list;
    }

    public static void selectCounterDB() {
        SQLiteDatabase db = sCounterDBHelper.getReadableDatabase();
        String sql = "SELECT " + sCounterDBHelper.getCounterFieldName() + " FROM " + sCounterDBHelper.getDatabaseName();
        Cursor c = db.rawQuery(sql, null);
        int counterIndex = c.getColumnIndex(sCounterDBHelper.getCounterFieldName());
        int counter;
        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                counter = Integer.valueOf(c.getString(counterIndex));
                sCounterDBHelper.setCounter(counter);
            }
        }
        c.close();
    }

    public static void insertIntoCounterDB(Context context) {
        CounterDBHelper helper = new CounterDBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(helper.getCounterFieldName(), sCounterDBHelper.getCounter());
        db.insert(helper.getDatabaseName(), null, values);
    }

    public static void updateNoteDB(int position, String memo) {
        sSqLiteDatabase = sNoteDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(sNoteDBHelper.getTextFieldName(), memo);
        sSqLiteDatabase.update(sNoteDBHelper.getDatabaseName(), values, sNoteDBHelper.getIdFieldName() + "=" + mArrayList.get(position).getId(), null);
        Log.i(sNoteDBHelper.getDatabaseName(), mArrayList.get(position).getId() + "정상적으로 업데이트 되었습니다.");
    }
}