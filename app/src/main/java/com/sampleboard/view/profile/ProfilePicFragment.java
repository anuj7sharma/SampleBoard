package com.sampleboard.view.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sampleboard.R;
import com.sampleboard.view.BaseFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by anuj on 4/8/2017.
 */

public class ProfilePicFragment extends BaseFragment {
    private View rootView;
    private Toolbar mToolbar;
    private ImageView mProfileImage;
    private ProgressBar mProgressBar;
    private String profilePicUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile_pic,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        ProfileActivity.getInstance().setSupportActionBar(mToolbar);
        ProfileActivity.getInstance().getSupportActionBar().setTitle("Profile Pic");
        mToolbar.setNavigationIcon(R.drawable.ic_navigation_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.getInstance().oneStepBack();
            }
        });
        mProfileImage = (ImageView)rootView.findViewById(R.id.profile_pic);
        mProgressBar = (ProgressBar)rootView.findViewById(R.id.progress_bar);

        try {
            if(getArguments()!=null && getArguments().getString("profile_pic")!=null){
                profilePicUrl = getArguments().getString("profile_pic");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(!TextUtils.isEmpty(profilePicUrl)){
            mProgressBar.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(profilePicUrl).into(mProfileImage, new Callback() {
                @Override
                public void onSuccess() {
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }


    }
}
