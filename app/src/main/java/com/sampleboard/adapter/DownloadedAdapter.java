package com.sampleboard.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sampleboard.R;
import com.sampleboard.bean.LikedBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anuj Sharma on 4/5/2017.
 */

public class DownloadedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<LikedBean> list;
    Context context;
    public DownloadedAdapter(Context ctx,List<LikedBean> listing){
        this.context = ctx;
        this.list = listing;
    }
    public void updateData(List<LikedBean> downloadedList) {
        this.list = downloadedList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_downloaded,parent,false);
        DownloadedHolder vh = new DownloadedHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DownloadedHolder){
            DownloadedHolder vh = (DownloadedHolder)holder;
            Picasso.with(context).load("file://" + list.get(position).imageUrl).resize(200,200).centerCrop().into(vh.mDownloadedImg);
        }
    }

    @Override
    public int getItemCount() {
        return (list==null)?0:list.size();
    }



    private class DownloadedHolder extends RecyclerView.ViewHolder{
        ImageView mDownloadedImg;

        public DownloadedHolder(View itemView) {
            super(itemView);
            mDownloadedImg = (ImageView) itemView.findViewById(R.id.img_download);
        }
    }
}
