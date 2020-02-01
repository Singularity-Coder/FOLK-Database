package com.singularitycoder.folkdatabase.database;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.helper.HelperCustomEditText;
import com.singularitycoder.folkdatabase.helper.HelperFrescoImageViewer;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.singularitycoder.folkdatabase.helper.HelperGeneral.hasInternet;
import static java.lang.String.valueOf;

public class DatabaseActivity extends AppCompatActivity {

    private static final String TAG = "DatabaseActivity";
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
                            new ContactFragment().contactFilterDialog(DatabaseActivity.this);
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


    ////////////////////////////////////////////////////// FRAGMENT 1
    public static class ContactFragment extends Fragment {

        private static final String TAG = "ContactFragment";
        private ArrayList<ContactItem> contactList;
        private RecyclerView recyclerView;
        private ContactsAdapter contactsAdapter;
        private ProgressDialog loadingBar;
        private ContactItem personItemModel;
        private SwipeRefreshLayout swipeRefreshLayout;
        private TextView noInternetText;
        private TextView tvListCount;
        private String strOldPassword;
        private ShimmerFrameLayout shimmerFrameLayout;
        private LottieAnimationView noFeedImage;
        private TextView noFeedText;

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
            if (null != getActivity()) {
                init(view);
                setRefreshLayout();
                getData(getActivity());
                setUpRecyclerView();
                Log.d(TAG, "onCreateView: current date: " + HelperGeneral.currentDate());
            }
            return view;
        }


        private void init(View view) {
            if (null != getActivity()) {
                Fresco.initialize(Objects.requireNonNull(getActivity()));
            }
            recyclerView = view.findViewById(R.id.recycler_person);
//            progressBar = view.findViewById(R.id.progress_circular);
            noInternetText = view.findViewById(R.id.tv_no_internet);

            noFeedText = view.findViewById(R.id.tv_no_feed_text);
            noFeedImage = view.findViewById(R.id.img_no_feed_lottie_image);

            noFeedImage.setAnimation(R.raw.empty_box);
            noFeedImage.playAnimation();

            tvListCount = view.findViewById(R.id.tv_list_count);
            shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
            if (null != getActivity()) {
                loadingBar = new ProgressDialog(getActivity());
            }
            swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        }


        private void setRefreshLayout() {
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            swipeRefreshLayout.setOnRefreshListener(() -> {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                if (null != getActivity()) {
                    getData(getActivity());
                }
            });
        }


        private void getData(final Context context) {
            if (hasInternet(context)) {
                setUpRecyclerView();
//                new ReadContactsAsync().execute();
                AsyncTask.execute(this::readContactsData);
                noInternetText.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                contactsAdapter.notifyDataSetChanged();
            } else {
                noInternetText.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        }


        private void setUpRecyclerView() {
            if (null != getActivity()) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                contactList = new ArrayList<>();
                contactsAdapter = new ContactsAdapter(getContext(), contactList);
                contactsAdapter.setHasStableIds(true);
                contactsAdapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                        // Start activity
                        ContactItem contactItem = contactList.get(position);
                        Intent intent = new Intent(getContext(), ProfileActivity.class);
                        intent.putExtra("profileKey", "CONTACT");
                        intent.putExtra("contactItem", contactItem);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(contactsAdapter);
            }
        }


        class ReadContactsAsync extends AsyncTask<String, Integer, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                readContactsData();
                return "Done";
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }


        // READ
        private void readContactsData() {
            if (null != getActivity()) {
                SharedPreferences sp = getActivity().getSharedPreferences("authItem", Context.MODE_PRIVATE);
                String folkGuideAbbr = sp.getString("folkGuideAbbr", "");
                String zone = sp.getString("zone", "");

            FirebaseFirestore.getInstance()
                    .collection(HelperConstants.COLL_FOLK_NEW_MEMBERS)
                    .whereEqualTo("folk_guide", folkGuideAbbr)
                    .whereEqualTo("zone", zone)
                    .get()
//            FirebaseFirestore.getInstance().collection(HelperConstants.COLL_FOLK_NEW_MEMBERS).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                            Log.d(TAG, "docList: " + docList);

                            for (DocumentSnapshot docSnap : docList) {
                                personItemModel = docSnap.toObject(ContactItem.class);
                                if (personItemModel != null) {
                                    Log.d(TAG, "personItem: " + personItemModel);

                                    if (!("").equals(valueOf(docSnap.getId()))) {
                                        personItemModel.setId(docSnap.getId());
                                    }

                                    if (!("").equals(valueOf(docSnap.getString("name")))) {
                                        personItemModel.setFirstName(valueOf(docSnap.getString("name")));
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
                                        personItemModel.setStrRecidencyInterest(valueOf(docSnap.getString("folk_residency_interest")));
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
                                contactList.add(personItemModel);
                                tvListCount.setText(contactList.size() + " Contacts");
                            }
                            contactsAdapter.notifyDataSetChanged();
                            if (null != getActivity()) {
                                Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
                            }

                            if (contactList.size() == 0) {
                                noFeedImage.setVisibility(View.VISIBLE);
                                noFeedText.setVisibility(View.VISIBLE);
                            } else {
                                noFeedImage.setVisibility(View.GONE);
                                noFeedText.setVisibility(View.GONE);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (null != getActivity()) {
                            Toast.makeText(getActivity(), "Couldn't get data!", Toast.LENGTH_SHORT).show();
                        }
                        noFeedImage.setVisibility(View.VISIBLE);
                        noFeedText.setVisibility(View.VISIBLE);
                    });
            }
        }


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_contacts, menu);

            MenuItem searchItem = menu.findItem(R.id.action_contact_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setQueryHint("Search Contacts");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchContacts(newText);
                    return false;
                }
            });

            super.onCreateOptionsMenu(menu, inflater);
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_contact_search:
                    return true;
                case R.id.action__contact_filter:
                    if (null != getActivity()) {
                        contactFilterDialog(getActivity());
                    }
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }


        private void searchContacts(String text) {
            ArrayList<ContactItem> filterdUsers = new ArrayList<>();
            //looping through existing elements
            for (ContactItem user : contactList) {
                if (user.getFirstName().toLowerCase().trim().contains(text.toLowerCase())) {
                    filterdUsers.add(user);
                }
            }
            contactsAdapter.flterList(filterdUsers);
            contactsAdapter.notifyDataSetChanged();
            tvListCount.setText(filterdUsers.size() + " Contacts");
        }


        private void contactFilterDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_filter_contacts);

            Rect displayRectangle = new Rect();
            Window window = activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            Objects.requireNonNull(dialog.getWindow()).setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ImageView imgClose = dialog.findViewById(R.id.iv_dialog_close);
            TextView tvResetContacts = dialog.findViewById(R.id.tv_dialog_reset);
            TextView tvFolkGuides = dialog.findViewById(R.id.tv_dialog_folk_guides);
            TextView tvRecidencyInterest = dialog.findViewById(R.id.tv_dialog_recidency_interest);
//            TextView tvTalentName = dialog.findViewById(R.id.tv_talent_name);
//            TextView tvTalentLevel = dialog.findViewById(R.id.tv_talent_level);

            HelperCustomEditText etCanCookFor = dialog.findViewById(R.id.et_talent_can_cook_for);
            HelperCustomEditText etCookingSelfRating = dialog.findViewById(R.id.tv_talent_cooking_self_rating);
            HelperCustomEditText etSouthIndian = dialog.findViewById(R.id.tv_talent_south_indian);
            HelperCustomEditText etDisclose = dialog.findViewById(R.id.tv_talent_disclose);
            HelperCustomEditText etSportsCollegeLevel = dialog.findViewById(R.id.tv_talent_college_level);
            HelperCustomEditText etSportsDirectLevel = dialog.findViewById(R.id.tv_talent_district_level);

            HelperCustomEditText etLocation = dialog.findViewById(R.id.et_dialog_location);
            HelperCustomEditText etDobMonth = dialog.findViewById(R.id.et_dialog_dob_month);
            Button btnApply = dialog.findViewById(R.id.btn_dialog_apply);

            imgClose.setOnClickListener(view -> dialog.dismiss());

            tvResetContacts.setOnClickListener(view -> {
                tvFolkGuides.setText("");
                contactsAdapter.flterList(contactList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(valueOf(contactList.size()) + " Contacts");
                dialog.dismiss();
            });

            etCanCookFor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "afterTextChanged: editable val: " + valueOf(editable));
                    findCanCookForContacts(valueOf(editable));
                }
            });

            etCookingSelfRating.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "afterTextChanged: editable val: " + valueOf(editable));
                    findCookingSelfRatingContacts(valueOf(editable));
                }
            });

            etSouthIndian.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "afterTextChanged: editable val: " + valueOf(editable));
                    findCanCookSouthIndianContacts(valueOf(editable));
                }
            });

            etDisclose.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "afterTextChanged: editable val: " + valueOf(editable));
                    findTalentDiscloseContacts(valueOf(editable));
                }
            });


            etSportsCollegeLevel.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "afterTextChanged: editable val: " + valueOf(editable));
                    findSportsCollegeLevelContacts(valueOf(editable));
                }
            });


            etSportsDirectLevel.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "afterTextChanged: editable val: " + valueOf(editable));
                    findSportsDistrictLevelContacts(valueOf(editable));
                }
            });


            etDobMonth.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "afterTextChanged: editable val: " + valueOf(editable));
                    findDobMonthContacts(valueOf(editable));
                }
            });

            etLocation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "afterTextChanged: editable val: " + valueOf(editable));
                    findLocationContacts(valueOf(editable));
                }
            });

            tvRecidencyInterest.setOnClickListener(view -> residencyInterest(tvRecidencyInterest));
            tvRecidencyInterest.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "afterTextChanged: editable val: " + valueOf(editable));
                    findRecidencyInterestedContacts(valueOf(editable));
                }
            });

            tvFolkGuides.setOnClickListener(view -> {
                folkGuideList(tvFolkGuides);
                Log.d(TAG, "Folk guides value: " + valueOf(tvFolkGuides.getText()));
            });
            tvFolkGuides.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "afterTextChanged: editable val: " + valueOf(editable));
                    findFolkGuideContacts(valueOf(editable));
                }
            });


            btnApply.setOnClickListener(view -> dialog.dismiss());

            dialog.show();
        }

        private void findCanCookForContacts(String text) {
            ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

            for (ContactItem contact : contactList) {
                if (text.toLowerCase().trim().contains(valueOf(contact.getStrCanCookFor()).toLowerCase().trim())) {
                    filteredContactsList.add(contact);
                    Log.d(TAG, "list: " + filteredContactsList);
                }
                contactsAdapter.flterList(filteredContactsList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(filteredContactsList.size() + " Contacts");
            }
        }

        private void findCookingSelfRatingContacts(String text) {
            ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

            for (ContactItem contact : contactList) {
                if (text.toLowerCase().trim().contains(valueOf(contact.getStrSelfRating()).toLowerCase().trim())) {
                    filteredContactsList.add(contact);
                    Log.d(TAG, "list: " + filteredContactsList);
                }
                contactsAdapter.flterList(filteredContactsList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(filteredContactsList.size() + " Contacts");
            }
        }

        private void findCanCookSouthIndianContacts(String text) {
            ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

            for (ContactItem contact : contactList) {
                if (text.toLowerCase().trim().contains(valueOf(contact.getStrCanCookSouthIndian()).toLowerCase().trim())) {
                    filteredContactsList.add(contact);
                    Log.d(TAG, "list: " + filteredContactsList);
                }
                contactsAdapter.flterList(filteredContactsList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(filteredContactsList.size() + " Contacts");
            }
        }

        private void findTalentDiscloseContacts(String text) {
            ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

            for (ContactItem contact : contactList) {
                if (text.toLowerCase().trim().contains(valueOf(contact.getStrTalentDisclose()).toLowerCase().trim())) {
                    filteredContactsList.add(contact);
                    Log.d(TAG, "list: " + filteredContactsList);
                }
                contactsAdapter.flterList(filteredContactsList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(filteredContactsList.size() + " Contacts");
            }
        }

        private void findSportsCollegeLevelContacts(String text) {
            ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

            for (ContactItem contact : contactList) {
                if (text.toLowerCase().trim().contains(valueOf(contact.getStrCollegeLevel()).toLowerCase().trim())) {
                    filteredContactsList.add(contact);
                    Log.d(TAG, "list: " + filteredContactsList);
                }
                contactsAdapter.flterList(filteredContactsList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(filteredContactsList.size() + " Contacts");
            }
        }

        private void findSportsDistrictLevelContacts(String text) {
            ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

            for (ContactItem contact : contactList) {
                if (text.toLowerCase().trim().contains(valueOf(contact.getStrDistrictLevel()).toLowerCase().trim())) {
                    filteredContactsList.add(contact);
                    Log.d(TAG, "list: " + filteredContactsList);
                }
                contactsAdapter.flterList(filteredContactsList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(filteredContactsList.size() + " Contacts");
            }
        }


        private void folkGuideList(TextView tvFolkGuide) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            builder.setTitle("Choose a FOLK Guide");
            String[] selectArray = {"SRRD", "VGND", "LMND", "SUVD", "HMCD", "KMPD", "CTCD", "Mataji", "No FOLK Guide"};
            builder.setItems(selectArray, (dialog, which) -> {
                switch (which) {
                    case 0:
                        tvFolkGuide.setText("SRRD");
                        Log.d(TAG, "Folk guides value 1: " + valueOf(tvFolkGuide.getText()));
                        break;
                    case 1:
                        tvFolkGuide.setText("VGND");
                        Log.d(TAG, "Folk guides value 2: " + valueOf(tvFolkGuide.getText()));
                        break;
                    case 2:
                        tvFolkGuide.setText("LMND");
                        Log.d(TAG, "Folk guides value 3: " + valueOf(tvFolkGuide.getText()));
                        break;
                    case 3:
                        tvFolkGuide.setText("SUVD");
                        Log.d(TAG, "Folk guides value 4: " + valueOf(tvFolkGuide.getText()));
                        break;
                    case 4:
                        tvFolkGuide.setText("HMCD");
                        Log.d(TAG, "Folk guides value 5: " + valueOf(tvFolkGuide.getText()));
                        break;
                    case 5:
                        tvFolkGuide.setText("KMPD");
                        Log.d(TAG, "Folk guides value 6: " + valueOf(tvFolkGuide.getText()));
                        break;
                    case 6:
                        tvFolkGuide.setText("CTCD");
                        Log.d(TAG, "Folk guides value 7: " + valueOf(tvFolkGuide.getText()));
                        break;
                    case 7:
                        tvFolkGuide.setText("Mataji");
                        Log.d(TAG, "Folk guides value 8: " + valueOf(tvFolkGuide.getText()));
                        break;
                    case 8:
                        tvFolkGuide.setText("No FOLK Guide");
                        Log.d(TAG, "Folk guides value 9: " + valueOf(tvFolkGuide.getText()));
                        break;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }


        private void residencyInterest(TextView tvResidencyInterest) {
            if (null != getActivity()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                builder.setTitle("Interested in Recidency?");
                String[] selectArray = {"YES", "NO"};
                builder.setItems(selectArray, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            tvResidencyInterest.setText("YES");
                            Log.d(TAG, "Recidency Interest 1: " + valueOf(tvResidencyInterest.getText()));
                            break;
                        case 1:
                            tvResidencyInterest.setText("NO");
                            Log.d(TAG, "Recidency Interest 2: " + valueOf(tvResidencyInterest.getText()));
                            break;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        private void findFolkGuideContacts(String text) {
            ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

            for (ContactItem contact : contactList) {
                if (text.toLowerCase().trim().contains(valueOf(contact.getStrFolkGuide()).toLowerCase().trim())) {
                    filteredContactsList.add(contact);
                    Log.d(TAG, "list: " + filteredContactsList);
                }
                contactsAdapter.flterList(filteredContactsList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(filteredContactsList.size() + " Contacts");
            }
        }

        private void findRecidencyInterestedContacts(String text) {
            ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

            for (ContactItem contact : contactList) {
                if (text.toLowerCase().trim().contains(valueOf(contact.getStrRecidencyInterest()).toLowerCase().trim())) {
                    filteredContactsList.add(contact);
                    Log.d(TAG, "list: " + filteredContactsList);
                }
                contactsAdapter.flterList(filteredContactsList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(filteredContactsList.size() + " Contacts");
            }
        }

        private void findDobMonthContacts(String text) {
            ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

            Log.d(TAG, "findDobMonthContacts: full list: " + contactList);

            for (ContactItem contact : contactList) {
                Log.d(TAG, "findDobMonthContacts: contact: " + contact);
                if (!("").equals(valueOf(contact.getStrDobMonth()).toLowerCase().trim()) || (null) != valueOf(contact.getStrDobMonth()).toLowerCase().trim()) {
                    Log.d(TAG, "findDobMonthContacts: got hit 1");
                    if (text.toLowerCase().trim().contains(valueOf(contact.getStrDobMonth()).toLowerCase().trim())) {
                        Log.d(TAG, "findDobMonthContacts: got hit 2");
                        filteredContactsList.add(contact);
                        Log.d(TAG, "filtered list: " + filteredContactsList);
                    }
                    contactsAdapter.flterList(filteredContactsList);
                    contactsAdapter.notifyDataSetChanged();
                    tvListCount.setText(filteredContactsList.size() + " Contacts");
                }
            }
        }

        private void findLocationContacts(String text) {
            ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

            for (ContactItem contact : contactList) {
                if (text.toLowerCase().trim().contains(valueOf(contact.getStrLocation()).toLowerCase().trim())) {
                    filteredContactsList.add(contact);
                    Log.d(TAG, "list: " + filteredContactsList);
                }
                contactsAdapter.flterList(filteredContactsList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(filteredContactsList.size() + " Contacts");
            }
        }


        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
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


    ////////////////////////////////////////////////////// FRAGMENT 2
    public static class FolkGuidesFragment extends Fragment {

        private static final String TAG = "FolkGuidesFragment";
        private ArrayList<FolkGuideItem> folkGuidesList;
        private RecyclerView recyclerView;
        private FolkGuidesAdapter folkGuidesAdapter;
        private FolkGuideItem folkGuideItem;
        private ProgressDialog loadingBar;
        private SwipeRefreshLayout swipeRefreshLayout;
        private TextView noInternetText;
        private TextView tvListCount;
        private ShimmerFrameLayout shimmerFrameLayout;
        private LottieAnimationView noFeedImage;
        private TextView noFeedText;

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
            if (null != getActivity()) {
                init(view);
                setRefreshLayout();
                getData(getActivity());
                setUpRecyclerView();
            }
            return view;
        }

        private void init(View view) {
            recyclerView = view.findViewById(R.id.recycler_person);
            noInternetText = view.findViewById(R.id.tv_no_internet);

            noFeedText = view.findViewById(R.id.tv_no_feed_text);
            noFeedImage = view.findViewById(R.id.img_no_feed_lottie_image);
            noFeedImage.setAnimation(R.raw.empty_box);
            noFeedImage.playAnimation();

            tvListCount = view.findViewById(R.id.tv_list_count);
            shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
            if (null != getActivity()) {
                loadingBar = new ProgressDialog(getActivity());
            }
            swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        }

        private void setRefreshLayout() {
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            swipeRefreshLayout.setOnRefreshListener(() -> {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                if (null != getActivity()) {
                    getData(getActivity());
                }
            });
        }

        private void setUpRecyclerView() {
            if (null != getActivity()) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Objects.requireNonNull(getActivity()).getBaseContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                folkGuidesList = new ArrayList<>();

                folkGuidesAdapter = new FolkGuidesAdapter(getContext(), folkGuidesList);
                folkGuidesAdapter.setHasStableIds(true);
                folkGuidesAdapter.setOnItemClickListener(new FolkGuidesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                        // Start activity
                        Intent intent = new Intent(getContext(), ProfileActivity.class);
                        FolkGuideItem folkGuideItem = folkGuidesList.get(position);
                        intent.putExtra("profileKey", "FOLKGUIDE");
                        intent.putExtra("folkguideItem", folkGuideItem);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(folkGuidesAdapter);
            }
        }

        private void getData(final Context context) {
            if (hasInternet(context)) {
                Log.d(TAG, "hit 1");
                setUpRecyclerView();
                AsyncTask.execute(this::readFolkGuidesData);
                noInternetText.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                folkGuidesAdapter.notifyDataSetChanged();
            } else {
                noInternetText.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        }

        // READ
        private void readFolkGuidesData() {
            if (null != getActivity()) {
                SharedPreferences sp = getActivity().getSharedPreferences("authItem", Context.MODE_PRIVATE);
                String zone = sp.getString("zone", "");

                FirebaseFirestore.getInstance()
                        .collection(HelperConstants.COLL_AUTH_FOLK_GUIDES)
                        .whereEqualTo("zone", zone)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            shimmerFrameLayout.setVisibility(View.GONE);
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                                Log.d(TAG, "docList: " + docList);

                                for (DocumentSnapshot docSnap : docList) {
                                    folkGuideItem = docSnap.toObject(FolkGuideItem.class);
                                    if (folkGuideItem != null) {
                                        Log.d(TAG, "personItem: " + folkGuideItem);
                                        folkGuideItem.setId(docSnap.getId());
                                        folkGuideItem.setStrFirstName(docSnap.getString(HelperConstants.KEY_FG_NAME));
                                        folkGuideItem.setStrFolkGuideAbbr(docSnap.getString(HelperConstants.KEY_FG_ABBR));
                                        folkGuideItem.setStrZone(docSnap.getString(HelperConstants.KEY_ZONE));
                                        folkGuideItem.setStrPhone(docSnap.getString(HelperConstants.KEY_MOBILE_NUMBER));
                                        folkGuideItem.setStrWhatsApp(docSnap.getString(HelperConstants.KEY_MOBILE_NUMBER));
                                        folkGuideItem.setStrEmail(docSnap.getString(HelperConstants.KEY_EMAIL));
                                    }
                                    Log.d(TAG, "firedoc id: " + docSnap.getId());
                                    folkGuidesList.add(folkGuideItem);
                                    tvListCount.setText(folkGuidesList.size() + " FOLK Guides");
                                }
                                folkGuidesAdapter.notifyDataSetChanged();
                                if (null != getActivity()) {
                                    Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (folkGuidesList.size() == 0) {
                                noFeedImage.setVisibility(View.VISIBLE);
                                noFeedText.setVisibility(View.VISIBLE);
                            } else {
                                noFeedImage.setVisibility(View.GONE);
                                noFeedText.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(e -> {
                            if (null != getActivity()) {
                                Toast.makeText(getActivity(), "Couldn't get data!", Toast.LENGTH_SHORT).show();
                            }
                            noFeedImage.setVisibility(View.VISIBLE);
                            noFeedText.setVisibility(View.VISIBLE);
                        });
            }
        }


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_folk_guides, menu);

            MenuItem searchItem = menu.findItem(R.id.action_folk_guide_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setQueryHint("Search Folk Guides");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchFolkGuides(newText);
                    return false;
                }
            });

            super.onCreateOptionsMenu(menu, inflater);
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_folk_guide_search:
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }


        private void searchFolkGuides(String text) {
            ArrayList<FolkGuideItem> filterdUsers = new ArrayList<>();
            for (FolkGuideItem folkGuide : folkGuidesList) {
                if (folkGuide.getStrFirstName().toLowerCase().trim().contains(text.toLowerCase())) {
                    filterdUsers.add(folkGuide);
                }
            }
            folkGuidesAdapter.flterList(filterdUsers);
        }
    }


    ////////////////////////////////////////////////////// FRAGMENT 3
    public static class TeamLeadsFragment extends Fragment {

        private static final String TAG = "TeamLeadsFragment";
        private ArrayList<TeamLeadItem> teamLeadList;
        private RecyclerView recyclerView;
        private TeamLeadsAdapter teamLeadsAdapter;
        private TeamLeadItem teamLeadItem;
        private ProgressDialog loadingBar;
        private SwipeRefreshLayout swipeRefreshLayout;
        private TextView noInternetText;
        private TextView tvListCount;
        private ShimmerFrameLayout shimmerFrameLayout;
        private LottieAnimationView noFeedImage;
        private TextView noFeedText;

        public TeamLeadsFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);
            if (null != getActivity()) {
                init(view);
                setRefreshLayout();
                getData(getActivity());
                setUpRecyclerView();
            }
            return view;
        }

        private void init(View view) {
            recyclerView = view.findViewById(R.id.recycler_person);
            noInternetText = view.findViewById(R.id.tv_no_internet);

            noFeedText = view.findViewById(R.id.tv_no_feed_text);
            noFeedImage = view.findViewById(R.id.img_no_feed_lottie_image);

            noFeedImage.setAnimation(R.raw.empty_box);
            noFeedImage.playAnimation();

            tvListCount = view.findViewById(R.id.tv_list_count);
            shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
            if (null != getActivity()) {
                loadingBar = new ProgressDialog(getActivity());
            }
            swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        }

        private void setRefreshLayout() {
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            swipeRefreshLayout.setOnRefreshListener(() -> {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                if (null != getActivity()) {
                    getData(getActivity());
                }
            });
        }

        private void setUpRecyclerView() {
            if (null != getActivity()) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Objects.requireNonNull(getActivity()).getBaseContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                teamLeadList = new ArrayList<>();

                teamLeadsAdapter = new TeamLeadsAdapter(getContext(), teamLeadList);
                teamLeadsAdapter.setHasStableIds(true);
                teamLeadsAdapter.setOnItemClickListener(new TeamLeadsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                        // Start activity
                        Intent intent = new Intent(getContext(), ProfileActivity.class);
                        TeamLeadItem teamLeadItem = teamLeadList.get(position);
                        intent.putExtra("profileKey", "TEAMLEAD");
                        intent.putExtra("teamleadItem", teamLeadItem);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(teamLeadsAdapter);
            }
        }

        private void getData(final Context context) {
            if (hasInternet(context)) {
                Log.d(TAG, "hit 1");
                setUpRecyclerView();
                AsyncTask.execute(this::readFolkGuidesData);
                noInternetText.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                teamLeadsAdapter.notifyDataSetChanged();
            } else {
                noInternetText.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        }

        // READ
        private void readFolkGuidesData() {
            if (null != getActivity()) {
                SharedPreferences sp = getActivity().getSharedPreferences("authItem", Context.MODE_PRIVATE);
                String zone = sp.getString("zone", "");

                FirebaseFirestore.getInstance()
                        .collection(HelperConstants.COLL_AUTH_FOLK_TEAM_LEADS)
                        .whereEqualTo("zone", zone)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            shimmerFrameLayout.setVisibility(View.GONE);
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                                Log.d(TAG, "docList: " + docList);

                                for (DocumentSnapshot docSnap : docList) {
                                    teamLeadItem = docSnap.toObject(TeamLeadItem.class);
                                    if (teamLeadItem != null) {
                                        Log.d(TAG, "personItem: " + teamLeadItem);
                                        teamLeadItem.setId(docSnap.getId());
                                        teamLeadItem.setStrFirstName(docSnap.getString(HelperConstants.KEY_FG_NAME));
                                        teamLeadItem.setstrTeamLeadAbbr(docSnap.getString(HelperConstants.KEY_FG_ABBR));
                                        teamLeadItem.setStrZone(docSnap.getString(HelperConstants.KEY_ZONE));
                                        teamLeadItem.setStrPhone(docSnap.getString(HelperConstants.KEY_MOBILE_NUMBER));
                                        teamLeadItem.setStrWhatsApp(docSnap.getString(HelperConstants.KEY_MOBILE_NUMBER));
                                        teamLeadItem.setStrEmail(docSnap.getString(HelperConstants.KEY_EMAIL));
                                    }
                                    Log.d(TAG, "firedoc id: " + docSnap.getId());
                                    teamLeadList.add(teamLeadItem);
                                    tvListCount.setText(teamLeadList.size() + " Team Leads");
                                }
                                teamLeadsAdapter.notifyDataSetChanged();
                                if (null != getActivity()) {
                                    Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (teamLeadList.size() == 0) {
                                noFeedImage.setVisibility(View.VISIBLE);
                                noFeedText.setVisibility(View.VISIBLE);
                            } else {
                                noFeedImage.setVisibility(View.GONE);
                                noFeedText.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(e -> {
                            if (null != getActivity()) {
                                Toast.makeText(getActivity(), "Couldn't get data!", Toast.LENGTH_SHORT).show();
                            }
                            noFeedImage.setVisibility(View.VISIBLE);
                            noFeedText.setVisibility(View.VISIBLE);
                        });
            }
        }


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_folk_guides, menu);

            MenuItem searchItem = menu.findItem(R.id.action_folk_guide_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setQueryHint("Search Team Leads");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchFolkGuides(newText);
                    return false;
                }
            });

            super.onCreateOptionsMenu(menu, inflater);
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_team_lead_search:
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }


        private void searchFolkGuides(String text) {
            ArrayList<TeamLeadItem> filterdUsers = new ArrayList<>();
            for (TeamLeadItem teamLead : teamLeadList) {
                if (teamLead.getStrFirstName().toLowerCase().trim().contains(text.toLowerCase())) {
                    filterdUsers.add(teamLead);
                }
            }
            teamLeadsAdapter.flterList(filterdUsers);
        }

    }


    ////////////////////////////////////////////////////// FRAGMENT 4
    public static class ZonalHeadsFragment extends Fragment {

        private static final String TAG = "ZonalHeadsFragment";
        private ArrayList<ZonalHeadItem> zonalHeadsList;
        private RecyclerView recyclerView;
        private ZonalHeadsAdapter zonalHeadsAdapter;
        private ZonalHeadItem zonalHeadItem;
        private ProgressDialog loadingBar;
        private SwipeRefreshLayout swipeRefreshLayout;
        private TextView noInternetText;
        private TextView tvListCount;
        private ShimmerFrameLayout shimmerFrameLayout;
        private LottieAnimationView noFeedImage;
        private TextView noFeedText;

        public ZonalHeadsFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);
            init(view);
            setRefreshLayout();
            if (null != getActivity()) {
                getData(getActivity());
            }
            setUpRecyclerView();
            return view;
        }

        private void init(View view) {
            recyclerView = view.findViewById(R.id.recycler_person);
            shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
            noInternetText = view.findViewById(R.id.tv_no_internet);

            noFeedText = view.findViewById(R.id.tv_no_feed_text);
            noFeedImage = view.findViewById(R.id.img_no_feed_lottie_image);

            noFeedImage.setAnimation(R.raw.empty_box);
            noFeedImage.playAnimation();

            tvListCount = view.findViewById(R.id.tv_list_count);
            if (null != getActivity()) {
                loadingBar = new ProgressDialog(getActivity());
            }
            swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        }

        private void setRefreshLayout() {
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            swipeRefreshLayout.setOnRefreshListener(() -> {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                if (null != getActivity()) {
                    getData(getActivity());
                }
            });
        }

        private void setUpRecyclerView() {
            if (null != getActivity()) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Objects.requireNonNull(getActivity()).getBaseContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                zonalHeadsList = new ArrayList<>();

                zonalHeadsAdapter = new ZonalHeadsAdapter(getContext(), zonalHeadsList);
                zonalHeadsAdapter.setHasStableIds(true);
                zonalHeadsAdapter.setOnItemClickListener(new ZonalHeadsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                        ZonalHeadItem zonalHeadItem = zonalHeadsList.get(position);
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        intent.putExtra("profileKey", "ZONALHEAD");
                        intent.putExtra("zonalheadItem", zonalHeadItem);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(zonalHeadsAdapter);
            }
        }

        private void getData(final Context context) {
            if (hasInternet(context)) {
                Log.d(TAG, "hit 1");
                setUpRecyclerView();
                AsyncTask.execute(this::readZonalHeadsData);
                noInternetText.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                zonalHeadsAdapter.notifyDataSetChanged();
            } else {
                noInternetText.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        }

        // READ
        private void readZonalHeadsData() {
            SharedPreferences sp = getActivity().getSharedPreferences("authItem", Context.MODE_PRIVATE);
            String zone = sp.getString("zone", "");

            FirebaseFirestore.getInstance()
                    .collection(HelperConstants.COLL_AUTH_FOLK_ZONAL_HEADS)
                    .whereEqualTo("zone", zone)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                            Log.d(TAG, "docList: " + docList);

                            for (DocumentSnapshot docSnap : docList) {
                                zonalHeadItem = docSnap.toObject(ZonalHeadItem.class);
                                if (zonalHeadItem != null) {
                                    Log.d(TAG, "personItem: " + zonalHeadItem);
                                    // catch null for every single field here only ...oooooooooooomg
                                    zonalHeadItem.setId(docSnap.getId());
                                    zonalHeadItem.setStrFirstName(docSnap.getString("firstName"));
                                    zonalHeadItem.setStrLastName(docSnap.getString("lastName"));
                                    zonalHeadItem.setStrKcExperience(docSnap.getString("kcExperience"));
                                    zonalHeadItem.setStrMemberType(docSnap.getString("memberType"));
                                    zonalHeadItem.setStrProfileImage(docSnap.getString("profileImageUrl"));
                                    zonalHeadItem.setStrPhone(docSnap.getString("phone"));
                                    zonalHeadItem.setStrWhatsApp(docSnap.getString("phone"));
                                    zonalHeadItem.setStrEmail(docSnap.getString("email"));
                                }
                                Log.d(TAG, "firedoc id: " + docSnap.getId());
                                zonalHeadsList.add(zonalHeadItem);
                                tvListCount.setText(zonalHeadsList.size() + " Zonal Heads");
                            }
                            zonalHeadsAdapter.notifyDataSetChanged();
                            if (null != getActivity()) {
                                Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (zonalHeadsList.size() == 0) {
                            noFeedImage.setVisibility(View.VISIBLE);
                            noFeedText.setVisibility(View.VISIBLE);
                        } else {
                            noFeedImage.setVisibility(View.GONE);
                            noFeedText.setVisibility(View.GONE);
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (null != getActivity()) {
                            Toast.makeText(getActivity(), "Couldn't get data!", Toast.LENGTH_SHORT).show();
                        }
                        noFeedImage.setVisibility(View.VISIBLE);
                        noFeedText.setVisibility(View.VISIBLE);
                    });
        }


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_all_users, menu);

            MenuItem searchItem = menu.findItem(R.id.action_all_users_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setQueryHint("Search Zonal Heads");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchAllUsers(newText);
                    return false;
                }
            });
            super.onCreateOptionsMenu(menu, inflater);
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_zonal_head_search:
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }


        private void searchAllUsers(String text) {
            ArrayList<ZonalHeadItem> filterdUsers = new ArrayList<>();
            for (ZonalHeadItem zonalHead : zonalHeadsList) {
                if (zonalHead.getStrFirstName().toLowerCase().trim().contains(text.toLowerCase())) {
                    filterdUsers.add(zonalHead);
                }
            }
            zonalHeadsAdapter.filterList(filterdUsers);
        }
    }


    ////////////////////////////////////////////////////// FRAGMENT 5
    public static class AllUsersFragment extends Fragment {

        private static final String TAG = "AllUsersFragment";
        private ArrayList<AllUsersItem> allUsersList;
        private RecyclerView recyclerView;
        private AllUsersAdapter allUsersAdapter;
        private AllUsersItem allUsersItem;
        private ProgressDialog loadingBar;
        private SwipeRefreshLayout swipeRefreshLayout;
        private TextView noInternetText;
        private TextView tvListCount;
        private ShimmerFrameLayout shimmerFrameLayout;
        private LottieAnimationView noFeedImage;
        private TextView noFeedText;

        public AllUsersFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);
            init(view);
            setRefreshLayout();
            if (null != getActivity()) {
                getData(getActivity());
            }
            setUpRecyclerView();
            return view;
        }

        private void init(View view) {
            recyclerView = view.findViewById(R.id.recycler_person);
//            progressBar = view.findViewById(R.id.progress_circular);
            shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
            noInternetText = view.findViewById(R.id.tv_no_internet);

            noFeedText = view.findViewById(R.id.tv_no_feed_text);
            noFeedImage = view.findViewById(R.id.img_no_feed_lottie_image);

            noFeedImage.setAnimation(R.raw.empty_box);
            noFeedImage.playAnimation();

            tvListCount = view.findViewById(R.id.tv_list_count);
            if (null != getActivity()) {
                loadingBar = new ProgressDialog(getActivity());
            }
            swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        }

        private void setRefreshLayout() {
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            swipeRefreshLayout.setOnRefreshListener(() -> {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                if (null != getActivity()) {
                    getData(getActivity());
                }
            });
        }

        private void setUpRecyclerView() {
            if (null != getActivity()) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Objects.requireNonNull(getActivity()).getBaseContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                allUsersList = new ArrayList<>();

                allUsersAdapter = new AllUsersAdapter(getContext(), allUsersList);
                allUsersAdapter.setHasStableIds(true);
                allUsersAdapter.setOnItemClickListener(new AllUsersAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                        AllUsersItem allUsersItem = allUsersList.get(position);
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        intent.putExtra("profileKey", "ALLUSER");
                        intent.putExtra("alluserItem", allUsersItem);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(allUsersAdapter);
            }
        }

        private void getData(final Context context) {
            if (hasInternet(context)) {
                Log.d(TAG, "hit 1");
                setUpRecyclerView();
                AsyncTask.execute(this::readAllUsersData);
                noInternetText.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                allUsersAdapter.notifyDataSetChanged();
            } else {
                noInternetText.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        }

        // READ
        private void readAllUsersData() {
            SharedPreferences sp = getActivity().getSharedPreferences("authItem", Context.MODE_PRIVATE);
            String zone = sp.getString("zone", "");

            FirebaseFirestore.getInstance()
                    .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                    .whereEqualTo("zone", zone)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                            Log.d(TAG, "docList: " + docList);

                            for (DocumentSnapshot docSnap : docList) {
                                allUsersItem = docSnap.toObject(AllUsersItem.class);
                                if (allUsersItem != null) {
                                    Log.d(TAG, "personItem: " + allUsersItem);
                                    // catch null for every single field here only ...oooooooooooomg
                                    allUsersItem.setId(docSnap.getId());
                                    allUsersItem.setStrFirstName(docSnap.getString("firstName"));
                                    allUsersItem.setStrLastName(docSnap.getString("lastName"));
                                    allUsersItem.setStrKcExperience(docSnap.getString("kcExperience"));
                                    allUsersItem.setStrMemberType(docSnap.getString("memberType"));
                                    allUsersItem.setStrProfileImage(docSnap.getString("profileImageUrl"));
                                    allUsersItem.setStrPhone(docSnap.getString("phone"));
                                    allUsersItem.setStrWhatsApp(docSnap.getString("phone"));
                                    allUsersItem.setStrEmail(docSnap.getString("email"));
                                }
                                Log.d(TAG, "firedoc id: " + docSnap.getId());
                                allUsersList.add(allUsersItem);
                                tvListCount.setText(allUsersList.size() + " Registered Users");
                            }
                            allUsersAdapter.notifyDataSetChanged();
                            if (null != getActivity()) {
                                Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (allUsersList.size() == 0) {
                            noFeedImage.setVisibility(View.VISIBLE);
                            noFeedText.setVisibility(View.VISIBLE);
                        } else {
                            noFeedImage.setVisibility(View.GONE);
                            noFeedText.setVisibility(View.GONE);
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (null != getActivity()) {
                            Toast.makeText(getActivity(), "Couldn't get data!", Toast.LENGTH_SHORT).show();
                        }
                        noFeedImage.setVisibility(View.VISIBLE);
                        noFeedText.setVisibility(View.VISIBLE);
                    });
        }


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_all_users, menu);

            MenuItem searchItem = menu.findItem(R.id.action_all_users_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setQueryHint("Search All Users");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchAllUsers(newText);
                    return false;
                }
            });
            super.onCreateOptionsMenu(menu, inflater);
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_all_users_search:
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }


        private void searchAllUsers(String text) {
            ArrayList<AllUsersItem> filterdUsers = new ArrayList<>();
            for (AllUsersItem user : allUsersList) {
                if (user.getStrFirstName().toLowerCase().trim().contains(text.toLowerCase())) {
                    filterdUsers.add(user);
                }
            }
            allUsersAdapter.filterList(filterdUsers);
        }
    }
}
