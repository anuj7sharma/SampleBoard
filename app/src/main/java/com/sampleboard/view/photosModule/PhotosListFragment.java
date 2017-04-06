package com.sampleboard.view.photosModule;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sampleboard.R;
import com.sampleboard.databinding.FragmentItemsBinding;
import com.sampleboard.enums.CurrentScreen;
import com.sampleboard.presenters.PhotosListPresenter;
import com.sampleboard.view.DashBoardActivity;

/**
 * Created by Mobilyte India Pvt Ltd on 2/28/2017.
 */

public class PhotosListFragment extends Fragment implements ItemView {
    FragmentItemsBinding binder;
    private PhotosListPresenter presenter;

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_dashboard,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
//                DashBoardActivity.getInstance().changeScreen(CurrentScreen.SEARCH_SCREEN,true,true,null);
                break;
            case R.id.action_profile:
                DashBoardActivity.getInstance().changeScreen(CurrentScreen.PROFILE_SCREEN,false,true,null);
                break;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_items,container,false);
        ((DashBoardActivity)getActivity()).setSupportActionBar(binder.toolbar);
        ((DashBoardActivity)getActivity()).getSupportActionBar().setTitle("SampleApp");
        presenter = new PhotosListPresenter(this,binder,PhotosListFragment.this);

        binder.btnListType.setOnClickListener(presenter);

        return binder.getRoot();
    }


    @Override
    public void backPress() {

    }

  }
