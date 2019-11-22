package com.singularitycoder.folkdatabase.profile;

public class ProfileContactItem {

    private int profileImage;
    private String name;
    private String dateTime;
    private String comment;
    private String activityName;

    public ProfileContactItem() {
    }

    public ProfileContactItem(String comment) {
        this.comment = comment;
    }

    // Comments
    public ProfileContactItem(int profileImage, String name, String dateTime, String comment) {
        this.profileImage = profileImage;
        this.name = name;
        this.dateTime = dateTime;
        this.comment = comment;
    }

    // Called By
    public ProfileContactItem(int profileImage, String name, String dateTime, String activityName, String empty) {
        this.profileImage = profileImage;
        this.name = name;
        this.dateTime = dateTime;
        this.activityName = activityName;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
