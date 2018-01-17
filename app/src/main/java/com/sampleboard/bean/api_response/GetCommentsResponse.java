package com.sampleboard.bean.api_response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author AnujSharma on 1/17/2018.
 */

public class GetCommentsResponse extends BaseResponse {

    @SerializedName("data")
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user_profile : /candy_profile/2018.01.10.\Walking Pug-2.jpg
         * user_name : Anuj
         * id : 2
         * post_id : 1
         * user_id : 11
         * comment_type : TEXT
         * comment : loved to see this is working.
         * creation_date : 2018-01-12T06:06:23Z
         */

        @SerializedName("user_profile")
        private String userProfile;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("id")
        private int id;
        @SerializedName("post_id")
        private int postId;
        @SerializedName("user_id")
        private int userId;
        @SerializedName("comment_type")
        private String commentType;
        @SerializedName("comment")
        private String comment;
        @SerializedName("creation_date")
        private String creationDate;

        public String getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(String userProfile) {
            this.userProfile = userProfile;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPostId() {
            return postId;
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getCommentType() {
            return commentType;
        }

        public void setCommentType(String commentType) {
            this.commentType = commentType;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }
    }
}
