package com.singularitycoder.folkdatabase.profile.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.model.AuthUserItem;
import com.singularitycoder.folkdatabase.database.model.AllUsersItem;
import com.singularitycoder.folkdatabase.database.model.ContactItem;
import com.singularitycoder.folkdatabase.database.model.FolkGuideItem;
import com.singularitycoder.folkdatabase.database.model.TeamLeadItem;
import com.singularitycoder.folkdatabase.database.model.ZonalHeadItem;
import com.singularitycoder.folkdatabase.helper.RequestStateMediator;
import com.singularitycoder.folkdatabase.helper.FrescoImageViewer;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.helper.HelperSharedPreference;
import com.singularitycoder.folkdatabase.helper.Status;
import com.singularitycoder.folkdatabase.profile.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;

import static com.singularitycoder.folkdatabase.helper.FolkDatabaseApp.hasInternet;
import static java.lang.String.valueOf;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    @Nullable
    @BindView(R.id.viewpager_profile)
    ViewPager viewPager;
    @Nullable
    @BindView(R.id.tablayout_profile)
    TabLayout tabLayout;
    @Nullable
    @BindView(R.id.toolbar_profile)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.img_profile_header)
    ImageView ivProfileImage;
    @Nullable
    @BindView(R.id.tv_main_title)
    TextView tvName;
    @Nullable
    @BindView(R.id.tv_main_subtitle)
    TextView tvSubTitle;
    @Nullable
    @BindView(R.id.con_lay_profile_action_icons)
    ConstraintLayout conLayProfileActions;
    @Nullable
    @BindView(R.id.coordinator_profile)
    CoordinatorLayout coordinatorProfile;

    private String profileKey;

    private ProgressDialog loadingBar;
    private HelperGeneral helperObject = new HelperGeneral();
    private ProfileViewModel profileViewModel;
    private ViewPagerAdapter profileAdapter;
    private Unbinder unbinder;

    private AuthUserItem authUserItem = new AuthUserItem();
    private ContactItem contactItem = new ContactItem();
    private FolkGuideItem folkGuideItem = new FolkGuideItem();
    private TeamLeadItem teamLeadItem = new TeamLeadItem();
    private AllUsersItem allUsersItem = new AllUsersItem();
    private ZonalHeadItem zonalHeadItem = new ZonalHeadItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helperObject.setStatuBarColor(this, R.color.colorPrimaryDark);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        unbinder = ButterKnife.bind(this);
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
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        profileKey = intent.getStringExtra("profileKey");
        Log.d(TAG, "getIntentData: profileKey: " + profileKey);
        if (("AUTHUSER").equals(profileKey)) {
            // Main Shared Pref
            HelperSharedPreference helperSharedPreference = HelperSharedPreference.getInstance(this);
            String email = helperSharedPreference.getEmail();
            getTopHalfAuthUserProfile(email);
        }

        if (("FOLKGUIDE").equals(profileKey)) {
//            folkGuideItem = (FolkGuideItem) intent.getSerializableExtra("folkguideItem");
            String email = intent.getStringExtra("email");
            getTopHalfFolkGuideProfile(email);
        }

        if (("TEAMLEAD").equals(profileKey)) {
//            teamLeadItem = (TeamLeadItem) intent.getSerializableExtra("teamleadItem");
            String email = intent.getStringExtra("email");
            getTopHalfTeamLeadProfile(email);
        }

        if (("CONTACT").equals(profileKey)) {
//            contactItem = (ContactItem) intent.getSerializableExtra("contactItem");
            String email = intent.getStringExtra("email");
            getTopHalfContactProfile(email);
        }

        if (("ZONALHEAD").equals(profileKey)) {
            String email = intent.getStringExtra("email");
//            zonalHeadItem = (ZonalHeadItem) intent.getSerializableExtra("zonalheadItem");
            initZonalHeadProfile(email);
        }

        if (("ALLUSER").equals(profileKey)) {
            String email = intent.getStringExtra("email");
//            allUsersItem = (AllUsersItem) intent.getSerializableExtra("alluserItem");
            getTopHalfAllUsersProfile(email);
        }
    }

    private void initAuthUserProfile(String emailKey, AuthUserItem authUserItem) {
        helperObject.glideProfileImage(this, authUserItem.getProfileImageUrl(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, FrescoImageViewer.class);
            intent.putExtra("image_url", authUserItem.getProfileImageUrl());
            startActivity(intent);
        });
        // DEMO
        tvName.setText(authUserItem.getFullName());
        tvSubTitle.setText(authUserItem.getEmail());

        // REAL
//        tvName.setText(authUserItem.getShortName());
//        tvSubTitle.setText(authUserItem.getFullName());
        Log.d(TAG, "initAuthUserProfile: name: " + authUserItem.getFullName() + " " + authUserItem.getMemberType());
//        profileActions(this, authUserItem.getPhone(), authUserItem.getPhone(), authUserItem.getEmail(), () -> helperObject.shareData(this, authUserItem.getProfileImageUrl(), ivProfileImage, authUserItem.getFullName(), authUserItem.getMemberType()));
        conLayProfileActions.setVisibility(View.GONE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(emailKey), "BASIC INFO");
//        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initFolkGuideProfile(String emailKey, FolkGuideItem folkGuideItem) {
        helperObject.glideProfileImage(this, folkGuideItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, FrescoImageViewer.class);
            intent.putExtra("image_url", folkGuideItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(folkGuideItem.getStrFolkGuideShortName());
        tvSubTitle.setText(folkGuideItem.getStrName());
        profileActions(this, folkGuideItem.getStrPhone(), folkGuideItem.getStrWhatsApp(), folkGuideItem.getStrEmail(), () -> helperObject.shareData(this, folkGuideItem.getStrProfileImage(), ivProfileImage, folkGuideItem.getStrName(), folkGuideItem.getStrName()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(emailKey), "BASIC INFO");
//        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initTeamLeadProfile(String emailKey, TeamLeadItem teamLeadItem) {
        helperObject.glideProfileImage(this, teamLeadItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, FrescoImageViewer.class);
            intent.putExtra("image_url", teamLeadItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(teamLeadItem.getstrTeamLeadAbbr());
        tvSubTitle.setText(teamLeadItem.getStrName());
        profileActions(this, teamLeadItem.getStrPhone(), teamLeadItem.getStrWhatsApp(), teamLeadItem.getStrEmail(), () -> helperObject.shareData(this, teamLeadItem.getStrProfileImage(), ivProfileImage, teamLeadItem.getStrName(), teamLeadItem.getStrName()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(emailKey), "BASIC INFO");
//        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initContactProfile(String emailKey, ContactItem contactItem) {
        helperObject.glideProfileImage(this, contactItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, FrescoImageViewer.class);
            intent.putExtra("image_url", contactItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(contactItem.getStrName());
        tvSubTitle.setText(contactItem.getStrFolkGuide());
        profileActions(this, contactItem.getStrPhone(), contactItem.getStrWhatsApp(), contactItem.getStrEmail(), () -> helperObject.shareData(this, contactItem.getStrProfileImage(), ivProfileImage, contactItem.getStrName(), contactItem.getStrName()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoContactFragment(emailKey), "BASIC INFO");
        profileAdapter.addFrag(new EducationFragment(emailKey), "EDUCATION");
        profileAdapter.addFrag(new OccupationFragment(emailKey), "OCCUPATION");
        profileAdapter.addFrag(new TalentFragment(emailKey), "TALENT");
        profileAdapter.addFrag(new ResidenceFragment(emailKey), "RESIDENCE");
        profileAdapter.addFrag(new FamilyFragment(emailKey), "FAMILY");
//        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
    }

    private void initZonalHeadProfile(String emailKey) {
        helperObject.glideProfileImage(this, zonalHeadItem.getStrProfileImage(), ivProfileImage);
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, FrescoImageViewer.class);
            intent.putExtra("image_url", zonalHeadItem.getStrProfileImage());
            startActivity(intent);
        });
        tvName.setText(zonalHeadItem.getStrFirstName());
        tvSubTitle.setText(zonalHeadItem.getStrEmail());
        profileActions(this, zonalHeadItem.getStrPhone(), zonalHeadItem.getStrWhatsApp(), zonalHeadItem.getStrEmail(), () -> helperObject.shareData(this, zonalHeadItem.getStrProfileImage(), ivProfileImage, zonalHeadItem.getStrFirstName() + " " + zonalHeadItem.getStrLastName(), zonalHeadItem.getStrMemberType()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(emailKey), "BASIC INFO");
//        profileAdapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(profileAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initAllUsersProfile(String emailKey, AllUsersItem allUsersItem) {
        Log.d(TAG, "initAllUsersProfile: hit");
        helperObject.glideProfileImage(this, allUsersItem.getStrProfileImage(), ivProfileImage);
        Log.d(TAG, "initAllUsersProfile: image: " + allUsersItem.getStrProfileImage());
        ivProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, FrescoImageViewer.class);
            intent.putExtra("image_url", allUsersItem.getStrProfileImage());
            startActivity(intent);
        });
        Log.d(TAG, "initAllUsersProfile: name: " + allUsersItem.getStrFirstName());
        Log.d(TAG, "initAllUsersProfile: email: " + allUsersItem.getStrEmail());
        tvName.setText(allUsersItem.getStrFirstName());
        tvSubTitle.setText(allUsersItem.getStrEmail());
        profileActions(this, allUsersItem.getStrPhone(), allUsersItem.getStrWhatsApp(), allUsersItem.getStrEmail(), () -> helperObject.shareData(this, allUsersItem.getStrProfileImage(), ivProfileImage, allUsersItem.getStrFirstName(), allUsersItem.getStrMemberType()));
        conLayProfileActions.setVisibility(View.VISIBLE);
        // Set up Viewpager tabs
        profileAdapter.addFrag(new BasicInfoFragment(emailKey), "BASIC INFO");
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
                            // DEMO
                            getSupportActionBar().setTitle(allUsersItem.getStrFirstName());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(allUsersItem.getStrEmail());

                            // REAL
//                            getSupportActionBar().setTitle(allUsersItem.getStrFirstName() + " " + allUsersItem.getStrLastName());
//                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(allUsersItem.getStrMemberType());
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
                toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
                toolbar.setSubtitleTextColor(getResources().getColor(R.color.white_70));

                tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tabLayout.setTabTextColors(getResources().getColor(R.color.white_70), getResources().getColor(R.color.colorWhite));

                getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back_white));
            } else {
                // EXPANDED STATE
//                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorWhite));
//                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorWhite));
                toolbar.setBackgroundColor(Color.TRANSPARENT);
                toolbar.setTitleTextColor(getResources().getColor(R.color.colorTransparent));
                toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorTransparent));

                tabLayout.setBackgroundColor(Color.TRANSPARENT);
                tabLayout.setTabTextColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccent));

                getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
                // toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
            }
        });
    }

    private void setUpCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_profile);
        // Set color of CollapsingToolbar when collapsing
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

    private void getTopHalfAuthUserProfile(String email) {
        profileViewModel.getAuthUserInfoFromRepository(email).observe(ProfileActivity.this, liveDataObserver(email));
    }

    private void getTopHalfFolkGuideProfile(String email) {
        profileViewModel.getFolkGuideInfoFromRepository(email).observe(ProfileActivity.this, liveDataObserver(email));
    }

    private void getTopHalfTeamLeadProfile(String email) {
        profileViewModel.getTeamLeadInfoFromRepository(email).observe(ProfileActivity.this, liveDataObserver(email));
    }

    private void getTopHalfContactProfile(String email) {
        profileViewModel.getContactInfoFromRepository(email).observe(ProfileActivity.this, liveDataObserver(email));
    }

    private void getTopHalfAllUsersProfile(String email) {
        profileViewModel.getAllUsersInfoFromRepository(email).observe(ProfileActivity.this, liveDataObserver(email));
    }

    private Observer liveDataObserver(String email) {
        Observer<RequestStateMediator> observer = null;
        if (hasInternet()) {
            observer = requestStateMediator -> {

                if (Status.LOADING == requestStateMediator.getStatus()) {
                    runOnUiThread(() -> {
                        loadingBar.setMessage(valueOf(requestStateMediator.getMessage()));
                        loadingBar.setCanceledOnTouchOutside(false);
                        if (null != loadingBar && !loadingBar.isShowing()) loadingBar.show();
                    });
                }

                if (Status.SUCCESS == requestStateMediator.getStatus()) {
                    runOnUiThread(() -> {
                        if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();

                        if (("AUTH USER").equals(requestStateMediator.getKey())) {
                            initAuthUserProfile(email, (AuthUserItem) requestStateMediator.getData());
                            authUserItem = (AuthUserItem) requestStateMediator.getData();
                        }

                        if (("FOLK GUIDE").equals(requestStateMediator.getKey())) {
                            initFolkGuideProfile(email, (FolkGuideItem) requestStateMediator.getData());
                            folkGuideItem = (FolkGuideItem) requestStateMediator.getData();
                        }

                        if (("TEAM LEAD").equals(requestStateMediator.getKey())) {
                            initTeamLeadProfile(email, (TeamLeadItem) requestStateMediator.getData());
                            teamLeadItem = (TeamLeadItem) requestStateMediator.getData();
                        }

                        if (("CONTACT").equals(requestStateMediator.getKey())) {
                            initContactProfile(email, (ContactItem) requestStateMediator.getData());
                            contactItem = (ContactItem) requestStateMediator.getData();
                        }

                        if (("ALL USERS").equals(requestStateMediator.getKey())) {
                            initAllUsersProfile(email, (AllUsersItem) requestStateMediator.getData());
                            allUsersItem = (AllUsersItem) requestStateMediator.getData();
                        }

                        setUpTabLayout();
                        setUpToolbar();
                        setUpAppbarLayout();
                        setUpCollapsingToolbar();
                        Toast.makeText(ProfileActivity.this, valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    });
                }

                if (Status.EMPTY == requestStateMediator.getStatus()) {
                    runOnUiThread(() -> {
                        if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                        Toast.makeText(ProfileActivity.this, valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();
                    });
                }

                if (Status.ERROR == requestStateMediator.getStatus()) {
                    runOnUiThread(() -> {
                        if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                        Toast.makeText(ProfileActivity.this, valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();

                        if (("AUTH USER").equals(requestStateMediator.getKey())) {
                            helperObject.showSnackBar(coordinatorProfile, "Couldn't get data!", getResources().getColor(R.color.colorWhite), "RELOAD", (view) -> getTopHalfAuthUserProfile(email));
                        }

                        if (("FOLK GUIDE").equals(requestStateMediator.getKey())) {
                            helperObject.showSnackBar(coordinatorProfile, "Couldn't get data!", getResources().getColor(R.color.colorWhite), "RELOAD", (view) -> getTopHalfFolkGuideProfile(email));
                        }

                        if (("TEAM LEAD").equals(requestStateMediator.getKey())) {
                            helperObject.showSnackBar(coordinatorProfile, "Couldn't get data!", getResources().getColor(R.color.colorWhite), "RELOAD", (view) -> getTopHalfTeamLeadProfile(email));
                        }

                        if (("CONTACT").equals(requestStateMediator.getKey())) {
                            helperObject.showSnackBar(coordinatorProfile, "Couldn't get data!", getResources().getColor(R.color.colorWhite), "RELOAD", (view) -> getTopHalfContactProfile(email));
                        }

                        if (("ALL USERS").equals(requestStateMediator.getKey())) {
                            helperObject.showSnackBar(coordinatorProfile, "Couldn't get data!", getResources().getColor(R.color.colorWhite), "RELOAD", (view) -> getTopHalfAllUsersProfile(email));
                        }
                    });
                }
            };
        }
        return observer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
