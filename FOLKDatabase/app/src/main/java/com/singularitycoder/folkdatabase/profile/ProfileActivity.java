package com.singularitycoder.folkdatabase.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.AuthUserItem;
import com.singularitycoder.folkdatabase.auth.MainActivity;
import com.singularitycoder.folkdatabase.database.AllUsersItem;
import com.singularitycoder.folkdatabase.database.ContactItem;
import com.singularitycoder.folkdatabase.database.FolkGuideItem;
import com.singularitycoder.folkdatabase.database.TeamLeadItem;
import com.singularitycoder.folkdatabase.database.ZonalHeadItem;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.helper.HelperFrescoImageViewer;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.helper.HelperSharedPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import static java.lang.String.valueOf;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CoordinatorLayout mCoordinatorLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private String profileKey;
    private ImageView ivProfileImage;
    private TextView tvName, tvSubTitle;
    private ConstraintLayout conLayProfileActions;
    private ViewPagerAdapter profileAdapter;
    private ProgressDialog loadingBar;
    private CoordinatorLayout coordinatorProfile;
    private HelperGeneral helperObject = new HelperGeneral();

    private AuthUserItem authUserItem = new AuthUserItem();
    private ContactItem contactItem = new ContactItem();
    private FolkGuideItem folkGuideItem = new FolkGuideItem();
    private TeamLeadItem teamLeadItem = new TeamLeadItem();
    private AllUsersItem allUsersItem = new AllUsersItem();
    private ZonalHeadItem zonalHeadItem = new ZonalHeadItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HelperGeneral().setStatuBarColor(this, R.color.colorPrimaryDark);
        setContentView(R.layout.activity_profile);
        inits();
        getIntentData();
//        setUpTabLayout();
//        setUpToolbar();
//        setUpAppbarLayout();
//        setUpCollapsingToolbar();
    }

    private void inits() {
        loadingBar = new ProgressDialog(this);
        profileAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        coordinatorProfile = findViewById(R.id.coordinator_profile);
        tabLayout = findViewById(R.id.tablayout_profile);
        viewPager = findViewById(R.id.viewpager_profile);
        ivProfileImage = findViewById(R.id.img_profile_header);
        tvName = findViewById(R.id.tv_main_title);
        tvSubTitle = findViewById(R.id.tv_main_subtitle);
        conLayProfileActions = findViewById(R.id.con_lay_profile_action_icons);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        profileKey = intent.getStringExtra("profileKey");
        Log.d(TAG, "getIntentData: profileKey: " + profileKey);
        if (("AUTHUSER").equals(profileKey)) {
            // Main Shared Pref
            HelperSharedPreference helperSharedPreference = HelperSharedPreference.getInstance(this);
            String email = helperSharedPreference.getEmail();
            AsyncTask.execute(() -> readAuthUserData(email));
        }

        if (("FOLKGUIDE").equals(profileKey)) {
//            folkGuideItem = (FolkGuideItem) intent.getSerializableExtra("folkguideItem");
            String email = intent.getStringExtra("email");
            AsyncTask.execute(() -> readFolkGuideData(email));
        }

        if (("TEAMLEAD").equals(profileKey)) {
//            teamLeadItem = (TeamLeadItem) intent.getSerializableExtra("teamleadItem");
            String email = intent.getStringExtra("email");
            AsyncTask.execute(() -> readTeamLeadData(email));
        }

        if (("CONTACT").equals(profileKey)) {
//            contactItem = (ContactItem) intent.getSerializableExtra("contactItem");
            String email = intent.getStringExtra("email");
            AsyncTask.execute(() -> readContactData(email));
        }

        if (("ZONALHEAD").equals(profileKey)) {
            zonalHeadItem = (ZonalHeadItem) intent.getSerializableExtra("zonalheadItem");
            initZonalHeadProfile();
        }

        if (("ALLUSER").equals(profileKey)) {
            allUsersItem = (AllUsersItem) intent.getSerializableExtra("alluserItem");
            initAllUsersProfile();
        }
    }

    private void initAuthUserProfile() {
        helperObject.glideProfileImage(this, authUserItem.getProfileImageUrl(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", authUserItem.getProfileImageUrl());
            startActivity(intent);
        });
        tvName.setText(authUserItem.getShortName());
        tvSubTitle.setText(authUserItem.getFullName());
        Log.d(TAG, "initAuthUserProfile: name: " + authUserItem.getFullName() + " " + authUserItem.getMemberType());
//        profileActions(this, authUserItem.getPhone(), authUserItem.getPhone(), authUserItem.getEmail(), () -> helperObject.shareData(this, authUserItem.getProfileImageUrl(), ivProfileImage, authUserItem.getFullName(), authUserItem.getMemberType()));
        conLayProfileActions.setVisibility(View.GONE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(), "BASIC INFO");
//        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initFolkGuideProfile() {
        helperObject.glideProfileImage(this, folkGuideItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", folkGuideItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(folkGuideItem.getStrFolkGuideShortName());
        tvSubTitle.setText(folkGuideItem.getStrName());
        profileActions(this, folkGuideItem.getStrPhone(), folkGuideItem.getStrWhatsApp(), folkGuideItem.getStrEmail(), () -> helperObject.shareData(this, folkGuideItem.getStrProfileImage(), ivProfileImage, folkGuideItem.getStrName(), folkGuideItem.getStrName()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(), "BASIC INFO");
//        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initTeamLeadProfile() {
        helperObject.glideProfileImage(this, teamLeadItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", teamLeadItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(teamLeadItem.getstrTeamLeadAbbr());
        tvSubTitle.setText(teamLeadItem.getStrName());
        profileActions(this, teamLeadItem.getStrPhone(), teamLeadItem.getStrWhatsApp(), teamLeadItem.getStrEmail(), () -> helperObject.shareData(this, teamLeadItem.getStrProfileImage(), ivProfileImage, teamLeadItem.getStrName(), teamLeadItem.getStrName()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(), "BASIC INFO");
//        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initContactProfile() {
        helperObject.glideProfileImage(this, contactItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", contactItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(contactItem.getStrName());
        tvSubTitle.setText(contactItem.getStrFolkGuide());
        profileActions(this, contactItem.getStrPhone(), contactItem.getStrWhatsApp(), contactItem.getStrEmail(), () -> helperObject.shareData(this, contactItem.getStrProfileImage(), ivProfileImage, contactItem.getStrName(), contactItem.getStrName()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoContactFragment(), "BASIC INFO");
        profileAdapter.addFrag(new OccupationFragment(), "OCCUPATION");
        profileAdapter.addFrag(new ResidenceFragment(), "RESIDENCE");
        profileAdapter.addFrag(new TalentFragment(), "TALENT");
        profileAdapter.addFrag(new FamilyFragment(), "FAMILY");
//        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
    }

    private void initZonalHeadProfile() {
        helperObject.glideProfileImage(this, zonalHeadItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", zonalHeadItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(zonalHeadItem.getStrFirstName());
        tvSubTitle.setText(zonalHeadItem.getStrFirstName());
        profileActions(this, zonalHeadItem.getStrPhone(), zonalHeadItem.getStrWhatsApp(), zonalHeadItem.getStrEmail(), () -> helperObject.shareData(this, zonalHeadItem.getStrProfileImage(), ivProfileImage, zonalHeadItem.getStrFirstName() + " " + zonalHeadItem.getStrLastName(), zonalHeadItem.getStrMemberType()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(), "BASIC INFO");
//        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initAllUsersProfile() {
        helperObject.glideProfileImage(this, allUsersItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", allUsersItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(allUsersItem.getStrFirstName());
        tvSubTitle.setText(allUsersItem.getStrFirstName());
        profileActions(this, allUsersItem.getStrPhone(), allUsersItem.getStrWhatsApp(), allUsersItem.getStrEmail(), () -> helperObject.shareData(this, allUsersItem.getStrProfileImage(), ivProfileImage, allUsersItem.getStrFirstName() + " " + allUsersItem.getStrLastName(), allUsersItem.getStrMemberType()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(), "BASIC INFO");
//        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    public void profileActions(Context context, String phone, String whatsApp, String email, Callable<Void> shareFunction) {
        ImageView imgCall = findViewById(R.id.img_call);
        imgCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            startActivity(callIntent);
        });

        ImageView imgSms = findViewById(R.id.img_sms);
        imgSms.setOnClickListener(v -> {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", phone);
            smsIntent.putExtra("sms_body", "Message Body check");
            smsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            if (smsIntent.resolveActivity(context.getPackageManager()) != null) {
                startActivity(smsIntent);
            }
        });

        ImageView imgWhatsApp = findViewById(R.id.img_whatsapp);
        imgWhatsApp.setOnClickListener(v -> {
            PackageManager packageManager = context.getPackageManager();
            try {
                // checks if such an app exists or not
                packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Uri uri = Uri.parse("smsto:" + whatsApp);
                Intent whatsAppIntent = new Intent(Intent.ACTION_SENDTO, uri);
                whatsAppIntent.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(whatsAppIntent, "Dummy Title"));
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(context, "WhatsApp not found. Install from Play Store.", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent openPlayStore = new Intent(Intent.ACTION_VIEW, uri);
                openPlayStore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(openPlayStore);
            }
        });

        ImageView imgEmail = findViewById(R.id.img_email);
        imgEmail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "What's Up");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, this is x...");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });

        ImageView imgShare = findViewById(R.id.img_share);
        imgShare.setOnClickListener(v -> {
            try {
                shareFunction.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setUpTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                }
            }
        });
    }


    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void setUpAppbarLayout() {
        AppBarLayout appBarLayout = findViewById(R.id.appbar_profile);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (null != getSupportActionBar()) {
                        if (("AUTHUSER").equals(profileKey)) {
                            getSupportActionBar().setTitle(authUserItem.getShortName());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(authUserItem.getFullName());
                        }

                        if (("FOLKGUIDE").equals(profileKey)) {
                            getSupportActionBar().setTitle(folkGuideItem.getStrFolkGuideShortName());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(folkGuideItem.getStrName());
                        }

                        if (("TEAMLEAD").equals(profileKey)) {
                            getSupportActionBar().setTitle(teamLeadItem.getstrTeamLeadAbbr());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(teamLeadItem.getStrName());
                        }

                        if (("CONTACT").equals(profileKey)) {
                            getSupportActionBar().setTitle(contactItem.getStrName());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(contactItem.getStrFolkGuide());
                        }

                        if (("ZONALHEAD").equals(profileKey)) {
                            getSupportActionBar().setTitle(zonalHeadItem.getStrFirstName() + " " + zonalHeadItem.getStrLastName());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(zonalHeadItem.getStrMemberType());
                        }

                        if (("ALLUSER").equals(profileKey)) {
                            getSupportActionBar().setTitle(allUsersItem.getStrFirstName() + " " + allUsersItem.getStrLastName());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(allUsersItem.getStrMemberType());
                        }
                    }
                    isShow = true;
                } else if (isShow) {
                    if (null != getSupportActionBar()) getSupportActionBar().setTitle(" ");
                    isShow = false;
                }
            }
        });

        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (Math.abs(verticalOffset) - appBarLayout1.getTotalScrollRange() == 0) {
                // COLLAPSED STATE
//                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
//                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorBlack));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
                tabLayout.setTabTextColors(getResources().getColor(R.color.white_70), getResources().getColor(R.color.colorWhite));
                getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back_white));
            } else {
                // EXPANDED STATE
//                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorWhite));
//                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorWhite));
                toolbar.setBackgroundColor(Color.TRANSPARENT);
                tabLayout.setBackgroundColor(Color.TRANSPARENT);
                tabLayout.setTabTextColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccent));
                getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
                // toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
            }
        });
    }


    private void setUpCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_profile);
        // Set color of CollaspongToolbar when collapsing
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.header);
            Palette.from(bitmap).generate(palette -> {
//                    int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
//                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimaryDark);
                collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
                collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorTransparent));
            });
        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary));
            collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    // READ
    private Void readAuthUserData(String email) {
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

                                if (!("").equals(valueOf(docSnap.getString("shortName")))) {
                                    authUserItem.setShortName(valueOf(docSnap.getString("shortName")));
                                    Log.d(TAG, "readAuthUserData: shortName: " + valueOf(docSnap.getString("shortName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("fullName")))) {
                                    authUserItem.setFullName(valueOf(docSnap.getString("fullName")));
                                    Log.d(TAG, "readAuthUserData: fullname: " + valueOf(docSnap.getString("fullName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("profileImageUrl")))) {
                                    authUserItem.setProfileImageUrl(valueOf(docSnap.getString("profileImageUrl")));
                                    Log.d(TAG, "readAuthUserData: profilepic: " + valueOf(docSnap.getString("profileImageUrl")));
                                }
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        runOnUiThread(() -> {
                            initAuthUserProfile();
                            setUpTabLayout();
                            setUpToolbar();
                            setUpAppbarLayout();
                            setUpCollapsingToolbar();
                        });
                        Toast.makeText(ProfileActivity.this, "Got Data", Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> loadingBar.dismiss());
                    }
                })
                .addOnFailureListener(e -> runOnUiThread(() -> {
                    helperObject.showSnack(coordinatorProfile, "Couldn't get data!", getResources().getColor(R.color.colorWhite), "RELOAD", () -> readAuthUserData(email));
                    loadingBar.dismiss();
                }));
        return null;
    }

    // READ
    private Void readFolkGuideData(String email) {
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
                            folkGuideItem = docSnap.toObject(FolkGuideItem.class);
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
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        runOnUiThread(() -> {
                            initFolkGuideProfile();
                            setUpTabLayout();
                            setUpToolbar();
                            setUpAppbarLayout();
                            setUpCollapsingToolbar();
                        });
                        Toast.makeText(ProfileActivity.this, "Got Data", Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> loadingBar.dismiss());
                    }
                })
                .addOnFailureListener(e -> runOnUiThread(() -> {
                    helperObject.showSnack(coordinatorProfile, "Couldn't get data!", getResources().getColor(R.color.colorWhite), "RELOAD", () -> readFolkGuideData(email));
                    loadingBar.dismiss();
                }));
        return null;
    }

    // READ
    private Void readTeamLeadData(String email) {
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
                            teamLeadItem = docSnap.toObject(TeamLeadItem.class);
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
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        runOnUiThread(() -> {
                            initTeamLeadProfile();
                            setUpTabLayout();
                            setUpToolbar();
                            setUpAppbarLayout();
                            setUpCollapsingToolbar();
                        });
                        Toast.makeText(ProfileActivity.this, "Got Data", Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> loadingBar.dismiss());
                    }
                })
                .addOnFailureListener(e -> runOnUiThread(() -> {
                    helperObject.showSnack(coordinatorProfile, "Couldn't get data!", getResources().getColor(R.color.colorWhite), "RELOAD", () -> readTeamLeadData(email));
                    loadingBar.dismiss();
                }));
        return null;
    }

    // READ
    private Void readContactData(String email) {
        runOnUiThread(() -> {
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        });

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_FOLK_NEW_MEMBERS)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            contactItem = docSnap.toObject(ContactItem.class);
                            if (contactItem != null) {
                                Log.d(TAG, "ContactItem: " + contactItem);

                                if (!("").equals(valueOf(docSnap.getString("folk_guide")))) {
                                    contactItem.setStrFolkGuide(valueOf(docSnap.getString("folk_guide")));
                                    Log.d(TAG, "readContactData: folkGuide: " + valueOf(docSnap.getString("folk_guide")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("name")))) {
                                    contactItem.setStrName(valueOf(docSnap.getString("name")));
                                    Log.d(TAG, "readContactData: name: " + valueOf(docSnap.getString("name")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("profileImageUrl")))) {
                                    contactItem.setStrProfileImage(valueOf(docSnap.getString("profileImageUrl")));
                                    Log.d(TAG, "readContactData: profilepic: " + valueOf(docSnap.getString("profileImageUrl")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("email")))) {
                                    contactItem.setStrEmail(valueOf(docSnap.getString("email")));
                                    Log.d(TAG, "readFolkGuideData: email: " + valueOf(docSnap.getString("email")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("mobile")))) {
                                    contactItem.setStrPhone(valueOf(docSnap.getString("mobile")));
                                    Log.d(TAG, "readFolkGuideData: phone: " + valueOf(docSnap.getString("phone")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("whatsapp")))) {
                                    contactItem.setStrWhatsApp(valueOf(docSnap.getString("whatsapp")));
                                    Log.d(TAG, "readFolkGuideData: whatsapp: " + valueOf(docSnap.getString("phone")));
                                }
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }

                        runOnUiThread(() -> {
                            initContactProfile();
                            setUpTabLayout();
                            setUpToolbar();
                            setUpAppbarLayout();
                            setUpCollapsingToolbar();
                            Toast.makeText(ProfileActivity.this, "Got Data", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        });
                    }
                })
                .addOnFailureListener(e -> runOnUiThread(() -> {
                    helperObject.showSnack(coordinatorProfile, "Couldn't get data!", getResources().getColor(R.color.colorWhite), "RELOAD", () -> readContactData(email));
                    loadingBar.dismiss();
                }));
        return null;
    }
}
