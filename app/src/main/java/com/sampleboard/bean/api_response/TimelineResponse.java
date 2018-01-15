package com.sampleboard.bean.api_response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author AnujSharma on 1/15/2018.
 */

public class TimelineResponse extends BaseResponse {

    @SerializedName("data")
    private List<TimelineObjResponse> data;

    public List<TimelineObjResponse> getData() {
        return data;
    }

    public void setData(List<TimelineObjResponse> data) {
        this.data = data;
    }
    
}
