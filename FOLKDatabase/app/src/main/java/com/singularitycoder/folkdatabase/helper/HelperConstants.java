package com.singularitycoder.folkdatabase.helper;

public class HelperConstants {

    // Firebase Firestore Auth Collections
    public static final String COLL_AUTH_FOLK_MEMBERS = "FolkMembers";
    public static final String COLL_AUTH_FOLK_GUIDES = "FolkGuides";
    public static final String COLL_AUTH_FOLK_TEAM_LEADS = "TeamLeads";
    public static final String COLL_AUTH_FOLK_ZONAL_HEADS = "ZonalHeads";
    public static final String COLL_AUTH_FOLK_APPROVE_MEMBERS = "ApproveMembers";
    public static final String COLL_AUTH_FOLK_APPROVE_FOLK_GUIDES = "ApproveFolkGuides";
    public static final String COLL_AUTH_FOLK_APPROVE_TEAM_LEADS = "ApproveTeamLeads";

    // Firebase Firestore Main Collections
    public static final String COLL_FOLK_NEW_MEMBERS = "Profile";

    // Firebase Storage
    public static final String DIR_FOLK_PROFILE_IMAGES_PATH = "ProfileImages/";
    public static final String DIR_IMAGES_PATH_FOLK_GUIDES = "FolkGuideProfileImages/";
    public static final String DIR_IMAGES_PATH_FOLK_TEAM_LEADS = "TeamLeadProfileImages/";
    public static final String DIR_IMAGES_PATH_FOLK_ZONAL_HEADS = "ZonalHeadProfileImages/";

    // FolkGuides Collection & TeamLeads Collection Keys
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FG_ABBR = "fg";
    public static final String KEY_FG_NAME = "fg_name";
    public static final String KEY_GMAIL = "gmail";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_TEAM_LEAD = "team_lead";
    public static final String KEY_ZONE = "zone";

    // FolkPeople, FolkGuides, TeamLeads, ZonalHeads Collections Keys
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


    // email, fg, fg_name, gmail, mobile_number, team_lead, telegram_group_id, zone
}
