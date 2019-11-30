package com.singularitycoder.folkdatabase.auth;

import com.google.firebase.firestore.Exclude;

public class AuthUserItem {

    @Exclude
    private String docId;
    // Make sure u use the same names as u provided in the Firebase. Same obj names
    private String zone;
    private String memberType;
    private String directAuthority;
    private String folkGuideAbbr;
    private String department;
    private String kcExperience;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String profileImageUrl;
    private String signUpStatus;
    private String creationTimeStamp;

    public AuthUserItem() {
    }

    public AuthUserItem(String zone, String memberType, String directAuthority, String folkGuideAbbr, String department, String kcExperience, String firstName, String lastName, String phone, String email, String password, String profileImageUrl, String signUpStatus, String creationTimeStamp) {
        this.zone = zone;
        this.memberType = memberType;
        this.directAuthority = directAuthority;
        this.folkGuideAbbr = folkGuideAbbr;
        this.department = department;
        this.kcExperience = kcExperience;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
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

    public String getFolkGuideAbbr() {
        return folkGuideAbbr;
    }

    public String getDepartment() {
        return department;
    }

    public String getKcExperience() {
        return kcExperience;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
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
