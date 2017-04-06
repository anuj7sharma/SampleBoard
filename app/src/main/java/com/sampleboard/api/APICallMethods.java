/*
 *
 *
 *  * Copyright © 2016, Mobilyte Inc. and/or its affiliates. All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  * - Redistributions of source code must retain the above copyright
 *  *    notice, this list of conditions and the following disclaimer.
 *  *
 *  * - Redistributions in binary form must reproduce the above copyright
 *  * notice, this list of conditions and the following disclaimer in the
 *  * documentation and/or other materials provided with the distribution.
 *
 * /
 */
package com.sampleboard.api;


import com.sampleboard.bean.MusicBean;
import com.sampleboard.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Mobilyte on 2/17/2016.
 */
public interface APICallMethods {


    @GET(Constants.MUSIC_API)
    Call<List<MusicBean>> getMusicList();

    /*
    @FormUrlEncoded
    @POST(ConstantFile.CHECK_IS_DRIVER)
    Call<CheckIsDriverResponse> checkIsDriver(@FieldMap Map<String, String> options);

    @Multipart
    @POST(ConstantFile.BECOME_A_DRIVER)
    Call<BasicResponse>becomeDriver(@PartMap Map<String, RequestBody> options, @Part MultipartBody.Part profile_pic
            , @Part MultipartBody.Part license_pic, @Part MultipartBody.Part insurance_pic);*/

}
