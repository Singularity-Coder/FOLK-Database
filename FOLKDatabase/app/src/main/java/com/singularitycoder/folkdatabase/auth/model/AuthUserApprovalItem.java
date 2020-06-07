package com.singularitycoder.folkdatabase.auth.model;

import com.google.firebase.firestore.Exclude;

public class AuthUserApprovalItem {

    @Exclude
    private String docId;

    private String zone;
    private String memberType;
    private String directAuthority;
    private String email;
    private String shortName;
    private String fullName;
    private String profileImageUrl;
    private String signUpStatus;
    private String redFlagStatus;
    private String approveRequestTimeStamp;
    private String approveRequestEpochTimeStamp;

    public AuthUserApprovalItem() {
    }

    public AuthUserApprovalItem(String docId, String zone, String memberType, String directAuthority, String email, String shortName, String fullName, String profileImageUrl, String signUpStatus, String redFlagStatus, String approveRequestTimeStamp, String approveRequestEpochTimeStamp) {
        this.docId = docId;
        this.zone = zone;
        this.memberType = memberType;
        this.directAuthority = directAuthority;
        this.email = email;
        this.shortName = shortName;
        this.fullName = fullName;
        this.profileImageUrl = profileImageUrl;
        this.signUpStatus = signUpStatus;
        this.redFlagStatus = redFlagStatus;
        this.approveRequestTimeStamp = approveRequestTimeStamp;
        this.approveRequestEpochTimeStamp = approveRequestEpochTimeStamp;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocId() {
        return docId;
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

    public String getEmail() {
        return email;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getSignUpStatus() {
        return signUpStatus;
    }

    public String getRedFlagStatus() {
        return redFlagStatus;
    }

    public String getApproveRequestTimeStamp() {
        return approveRequestTimeStamp;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public void setDirectAuthority(String directAuthority) {
        this.directAuthority = directAuthority;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setSignUpStatus(String signUpStatus) {
        this.signUpStatus = signUpStatus;
    }

    public void setRedFlagStatus(String redFlagStatus) {
        this.redFlagStatus = redFlagStatus;
    }

    public void setApproveRequestTimeStamp(String approveRequestTimeStamp) {
        this.approveRequestTimeStamp = approveRequestTimeStamp;
    }

    public String getApproveRequestEpochTimeStamp() {
        return approveRequestEpochTimeStamp;
    }

    public void setApproveRequestEpochTimeStamp(String approveRequestEpochTimeStamp) {
        this.approveRequestEpochTimeStamp = approveRequestEpochTimeStamp;
    }
}
