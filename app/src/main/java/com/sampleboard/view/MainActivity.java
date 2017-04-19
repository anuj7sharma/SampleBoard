package com.sampleboard.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sampleboard.enums.CurrentScreen;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.digitalSignature.DigitalSignatureFragment;
import com.sampleboard.view.digitalSignature.PrintBitmapFragment;
import com.sampleboard.view.fragment.PhotosListFragment;
import com.sampleboard.view.fragment.profile.EditProfileFragment;
import com.sampleboard.view.fragment.profile.PostDetailFragment;
import com.sampleboard.view.fragment.profile.ProfileFragment;
import com.sampleboard.view.fragment.profile.ProfilePicFragment;
import com.sampleboard.view.fragment.SearchFragment;
import com.sampleboard.view.fragment.profile.UserProfileFragment;

public class MainActivity extends AppCompatActivity {
    public void navigateTo(int container, Fragment fragment, boolean isBackStack) {
        try {
            FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
            fts.replace(container, fragment, fragment.getClass().getSimpleName());
            if (isBackStack)
                fts.addToBackStack(fragment.getClass().getSimpleName());
            fts.commit();

        } catch (Exception e) {
            Utils.e(e);
        }

    }

    public void navigateToWithBundle(int container,Fragment fragment, boolean isBackStack, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        fts.replace(container, fragment, fragment.getClass().getSimpleName());
        if(isBackStack)
            fts.addToBackStack(fragment.getClass().getSimpleName());
        fts.commit();
    }

    public void navigateReplacingCurrentWithBundle(int container,Fragment currentFragment, Fragment fragmentToNavigate, Bundle bundle) {
        fragmentToNavigate.setArguments(bundle);
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack();
        fts.replace(container, fragmentToNavigate);
        fts.addToBackStack(fragmentToNavigate.getClass().getSimpleName());
        fts.remove(currentFragment).commit();
    }

    public void addFragment(int container,Fragment fragment, boolean isBackStack) {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        fts.add(container, fragment, fragment.getClass().getSimpleName());
        if(isBackStack)
            fts.addToBackStack(fragment.getClass().getSimpleName());
        fts.commit();
    }

    public void addFragmentWithBundle(int container,Fragment fragment, boolean isBackStack, Bundle bundle) {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        fts.add(container, fragment, fragment.getClass().getSimpleName());
        if(isBackStack)
            fts.addToBackStack(fragment.getClass().getSimpleName());
        fts.commit();
    }

    public void oneStepBack() {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() >= 1) {
            fragmentManager.popBackStackImmediate();
            fts.commit();
        } else {
            supportFinishAfterTransition();
        }
    }


    public void changeScreen(int container, CurrentScreen currentScreen, boolean isAddFragment, boolean isBackStack, Bundle bundle) {
        Fragment currentFragment = null;
        switch (currentScreen) {
            case ITEM_LIST_SCREEN:
                currentFragment = new PhotosListFragment();
                break;
            case SEARCH_SCREEN:
                currentFragment = new SearchFragment();
                break;
            case PROFILE_SCREEN:
                currentFragment = new ProfileFragment();
                break;
            case EDIT_PROFILE_SCREEN:
                currentFragment = new EditProfileFragment();
                break;
            case PROFILE_PIC_SCREEN:
                currentFragment = new ProfilePicFragment();
                break;
            case USER_PROFILE_SCREEN:
                currentFragment = new UserProfileFragment();
                break;
            case POST_DETAIL_SCREEN:
                currentFragment = new PostDetailFragment();
                currentFragment.setArguments(bundle);
                break;
            case CALANDER_SCREEN:
//                currentFragment = initializeCalanderFragment();
                break;
            case DIGITAL_SIGNATURE_SCREEN:
//                currentFragment = initializeDigitalSignatureFragment();
                currentFragment = new DigitalSignatureFragment();
                break;
            case PRINT_BITMAP_SCREEN:
//                currentFragment = initializePrintBitmapFragment();
                currentFragment = new PrintBitmapFragment();
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
                addFragmentWithBundle(container, currentFragment, true, bundle);
            else
                addFragmentWithBundle(container, currentFragment, false, bundle);
        } else {
            //Replace Fragment
            if (isBackStack)
                navigateToWithBundle(container, currentFragment, true, bundle);
            else
                navigateToWithBundle(container, currentFragment, false, bundle);
        }


    }
}
