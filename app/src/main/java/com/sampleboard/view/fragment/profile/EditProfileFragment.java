package com.sampleboard.view.fragment.profile;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.sampleboard.R;
import com.sampleboard.bean.api_response.UserResponse;
import com.sampleboard.databinding.FragmentEditProfileBinding;
import com.sampleboard.utils.Constants;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.BaseFragment;
import com.sampleboard.view.activity.HolderActivity;
import com.sampleboard.view.activity.ProfileActivity;
import com.sampleboard.viewmodel.EditProfileViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * @author Anuj Sharma on 4/6/2017.
 */

public class EditProfileFragment extends BaseFragment implements View.OnClickListener {
    private FragmentEditProfileBinding binding;
    private EditProfileViewModel viewModel;
    private int gender = 1;
//    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        viewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVIews();
        if (Utils.isNetworkAvailable(getActivity())) {
            viewModel.getProfileInfo(11);
        } else {
            Utils.getInstance().showSnakBar(binding.getRoot(), getString(R.string.error_internet));
        }
    }

    private void initVIews() {
        if (binding != null) {
            if (getActivity() instanceof ProfileActivity) {
                ((ProfileActivity) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);
                ((ProfileActivity) getActivity()).getSupportActionBar().setTitle("Edit Profile");
            } else if (getActivity() instanceof HolderActivity) {
                ((HolderActivity) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);
                ((HolderActivity) getActivity()).getSupportActionBar().setTitle("Edit Profile");
            }

            binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_navigation_back);
            binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() instanceof ProfileActivity)
                        ((ProfileActivity) getActivity()).oneStepBack();
                    else if (getActivity() instanceof HolderActivity)
                        ((HolderActivity) getActivity()).oneStepBack();
                }
            });
            binding.profileImage.setOnClickListener(this);
            binding.btnEditProfilePic.setOnClickListener(this);
            binding.btnSaveProfile.setOnClickListener(this);

            binding.groupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radio_male:
                            gender = 1;
                            break;
                        case R.id.radio_female:
                            gender = 2;
                            break;
                    }
                }
            });

            subscribeObservers();

        }
    }

    private void subscribeObservers() {
        if (viewModel != null) {
            viewModel.getMessageObserver().observe(this, s -> {
                if (!TextUtils.isEmpty(s)) Utils.getInstance().showSnakBar(binding.getRoot(), s);
            });
            viewModel.getUserResponseObserver().observe(this, userResponse -> {
                if (userResponse != null) {
                    //Update UI accordingly
                    if (!TextUtils.isEmpty(userResponse.getProfile_avatar())) {
                        Picasso.with(getActivity()).load(Constants.API_BASE_URL + Constants.DEF_PROFILE_URL).into(binding.profileImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                binding.profileProgressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                binding.profileProgressBar.setVisibility(View.GONE);
                                binding.profileImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.def_profile_img));
                            }
                        });
                    } else {
                        binding.profileImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.def_profile_img));
                    }

                    switch (userResponse.getGender()) {
                        case "1":
                            gender = 1;
                            binding.radioMale.setSelected(true);
                            binding.radioFemale.setSelected(false);
                            break;
                        case "2":
                            gender = 2;
                            binding.radioFemale.setSelected(true);
                            binding.radioMale.setSelected(false);
                            break;
                    }

                    binding.setViewModel(userResponse);
                    binding.executePendingBindings();
                }
            });

            viewModel.getUpdateProfileObserver().observe(this, aBoolean -> {
                if (aBoolean) {
                    //Profile Updated
                } else {
                    //Error Occured
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image:
                moveToProfilePicFragment();
                break;
            case R.id.btn_edit_profile_pic:
                moveToProfilePicFragment();
                break;
            case R.id.btn_saveProfile:
                if (Utils.isNetworkAvailable(getActivity())) {
                    UserResponse userObj = new UserResponse();
                    userObj.setFirst_name(binding.etFirstname.getText().toString());
                    userObj.setLast_name(binding.etLastname.getText().toString());
                    userObj.setGender(String.valueOf(gender));
                    viewModel.validateProfile(userObj, 11);
                } else {
                    Utils.getInstance().showSnakBar(binding.getRoot(), getString(R.string.error_internet));
                }
                break;
        }
    }

    private void moveToProfilePicFragment() {
        /*btnEditProfile.hide();
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
        },500);*/

        Intent profilePicIntent = new Intent(getActivity(), ProfileActivity.class);
        profilePicIntent.putExtra("destination", "profile_pic");
        if (Utils.getInstance().isEqualLollipop()) {
            Pair<View, String> p1 = Pair.create((View) binding.profileImage, "profile_pic");
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(getActivity(), p1);
            getActivity().startActivity(profilePicIntent, options.toBundle());
        } else {
            getActivity().startActivity(profilePicIntent);
        }
    }
}
