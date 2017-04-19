package com.sampleboard.view.fragment.profile;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sampleboard.R;
import com.sampleboard.adapter.LikedAdapter;
import com.sampleboard.bean.LikedBean;
import com.sampleboard.bean.PostDetailBean;
import com.sampleboard.enums.CurrentScreen;
import com.sampleboard.utils.CustomAnimationDrawableNew;
import com.sampleboard.view.BaseFragment;
import com.sampleboard.view.activity.ProfileActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
    private TextView mOwnerName, mLikeCount, mCommentCount, mTags, mDesc, mReadMore;
    private LinearLayout ownerProfileView;

    private RecyclerView relatedRecycler;


    private String tagString = "#office #nature #wild #beauty";
    private String descString = "testing testing testing testing testing testing testing testing testing testing testing testing testing" +
            "testing testing testing";

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
        mTags = (TextView) rootVIew.findViewById(R.id.tags);
        mDesc = (TextView) rootVIew.findViewById(R.id.description);
        mReadMore = (TextView) rootVIew.findViewById(R.id.readmore);
        ownerProfileView = (LinearLayout) rootVIew.findViewById(R.id.view_owner_info);

        relatedRecycler = (RecyclerView) rootVIew.findViewById(R.id.related_recycler);
        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        relatedRecycler.setLayoutManager(sm);

        loadDummyRelatedData();

        //set click listeners
        mLikeImgInitial.setOnClickListener(this);
        mLikeImgFinal.setOnClickListener(this);
        mReadMore.setOnClickListener(this);
        ownerProfileView.setOnClickListener(this);

    }

    private void loadDummyRelatedData() {
        List<LikedBean> likeList = new ArrayList<>();
        LikedBean obj = new LikedBean();
        obj.imageUrl = "https://i.ytimg.com/vi/x30YOmfeVTE/maxresdefault.jpg";

        likeList.add(obj);

        obj = new LikedBean();
        obj.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/36/Hopetoun_falls.jpg";

        likeList.add(obj);

        obj = new LikedBean();
        obj.imageUrl = "https://static.pexels.com/photos/33109/fall-autumn-red-season.jpg";

        likeList.add(obj);

        obj = new LikedBean();
        obj.imageUrl = "https://cdn.pixabay.com/photo/2014/10/15/15/14/man-489744_960_720.jpg";

        likeList.add(obj);

        obj = new LikedBean();
        obj.imageUrl = "https://static.pexels.com/photos/39811/pexels-photo-39811.jpeg";

        likeList.add(obj);
        LikedAdapter mAdapter = new LikedAdapter(getActivity(), likeList, PostDetailFragment.this);
        relatedRecycler.setAdapter(mAdapter);
    }

    private void loadIntiialData() {
        if (getArguments() != null && getArguments().getParcelable("post_detail") != null) {
            PostDetailBean bean = getArguments().getParcelable("post_detail");
            if (bean.photoUrl.contains("http:") || bean.photoUrl.contains("https:")) {
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
            } else {
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

            //set tags
            mTags.setText(tagString);

            //set descString
            if (descString.length() > 100) {
                mDesc.setText(descString.substring(0, 100));
                mReadMore.setVisibility(View.VISIBLE);
            } else {
                mDesc.setText(descString);
                mReadMore.setVisibility(View.GONE);
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
            case R.id.readmore:
                showMoreDescription();
                break;
            case R.id.view_owner_info:
                ((ProfileActivity) getActivity()).changeScreen(R.id.profile_container, CurrentScreen.USER_PROFILE_SCREEN, true, true, null);
                break;
        }
    }

    private void showMoreDescription() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition((ViewGroup) rootVIew);
        }
        if (mReadMore.getText().equals(getString(R.string.show_more))) {
            mDesc.setText(descString.substring(0, descString.length()));
            mReadMore.setText(getString(R.string.show_less));
        } else if (mReadMore.getText().equals(getString(R.string.show_less))) {
            mDesc.setText(descString.substring(0, 100));
            mReadMore.setText(getString(R.string.show_more));
        }
    }


    /**
     * Shared Element Transition
     */
}
