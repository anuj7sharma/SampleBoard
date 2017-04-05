package com.androidpay.view.searchView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.androidpay.R;
import com.androidpay.utils.AnimateSearchMenu;
import com.androidpay.utils.Utils;
import com.androidpay.view.BaseFragment;
import com.androidpay.view.DashBoardActivity;
import com.github.jorgecastilloprz.FABProgressCircle;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by Anuj Sharma on 4/3/2017.
 */

public class SearchFragment extends BaseFragment {
    View rootView;
    private Toolbar mToolbar;
    private MenuItem mSearchItem;

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search,menu);
        mSearchItem = menu.findItem(R.id.action_search);

        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Called when SearchView is collapsing
                if (mSearchItem.isActionViewExpanded()) {
                    AnimateSearchMenu.getInstance().animateSearchToolbar(mToolbar,getActivity(),1, false, false);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Called when SearchView is expanding
                AnimateSearchMenu.getInstance().animateSearchToolbar(mToolbar,getActivity(),1, true, true);
                return true;
            }
        });

        //get search query
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        //search query listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //text has changed apply filter
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //perform the final search

                if (!TextUtils.isEmpty(newText) && newText.length() > 0) {
                    Utils.d("SearchModule","Entered value-> " + newText);
//                    Utils.getInstance().showToast(newText);
//                    tagsHandler.sendMessageDelayed(tagsHandler.obtainMessage(QUERY_CHANGED),DEFAULT_DELAY);
                }
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search,container,false);
        if(Utils.getInstance().isEqualLollipop())
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.quantum_grey_600));
        mToolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        ((DashBoardActivity)getActivity()).setSupportActionBar(mToolbar);
        ((DashBoardActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_navigation_back);

        //initialize fab progress circle
        final FABProgressCircle fabProgress = (FABProgressCircle)rootView.findViewById(R.id.fabProgressCircle);

        fabProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabProgress.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fabProgress.onCompleteFABAnimationEnd();
                        fabProgress.onCompleteFABAnimationEnd();
                        fabProgress.onArcAnimationComplete();
                    }
                },3000);
            }
        });
        return rootView;
    }


}
