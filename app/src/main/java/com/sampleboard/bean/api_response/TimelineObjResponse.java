package com.sampleboard.bean.api_response;

import com.google.gson.annotations.SerializedName;

/**
 * @author AnujSharma on 1/15/2018.
 */

public class TimelineObjResponse {
    /**
     * like_count : 2
     * is_liked :
     * id : 1
     * user_id : 11
     * title : Evening Beauty
     * description : Such a nice time to spent time with your loved ones in this beautiful evening.
     * media : /candy_media/2018.01.08.\beautiful-evening-wallpaper-1.jpg
     * media_thumbnail :
     * media_fullsize :
     * creation_date : 2018-01-08T07:14:26Z
     * updation_date : 2018-01-08T07:14:26Z
     * is_active : 1
     */

    @SerializedName("like_count")
    private int likeCount;
    @SerializedName("is_liked")
    private String isLiked;
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("media")
    private String media;
    @SerializedName("media_thumbnail")
    private String mediaThumbnail;
    @SerializedName("media_fullsize")
    private String mediaFullsize;
    @SerializedName("creation_date")
    private String creationDate;
    @SerializedName("updation_date")
    private String updationDate;
    @SerializedName("is_active")
    private int isActive;

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getMediaThumbnail() {
        return mediaThumbnail;
    }

    public void setMediaThumbnail(String mediaThumbnail) {
        this.mediaThumbnail = mediaThumbnail;
    }

    public String getMediaFullsize() {
        return mediaFullsize;
    }

    public void setMediaFullsize(String mediaFullsize) {
        this.mediaFullsize = mediaFullsize;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdationDate() {
        return updationDate;
    }

    public void setUpdationDate(String updationDate) {
        this.updationDate = updationDate;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
