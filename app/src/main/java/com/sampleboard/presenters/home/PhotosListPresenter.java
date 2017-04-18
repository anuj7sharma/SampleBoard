package com.sampleboard.presenters.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.sampleboard.R;
import com.sampleboard.adapter.PhotosListAdapter;
import com.sampleboard.bean.PhotosBean;
import com.sampleboard.utils.Utils;
import com.sampleboard.interfaces.GenericAdapterInterface;
import com.sampleboard.view.detail.DetailActivity;
import com.sampleboard.view.fragment.PhotosListFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mobilyte India Pvt Ltd on 3/1/2017.
 */

public class PhotosListPresenter implements View.OnClickListener, GenericAdapterInterface<PhotosListAdapter> {
    private WeakReference<PhotosListView> photosView;
    private PhotosListFragment mFragment;
//    private FragmentItemsBinding binder;
    private // Add Sample Data
            List<PhotosBean> list =new ArrayList<>();

    //list type i.e grid=1 or list=0
    private int gridType = 0;
    private StaggeredGridLayoutManager sm;
    private PhotosListAdapter mAdapter;

    public PhotosListPresenter(PhotosListView itemView, PhotosListFragment fragment) {
        this.photosView = new WeakReference<>(itemView);
        this.mFragment = fragment;
        init();
    }

    private void init() {
        if(photosView!=null && photosView.get()!=null){
            // initialize RecyclerView
            sm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            photosView.get().getRecyclerView().setLayoutManager(sm);

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

            //add adapter
            mAdapter = new PhotosListAdapter(mFragment.getActivity(), list, this, mFragment);
            photosView.get().getRecyclerView().setAdapter(initAdapter());
        }

    }

    public void onItemClick(PhotosBean photosBean, View view) {
        //Move to Detail Activity
        ImageView img = (ImageView) view;
        Intent intent = new Intent(mFragment.getActivity(), DetailActivity.class);
        intent.putExtra("image_detail",photosBean);
        if (Utils.getInstance().isEqualLollipop()) {
            Pair<View, String> p1 = Pair.create((View) img, "photo_detail");
//            Pair<View, String> p2 = Pair.create((View)priceView, "price");
//            Pair<View, String> p3 = Pair.create(null, "content");
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(mFragment.getActivity(), p1);
            mFragment.getActivity().startActivity(intent, options.toBundle());
        } else {
            mFragment.getActivity().startActivity(intent);
        }
    }

    public void updateUI(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_list_type:
                if(photosView.get()!=null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(photosView.get().getRecyclerView(), new ChangeBounds());
                    }
                    if (gridType == 0) {
                        //change it to grid
                        sm.setSpanCount(2);
                        photosView.get().getListTypeBtn().setImageResource(R.drawable.ic_list);
                        gridType = 1;
                    } else if (gridType == 1) {
                        //change it to list
                        sm.setSpanCount(1);
                        photosView.get().getListTypeBtn().setImageResource(R.drawable.ic_grid);
                        gridType = 0;
                    }
                }
                break;
            case R.id.category_type:
                //show bottomsheet dialog
                Utils.getInstance().showToast("open bottomsheet");
                break;
        }
    }

    @Override
    public PhotosListAdapter initAdapter() {
        mAdapter = new PhotosListAdapter(mFragment.getActivity(), list, this, mFragment);
        return mAdapter;
    }
}
