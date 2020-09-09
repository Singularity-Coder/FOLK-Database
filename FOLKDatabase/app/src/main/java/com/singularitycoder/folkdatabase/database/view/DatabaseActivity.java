package com.singularitycoder.folkdatabase.database.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.helper.HelperSharedPreference;

import java.util.ArrayList;
import java.util.List;

public class DatabaseActivity extends AppCompatActivity {

    private static final String TAG = "DatabaseActivity";

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab1;
    private ViewPagerAdapter viewPagerAdapter;
    private HelperGeneral helperObject = new HelperGeneral();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helperObject.setStatusBarColor(this, R.color.colorPrimaryDark);
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
        // Main Shared Pref
        HelperSharedPreference helperSharedPreference = HelperSharedPreference.getInstance(this);
        String memberType = helperSharedPreference.getMemberType();


//        SharedPreferences sp = getSharedPreferences("authItem", Context.MODE_PRIVATE);
//        String memberType = sp.getString("memberType", "");
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

        // DEMO
        if (("").toLowerCase().equals(memberType.toLowerCase())) {
            viewPagerAdapter.addFrag(new AllUsersFragment(), "ALL USERS");
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
