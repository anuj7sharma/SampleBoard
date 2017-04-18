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
import com.sampleboard.utils.Utils;
import com.sampleboard.view.fragment.profile.LikedFragment;
import com.sampleboard.view.activity.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anuj Sharma on 4/6/2017.
 */

public class LikedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<LikedBean> list;
    Context context;
    LikedFragment fragment;
    public LikedAdapter(Context ctx, List<LikedBean> listing, LikedFragment fragment){
        this.context = ctx;
        this.list = listing;
        this.fragment = fragment;
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

                    Intent profilePicIntent = new Intent(context,ProfileActivity.class);
                    profilePicIntent.putExtra("post_detail",detailBean);
                    profilePicIntent.putExtra("destination","post_detail");
                    if (Utils.getInstance().isEqualLollipop() && fragment!=null) {
                        Pair<View, String> p1 = Pair.create((View) mLikedImg, "detail_image");
                        ActivityOptions options =
                                ActivityOptions.makeSceneTransitionAnimation(fragment.getActivity(), p1);
                        context.startActivity(profilePicIntent, options.toBundle());
                    } else {
                        context.startActivity(profilePicIntent);
                    }
                }
            });
        }
    }
}
