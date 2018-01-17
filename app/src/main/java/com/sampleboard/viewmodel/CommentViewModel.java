package com.sampleboard.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.sampleboard.api.APIHandler;
import com.sampleboard.bean.api_response.GetCommentsResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AnujSharma on 12/27/2017.
 */

public class CommentViewModel extends ViewModel {
    private MutableLiveData<String> message;
    private MutableLiveData<GetCommentsResponse> messageResponse;

    public MutableLiveData<String> getMessage() {
        if (message == null) message = new MutableLiveData<>();
        return message;
    }

    public MutableLiveData<GetCommentsResponse> getMessageResponse() {
        if (messageResponse == null) messageResponse = new MutableLiveData<>();
        return messageResponse;
    }

    //    private MutableLiveData<>

    public void getComments(String postId, int page) {
        APIHandler.getInstance().getHandler().getComments(postId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<GetCommentsResponse>() {
                    @Override
                    public void onSuccess(GetCommentsResponse getCommentsResponse) {
                        if (getCommentsResponse != null) {
                            getMessageResponse().postValue(getCommentsResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


}
