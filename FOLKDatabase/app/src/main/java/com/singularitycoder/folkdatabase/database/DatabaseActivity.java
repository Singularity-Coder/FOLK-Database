package com.singularitycoder.folkdatabase.database;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.helper.HelperFrescoImageViewer;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DatabaseActivity extends AppCompatActivity {

    private static final String TAG = DatabaseActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab1;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpStatusBar();
        setContentView(R.layout.activity_database);
        inits();
        initToolBar();
//        initViewPager();
//        setupViewPager(viewPager);
        showHideTabs();
        initTabLayout();
    }

    private void inits() {
        toolbar = findViewById(R.id.toolbar_home);
        fab1 = findViewById(R.id.floating_button);
        viewPager = findViewById(R.id.viewpager_home);
        tabLayout = findViewById(R.id.tabs_home);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    }


    private void setUpStatusBar() {
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


    private void initToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("FOLK Database");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void initViewPager() {
        final int CONTACTS = 0;
        final int FOLK_GUIDES = 1;
        final int TEAM_LEADS = 2;
        final int ZONAL_HEADS = 3;
        final int ALL_USERS = 4;

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case CONTACTS:
                        fab1.hide();
//                        fab1.show();
                        fab1.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_list_black_24dp));
                        fab1.setOnClickListener(view -> {
//                            new ContactFragment().contactFilterDialog(DatabaseActivity.this);
                            Toast.makeText(getApplicationContext(), "Filter Contacts", Toast.LENGTH_SHORT).show();
                        });
                        break;
                    case FOLK_GUIDES:
                        fab1.hide();
                        break;
                    case TEAM_LEADS:
                        fab1.hide();
                        break;
//                    case ZONAL_HEADS:
//                        fab1.hide();
//                        break;
//                    case ALL_USERS:
//                        fab1.hide();
//                        break;
                }
            }
        });
    }


    private void initTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
//                        new HelperGeneral().toast("1", getApplicationContext(), 0);
                        break;
                    case 1:
//                        new HelperGeneral().toast("2", getApplicationContext(), 0);
                        break;
                    case 2:
//                        new HelperGeneral().toast("3", getApplicationContext(), 0);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    private void showHideTabs() {
        SharedPreferences sp = getSharedPreferences("authItem", Context.MODE_PRIVATE);
        String memberType = sp.getString("memberType", "");
        Log.d(TAG, "hideTabs: member type: " + memberType);

        if (("Folk Guide").toLowerCase().equals(memberType.toLowerCase())) {
//            if (tabLayout.getTabCount() >= 1 && null != tabLayout && null != viewPager) {
//                viewPager.removeViewAt(0);
//                tabLayout.removeTabAt(1);
//                viewPager.removeView();
//            }
            viewPagerAdapter.addFrag(new ContactFragment(), "CONTACTS");
            viewPager.setAdapter(viewPagerAdapter);
        }

        if (("Team Lead").toLowerCase().equals(memberType.toLowerCase())) {
//            if (tabLayout.getTabCount() >= 1 && null != tabLayout && null != viewPager) {
//                viewPager.removeViewAt(2);
//                tabLayout.removeTabAt(2);
//            }
            viewPagerAdapter.addFrag(new ContactFragment(), "CONTACTS");
            viewPagerAdapter.addFrag(new FolkGuidesFragment(), "FOLK GUIDES");
            viewPager.setAdapter(viewPagerAdapter);
        }

        if (("Zonal Head").toLowerCase().equals(memberType.toLowerCase())) {
            viewPagerAdapter.addFrag(new ContactFragment(), "CONTACTS");
            viewPagerAdapter.addFrag(new FolkGuidesFragment(), "FOLK GUIDES");
            viewPagerAdapter.addFrag(new TeamLeadsFragment(), "TEAM LEADS");
            viewPager.setAdapter(viewPagerAdapter);
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new ContactFragment(), "CONTACTS");
        viewPagerAdapter.addFrag(new FolkGuidesFragment(), "FOLK GUIDES");
        viewPagerAdapter.addFrag(new TeamLeadsFragment(), "TEAM LEADS");
//        adapter.addFrag(new ZonalHeadsFragment(), "ZONAL HEADS");
//        adapter.addFrag(new AllUsersFragment(), "ALL USERS");
        viewPager.setAdapter(viewPagerAdapter);
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


    public void showQuickInfoDialog(Context context, String fullName, String imageUrl, String phone, String whatsApp, String email) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_quick_profile);

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(dialog.getWindow()).setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvFullName = dialog.findViewById(R.id.tv_name);
        tvFullName.setText(fullName);

        ImageView imgProfilePic = dialog.findViewById(R.id.img_profile_image);
        HelperGeneral.glideProfileImage(context, imageUrl, imgProfilePic);
        imgProfilePic.setOnClickListener(v -> {
//            SimpleDraweeView draweeView = findViewById(R.id.img_fresco_full_image);
//            draweeView.setImageURI(imageUrl);
            Intent intent = new Intent(context, HelperFrescoImageViewer.class);
            intent.putExtra("image_url", imageUrl);
            context.startActivity(intent);
        });

        ImageView imgCall = dialog.findViewById(R.id.img_quick_call);
        imgCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            context.startActivity(callIntent);
        });

        ImageView imgSms = dialog.findViewById(R.id.img_quick_message);
        imgSms.setOnClickListener(v -> {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", phone);
            smsIntent.putExtra("sms_body", "Message Body check");
            smsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            if (smsIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(smsIntent);
            }
        });

        ImageView imgWhatsApp = dialog.findViewById(R.id.img_quick_whatsapp);
        imgWhatsApp.setOnClickListener(v -> {
            PackageManager packageManager = context.getPackageManager();
            try {
                // checks if such an app exists or not
                packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Uri uri = Uri.parse("smsto:" + whatsApp);
                Intent whatsAppIntent = new Intent(Intent.ACTION_SENDTO, uri);
                whatsAppIntent.setPackage("com.whatsapp");
                context.startActivity(Intent.createChooser(whatsAppIntent, "Dummy Title"));
            } catch (PackageManager.NameNotFoundException e) {
                new HelperGeneral().toast("WhatsApp not found. Install from playstore.", context, 1);
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent openPlayStore = new Intent(Intent.ACTION_VIEW, uri);
                openPlayStore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                context.startActivity(openPlayStore);
            }
        });

        ImageView imgEmail = dialog.findViewById(R.id.img_quick_email);
        imgEmail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Follow Up");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi Contact, this is telecaller...");
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });

        ImageView imgShare = dialog.findViewById(R.id.img_quick_share);
        imgShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Full Name");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.singularitycoder.com");
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            context.startActivity(Intent.createChooser(shareIntent, "Share to"));
        });

        dialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
