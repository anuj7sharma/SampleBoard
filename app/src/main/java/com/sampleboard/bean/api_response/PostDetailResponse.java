package com.sampleboard.bean.api_response;

/**
 * @author AnujSharma on 1/16/2018.
 */

public class PostDetailResponse extends BaseResponse {
    private TimelineObjResponse data;

    public TimelineObjResponse getData() {
        return data;
    }

    public void setData(TimelineObjResponse data) {
        this.data = data;
    }
}
