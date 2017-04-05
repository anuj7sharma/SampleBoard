package com.androidpay.view.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.view.Window;
import android.view.WindowManager;

import com.androidpay.MainActivity;
import com.androidpay.R;
import com.androidpay.databinding.ActivityDetailBinding;
import com.androidpay.presenters.DetailPresenter;
import com.androidpay.services.DownloadDataService;
import com.androidpay.utils.Utils;

import java.lang.reflect.AccessibleObject;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Anuj Sharma on 2/28/2017.
 */

public class DetailActivity extends MainActivity implements DetailView {

    ActivityDetailBinding binder;
    DetailPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        presenter = new DetailPresenter(this, binder, this,this);
        binder.btnDownload.setOnClickListener(presenter);
    }

    @Override
    public void backPress() {
        if(presenter!=null){
            presenter.onBackPressed();
        }
    }
}


