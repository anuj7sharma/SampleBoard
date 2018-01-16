package com.sampleboard.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.sampleboard.api.APIHandler;
import com.sampleboard.bean.api_response.UpdateLikeResponse;
import com.sampleboard.bean.api_response.TimelineResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * author by Anuj Sharma on 12/8/2017.
 */

public class HomeFragmentViewModel extends ViewModel {
    private MutableLiveData<TimelineResponse> timelineResponse;
    private MutableLiveData<String> message;
    private MutableLiveData<UpdateLikeResponse> updateLikeResponse;

    public MutableLiveData<UpdateLikeResponse> getUpdateLikeResponse() {
        if (updateLikeResponse == null) updateLikeResponse = new MutableLiveData<>();
        return updateLikeResponse;
    }

    public MutableLiveData<TimelineResponse> getTimelineResponse() {
        if (timelineResponse == null) timelineResponse = new MutableLiveData<>();
        return timelineResponse;
    }



    public MutableLiveData<String> getMessage() {
        if (message == null) message = new MutableLiveData<>();
        return message;
    }

    /**
     * Get Timeline when App opens for first time
     *
     * @param userId
     * @param page
     */
    public void getTimeLine(String userId, int page) {
        APIHandler.getInstance().getHandler().getTimeline(userId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<TimelineResponse>() {
                    @Override
                    public void onSuccess(TimelineResponse timelineResponse) {
                        if (timelineResponse != null) {
                            if (timelineResponse.getCode() == 1) {
                                getTimelineResponse().postValue(timelineResponse);
                            } else {
                                getMessage().postValue(timelineResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    /**
     * Update Like API
     */
    public void updateLike(Map<String, String> param) {
        APIHandler.getInstance().getHandler().updateLike(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UpdateLikeResponse>() {
                    @Override
                    public void onSuccess(UpdateLikeResponse likeResponse) {
                        if (likeResponse != null) {
                            if (likeResponse.getCode() == 1)
                                getUpdateLikeResponse().postValue(likeResponse);
                            else
                                getMessage().postValue(likeResponse.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
