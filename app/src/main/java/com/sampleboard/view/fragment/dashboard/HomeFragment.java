package com.sampleboard.view.fragment.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sampleboard.R;
import com.sampleboard.enums.CurrentScreen;
import com.sampleboard.presenters.home.PhotosListPresenter;
import com.sampleboard.presenters.home.PhotosListView;
import com.sampleboard.view.activity.DashBoardActivity;

/**
 * @author Anuj Sharma on 2/28/2017.
 */

public class HomeFragment extends Fragment implements PhotosListView {

    private View rootView;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    //    private ImageView btnListType;
    private RelativeLayout categoryType;

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_search_icon).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_icon:
                if (getActivity() instanceof DashBoardActivity) {
                    ((DashBoardActivity) getActivity()).changeScreen(R.id.dashboard_container, CurrentScreen.SEARCH_SCREEN, false, true, null);
                }
                break;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mToolbar = rootView.findViewById(R.id.toolbar);
        ((DashBoardActivity) getActivity()).setSupportActionBar(mToolbar);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setTitle("");
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher));

        categoryType = rootView.findViewById(R.id.category_type);
        mRecyclerView = rootView.findViewById(R.id.recycler_items);
//        btnListType = rootView.findViewById(R.id.btn_list_type);

        PhotosListPresenter presenter = new PhotosListPresenter(this, HomeFragment.this);

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

//    @Override
//    public ImageView getListTypeBtn() {
//        return btnListType;
//    }

}
