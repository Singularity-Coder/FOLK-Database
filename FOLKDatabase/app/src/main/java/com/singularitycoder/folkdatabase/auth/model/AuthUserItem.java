package com.singularitycoder.folkdatabase.auth.model;

import com.google.firebase.firestore.Exclude;

public class AuthUserItem {

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
    private String epochTimeStamp;

//    @SerializedName("List of Zones")
//    @Expose(serialize = true)
//    private ArrayList<String> zonesArray;
//
//    @SerializedName("TeamLeads")
//    @Expose(serialize = true)
//    private ArrayList<String> teamLeadsArray;

    public AuthUserItem() {
    }

    public AuthUserItem(String zone, String memberType, String directAuthority, String shortName, String hkmJoiningDate, String fullName, String phone, String email, String gmail, String password, String profileImageUrl, String signUpStatus, String creationTimeStamp, String epochTimeStamp) {
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
        this.epochTimeStamp = epochTimeStamp;
    }

    @Exclude
    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getDirectAuthority() {
        return directAuthority;
    }

    public void setDirectAuthority(String directAuthority) {
        this.directAuthority = directAuthority;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getHkmJoiningDate() {
        return hkmJoiningDate;
    }

    public void setHkmJoiningDate(String hkmJoiningDate) {
        this.hkmJoiningDate = hkmJoiningDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getSignUpStatus() {
        return signUpStatus;
    }

    public void setSignUpStatus(String signUpStatus) {
        this.signUpStatus = signUpStatus;
    }

    public String getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(String creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public String getEpochTimeStamp() {
        return epochTimeStamp;
    }

    public void setEpochTimeStamp(String epochTimeStamp) {
        this.epochTimeStamp = epochTimeStamp;
    }

    //    public ArrayList<String> getZonesArray() {
//        return zonesArray;
//    }
//
//    public ArrayList<String> getTeamLeadsArray() {
//        return teamLeadsArray;
//    }
}
