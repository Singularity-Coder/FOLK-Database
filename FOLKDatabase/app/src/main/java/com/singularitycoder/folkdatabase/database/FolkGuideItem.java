package com.singularitycoder.folkdatabase.database;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class FolkGuideItem implements Serializable {

    @Exclude
    private String id;

    private String strProfileImage;
    private String strFirstName;
    private String strLastName;
    private String strFolkGuideAbbr;
    private String strZone;

    private String strPhone;
    private String strWhatsApp;
    private String strEmail;

    public FolkGuideItem() {
    }

    public FolkGuideItem(String strProfileImage, String strFirstName, String strLastName) {
        this.strProfileImage = strProfileImage;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
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

    public String getStrPhone() {
        return strPhone;
    }

    public void setStrPhone(String strPhone) {
        this.strPhone = strPhone;
    }

    public String getStrWhatsApp() {
        return strWhatsApp;
    }

    public void setStrWhatsApp(String strWhatsApp) {
        this.strWhatsApp = strWhatsApp;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrFolkGuideAbbr() {
        return strFolkGuideAbbr;
    }

    public void setStrFolkGuideAbbr(String strFolkGuideAbbr) {
        this.strFolkGuideAbbr = strFolkGuideAbbr;
    }

    public String getStrZone() {
        return strZone;
    }

    public void setStrZone(String strZone) {
        this.strZone = strZone;
    }
}
