package com.singularitycoder.folkdatabase.home;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.util.Map;

public class PersonModel {

    @Exclude
    private String id;

    private String strProfileImage;
    private String firstName;
    private String lastName;
    private String strName;
    private String strSubTitle1;
    private String strSubTitle2;
    private String strDate;
    private String strChatCount;
    private String strFolkGuide;
    private String strOccupation;

    private Uri ivProfileImage;
    private String imageName;
    private String imageExtension;

    public PersonModel() {
    }

    // Contact, Caller, Admin
    public PersonModel(String imgProfileImage, String strName, String strSubTitle1, String strSubTitle2, String empty1, String empty2, String empty3) {
        this.strProfileImage = imgProfileImage;
        this.strName = strName;
        this.strSubTitle1 = strSubTitle1;
        this.strSubTitle2 = strSubTitle2;
    }

    // Notifications
    public PersonModel(String strName, String imgProfileImage, String strSubTitle2, String strDate) {
        this.strName = strName;
        this.strProfileImage = imgProfileImage;
        this.strSubTitle2 = strSubTitle2;
        this.strDate = strDate;
    }

    // Notifications
    public PersonModel(String strName, String imgProfileImage, String strSubTitle2, String strDate, String strChatCount) {
        this.strName = strName;
        this.strProfileImage = imgProfileImage;
        this.strSubTitle2 = strSubTitle2;
        this.strDate = strDate;
        this.strChatCount = strChatCount;
    }

    public String getImgProfileImage() {
        return strProfileImage;
    }

    public void setImgProfileImage(String imgProfileImage) {
        this.strProfileImage = imgProfileImage;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStrFolkGuide() {
        return strFolkGuide;
    }

    public void setStrFolkGuide(String strFolkGuide) {
        this.strFolkGuide = strFolkGuide;
    }

    public String getStrOccupation() {
        return strOccupation;
    }

    public void setStrOccupation(String strOccupation) {
        this.strOccupation = strOccupation;
    }

    public String getStrProfileImage() {
        return strProfileImage;
    }

    public void setStrProfileImage(String strProfileImage) {
        this.strProfileImage = strProfileImage;
    }
}
