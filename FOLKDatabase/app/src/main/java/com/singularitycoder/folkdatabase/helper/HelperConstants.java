package com.singularitycoder.folkdatabase.helper;

public class HelperConstants {

    // Firebase Firestore Auth Collections
    public static final String AUTH_FOLK_PEOPLE = "AllFolkPeople";
    public static final String AUTH_FOLK_GUIDES = "AllFolkGuides";
    public static final String AUTH_FOLK_TEAM_LEADS = "AllTeamLeads";
    public static final String AUTH_FOLK_ZONAL_HEADS = "AllZonalHeads";
    public static final String AUTH_FOLK_GUIDE_APPROVALS = "AllFolkGuideApprovals";
    public static final String AUTH_FOLK_TEAM_LEAD_APPROVALS = "AllTeamLeadApprovals";

    // Firebase Firestore Main Collections
    public static final String FOLK_MEMBERS = "FolkMembers";
    public static final String FOLK_GUIDES = "FOLKGuides";
    public static final String TEAM_LEADS = "TeamLeads";

    // Firebase Storage
    public static final String FOLK_PROFILE_IMAGES_PATH = "ProfileImages/";

    // FolkGuides Collection & TeamLeads Collection Keys
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FG_ABBR = "fg";
    public static final String KEY_FG_NAME = "fg_name";
    public static final String KEY_GMAIL = "gmail";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_TEAM_LEAD = "team_lead";
    public static final String KEY_ZONE = "zone";

    // AllFolkPeople, AllFolkGuides, AllTeamLeads, AllZonalHeads Collections Keys
    public static final String KEY_AUTH_CREATION_TIME = "creationTimeStamp";
    public static final String KEY_AUTH_DEPARTMENT = "department";
    public static final String KEY_AUTH_DIRECT_AUTHORITY = "directAuthority";
    public static final String KEY_AUTH_FIRESTORE_DOC_ID = "docId";
    public static final String KEY_AUTH_EMAIL = "email";
    public static final String KEY_AUTH_FIRST_NAME = "firstName";
    public static final String KEY_AUTH_FOLK_GUIDE_ABBR = "folkGuideAbbr";
    public static final String KEY_AUTH_KC_EXPERIENCE = "kcExperience";
    public static final String KEY_AUTH_LAST_NAME = "lastName";
    public static final String KEY_AUTH_MEMBER_TYPE = "memberType";
    public static final String KEY_AUTH_PHONE = "phone";
    public static final String KEY_AUTH_PROFILE_IMAGE = "profileImageUrl";
    public static final String KEY_AUTH_SIGNUP_STATUS = "signUpStatus";
    public static final String KEY_AUTH_ZONE = "zone";
}
