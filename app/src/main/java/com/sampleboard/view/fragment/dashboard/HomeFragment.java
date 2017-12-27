package com.sampleboard.view.fragment.dashboard;

import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.sampleboard.GlobalActivity;
import com.sampleboard.R;
import com.sampleboard.adapter.HomeListAdapter;
import com.sampleboard.bean.MediaItem;
import com.sampleboard.bean.MediaModel;
import com.sampleboard.bean.PhotosBean;
import com.sampleboard.bean.PostDetailBean;
import com.sampleboard.interfaces.MediaListInterface;
import com.sampleboard.utils.Constants;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.BaseFragment;
import com.sampleboard.view.activity.DashBoardActivity;
import com.sampleboard.view.activity.DetailActivityV2;
import com.sampleboard.view.activity.HolderActivity;
import com.sampleboard.viewmodel.HomeFragmentViewModel;

import java.util.List;

/**
 * @author Anuj Sharma on 2/28/2017.
 */

public class HomeFragment extends BaseFragment implements MediaListInterface {
    private View rootView;
    private HomeFragmentViewModel viewModel;
    private HomeListAdapter mAdapter;
    private List<PhotosBean> list;
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
                Intent intent = new Intent(getActivity(), HolderActivity.class);
                intent.putExtra(Constants.DESTINATION, Constants.SEARCH_SCREEN);
                getActivity().startActivity(intent);
                break;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
        Toolbar mToolbar = rootView.findViewById(R.id.toolbar);
        ((DashBoardActivity) getActivity()).setSupportActionBar(mToolbar);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setTitle("");
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher));

        categoryType = rootView.findViewById(R.id.category_type);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.recycler_items);
        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sm);
//        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        mAdapter = new HomeListAdapter(getActivity(), null, this);
        mRecyclerView.setAdapter(mAdapter);
        //Static Data coming from Json stored in assets folder
        try {
            Gson gson = new Gson();
            MediaModel mediaModel = gson.fromJson(getStringFromLocalJson("media_list.json", getActivity()), MediaModel.class);

            mAdapter.updateList(mediaModel.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onItemClick(MediaItem obj, ImageView imageView, int position) {
        PostDetailBean detailBean = new PostDetailBean();
        detailBean.photoName = obj.getTitle();
        detailBean.photoUrl = obj.getMedia();
        detailBean.likeCount = 514;
        detailBean.commentCount = 356;
        detailBean.isLiked = true;
        detailBean.ownerName = "Anuj Sharma";
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.OBJ_DETAIL, detailBean);

        //Move to Detail Activity
        Intent intent = new Intent(getActivity(), DetailActivityV2.class);
        intent.putExtra(Constants.DESTINATION, Constants.DETAIL_SCREEN);
        intent.putExtra(Constants.OBJ_DETAIL, detailBean);
        intent.putExtra(Constants.POSITION, position);
        if (imageView != null && imageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                GlobalActivity.photoCache.put(position, bitmap);
            }
        }
        if (Utils.getInstance().isEqualLollipop()) {
            Pair<View, String> p1 = Pair.create(imageView, getString(R.string.transition_image));
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(getActivity(), p1);
            getActivity().startActivity(intent, options.toBundle());

//            ActivityOptionsCompat options = ActivityOptionsCompat.
//                    makeSceneTransitionAnimation(getActivity(), imageView, getString(R.string.transition_image));
//            getActivity().startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

}
