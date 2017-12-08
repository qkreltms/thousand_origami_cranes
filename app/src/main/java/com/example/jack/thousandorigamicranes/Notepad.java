package com.example.jack.thousandorigamicranes;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jack.thousandorigamicranes.data.CounterDBHelper;
import com.example.jack.thousandorigamicranes.data.NoteDBHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notepad extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private EditText mMemoEditText;
    private String mMemoText;
    private String mMemoImgUri;
    private CounterDBHelper mCounterDBHelper;
    private static final int GALLERY_CODE = 1;
    private static final int READ_STORAGE_PERMISSION = 2;
    private Context mContext;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        setNotibarColor();
        setToolbar();
        mMemoEditText = (EditText) findViewById(R.id.edt_memo);
        mMemoEditText.addTextChangedListener(this);
        mCounterDBHelper = new CounterDBHelper(this);
        mContext = this.getApplicationContext();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNotepad();
        //카운터 내용 디비 지우기
//        SQLiteDatabase db = mCounterDBHelper.getWritableDatabase();
//        db.execSQL("DELETE FROM CounterDB");
    }

    public void setToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        //TODO : 타이틀이름 xml에서 설정하기
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notepad_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_CODE:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION);
                        } else {
                            addPicture(data);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STORAGE_PERMISSION)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPictureFromGallery();
            }
    }

    public void addPicture(Intent data) {
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            String uri = getPath(data.getData());
            Toast.makeText(this, uri, Toast.LENGTH_LONG).show();
            mMemoImgUri = uri;
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPicture(bitmap);
    }

    private void getPictureFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }

    public void setPicture(Bitmap bitmap) {
        //TODO: 레이아웃 인플레이터 사용.
        if (bitmap != null) {
            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.notepadLayout);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmap);
            Picasso.with(this)
                    .load(bitmap.toString())
                    .resize(1000, 800)
                    .into(imageView);
            layout.addView(imageView);
            View v = LayoutInflater.from(mContext).inflate(R.layout.list_view_date, null, false);
            layout.addView(v);

        }
    }

    public String getPath(Uri uri) {
        // uri가 null일경우 null반환
        if( uri == null ) {
            return null;
        }
        // 미디어스토어에서 유저가 선택한 사진의 URI를 받아온다.
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // URI경로를 반환한다.
        return uri.getPath();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_picture:
                getPictureFromGallery();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setNotibarColor() {
        //TODO : deprecated 된거 사용 ㄴㄴ
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorCyan));
        }
    }

    public void updateNotepad() {
        if (getIntent().hasExtra("update")) {
            setTextAtNotepad();
        }
    }

    public void setTextAtNotepad() {
        String memo = getIntent().getStringArrayExtra("update")[0];
        if (memo.length() > 0) {
            mMemoEditText.setText(memo);
        }
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
        SimpleDateFormat dayOfTheWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date) + " " + dayOfTheWeekFormat.format(date);
    }

    public boolean checkDataIsNull() {
        return (mMemoText == null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        update();
    }

    public void update() {
        if (!checkDataIsNull()) {
            if (!isUpdate()) {
                if (mMemoImgUri == null) {
                    insertAndCountNote(mMemoText, null);
                } else {
                    insertAndCountNote(mMemoText, mMemoImgUri);
                }
            } else {
                int position = Integer.parseInt(getIntent().getStringArrayExtra("update")[1]);
                NoteList.updateNoteDB(position, mMemoText);
            }
        }
    }

    public boolean isUpdate() {
        return getIntent().hasExtra("update");
    }

    public void countNote() {
        selectCounterDB();
        mCounterDBHelper.addCounter();
        insertIntoCounterDB();
    }

    public void insertAndCountNote(String text, @Nullable String uri) {
        NoteDBHelper helper = new NoteDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(helper.getDateFieldName(), getDate());
        values.put(helper.getTextFieldName(), text);
        if (uri != null) {
            values.put(helper.getURIFieldName(), uri);
        }
        db.insert(helper.getDatabaseName(), null, values);
        countNote();
    }

    public void selectCounterDB() {
        SQLiteDatabase db = mCounterDBHelper.getReadableDatabase();
        String sql = "SELECT " + mCounterDBHelper.getCounterFieldName() + " FROM " + mCounterDBHelper.getDatabaseName();
        Cursor c = db.rawQuery(sql, null);
        int counterIndex = c.getColumnIndex(mCounterDBHelper.getCounterFieldName());
        int counter;
        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                counter = Integer.valueOf(c.getString(counterIndex));
                mCounterDBHelper.setCounter(counter);
            }
        }
        c.close();
    }



    public void insertIntoCounterDB() {
        CounterDBHelper helper = new CounterDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(helper.getCounterFieldName(), mCounterDBHelper.getCounter());
        db.insert(helper.getDatabaseName(), null, values);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mMemoText = String.valueOf(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void onClick(View view) {
        Log.i("test", "onClick: ");
        switch (view.getId()) {
            case R.id.fab :
                update();
                finish();
                Log.i("test", "onClick: ");
                break;
        }
    }
}
