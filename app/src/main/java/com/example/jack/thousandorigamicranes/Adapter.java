package com.example.jack.thousandorigamicranes;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<ListViewItem> mDataset;

    // 1. 어뎁터에서 데이터를 받는다
    public Adapter(ArrayList<ListViewItem> myDataset) {
        mDataset = myDataset;
    }

    // 2. 뷰가 처음 생성시 만들어준다
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_date, parent, false);

        switch (viewType) {
            case 0 :
                break;
            case 1 :
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_memo, parent, false);
                break;

        }
        return new ViewHolder(v);
    }

    //3. view 재생성시
    //recyclerview에서는 getView와 같은용도
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mDataset.get(position).getType() == 0) {
            holder.date.setText(mDataset.get(position).getDate());
            setAnimation(holder.date);
        } else if (mDataset.get(position).getType() == 1) {
            holder.textView.setText(mDataset.get(position).getMemo());
            setAnimation(holder.textView);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



    @Override
    public int getItemViewType(int position) {
        Log.i("뷰타입", Integer.toString(mDataset.get(position).getType()));
        return mDataset.get(position).getType();
    }



    //viewholder 만들기 필수!!
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView date;

        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.text_memo);
            date = v.findViewById(R.id.text_date);
        }
    }

//    엑티비티 실행시, 새로 생성시 옆에서 나타나는 에니메이션
    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
    }
}

