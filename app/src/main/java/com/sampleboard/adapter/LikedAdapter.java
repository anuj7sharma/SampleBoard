package com.sampleboard.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sampleboard.R;
import com.sampleboard.bean.LikedBean;
import com.sampleboard.bean.PostDetailBean;
import com.sampleboard.utils.Constants;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.activity.DetailActivityV2;
import com.sampleboard.view.activity.HolderActivity;
import com.sampleboard.view.fragment.profile.LikedFragment;
import com.sampleboard.view.activity.ProfileActivity;
import com.sampleboard.view.fragment.detail.DetailFragment;
import com.sampleboard.view.fragment.profile.UserProfileFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anuj Sharma on 4/6/2017.
 */

public class LikedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<LikedBean> list;
    Context context;
    LikedFragment fragment;
    DetailFragment detailFragment;
    UserProfileFragment userProfileFragment;
    public LikedAdapter(Context ctx, List<LikedBean> listing, LikedFragment fragment){
        this.context = ctx;
        this.list = listing;
        this.fragment = fragment;
    }
    public LikedAdapter(Context ctx, List<LikedBean> listing, DetailFragment fragment){
        this.context = ctx;
        this.list = listing;
        this.detailFragment = fragment;
    }

    public LikedAdapter(Context ctx, List<LikedBean> listing, UserProfileFragment fragment){
        this.context = ctx;
        this.list = listing;
        this.userProfileFragment = fragment;
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
            mLikedImg =  itemView.findViewById(R.id.img_like);

            mLikedImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Move to Image
                    PostDetailBean detailBean = new PostDetailBean();
                    detailBean.photoName = list.get(getAdapterPosition()).imageName;
                    detailBean.photoUrl = list.get(getAdapterPosition()).imageUrl;
                    detailBean.likeCount = 514;
                    detailBean.commentCount = 356;
                    detailBean.isLiked = true;
                    detailBean.ownerName = "Anuj Sharma";

                    Intent intent = new Intent(context, DetailActivityV2.class);
                    intent.putExtra(Constants.DESTINATION, Constants.DETAIL_SCREEN);
                    intent.putExtra(Constants.OBJ_DETAIL,detailBean);
                    if (Utils.getInstance().isEqualLollipop() && fragment!=null) {
                        Pair<View, String> p1 = Pair.create((View) mLikedImg, mLikedImg.getTransitionName());
                        ActivityOptions options =
                                ActivityOptions.makeSceneTransitionAnimation(fragment.getActivity(), p1);
                        context.startActivity(intent, options.toBundle());
                    }
                    else if (Utils.getInstance().isEqualLollipop() && detailFragment!=null) {
                        Pair<View, String> p1 = Pair.create((View) mLikedImg, "detail_image");
                        ActivityOptions options =
                                ActivityOptions.makeSceneTransitionAnimation(detailFragment.getActivity(), p1);
                        context.startActivity(intent, options.toBundle());
                    }
                    else if (Utils.getInstance().isEqualLollipop() && userProfileFragment!=null) {
                        Pair<View, String> p1 = Pair.create((View) mLikedImg, "detail_image");
                        ActivityOptions options =
                                ActivityOptions.makeSceneTransitionAnimation(userProfileFragment.getActivity(), p1);
                        context.startActivity(intent, options.toBundle());
                    }
                    else {
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
