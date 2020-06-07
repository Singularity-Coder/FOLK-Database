package com.singularitycoder.folkdatabase.database.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class AllUsersItem implements Serializable {

    private String id;

    private String strProfileImage;
    private String strFirstName;
    private String strMemberType;

    private String strPhone;
    private String strWhatsApp;
    private String strEmail;
    private String strGmail;
    private String strAccountCreationDate;

    public AllUsersItem() {
    }

    @Exclude
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

    public String getStrMemberType() {
        return strMemberType;
    }

    public void setStrMemberType(String strMemberType) {
        this.strMemberType = strMemberType;
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

    public String getStrGmail() {
        return strGmail;
    }

    public void setStrGmail(String strGmail) {
        this.strGmail = strGmail;
    }

    public String getStrAccountCreationDate() {
        return strAccountCreationDate;
    }

    public void setStrAccountCreationDate(String strAccountCreationDate) {
        this.strAccountCreationDate = strAccountCreationDate;
    }
}
