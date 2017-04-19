package com.sampleboard.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sampleboard.R;
import com.sampleboard.presenters.home.PhotosListPresenter;
import com.sampleboard.presenters.home.PhotosListView;
import com.sampleboard.view.activity.DashBoardActivity;

/**
 * Created by Mobilyte India Pvt Ltd on 2/28/2017.
 */

public class PhotosListFragment extends Fragment implements PhotosListView {
    private PhotosListPresenter presenter;

    private View rootView;
    private Toolbar mToolbar;
    private  RecyclerView mRecyclerView;
    private ImageView btnListType;
    private RelativeLayout categoryType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_items,container,false);

        mToolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        ((DashBoardActivity)getActivity()).setSupportActionBar(mToolbar);
        ((DashBoardActivity)getActivity()).getSupportActionBar().setTitle("SampleApp");

        categoryType = (RelativeLayout)rootView.findViewById(R.id.category_type);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_items);
        btnListType = (ImageView)rootView.findViewById(R.id.btn_list_type);

        presenter = new PhotosListPresenter(this,PhotosListFragment.this);

        categoryType.setOnClickListener(presenter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public RelativeLayout getCategory() {
        return categoryType;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public ImageView getListTypeBtn() {
        return btnListType;
    }

}
