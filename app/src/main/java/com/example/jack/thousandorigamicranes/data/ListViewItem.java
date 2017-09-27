package com.example.jack.thousandorigamicranes.data;

import android.support.annotation.Nullable;

import java.net.URI;

/**
 * Created by jack on 2017-08-23.
 */

public class ListViewItem {
    private int mId;
    private String mDate;
    private String mMemo;
    private int mType;
    private String mUri;

    public ListViewItem(int id, String memo, String date, int type, @Nullable String uri) {
        mId = id;
        mDate = date;
        mMemo = memo;
        mType = type;
        mUri = uri;
    }

    public ListViewItem(int id, String memo, String date, @Nullable String uri) {
        mId = id;
        mDate = date;
        mMemo = memo;
        mUri = uri;
    }

    public void setUri(String uri) { mUri = uri; }

    public void setId(int id) { mId = id; }

    public void setType(int type) {
        mType = type;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setMemo(String memo) {
        mMemo = memo;
    }

    public String getUri() { return mUri; }

    public String getDate() {
        return mDate;
    }

    public String getMemo() {
        return mMemo;
    }

    public int getType() { return mType; }

    public int getId() { return mId; }
}
