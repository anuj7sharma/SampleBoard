package com.sampleboard.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.sampleboard.api.APIHandler;
import com.sampleboard.bean.LikedBean;
import com.sampleboard.bean.api_response.PostDetailResponse;
import com.sampleboard.bean.api_response.TimelineObjResponse;
import com.sampleboard.bean.api_response.UpdateLikeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * author by Anuj Sharma on 12/15/2017.
 */

public class DetailFragmentViewModel extends ViewModel {
    private MutableLiveData<TimelineObjResponse> getPostResponse;
    private MutableLiveData<String> message;
    private MutableLiveData<UpdateLikeResponse> updateLikeResponse;

    public MutableLiveData<UpdateLikeResponse> getUpdateLikeResponse() {
        if (updateLikeResponse == null) updateLikeResponse = new MutableLiveData<>();
        return updateLikeResponse;
    }

    public MutableLiveData<TimelineObjResponse> getGetPostResponse() {
        if (getPostResponse == null) getPostResponse = new MutableLiveData<>();
        return getPostResponse;
    }

    public MutableLiveData<String> getMessage() {
        if (message == null) message = new MutableLiveData<>();
        return message;
    }

    public List<LikedBean> loadDummyRelatedData() {
        List<LikedBean> likeList = new ArrayList<>();
        LikedBean obj = new LikedBean();
        obj.imageUrl = "https://i.ytimg.com/vi/x30YOmfeVTE/maxresdefault.jpg";

        likeList.add(obj);

        obj = new LikedBean();
        obj.imageName = "Testing image";
        obj.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/36/Hopetoun_falls.jpg";

        likeList.add(obj);

        obj = new LikedBean();
        obj.imageName = "Testing image";
        obj.imageUrl = "https://static.pexels.com/photos/33109/fall-autumn-red-season.jpg";

        likeList.add(obj);

        obj = new LikedBean();
        obj.imageName = "Testing image";
        obj.imageUrl = "https://static.pexels.com/photos/39811/pexels-photo-39811.jpeg";

        likeList.add(obj);

        return likeList;

    }

    public int updateLikesCounter(int count, boolean isIncreased) {
        if (isIncreased)
            return count + 1;
        else
            return count - 1;
    }

    /**
     * Get Post Detail from server
     *
     * @param userId
     * @param postid
     */
    public void getPostDetail(String userId, int postid) {
        if (TextUtils.isEmpty(userId)) userId = "";
        APIHandler.getInstance().getHandler().getPostDetail(userId, postid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<PostDetailResponse>() {
                    @Override
                    public void onSuccess(PostDetailResponse postDetailResponse) {
                        if (postDetailResponse != null) {
                            if (postDetailResponse.getCode() == 1)
                                getGetPostResponse().postValue(postDetailResponse.getData());
                            else getMessage().postValue(postDetailResponse.getMessage());
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
