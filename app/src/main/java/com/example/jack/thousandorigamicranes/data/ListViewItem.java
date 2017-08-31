package com.example.jack.thousandorigamicranes.data;

/**
 * Created by jack on 2017-08-23.
 */

public class ListViewItem {

    private int mId;
    private String mDate;
    private String mMemo;
    private int mType;

    public ListViewItem(int id, String memo, String date, int type) {
        mId = id;
        mDate = date;
        mMemo = memo;
        mType = type;
    }

    public ListViewItem(int id, String memo, String date) {
        mId = id;
        mDate = date;
        mMemo = memo;
    }

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

    public String getDate() {
        return mDate;
    }

    public String getMemo() {
        return mMemo;
    }

    public int getType() { return mType; }

    public int getId() { return mId; }
}
