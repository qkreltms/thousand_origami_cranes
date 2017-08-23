package com.example.jack.thousandorigamicranes;

/**
 * Created by jack on 2017-08-23.
 */

public class ListViewItem {

    private String mDate;
    private String mMemo;
    private int mType;

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

    public int getType() {
        return mType;
    }
}
