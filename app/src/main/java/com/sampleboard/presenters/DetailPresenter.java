package com.sampleboard.presenters;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.view.View;

import com.sampleboard.R;
import com.sampleboard.bean.PhotosBean;
import com.sampleboard.databinding.ActivityDetailBinding;
import com.sampleboard.permission.PermissionsAndroid;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.detail.DetailActivity;
import com.sampleboard.view.detail.DetailView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * @author Anuj Sharma.
 */

public class DetailPresenter implements View.OnClickListener{

    private WeakReference<DetailView> detailView;

    private ActivityDetailBinding binder;
    private DetailActivity detailActivity;
    private WeakReference<Context> context;
    private PhotosBean photosBean;

    Intent serviceIntent;

    public DetailPresenter(DetailView detailView, ActivityDetailBinding binder,Context context,DetailActivity activity) {
        this.binder = binder;
        this.context = new WeakReference<>(context);
        detailActivity = activity;
        this.detailView = new WeakReference<>(detailView);
        onCreate();
    }

    private void onCreate() {
        subscribeDownloadReceiver();
        //initially download button hidden
        binder.btnDownload.hide();

        //get intent info and place into View
        detailActivity.setSupportActionBar(binder.toolbar);
        detailActivity.getSupportActionBar().setTitle("");
        binder.toolbar.setNavigationIcon(R.drawable.ic_navigation_back);
        binder.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(detailActivity.getIntent()!=null && detailActivity.getIntent().getParcelableExtra("image_detail")!=null){
            photosBean = detailActivity.getIntent().getParcelableExtra("image_detail");
        }

        if(context.get()!=null && photosBean!=null){
            // load image into main view
            Picasso.with(context.get()).load(photosBean.photoUrl).resize(500,500).centerCrop()
                    .into(binder.detailImage);
            detailActivity.getSupportActionBar().setTitle(photosBean.title);
        }

        //show download image after 500 ms
        Handler handler = new Handler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //visible download button
                binder.btnDownload.show();
            }
        },500);
    }


    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                if(context.get()!=null && Utils.isNetworkAvailable(context.get())){
                    //show progressbar
//                    Intent intent = new Intent("com.start");
//                    context.get().sendBroadcast(intent);
                    if(PermissionsAndroid.getInstance().checkWriteExternalStoragePermission(detailActivity)){
                        if(photosBean!=null){
                            downloadFile(photosBean);
                        }
                    }else{
                        PermissionsAndroid.getInstance().requestForWriteExternalStoragePermission(detailActivity);
                    }
                }
                else{
                    System.out.println("null something");
                }
                break;
        }
    }

    public void onBackPressed(){
            if(detailActivity!=null){
                binder.btnDownload.hide();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        detailActivity.oneStepBack();
                    }
                },500);

            }
    }


    /**
     * Download File Concept implemented here
     */
    private DownloadManager dm;
    private long enqueue;
    public void downloadFile(final PhotosBean photosBean) {
        //download music functionality here
//        songsProgressMap.put(musicBean.getUrl(),progressBar);
//        progressBar.setVisibility(View.VISIBLE);

        dm = (DownloadManager)detailActivity.getSystemService(detailActivity.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(photosBean.photoUrl));
        request.setTitle(photosBean.photoName);
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        File file = Utils.getInstance().saveToDirectory(detailActivity,photosBean.photoName, "jpg");
        request.setDestinationUri(Uri.parse("file://"  + "/" + file.getAbsolutePath()));
        if(file!=null && file.exists())file.delete();
        enqueue = dm.enqueue(request);

    }

    public void subscribeDownloadReceiver(){
        detailActivity.registerReceiver(downloadCompleteReceiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    public void unSubscribeDownloadReceiver(){
        try {
//            if(downloadCompleteReceiver!=null && detailActivity!=null)
//                detailActivity.unregisterReceiver(downloadCompleteReceiver);
        }
        catch (IllegalStateException illegal){

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


}
