package com.sampleboard.view.fragment.dashboard;

import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.sampleboard.R;
import com.sampleboard.adapter.HomeListAdapter;
import com.sampleboard.bean.PhotosBean;
import com.sampleboard.bean.PostDetailBean;
import com.sampleboard.interfaces.MediaListInterface;
import com.sampleboard.utils.Constants;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.activity.DashBoardActivity;
import com.sampleboard.view.activity.DetailActivityV2;
import com.sampleboard.view.activity.HolderActivity;
import com.sampleboard.viewmodel.HomeFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anuj Sharma on 2/28/2017.
 */

public class HomeFragment extends Fragment implements MediaListInterface {
    private View rootView;
    private HomeFragmentViewModel viewModel;
    private HomeListAdapter mAdapter;
    private List<PhotosBean> list;
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
        mAdapter = new HomeListAdapter(getActivity(), null, this);
        mRecyclerView.setAdapter(mAdapter);
        addData();
        mAdapter.updateList(list);
//        btnListType = rootView.findViewById(R.id.btn_list_type);

//        PhotosListPresenter presenter = new PhotosListPresenter(this, HomeFragment.this);

//        categoryType.setOnClickListener(presenter);

        return rootView;
    }

    private void addData() {
        if (list == null) list = new ArrayList<>();
        PhotosBean obj;
        obj = new PhotosBean();
        obj.title = "Beautiful Nature view";
        obj.photoName = "Beautiful Nature view";
        obj.availableCount = 3;
        obj.photoUrl = "https://i.ytimg.com/vi/x30YOmfeVTE/maxresdefault.jpg";
        obj.price = "Rs 325";

        list.add(obj);

        obj = new PhotosBean();
        obj.title = "Water fall";
        obj.photoName = "Water fall Image";
        obj.availableCount = 1;
        obj.photoUrl = "https://upload.wikimedia.org/wikipedia/commons/3/36/Hopetoun_falls.jpg";
        obj.price = "Rs 325";

        list.add(obj);

        obj = new PhotosBean();
        obj.title = "Automn Season";
        obj.photoName = "Automn Season View";
        obj.availableCount = 1;
        obj.photoUrl = "https://static.pexels.com/photos/33109/fall-autumn-red-season.jpg";
        obj.price = "Rs 325";

        list.add(obj);

        obj = new PhotosBean();
        obj.title = "Little Strom coming";
        obj.photoName = "Strom coming";
        obj.availableCount = 2;
        obj.photoUrl = "https://cdn.pixabay.com/photo/2014/10/15/15/14/man-489744_960_720.jpg";
        obj.price = "Rs 525";

        list.add(obj);

        obj = new PhotosBean();
        obj.title = "Evening View";
        obj.photoName = "Evening View";
        obj.availableCount = 2;
        obj.photoUrl = "https://static.pexels.com/photos/39811/pexels-photo-39811.jpeg";
        obj.price = "Rs 1425";

        list.add(obj);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onItemClick(PhotosBean obj, ImageView imageView) {
        PostDetailBean detailBean = new PostDetailBean();
        detailBean.photoName = obj.photoName;
        detailBean.photoUrl = obj.photoUrl;
        detailBean.likeCount = 514;
        detailBean.commentCount = 356;
        detailBean.isLiked = true;
        detailBean.ownerName = "Anuj Sharma";
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.OBJ_DETAIL, detailBean);

//        if(mFragment.getActivity() instanceof DashBoardActivity){
//            ((DashBoardActivity)mFragment.getActivity()).performTransition(R.id.dashboard_container,new DetailFragment(),(ImageView)view,bundle);
//        }

        //Move to Detail Activity
        Intent intent = new Intent(getActivity(), DetailActivityV2.class);
        intent.putExtra(Constants.DESTINATION, Constants.DETAIL_SCREEN);
        intent.putExtra(Constants.OBJ_DETAIL, detailBean);
        if (Utils.getInstance().isEqualLollipop()) {
            Pair<View, String> p1 = Pair.create(imageView, imageView.getTransitionName());
//            Pair<View, String> p2 = Pair.create((View)priceView, "price");
//            Pair<View, String> p3 = Pair.create(null, "content");
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(getActivity(), p1);
            getActivity().startActivity(intent, options.toBundle());
        } else {
            getActivity().startActivity(intent);
        }
    }


//    @Override
//    public ImageView getListTypeBtn() {
//        return btnListType;
//    }

}
