package com.sampleboard.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.sampleboard.R;
import com.sampleboard.bean.PhotosBean;
import com.sampleboard.presenters.PhotosListPresenter;
import com.sampleboard.utils.CustomAnimationDrawableNew;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.DashBoardActivity;
import com.sampleboard.view.shortcutView.ShortCutFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by Mobilyte India Pvt Ltd on 3/1/2017.
 */

public class PhotosListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PhotosBean> mResponse;
    private PhotosListPresenter presenter;
    private AnimationDrawable frameAnimation;
    private int lastPosition = -1;

    public final static int COLOR_ANIMATION_DURATION = 1000;
    private int mDefaultBackgroundColor;

    public PhotosListAdapter(Context ctx, List<PhotosBean> response, PhotosListPresenter presenter) {
        this.mContext = ctx;
        this.mResponse = response;
        this.presenter = presenter;
    }

    public void updateList(List<PhotosBean> response) {
        this.mResponse = response;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView;
        RecyclerView.ViewHolder vh;
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_photoslist, parent, false);
        vh = new LoadMoreViewHolder(rowView);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LoadMoreViewHolder vh = (LoadMoreViewHolder) holder;
        try {
            vh.mTitle.setText(mResponse.get(position).title);
            vh.mPrice.setText(mResponse.get(position).price);

//            Picasso.with(mContext).load(mResponse.get(position).photoUrl).resize(500,500).centerCrop().into(vh.mImage);

            Picasso.with(mContext)
                    .load(mResponse.get(position).photoUrl)
                    .resize(500, 500).centerCrop()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            /* Save the bitmap or do something with it here */
                            //Set it in the ImageView
                            vh.mImage.setImageBitmap(bitmap);
                            /*
                            Use Pallet For Getting Color
                             */
                            Palette palette = Palette.from(bitmap).generate();
                            if (palette != null) {
                                Palette.Swatch s = palette.getVibrantSwatch();
                                if (s == null) {
                                    s = palette.getDarkVibrantSwatch();
                                }
                                if (s == null) {
                                    s = palette.getLightVibrantSwatch();
                                }
                                if (s == null) {
                                    s = palette.getMutedSwatch();
                                }

                                if (s != null) {
                                    vh.mParentLayout.setBackgroundColor(s.getTitleTextColor());
                                    vh.mInfoContainer.setBackgroundColor(s.getTitleTextColor());
                                    vh.mTitle.setTextColor(s.getTitleTextColor());
                                    vh.mPrice.setTextColor(s.getTitleTextColor());
                                }
                                Utils.getInstance().animateViewColor(vh.mInfoContainer, mDefaultBackgroundColor, s.getRgb());
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });


//            frameAnimation = (AnimationDrawable) vh.mLikeImgInitial .getBackground();
            //set true if you want to animate only once
//            frameAnimation.setOneShot(true);

            // Here you apply the animation when the view is bound
//            setAnimation(holder.itemView, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
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
        private LinearLayout mParentLayout;
        private RelativeLayout mInfoContainer;
        private ImageView mImage, mLikeImgInitial, mLikeImgFinal;
        ;
        private TextView mTitle, mPrice, mLikesCount;

        private LoadMoreViewHolder(View itemView) {
            super(itemView);
            mParentLayout = (LinearLayout) itemView.findViewById(R.id.parent);
            mInfoContainer = (RelativeLayout) itemView.findViewById(R.id.info_container);
            mImage = (ImageView) itemView.findViewById(R.id.image);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mPrice = (TextView) itemView.findViewById(R.id.price);
            mLikeImgInitial = (ImageView) itemView.findViewById(R.id.ic_heart_initial);
            mLikeImgFinal = (ImageView) itemView.findViewById(R.id.ic_heart_final);
            mLikesCount = (TextView) itemView.findViewById(R.id.likes_count);

            mParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (presenter != null) {
                        presenter.onItemClick(mResponse.get(getAdapterPosition()), mImage);
                    }
                }
            });

            mParentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {

                    // Create a system to run the physics loop for a set of springs.
                    SpringSystem springSystem = SpringSystem.create();

                    // Add a spring to the system.
                    Spring spring = springSystem.createSpring();

                    // Add a listener to observe the motion of the spring.
                    spring.addListener(new SimpleSpringListener() {

                        @Override
                        public void onSpringUpdate(Spring spring) {
                            // You can observe the updates in the spring
                            // state by asking its current value in onSpringUpdate.


                            float value = (float) spring.getCurrentValue();
                            float scale = 1f - (value * 0.5f);
                            v.setScaleX(scale);
                            v.setScaleY(scale);
                        }
                    });

// Set the spring in motion; moving from 0 to 1
                    spring.setEndValue(1);


                    return false;
                }
            });

            mLikeImgInitial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLikeImgInitial.setClickable(false);
                    CustomAnimationDrawableNew cad = new CustomAnimationDrawableNew((AnimationDrawable) mContext.getResources().getDrawable(
                            R.drawable.animation_list_layout)) {
                        @Override
                        public void onAnimationFinish() {
                            mLikeImgInitial.setVisibility(View.GONE);
                            mLikeImgFinal.setVisibility(View.VISIBLE);
                            mLikeImgFinal.setClickable(true);
//                            updateLikesCounter(1,true);
                        }
                    };
                    mLikeImgInitial.setBackgroundDrawable(cad);
                    cad.start();
                }
            });

            mLikeImgFinal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLikeImgInitial.setVisibility(View.VISIBLE);
                    mLikeImgInitial.setClickable(true);
                    mLikeImgFinal.setVisibility(View.GONE);
                    mLikeImgFinal.setClickable(false);
                    mLikeImgInitial.setBackgroundResource(R.drawable.animation_list_layout);
//                    updateLikesCounter(1,false);
                }
            });
        }

    }
}
