package com.singularitycoder.folkdatabase.home;

import android.net.Uri;

public class PersonModel {

    int imgProfileImage;
    String firstName;
    String lastName;
    String booksSold;
    String lakshmiEarned;
    int imgGroupProfileImage;
    String strName;
    String strSubTitle1;
    String strSubTitle2;
    String strDate;
    String strChatCount;

    Uri ivProfileImage;
    String imageName;
    String imageExtension;

    public PersonModel() {
    }

    // Contact, Caller, Admin
    public PersonModel(int imgProfileImage, String strName, String strSubTitle1, String strSubTitle2) {
        this.imgProfileImage = imgProfileImage;
        this.strName = strName;
        this.strSubTitle1 = strSubTitle1;
        this.strSubTitle2 = strSubTitle2;
    }

    // Group Chat
    public PersonModel(int imgGroupProfileImage, String strName, String strSubTitle1, String strSubTitle2, String strDate, String strChatCount) {
        this.imgGroupProfileImage = imgGroupProfileImage;
        this.strName = strName;
        this.strSubTitle1 = strSubTitle1;
        this.strSubTitle2 = strSubTitle2;
        this.strDate = strDate;
        this.strChatCount = strChatCount;
    }

    // Chat
    public PersonModel(int imgProfileImage, String strName, String strSubTitle1, String strDate, String strChatCount) {
        this.imgProfileImage = imgProfileImage;
        this.strName = strName;
        this.strSubTitle1 = strSubTitle1;
        this.strDate = strDate;
        this.strChatCount = strChatCount;
    }

    // Notifications
    public PersonModel(String strName, int imgProfileImage, String strSubTitle2, String strDate) {
        this.strName = strName;
        this.imgProfileImage = imgProfileImage;
        this.strSubTitle2 = strSubTitle2;
        this.strDate = strDate;
    }

    // Notifications
    public PersonModel(String strName, int imgProfileImage, String strSubTitle2, String strDate, String strChatCount) {
        this.strName = strName;
        this.imgProfileImage = imgProfileImage;
        this.strSubTitle2 = strSubTitle2;
        this.strDate = strDate;
        this.strChatCount = strChatCount;
    }

    public int getImgProfileImage() {
        return imgProfileImage;
    }

    public void setImgProfileImage(int imgProfileImage) {
        this.imgProfileImage = imgProfileImage;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrSubTitle1() {
        return strSubTitle1;
    }

    public void setStrSubTitle1(String strSubTitle1) {
        this.strSubTitle1 = strSubTitle1;
    }

    public String getStrSubTitle2() {
        return strSubTitle2;
    }

    public void setStrSubTitle2(String strSubTitle2) {
        this.strSubTitle2 = strSubTitle2;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrChatCount() {
        return strChatCount;
    }

    public void setStrChatCount(String strChatCount) {
        this.strChatCount = strChatCount;
    }

    public Uri getIvProfileImage() {
        return ivProfileImage;
    }

    public void setIvProfileImage(Uri ivProfileImage) {
        this.ivProfileImage = ivProfileImage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageExtension() {
        return imageExtension;
    }

    public void setImageExtension(String imageExtension) {
        this.imageExtension = imageExtension;
    }
}
