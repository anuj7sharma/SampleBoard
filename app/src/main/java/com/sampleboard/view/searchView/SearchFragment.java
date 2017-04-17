package com.sampleboard.view.searchView;

import android.app.SearchManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sampleboard.R;
import com.sampleboard.utils.AnimateSearchMenu;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.BaseFragment;
import com.sampleboard.view.DashBoardActivity;

import static android.content.Context.SEARCH_SERVICE;

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
