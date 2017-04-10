package com.sampleboard.view.profile;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sampleboard.R;
import com.sampleboard.bean.PhotosBean;
import com.sampleboard.bean.PostDetailBean;
import com.sampleboard.utils.CustomAnimationDrawableNew;
import com.sampleboard.view.BaseFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anuj Sharma on 4/10/2017.
 */

public class PostDetailFragment extends BaseFragment implements View.OnClickListener {
    private View rootVIew;

    private Toolbar mToolbar;
    private ImageView mDetailImage, mLikeImgInitial, mLikeImgFinal;
    private ProgressBar mProgresbar;
    private CircleImageView mOwnerImg;
    private TextView mOwnerName, mLikeCount, mCommentCount;

    private AnimationDrawable frameAnimation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootVIew = inflater.inflate(R.layout.fragment_post_detail, container, false);
        return rootVIew;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        loadIntiialData();
    }

    private void initViews() {
        mToolbar = (Toolbar) rootVIew.findViewById(R.id.toolbar);
        ((ProfileActivity) getActivity()).setSupportActionBar(mToolbar);
        ((ProfileActivity) getActivity()).getSupportActionBar().setTitle("Detail");
        mToolbar.setNavigationIcon(R.drawable.ic_navigation_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProfileActivity) getActivity()).oneStepBack();
            }
        });

        mDetailImage = (ImageView) rootVIew.findViewById(R.id.post_detail_img);
        mProgresbar = (ProgressBar) rootVIew.findViewById(R.id.progress_bar);
        mOwnerImg = (CircleImageView) rootVIew.findViewById(R.id.owner_img);
        mOwnerName = (TextView) rootVIew.findViewById(R.id.owner_name);
        mLikeImgInitial = (ImageView) rootVIew.findViewById(R.id.ic_heart_initial);
        mLikeImgFinal = (ImageView) rootVIew.findViewById(R.id.ic_heart_final);
        mLikeCount = (TextView) rootVIew.findViewById(R.id.like_count);
        mCommentCount = (TextView) rootVIew.findViewById(R.id.comment_count);

        //set click listeners
        mLikeImgInitial.setOnClickListener(this);
        mLikeImgFinal.setOnClickListener(this);

    }

    private void loadIntiialData() {
        if (getArguments() != null && getArguments().getParcelable("post_detail") != null) {
            PostDetailBean bean = getArguments().getParcelable("post_detail");
            if(bean.photoUrl.contains("http:") || bean.photoUrl.contains("https:")){
                Picasso.with(getActivity()).load(bean.photoUrl).resize(600, 600).centerCrop().into(mDetailImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgresbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        mProgresbar.setVisibility(View.GONE);
                    }
                });
            }else{
                Picasso.with(getActivity()).load("file://" + bean.photoUrl).resize(600, 600).centerCrop().into(mDetailImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgresbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        mProgresbar.setVisibility(View.GONE);
                    }
                });
            }


            //set owner info
            mOwnerName.setText(bean.ownerName);
            mLikeCount.setText(String.valueOf(bean.likeCount));
            mCommentCount.setText(String.valueOf(bean.commentCount));
            // check post is liked or not
            if (bean.isLiked) {
                //show final image
                mLikeImgFinal.setVisibility(View.VISIBLE);
                mLikeImgInitial.setVisibility(View.GONE);
            } else {
                //show initial image
                mLikeImgInitial.setVisibility(View.VISIBLE);
                mLikeImgFinal.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_heart_initial:
                mLikeImgInitial.setClickable(false);
                CustomAnimationDrawableNew cad = new CustomAnimationDrawableNew((AnimationDrawable) ContextCompat.getDrawable(getActivity(),
                        R.drawable.animation_list_layout)) {
                    @Override
                    public void onAnimationFinish() {
                        mLikeImgInitial.setVisibility(View.GONE);
                        mLikeImgFinal.setVisibility(View.VISIBLE);
                        mLikeImgFinal.setClickable(true);
//                            updateLikesCounter(1,true);
                    }
                };
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mLikeImgInitial.setBackground(cad);
                } else {
                    mLikeImgInitial.setBackgroundDrawable(cad);
                }
                cad.start();
                break;
            case R.id.ic_heart_final:
                mLikeImgInitial.setVisibility(View.VISIBLE);
                mLikeImgInitial.setClickable(true);
                mLikeImgFinal.setVisibility(View.GONE);
                mLikeImgFinal.setClickable(false);
                mLikeImgInitial.setBackgroundResource(R.drawable.animation_list_layout);
                break;
        }
    }
}
