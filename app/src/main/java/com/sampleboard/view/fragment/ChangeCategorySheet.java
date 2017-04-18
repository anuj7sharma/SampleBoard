package com.sampleboard.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sampleboard.R;

/**
 * Created by Anuj Sharma on 4/18/2017.
 */

public class ChangeCategorySheet extends BottomSheetDialogFragment {

    private View rootView;
    private BottomSheetDialog mBottomSheetDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category_sheet,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mBottomSheetDialog = (BottomSheetDialog) dialog;
                View bottomSheetInternal = mBottomSheetDialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheetInternal).
                        setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }
}
