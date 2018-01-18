package com.sampleboard.view.fragment.dashboard;

import android.app.ActivityOptions;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sampleboard.GlobalActivity;
import com.sampleboard.R;
import com.sampleboard.adapter.HomeListAdapter;
import com.sampleboard.bean.api_response.TimelineObjResponse;
import com.sampleboard.databinding.FragmentHomeBinding;
import com.sampleboard.interfaces.TimelineInterface;
import com.sampleboard.utils.Constants;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.BaseFragment;
import com.sampleboard.view.activity.DashBoardActivity;
import com.sampleboard.view.activity.DetailActivityV2;
import com.sampleboard.view.activity.HolderActivity;
import com.sampleboard.viewmodel.HomeFragmentViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Anuj Sharma on 2/28/2017.
 */

public class HomeFragment extends BaseFragment implements TimelineInterface {
    private FragmentHomeBinding binding;
    private HomeFragmentViewModel viewModel;
    private StaggeredGridLayoutManager sm;
    private HomeListAdapter mAdapter;
    private List<TimelineObjResponse> timelineList;
    private int page = 1;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((DashBoardActivity) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setTitle("");
        binding.includeToolbar.toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher));

        sm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerItems.setLayoutManager(sm);
//        binding.recyclerItems.setItemAnimator();
        mAdapter = new HomeListAdapter(getActivity(), null, this,this);
        binding.recyclerItems.setAdapter(mAdapter);
        //Add recyclerview scroll listener
        binding.recyclerItems.addOnScrollListener(recyclerViewOnScrollListener);

        subscribeObservers();
        fetchTimeLine();
        //Static Data coming from Json stored in assets folder
        /*try {
            Gson gson = new Gson();
            MediaModel mediaModel = gson.fromJson(getStringFromLocalJson("media_list.json", getActivity()), MediaModel.class);
            mAdapter.updateList(mediaModel.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void fetchTimeLine() {
        if (Utils.isNetworkAvailable(getActivity()))
            viewModel.getTimeLine("11", page);
        else
            Utils.getInstance().showSnakBar(binding.getRoot(), getString(R.string.error_internet));
    }

    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            visibleItemCount = sm.getChildCount();
            totalItemCount = sm.getItemCount();
            int[] firstVisibleItems = null;
            firstVisibleItems = sm.findFirstVisibleItemPositions(firstVisibleItems);
            if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                pastVisibleItems = firstVisibleItems[0];
            }
            if (loading) {
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    loading = false;
                    Log.d("tag", "LOAD NEXT ITEM");
                    page++;
                    fetchTimeLine();
                }
            }
        }
    };

    private void subscribeObservers() {
        if (viewModel != null) {
            viewModel.getMessage().observe(this, s -> {
                if (!TextUtils.isEmpty(s))
                    Utils.getInstance().showSnakBar(binding.getRoot(), s);
            });

            viewModel.getTimelineResponse().observe(this, timelineResponse -> {
                if (timelineResponse != null && timelineResponse.getCode() == 1) {
                    //update Tineline List
                    if (page == 1 && timelineResponse.getData().size() == 0) {
                        //show Empty View
                        return;
                    }
                    if (timelineList == null) timelineList = new ArrayList<>();

                    if (page == 1 && timelineResponse.getData().size() > 0) {
                        timelineList = timelineResponse.getData();
                        mAdapter.updateList(timelineList);
                        mAdapter.notifyDataSetChanged();
                    } else if (page > 1 && timelineResponse.getData().size() > 0) {
                        int previousPos = timelineList.size() - 1;
                        timelineList.addAll(timelineResponse.getData());
                        mAdapter.updateList(timelineList);
                        mAdapter.notifyItemRangeInserted(previousPos, ((timelineList.size() - 1) - previousPos));
                        loading = true;
                    } else {
                        //No more data
                        Utils.getInstance().showSnakBar(binding.getRoot(), "No more data");
                    }
                }
            });

            viewModel.getUpdateLikeResponse().observe(this, updateLikeResponse -> {
                if (updateLikeResponse != null && updateLikeResponse.getCode() != 1) {
                    if (timelineList != null && timelineList.size() > position + 1) {
                        if (isLiked) {
                            // make it like previous not liked
                            timelineList.get(position).setIsLiked("0");
                        } else {
                            //make is like previous liked
                            timelineList.get(position).setIsLiked("1");
                        }
                        mAdapter.notifyItemChanged(position);
                    }
                }
            });
        }
    }

    @Override
    public void onItemClick(TimelineObjResponse obj, ImageView imageView, int position) {
        //Move to Detail Activity
        Intent intent = new Intent(getActivity(), DetailActivityV2.class);
        intent.putExtra(Constants.DESTINATION, Constants.DETAIL_SCREEN);
        intent.putExtra(Constants.OBJ_DETAIL, obj);
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
        } else {
            startActivity(intent);
        }
    }

    private boolean isLiked;
    private int position;
    private MutableLiveData<Boolean> isEverythingFine;

    public MutableLiveData<Boolean> getIsEverythingFine() {
        if(isEverythingFine==null)isEverythingFine = new MutableLiveData<>();
        return isEverythingFine;
    }

    @Override
    public void onLikeBtnClicked(TimelineObjResponse obj, ImageView imageView, int position, boolean isLiked) {
        if (TextUtils.isEmpty(Utils.getInstance().getUserId(getActivity()))) {
            Utils.getInstance().showSnakBar(binding.getRoot(), "You need to do login first.");
            return;
        }
        if (!Utils.isNetworkAvailable(getActivity())) {
            Utils.getInstance().showSnakBar(binding.getRoot(), getString(R.string.error_internet));
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put("user_id", Utils.getInstance().getUserId(getActivity()));
        param.put("post_id", String.valueOf(obj.getId()));
        this.isLiked = isLiked;
        this.position = position;
        if (viewModel != null) {
            viewModel.updateLike(param);
            isEverythingFine.postValue(true);
        }
    }

}
