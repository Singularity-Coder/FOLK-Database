package com.singularitycoder.folkdatabase.home.model;

import java.io.Serializable;

public class HomeItem implements Serializable {

    private int intHomeImage;
    private String strHomeTitle;
    private String strHomeCount;

    private String strImageUrl;
    private String strUserName;

    // Header
    public HomeItem(String strImageUrl, String strUserName) {
        this.strImageUrl = strImageUrl;
        this.strUserName = strUserName;
    }

    public HomeItem(int intHomeImage, String strHomeTitle, String strHomeCount) {
        this.intHomeImage = intHomeImage;
        this.strHomeTitle = strHomeTitle;
        this.strHomeCount = strHomeCount;
    }

    public int getIntHomeImage() {
        return intHomeImage;
    }

    public void setIntHomeImage(int intHomeImage) {
        this.intHomeImage = intHomeImage;
    }

    public String getStrHomeTitle() {
        return strHomeTitle;
    }

    public void setStrHomeTitle(String strHomeTitle) {
        this.strHomeTitle = strHomeTitle;
    }

    public String getStrHomeCount() {
        return strHomeCount;
    }

    public void setStrHomeCount(String strHomeCount) {
        this.strHomeCount = strHomeCount;
    }

    public String getStrImageUrl() {
        return strImageUrl;
    }

    public void setStrImageUrl(String strImageUrl) {
        this.strImageUrl = strImageUrl;
    }

    public String getStrUserName() {
        return strUserName;
    }

    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
    }
}
