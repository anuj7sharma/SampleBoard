package com.sampleboard.presenters.home;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Mobilyte India Pvt Ltd on 3/1/2017.
 */

public interface PhotosListView {
    void showProgress();
    void hideProgress();

    RelativeLayout getCategory();
    RecyclerView getRecyclerView();
//    ImageView getListTypeBtn();
}
