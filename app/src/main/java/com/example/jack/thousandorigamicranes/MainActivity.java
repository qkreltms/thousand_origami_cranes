package com.example.jack.thousandorigamicranes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
/*
    170822 시작
 */
//TODO : 노트의 갯수에따라 병 이미지 변경
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();

        hideActionBar();
    }

    public void hideActionBar() {
        getSupportActionBar().hide();
    }

    public void onShowNoteList(View view) {
        Intent intent = new Intent(this, NoteList.class);
        startActivity(intent);
    }

    public void onAddNote(View view) {
        Intent intent = new Intent(this, Notepad.class);
        startActivity(intent);
    }
}
