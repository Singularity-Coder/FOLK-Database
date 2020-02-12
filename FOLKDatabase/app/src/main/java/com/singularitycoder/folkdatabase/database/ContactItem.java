package com.singularitycoder.folkdatabase.database;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ContactItem implements Serializable {

    @Exclude
    private String id;

    private String strProfileImage;
    private String strName;
    private String strSubTitle1;
    private String strSubTitle2;
    private String strDate;
    private String strChatCount;
    private String strFolkGuide;
    private String strOccupation;
    private String strDobMonth;
    private String strLocation;
    private String strRecidencyInterest;
    private String strPhone;
    private String strWhatsApp;
    private String strEmail;
    private String strBirthday;

    private Uri ivProfileImage;
    private String imageName;
    private String imageExtension;

    private String strCanCookFor;
    private String strSelfRating;
    private String strCanCookSouthIndian;
    private String strTalentDisclose;
    private String strCollegeLevel;
    private String strDistrictLevel;


    public ContactItem() {
    }

    // Talent
    public ContactItem(String strCanCookFor, String strSelfRating, String strCanCookSouthIndian, String strTalentDisclose, String strCollegeLevel, String strDistrictLevel) {
        this.strCanCookFor = strCanCookFor;
        this.strSelfRating = strSelfRating;
        this.strCanCookSouthIndian = strCanCookSouthIndian;
        this.strTalentDisclose = strTalentDisclose;
        this.strCollegeLevel = strCollegeLevel;
        this.strDistrictLevel = strDistrictLevel;
    }

    // Contact, Caller, Admin
    public ContactItem(String imgProfileImage, String strName, String strSubTitle1, String strSubTitle2, String empty1, String empty2, String empty3) {
        this.strProfileImage = imgProfileImage;
        this.strName = strName;
        this.strSubTitle1 = strSubTitle1;
        this.strSubTitle2 = strSubTitle2;
    }

    // Notifications
    public ContactItem(String strName, String imgProfileImage, String strSubTitle2, String strDate) {
        this.strName = strName;
        this.strProfileImage = imgProfileImage;
        this.strSubTitle2 = strSubTitle2;
        this.strDate = strDate;
    }

    // Notifications
    public ContactItem(String strName, String imgProfileImage, String strSubTitle2, String strDate, String strChatCount) {
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

    public String getStrDobMonth() {
        return strDobMonth;
    }

    public void setStrDobMonth(String strDobMonth) {
        this.strDobMonth = strDobMonth;
    }

    public String getStrLocation() {
        return strLocation;
    }

    public void setStrLocation(String strLocation) {
        this.strLocation = strLocation;
    }

    public String getStrRecidencyInterest() {
        return strRecidencyInterest;
    }

    public void setStrRecidencyInterest(String strRecidencyInterest) {
        this.strRecidencyInterest = strRecidencyInterest;
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

    public String getStrBirthday() {
        return strBirthday;
    }

    public void setStrBirthday(String strBirthday) {
        this.strBirthday = strBirthday;
    }

    public String getStrCanCookFor() {
        return strCanCookFor;
    }

    public void setStrCanCookFor(String strCanCookFor) {
        this.strCanCookFor = strCanCookFor;
    }

    public String getStrSelfRating() {
        return strSelfRating;
    }

    public void setStrSelfRating(String strSelfRating) {
        this.strSelfRating = strSelfRating;
    }

    public String getStrCanCookSouthIndian() {
        return strCanCookSouthIndian;
    }

    public void setStrCanCookSouthIndian(String strCanCookSouthIndian) {
        this.strCanCookSouthIndian = strCanCookSouthIndian;
    }

    public String getStrTalentDisclose() {
        return strTalentDisclose;
    }

    public void setStrTalentDisclose(String strTalentDisclose) {
        this.strTalentDisclose = strTalentDisclose;
    }

    public String getStrCollegeLevel() {
        return strCollegeLevel;
    }

    public void setStrCollegeLevel(String strCollegeLevel) {
        this.strCollegeLevel = strCollegeLevel;
    }

    public String getStrDistrictLevel() {
        return strDistrictLevel;
    }

    public void setStrDistrictLevel(String strDistrictLevel) {
        this.strDistrictLevel = strDistrictLevel;
    }
}
