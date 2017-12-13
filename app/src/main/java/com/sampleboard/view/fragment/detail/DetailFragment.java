package com.sampleboard.view.fragment.detail;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sampleboard.R;
import com.sampleboard.adapter.LikedAdapter;
import com.sampleboard.bean.LikedBean;
import com.sampleboard.bean.PostDetailBean;
import com.sampleboard.enums.CurrentScreen;
import com.sampleboard.permission.PermissionsAndroid;
import com.sampleboard.utils.Constants;
import com.sampleboard.utils.CustomAnimationDrawableNew;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.BaseFragment;
import com.sampleboard.view.activity.DetailActivityV2;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Anuj Sharma on 4/10/2017.
 */

public class DetailFragment extends BaseFragment implements View.OnClickListener {
    private View rootVIew;

    private ImageView mDetailImage, mLikeImgInitial, mLikeImgFinal;
    private ProgressBar mProgresbar;
    private TextView mOwnerName, mLikeCount, mCommentCount, mTags, mDesc, mReadMore;
    private PostDetailBean bean;

    private RecyclerView relatedRecycler;
    private LikedAdapter mAdapter;


    private String tagString = "#office #nature #wild #beauty";
    private String descString = "testing testing testing testing testing testing testing testing testing testing testing testing testing" +
            "testing testing testing";

//    private AnimationDrawable frameAnimation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootVIew = inflater.inflate(R.layout.fragment_post_detail, container, false);
        subscribeDownloadReceiver();
        return rootVIew;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        loadIntiialData();
    }

    @Override
    public void onDestroyView() {
        unSubscribeDownloadReceiver();
        super.onDestroyView();
    }

    private void initViews() {
        Toolbar mToolbar = rootVIew.findViewById(R.id.toolbar);
        if (getActivity() instanceof DetailActivityV2) {
            ((DetailActivityV2) getActivity()).setSupportActionBar(mToolbar);
            ((DetailActivityV2) getActivity()).getSupportActionBar().setTitle("Detail");
            mToolbar.setNavigationIcon(R.drawable.ic_navigation_back);
            mToolbar.setNavigationOnClickListener(v -> ((DetailActivityV2) getActivity()).oneStepBack());
        }

        mDetailImage = rootVIew.findViewById(R.id.post_detail_img);
        mProgresbar = rootVIew.findViewById(R.id.progress_bar);
        CircleImageView mOwnerImg = rootVIew.findViewById(R.id.owner_img);
        mOwnerName = rootVIew.findViewById(R.id.owner_name);
        mLikeImgInitial = rootVIew.findViewById(R.id.ic_heart_initial);
        mLikeImgFinal = rootVIew.findViewById(R.id.ic_heart_final);
        mLikeCount = rootVIew.findViewById(R.id.like_count);
        mCommentCount = rootVIew.findViewById(R.id.comment_count);
        mTags = rootVIew.findViewById(R.id.tags);
        mDesc = rootVIew.findViewById(R.id.description);
        mReadMore = rootVIew.findViewById(R.id.readmore);
        LinearLayout ownerProfileView = rootVIew.findViewById(R.id.view_owner_info);

        relatedRecycler = rootVIew.findViewById(R.id.related_recycler);
        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        relatedRecycler.setLayoutManager(sm);

        mAdapter = new LikedAdapter(getActivity(), null, DetailFragment.this);
        relatedRecycler.setAdapter(mAdapter);

        loadDummyRelatedData();

        //set click listeners
        mLikeImgInitial.setOnClickListener(this);
        mLikeImgFinal.setOnClickListener(this);
        mReadMore.setOnClickListener(this);
        ownerProfileView.setOnClickListener(this);
        rootVIew.findViewById(R.id.btn_download).setOnClickListener(this);

    }

    private void loadDummyRelatedData() {
        List<LikedBean> likeList = new ArrayList<>();
        LikedBean obj = new LikedBean();
        obj.imageUrl = "https://i.ytimg.com/vi/x30YOmfeVTE/maxresdefault.jpg";

        likeList.add(obj);

        obj = new LikedBean();
        obj.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/36/Hopetoun_falls.jpg";

        likeList.add(obj);

        obj = new LikedBean();
        obj.imageUrl = "https://static.pexels.com/photos/33109/fall-autumn-red-season.jpg";

        likeList.add(obj);

        obj = new LikedBean();
        obj.imageUrl = "https://cdn.pixabay.com/photo/2014/10/15/15/14/man-489744_960_720.jpg";

        likeList.add(obj);

        obj = new LikedBean();
        obj.imageUrl = "https://static.pexels.com/photos/39811/pexels-photo-39811.jpeg";

        likeList.add(obj);
        mAdapter.updateData(likeList);
    }

    private void loadIntiialData() {
        if (getArguments() != null && getArguments().getParcelable(Constants.OBJ_DETAIL) != null) {
            bean = getArguments().getParcelable(Constants.OBJ_DETAIL);
            assert bean != null;
            if (bean.photoUrl.contains("http:") || bean.photoUrl.startsWith("http")) {
                Picasso.with(getActivity()).load(bean.photoUrl).resize(600, 600).centerCrop().into(mDetailImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgresbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        mProgresbar.setVisibility(View.GONE);
                    }
                });
            } else {
                Picasso.with(getActivity()).load("file://" + bean.photoUrl).resize(600, 600).centerCrop().into(mDetailImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgresbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        mProgresbar.setVisibility(View.GONE);
                    }
                });
            }


            //set owner info
            mOwnerName.setText(bean.ownerName);
            mLikeCount.setText(String.valueOf(bean.likeCount));
            mCommentCount.setText(String.valueOf(bean.commentCount));
            // check post is liked or not
            if (bean.isLiked) {
                //show final image
                mLikeImgFinal.setVisibility(View.VISIBLE);
                mLikeImgInitial.setVisibility(View.GONE);
            } else {
                //show initial image
                mLikeImgInitial.setVisibility(View.VISIBLE);
                mLikeImgFinal.setVisibility(View.GONE);
            }

            //set tags
            mTags.setText(tagString);

            //set descString
            if (descString.length() > 100) {
                mDesc.setText(descString.substring(0, 100));
                mReadMore.setVisibility(View.VISIBLE);
            } else {
                mDesc.setText(descString);
                mReadMore.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_heart_initial:
                mLikeImgInitial.setClickable(false);
                CustomAnimationDrawableNew cad = new CustomAnimationDrawableNew((AnimationDrawable) ContextCompat.getDrawable(getActivity(),
                        R.drawable.animation_list_layout)) {
                    @Override
                    public void onAnimationFinish() {
                        mLikeImgInitial.setVisibility(View.GONE);
                        mLikeImgFinal.setVisibility(View.VISIBLE);
                        mLikeImgFinal.setClickable(true);
//                            updateLikesCounter(1,true);
                    }
                };
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mLikeImgInitial.setBackground(cad);
                } else {
                    mLikeImgInitial.setBackgroundDrawable(cad);
                }
                cad.start();
                break;
            case R.id.ic_heart_final:
                mLikeImgInitial.setVisibility(View.VISIBLE);
                mLikeImgInitial.setClickable(true);
                mLikeImgFinal.setVisibility(View.GONE);
                mLikeImgFinal.setClickable(false);
                mLikeImgInitial.setBackgroundResource(R.drawable.animation_list_layout);
                break;
            case R.id.readmore:
                showMoreDescription();
                break;
            case R.id.view_owner_info:
                if (getActivity() instanceof DetailActivityV2) {
                    ((DetailActivityV2) getActivity()).changeScreen(R.id.container_holder, CurrentScreen.USER_PROFILE_SCREEN, true, true, null);
                }
                break;
            case R.id.btn_download:
                if (bean != null) {
                    downloadFile(bean.photoUrl, bean.photoName);
                }
                break;
        }
    }

    private void showMoreDescription() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition((ViewGroup) rootVIew);
        }
        if (mReadMore.getText().equals(getString(R.string.show_more))) {
            mDesc.setText(descString.substring(0, descString.length()));
            mReadMore.setText(getString(R.string.show_less));
        } else if (mReadMore.getText().equals(getString(R.string.show_less))) {
            mDesc.setText(descString.substring(0, 100));
            mReadMore.setText(getString(R.string.show_more));
        }
    }

    /**
     * Download File Concept implemented here
     */
    private DownloadManager dm;
    private long enqueue;

    public void downloadFile(String url, String photoName) {
        if (!PermissionsAndroid.getInstance().checkWriteExternalStoragePermission(getActivity())) {
            PermissionsAndroid.getInstance().requestForWriteExternalStoragePermission(this);
            return;
        }
        //download music functionality here
//        songsProgressMap.put(musicBean.getUrl(),progressBar);
//        progressBar.setVisibility(View.VISIBLE);

        dm = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.setTitle(photoName);
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        File file = Utils.getInstance().saveToDirectory(getActivity(), photoName, "jpg");
        if (file == null)
            return;

        request.setDestinationUri(Uri.parse("file://" + "/" + file.getAbsolutePath()));
//        if (file != null && file.exists()) file.delete();
        enqueue = dm.enqueue(request);

    }

    public void subscribeDownloadReceiver() {
        getActivity().registerReceiver(downloadCompleteReceiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void unSubscribeDownloadReceiver() {
        try {
            if (downloadCompleteReceiver != null)
                getActivity().unregisterReceiver(downloadCompleteReceiver);
        } catch (IllegalStateException illegal) {

        }

    }

    BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enqueue);
                Cursor c = dm.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {
//                        if(songsProgressMap!=null && songsProgressMap.size()>0){
//                            ProgressBar progress = songsProgressMap.get(intent.getStringExtra("song_url"));
//                            if(progress!=null)progress.setVisibility(View.GONE);
//                        }
                        Utils.getInstance().showToast("Download complete");
                    }
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsAndroid.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (bean != null) {
                    downloadFile(bean.photoUrl, bean.photoName);
                }
            } else {
                // permission denied, boo! Disable the
                Utils.getInstance().showToast("Permission denied");
            }
        }

    }
}
