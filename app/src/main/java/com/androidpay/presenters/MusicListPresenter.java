package com.androidpay.presenters;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.widget.ProgressBar;

import com.androidpay.R;
import com.androidpay.adapter.MusicListAdapter;
import com.androidpay.api.APIHandler;
import com.androidpay.api.APIResponseInterface;
import com.androidpay.bean.MusicBean;
import com.androidpay.databinding.FragmentMusicListBinding;
import com.androidpay.enums.ApiName;
import com.androidpay.utils.Utils;
import com.androidpay.view.musicModule.MusicPlayerActivity;
import com.androidpay.view.musicModule.MusicListFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Response;
import retrofit2.Retrofit;

import static com.androidpay.enums.ApiName.GET_MUSIC_LIST;

/**
 * Created by Anuj Sharma on 3/20/2017.
 */

public class MusicListPresenter implements APIResponseInterface{
    private FragmentMusicListBinding binder;
    private MusicListFragment fragment;
    private MusicListAdapter mAdapter;
    private List<MusicBean> musicList = null;

    //lazy loading information
    private int page = 0;

    public MusicListPresenter(FragmentMusicListBinding binder, MusicListFragment fragment) {
        this.binder = binder;
        this.fragment = fragment;
        init();
    }

    /**
     * Get List of music
     */
    public List<MusicBean> getMusicList(){
        return musicList;
    }


    private void init() {
        LinearLayoutManager lm = new LinearLayoutManager(fragment.getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        binder.musicListRecycler.setLayoutManager(lm);
        mAdapter = new MusicListAdapter(fragment,null);
        binder.musicListRecycler.setAdapter(mAdapter);

        //fetch Songs list from API
        if(Utils.isNetworkAvailable(fragment.getActivity())){
            MusicPlayerActivity.getInstance().getDashboardPresenter().showProgress(true);
            fetchMusicList();
        }else{
            Utils.getInstance().showToast(fragment.getString(R.string.error_internet));
        }
    }

    private void fetchMusicList() {
        APIHandler.getInstance().getMusicList(this,GET_MUSIC_LIST);
    }

    @Override
    public void onSuccess(Response response, Retrofit retrofit, ApiName api) {
        MusicPlayerActivity.getInstance().getDashboardPresenter().showProgress(false);
        switch (api){
            case GET_MUSIC_LIST:
                if(response.errorBody()==null){
                    if(musicList==null)musicList = new ArrayList<>();
                    musicList = (List)response.body();
                    if(mAdapter!=null){
                        //upadte UI
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition(binder.musicListRecycler, new ChangeBounds());
                        }
                        // fetch data and insert into list
                        mAdapter.updateAdapter(musicList);
                    }
                }else{
                    //show error message
                    Utils.getInstance().showSnakBar(binder.getRoot(),response.message());
                }
                break;
        }
    }

    @Override
    public void onFailure(Throwable t, ApiName api) {
        MusicPlayerActivity.getInstance().getDashboardPresenter().showProgress(false);
        if(binder!=null && fragment.isVisible()){
            Utils.getInstance().showSnakBar(binder.getRoot(),fragment.getString(R.string.error_api));
        }else{
            Utils.getInstance().showSnakBar(binder.getRoot(),fragment.getString(R.string.error_api));
        }
    }

    /**
     *
     * @param query searched info
     */
    List<MusicBean> searchList;
    public void searchSongs(String query) {
        if(musicList!=null && musicList.size()>0){
            if(searchList==null)searchList = new ArrayList<>();
            searchList.clear();
            for (MusicBean obj:musicList
                 ) {
                if(obj.getSong().toLowerCase(Locale.ENGLISH).contains(query) || obj.getArtists().toLowerCase(Locale.ENGLISH).contains(query)){
                    searchList.add(obj);
                }
            }
            //update adapter
            mAdapter.updateAdapter(searchList);
        }else if(mAdapter!=null){
            mAdapter.updateAdapter(musicList);
            //No item in list
            Utils.getInstance().showSnakBar(binder.getRoot(),"No Songs in list");
        }
    }

    /**
     * Download File
     * @param musicBean
     * @param progressBar
     */
    Map<String,ProgressBar> songsProgressMap = new HashMap<>();

    private DownloadManager dm;
    private long enqueue;
    public void downloadFile(final MusicBean musicBean, final ProgressBar progressBar) {
        //download music functionality here
//        songsProgressMap.put(musicBean.getUrl(),progressBar);
//        progressBar.setVisibility(View.VISIBLE);

        dm = (DownloadManager)fragment.getActivity().getSystemService(fragment.getActivity().DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(musicBean.getUrl()));
        request.setTitle(musicBean.getSong());
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        File file = Utils.getInstance().saveToDirectory(fragment.getActivity(),musicBean.getSong(), "mp3");
        request.setDestinationUri(Uri.parse("file://"  + "/" + file.getAbsolutePath()));
        if(file!=null && file.exists())file.delete();
        enqueue = dm.enqueue(request);

    }

    public void subscribeMusicDownload(){
        fragment.getActivity().registerReceiver(downloadCompleteReceiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    public void unSubscribeMusicDownload(){
        try {
//            if(downloadCompleteReceiver!=null)
//                fragment.getActivity().unregisterReceiver(downloadCompleteReceiver);
        }
        catch (IllegalStateException illegal){

        }

    }
    BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(!fragment.isVisible()) return; // return if fragment is not visible

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
