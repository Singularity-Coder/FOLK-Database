package com.singularitycoder.folkdatabase.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.singularitycoder.folkdatabase.Helper;
import com.singularitycoder.folkdatabase.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    Context mContext;
    FloatingActionButton fab1;

    static ArrayList<PersonModel> adminList;
    static ArrayList<PersonModel> contactList;
    static ArrayList<PersonModel> memberList;
    static MembersAdapter membersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpStatusBar();
        setContentView(R.layout.activity_home);
        initToolBar();
        initViewPager();
        initTabLayout();
        mContext = this.getApplicationContext();

        fab1 = findViewById(R.id.floating_button);

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
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Brihat Mridanga");
        }
        // For back navigation button use this
        // if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViewPager() {
        final int TODAY = 0;
        final int DASHBOARD = 1;
        final int TARGETED_PLACES = 2;
        final int BOOK_VAULT = 3;
        final int TEAMS = 4;
        final int MEMBERS = 5;
        final int ADMINS = 6;

        viewPager = findViewById(R.id.viewpager_home);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case TODAY:
                        fab1.hide();
                        break;
                    case DASHBOARD:
                        fab1.hide();
                        break;
                    case TARGETED_PLACES:
                        fab1.hide();
                        break;
                    case BOOK_VAULT:
                        fab1.hide();
                        break;
                    case MEMBERS:
                        fab1.hide();
                        break;
                    case ADMINS:
                        fab1.hide();
                        break;
                    case TEAMS:
                        fab1.hide();
                        fab1.show();
                        fab1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                        fab1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "Create a Team", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
            }
        });
    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.tabs_home);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        new Helper().toast("1", getApplicationContext(), 0);
                        break;
                    case 1:
                        new Helper().toast("2", getApplicationContext(), 0);
                        break;
                    case 2:
                        new Helper().toast("3", getApplicationContext(), 0);
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

    private void setupViewPager(ViewPager viewPager) {
        // All of them will have their own respective actions for uploading n downloading etc.
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // adapter.addFrag(new AdminFragment(ContextCompat.getColor(this, R.color.bg_light_grey)), "CHATS");
        // adapter.addFrag(new AdminFragment(ContextCompat.getColor(this, R.color.bg_light_grey)), "NOTIFICATIONS");     // they must be visible
        adapter.addFrag(new TodayFragment(), "TODAY");
        adapter.addFrag(new DashboardFragment(), "DASHBOARD");
        adapter.addFrag(new TargetedPlacesFragment(), "TARGETED PLACES");
        adapter.addFrag(new BookVaultFragment(), "BOOK VAULT");
        adapter.addFrag(new TeamsFragment(), "TEAMS"); // For teams
        adapter.addFrag(new MemberFragment(), "MEMBERS");
        adapter.addFrag(new AdminFragment(), "ADMINS");

        viewPager.setAdapter(adapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // Admin Menu
            case R.id.action_admin_search:
                return true;
            case R.id.action_admin_filter:
                adminFilterDialog(this);
                return true;

            // Caller Menu
            case R.id.action_caller_search:
                return true;
            case R.id.action_caller_filter:
                callerFilterDialog(this);
                return true;

            // Contact Menu
            case R.id.action_contact_search:
                return true;
            case R.id.action__contact_filter:
                contactFilterDialog(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void aboutDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_about);

        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // for rounded corners

        TextView tvContactUs = dialog.findViewById(R.id.tv_contact_us);
        tvContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "name@emailaddress.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Feedback, Help, Report Bugs etc.");
                activity.startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        TextView tvRateUs = dialog.findViewById(R.id.tv_rate_us);
        tvRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.srilaprabhupadalila&hl=en")));
            }
        });
        TextView tvVolunteer = dialog.findViewById(R.id.tv_volunteer_appdev);
        tvVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/WCBV2q4b1ZBgDf3B9")));
            }
        });
        TextView tvDedicated = dialog.findViewById(R.id.tv_dedicated_to);
        tvDedicated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.srilaprabhupadalila.org/who-is-srila-prabhupada")));
            }
        });
        TextView tvShareApk = dialog.findViewById(R.id.tv_share_app_apk);
        tvShareApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        TextView tvShareLink = dialog.findViewById(R.id.tv_share_app_link);
        tvShareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.singularitycoder.com");
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                activity.startActivity(Intent.createChooser(shareIntent, "Share to"));
            }
        });
        dialog.show();
    }

    public void contactFilterDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_filter_contacts);

        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    public void adminFilterDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_filter_admins);

        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    /**
     * Get activity instance from desired context.
     */
    public static Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) return (Activity) context;
        if (context instanceof ContextWrapper)
            return getActivity(((ContextWrapper) context).getBaseContext());
        return null;
    }

    public void showQuickInfoDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_quick_profile);

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView imgProfilePic = dialog.findViewById(R.id.img_profile_image);
        imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
            }
        });

        ImageView imgCall = dialog.findViewById(R.id.img_quick_call);
        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "9535509155";
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(callIntent);
            }
        });

        ImageView imgSms = dialog.findViewById(R.id.img_quick_message);
        imgSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "9535509155";
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", phone);
                smsIntent.putExtra("sms_body", "Message Body check");
                smsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(smsIntent);
                }
            }
        });

        ImageView imgWhatsApp = dialog.findViewById(R.id.img_quick_whatsapp);
        imgWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getApplicationContext().getPackageManager();
                try {
                    // checks if such an app exists or not
                    packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    String phone = "9535509155";
                    Uri uri = Uri.parse("smsto:" + phone);
                    Intent whatsAppIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    whatsAppIntent.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(whatsAppIntent, "Dummy Title"));
                } catch (PackageManager.NameNotFoundException e) {
                    new Helper().toast("WhatsApp not found. Install from playstore.", getApplicationContext(), 1);
                    Uri uri = Uri.parse("market://details?id=com.whatsapp");
                    Intent openPlayStore = new Intent(Intent.ACTION_VIEW, uri);
                    openPlayStore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(openPlayStore);
                }
            }
        });

        ImageView imgEmail = dialog.findViewById(R.id.img_quick_email);
        imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "name@emailaddress.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Follow Up");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi Contact, this is telecaller...");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        ImageView imgShare = dialog.findViewById(R.id.img_quick_share);
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.singularitycoder.com");
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(Intent.createChooser(shareIntent, "Share to"));
            }
        });

        dialog.show();
    }

    public void dialogNotifications(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_notifications);

        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView imgCloseBtn = dialog.findViewById(R.id.img_dialog_close);
        imgCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ArrayList<PersonModel> notificationList = new ArrayList<>();
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));
        notificationList.add(new PersonModel("Michael Marvin", R.drawable.face2, "Team Gauranga sold 8 million books today! Hari Bol!", "19/2/20"));

        LinearLayoutManager commentLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        RecyclerView notificationsRecycler = dialog.findViewById(R.id.dialog_notif_recycler);
        notificationsRecycler.setLayoutManager(commentLayoutManager);
        notificationsRecycler.setHasFixedSize(true);
        notificationsRecycler.setItemViewCacheSize(20);
        notificationsRecycler.setDrawingCacheEnabled(true);
        notificationsRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        NotificationAdapter notificationAdapter = new NotificationAdapter(notificationList, this);
        notificationAdapter.setHasStableIds(true);

        notificationsRecycler.setAdapter(notificationAdapter);


        dialog.show();
    }

    public static class DashModel {
        int intDashImage;
        String strDashTitle;
        String strDashCount;
//        String strDashHeaderCount;

        public DashModel() {
        }

        public DashModel(String strDashCount) {
            this.strDashCount = strDashCount;
        }

        public DashModel(int intDashImage, String strDashTitle, String strDashCount) {
            this.intDashImage = intDashImage;
            this.strDashTitle = strDashTitle;
            this.strDashCount = strDashCount;
        }

        public int getIntDashImage() {
            return intDashImage;
        }

        public void setIntDashImage(int intDashImage) {
            this.intDashImage = intDashImage;
        }

        public String getStrDashTitle() {
            return strDashTitle;
        }

        public void setStrDashTitle(String strDashTitle) {
            this.strDashTitle = strDashTitle;
        }

        public String getStrDashCount() {
            return strDashCount;
        }

        public void setStrDashCount(String strDashCount) {
            this.strDashCount = strDashCount;
        }
    }

    public static class TodayModel {
        int intTodayImage;
        String strTodayTitle;
        String strTodayCount;

        public TodayModel(int intTodayImage, String strTodayTitle, String strTodayCount) {
            this.intTodayImage = intTodayImage;
            this.strTodayTitle = strTodayTitle;
            this.strTodayCount = strTodayCount;
        }

        public int getIntTodayImage() {
            return intTodayImage;
        }

        public void setIntTodayImage(int intTodayImage) {
            this.intTodayImage = intTodayImage;
        }

        public String getStrTodayTitle() {
            return strTodayTitle;
        }

        public void setStrTodayTitle(String strTodayTitle) {
            this.strTodayTitle = strTodayTitle;
        }

        public String getStrTodayCount() {
            return strTodayCount;
        }

        public void setStrTodayCount(String strTodayCount) {
            this.strTodayCount = strTodayCount;
        }
    }

    public static class TodayFragment extends Fragment {
        ArrayList<TodayModel> todayList;
        TodayAdapter mTodayAdapter;

        public TodayFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_today, container, false);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_today);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            todayList = new ArrayList<>();
            todayList.add(new TodayModel(R.drawable.ic_attach_money_black_24dp, "Lakshmi Earned", "26,500"));
            todayList.add(new TodayModel(R.drawable.ic_book_black_24dp, "Books Sold", "550"));
            todayList.add(new TodayModel(R.drawable.ic_collections_bookmark_black_24dp, "Books Taken", "900"));
            todayList.add(new TodayModel(R.drawable.ic_target_black_24dp, "Total Target Places", "13"));
            todayList.add(new TodayModel(R.drawable.ic_people_black_24dp, "Total Teams", "42"));
            todayList.add(new TodayModel(R.drawable.ic_person_black_24dp, "Total Members", "161"));

            mTodayAdapter = new TodayAdapter(todayList, getContext());
            mTodayAdapter.setHasStableIds(true);
            recyclerView.setAdapter(mTodayAdapter);
            mTodayAdapter.notifyDataSetChanged();

            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_today, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // Home Menu
                case R.id.action_notifications:
                    new HomeActivity().dialogNotifications(getActivity());
                    return true;
                case R.id.action_my_profile:
                    Intent profileIntent = new Intent(getActivity(), ProfileView.class);
                    profileIntent.putExtra("openMyProfile", "MYPROFILE");
                    startActivity(profileIntent);
                    return true;
                case R.id.action_team_rankings:
                    Intent rankIntent = new Intent(getActivity(), TeamRankingsActivity.class);
                    startActivity(rankIntent);
                    return true;
                case R.id.action_member_rankings:
                    Intent teamRankIntent = new Intent(getActivity(), MemberRankingsActivity.class);
                    startActivity(teamRankIntent);
                    return true;
                case R.id.action_about:
                    new HomeActivity().aboutDialog(getActivity());
                    return true;
                case R.id.action_change_password:
                    return true;
                case R.id.action_delete_account:
                    return true;
                case R.id.action_log_out:
                    return true;
            }
            return super.onOptionsItemSelected(item);
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


    public static class DashboardFragment extends Fragment {

        private static final String TAG = "DashboardFragment";

        ArrayList<DashModel> dashList;
        DashAdapter sDashAdapter;

        private FirebaseFirestore db;

        public DashboardFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_dash, container, false);

            db = FirebaseFirestore.getInstance();

            RecyclerView recyclerView = view.findViewById(R.id.recycler_dash);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            dashList = new ArrayList<>();
            dashList.add(new DashModel("300"));
            dashList.add(new DashModel(R.drawable.ic_book_black_24dp, "Total Books Sold", "6500"));
            dashList.add(new DashModel(R.drawable.ic_attach_money_black_24dp, "Total Lakshmi Earned", "24"));
            dashList.add(new DashModel(R.drawable.ic_location_on_black_24dp, "Total Places Targeted", "3"));
            dashList.add(new DashModel(R.drawable.ic_target_black_24dp, "This month's target", "6500"));
            dashList.add(new DashModel(R.drawable.ic_tasks_finished_black_24dp, "Tasks finished", "24"));
            dashList.add(new DashModel(R.drawable.ic_pending_black_24dp, "Tasks pending", "3"));
            dashList.add(new DashModel(R.drawable.ic_hourspent_black_24dp, "Hours spent", "54"));
            dashList.add(new DashModel(R.drawable.ic_emoji_events_24px, "Member Rank", "3"));
            dashList.add(new DashModel(R.drawable.ic_group_work_black_24dp, "Team Rank", "2"));

            // Get Dashboard data from Firestore
            db.collection("dashboard")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments(); // got all documents in DocumentSnapshot format
                                for (DocumentSnapshot d : list) {
                                    DashModel dash = d.toObject(DashModel.class);
                                    dashList.add(dash);
                                }
//                                sDashAdapter.notifyDataSetChanged();
                            }
                        }
                    });

            sDashAdapter = new DashAdapter(dashList, getContext());
            sDashAdapter.setHasStableIds(true);
            recyclerView.setAdapter(sDashAdapter);
            sDashAdapter.notifyDataSetChanged();


            return view;
        }

        // Fetch all documents inside the "Users" collection n put them in a list

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_today, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // Home Menu
                case R.id.action_notifications:
                    new HomeActivity().dialogNotifications(getActivity());
                    return true;
                case R.id.action_team_rankings:
                    Intent rankIntent = new Intent(getActivity(), MemberRankingsActivity.class);
                    startActivity(rankIntent);
                    return true;
                case R.id.action_my_profile:
                    Intent profileIntent = new Intent(getActivity(), ProfileView.class);
                    profileIntent.putExtra("openMyProfile", "MYPROFILE");
                    startActivity(profileIntent);
                    return true;
                case R.id.action_about:
                    new HomeActivity().aboutDialog(getActivity());
                    return true;
                case R.id.action_change_password:
                    return true;
                case R.id.action_delete_account:
                    return true;
                case R.id.action_log_out:
                    return true;
            }
            return super.onOptionsItemSelected(item);
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

    public static class TargetedPlacesFragment extends Fragment {
        ArrayList<PlaceModel> placeList;
        PlaceAdapter mPlaceAdapter;

        private FirebaseFirestore db;

        public TargetedPlacesFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_place, container, false);

            db = FirebaseFirestore.getInstance();

            final FrameLayout frameLayout = view.findViewById(R.id.frame_lay_place);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_place);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            placeList = new ArrayList<>();
            placeList.add(new PlaceModel("Koramangala Metro", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232", "13/03/19"));
            placeList.add(new PlaceModel("Bingo Plaza", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232", "13/03/19"));
            placeList.add(new PlaceModel("Chattinad", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232", "13/03/19"));
            placeList.add(new PlaceModel("Disny Land", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232", "13/03/19"));
            placeList.add(new PlaceModel("PVR Cinemas", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232", "13/03/19"));
            placeList.add(new PlaceModel("Ghetto", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232", "13/03/19"));
            placeList.add(new PlaceModel("Bla Bla Beach", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232", "13/03/19"));
            placeList.add(new PlaceModel("Trivi Metro", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232", "13/03/19"));
            placeList.add(new PlaceModel("Bela Binja", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232", "13/03/19"));
            placeList.add(new PlaceModel("Steam Punk Road", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232", "13/03/19"));
            placeList.add(new PlaceModel("Mahatma Gandhi Road", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232", "13/03/19"));


            // Get Place data from Firestore
//            db.collection("placesVisited")
//                    .get()
//                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            if (!queryDocumentSnapshots.isEmpty()) {
//                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments(); // got all documents in DocumentSnapshot format
//                                for (DocumentSnapshot c : list) {
//                                    PersonModel person = c.toObject(PersonModel.class);
//                                    memberList.add(person);
//                                }
////                                mPlaceAdapter.notifyDataSetChanged();
//                            }
//                        }
//                    });


            mPlaceAdapter = new PlaceAdapter(placeList, getContext());
            mPlaceAdapter.setHasStableIds(true);
//            mPlaceAdapter.setOnItemClickListener(new MembersAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
//                    // Start activity
//                    Intent contactIntent = new Intent(getContext(), ProfileView.class);
//                    contactIntent.putExtra("openContact", "CONTACT");
//                    startActivity(contactIntent);
//                }
//            });
            recyclerView.setAdapter(mPlaceAdapter);

            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_contacts, menu);
            super.onCreateOptionsMenu(menu, inflater);
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

    public static class BookVaultFragment extends Fragment {
        ArrayList<BookModel> bookList;
        BookAdapter mBookAdapter;

        public BookVaultFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_place, container, false);

            final FrameLayout frameLayout = view.findViewById(R.id.frame_lay_place);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_place);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            bookList = new ArrayList<>();
            bookList.add(new BookModel(R.drawable.bg, "Bhagavad Gita As It Is", "Books Available: 3,35,23", "Books Sold: 3,35,23,32,243"));
            bookList.add(new BookModel(R.drawable.bhag, "Bhagavatam Canto 1", "Books Available: 4,35,2", "Books Sold: 3,35,23,32,243"));
            bookList.add(new BookModel(R.drawable.bg, "Bhagavad Gita As It Is", "Books Available: 3,35,23", "Books Sold: 3,35,23,32,243"));
            bookList.add(new BookModel(R.drawable.bhag, "Bhagavatam Canto 1", "Books Available: 4,35,2", "Books Sold: 3,35,23,32,243"));
            bookList.add(new BookModel(R.drawable.bg, "Bhagavad Gita As It Is", "Books Available: 3,35,23", "Books Sold: 3,35,23,32,243"));
            bookList.add(new BookModel(R.drawable.bhag, "Bhagavatam Canto 1", "Books Available: 4,35,2", "Books Sold: 3,35,23,32,243"));
            bookList.add(new BookModel(R.drawable.bg, "Bhagavad Gita As It Is", "Books Available: 3,35,23", "Books Sold: 3,35,23,32,243"));
            bookList.add(new BookModel(R.drawable.bhag, "Bhagavatam Canto 1", "Books Available: 4,35,2", "Books Sold: 3,35,23,32,243"));
            bookList.add(new BookModel(R.drawable.bg, "Bhagavad Gita As It Is", "Books Available: 3,35,23", "Books Sold: 3,35,23,32,243"));
            bookList.add(new BookModel(R.drawable.bhag, "Bhagavatam Canto 1", "Books Available: 4,35,2", "Books Sold: 3,35,23,32,243"));
            bookList.add(new BookModel(R.drawable.bg, "Bhagavad Gita As It Is", "Books Available: 3,35,23", "Books Sold: 3,35,23,32,243"));
            bookList.add(new BookModel(R.drawable.bhag, "Bhagavatam Canto 1", "Books Available: 4,35,2", "Books Sold: 3,35,23,32,243"));

            mBookAdapter = new BookAdapter(bookList, getContext());
            mBookAdapter.setHasStableIds(true);
//            mPlaceAdapter.setOnItemClickListener(new MembersAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
//                    // Start activity
//                    Intent contactIntent = new Intent(getContext(), ProfileView.class);
//                    contactIntent.putExtra("openContact", "CONTACT");
//                    startActivity(contactIntent);
//                }
//            });
            recyclerView.setAdapter(mBookAdapter);

            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_contacts, menu);
            super.onCreateOptionsMenu(menu, inflater);
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

    public static class TeamsFragment extends Fragment {
        ArrayList<TeamModel> teamsList;
        TeamsAdapter mTeamsAdapter;

        private FirebaseFirestore db;

        public TeamsFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_team, container, false);

            db = FirebaseFirestore.getInstance();

            final FrameLayout frameLayout = view.findViewById(R.id.frame_lay_team);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_team);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            teamsList = new ArrayList<>();
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
            teamsList.add(new TeamModel("Phoenix", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
//            teamsList.add(new TeamModel(R.drawable.header2_small, "Gauranga", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
//            teamsList.add(new TeamModel(R.drawable.header_small, "Mahatmas", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
//            teamsList.add(new TeamModel(R.drawable.header2_small, "Bhaktivedantas", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
//            teamsList.add(new TeamModel(R.drawable.header_small, "Baby Krishnas", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
//            teamsList.add(new TeamModel(R.drawable.header2_small, "Mega Bhagavats", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
//            teamsList.add(new TeamModel(R.drawable.header_small, "Worshippers", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
//            teamsList.add(new TeamModel(R.drawable.header2_small, "Dragons", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
//            teamsList.add(new TeamModel(R.drawable.header_small, "Mala Mala", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));
//            teamsList.add(new TeamModel(R.drawable.header2_small, "Kala Jala Tala", "Books Sold: 343", "Lakshmi Earned: Rs 13,414,232"));


            mTeamsAdapter = new TeamsAdapter(teamsList, getContext());
            mTeamsAdapter.setHasStableIds(true);
//            mPlaceAdapter.setOnItemClickListener(new MembersAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
//                    // Start activity
//                    Intent contactIntent = new Intent(getContext(), ProfileView.class);
//                    contactIntent.putExtra("openContact", "CONTACT");
//                    startActivity(contactIntent);
//                }
//            });
            recyclerView.setAdapter(mTeamsAdapter);

            // Get Team data from Firestore
//            db.collection("teams")
//                    .get()
//                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            if (!queryDocumentSnapshots.isEmpty()) {
//                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments(); // got all documents in DocumentSnapshot format
//                                for (DocumentSnapshot t : list) {
//                                    TeamModel team = t.toObject(TeamModel.class);
//                                    teamsList.add(team);
//                                }
//                                mTeamsAdapter.notifyDataSetChanged();
//                            }
//                        }
//                    });


            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_admin, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }


    public static class AdminFragment extends Fragment {
        int color;

        public AdminFragment() {
        }

        @SuppressLint("ValidFragment")
        public AdminFragment(int color) {
            this.color = color;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);

            final FrameLayout frameLayout = view.findViewById(R.id.frame_lay_person);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_person);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            adminList = new ArrayList<>();
            adminList.add(new PersonModel(R.drawable.face2, "Gauranga Das", "Books Sold: 413", "Lakshmi Earned: 4,32,423"));
            adminList.add(new PersonModel(R.drawable.face2, "Gauranga Das", "Books Sold: 413", "Lakshmi Earned: 4,32,423"));
            adminList.add(new PersonModel(R.drawable.face2, "Gauranga Das", "Books Sold: 413", "Lakshmi Earned: 4,32,423"));


            membersAdapter = new MembersAdapter(getContext(), adminList);
            membersAdapter.setHasStableIds(true);
            membersAdapter.setOnItemClickListener(new MembersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    // Start activity
                    Intent adminIntent = new Intent(getContext(), ProfileView.class);
                    adminIntent.putExtra("openAdmin", "ADMIN");
                    startActivity(adminIntent);
                }
            });
            recyclerView.setAdapter(membersAdapter);


            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_admin, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    public static class ContactFragment extends Fragment {
        int color;

        public ContactFragment() {
        }

        @SuppressLint("ValidFragment")
        public ContactFragment(int color) {
            this.color = color;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);

            final FrameLayout frameLayout = view.findViewById(R.id.frame_lay_person);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_person);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            contactList = new ArrayList<>();
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));
            contactList.add(new PersonModel(R.drawable.face1, "Catherine Millers", "Program: Yoga for Happiness", "Registered Date: July 15, 4019 - 10:15 AM"));

            membersAdapter = new MembersAdapter(getContext(), contactList);
            membersAdapter.setHasStableIds(true);
            membersAdapter.setOnItemClickListener(new MembersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    // Start activity
                    Intent contactIntent = new Intent(getContext(), ProfileView.class);
                    contactIntent.putExtra("openContact", "CONTACT");
                    startActivity(contactIntent);
                }
            });
            recyclerView.setAdapter(membersAdapter);

            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_contacts, menu);
            super.onCreateOptionsMenu(menu, inflater);
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


    public static class MemberFragment extends Fragment {
        int color;

        private FirebaseFirestore db;

        public MemberFragment() {
        }

        @SuppressLint("ValidFragment")
        public MemberFragment(int color) {
            this.color = color;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);

            db = FirebaseFirestore.getInstance();

            final FrameLayout frameLayout = view.findViewById(R.id.frame_lay_person);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_person);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            memberList = new ArrayList<>();
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Books Sold: 234", "Lakshmi Earned: 52,352"));

            // Get Member data from Firestore
//            db.collection("users")
//                    .get()
//                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            if (!queryDocumentSnapshots.isEmpty()) {
//                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments(); // got all documents in DocumentSnapshot format
//                                for (DocumentSnapshot c : list) {
//                                    PersonModel person = c.toObject(PersonModel.class);
//                                    memberList.add(person);
//                                }
////                                sDashAdapter.notifyDataSetChanged();
//                            }
//                        }
//                    });

            membersAdapter = new MembersAdapter(getContext(), memberList);
            membersAdapter.setHasStableIds(true);
            membersAdapter.setOnItemClickListener(new MembersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    // Start activity
                    Intent callerIntent = new Intent(getContext(), ProfileView.class);
                    callerIntent.putExtra("OpenMember", "MEMBER");
                    startActivity(callerIntent);
                }
            });
            recyclerView.setAdapter(membersAdapter);

            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_callers, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    public static class MyTasksFragment extends Fragment {
        int color;

        public MyTasksFragment() {
        }

        @SuppressLint("ValidFragment")
        public MyTasksFragment(int color) {
            this.color = color;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);

            final FrameLayout frameLayout = view.findViewById(R.id.frame_lay_person);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_person);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            memberList = new ArrayList<>();
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));

            membersAdapter = new MembersAdapter(getContext(), memberList);
            membersAdapter.setHasStableIds(true);
            membersAdapter.setOnItemClickListener(new MembersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    // Start activity
                    Intent callerIntent = new Intent(getContext(), ProfileView.class);
                    callerIntent.putExtra("openCaller", "CALLER");
                    startActivity(callerIntent);
                }
            });
            recyclerView.setAdapter(membersAdapter);

            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_callers, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    public static class AssignedTasksFragment extends Fragment {
        int color;

        public AssignedTasksFragment() {
        }

        @SuppressLint("ValidFragment")
        public AssignedTasksFragment(int color) {
            this.color = color;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);

            final FrameLayout frameLayout = view.findViewById(R.id.frame_lay_person);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_person);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            memberList = new ArrayList<>();
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));

            membersAdapter = new MembersAdapter(getContext(), memberList);
            membersAdapter.setHasStableIds(true);
            membersAdapter.setOnItemClickListener(new MembersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    // Start activity
                    Intent callerIntent = new Intent(getContext(), ProfileView.class);
                    callerIntent.putExtra("openCaller", "CALLER");
                    startActivity(callerIntent);
                }
            });
            recyclerView.setAdapter(membersAdapter);

            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_callers, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    public static class CallHistoryFragment extends Fragment {
        int color;

        public CallHistoryFragment() {
        }

        @SuppressLint("ValidFragment")
        public CallHistoryFragment(int color) {
            this.color = color;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);

            final FrameLayout frameLayout = view.findViewById(R.id.frame_lay_person);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_person);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            memberList = new ArrayList<>();
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));
            memberList.add(new PersonModel(R.drawable.face3, "Miki Mathews", "Position: Folk Member", "Availability: 9 AM to 3 PM"));

            membersAdapter = new MembersAdapter(getContext(), memberList);
            membersAdapter.setHasStableIds(true);
            membersAdapter.setOnItemClickListener(new MembersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    // Start activity
                    Intent callerIntent = new Intent(getContext(), ProfileView.class);
                    callerIntent.putExtra("openCaller", "CALLER");
                    startActivity(callerIntent);
                }
            });
            recyclerView.setAdapter(membersAdapter);

            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_callers, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    public static class SmsFragment extends Fragment {
        ArrayList<PersonModel> smsList;
        SmsAdapter smsAdapter;

        public SmsFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_person);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            smsList = new ArrayList<>();
            smsList.add(new PersonModel("Jasmin Jamon", R.drawable.face3, "Hey Jasmin, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "19/2/19", null));
            smsList.add(new PersonModel("Marvin Michael", R.drawable.face2, "Hey Marvin, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "21/6/20", "7"));
            smsList.add(new PersonModel("Juniper Jupiter", R.drawable.face1, "Hey Juniper, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "7/2/20", null));
            smsList.add(new PersonModel("Jasmin Jamon", R.drawable.face3, "Hey Jasmin, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "19/2/19", "3"));
            smsList.add(new PersonModel("Marvin Michael", R.drawable.face2, "Hey Marvin, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "21/6/20", "7"));
            smsList.add(new PersonModel("Juniper Jupiter", R.drawable.face1, "Hey Juniper, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "7/2/20", null));
            smsList.add(new PersonModel("Jasmin Jamon", R.drawable.face3, "Hey Jasmin, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "19/2/19", "3"));
            smsList.add(new PersonModel("Marvin Michael", R.drawable.face2, "Hey Marvin, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "21/6/20", "7"));
            smsList.add(new PersonModel("Juniper Jupiter", R.drawable.face1, "Hey Juniper, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "7/2/20", "1"));
            smsList.add(new PersonModel("Jasmin Jamon", R.drawable.face3, "Hey Jasmin, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "19/2/19", "3"));
            smsList.add(new PersonModel("Marvin Michael", R.drawable.face2, "Hey Marvin, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "21/6/20", "7"));
            smsList.add(new PersonModel("Juniper Jupiter", R.drawable.face1, "Hey Juniper, what happened to the 2 million contacts i told u to call in 3 minutes? Huhahahahah huahahaha huahahahahahahahaaaaaaaaa!", "7/2/20", "1"));


            smsAdapter = new SmsAdapter(smsList, getContext());
            smsAdapter.setHasStableIds(true);

            recyclerView.setAdapter(smsAdapter);

            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_callers, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }


}
