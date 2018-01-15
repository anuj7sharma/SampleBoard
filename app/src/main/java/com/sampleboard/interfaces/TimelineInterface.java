package com.sampleboard.interfaces;

import android.widget.ImageView;

import com.sampleboard.bean.MediaItem;
import com.sampleboard.bean.PhotosBean;
import com.sampleboard.bean.api_response.TimelineObjResponse;

/**
 * author by Anuj Sharma on 12/13/2017.
 */

public interface TimelineInterface {
    void onItemClick(TimelineObjResponse obj, ImageView imageView, int position);
    void onLikeBtnClicked(TimelineObjResponse obj, ImageView imageView, int position, boolean isLiked);
}
