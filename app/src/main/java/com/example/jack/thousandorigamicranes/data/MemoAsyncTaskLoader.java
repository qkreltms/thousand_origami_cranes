package com.example.jack.thousandorigamicranes.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by jack on 2017-08-31.
 * 다 뜯어 고쳐야하므로 다음 앱으로 미룸
 */

public class MemoAsyncTaskLoader extends AsyncTaskLoader {

    NoteDBHelper noteDBHelper;
    SQLiteDatabase db;

    public MemoAsyncTaskLoader(Context context) {
        super(context);
        noteDBHelper = new NoteDBHelper(context);
        db = noteDBHelper.getReadableDatabase();
    }

    @Override
    public ArrayList<ListViewItem> loadInBackground() {
        return initList();
    }

    public ArrayList<ListViewItem> initList() {
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

    public ListViewItem addItem(int id, @Nullable String memo, @Nullable String date, int type) {
        return  new ListViewItem(id, memo, date, type);
    }

    public ArrayList<ListViewItem> selectDB() {
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
}
