package com.singularitycoder.folkdatabase.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.AuthUserItem;
import com.singularitycoder.folkdatabase.auth.MainActivity;
import com.singularitycoder.folkdatabase.helper.Helper;
import com.singularitycoder.folkdatabase.database.AllUsersItem;
import com.singularitycoder.folkdatabase.database.ContactItem;
import com.singularitycoder.folkdatabase.database.FolkGuideItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CoordinatorLayout mCoordinatorLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private String profileKey;
    private ImageView ivProfileImage;
    private TextView tvName, tvSubTitle;
    private ImageView ivCall, ivSms, ivWhatsApp, ivEmail, ivShare;

    ContactItem contactItem;
    FolkGuideItem folkGuideItem;
    AllUsersItem allUsersItem;
    AuthUserItem authUserItem;

    // Create a firebase auth object
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatuBarColor();
        setContentView(R.layout.activity_profile);
        getIntentData();
        inits();
        setUpViewPager();
        setUpTabLayout();
        setUpToolbar();
        setUpAppbarLayout();
        setUpCollapsingToolbar();

        // Initialize Firebase Auth.
        mAuth = FirebaseAuth.getInstance();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        profileKey = intent.getStringExtra("profileKey");
        Log.d(TAG, "getIntentData: profileKey: " + profileKey);
        if (("AUTHUSER").equals(profileKey)) {
            // load ur own profile data from firestore
            SharedPreferences sp = getSharedPreferences("authItem", Context.MODE_PRIVATE);
            authUserItem = new AuthUserItem(
                    "",
                    sp.getString("memberType", ""),
                    "",
                    "",
                    "",
                    "",
                    sp.getString("firstName", ""),
                    sp.getString("lastName", ""),
                    sp.getString("phone", ""),
                    sp.getString("email", ""),
                    "",
                    sp.getString("profileImage", ""),
                    "",
                    ""
            );
        }

        if (("FOLKGUIDE").equals(profileKey)) {
            // load registered folk guide data from firestore
            folkGuideItem = (FolkGuideItem) intent.getSerializableExtra("folkguideItem");
        }

        if (("CONTACT").equals(profileKey)) {
            // load contact data from firestore
            contactItem = (ContactItem) intent.getSerializableExtra("contactItem");
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

    private void setStatuBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  // clear FLAG_TRANSLUCENT_STATUS flag:
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));   // change the color
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    private void inits() {
        mCoordinatorLayout = findViewById(R.id.coordinator_profile);

        ivProfileImage = findViewById(R.id.img_profile_header);
        tvName = findViewById(R.id.tv_main_title);
        tvSubTitle = findViewById(R.id.tv_main_subtitle);

//        ivCall = findViewById(R.id.img_call);
//        ivSms = findViewById(R.id.img_sms);
//        ivWhatsApp = findViewById(R.id.img_whatsapp);
//        ivEmail = findViewById(R.id.img_email);
//        ivShare = findViewById(R.id.img_share);

        if (("AUTHUSER").equals(profileKey)) {
            Helper.glideProfileImage(this, authUserItem.getProfileImageUrl(), ivProfileImage);
            tvName.setText(authUserItem.getFirstName() + " " + authUserItem.getLastName());
            tvSubTitle.setText(authUserItem.getMemberType());
            profileActions(this, authUserItem.getPhone(), authUserItem.getPhone(), authUserItem.getEmail());
        }

        if (("FOLKGUIDE").equals(profileKey)) {
            Helper.glideProfileImage(this, folkGuideItem.getStrProfileImage(), ivProfileImage);
            tvName.setText(folkGuideItem.getStrFirstName());
            tvSubTitle.setText("KC Experience: " + folkGuideItem.getStrKcExperience());
//            profileActions(this, folkGuideItem.getStrPhone(), folkGuideItem.getStrWhatsApp(), folkGuideItem.getStrEmail());
            profileActions(this, "9999999999", "9999999999", "email@email.com");
        }

        if (("CONTACT").equals(profileKey)) {
            Helper.glideProfileImage(this, contactItem.getStrProfileImage(), ivProfileImage);
            tvName.setText(contactItem.getFirstName());
            tvSubTitle.setText(contactItem.getStrFolkGuide());
            profileActions(this, contactItem.getStrPhone(), contactItem.getStrWhatsApp(), contactItem.getStrEmail());
        }

        if (("ALLUSER").equals(profileKey)) {
            Helper.glideProfileImage(this, allUsersItem.getStrProfileImage(), ivProfileImage);
            tvName.setText(allUsersItem.getStrFirstName() + " " + allUsersItem.getStrLastName());
            tvSubTitle.setText(allUsersItem.getStrMemberType());
            profileActions(this, allUsersItem.getStrPhone(), allUsersItem.getStrWhatsApp(), allUsersItem.getStrEmail());
        }
    }

    public void profileActions(Context context, String phone, String whatsApp, String email) {
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
                new Helper().toast("WhatsApp not found. Install from playstore.", context, 1);
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
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Full Name");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.singularitycoder.com");
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            startActivity(Intent.createChooser(shareIntent, "Share to"));
        });
    }


    private void setUpViewPager() {
        // Set ViewPager
        viewPager = findViewById(R.id.viewpager_profile);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AboutFragment(), "BASIC INFO");
        adapter.addFrag(new AboutFragment(), "OCCUPATION");
        adapter.addFrag(new AboutFragment(), "RESIDENCE");
        adapter.addFrag(new TalentFragment(), "TALENT");
        adapter.addFrag(new TalentFragment(), "FAMILY");
        adapter.addFrag(new ActivityFragment(), "ACTIVITY");
        viewPager.setAdapter(adapter);
    }


    private void setUpTabLayout() {
        // Set TabLayout
        tabLayout = findViewById(R.id.tablayout_profile);
        tabLayout.setupWithViewPager(viewPager);

        // Do something on selecting each tab of tab layout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                Log.d(TAG, "onTabSelected: pos: " + tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "You clciked this 1", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "You clciked this 2", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Snackbar.make(mCoordinatorLayout, "1 got away", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Snackbar.make(mCoordinatorLayout, "2 got away", Snackbar.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "You clciked 1 again", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "You clciked 2 again", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }


    private void setUpToolbar() {
        // Set Toolbar
        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
// For back navigation button use this
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
                            getSupportActionBar().setTitle(authUserItem.getFirstName() + " " + authUserItem.getLastName());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(authUserItem.getMemberType());
                        }

                        if (("FOLKGUIDE").equals(profileKey)) {
                            getSupportActionBar().setTitle(folkGuideItem.getStrFirstName());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle("KC Experience: " + folkGuideItem.getStrKcExperience());
                        }

                        if (("CONTACT").equals(profileKey)) {
                            getSupportActionBar().setTitle(contactItem.getFirstName());
                            Objects.requireNonNull(getSupportActionBar()).setSubtitle(contactItem.getStrFolkGuide());
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

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
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
            }
        });
    }


    private void setUpCollapsingToolbar() {
        // Set CollapsingToolbar
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


    ///////////////////////////////////////////////////////////// FRAGMENT 1
    public static class AboutFragment extends Fragment {

        public AboutFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile_about, container, false);

            return view;
        }

    }


    ///////////////////////////////////////////////////////////// FRAGMENT 2
    public static class ActivityFragment extends Fragment {

        RecyclerView recyclerView;
        ProfileActivitiesAdapter mProfileActivitiesAdapter;
        ArrayList<ProfileContactItem> activityList;

        public ActivityFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile_activity, container, false);

            recyclerView = view.findViewById(R.id.recycler_profile_activity);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            activityList = new ArrayList<>();
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));


            mProfileActivitiesAdapter = new ProfileActivitiesAdapter(activityList, getActivity());
            mProfileActivitiesAdapter.setHasStableIds(true);
            recyclerView.setAdapter(mProfileActivitiesAdapter);

            return view;
        }

//        @Override
//        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            inflater.inflate(R.menu.menu_contacts, menu);
//            super.onCreateOptionsMenu(menu, inflater);
//        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
    }

    ///////////////////////////////////////////////////////////// FRAGMENT 3
    public static class TalentFragment extends Fragment {

        public TalentFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile_talent, container, false);

            return view;
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
    }

}
