package com.example.jack.thousandorigamicranes;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList mDataset;

    // 1. 어뎁터에서 데이터를 받는다
    public Adapter(ArrayList myDataset) {
        mDataset = myDataset;
    }

    // 2. 뷰가 처음 생성시 만들어준다
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_contents, parent, false);
        return new ViewHolder(v);
    }

    //3. view 재생성시
    //recyclerview에서는 getView와 같은용도
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText("test"); //TODO : database에서 불러오기
        //TODO : 날짜가 같은지 판별하는 if문 넣어서 다르면 날짜 표시하는 뷰 생성 후 text뷰 만들기 같으면 text뷰만 생성
        setAnimation(holder.textView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    //viewholder 만들기 필수!!
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text_memo);
        }
    }

    //엑티비티 실행시, 새로 생성시 옆에서 나타나는 에니메이션
    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);

        return;
    }
}

