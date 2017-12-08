package com.example.jack.thousandorigamicranes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jack.thousandorigamicranes.data.ListViewItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private  Context context;
    private ArrayList<ListViewItem> mDataset;
    // 1. 어뎁터에서 데이터를 받는다

    public Adapter(Context context, ArrayList<ListViewItem> data) {
        this.context = context;
        mDataset = data;
    }

    public void setData(ArrayList<ListViewItem> myDataset) {
        this.mDataset = myDataset;
    }

    // 2. 뷰가 처음 생성시 만들어준다
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_date, parent, false);
        switch (viewType) {
            case 0:
                break;
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_memo, parent, false);
                break;
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_image, parent, false);
                break;
        }
        return new ViewHolder(v);
    }

    //3. view 재생성시
    //recyclerview에서는 getView와 같은용도
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mDataset.get(position).getType() == 0) {
            holder.mDate.setText(mDataset.get(position).getDate());
            setAnimation(holder.mDate);
        } else if (mDataset.get(position).getType() == 1) {
            holder.mTextView.setText(mDataset.get(position).getMemo());
            holder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    NoteList.showPopUpMenu(view, position);
                    return false;
                }
            });
            setAnimation(holder.mTextView);
        } else if (mDataset.get(position).getType() == 2) {
            String imgUri = mDataset.get(position).getUri();
            //TODO : 사진누르면 확대기능 추가
            Picasso.with(context)
                    .load(new File(imgUri))
                    .resize(1000, 800)
                    .into(holder.mImageView);
            holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    NoteList.showPopUpMenu(view, position);
                    return false;
                }
            });
            setAnimation(holder.mImageView);

            holder.mTextView.setText(mDataset.get(position).getMemo());
            holder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    NoteList.showPopUpMenu(view, position);
                    return false;
                }
            });
            setAnimation(holder.mTextView);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).getType();
    }

    //viewholder 만들기 필수!!
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private TextView mDate;
        private ImageView mImageView;

        private ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.text_memo);
            mDate = v.findViewById(R.id.text_date);
            mImageView = v.findViewById(R.id.img_memo);
        }
    }

    //    엑티비티 실행시, 새로 생성시 옆에서 나타나는 에니메이션
    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
    }
}

