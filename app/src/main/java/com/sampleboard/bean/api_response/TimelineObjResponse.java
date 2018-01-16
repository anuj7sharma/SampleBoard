package com.sampleboard.bean.api_response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author AnujSharma on 1/15/2018.
 */

public class TimelineObjResponse implements Parcelable {
    /**
     * like_count : 2
     * is_liked : null
     * comment_count: 2
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
    @SerializedName("comment_count")
    private int comment_count;
    @SerializedName("user_profile")
    private String user_profile;
    @SerializedName("user_name")
    private String user_name;
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

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

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

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
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

    public TimelineObjResponse() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.likeCount);
        dest.writeString(this.isLiked);
        dest.writeInt(this.comment_count);
        dest.writeString(this.user_profile);
        dest.writeString(this.user_name);
        dest.writeInt(this.id);
        dest.writeInt(this.userId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.media);
        dest.writeString(this.mediaThumbnail);
        dest.writeString(this.mediaFullsize);
        dest.writeString(this.creationDate);
        dest.writeString(this.updationDate);
        dest.writeInt(this.isActive);
    }

    protected TimelineObjResponse(Parcel in) {
        this.likeCount = in.readInt();
        this.isLiked = in.readString();
        this.comment_count = in.readInt();
        this.user_profile = in.readString();
        this.user_name = in.readString();
        this.id = in.readInt();
        this.userId = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.media = in.readString();
        this.mediaThumbnail = in.readString();
        this.mediaFullsize = in.readString();
        this.creationDate = in.readString();
        this.updationDate = in.readString();
        this.isActive = in.readInt();
    }

    public static final Creator<TimelineObjResponse> CREATOR = new Creator<TimelineObjResponse>() {
        @Override
        public TimelineObjResponse createFromParcel(Parcel source) {
            return new TimelineObjResponse(source);
        }

        @Override
        public TimelineObjResponse[] newArray(int size) {
            return new TimelineObjResponse[size];
        }
    };
}
