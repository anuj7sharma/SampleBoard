package com.sampleboard.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.sampleboard.MainActivity;
import com.sampleboard.R;
import com.sampleboard.enums.CurrentScreen;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.calander.CalanderFragment;
import com.sampleboard.view.digitalSignature.DigitalSignatureFragment;
import com.sampleboard.view.digitalSignature.PrintBitmapFragment;
import com.sampleboard.view.photosModule.PhotosListFragment;
import com.sampleboard.view.profile.ProfileActivity;
import com.sampleboard.view.profile.ProfileFragment;
import com.sampleboard.view.searchView.SearchFragment;

/**
 * Created by Anuj Sharma on 3/27/2017.
 */

public class DashBoardActivity extends MainActivity {

    private static DashBoardActivity instance;
    public static DashBoardActivity getInstance(){
        return instance;
    }


    private static CalanderFragment calanderFragment;

    private static CalanderFragment initializeCalanderFragment() {
        if (calanderFragment == null) calanderFragment = new CalanderFragment();
        return calanderFragment;
    }

    private static DigitalSignatureFragment digitalSignatureFragment;

    private static DigitalSignatureFragment initializeDigitalSignatureFragment() {
        if (digitalSignatureFragment == null) digitalSignatureFragment = new DigitalSignatureFragment();
        return digitalSignatureFragment;
    }

    private static PrintBitmapFragment printBitmapFragment;

    private static PrintBitmapFragment initializePrintBitmapFragment() {
        if (printBitmapFragment == null) printBitmapFragment = new PrintBitmapFragment();
        return printBitmapFragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        instance = DashBoardActivity.this;


        changeScreen(R.id.dashboard_container,CurrentScreen.ITEM_LIST_SCREEN, false, false, null);

    }

}
