package com.singularitycoder.folkdatabase.auth;

import com.google.firebase.firestore.Exclude;

public class AuthUserApprovalItem {

    @Exclude
    private String docId;


    private String zone;
    private String memberType;
    private String directAuthority;
    private String folkGuideAbbr;
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private String signUpStatus;
    private String redFlagStatus;
    private String approveRequestTimeStamp;

    public AuthUserApprovalItem(String docId, String zone, String memberType, String directAuthority, String folkGuideAbbr, String firstName, String lastName, String profileImageUrl, String signUpStatus, String redFlagStatus, String approveRequestTimeStamp) {
        this.docId = docId;
        this.zone = zone;
        this.memberType = memberType;
        this.directAuthority = directAuthority;
        this.folkGuideAbbr = folkGuideAbbr;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
        this.signUpStatus = signUpStatus;
        this.redFlagStatus = redFlagStatus;
        this.approveRequestTimeStamp = approveRequestTimeStamp;
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

    public String getFolkGuideAbbr() {
        return folkGuideAbbr;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
}
