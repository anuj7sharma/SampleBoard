package com.androidpay.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidpay.R;
import com.androidpay.bean.PhotosBean;
import com.androidpay.presenters.PhotosListPresenter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mobilyte India Pvt Ltd on 3/1/2017.
 */

public class PhotosListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<PhotosBean> mResponse;
    private PhotosListPresenter presenter;

    public PhotosListAdapter(Context ctx, List<PhotosBean> response, PhotosListPresenter presenter){
        this.mContext = ctx;
        this.mResponse = response;
        this.presenter = presenter;
    }

    public void updateList(List<PhotosBean> response){
        this.mResponse = response;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView;
        RecyclerView.ViewHolder vh;
        rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);
        vh = new LoadMoreViewHolder(rowView);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LoadMoreViewHolder vh = (LoadMoreViewHolder)holder;
        try {
            Picasso.with(mContext).load(mResponse.get(position).photoUrl).resize(500,500).centerCrop().into(vh.mImage);
            vh.mTitle.setText(mResponse.get(position).title);
            vh.mPrice.setText(mResponse.get(position).price);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mResponse.size();
    }

    /*
    View Holder For Trip History
     */
    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        //        CardView mCardView;
        private CardView mParentLayout;
        private ImageView mImage;
        private TextView mTitle, mPrice;

        private LoadMoreViewHolder(View itemView) {
            super(itemView);
            mParentLayout = (CardView)itemView.findViewById(R.id.parent);
            mImage = (ImageView)itemView.findViewById(R.id.image);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mPrice = (TextView) itemView.findViewById(R.id.price);

            mParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(presenter!=null){
                        presenter.onItemClick(mResponse.get(getAdapterPosition()),mImage);
                    }
                }
            });
        }

    }
}
