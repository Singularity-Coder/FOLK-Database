package com.singularitycoder.folkdatabase.profile.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.singularitycoder.folkdatabase.auth.model.AuthUserItem;
import com.singularitycoder.folkdatabase.database.model.AllUsersItem;
import com.singularitycoder.folkdatabase.database.model.ContactItem;
import com.singularitycoder.folkdatabase.database.model.FolkGuideItem;
import com.singularitycoder.folkdatabase.database.model.TeamLeadItem;
import com.singularitycoder.folkdatabase.helper.RequestStateMediator;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.helper.Status;

import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;

public class ProfileRepository {

    private static final String TAG = "ProfileRepository";

    private static ProfileRepository _instance;

    public ProfileRepository() {
        // Initialize Firebase if necessary
    }

    public static ProfileRepository getInstance() {
        if (_instance == null) {
            _instance = new ProfileRepository();
        }
        return _instance;
    }

    // READ
    public MutableLiveData<RequestStateMediator> getAuthUserData(String email) {
        final MutableLiveData<RequestStateMediator> authUserLiveData = new MutableLiveData<>();
        RequestStateMediator requestStateMediator = new RequestStateMediator();

        requestStateMediator.set(null, Status.LOADING, "Please wait...", null);
        authUserLiveData.postValue(requestStateMediator);

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            AuthUserItem authUserItem = docSnap.toObject(AuthUserItem.class);
                            if (authUserItem != null) {
                                Log.d(TAG, "AuthItem: " + authUserItem);

                                // REAL
                                if (!("").equals(valueOf(docSnap.getString("shortName")))) {
                                    authUserItem.setShortName(valueOf(docSnap.getString("shortName")));
                                    Log.d(TAG, "readAuthUserData: shortName: " + valueOf(docSnap.getString("shortName")));
                                }
//
                                if (!("").equals(valueOf(docSnap.getString("fullName")))) {
                                    authUserItem.setFullName(valueOf(docSnap.getString("fullName")));
                                    Log.d(TAG, "readAuthUserData: fullname: " + valueOf(docSnap.getString("fullName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("profileImageUrl")))) {
                                    authUserItem.setProfileImageUrl(valueOf(docSnap.getString("profileImageUrl")));
                                    Log.d(TAG, "readAuthUserData: profilepic: " + valueOf(docSnap.getString("profileImageUrl")));
                                }

                                requestStateMediator.set(authUserLiveData, Status.SUCCESS, "Got Basic Info!", "AUTH USER");
                                authUserLiveData.postValue(requestStateMediator);
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                    } else {
                        requestStateMediator.set(null, Status.EMPTY, "Nothing!", null);
                        authUserLiveData.postValue(requestStateMediator);
                    }
                })
                .addOnFailureListener(e -> {
                    requestStateMediator.set(null, Status.ERROR, e.getMessage(), null);
                    authUserLiveData.postValue(requestStateMediator);
                });
        return authUserLiveData;
    }

    // READ
    public MutableLiveData<RequestStateMediator> getFolkGuideData(String email) {
        final MutableLiveData<RequestStateMediator> folkGuideLiveData = new MutableLiveData<>();
        RequestStateMediator requestStateMediator = new RequestStateMediator();

        requestStateMediator.set(null, Status.LOADING, "Please wait...", null);
        folkGuideLiveData.postValue(requestStateMediator);

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            FolkGuideItem folkGuideItem = docSnap.toObject(FolkGuideItem.class);
                            if (folkGuideItem != null) {
                                Log.d(TAG, "FolkGuideItem: " + folkGuideItem);

                                if (!("").equals(valueOf(docSnap.getString("shortName")))) {
                                    folkGuideItem.setStrFolkGuideShortName(valueOf(docSnap.getString("shortName")));
                                    Log.d(TAG, "readFolkGuideData: shortName: " + valueOf(docSnap.getString("shortName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("fullName")))) {
                                    folkGuideItem.setStrName(valueOf(docSnap.getString("fullName")));
                                    Log.d(TAG, "readFolkGuideData: fullname: " + valueOf(docSnap.getString("fullName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("profileImageUrl")))) {
                                    folkGuideItem.setStrProfileImage(valueOf(docSnap.getString("profileImageUrl")));
                                    Log.d(TAG, "readFolkGuideData: profilepic: " + valueOf(docSnap.getString("profileImageUrl")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("email")))) {
                                    folkGuideItem.setStrEmail(valueOf(docSnap.getString("email")));
                                    Log.d(TAG, "readFolkGuideData: email: " + valueOf(docSnap.getString("email")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("phone")))) {
                                    folkGuideItem.setStrPhone(valueOf(docSnap.getString("phone")));
                                    Log.d(TAG, "readFolkGuideData: phone: " + valueOf(docSnap.getString("phone")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("phone")))) {
                                    folkGuideItem.setStrWhatsApp(valueOf(docSnap.getString("phone")));
                                    Log.d(TAG, "readFolkGuideData: whatsapp: " + valueOf(docSnap.getString("phone")));
                                }

                                requestStateMediator.set(folkGuideItem, Status.SUCCESS, "Got Folk Guide Info!", "FOLK GUIDE");
                                folkGuideLiveData.postValue(requestStateMediator);
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                    } else {
                        requestStateMediator.set(null, Status.EMPTY, "Nothing!", null);
                        folkGuideLiveData.postValue(requestStateMediator);
                    }
                })
                .addOnFailureListener(e -> {
                    requestStateMediator.set(null, Status.ERROR, e.getMessage(), null);
                    folkGuideLiveData.postValue(requestStateMediator);
                });
        return folkGuideLiveData;
    }

    // READ
    public MutableLiveData<RequestStateMediator> getTeamLeadData(String email) {
        final MutableLiveData<RequestStateMediator> teamLeadLiveData = new MutableLiveData<>();
        RequestStateMediator requestStateMediator = new RequestStateMediator();

        requestStateMediator.set(null, Status.LOADING, "Please wait...", null);
        teamLeadLiveData.postValue(requestStateMediator);

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            TeamLeadItem teamLeadItem = docSnap.toObject(TeamLeadItem.class);
                            if (teamLeadItem != null) {
                                Log.d(TAG, "TeamLeadItem: " + teamLeadItem);

                                if (!("").equals(valueOf(docSnap.getString("shortName")))) {
                                    teamLeadItem.setStrTeamLeadShortName(valueOf(docSnap.getString("shortName")));
                                    Log.d(TAG, "readTeamLeadData: shortName: " + valueOf(docSnap.getString("shortName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("fullName")))) {
                                    teamLeadItem.setStrName(valueOf(docSnap.getString("fullName")));
                                    Log.d(TAG, "readTeamLeadData: fullname: " + valueOf(docSnap.getString("fullName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("profileImageUrl")))) {
                                    teamLeadItem.setStrProfileImage(valueOf(docSnap.getString("profileImageUrl")));
                                    Log.d(TAG, "readTeamLeadData: profilepic: " + valueOf(docSnap.getString("profileImageUrl")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("email")))) {
                                    teamLeadItem.setStrEmail(valueOf(docSnap.getString("email")));
                                    Log.d(TAG, "readFolkGuideData: email: " + valueOf(docSnap.getString("email")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("phone")))) {
                                    teamLeadItem.setStrPhone(valueOf(docSnap.getString("phone")));
                                    Log.d(TAG, "readFolkGuideData: phone: " + valueOf(docSnap.getString("phone")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("phone")))) {
                                    teamLeadItem.setStrWhatsApp(valueOf(docSnap.getString("phone")));
                                    Log.d(TAG, "readFolkGuideData: whatsapp: " + valueOf(docSnap.getString("phone")));
                                }

                                requestStateMediator.set(teamLeadItem, Status.SUCCESS, "Got Team Lead Info!", "TEAM LEAD");
                                teamLeadLiveData.postValue(requestStateMediator);
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                    } else {
                        requestStateMediator.set(null, Status.EMPTY, "Nothing!", null);
                        teamLeadLiveData.postValue(requestStateMediator);
                    }
                })
                .addOnFailureListener(e -> {
                    requestStateMediator.set(null, Status.ERROR, e.getMessage(), null);
                    teamLeadLiveData.postValue(requestStateMediator);
                });
        return teamLeadLiveData;
    }

    // READ
    public MutableLiveData<RequestStateMediator> getContactData(String email) {
        final MutableLiveData<RequestStateMediator> contactLiveData = new MutableLiveData<>();
        RequestStateMediator requestStateMediator = new RequestStateMediator();

        requestStateMediator.set(null, Status.LOADING, "Please wait...", null);
        contactLiveData.postValue(requestStateMediator);

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_FOLK_NEW_MEMBERS)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            ContactItem contactItem = docSnap.toObject(ContactItem.class);
                            if (contactItem != null) {
                                Log.d(TAG, "ContactItem: " + contactItem);

                                if (!("").equals(valueOf(docSnap.getString("folk_guide")))) {
                                    contactItem.setStrFolkGuide(valueOf(docSnap.getString("folk_guide")));
                                    Log.d(TAG, "readContactData: folkGuide: " + valueOf(docSnap.getString("folk_guide")));
                                } else {
                                    contactItem.setStrFolkGuide("No Data");
                                }

                                if (!("").equals(valueOf(docSnap.getString("name")))) {
                                    contactItem.setStrName(valueOf(docSnap.getString("name")));
                                    Log.d(TAG, "readContactData: name: " + valueOf(docSnap.getString("name")));
                                } else {
                                    contactItem.setStrName("No Data");
                                }

                                if (!("").equals(valueOf(docSnap.getString("email")))) {
                                    contactItem.setStrEmail(valueOf(docSnap.getString("email")));
                                    Log.d(TAG, "readFolkGuideData: email: " + valueOf(docSnap.getString("email")));
                                } else {
                                    contactItem.setStrEmail("No Data");
                                }

                                if (!("").equals(valueOf(docSnap.getString("mobile")))) {
                                    contactItem.setStrPhone(valueOf(docSnap.getString("mobile")));
                                    Log.d(TAG, "readFolkGuideData: phone: " + valueOf(docSnap.getString("phone")));
                                } else {
                                    contactItem.setStrPhone("0000000000");
                                }

                                if (!("").equals(valueOf(docSnap.getString("whatsapp")))) {
                                    contactItem.setStrWhatsApp(valueOf(docSnap.getString("whatsapp")));
                                    Log.d(TAG, "readFolkGuideData: whatsapp: " + valueOf(docSnap.getString("phone")));
                                } else {
                                    contactItem.setStrWhatsApp("0000000000");
                                }

                                HashMap<String, String> imageData = new HashMap<>();
                                if (null != docSnap.get("docs")) {
                                    imageData = (HashMap<String, String>) docSnap.get("docs");
                                    if (!("").equals(imageData.get("doc_url"))) {
                                        contactItem.setStrDocumentImage(imageData.get("doc_url"));
                                    } else {
                                        contactItem.setStrDocumentImage(imageData.get(""));
                                    }

                                    if (!("").equals(imageData.get("photo_url"))) {
                                        contactItem.setStrProfileImage(imageData.get("photo_url"));
                                    } else {
                                        contactItem.setStrProfileImage(imageData.get(""));
                                    }
                                } else {
                                    contactItem.setStrDocumentImage(imageData.get(""));
                                    contactItem.setStrProfileImage(imageData.get(""));
                                }

                                requestStateMediator.set(contactItem, Status.SUCCESS, "Got Contact Info!", "CONTACT");
                                contactLiveData.postValue(requestStateMediator);
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                    } else {
                        requestStateMediator.set(null, Status.EMPTY, "Nothing!", null);
                        contactLiveData.postValue(requestStateMediator);
                    }
                })
                .addOnFailureListener(e -> {
                    requestStateMediator.set(null, Status.ERROR, e.getMessage(), null);
                    contactLiveData.postValue(requestStateMediator);
                });
        return contactLiveData;
    }

    // READ
    public MutableLiveData<RequestStateMediator> getAllUsersData(String email) {
        final MutableLiveData<RequestStateMediator> allUsersLiveData = new MutableLiveData<>();
        RequestStateMediator requestStateMediator = new RequestStateMediator();

        requestStateMediator.set(null, Status.LOADING, "Please wait...", null);
        allUsersLiveData.postValue(requestStateMediator);

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            AllUsersItem allUsersItem = docSnap.toObject(AllUsersItem.class);
                            if (null != allUsersItem) {
                                Log.d(TAG, "AllUsersItem: " + allUsersItem);

                                // REAL
                                if (!("").equals(valueOf(docSnap.getString("email")))) {
                                    allUsersItem.setStrEmail(valueOf(docSnap.getString("email")));
                                    Log.d(TAG, "readAllUsersData: email: " + valueOf(docSnap.getString("email")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("phone")))) {
                                    allUsersItem.setStrPhone(valueOf(docSnap.getString("phone")));
                                    Log.d(TAG, "readAllUsersData: phone: " + valueOf(docSnap.getString("phone")));
                                }
//
                                if (!("").equals(valueOf(docSnap.getString("fullName")))) {
                                    allUsersItem.setStrFirstName(valueOf(docSnap.getString("fullName")));
                                    Log.d(TAG, "readAllUsersData: fullname: " + valueOf(docSnap.getString("fullName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("profileImageUrl")))) {
                                    allUsersItem.setStrProfileImage(valueOf(docSnap.getString("profileImageUrl")));
                                    Log.d(TAG, "readAllUsersData: profilepic: " + valueOf(docSnap.getString("profileImageUrl")));
                                }

                                requestStateMediator.set(allUsersItem, Status.SUCCESS, "Got All Users Info!", "ALL USERS");
                                allUsersLiveData.postValue(requestStateMediator);
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                    } else {
                        requestStateMediator.set(null, Status.EMPTY, "Nothing!", null);
                        allUsersLiveData.postValue(requestStateMediator);
                    }
                })
                .addOnFailureListener(e -> {
                    requestStateMediator.set(null, Status.ERROR, e.getMessage(), null);
                    allUsersLiveData.postValue(requestStateMediator);
                });
        return allUsersLiveData;
    }

    // READ
    public MutableLiveData<RequestStateMediator> getBasicInfo(String emailKey) {
        final MutableLiveData<RequestStateMediator> basicInfoLiveData = new MutableLiveData<>();
        RequestStateMediator requestStateMediator = new RequestStateMediator();

        requestStateMediator.set(null, Status.LOADING, "Please wait...", null);
        basicInfoLiveData.postValue(requestStateMediator);

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", emailKey)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            AuthUserItem authUserItem = docSnap.toObject(AuthUserItem.class);
                            if (null != authUserItem) {
                                Log.d(TAG, "AuthUserItem: " + authUserItem);

                                if (!("").equals(valueOf(docSnap.getString("email")))) {
                                    authUserItem.setEmail(valueOf(docSnap.getString("email")));
                                    Log.d(TAG, "readBasicData: email: " + valueOf(docSnap.getString("email")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("fullName")))) {
                                    authUserItem.setFullName(valueOf(docSnap.getString("fullName")));
                                    Log.d(TAG, "readBasicData: fullname: " + valueOf(docSnap.getString("fullName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("gmail")))) {
                                    authUserItem.setGmail(valueOf(docSnap.getString("gmail")));
                                    Log.d(TAG, "readBasicData: gmail: " + valueOf(docSnap.getString("gmail")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("phone")))) {
                                    authUserItem.setPhone(valueOf(docSnap.getString("phone")));
                                    Log.d(TAG, "readBasicData: phone: " + valueOf(docSnap.getString("phone")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("creationTimeStamp")))) {
                                    authUserItem.setCreationTimeStamp(docSnap.getString("creationTimeStamp"));
                                    Log.d(TAG, "readBasicData: creationTimeStamp: " + valueOf(docSnap.getString("creationTimeStamp")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("hkmJoiningDate")))) {
                                    authUserItem.setHkmJoiningDate(valueOf(docSnap.getString("hkmJoiningDate")));
                                    Log.d(TAG, "readBasicData: hkmJoiningDate: " + valueOf(docSnap.getString("hkmJoiningDate")));
                                }

                                requestStateMediator.set(authUserItem, Status.SUCCESS, "Got Basic Info!", "BASIC INFO");
                                basicInfoLiveData.postValue(requestStateMediator);
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                    } else {
                        requestStateMediator.set(null, Status.EMPTY, "Nothing!", null);
                        basicInfoLiveData.postValue(requestStateMediator);
                    }
                })
                .addOnFailureListener(e -> {
                    requestStateMediator.set(null, Status.ERROR, e.getMessage(), null);
                    basicInfoLiveData.postValue(requestStateMediator);
                });
        return basicInfoLiveData;
    }

    // READ
//    public Observable<AllCallbacks> getBasicInfoObservable(String emailKey) {
//        final MutableLiveData<AllCallbacks> basicInfoLiveData = new MutableLiveData<>();
//        AllCallbacks allCallbacks = new AllCallbacks();
//
//        allCallbacks.set(null, Status.LOADING, "Please wait...");
//        basicInfoLiveData.postValue(allCallbacks);
//
//        FirebaseFirestore.getInstance()
//                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
//                .whereEqualTo("email", emailKey)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//
//                    if (!queryDocumentSnapshots.isEmpty()) {
//                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
//                        Log.d(TAG, "docList: " + docList);
//
//                        for (DocumentSnapshot docSnap : docList) {
//                            AuthUserItem authUserItem = docSnap.toObject(AuthUserItem.class);
//                            if (null != authUserItem) {
//                                Log.d(TAG, "AuthUserItem: " + authUserItem);
//
//                                if (!("").equals(valueOf(docSnap.getString("email")))) {
//                                    authUserItem.setEmail(valueOf(docSnap.getString("email")));
//                                    Log.d(TAG, "readBasicData: email: " + valueOf(docSnap.getString("email")));
//                                }
//
//                                if (!("").equals(valueOf(docSnap.getString("fullName")))) {
//                                    authUserItem.setFullName(valueOf(docSnap.getString("fullName")));
//                                    Log.d(TAG, "readBasicData: fullname: " + valueOf(docSnap.getString("fullName")));
//                                }
//
//                                if (!("").equals(valueOf(docSnap.getString("gmail")))) {
//                                    authUserItem.setGmail(valueOf(docSnap.getString("gmail")));
//                                    Log.d(TAG, "readBasicData: gmail: " + valueOf(docSnap.getString("gmail")));
//                                }
//
//                                if (!("").equals(valueOf(docSnap.getString("phone")))) {
//                                    authUserItem.setPhone(valueOf(docSnap.getString("phone")));
//                                    Log.d(TAG, "readBasicData: phone: " + valueOf(docSnap.getString("phone")));
//                                }
//
//                                if (!("").equals(valueOf(docSnap.getString("creationTimeStamp")))) {
//                                    authUserItem.setCreationTimeStamp(docSnap.getString("creationTimeStamp"));
//                                    Log.d(TAG, "readBasicData: creationTimeStamp: " + valueOf(docSnap.getString("creationTimeStamp")));
//                                }
//
//                                if (!("").equals(valueOf(docSnap.getString("hkmJoiningDate")))) {
//                                    authUserItem.setHkmJoiningDate(valueOf(docSnap.getString("hkmJoiningDate")));
//                                    Log.d(TAG, "readBasicData: hkmJoiningDate: " + valueOf(docSnap.getString("hkmJoiningDate")));
//                                }
//
//                                allCallbacks.set(authUserItem, Status.SUCCESS, "Got Basic Info!");
//                                basicInfoLiveData.postValue(allCallbacks);
//                            }
//                            Log.d(TAG, "firedoc id: " + docSnap.getId());
//                        }
//                    } else {
//                        allCallbacks.set(null, Status.EMPTY, "Nothing!");
//                        basicInfoLiveData.postValue(allCallbacks);
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    allCallbacks.set(null, Status.ERROR, e.getMessage());
//                    basicInfoLiveData.postValue(allCallbacks);
//                });
//        return basicInfoLiveData;
//    }
}
