package com.singularitycoder.folkdatabase.home;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.singularitycoder.folkdatabase.helper.Helper;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

import static com.singularitycoder.folkdatabase.helper.Helper.hasInternet;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    Context mContext;
    FloatingActionButton fab1;

    static ArrayList<PersonModel> adminList;
    static ArrayList<PersonModel> memberList;


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
            getSupportActionBar().setTitle("FOLK Database");
        }
    }

    private void initViewPager() {

        final int CONTACTS = 0;
        final int FOLK_GUIDES = 1;
        final int TEAM_LEADS = 2;
        final int ZONAL_HEADS = 3;

        viewPager = findViewById(R.id.viewpager_home);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case CONTACTS:
                        fab1.show();
                        fab1.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_list_black_24dp));
                        fab1.setOnClickListener(view -> {
                            Toast.makeText(getApplicationContext(), "Filter Contacts", Toast.LENGTH_SHORT).show();
                        });
                        break;
                    case FOLK_GUIDES:
                        fab1.hide();
                        break;
                    case TEAM_LEADS:
                        fab1.hide();
                        break;
                    case ZONAL_HEADS:
                        fab1.hide();
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ContactFragment(), "CONTACTS");
        adapter.addFrag(new FolkGuidesFragment(), "FOLK GUIDES");
        adapter.addFrag(new TeamLeadsFragment(), "TEAM LEADS");
        adapter.addFrag(new ZonalHeadsFragment(), "ZONAL HEADS");
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
//        getMenuInflater().inflate(R.menu.menu_folk_guides, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // Admin Menu
//            case R.id.action_admin_search:
//                return true;
//            case R.id.action_admin_filter:
//                adminFilterDialog(this);
//                return true;
//
//            // Caller Menu
//            case R.id.action_caller_search:
//                return true;
//            case R.id.action_caller_filter:
//                callerFilterDialog(this);
//                return true;

            // Contact Menu
            case R.id.action_contact_search:
                return true;
            case R.id.action_notifications:
                dialogNotifications(this);
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

        // if today's date matches birthday - show notif badge
        // Show all birthday's in the notifications


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

    public static class ContactFragment extends Fragment {

        private static final String TAG = "ContactFragment";
        private ArrayList<PersonModel> contactList;
        RecyclerView recyclerView;
        private ContactsAdapter contactsAdapter;
        private ProgressBar progressBar;
        private PersonModel personItemModel;

        private SwipeRefreshLayout swipeRefreshLayout;
        private TextView noInternetText;


        public ContactFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);

            recyclerView = view.findViewById(R.id.recycler_person);
            progressBar = view.findViewById(R.id.progress_circular);
            noInternetText = view.findViewById(R.id.tv_no_internet);

            swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            swipeRefreshLayout.setOnRefreshListener(() -> {
                progressBar.setVisibility(View.VISIBLE);
                getData(getActivity());
            });

            getData(getActivity());
            setUpRecyclerView();


            return view;
        }

        private void setUpRecyclerView() {
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

            contactsAdapter = new ContactsAdapter(getContext(), contactList);
            contactsAdapter.setHasStableIds(true);
            contactsAdapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    // Start activity
                    Intent contactIntent = new Intent(getContext(), ProfileActivity.class);
                    contactIntent.putExtra("openContact", "CONTACT");
                    startActivity(contactIntent);
                }
            });
            recyclerView.setAdapter(contactsAdapter);
        }

        public void getData(final Context context) {
            if (hasInternet(context)) {
                Log.d(TAG, "hit 1");
                setUpRecyclerView();
                readContactsData();
                noInternetText.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                contactsAdapter.notifyDataSetChanged();
            } else {
                noInternetText.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        }

        // READ
        private void readContactsData() {
            FirebaseFirestore.getInstance().collection("FolkMembers").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            progressBar.setVisibility(View.GONE);
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot docSnap : docList) {
                                    personItemModel = docSnap.toObject(PersonModel.class);
                                    if (personItemModel != null) {
                                        Log.d(TAG, "personItem: " + personItemModel);
                                        personItemModel.setId(docSnap.getId());
                                    }
                                    Log.d(TAG, "firedoc id: " + docSnap.getId());
                                    contactList.add(personItemModel);
                                }
                                contactsAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't get data!", Toast.LENGTH_SHORT).show();
                        }
                    });
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

    public static class FolkGuidesFragment extends Fragment {
        private ContactsAdapter contactsAdapter;

        public FolkGuidesFragment() {
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

            adminList = new ArrayList<>();
            adminList.add(new PersonModel(R.drawable.face2, "Gauranga Das", "Books Sold: 413", "Lakshmi Earned: 4,32,423"));
            adminList.add(new PersonModel(R.drawable.face2, "Gauranga Das", "Books Sold: 413", "Lakshmi Earned: 4,32,423"));
            adminList.add(new PersonModel(R.drawable.face2, "Gauranga Das", "Books Sold: 413", "Lakshmi Earned: 4,32,423"));


            contactsAdapter = new ContactsAdapter(getContext(), adminList);
            contactsAdapter.setHasStableIds(true);
            contactsAdapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    // Start activity
                    Intent adminIntent = new Intent(getContext(), ProfileActivity.class);
                    adminIntent.putExtra("openAdmin", "ADMIN");
                    startActivity(adminIntent);
                }
            });
            recyclerView.setAdapter(contactsAdapter);


            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_folk_guides, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    public static class TeamLeadsFragment extends Fragment {

        public TeamLeadsFragment() {
        }
    }

    public static class ZonalHeadsFragment extends Fragment {

        public ZonalHeadsFragment() {
        }
    }


}
