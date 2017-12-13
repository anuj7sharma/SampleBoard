package com.sampleboard.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peekandpop.shalskar.peekandpop.PeekAndPop;
import com.sampleboard.R;
import com.sampleboard.bean.PhotosBean;
import com.sampleboard.interfaces.MediaListInterface;
import com.sampleboard.utils.CustomAnimationDrawableNew;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.fragment.dashboard.HomeFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by Mobilyte India Pvt Ltd on 3/1/2017.
 */

public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PhotosBean> mResponse;
    private AnimationDrawable frameAnimation;
    private int lastPosition = -1;
    public final static int COLOR_ANIMATION_DURATION = 1000;
    private int mDefaultBackgroundColor;
    private MediaListInterface listener;

    public HomeListAdapter(Context ctx, List<PhotosBean> response, MediaListInterface listener) {
        this.mContext = ctx;
        this.mResponse = response;
        this.listener = listener;
    }

    public void updateList(List<PhotosBean> response) {
        this.mResponse = response;
        notifyDataSetChanged();
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
            vh.mTitle.setText(mResponse.get(position).title);
            vh.mPrice.setText(mResponse.get(position).price);

            Picasso.with(mContext).load(mResponse.get(position).photoUrl).resize(500, 500).centerCrop().into(vh.mImage);

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
            mParentLayout = itemView.findViewById(R.id.parent);
            mInfoContainer = itemView.findViewById(R.id.info_container);
            mImage = itemView.findViewById(R.id.image);
            mTitle = itemView.findViewById(R.id.title);
            mPrice = itemView.findViewById(R.id.price);
            mLikeImgInitial = itemView.findViewById(R.id.ic_heart_initial);
            mLikeImgFinal = itemView.findViewById(R.id.ic_heart_final);
            mLikesCount = itemView.findViewById(R.id.likes_count);

//            mParentLayout.setOnLongClickListener(v -> {
////                manageShortView(v, mResponse.get(getAdapterPosition()));
//                return false;
//            });
            mParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(mResponse.get(getAdapterPosition()), mImage);

                }
            });

            mLikeImgInitial.setOnClickListener(v -> {
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
            });

            mLikeImgFinal.setOnClickListener(v -> {
                mLikeImgInitial.setVisibility(View.VISIBLE);
                mLikeImgInitial.setClickable(true);
                mLikeImgFinal.setVisibility(View.GONE);
                mLikeImgFinal.setClickable(false);
                mLikeImgInitial.setBackgroundResource(R.drawable.animation_list_layout);
//                    updateLikesCounter(1,false);
            });
        }
    }

    /*private void manageShortView(View v, PhotosBean bean) {
        v.setHapticFeedbackEnabled(true);
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        PeekAndPop peekAndPop = new PeekAndPop.Builder(fragment.getActivity())
                .peekLayout(R.layout.fragment_shortview)
                .longClickViews(v)
                .blurBackground(true)
                .parentViewGroupToDisallowTouchEvents((ViewGroup) fragment.getView())
                .build();
        View peekView = peekAndPop.getPeekView();
        CardView cardView = (CardView) peekView.findViewById(R.id.parent_shortView);
        ImageView imageView = (ImageView) peekView.findViewById(R.id.img_shortview);
        ImageView likeImg = (ImageView) peekView.findViewById(R.id.btn_like);
        ImageView commentImg = (ImageView) peekView.findViewById(R.id.btn_message);
        ImageView shareImg = (ImageView) peekView.findViewById(R.id.btn_share);
        final ProgressBar progressBar = (ProgressBar) peekView.findViewById(R.id.progress_bar);

        peekAndPop.addLongHoldView(R.id.parent_shortView, false);
        peekAndPop.addHoldAndReleaseView(R.id.btn_like);
        peekAndPop.addHoldAndReleaseView(R.id.btn_message);
        peekAndPop.addHoldAndReleaseView(R.id.btn_share);
        peekAndPop.setOnHoldAndReleaseListener(new PeekAndPop.OnHoldAndReleaseListener() {
            @Override
            public void onHold(View view, int position) {
                switch (view.getId()) {
                    case R.id.btn_like:
                        Toast.makeText(mContext, "Like", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_message:
                        Toast.makeText(mContext, "Message", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_share:
                        Toast.makeText(mContext, "Share", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onLeave(View view, int position) {

            }

            @Override
            public void onRelease(View view, int position) {

            }
        });

//                    peekAndPop.addHoldAndReleaseView(R.id.img_shortview);

        Picasso.with(mContext).load(bean.photoUrl).resize(500, 500).
                centerCrop().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }*/
}
