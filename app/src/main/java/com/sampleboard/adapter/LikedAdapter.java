package com.sampleboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sampleboard.R;
import com.sampleboard.bean.LikedBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anuj Sharma on 4/6/2017.
 */

public class LikedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<LikedBean> list;
    Context context;
    public LikedAdapter(Context ctx, List<LikedBean> listing){
        this.context = ctx;
        this.list = listing;
    }
    public void updateData(List<LikedBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_liked,parent,false);
        LIkedHolder vh = new LIkedHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LIkedHolder){
            LIkedHolder vh = (LIkedHolder)holder;
            Picasso.with(context).load(list.get(position).imageUrl).resize(200,200).centerCrop().into(vh.mLikedImg);
        }
    }

    @Override
    public int getItemCount() {
        return (list==null)?0:list.size();
    }



    private class LIkedHolder extends RecyclerView.ViewHolder{
        ImageView mLikedImg;

        public LIkedHolder(View itemView) {
            super(itemView);
            mLikedImg = (ImageView) itemView.findViewById(R.id.img_like);
        }
    }
}
