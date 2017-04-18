package com.sampleboard.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sampleboard.R;
import com.sampleboard.view.BaseFragment;

/**
 * Created by Anuj Sharma on 4/3/2017.
 */

public class SearchFragment extends BaseFragment {
    View rootView;

    @Override
    public void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search,container,false);
        return rootView;
    }


}
