package com.singularitycoder.folkdatabase.profile.repository;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.model.AuthUserItem;
import com.singularitycoder.folkdatabase.database.model.AllUsersItem;
import com.singularitycoder.folkdatabase.database.model.ContactItem;
import com.singularitycoder.folkdatabase.database.model.FolkGuideItem;
import com.singularitycoder.folkdatabase.database.model.TeamLeadItem;
import com.singularitycoder.folkdatabase.helper.AllCallbacks;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.helper.Status;
import com.singularitycoder.folkdatabase.profile.view.ProfileActivity;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

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
    public MutableLiveData<AllCallbacks> getAllUsersData(String email) {
        final MutableLiveData<AllCallbacks> allUsersLiveData = new MutableLiveData<>();
        AllCallbacks allCallbacks = new AllCallbacks();

        allCallbacks.set(null, Status.LOADING, "Please wait...");
        allUsersLiveData.postValue(allCallbacks);

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

                                allCallbacks.set(allUsersItem, Status.SUCCESS, "Got Basic Info!");
                                allUsersLiveData.postValue(allCallbacks);
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                    } else {
                        allCallbacks.set(null, Status.EMPTY, "Nothing!");
                        allUsersLiveData.postValue(allCallbacks);
                    }
                })
                .addOnFailureListener(e -> {
                    allCallbacks.set(null, Status.ERROR, e.getMessage());
                    allUsersLiveData.postValue(allCallbacks);
                });
        return allUsersLiveData;
    }

    // READ
    public MutableLiveData<AllCallbacks> getBasicInfo(String emailKey) {
        final MutableLiveData<AllCallbacks> basicInfoLiveData = new MutableLiveData<>();
        AllCallbacks allCallbacks = new AllCallbacks();

        allCallbacks.set(null, Status.LOADING, "Please wait...");
        basicInfoLiveData.postValue(allCallbacks);

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

                                allCallbacks.set(authUserItem, Status.SUCCESS, "Got Basic Info!");
                                basicInfoLiveData.postValue(allCallbacks);
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                    } else {
                        allCallbacks.set(null, Status.EMPTY, "Nothing!");
                        basicInfoLiveData.postValue(allCallbacks);
                    }
                })
                .addOnFailureListener(e -> {
                    allCallbacks.set(null, Status.ERROR, e.getMessage());
                    basicInfoLiveData.postValue(allCallbacks);
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
