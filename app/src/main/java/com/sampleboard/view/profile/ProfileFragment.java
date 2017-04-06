package com.sampleboard.view.profile;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
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
import com.sampleboard.enums.CurrentScreen;
import com.sampleboard.utils.AnimateSearchMenu;
import com.sampleboard.utils.SharedPreferencesHandler;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.BaseFragment;
import com.sampleboard.view.DashBoardActivity;
import com.sampleboard.view.SettingsActivity;
import com.sampleboard.view.otpLogin.LoginActivity;
import com.facebook.accountkit.AccountKit;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by Anuj Sharma on 4/5/2017.
 */

public class ProfileFragment extends BaseFragment {
    private View rootView;
    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
        inflater.inflate(R.menu.menu_profile, menu);
        mSearchItem = menu.findItem(R.id.action_search);

        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Called when SearchView is collapsing
                if (mSearchItem.isActionViewExpanded()) {
                    AnimateSearchMenu.getInstance().animateSearchToolbar(mToolbar, getActivity(), 2, false, false);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Called when SearchView is expanding
                AnimateSearchMenu.getInstance().animateSearchToolbar(mToolbar, getActivity(), 2, true, true);
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
                    Utils.d("SearchModule", "Entered value-> " + newText);
//                    Utils.getInstance().showToast(newText);
//                    tagsHandler.sendMessageDelayed(tagsHandler.obtainMessage(QUERY_CHANGED),DEFAULT_DELAY);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editProfile:
                ProfileActivity.getInstance().changeScreen(R.id.profile_container, CurrentScreen.EDIT_PROFILE_SCREEN, false,true, null);
                break;
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                Utils.getInstance().confirmDialog(getActivity(), "Do you want to Logout ?", "logout", new Utils.ConfirmDialogCallbackInterface() {
                    @Override
                    public void onYesClick(String tag) {
                        AccountKit.logOut();
                        SharedPreferencesHandler.clearAll(getActivity());
                        //Move to Login Screen
                        Intent logoutintent = new Intent(getActivity(), LoginActivity.class);
                        logoutintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logoutintent);
                    }

                    @Override
                    public void onNoClick(String tag) {

                    }
                });

                break;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((ProfileActivity) getActivity()).setSupportActionBar(mToolbar);
        ((ProfileActivity) getActivity()).setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_navigation_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardActivity.getInstance().oneStepBack();
            }
        });
//        ((DashBoardActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new LikedFragment(), "Liked");
        adapter.addFragment(new DownloadedFragment(), "Downloaded");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

