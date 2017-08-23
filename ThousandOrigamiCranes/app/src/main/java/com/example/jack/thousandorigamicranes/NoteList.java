package com.example.jack.thousandorigamicranes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NoteList extends AppCompatActivity {
    private Adapter mAdapter;
    private ArrayList<String> mList;
    private RecyclerView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        createList();
        hideActionBar();
    }

    public void createList() {
        mListView = (RecyclerView) findViewById(R.id.list_show_memo);
        mList = new ArrayList<>();
        mAdapter = new Adapter(mList);
        //TODO : 분리하기
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLayoutManager);

        mList.add("test");

        mAdapter = new Adapter(mList);
        mListView.setAdapter(mAdapter);
    }

    public void hideActionBar() {
        getSupportActionBar().hide();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO : save data
    }
}
