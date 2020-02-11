package com.singularitycoder.folkdatabase.database;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class FolkGuideItem implements Serializable {

    @Exclude
    private String id;

    private String strProfileImage;
    private String strName;
    private String strFolkGuideShortName;
    private String strZone;
    private String strPhone;
    private String strWhatsApp;
    private String strEmail;
    private String strGmail;
    private String strCreationTimeStamp;
    private String strMemberType;
    private String strDirectAuthority;
    private String strHkmJoiningDate;


    public FolkGuideItem() {
    }

    public FolkGuideItem(String strProfileImage, String strName, String strLastName) {
        this.strProfileImage = strProfileImage;
        this.strName = strName;
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

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
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

    public String getStrFolkGuideShortName() {
        return strFolkGuideShortName;
    }

    public void setStrFolkGuideShortName(String strFolkGuideShortName) {
        this.strFolkGuideShortName = strFolkGuideShortName;
    }

    public String getStrZone() {
        return strZone;
    }

    public void setStrZone(String strZone) {
        this.strZone = strZone;
    }

    public String getStrGmail() {
        return strGmail;
    }

    public void setStrGmail(String strGmail) {
        this.strGmail = strGmail;
    }

    public String getStrCreationTimeStamp() {
        return strCreationTimeStamp;
    }

    public void setStrCreationTimeStamp(String strCreationTimeStamp) {
        this.strCreationTimeStamp = strCreationTimeStamp;
    }

    public String getStrMemberType() {
        return strMemberType;
    }

    public void setStrMemberType(String strMemberType) {
        this.strMemberType = strMemberType;
    }

    public String getStrDirectAuthority() {
        return strDirectAuthority;
    }

    public void setStrDirectAuthority(String strDirectAuthority) {
        this.strDirectAuthority = strDirectAuthority;
    }

    public String getStrHkmJoiningDate() {
        return strHkmJoiningDate;
    }

    public void setStrHkmJoiningDate(String strHkmJoiningDate) {
        this.strHkmJoiningDate = strHkmJoiningDate;
    }
}
