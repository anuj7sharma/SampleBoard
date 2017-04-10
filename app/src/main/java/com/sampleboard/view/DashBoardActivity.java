package com.sampleboard.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sampleboard.MainActivity;
import com.sampleboard.R;
import com.sampleboard.enums.CurrentScreen;
import com.sampleboard.view.calander.CalanderFragment;
import com.sampleboard.view.digitalSignature.DigitalSignatureFragment;
import com.sampleboard.view.digitalSignature.PrintBitmapFragment;
import com.sampleboard.view.photosModule.PhotosListFragment;
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

    /*public void changeScreen(CurrentScreen currentScreen, boolean isAddFragment, boolean isBackStack, Bundle bundle) {
        Fragment currentFragment = null;
        switch (currentScreen) {
            case ITEM_LIST_SCREEN:
                currentFragment = new PhotosListFragment();
                break;
            case PROFILE_SCREEN:
                currentFragment = new ProfileFragment();
                break;
            case CALANDER_SCREEN:
                currentFragment = initializeCalanderFragment();
                break;
            case DIGITAL_SIGNATURE_SCREEN:
//                currentFragment = initializeDigitalSignatureFragment();
                currentFragment = new DigitalSignatureFragment();
                break;
            case PRINT_BITMAP_SCREEN:
//                currentFragment = initializePrintBitmapFragment();
                currentFragment = new PrintBitmapFragment();
                break;
            case SEARCH_SCREEN:
                currentFragment = new SearchFragment();
                break;

        }
        if (currentFragment == null) {
            return;
        }
        //hide Keyboard
//        Utils.getInstance().hideSoftKeyboard(this, roo);

        if (isAddFragment) {
            //Add Fragment
            if (isBackStack)
                addFragmentWithBundle(R.id.dashboard_container, currentFragment, true, bundle);
            else
                addFragmentWithBundle(R.id.dashboard_container, currentFragment, false, bundle);
        } else {
            //Replace Fragment
            if (isBackStack)
                navigateToWithBundle(R.id.dashboard_container, currentFragment, true, bundle);
            else
                navigateToWithBundle(R.id.dashboard_container, currentFragment, false, bundle);
        }


    }*/
}
