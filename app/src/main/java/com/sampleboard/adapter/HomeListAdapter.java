package com.sampleboard.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sampleboard.R;
import com.sampleboard.bean.api_response.TimelineObjResponse;
import com.sampleboard.interfaces.TimelineInterface;
import com.sampleboard.utils.CustomAnimationDrawableNew;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.fragment.dashboard.HomeFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * @author AnujSharma
 */

public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<TimelineObjResponse> mResponse;
    private int lastPosition = -1;
    //    public final static int COLOR_ANIMATION_DURATION = 1000;
    private int mDefaultBackgroundColor;
    private TimelineInterface listener;
    private Fragment fragment;

    public HomeListAdapter(Context ctx, List<TimelineObjResponse> response, TimelineInterface listener, Fragment fragment) {
        this.mContext = ctx;
        this.mResponse = response;
        this.listener = listener;
        this.fragment = fragment;
    }

    public void updateList(List<TimelineObjResponse> response) {
        this.mResponse = response;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView;
        RecyclerView.ViewHolder vh;
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list, parent, false);
        vh = new LoadMoreViewHolder(rowView);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LoadMoreViewHolder vh = (LoadMoreViewHolder) holder;
        try {
            TimelineObjResponse obj = mResponse.get(position);
            vh.mTitle.setText(obj.getTitle());
            vh.mLikesCount.setText(String.valueOf(obj.getLikeCount()));
            // update like heart symbol
            if (!TextUtils.isEmpty(obj.getIsLiked()) && obj.getIsLiked().equals("1")) {
                vh.mLikeImgInitial.setVisibility(View.GONE);
                vh.mLikeImgFinal.setVisibility(View.VISIBLE);
                vh.mLikesCount.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                vh.mLikeImgFinal.setClickable(true);
            } else {
                vh.mLikeImgInitial.setVisibility(View.VISIBLE);
                vh.mLikeImgInitial.setClickable(true);
                vh.mLikeImgFinal.setVisibility(View.GONE);
                vh.mLikesCount.setTextColor(ContextCompat.getColor(mContext, R.color.app_textcolor_heading));
                vh.mLikeImgFinal.setClickable(false);
                vh.mLikeImgInitial.setBackgroundResource(R.drawable.animation_list_layout);
            }

//            if (obj.isShared()) {
//                vh.reshareImg.setColorFilter(ContextCompat.getColor(mContext, R.color.green));
//            } else {
//                vh.reshareImg.setColorFilter(ContextCompat.getColor(mContext, R.color.app_textcolor_heading));
//            }
            //cancel any loading images on this view
            Picasso.with(mContext).cancelRequest(vh.mImage);
            vh.mImage.setImageBitmap(null);
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    assert vh.mImage != null;
                            /* Save the bitmap or do something with it here */
                    Palette.from(bitmap)
                            .generate(palette -> {
                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                if (textSwatch == null) {
                                    return;
                                }
                                vh.mParentLayout.setBackgroundColor(textSwatch.getRgb());
//                                            vh.mInfoContainer.setBackgroundColor(textSwatch.getRgb());
                                vh.mTitle.setTextColor(textSwatch.getTitleTextColor());
                                Utils.animateViewColor(vh.mInfoContainer, mDefaultBackgroundColor, textSwatch.getRgb());
                            });
                    vh.mImage.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    vh.mImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_default_image));
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            // set the tag to the view
            vh.mImage.setTag(target);
            if (!TextUtils.isEmpty(obj.getMedia())) {
                Picasso.with(mContext).load("http://10.20.3.169" + obj.getMedia())
                        .resize(500, 500).centerCrop()
                        .into(target);
            } else {
                vh.mImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_default_image));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (mResponse == null) ? 0 : mResponse.size();
    }

    /*
    View Holder For Trip History
     */
    private class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        //        CardView mCardView;
        private LinearLayout mParentLayout;
        private RelativeLayout mInfoContainer;
        private ImageView reshareImg, mImage, mLikeImgInitial, mLikeImgFinal;
        private TextView mTitle, mLikesCount;

        private LoadMoreViewHolder(View itemView) {
            super(itemView);
            mParentLayout = itemView.findViewById(R.id.parent);
            mInfoContainer = itemView.findViewById(R.id.info_container);
            reshareImg = itemView.findViewById(R.id.img_re_share);
            mImage = itemView.findViewById(R.id.image);
            mTitle = itemView.findViewById(R.id.title);
            mLikeImgInitial = itemView.findViewById(R.id.ic_heart_initial);
            mLikeImgFinal = itemView.findViewById(R.id.ic_heart_final);
            mLikesCount = itemView.findViewById(R.id.likes_count);

            mImage.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(mResponse.get(getAdapterPosition()), (ImageView) v, getAdapterPosition());

            });
            reshareImg.setOnClickListener(view -> {

            });

            mLikeImgInitial.setOnClickListener(v -> {
                if (listener != null && mResponse.get(getAdapterPosition()) != null) {
                    //Hit API to set is_liked 1
                    listener.onLikeBtnClicked(mResponse.get(getAdapterPosition()), null, getAdapterPosition(), true);
                }

                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragment).getIsEverythingFine().observe(fragment, aBoolean -> {
                        if (aBoolean) {
                            mLikeImgInitial.setClickable(false);
                            CustomAnimationDrawableNew cad = new CustomAnimationDrawableNew((AnimationDrawable) mContext.getResources().getDrawable(
                                    R.drawable.animation_list_layout)) {
                                @Override
                                public void onAnimationFinish() {
                                    mLikeImgInitial.setVisibility(View.GONE);
                                    mLikeImgFinal.setVisibility(View.VISIBLE);
                                    mLikeImgFinal.setClickable(true);
                                }

                                @Override
                                public void onAnimtionStart() {
                                    final LinearInterpolator interpolator = new LinearInterpolator();
                                    int updatedCount = updateLikesCounter(mResponse.get(getAdapterPosition()).getLikeCount(),
                                            true);
                                    mResponse.get(getAdapterPosition()).setLikeCount(updatedCount);
                                    mLikesCount.animate()
                                            .alpha(0)
                                            .setDuration(100)
                                            .setStartDelay(200)
                                            .setInterpolator(interpolator)
                                            .withEndAction(() -> {
                                                mLikesCount.animate()
                                                        .alpha(1)
                                                        .setDuration(100)
                                                        .setInterpolator(interpolator);
                                                mLikesCount.setText(String.valueOf(updatedCount));
                                                mLikesCount.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                                            });
                                }

                            };
                            mLikeImgInitial.setBackgroundDrawable(cad);
                            cad.start();
                        }
                    });
                }


            });

            mLikeImgFinal.setOnClickListener(v -> {
                if (listener != null && mResponse.get(getAdapterPosition()) != null) {
                    //Hit API to set is_liked 0
                    listener.onLikeBtnClicked(mResponse.get(getAdapterPosition()), null, getAdapterPosition(), false);
                }
                ((HomeFragment) fragment).getIsEverythingFine().observe(fragment, aBoolean -> {
                    if (aBoolean) {
                        mLikeImgInitial.setVisibility(View.VISIBLE);
                        mLikeImgInitial.setClickable(true);
                        mLikeImgFinal.setVisibility(View.GONE);
                        mLikesCount.setTextColor(ContextCompat.getColor(mContext, R.color.app_textcolor));
                        mLikeImgFinal.setClickable(false);
                        mLikeImgInitial.setBackgroundResource(R.drawable.animation_list_layout);
                        int updatedCount = updateLikesCounter(mResponse.get(getAdapterPosition()).getLikeCount(),
                                false);


                        mResponse.get(getAdapterPosition()).setLikeCount(updatedCount);
                        mLikesCount.setText(String.valueOf(updatedCount));
                    }
                });
            });
        }
    }

    private int updateLikesCounter(int count, boolean isIncreased) {
        if (isIncreased)
            return count + 1;
        else
            return count - 1;
    }

}
