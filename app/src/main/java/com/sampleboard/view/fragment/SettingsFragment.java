package com.sampleboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.facebook.accountkit.AccountKit;
import com.sampleboard.R;
import com.sampleboard.utils.SharedPreferencesHandler;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.BaseFragment;
import com.sampleboard.view.activity.DashBoardActivity;
import com.sampleboard.view.otpLogin.LoginActivity;
import com.sampleboard.view.activity.ProfileActivity;

/**
 * Created by Anuj Sharma on 4/17/2017.
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;

    private Toolbar mToolbar;
    private RelativeLayout btnEditProfile,containerFingerPrint, btnTerms, btnLogout;
    private Switch mTouchSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        ((DashBoardActivity)getActivity()).setSupportActionBar(mToolbar);
        ((DashBoardActivity)getActivity()).getSupportActionBar().setTitle("Settings");
        btnEditProfile = (RelativeLayout)rootView.findViewById(R.id.btn_editProfile);
        containerFingerPrint = (RelativeLayout)rootView.findViewById(R.id.finger_print_container);
        mTouchSwitch = (Switch) rootView.findViewById(R.id.btn_switch);
        btnTerms = (RelativeLayout)rootView.findViewById(R.id.btn_terms);
        btnLogout = (RelativeLayout)rootView.findViewById(R.id.btn_logout);

        btnEditProfile.setOnClickListener(this);
        btnTerms.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //First check FInger touch hardware is present or not
            boolean isFingerPrintAvailable = false;
            FingerprintManager fingerprintManager = (FingerprintManager)getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
            } else {
                // Everything is ready for fingerprint authentication
                isFingerPrintAvailable = true;
            }
            if(isFingerPrintAvailable){
                containerFingerPrint.setVisibility(View.VISIBLE);
            }else{
                containerFingerPrint.setVisibility(View.GONE);
            }
        }else{
            containerFingerPrint.setVisibility(View.GONE);
        }

        boolean isFingerTouchEnable = SharedPreferencesHandler.getBooleanValues(getActivity(), getString(R.string.pref_isFingertouchEnable));
        if(isFingerTouchEnable){
            // Enable Touch Switch
            mTouchSwitch.setChecked(true);
        }else{
            //Disable Touch Switch
            mTouchSwitch.setChecked(false);
        }

        mTouchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferencesHandler.setBooleanValues(getActivity(),getString(R.string.pref_isFingertouchEnable),true);
                }else{
                    SharedPreferencesHandler.setBooleanValues(getActivity(),getString(R.string.pref_isFingertouchEnable),false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_editProfile:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("destination","edit_profile");
                startActivity(intent);
                break;
            case R.id.btn_terms:
                break;
            case R.id.btn_logout:
                Utils.getInstance().confirmDialog(getActivity(), "Do you want to Logout ?", "logout", new Utils.ConfirmDialogCallbackInterface() {
                    @Override
                    public void onYesClick(String tag) {
                        AccountKit.logOut();
                        SharedPreferencesHandler.clearAll(getActivity());
                        //Move to Login Screen
                        Intent logoutintent = new Intent(getActivity(), LoginActivity.class);
                        logoutintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logoutintent);
                    }

                    @Override
                    public void onNoClick(String tag) {

                    }
                });

                break;
        }
    }
}
