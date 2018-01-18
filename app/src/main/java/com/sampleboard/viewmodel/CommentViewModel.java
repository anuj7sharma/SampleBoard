package com.sampleboard.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.sampleboard.adapter.CommentAdapter;
import com.sampleboard.api.APIHandler;
import com.sampleboard.bean.api_response.BaseResponse;
import com.sampleboard.bean.api_response.GetCommentsResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AnujSharma on 12/27/2017.
 */

public class CommentViewModel extends ViewModel {
    private MutableLiveData<String> message;
    private MutableLiveData<GetCommentsResponse> getCommentResponse;
    private MutableLiveData<BaseResponse> postCommentResponse;

    public MutableLiveData<BaseResponse> getPostCommentResponse() {
        if (postCommentResponse == null) postCommentResponse = new MutableLiveData<>();
        return postCommentResponse;
    }

    public MutableLiveData<String> getMessage() {
        if (message == null) message = new MutableLiveData<>();
        return message;
    }

    public MutableLiveData<GetCommentsResponse> getGetCommentResponse() {
        if (getCommentResponse == null) getCommentResponse = new MutableLiveData<>();
        return getCommentResponse;
    }

    //    private MutableLiveData<>

    /**
     * Get Comments with pagination
     *
     * @param postId
     * @param page
     */
    public void getComments(String postId, int page) {
        APIHandler.getInstance().getHandler().getComments(postId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<GetCommentsResponse>() {
                    @Override
                    public void onSuccess(GetCommentsResponse getCommentsResponse) {
                        if (getCommentsResponse != null) {
                            getGetCommentResponse().postValue(getCommentsResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    /**
     * Post Comments API
     *
     * @param comment
     * @param commentType
     * @param postId
     * @param userId
     */
    public void postComment(String comment, CommentAdapter.CommentType commentType, String postId, String userId) {
        Map<String, String> param = new HashMap<>();
        param.put("user_id", userId);
        param.put("post_id", postId);
        param.put("comment", comment);
        param.put("comment_type", commentType.toString());
        /*switch (commentType) {
            case TEXT:
                param.put("comment_type",commentType.toString());
                break;
            case IMAGE:
                break;
        }*/

        APIHandler.getInstance().getHandler().postComment(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            getPostCommentResponse().postValue(baseResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        
                    }
                });

    }


}
