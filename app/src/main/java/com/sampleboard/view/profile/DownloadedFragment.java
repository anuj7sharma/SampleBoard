package com.sampleboard.view.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sampleboard.R;
import com.sampleboard.adapter.DownloadedAdapter;
import com.sampleboard.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anuj Sharma on 4/5/2017.
 */

public class DownloadedFragment extends BaseFragment {

    private View rootView;
    private RecyclerView mRecyclerView;
    private DownloadedAdapter mAdapter;
    private List<String> jobsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_completed_task,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState!=null){
            //retrieve data first here
        }
        initViews();
    }

    private void initViews() {
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.completed_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);
        if(jobsList==null){
            jobsList = new ArrayList<>();
            jobsList.add(new String("First Job"));
            jobsList.add(new String("Second Job"));
            jobsList.add(new String("Third Job"));
            jobsList.add(new String("Fourth Job"));
            jobsList.add(new String("Fifth Job"));
            jobsList.add(new String("Sixth Job"));
            jobsList.add(new String("Seventh Job"));
            jobsList.add(new String("First Job"));
            jobsList.add(new String("Second Job"));
            jobsList.add(new String("Third Job"));
            jobsList.add(new String("Fourth Job"));
            jobsList.add(new String("Fifth Job"));
            jobsList.add(new String("Sixth Job"));
            jobsList.add(new String("Seventh Job"));
        }
        if(mAdapter==null){
            mAdapter = new DownloadedAdapter(getActivity(),jobsList);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.updateData(jobsList);
        }
    }
}
