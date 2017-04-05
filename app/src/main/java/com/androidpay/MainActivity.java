package com.androidpay;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidpay.utils.Utils;

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
}
