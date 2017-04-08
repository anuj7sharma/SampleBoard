package com.sampleboard.view.profile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sampleboard.R;
import com.sampleboard.enums.CurrentScreen;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.BaseFragment;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anuj Sharma on 4/6/2017.
 */

public class EditProfileFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private Toolbar mToolbar;
    private EditText etFirstName,etLastName,etEmail;
    private FloatingActionButton btnEditProfile;
    private CircleImageView mCircleImageView;
    private Button btnSaveProfile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVIews();
    }

    private void initVIews() {
        mToolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        ((ProfileActivity)getActivity()).setSupportActionBar(mToolbar);
        ((ProfileActivity)getActivity()).getSupportActionBar().setTitle("Edit Profile");
        mToolbar.setNavigationIcon(R.drawable.ic_navigation_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.getInstance().oneStepBack();
            }
        });
        mCircleImageView = (CircleImageView)rootView.findViewById(R.id.profile_image);
        etFirstName = (EditText)rootView.findViewById(R.id.et_firstname);
        etLastName = (EditText)rootView.findViewById(R.id.et_lastname);
        etEmail = (EditText)rootView.findViewById(R.id.et_email);
        btnEditProfile = (FloatingActionButton)rootView.findViewById(R.id.btn_edit_profile);
        btnSaveProfile = (Button)rootView.findViewById(R.id.btn_saveProfile);

        btnEditProfile.setOnClickListener(this);
        btnSaveProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_profile:
                btnEditProfile.hide();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnEditProfile.show();
                        Intent profilePicIntent = new Intent(getActivity(),ProfileActivity.class);
                        profilePicIntent.putExtra("destination","profile_pic");
                        if (Utils.getInstance().isEqualLollipop()) {
                            Pair<View, String> p1 = Pair.create((View) mCircleImageView, "profile_pic");
                            ActivityOptions options =
                                    ActivityOptions.makeSceneTransitionAnimation(getActivity(), p1);
                            getActivity().startActivity(profilePicIntent, options.toBundle());
                        } else {
                            getActivity().startActivity(profilePicIntent);
                        }
                    }
                },500);

//                Bundle bundle = new Bundle();
//                bundle.putString("profile_pic","https://organicthemes.com/demo/profile/files/2012/12/profile_img.png");
//                ProfileActivity.getInstance().changeScreen(R.id.profile_container, CurrentScreen.PROFILE_PIC_SCREEN,true,true,bundle);
                break;
            case R.id.btn_saveProfile:
                Utils.getInstance().showToast("Save clicked");
                break;
        }
    }
}
