package com.singularitycoder.folkdatabase.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Map;
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
    private HelperGeneral helperObject = new HelperGeneral();

    private ContactItem contactItem;
    private FolkGuideItem folkGuideItem;
    private TeamLeadItem teamLeadItem;
    private AllUsersItem allUsersItem;
    private ZonalHeadItem zonalHeadItem;
    private AuthUserItem authUserItem;

    // Create a firebase auth object
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HelperGeneral().setStatuBarColor(this, R.color.colorPrimaryDark);
        setContentView(R.layout.activity_profile);
        inits();
        getIntentData();
        initProfiles();
        setUpTabLayout();
        setUpToolbar();
        setUpAppbarLayout();
        setUpCollapsingToolbar();
    }


    private void inits() {
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        profileAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        tabLayout = findViewById(R.id.tablayout_profile);
        viewPager = findViewById(R.id.viewpager_profile);
        mCoordinatorLayout = findViewById(R.id.coordinator_profile);
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
            // load ur own profile data from firestore
//            SharedPreferences sp = getSharedPreferences("authItem", Context.MODE_PRIVATE);
//            authUserItem = new AuthUserItem(
//                    "",
//                    sp.getString("memberType", ""),
//                    "",
//                    "",
//                    "",
//                    sp.getString("fullName", ""),
//                    sp.getString("phone", ""),
//                    sp.getString("email", ""),
//                    sp.getString("gmail", ""),
//                    "",
//                    sp.getString("profileImage", ""),
//                    "",
//                    ""
//            );
            Log.d(TAG, "getIntentData: auth prof hit");
            readAuthUserData();
        }

        if (("FOLKGUIDE").equals(profileKey)) {
            // load registered folk guide data from firestore
            folkGuideItem = (FolkGuideItem) intent.getSerializableExtra("folkguideItem");
        }

        if (("TEAMLEAD").equals(profileKey)) {
            // load registered folk guide data from firestore
            teamLeadItem = (TeamLeadItem) intent.getSerializableExtra("teamleadItem");
        }

        if (("CONTACT").equals(profileKey)) {
            // load contact data from firestore
            contactItem = (ContactItem) intent.getSerializableExtra("contactItem");
        }

        if (("ZONALHEAD").equals(profileKey)) {
            // load contact data from firestore
            zonalHeadItem = (ZonalHeadItem) intent.getSerializableExtra("zonalheadItem");
        }

        if (("ALLUSER").equals(profileKey)) {
            // load contact data from firestore
            allUsersItem = (AllUsersItem) intent.getSerializableExtra("alluserItem");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void initProfiles() {
        if (("AUTHUSER").equals(profileKey)) {
            initAuthUserProfile();
        }

        if (("FOLKGUIDE").equals(profileKey)) {
            initFolkGuideProfile();
        }

        if (("TEAMLEAD").equals(profileKey)) {
            initTeamLeadProfile();
        }

        if (("CONTACT").equals(profileKey)) {
            initContactProfile();
        }

        if (("ZONALHEAD").equals(profileKey)) {
            initZonalHeadProfile();
        }

        if (("ALLUSER").equals(profileKey)) {
            initAllUsersProfile();
        }
    }

    private void initAuthUserProfile() {
        HelperGeneral.glideProfileImage(this, authUserItem.getProfileImageUrl(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", authUserItem.getProfileImageUrl());
            startActivity(intent);
        });
        tvName.setText(authUserItem.getFullName());
        tvSubTitle.setText(authUserItem.getMemberType());
        profileActions(this, authUserItem.getPhone(), authUserItem.getPhone(), authUserItem.getEmail(), () -> helperObject.shareData(this, authUserItem.getProfileImageUrl(), ivProfileImage, authUserItem.getFullName(), authUserItem.getMemberType()));
        conLayProfileActions.setVisibility(View.GONE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(), "BASIC INFO");
        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initFolkGuideProfile() {
        HelperGeneral.glideProfileImage(this, folkGuideItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", folkGuideItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(folkGuideItem.getStrFolkGuideAbbr());
        tvSubTitle.setText("Full Name: " + folkGuideItem.getStrFirstName());
        profileActions(this, folkGuideItem.getStrPhone(), folkGuideItem.getStrWhatsApp(), folkGuideItem.getStrEmail(), () -> helperObject.shareData(this, folkGuideItem.getStrProfileImage(), ivProfileImage, folkGuideItem.getStrFirstName(), folkGuideItem.getStrFirstName()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(), "BASIC INFO");
        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initTeamLeadProfile() {
        HelperGeneral.glideProfileImage(this, teamLeadItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", teamLeadItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(teamLeadItem.getstrTeamLeadAbbr());
        tvSubTitle.setText("Full Name: " + teamLeadItem.getStrFirstName());
        profileActions(this, teamLeadItem.getStrPhone(), teamLeadItem.getStrWhatsApp(), teamLeadItem.getStrEmail(), () -> helperObject.shareData(this, teamLeadItem.getStrProfileImage(), ivProfileImage, teamLeadItem.getStrFirstName(), teamLeadItem.getStrFirstName()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(), "BASIC INFO");
        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initContactProfile() {
        HelperGeneral.glideProfileImage(this, contactItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", contactItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(contactItem.getFirstName());
        tvSubTitle.setText(contactItem.getStrFolkGuide());
        profileActions(this, contactItem.getStrPhone(), contactItem.getStrWhatsApp(), contactItem.getStrEmail(), () -> helperObject.shareData(this, contactItem.getStrProfileImage(), ivProfileImage, contactItem.getFirstName(), contactItem.getStrFolkGuide()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(), "BASIC INFO");
        profileAdapter.addFrag(new OccupationFragment(), "OCCUPATION");
        profileAdapter.addFrag(new ResidenceFragment(), "RESIDENCE");
        profileAdapter.addFrag(new TalentFragment(), "TALENT");
        profileAdapter.addFrag(new FamilyFragment(), "FAMILY");
        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
    }

    private void initZonalHeadProfile() {
        HelperGeneral.glideProfileImage(this, zonalHeadItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", zonalHeadItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(zonalHeadItem.getStrFirstName() + " " + zonalHeadItem.getStrLastName());
        tvSubTitle.setText(zonalHeadItem.getStrMemberType());
        profileActions(this, zonalHeadItem.getStrPhone(), zonalHeadItem.getStrWhatsApp(), zonalHeadItem.getStrEmail(), () -> helperObject.shareData(this, zonalHeadItem.getStrProfileImage(), ivProfileImage, zonalHeadItem.getStrFirstName() + " " + zonalHeadItem.getStrLastName(), zonalHeadItem.getStrMemberType()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(), "BASIC INFO");
        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initAllUsersProfile() {
        HelperGeneral.glideProfileImage(this, allUsersItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", allUsersItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(allUsersItem.getStrFirstName() + " " + allUsersItem.getStrLastName());
        tvSubTitle.setText(allUsersItem.getStrMemberType());
        profileActions(this, allUsersItem.getStrPhone(), allUsersItem.getStrWhatsApp(), allUsersItem.getStrEmail(), () -> helperObject.shareData(this, allUsersItem.getStrProfileImage(), ivProfileImage, allUsersItem.getStrFirstName() + " " + allUsersItem.getStrLastName(), allUsersItem.getStrMemberType()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(), "BASIC INFO");
        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
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
                Toast.makeText(context, "WhatsApp not found. Install from playstore.", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent openPlayStore = new Intent(Intent.ACTION_VIEW, uri);
                openPlayStore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(openPlayStore);
            }
        });

        ImageView imgEmail = findViewById(R.id.img_email);
        imgEmail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Follow Up");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi Contact, this is telecaller...");
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
        // Set TabLayout
        tabLayout.setupWithViewPager(viewPager);

        // Do something on selecting each tab of tab layout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                Log.d(TAG, "onTabSelected: pos: " + tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
//                        Toast.makeText(getApplicationContext(), "You clciked this 1", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
//                        Toast.makeText(getApplicationContext(), "You clciked this 2", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
//                        Snackbar.make(mCoordinatorLayout, "1 got away", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
//                        Snackbar.make(mCoordinatorLayout, "2 got away", Snackbar.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
//                        Toast.makeText(getApplicationContext(), "You clciked 1 again", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
//                        Toast.makeText(getApplicationContext(), "You clciked 2 again", Toast.LENGTH_LONG).show();
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
                    if (getSupportActionBar() != null) {
                        if (("AUTHUSER").equals(profileKey)) {
                            getSupportActionBar().setTitle(authUserItem.getFullName());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(authUserItem.getMemberType());
                        }

                        if (("FOLKGUIDE").equals(profileKey)) {
                            getSupportActionBar().setTitle(folkGuideItem.getStrFolkGuideAbbr());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(folkGuideItem.getStrFirstName());
                        }

                        if (("TEAMLEAD").equals(profileKey)) {
                            getSupportActionBar().setTitle(teamLeadItem.getstrTeamLeadAbbr());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(teamLeadItem.getStrFirstName());
                        }

                        if (("CONTACT").equals(profileKey)) {
                            getSupportActionBar().setTitle(contactItem.getFirstName());
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
                    if (getSupportActionBar() != null) getSupportActionBar().setTitle(" ");
                    isShow = false;
                }
            }
        });

        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (Math.abs(verticalOffset) - appBarLayout1.getTotalScrollRange() == 0) {
                // COLLAPSED STATE
//                    Toast.makeText(getApplicationContext(), "Collapsed", Toast.LENGTH_LONG).show();
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
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {
//                    int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
//                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimaryDark);
                    collapsingToolbarLayout.setContentScrimColor(R.color.colorPrimary);
                    collapsingToolbarLayout.setStatusBarScrimColor(R.color.colorTransparent);
                }
            });
        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary));
            collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }


    private static class ViewPagerAdapter extends FragmentPagerAdapter {
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
                                    Log.d(TAG, "readAuthUserData: fullname: " + valueOf(docSnap.getString("fullName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("profileImageUrl")))) {
                                    authUserItem.setProfileImageUrl(valueOf(docSnap.getString("profileImageUrl")));
                                    Log.d(TAG, "readAuthUserData: profilepic: " + valueOf(docSnap.getString("profileImageUrl")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("memberType")))) {
                                    authUserItem.setMemberType(valueOf(docSnap.getString("memberType")));
                                    Log.d(TAG, "readAuthUserData: memberType: " + valueOf(docSnap.getString("memberType")));
                                }

                                Map<String, Object> talent = (Map<String, Object>) docSnap.getData().get("talent");
                                Log.d(TAG, "readContactsData: talent map: " + talent);
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        Toast.makeText(ProfileActivity.this, "Got Data", Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> {
                            loadingBar.dismiss();
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivity.this, "Couldn't get data!", Toast.LENGTH_SHORT).show();
                    runOnUiThread(() -> loadingBar.dismiss());
                });
    }
}
