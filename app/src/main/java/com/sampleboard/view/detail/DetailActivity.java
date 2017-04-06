package com.sampleboard.view.detail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sampleboard.MainActivity;
import com.sampleboard.R;
import com.sampleboard.databinding.ActivityDetailBinding;
import com.sampleboard.presenters.DetailPresenter;

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


