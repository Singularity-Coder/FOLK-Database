package com.singularitycoder.folkdatabase.home.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.perf.metrics.AddTrace;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.view.AuthApprovalStatusActivity;
import com.singularitycoder.folkdatabase.auth.model.AuthUserApprovalItem;
import com.singularitycoder.folkdatabase.auth.model.AuthUserItem;
import com.singularitycoder.folkdatabase.auth.view.MainActivity;
import com.singularitycoder.folkdatabase.database.model.ContactItem;
import com.singularitycoder.folkdatabase.database.view.DatabaseActivity;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.helper.CustomEditText;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.helper.HelperSharedPreference;
import com.singularitycoder.folkdatabase.home.adapter.ApproveUsersAdapter;
import com.singularitycoder.folkdatabase.home.adapter.HomeAdapter;
import com.singularitycoder.folkdatabase.home.model.HomeItem;
import com.singularitycoder.folkdatabase.home.adapter.NotificationAdapter;
import com.singularitycoder.folkdatabase.profile.view.ProfileActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.singularitycoder.folkdatabase.helper.FolkDatabaseApp.hasInternet;
import static java.lang.String.valueOf;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private final ArrayList<HomeItem> homeList = new ArrayList<>();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private String strOldPassword;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private AuthUserItem authUserItem;
    private ContactItem personItemModel;
    private AuthUserApprovalItem authUserApprovalItem;
    private ArrayList<ContactItem> contactList;
    private FirebaseFirestore firestore;
    private String collectionName;
    private String approveCollectionName;
    private String approveAllMembersCollectionName;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean setSignUpStatus = true;
    private boolean setRedFlagStatus = true;
    private HelperSharedPreference helperSharedPreference;
    private HelperGeneral helperObject = new HelperGeneral();

    // this listener is called when there is change in firebase fireUser session
    private FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            // fireUser fireAuth state is changed - fireUser is null launch login activity
            startActivity(new Intent(this, MainActivity.class));
            Objects.requireNonNull(this).finish();
        } else {
            Toast.makeText(this, "AuthUserItem: " + user.getEmail(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    @AddTrace(name = "onCreateTrace", enabled = true)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helperObject.setStatuBarColor(this, R.color.colorPrimaryDark);
        setContentView(R.layout.activity_home);
        init();
        authCheck();
        initToolBar();
        if (hasInternet()) AsyncTask.execute(this::readSignUpStatus);
        setRefreshLayout();
//        setUpRecyclerView();
//        findBirthdays(HelperGeneral.currentDate());

    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        helperSharedPreference = HelperSharedPreference.getInstance(this);
        loadingBar = new ProgressDialog(this);
        contactList = new ArrayList<>();

        swipeRefreshLayout = findViewById(R.id.refresh_layout);
    }

    private void authCheck() {
        authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // fireUser fireAuth state is changed - fireUser is null launch login activity
                startActivity(new Intent(this, MainActivity.class));
                Objects.requireNonNull(this).finish();
            }
        };
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("FOLK Home");
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_black_24dp));
    }

    private void setRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getData(this);
        });
    }

    private void getData(final Context context) {
        if (hasInternet()) {
            AsyncTask.execute(this::readAuthUserData);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void findBirthdays(String text) {
        ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

        Log.d(TAG, "findBirthdays: bd 1");
        for (ContactItem contact : contactList) {
            Log.d(TAG, "findBirthdays: bd 2");

            if (text.toLowerCase().trim().contains(valueOf(contact.getStrDobMonth()).toLowerCase().trim())) {
                Log.d(TAG, "findBirthdays: bd 3");

                filteredContactsList.add(contact);
                Log.d(TAG, "birthday list: " + filteredContactsList);
                Toast.makeText(HomeActivity.this, "got a match", Toast.LENGTH_SHORT).show();

                // send notification that many number of times as the lsit loops
            }
//                contactsAdapter.flterList(filteredContactsList);
//                contactsAdapter.notifyDataSetChanged();
//                tvListCount.setText(filteredContactsList.size() + " Contacts");
        }
    }

    // READ - checks signup status of user
    private void readSignUpStatus() {
        runOnUiThread(() -> {
            loadingBar.setMessage("Just a moment...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        });

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", helperSharedPreference.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            authUserItem = docSnap.toObject(AuthUserItem.class);
                            if (authUserItem != null) {
                                Log.d(TAG, "AuthItem: " + authUserItem);

                                if (!("").equals(valueOf(docSnap.getString("signUpStatus")))) {
                                    authUserItem.setSignUpStatus(valueOf(docSnap.getString("signUpStatus")));

                                    if (("false").equals(docSnap.getString("signUpStatus"))) {
                                        runOnUiThread(() -> loadingBar.dismiss());
                                        helperSharedPreference.setSignupStatus("false");
                                        helperSharedPreference.setFullName(helperSharedPreference.getFullName());
                                        helperSharedPreference.setDirectAuthority(helperSharedPreference.getDirectAuthority());
                                        helperSharedPreference.setMemberType(helperSharedPreference.getMemberType());
                                        helperSharedPreference.setUserShortName(helperSharedPreference.getUserShortName());
                                        helperSharedPreference.setZone(helperSharedPreference.getZone());
                                        startActivity(new Intent(this, AuthApprovalStatusActivity.class));
                                        finish();
                                    } else {
                                        runOnUiThread(() -> loadingBar.dismiss());
                                        AsyncTask.execute(this::readAuthUserData);
                                        AsyncTask.execute(this::readContactsData);
                                    }
                                }

                                authUserItem.setDocId(docSnap.getId());
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        Toast.makeText(this, "Got Data", Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> loadingBar.dismiss());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Couldn't get data!", Toast.LENGTH_SHORT).show();
                    runOnUiThread(() -> loadingBar.dismiss());
                });
    }


    // READ
    private void readContactsData() {
        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_FOLK_NEW_MEMBERS)
                .whereEqualTo("folk_guide", helperSharedPreference.getUserShortName())
                .whereEqualTo("zone", helperSharedPreference.getZone())
                .get()
//            FirebaseFirestore.getInstance().collection(HelperConstants.COLL_FOLK_NEW_MEMBERS).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        Log.d(TAG, "readContactsData: hittt 1");
                        for (DocumentSnapshot docSnap : docList) {
                            personItemModel = docSnap.toObject(ContactItem.class);
                            if (personItemModel != null) {
                                Log.d(TAG, "readContactsData: hittt 2");
                                Log.d(TAG, "personItem: " + personItemModel);

                                if (!("").equals(valueOf(docSnap.getId()))) {
                                    personItemModel.setId(docSnap.getId());
                                }

                                if (!("").equals(valueOf(docSnap.getString("name")))) {
                                    personItemModel.setStrName(valueOf(docSnap.getString("name")));
                                }

                                if (("").equals(valueOf(docSnap.getString("folk_guide")))) {
                                    personItemModel.setStrFolkGuide("No FOLK Guide");
                                } else {
                                    personItemModel.setStrFolkGuide(valueOf(docSnap.getString("folk_guide")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("occupation")))) {
                                    personItemModel.setStrOccupation(docSnap.getString("occupation"));
                                }

                                if (("").equals(docSnap.getString("dob_month"))) {
                                    personItemModel.setStrDobMonth(docSnap.getString("0"));
                                } else {
                                    personItemModel.setStrDobMonth(valueOf(docSnap.getString("dob_month")));
                                    Log.d(TAG, "dob month: " + docSnap.getString("dob_month"));
                                }

                                if (("").equals(docSnap.getString("dob"))) {
                                    personItemModel.setStrBirthday("Missing Birthday");
                                } else {
                                    personItemModel.setStrBirthday(docSnap.getString("dob"));
                                }

                                if (!("").equals(docSnap.getString("city"))) {
                                    personItemModel.setStrLocation(docSnap.getString("city"));
                                }

                                if (!("").equals(docSnap.getString("folk_residency_interest"))) {
                                    personItemModel.setStrResidencyInterest(valueOf(docSnap.getString("folk_residency_interest")));
                                }

                                if (!("").equals(docSnap.getString("mobile"))) {
                                    personItemModel.setStrPhone(valueOf(docSnap.getString("mobile")));
                                }

                                if (!("").equals(docSnap.getString("mobile"))) {
                                    personItemModel.setStrWhatsApp(valueOf(docSnap.getString("whatsapp")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("email")))) {
                                    personItemModel.setStrEmail(valueOf(docSnap.getString("email")));
                                }

                                Map<String, Object> talent = (Map<String, Object>) docSnap.getData().get("talent");
                                Log.d(TAG, "readContactsData: talent map: " + talent);

//                                    // Cooking
//                                    Map<String, Object> cooking = (Map<String, Object>) talent.get("cooking");
//                                    if (!("").equals(valueOf(talent.get("disclose")))) {
//                                        personItemModel.setStrTalentDisclose(valueOf(talent.get("disclose")));
//                                    }
//
//                                    if (!("").equals(valueOf(Objects.requireNonNull(cooking).get("can_cook_for")))) {
//                                        personItemModel.setStrCanCookFor(valueOf(cooking.get("can_cook_for")));
//                                    }
//
//                                    if (!("").equals(valueOf(cooking.get("cooking_self_rating")))) {
//                                        personItemModel.setStrSelfRating(valueOf(cooking.get("cooking_self_rating")));
//                                    }
//
//                                    Map<String, Object> cookingSkills = (Map<String, Object>) cooking.get("skills");
//
//                                    if (!("").equals(valueOf(cookingSkills.get("south_indian")))) {
//                                        personItemModel.setStrCanCookSouthIndian(valueOf(cookingSkills.get("south_indian")));
//                                    }
//
//                                    // Sports
//                                    Map<String, Object> sports = (Map<String, Object>) talent.get("sports_talent");
//                                    Map<String, Object> sportsParticipation = (Map<String, Object>) sports.get("participation");
//
//                                    if (!("").equals(valueOf(sportsParticipation.get("college_level")))) {
//                                        personItemModel.setStrCollegeLevel(valueOf(sportsParticipation.get("college_level")));
//                                    }
//
//                                    if (!("").equals(valueOf(sportsParticipation.get("college_level")))) {
//                                        personItemModel.setStrDistrictLevel(valueOf(sportsParticipation.get("college_level")));
//                                    }


//                                    for (Map.Entry<String, Object> entry : talent.entrySet()) {
//                                        Log.d(TAG, "Key: " + entry.getKey() + "\n Value: " + entry.getValue());
//                                        Map<String, Object> cooking2 = (Map<String, Object>) entry;
//                                        if (("disclose").equals(entry.getKey())) {
//                                            personItemModel.setStrTalentDisclose(entry.getKey());
//                                        }
//
//                                    }

//                                    // Send to Profile Talent
//                                    SharedPreferences spTalent = Objects.requireNonNull(getActivity()).getSharedPreferences("talentItem", Context.MODE_PRIVATE);
//                                    spTalent.edit().putString("canCook", valueOf(cooking.get("can_cook_for"))).apply();
//                                    spTalent.edit().putString("disclose", valueOf(talent.get("disclose"))).apply();
//                                    spTalent.edit().putString("cookingSelfRating", valueOf(cooking.get("cooking_self_rating"))).apply();
//                                    spTalent.edit().putString("canCookSouthIndian", valueOf(cookingSkills.get("south_indian"))).apply();
//                                    spTalent.edit().putString("sportsCollegeLevel", valueOf(sportsParticipation.get("college_level"))).apply();
//                                    spTalent.edit().putString("sportsDistrictLevel", valueOf(sportsParticipation.get("district_level"))).apply();

                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        Toast.makeText(HomeActivity.this, "Got Data", Toast.LENGTH_SHORT).show();
                    }
                    contactList.add(personItemModel);
                    Log.d(TAG, "readContactsData: contactList: " + contactList);
//                    findBirthdays("07-20");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Couldn't get data!", Toast.LENGTH_SHORT).show();
                });
    }


    // READ
    private void readAuthUserData() {
        // Main Shared Pref
        HelperSharedPreference helperSharedPreference = HelperSharedPreference.getInstance(this);
        String email = helperSharedPreference.getEmail();

//        SharedPreferences sp = getSharedPreferences("authItem", Context.MODE_PRIVATE);
//        String email = sp.getString("email", "");

        runOnUiThread(() -> {
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        });

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            authUserItem = docSnap.toObject(AuthUserItem.class);
                            if (authUserItem != null) {
                                Log.d(TAG, "AuthItem: " + authUserItem);

                                if (!("").equals(valueOf(docSnap.getString("fullName")))) {
                                    authUserItem.setFullName(valueOf(docSnap.getString("fullName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("profileImageUrl")))) {
                                    authUserItem.setProfileImageUrl(valueOf(docSnap.getString("profileImageUrl")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("zone")))) {
                                    authUserItem.setZone(valueOf(docSnap.getString("zone")));
                                    helperSharedPreference.setZone(valueOf(docSnap.getString("zone")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("shortName")))) {
                                    authUserItem.setShortName(valueOf(docSnap.getString("shortName")));
                                    helperSharedPreference.setUserShortName(valueOf(docSnap.getString("shortName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("memberType")))) {
                                    authUserItem.setMemberType(valueOf(docSnap.getString("memberType")));
                                    helperSharedPreference.setMemberType(valueOf(docSnap.getString("memberType")));
                                }

                                Map<String, Object> talent = (Map<String, Object>) docSnap.getData().get("talent");
                                authUserItem.setDocId(docSnap.getId());
                                Log.d(TAG, "readContactsData: talent map: " + talent);
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        Toast.makeText(HomeActivity.this, "Got Data", Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> {
                            loadingBar.dismiss();
                            setUpRecyclerView();
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Couldn't get data!", Toast.LENGTH_SHORT).show();
                    runOnUiThread(() -> loadingBar.dismiss());
                });
    }


    private void setUpRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerView = findViewById(R.id.recycler_home);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        homeList.add(new HomeItem(authUserItem.getProfileImageUrl(), authUserItem.getFullName()));
        homeList.add(new HomeItem(R.drawable.ic_accomodation3, "Accomodation", ""));
        homeList.add(new HomeItem(R.drawable.ic_database3, "Database", ""));
        homeList.add(new HomeItem(R.drawable.ic_posting3, "Posting", ""));
        homeList.add(new HomeItem(R.drawable.ic_service3, "Service", ""));
        homeList.add(new HomeItem(R.drawable.ic_payments3, "Payments", ""));
        homeList.add(new HomeItem(R.drawable.ic_prasadum3, "Prasad Coupon", ""));

        homeAdapter = new HomeAdapter(homeList, this);
        homeAdapter.setHasStableIds(true);
        homeAdapter.setOnItemClickListener((view, position) -> {
            if (position == 0) {
                new HelperGeneral().dialogActionMessage(HomeActivity.this, null, "Coming Soon", "OK", "", null, null, true);
            }

            if (position == 1) {
                new HelperGeneral().dialogActionMessage(HomeActivity.this, null, "Coming Soon", "OK", "", null, null, true);
            }

            if (position == 2) {
                startActivity(new Intent(HomeActivity.this, DatabaseActivity.class));
            }

            if (position == 3) {
                new HelperGeneral().dialogActionMessage(HomeActivity.this, null, "Coming Soon", "OK", "", null, null, true);
            }

            if (position == 4) {
                new HelperGeneral().dialogActionMessage(HomeActivity.this, null, "Coming Soon", "OK", "", null, null, true);
            }

            if (position == 5) {
                new HelperGeneral().dialogActionMessage(HomeActivity.this, null, "Coming Soon", "OK", "", null, null, true);
            }

            if (position == 6) {
                new HelperGeneral().dialogActionMessage(HomeActivity.this, null, "Coming Soon", "OK", "", null, null, true);
            }
        });
        recyclerView.setAdapter(homeAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        SharedPreferences sp = getSharedPreferences("authItem", Context.MODE_PRIVATE);
        String memberType = sp.getString("memberType", "");
        if (memberType.equals("Folk Guide")) {
            MenuItem itemApproveUsers = findViewById(R.id.action_approve_users);
            itemApproveUsers.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_birthdays:
//                dialogNotifications(this);
                new HelperGeneral().dialogActionMessage(HomeActivity.this, null, "Birthdays is Coming Soon!", "OK", "", null, null, true);
                return true;
            case R.id.action_approve_users:
//                SharedPreferences sp = getSharedPreferences("authItem", Context.MODE_PRIVATE);
//                String memberType = sp.getString("memberType", "");
//                dialogApproveUsers(this, memberType);
                new HelperGeneral().dialogActionMessage(HomeActivity.this, null, "Approving users is Coming Soon!", "OK", "", null, null, true);
                return true;
            case R.id.action_my_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("profileKey", "AUTHUSER");
                startActivity(intent);
                return true;
            case R.id.action_about:
                aboutDialog(this);
                return true;
            case R.id.action_change_password:
                dialogChangePassword(this);
                return true;
            case R.id.action_delete_account:
                new HelperGeneral().dialogActionMessage(this, "Are you sure?", "You cannot undo this!", "Yes", "No", () -> deleteAccount(), null, true);
                return true;
            case R.id.action_log_out:
                new HelperGeneral().dialogActionMessage(this, null, "Do you want to Log Out?", "Yes", "No", () -> logOut(), null, true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void aboutDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        new HelperGeneral().dialogCustomBuild(activity, R.layout.dialog_about, dialog, true);

        ImageView ivLogo = dialog.findViewById(R.id.img_about_logo);
        TextView tvContactUs = dialog.findViewById(R.id.tv_contact_us);
        TextView tvRateUs = dialog.findViewById(R.id.tv_rate_us);
        TextView tvVolunteer = dialog.findViewById(R.id.tv_volunteer_appdev);
        TextView tvDedicated = dialog.findViewById(R.id.tv_dedicated_to);
        TextView tvShareApk = dialog.findViewById(R.id.tv_share_app_apk);
        TextView tvShareLink = dialog.findViewById(R.id.tv_share_app_link);

        tvContactUs.setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "name@emailaddress.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Feedback, Help, Report Bugs etc.");
            activity.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });
        tvRateUs.setOnClickListener(view -> activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.srilaprabhupadalila&hl=en"))));
        tvVolunteer.setOnClickListener(view -> activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/WCBV2q4b1ZBgDf3B9"))));
        tvDedicated.setOnClickListener(view -> activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.srilaprabhupadalila.org/who-is-srila-prabhupada"))));
        tvShareApk.setOnClickListener(view -> {
            try {
                helperObject.shareApp(HomeActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tvShareLink.setOnClickListener(view -> helperObject.shareData(activity, getResources().getDrawable(R.drawable.logo192), ivLogo, "FOLK Database App", "Get the FOLK Database App from Playstore!"));
        dialog.show();
    }


    private void dialogNotifications(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        new HelperGeneral().dialogCustomBuild(activity, R.layout.dialog_notifications, dialog, true);

        // if today's date matches birthday - show notif badge
        // Show all birthday's in the notifications

        ImageView imgCloseBtn = dialog.findViewById(R.id.img_dialog_close);
        imgCloseBtn.setOnClickListener(view -> dialog.dismiss());

        ArrayList<ContactItem> notificationList = new ArrayList<>();
        notificationList.add(new ContactItem("Michael Marvin", "", "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new ContactItem("Michael Marvin", "", "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new ContactItem("Michael Marvin", "", "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new ContactItem("Michael Marvin", "", "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new ContactItem("Michael Marvin", "", "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));

        LinearLayoutManager commentLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);

        RecyclerView notificationsRecycler = dialog.findViewById(R.id.dialog_notif_recycler);
        notificationsRecycler.setLayoutManager(commentLayoutManager);
        notificationsRecycler.setHasFixedSize(true);
        notificationsRecycler.setItemViewCacheSize(20);
        notificationsRecycler.setDrawingCacheEnabled(true);
        notificationsRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        NotificationAdapter notificationAdapter = new NotificationAdapter(notificationList, activity);
        notificationAdapter.setHasStableIds(true);

        notificationsRecycler.setAdapter(notificationAdapter);

        dialog.show();
    }


    // Get All sub collection details
    private void dialogApproveUsers(Activity activity, String memberType) {
        final Dialog dialog = new Dialog(activity);
        new HelperGeneral().dialogCustomBuild(activity, R.layout.dialog_approve_users, dialog, true);

        // if today's date matches birthday - show notif badge
        // Show all birthday's in the notifications

        ShimmerFrameLayout shimmerFrameLayout = dialog.findViewById(R.id.shimmer_view_container);
        LottieAnimationView noFeedImage = dialog.findViewById(R.id.img_no_feed_lottie_image);
        noFeedImage.setAnimation(R.raw.empty_box);
        noFeedImage.playAnimation();
        TextView noFeedText = dialog.findViewById(R.id.tv_no_feed_text);
        TextView tvListCount = dialog.findViewById(R.id.tv_list_count);
        ImageView imgCloseBtn = dialog.findViewById(R.id.img_dialog_close);
        imgCloseBtn.setOnClickListener(view -> {
            dialog.dismiss();
        });
        TextView noInternetText = dialog.findViewById(R.id.tv_no_internet);
        SwipeRefreshLayout swipeRefreshLayout = dialog.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        ArrayList<AuthUserApprovalItem> approveUsersList = new ArrayList<>();
        LinearLayoutManager commentLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        RecyclerView approveUsersRecycler = dialog.findViewById(R.id.dialog_approve_users_recycler);
        approveUsersRecycler.setLayoutManager(commentLayoutManager);
        approveUsersRecycler.setHasFixedSize(true);
        approveUsersRecycler.setItemViewCacheSize(20);
        approveUsersRecycler.setDrawingCacheEnabled(true);
        approveUsersRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        ApproveUsersAdapter approveUsersAdapter = new ApproveUsersAdapter(approveUsersList, activity);
        approveUsersAdapter.setHasStableIds(true);
        approveUsersRecycler.setAdapter(approveUsersAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            if (hasInternet()) {
                AsyncTask.execute(this::readContactsData);
                noInternetText.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.setVisibility(View.GONE);
                approveUsersAdapter.notifyDataSetChanged();
            } else {
                noInternetText.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });

        // READ - Read sub collection details
        SharedPreferences sp = getSharedPreferences("authItem", Context.MODE_PRIVATE);
        SharedPreferences approveMembersSharedPrefs = HomeActivity.this.getSharedPreferences("ApproveMember", Context.MODE_PRIVATE);

        String folkGuideAbbr = sp.getString("folkGuideAbbr", "");
        String zone = sp.getString("zone", "");

        if (memberType.equals("FOLK Guide")) {
            collectionName = HelperConstants.COLL_AUTH_FOLK_APPROVE_FOLK_GUIDES;
        }

        if (memberType.equals("Team Lead")) {
            collectionName = HelperConstants.COLL_AUTH_FOLK_APPROVE_TEAM_LEADS;
        }

        FirebaseFirestore
                .getInstance()
                .collection(collectionName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            authUserApprovalItem = docSnap.toObject(AuthUserApprovalItem.class);
                            if (authUserApprovalItem != null) {
                                Log.d(TAG, "authUserApproveItem: " + authUserApprovalItem);

                                if (!("").equals(valueOf(docSnap.getString("profileImageUrl")))) {
                                    authUserApprovalItem.setProfileImageUrl(valueOf(docSnap.getString("profileImageUrl")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("fullName")))) {
                                    authUserApprovalItem.setFullName(valueOf(docSnap.getString("fullName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("approveRequestTimeStamp")))) {
                                    authUserApprovalItem.setApproveRequestTimeStamp(valueOf(docSnap.getString("approveRequestTimeStamp")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("directAuthority")))) {
                                    authUserApprovalItem.setDirectAuthority(valueOf(docSnap.getString("directAuthority")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("docId")))) {
                                    authUserApprovalItem.setDocId(valueOf(docSnap.getString("docId")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("shortName")))) {
                                    authUserApprovalItem.setShortName(valueOf(docSnap.getString("shortName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("email")))) {
                                    authUserApprovalItem.setEmail(valueOf(docSnap.getString("email")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("memberType")))) {
                                    authUserApprovalItem.setMemberType(valueOf(docSnap.getString("memberType")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("redFlagStatus")))) {
                                    authUserApprovalItem.setRedFlagStatus(valueOf(docSnap.getString("redFlagStatus")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("signUpStatus")))) {
                                    authUserApprovalItem.setSignUpStatus(valueOf(docSnap.getString("signUpStatus")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("zone")))) {
                                    authUserApprovalItem.setZone(valueOf(docSnap.getString("zone")));
                                }
                            }
                            approveUsersList.add(authUserApprovalItem);
                            tvListCount.setText(approveUsersList.size() + " Members to Approve");
                        }

                        if (authUserApprovalItem.getMemberType().equals("FOLK Guide")) {
                            // go to folk guide and folk members coll
                            approveCollectionName = HelperConstants.COLL_AUTH_FOLK_GUIDES;
                            approveAllMembersCollectionName = HelperConstants.COLL_AUTH_FOLK_MEMBERS;
                        }

                        if (authUserApprovalItem.getMemberType().equals("Team Lead")) {
                            // go to team leads and folk members coll
                            approveCollectionName = HelperConstants.COLL_AUTH_FOLK_TEAM_LEADS;
                            approveAllMembersCollectionName = HelperConstants.COLL_AUTH_FOLK_MEMBERS;
                        }

                        Log.d(TAG, "dialogApproveUsers: approveCollName: " + approveCollectionName);
                        Log.d(TAG, "dialogApproveUsers: allApproveCollName: " + approveAllMembersCollectionName);

                        // APPROVE MEMBER
                        approveUsersAdapter.setOnApproveMemberClickedListener((view, position, button) -> {
                            // change signUpStatus = true
                            runOnUiThread(() -> {
                                loadingBar.setMessage("Please wait...");
                                loadingBar.setCanceledOnTouchOutside(false);
                                loadingBar.show();
                            });

                            approveMembersSharedPrefs.edit().putString("setSignUpStatus", valueOf(setSignUpStatus)).apply();

                            firestore
                                    .collection(approveCollectionName)
                                    .document(authUserItem.getDocId())
                                    .update("signUpStatus", valueOf(setSignUpStatus))
                                    .addOnSuccessListener(documentReference1 -> {
                                        if (valueOf(button.getText()).toLowerCase().equals("approve")) {
                                            button.setText("APPROVED");
                                            button.setTextColor(getResources().getColor(R.color.colorLightBlue));
                                            button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                            button.setElevation(1);
                                            setSignUpStatus = false;
                                            approveMembersSharedPrefs.edit().putString("setSignUpStatus", valueOf(setSignUpStatus)).apply();
                                        }

                                        if (valueOf(button.getText()).toLowerCase().equals("approved")) {
                                            button.setText("APPROVE");
                                            button.setTextColor(getResources().getColor(R.color.colorAccent));
                                            button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                            button.setElevation(1);
                                            approveMembersSharedPrefs.edit().putString("setSignUpStatus", valueOf(setSignUpStatus)).apply();
                                            setSignUpStatus = true;
                                        }
                                        loadingBar.dismiss();
                                        Toast.makeText(HomeActivity.this, "Item Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(HomeActivity.this, AuthApprovalStatusActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error adding document", e);
                                        loadingBar.dismiss();
                                        Toast.makeText(HomeActivity.this, "Failed to Update Item", Toast.LENGTH_SHORT).show();
                                    });
                        });

                        // REJECT MEMBER
                        approveUsersAdapter.setOnRejectMemberClickedListener((view, position, button) -> {
                            // change redFlagStatus = true
                            runOnUiThread(() -> {
                                loadingBar.setMessage("Please wait...");
                                loadingBar.setCanceledOnTouchOutside(false);
                                loadingBar.show();
                            });

                            firestore
                                    .collection(approveCollectionName)
                                    .document(authUserItem.getDocId())
                                    .update("redFlagStatus", valueOf(setRedFlagStatus))
                                    .addOnSuccessListener(documentReference1 -> {
                                        if (valueOf(button.getText()).toLowerCase().equals("reject")) {
                                            button.setText("REJECTED");
                                            button.setTextColor(getResources().getColor(R.color.colorLightRed));
                                            button.setBackgroundColor(getResources().getColor(R.color.colorRed));
                                            button.setElevation(1);
                                            approveMembersSharedPrefs.edit().putString("setRedFlagStatus", valueOf(setRedFlagStatus)).apply();
                                            setRedFlagStatus = false;
                                        }

                                        if (valueOf(button.getText()).toLowerCase().equals("rejected")) {
                                            button.setText("REJECT");
                                            button.setTextColor(getResources().getColor(R.color.colorRed));
                                            button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                            button.setElevation(1);
                                            approveMembersSharedPrefs.edit().putString("setRedFlagStatus", valueOf(setRedFlagStatus)).apply();
                                            setRedFlagStatus = true;
                                        }
                                        loadingBar.dismiss();
                                        Toast.makeText(HomeActivity.this, "Item Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(HomeActivity.this, AuthApprovalStatusActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error adding document", e);
                                        loadingBar.dismiss();
                                        Toast.makeText(HomeActivity.this, "Failed to Update Item", Toast.LENGTH_SHORT).show();
                                    });
                        });
                        approveUsersAdapter.notifyDataSetChanged();
                        Toast.makeText(HomeActivity.this, "Got Data", Toast.LENGTH_SHORT).show();

                        if (approveUsersList.size() == 0) {
                            noFeedImage.setVisibility(View.VISIBLE);
                            noFeedText.setVisibility(View.VISIBLE);
                        } else {
                            noFeedImage.setVisibility(View.GONE);
                            noFeedText.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Couldn't get data!", Toast.LENGTH_SHORT).show();
                    noFeedImage.setVisibility(View.VISIBLE);
                    noFeedText.setVisibility(View.VISIBLE);
                });

        dialog.show();
    }


    private void dialogChangePassword(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        new HelperGeneral().dialogCustomBuild(activity, R.layout.dialog_change_password, dialog, false);

        ImageView imgClose = dialog.findViewById(R.id.img_close);
        CustomEditText etOldPassword = dialog.findViewById(R.id.et_old_password);
        CustomEditText etNewPassword = dialog.findViewById(R.id.et_new_password);
        CustomEditText etNewPasswordAgain = dialog.findViewById(R.id.et_new_password_again);
        Button btnReset = dialog.findViewById(R.id.btn_change_password);

        imgClose.setOnClickListener(view -> dialog.dismiss());

        btnReset.setOnClickListener(view -> {
            if (hasValidInput(etOldPassword, etNewPassword, etNewPasswordAgain)) {
                // 1. Check old password matches or not
                // 2. Update in firestore new password value
                // 3. Update in auth new password value
                AsyncTask.execute(() -> changePassword(valueOf(etNewPassword.getText()).trim()));
            }
        });
        dialog.show();
    }


    private boolean hasValidInput(CustomEditText etOldPassword, CustomEditText etNewPassword, CustomEditText etNewPasswordAgain) {
        if (valueOf(etOldPassword.getText()).trim().equals("")) {
            etOldPassword.setError("Password is Required!");
            etOldPassword.requestFocus();
            return false;
        }

        if (!helperObject.hasValidPassword(valueOf(etOldPassword.getText()).trim())) {
            etOldPassword.setError("Password must have at least 8 characters with One Uppercase and One lower case. These Special Characters are allwoed .,#@-_+!?;':*");
            etOldPassword.requestFocus();
            return false;
        }

//            if (!valueOf(etOldPassword.getText()).trim().equals(oldPassword())) {
//                etOldPassword.setError("Wrong old password!");
//                etOldPassword.requestFocus();
//                return false;
//            }

        if (valueOf(etNewPassword.getText()).trim().equals("")) {
            etNewPassword.setError("Password is Required!");
            etNewPassword.requestFocus();
            return false;
        }

        if (!helperObject.hasValidPassword(valueOf(etNewPassword.getText()).trim())) {
            etNewPassword.setError("Password must have at least 8 characters with One Uppercase and One lower case. These Special Characters are allwoed .,#@-_+!?;':*");
            etNewPassword.requestFocus();
            return false;
        }

        if (valueOf(etNewPasswordAgain.getText()).trim().equals("")) {
            etNewPasswordAgain.setError("Password is Required!");
            etNewPasswordAgain.requestFocus();
            return false;
        }

        if (!helperObject.hasValidPassword(valueOf(etNewPasswordAgain.getText()).trim())) {
            etNewPasswordAgain.setError("Password must have at least 8 characters with One Uppercase and One lower case. These Special Characters are allwoed .,#@-_+!?;':*");
            etNewPasswordAgain.requestFocus();
            return false;
        }

        if (!valueOf(etNewPassword.getText()).trim().equals(valueOf(etNewPasswordAgain.getText()).trim())) {
            etNewPassword.setError("Password is not matching!");
            etNewPasswordAgain.setError("Password is not matching!");
            etNewPassword.requestFocus();
            etNewPasswordAgain.requestFocus();
        }

        return true;
    }


    private String oldPassword() {
        FirebaseFirestore
                .getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        strOldPassword = queryDocumentSnapshots.getDocuments().get(0).getString("password");
                        // store user doc id, name, pasword all basic details in shared prefs
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: got hit"));
        return strOldPassword;
    }


    private void changePassword(String newPassword) {
        runOnUiThread(() -> {
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        });

        if (firebaseUser != null) {
            firebaseUser.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(HomeActivity.this, "Password is updated! LogIn with new password!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "changePassword: docId: " + authUserItem.getDocId());

                            // Update existing values
                            firestore
                                    .collection(HelperConstants.COLL_AUTH_FOLK_GUIDES)
                                    .document(authUserItem.getDocId())
                                    .update("password", newPassword)
                                    .addOnSuccessListener(documentReference1 -> {
                                        Toast.makeText(HomeActivity.this, "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(HomeActivity.this, AuthApprovalStatusActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error adding document", e);
                                        Toast.makeText(HomeActivity.this, "Failed to create AuthUserItem", Toast.LENGTH_SHORT).show();
                                    });

                            logOut();
                            runOnUiThread(() -> loadingBar.dismiss());
                        } else {
                            Toast.makeText(HomeActivity.this, "Failed to update Password!", Toast.LENGTH_SHORT).show();
                            runOnUiThread(() -> loadingBar.dismiss());
                        }
                    });
        }
    }


    private Void deleteAccount() {
        AsyncTask.execute(() -> {
            runOnUiThread(() -> {
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            });
            if (firebaseUser != null) {
                firebaseUser.delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(HomeActivity.this, "Your profile is deleted :( Create an account now!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                                Objects.requireNonNull(HomeActivity.this).finish();
                                runOnUiThread(() -> loadingBar.dismiss());
                            } else {
                                Toast.makeText(HomeActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                runOnUiThread(() -> loadingBar.dismiss());
                            }
                        });
            }
        });
        return null;
    }


    private Void logOut() {
        AsyncTask.execute(() -> {
            firebaseAuth.signOut();
            // this listener will be called when there is change in firebase fireUser session
            FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // unload all shared prefs
                    helperSharedPreference.setSignupStatus("");
                    helperSharedPreference.setEmail("");
                    helperSharedPreference.setFullName("");
                    helperSharedPreference.setDirectAuthority("");
                    helperSharedPreference.setMemberType("");
                    helperSharedPreference.setUserShortName("");
                    helperSharedPreference.setZone("");
                    // fireUser fireAuth state is changed - fireUser is null launch login activity
                    startActivity(new Intent(HomeActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            };
        });
        return null;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (authListener != null) {
            firebaseAuth.addAuthStateListener(authListener);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            firebaseAuth.removeAuthStateListener(authListener);
        }
    }
}
