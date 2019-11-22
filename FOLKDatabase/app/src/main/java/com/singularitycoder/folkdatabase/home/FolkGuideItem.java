package com.singularitycoder.folkdatabase.home;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

public class FolkGuideItem {

    @Exclude
    private String id;

    private String strProfileImage;
    private String strFirstName;
    private String strLastName;
    private String strDepartment;
    private String strKcExperience;

    public FolkGuideItem() {
    }

    public FolkGuideItem(String id, String strProfileImage, String strFirstName, String strLastName, String strDepartment, String strKcExperience) {
        this.id = id;
        this.strProfileImage = strProfileImage;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.strDepartment = strDepartment;
        this.strKcExperience = strKcExperience;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStrProfileImage() {
        return strProfileImage;
    }

    public void setStrProfileImage(String strProfileImage) {
        this.strProfileImage = strProfileImage;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public void setStrLastName(String strLastName) {
        this.strLastName = strLastName;
    }

    public String getStrDepartment() {
        return strDepartment;
    }

    public void setStrDepartment(String strDepartment) {
        this.strDepartment = strDepartment;
    }

    public String getStrKcExperience() {
        return strKcExperience;
    }

    public void setStrKcExperience(String strKcExperience) {
        this.strKcExperience = strKcExperience;
    }
}
