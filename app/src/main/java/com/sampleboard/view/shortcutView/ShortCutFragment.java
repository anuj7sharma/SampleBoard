package com.sampleboard.view.shortcutView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sampleboard.R;

/**
 * Created by Anuj Sharma on 4/17/2017.
 */

public class ShortCutFragment extends AppCompatDialogFragment {

    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragmen_shortview,container,false);
        return rootView;
    }
}
