package com.sampleboard;

import com.sampleboard.bean.PhotosBean;

import java.util.Comparator;

/**
 * Created by Anuj Sharma on 3/16/2017.
 */

public class sortList implements Comparator<PhotosBean>{

    @Override
    public int compare(PhotosBean o1, PhotosBean o2) {
        return o1.availableCount - o2.availableCount;
    }




}