package com.sampleboard.bean.api_response;

import com.google.gson.annotations.SerializedName;

/**
 * @author AnujSharma on 1/15/2018.
 */

public class UpdateLikeResponse extends BaseResponse {

    /**
     * like_status : 0
     */

    @SerializedName("like_status")
    private int likeStatus;

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }
}
