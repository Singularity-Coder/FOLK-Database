package com.singularitycoder.folkdatabase.auth;

import com.google.firebase.firestore.Exclude;

public class AuthUserItem {

    @Exclude
    private String docId;
    // Make sure u use the same names as u provided in the Firebase. Same obj names
    private String zone;
    private String memberType;
    private String directAuthority;
    private String shortName;
    private String hkmJoiningDate;
    private String fullName;
    private String phone;
    private String email;
    private String gmail;
    private String password;
    private String profileImageUrl;
    private String signUpStatus;
    private String creationTimeStamp;

    public AuthUserItem() {
    }

    public AuthUserItem(String zone, String memberType, String directAuthority, String shortName, String hkmJoiningDate, String fullName, String phone, String email, String gmail, String password, String profileImageUrl, String signUpStatus, String creationTimeStamp) {
        this.zone = zone;
        this.memberType = memberType;
        this.directAuthority = directAuthority;
        this.shortName = shortName;
        this.hkmJoiningDate = hkmJoiningDate;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.gmail = gmail;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.signUpStatus = signUpStatus;
        this.creationTimeStamp = creationTimeStamp;
    }

    public String getZone() {
        return zone;
    }

    public String getMemberType() {
        return memberType;
    }

    public String getDirectAuthority() {
        return directAuthority;
    }

    public String getShortName() {
        return shortName;
    }

    public String getHkmJoiningDate() {
        return hkmJoiningDate;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getGmail() {
        return gmail;
    }

    public String getPassword() {
        return password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getSignUpStatus() {
        return signUpStatus;
    }

    public String getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
