package com.singularitycoder.folkdatabase.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class HelperSharedPreference {
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_DOC_ID = "docId";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ZONE = "zone";
    private static final String KEY_USER_SHORT_NAME = "shortName";
    private static final String KEY_USER_MEMBER_TYPE = "memberType";
    private static final String KEY_USER_DIRECT_AUTHORITY = "directAuthority";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_PIC = "pic";

    private static final String KEY_SIGNUP_STATUS = "signUpStatus";

    private static HelperSharedPreference _instance;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;

    // Singleton
    public static HelperSharedPreference getInstance(Context context) {
        if (_instance == null) {
            _instance = new HelperSharedPreference();
            _instance.configSessionUtils(context);
        }
        return _instance;
    }

    // Singleton
    public static HelperSharedPreference instance() {
        return _instance;
    }

    private void configSessionUtils(Context context) {
        Context context1 = context;
        sharedPref = context.getSharedPreferences("AppPreferences", Activity.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.apply();
    }

    public void setUser(String uid, String email, String role, String userName, String pic, String mobile) {
        sharedPrefEditor.putString(KEY_DOC_ID, uid);
        sharedPrefEditor.putString(KEY_EMAIL, email);
        sharedPrefEditor.putString(KEY_ROLE, role);
        sharedPrefEditor.putString(KEY_USERNAME, userName);
        sharedPrefEditor.putString(KEY_PIC, pic);
        sharedPrefEditor.putString(KEY_MOBILE, mobile);
        sharedPrefEditor.commit();
    }

    public void setSignupStatus(String firebaseToken) {
        sharedPrefEditor.putString(KEY_SIGNUP_STATUS, firebaseToken);
        sharedPrefEditor.commit();
    }

    public String getSignupStatus() {
        return sharedPref.getString(KEY_SIGNUP_STATUS, "");
    }

    public void setEmail(String email) {
        sharedPrefEditor.putString(KEY_EMAIL, email);
        sharedPrefEditor.commit();
    }

    public String getEmail() {
        return sharedPref.getString(KEY_EMAIL, "");
    }

    public void setZone(String zone) {
        sharedPrefEditor.putString(KEY_ZONE, zone);
        sharedPrefEditor.commit();
    }

    public String getZone() {
        return sharedPref.getString(KEY_ZONE, "");
    }

    public void setUserShortName(String shortName) {
        sharedPrefEditor.putString(KEY_USER_SHORT_NAME, shortName);
        sharedPrefEditor.commit();
    }

    public String getUserShortName() {
        return sharedPref.getString(KEY_USER_SHORT_NAME, "");
    }

    public void setMemberType(String memberType) {
        sharedPrefEditor.putString(KEY_USER_MEMBER_TYPE, memberType);
        sharedPrefEditor.commit();
    }

    public String getMemberType() {
        return sharedPref.getString(KEY_USER_MEMBER_TYPE, "");
    }

    public void setDirectAuthority(String directAuthority) {
        sharedPrefEditor.putString(KEY_USER_DIRECT_AUTHORITY, directAuthority);
        sharedPrefEditor.commit();
    }

    public String getDirectAuthority() {
        return sharedPref.getString(KEY_USER_DIRECT_AUTHORITY, "");
    }

    public void setFullName(String fullName) {
        sharedPrefEditor.putString(KEY_FULL_NAME, fullName);
        sharedPrefEditor.commit();
    }

    public String getFullName() {
        return sharedPref.getString(KEY_FULL_NAME, "");
    }
}
