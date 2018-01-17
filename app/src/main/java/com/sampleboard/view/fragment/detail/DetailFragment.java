package com.sampleboard.view.fragment.detail;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.sampleboard.GlobalActivity;
import com.sampleboard.R;
import com.sampleboard.adapter.LikedAdapter;
import com.sampleboard.bean.api_response.TimelineObjResponse;
import com.sampleboard.databinding.FragmentPostDetailBinding;
import com.sampleboard.enums.CurrentScreen;
import com.sampleboard.permission.PermissionsAndroid;
import com.sampleboard.utils.Constants;
import com.sampleboard.utils.CustomAnimationDrawableNew;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.BaseFragment;
import com.sampleboard.view.activity.DetailActivityV2;
import com.sampleboard.viewmodel.DetailFragmentViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * @author Anuj Sharma on 4/10/2017.
 */

public class DetailFragment extends BaseFragment implements View.OnClickListener {
    private FragmentPostDetailBinding binding;
    private DetailFragmentViewModel viewModel;
    private TimelineObjResponse bean;
    private boolean isLikeClicked;
    /**
     * Download File Concept implemented here
     */
    private DownloadManager dm;
    private long enqueue;
    BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isVisible()) return;
            String action = intent.getAction();

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
//                long downloadId = intent.getLongExtra(
//                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enqueue);
                if (dm == null) return;

                Cursor c = dm.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {
                        Utils.getInstance().showToast(getString(R.string.message_download_complete));
                    }
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false);
        viewModel = ViewModelProviders.of(this).get(DetailFragmentViewModel.class);
        subscribeDownloadReceiver();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        if (getArguments() != null && getArguments().getParcelable(Constants.OBJ_DETAIL) != null &&
                getArguments().getParcelable(Constants.OBJ_DETAIL) instanceof TimelineObjResponse) {
            bean = getArguments().getParcelable(Constants.OBJ_DETAIL);
            final int position = getArguments().getInt(Constants.POSITION);
            Bitmap imageCoverBitmap = GlobalActivity.photoCache.get(position);

            inflateImage(imageCoverBitmap);
            updateDetailUI();
        }
        getPostDetail();
        subscribeObservers();
    }

    /**
     * Get Post Detail
     */
    private void getPostDetail() {
        if (Utils.isNetworkAvailable(getActivity())) {
            if (bean != null && viewModel != null) {
                if (TextUtils.isEmpty(Utils.getInstance().getUserId(getActivity()))) {
                    // user is not logged in
                    viewModel.getPostDetail("", bean.getId());
                } else {
                    // user is logged in
                    viewModel.getPostDetail(Utils.getInstance().getUserId(getActivity()), bean.getId());
                }
            } else {
                Utils.getInstance().showSnakBar(binding.getRoot(), "Post Id not found");
            }
        } else {
            Utils.getInstance().showSnakBar(binding.getRoot(), getString(R.string.error_internet));
        }
    }

    private void subscribeObservers() {
        if (viewModel != null) {
            viewModel.getMessage().observe(this, message -> {
                if (!TextUtils.isEmpty(message)) {
                    Utils.getInstance().showSnakBar(binding.getRoot(), message);
                }
            });

            viewModel.getGetPostResponse().observe(this, postResponse -> {
                if (postResponse != null) {
                    bean = postResponse;
                    updateDetailUI();
                }
            });

            viewModel.getUpdateLikeResponse().observe(this, updateLikeResponse -> {
                if (updateLikeResponse != null && updateLikeResponse.getCode() == 1) {
                    // API Hit Successfully
                } else {
                    //Something went wrong
                    if (isLikeClicked) {
                        //Need to set to gray icon again
                        binding.icHeartInitial.setVisibility(View.VISIBLE);
                        binding.icHeartFinal.setVisibility(View.GONE);
                    } else {
                        //Need to set to red icon again
                        binding.icHeartFinal.setVisibility(View.VISIBLE);
                        binding.icHeartInitial.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void inflateImage(Bitmap imageCoverBitmap) {
        //safety check to prevent nullPointer in the palette if the detailActivity was in the background for too long
            /*if (imageCoverBitmap == null || imageCoverBitmap.isRecycled()) {
                getActivity().finish();
                return;
            }*/
        if (imageCoverBitmap != null && !imageCoverBitmap.isRecycled()) {
            binding.postDetailImg.setImageBitmap(imageCoverBitmap);
            binding.progressBar.setVisibility(View.GONE);
            generatePallet(imageCoverBitmap);
        } else if (bean != null && !TextUtils.isEmpty(bean.getMedia())) {
            assert bean != null;
            String imageUrl;
            if (bean.getMedia().startsWith("http")) {
                imageUrl = bean.getMedia();
            } else {
                imageUrl = "file://" + bean.getMedia();
            }
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    binding.progressBar.setVisibility(View.GONE);
                    assert binding.postDetailImg != null;
                    generatePallet(bitmap);
                    if (bitmap != null)
                        binding.postDetailImg.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.postDetailImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_default_image));
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            // set the tag to the view
            binding.postDetailImg.setTag(target);
            Picasso.with(getActivity()).load(imageUrl)
                    .into(target);
        }

    }

    @Override
    public void onDestroyView() {
        unSubscribeDownloadReceiver();
        super.onDestroyView();
    }

    private void initViews() {
        if (getActivity() instanceof DetailActivityV2) {
            ((DetailActivityV2) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);
            ((DetailActivityV2) getActivity()).getSupportActionBar().setTitle("Detail");
            ((DetailActivityV2) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> ((DetailActivityV2) getActivity()).oneStepBack());
        }
        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.relatedRecycler.setLayoutManager(sm);
        LikedAdapter mAdapter = new LikedAdapter(getActivity(), null, DetailFragment.this);
        binding.relatedRecycler.setAdapter(mAdapter);
        /*try {
            Gson gson = new Gson();
            MediaModel mediaModel = gson.fromJson(getStringFromLocalJson("media_list.json", getActivity()), MediaModel.class);

            mAdapter.updateList(mediaModel.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

//        mAdapter.updateData(viewModel.loadDummyRelatedData());

        //set click listeners
        binding.icHeartInitial.setOnClickListener(this);
        binding.icHeartFinal.setOnClickListener(this);
        binding.commentContainer.setOnClickListener(this);
        binding.readmore.setOnClickListener(this);
        binding.viewOwnerInfo.setOnClickListener(this);
        binding.fabDownload.setOnClickListener(this);

    }

    private void updateDetailUI() {
        if (bean != null) {
            if (getActivity() instanceof DetailActivityV2) {
                ((DetailActivityV2) getActivity()).getSupportActionBar().setTitle(String.valueOf(bean.getTitle()));
            }
            binding.likeCount.setText(String.valueOf(bean.getLikeCount()));
            binding.commentCount.setText(String.valueOf(bean.getComment_count()));
            // check post is liked or not
            if (!TextUtils.isEmpty(bean.getIsLiked()) && bean.getIsLiked().equals("1")) {
                // post is liked by user
                //show final image
                binding.icHeartFinal.setVisibility(View.VISIBLE);
                binding.likeCount.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                binding.icHeartInitial.setVisibility(View.GONE);
            } else {
                // Post is not liked by user
                //show initial image
                binding.icHeartInitial.setVisibility(View.VISIBLE);
                binding.likeCount.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_textcolor));
                binding.icHeartFinal.setVisibility(View.GONE);
            }

            //set tags
            binding.tags.setText(bean.getTitle());

            //set descString
            if (!TextUtils.isEmpty(bean.getDescription())) {
                if (bean.getDescription().length() > 100) {
                    binding.description.setText(bean.getDescription().substring(0, 100));
                    binding.readmore.setVisibility(View.VISIBLE);
                } else {
                    binding.description.setText(bean.getDescription());
                    binding.readmore.setVisibility(View.GONE);
                }
            }

            //set owner info
            if (!TextUtils.isEmpty(bean.getUser_name()))
                binding.ownerName.setText(bean.getUser_name());
            else
                binding.ownerName.setText("N/A");
            if (!TextUtils.isEmpty(bean.getUser_profile())) {
                Picasso.with(getActivity()).load(Constants.BaseURL + bean.getUser_profile())
                        .resize(100, 100).centerCrop().into(binding.ownerImg, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        binding.ownerImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.def_profile_img));
                    }
                });
            }
        }

    }

    @SuppressLint("NewApi")
    private void generatePallet(Bitmap bitmap) {
        if (bitmap != null) {
            Palette.from(bitmap)
                    .generate(palette -> {
                        Palette.Swatch textSwatch = palette.getVibrantSwatch();
                        if (textSwatch == null) {
                            return;
                        }
                        int mDefaultBackgroundColor = 0;
                        Utils.animateViewColor(binding.appBar,
                                mDefaultBackgroundColor, textSwatch.getRgb());
                        Utils.animateViewColor(binding.includeToolbar.toolbar,
                                mDefaultBackgroundColor, textSwatch.getRgb());
                        Utils.animateBackgroundTintColor(binding.fabDownload,
                                mDefaultBackgroundColor, textSwatch.getRgb());
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_heart_initial:
                if (checkLike())
                    doLike();
                break;
            case R.id.ic_heart_final:
                if (checkLike())
                    doUnLike();
                break;
            case R.id.comment_container:
                //Move to comment screen
                if (getActivity() instanceof DetailActivityV2) {
                    if (bean != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.EXTRA_POST_ID, bean.getId());
                        CommentFragment commentFragment = new CommentFragment();
                        FragmentTransaction fts = getActivity().getSupportFragmentManager().beginTransaction();
                        commentFragment.setArguments(bundle);
                        fts.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
                        fts.add(R.id.container_detail, commentFragment, commentFragment.getClass().getSimpleName());
                        fts.addToBackStack(commentFragment.getClass().getSimpleName());
                        fts.commit();
                    }
                }
                break;
            case R.id.readmore:
                showMoreDescription();
                break;
            case R.id.view_owner_info:
                if (getActivity() instanceof DetailActivityV2) {
                    ((DetailActivityV2) getActivity()).changeScreen(R.id.container_detail, CurrentScreen.USER_PROFILE_SCREEN, true, true, null);
                }
                break;
            case R.id.fab_download:
                if (bean != null) {
                    if (!TextUtils.isEmpty(bean.getMedia()) && bean.getMedia().trim().startsWith("http"))
                        downloadFile(bean.getMedia(), bean.getTitle());
                    else
                        Utils.getInstance().showSnakBar(binding.getRoot(), getString(R.string.warning_media_valid_url)
                        );
                }
                break;
        }
    }

    /**
     * DO Like functionality and update UI
     */
    private void doLike() {
        isLikeClicked = true;
        binding.icHeartInitial.setClickable(false);
        CustomAnimationDrawableNew cad = new CustomAnimationDrawableNew((AnimationDrawable) ContextCompat.getDrawable(getActivity(),
                R.drawable.animation_list_layout)) {
            @Override
            public void onAnimationFinish() {
                binding.icHeartInitial.setVisibility(View.GONE);
                binding.icHeartFinal.setVisibility(View.VISIBLE);
                binding.icHeartFinal.setClickable(true);
            }

            @Override
            public void onAnimtionStart() {
                final LinearInterpolator interpolator = new LinearInterpolator();
                int updatedCount = viewModel.updateLikesCounter(bean.getLikeCount(),
                        true);
                bean.setLikeCount(updatedCount);
                binding.likeCount.animate()
                        .alpha(0)
                        .setDuration(100)
                        .setStartDelay(200)
                        .setInterpolator(interpolator)
                        .withEndAction(() -> {
                            binding.likeCount.animate()
                                    .alpha(1)
                                    .setDuration(100)
                                    .setInterpolator(interpolator);
                            binding.likeCount.setText(String.valueOf(updatedCount));
                            binding.likeCount.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                        });
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            binding.icHeartInitial.setBackground(cad);
        } else {
            binding.icHeartInitial.setBackgroundDrawable(cad);
        }
        cad.start();
    }

    /**
     * DO UnLike functionality and update UI
     */
    private void doUnLike() {
        isLikeClicked = false;
        binding.icHeartInitial.setVisibility(View.VISIBLE);
        binding.icHeartInitial.setClickable(true);
        binding.icHeartFinal.setVisibility(View.GONE);
        binding.icHeartFinal.setClickable(false);
        binding.icHeartInitial.setBackgroundResource(R.drawable.animation_list_layout);
        int updatedCount = viewModel.updateLikesCounter(bean.getLikeCount(),
                false);
        bean.setLikeCount(updatedCount);
        binding.likeCount.setText(String.valueOf(updatedCount));
        binding.likeCount.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_textcolor));
    }

    /**
     * Check conditions before hitting like or unlike button
     *
     * @return true if all conditions are valid
     */
    private boolean checkLike() {
        if (TextUtils.isEmpty(Utils.getInstance().getUserId(getActivity()))) {
            Utils.getInstance().showSnakBar(binding.getRoot(), "You need to do login first.");
            return false;
        }
        if (!Utils.isNetworkAvailable(getActivity())) {
            Utils.getInstance().showSnakBar(binding.getRoot(), getString(R.string.error_internet));
            return false;
        }
        if (bean != null && viewModel != null) {
            Map<String, String> param = new HashMap<>();
            param.put("user_id", Utils.getInstance().getUserId(getActivity()));
            param.put("post_id", String.valueOf(bean.getId()));
            viewModel.updateLike(param);

            return true;
        }
        return false;
    }

    private void showMoreDescription() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot());
        }
        try {
            if (bean != null && !TextUtils.isEmpty(bean.getDescription())) {
                if (binding.readmore.getText().equals(getString(R.string.show_more))) {
                    binding.description.setText(bean.getDescription().substring(0, bean.getDescription().length()));
                    binding.readmore.setText(getString(R.string.show_less));
                } else if (binding.readmore.getText().equals(getString(R.string.show_less))) {
                    binding.description.setText(bean.getDescription().substring(0, 100));
                    binding.readmore.setText(getString(R.string.show_more));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String url, String photoName) {
        if (!PermissionsAndroid.getInstance().checkWriteExternalStoragePermission(getActivity())) {
            PermissionsAndroid.getInstance().requestForWriteExternalStoragePermission(this);
            return;
        }

        dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
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
            illegal.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsAndroid.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (bean != null) {
                    downloadFile(bean.getMedia(), bean.getTitle());
                }
            } else {
                // permission denied, boo! Disable the
                Utils.getInstance().showToast("Permission denied");
            }
        }

    }
}
