package com.singularitycoder.folkdatabase.home;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.ProgressBar;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.singularitycoder.folkdatabase.auth.MainActivity;
import com.singularitycoder.folkdatabase.helper.CustomEditText;
import com.singularitycoder.folkdatabase.helper.Helper;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.singularitycoder.folkdatabase.helper.Helper.getActivity;
import static com.singularitycoder.folkdatabase.helper.Helper.hasInternet;
import static java.lang.String.valueOf;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    Context mContext;
    FloatingActionButton fab1;


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
                        FloatingActionButton fabMain = findViewById(R.id.floating_button);
                        fabMain.show();
                        fabMain.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_list_black_24dp));
                        fabMain.setOnClickListener(view -> {
                            new ContactFragment().contactFilterDialog(HomeActivity.this);
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
        adapter.addFrag(new AllUsersFragment(), "ALL USERS");
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


    ////////////////////////////////////////////////////// FRAGMENT 1
    public static class ContactFragment extends Fragment {

        private static final String TAG = "ContactFragment";
        private ArrayList<ContactItem> contactList;
        private RecyclerView recyclerView;
        private ContactsAdapter contactsAdapter;
        private ProgressBar progressBar;
        private ProgressDialog loadingBar;
        private ContactItem personItemModel;
        private SwipeRefreshLayout swipeRefreshLayout;
        private TextView noInternetText;
        private TextView tvListCount;
        private String strOldPassword;

        private FirebaseAuth firebaseAuth;
        private FirebaseUser firebaseUser;


        // this listener is called when there is change in firebase fireUser session
        FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // fireUser fireAuth state is changed - fireUser is null launch login activity
                startActivity(new Intent(getActivity(), MainActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            } else {
                Toast.makeText(getActivity(), "AuthUserItem: " + user.getEmail(), Toast.LENGTH_SHORT).show();
            }
        };

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

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            authListener = firebaseAuth -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // fireUser fireAuth state is changed - fireUser is null launch login activity
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }
            };

            recyclerView = view.findViewById(R.id.recycler_person);
            progressBar = view.findViewById(R.id.progress_circular);
            noInternetText = view.findViewById(R.id.tv_no_internet);
            tvListCount = view.findViewById(R.id.tv_list_count);

            loadingBar = new ProgressDialog(getActivity());

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

        private void getData(final Context context) {
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
                                Log.d(TAG, "docList: " + docList);

//                                Map<String, Object> forms = queryDocumentSnapshots.get("dynForms");
//                                for (Map.Entry<String, Object> form: forms.entrySet()) {
//                                    String key = (String) form.getKey();
//                                    Map<String, Object> values = (Map<String, Object>)form.getValues();
//                                    String name = (String) values.get("formName");
//                                }

                                for (DocumentSnapshot docSnap : docList) {
                                    personItemModel = docSnap.toObject(ContactItem.class);
                                    if (personItemModel != null) {
                                        Log.d(TAG, "personItem: " + personItemModel);
                                        personItemModel.setId(docSnap.getId());
                                        personItemModel.setFirstName(docSnap.getString("name"));
                                        if (("").equals(docSnap.getString("folk_guide")) || (null) == (docSnap.getString("folk_guide"))) {
                                            personItemModel.setStrFolkGuide("No FOLK Guide");
                                        } else {
                                            personItemModel.setStrFolkGuide(docSnap.getString("folk_guide"));
                                        }
                                        personItemModel.setStrOccupation(docSnap.getString("occupation"));
//                                        personItemModel.setImgProfileImage(docSnap.getData().get("photo_url"));
                                        Log.d(TAG, "profile image: " + docSnap.getData());
                                        Log.d(TAG, "profile image 2: " + docSnap.getData().get("docs"));
                                        Object profileImages = docSnap.getData().get("docs");
                                        Map<String, String> mapImage = (Map<String, String>) docSnap.getData().get("docs");
                                        Log.d(TAG, "prof image map: " + mapImage);
                                        if (mapImage != null) {

                                        }

//                                        for (String img : mapImage.values()) {
//                                            Log.d(TAG, "value is : " + img);
//                                        }


//                                        for (Map.Entry<String, Object> entry : Objects.requireNonNull(mapImage).entrySet()) {
//
//                                                Log.d(TAG, "key: " + entry.getKey() + " value: " + entry.getValue());
//                                        }


//                                        Iterator<Map.Entry<String, String>> imgs = profImages.entrySet().iterator();
//
//                                        while (imgs.hasNext()) {
//                                            Map.Entry<String, String> entry = imgs.next();
//                                            Log.d(TAG, "key: " + entry.getKey() + " value: " + entry.getValue());
//                                        }


//                                        if (profImages.keySet() != null) {
//                                            for (String imageKey : profImages.keySet()) {
//                                                Log.d(TAG, "key is: " + imageKey);
//                                            }
//                                        }

                                    }
                                    Log.d(TAG, "firedoc id: " + docSnap.getId());
                                    contactList.add(personItemModel);
                                    tvListCount.setText(contactList.size() + " Contacts");
                                }
                                contactsAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Couldn't get data!", Toast.LENGTH_SHORT).show());
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
                case R.id.action_notifications:
                    dialogNotifications(getActivity());
                    return true;
                case R.id.action__contact_filter:
                    contactFilterDialog(getActivity());
                    return true;
                case R.id.action_my_profile:
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra("profileKey", "SELF");
                    startActivity(intent);
                    return true;
                case R.id.action_about:
                    aboutDialog(getActivity());
                    return true;
                case R.id.action_change_password:
                    dialogChangePassword(getActivity());
                    return true;
                case R.id.action_delete_account:
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                    builder.setTitle("Are you sure?");
                    builder.setMessage("You cannot undo this!");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAccount();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return true;
                case R.id.action_log_out:
                    AlertDialog.Builder builderLogOut = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                    builderLogOut.setMessage("Do you want to Log Out?");

                    builderLogOut.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logOut();
                            dialog.dismiss();
                        }
                    });

                    builderLogOut.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialogLogOut = builderLogOut.create();
                    alertDialogLogOut.show();
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
            Window window = activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ImageView imgClose = dialog.findViewById(R.id.iv_dialog_close);
            TextView tvResetContacts = dialog.findViewById(R.id.tv_dialog_reset);
            TextView tvFolkGuides = dialog.findViewById(R.id.tv_dialog_folk_guides);
            Button btnApply = dialog.findViewById(R.id.btn_dialog_apply);

            imgClose.setOnClickListener(view -> dialog.dismiss());

            tvResetContacts.setOnClickListener(view -> {
                tvFolkGuides.setText("");
                contactsAdapter.flterList(contactList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(contactList.size() + " Contacts");
                dialog.dismiss();
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
                    Log.d(TAG, "afterTextChanged: editable val: " + editable);
                    findFolkGuideContacts(valueOf(editable));
                }
            });


            btnApply.setOnClickListener(view -> dialog.dismiss());

            dialog.show();
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

        private void findFolkGuideContacts(String text) {
            ArrayList<ContactItem> filteredContactsList = new ArrayList<>();

            for (ContactItem contact : contactList) {
                if (text.toLowerCase().trim().contains(contact.getStrFolkGuide().toString().toLowerCase().trim())) {
                    filteredContactsList.add(contact);
                    Log.d(TAG, "list: " + filteredContactsList);
                }
                contactsAdapter.flterList(filteredContactsList);
                contactsAdapter.notifyDataSetChanged();
                tvListCount.setText(filteredContactsList.size() + " Contacts");
            }
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

            ArrayList<ContactItem> notificationList = new ArrayList<>();
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


        private void dialogChangePassword(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_change_password);

            Rect displayRectangle = new Rect();
            Window window = activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            Objects.requireNonNull(dialog.getWindow()).setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                    changePassword(etNewPassword.getText().toString().trim());
                }
            });

            dialog.show();
        }

        private boolean hasValidInput(CustomEditText etOldPassword, CustomEditText etNewPassword, CustomEditText etNewPasswordAgain) {
            if (etOldPassword.getText().toString().trim().equals("")) {
                etOldPassword.setError("Password is Required!");
                etOldPassword.requestFocus();
                return false;
            }

            if (!Helper.hasValidPassword(etOldPassword.getText().toString().trim())) {
                etOldPassword.setError("Password must have at least 8 characters with One Uppercase and One lower case. These Special Characters are allwoed .,#@-_+!?;':*");
                etOldPassword.requestFocus();
                return false;
            }

//            if (!etOldPassword.getText().toString().trim().equals("fewea")) {
//                etOldPassword.setError("Wrong old password!");
//                etOldPassword.requestFocus();
//                return false;
//            }

            if (etNewPassword.getText().toString().trim().equals("")) {
                etNewPassword.setError("Password is Required!");
                etNewPassword.requestFocus();
                return false;
            }

            if (!Helper.hasValidPassword(etNewPassword.getText().toString().trim())) {
                etNewPassword.setError("Password must have at least 8 characters with One Uppercase and One lower case. These Special Characters are allwoed .,#@-_+!?;':*");
                etNewPassword.requestFocus();
                return false;
            }

            if (etNewPasswordAgain.getText().toString().trim().equals("")) {
                etNewPasswordAgain.setError("Password is Required!");
                etNewPasswordAgain.requestFocus();
                return false;
            }

            if (!Helper.hasValidPassword(etNewPasswordAgain.getText().toString().trim())) {
                etNewPasswordAgain.setError("Password must have at least 8 characters with One Uppercase and One lower case. These Special Characters are allwoed .,#@-_+!?;':*");
                etNewPasswordAgain.requestFocus();
                return false;
            }

            if (!etNewPassword.getText().toString().trim().equals(etNewPasswordAgain.getText().toString().trim())) {
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
                    .collection("FolkPeople")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                strOldPassword = queryDocumentSnapshots.getDocuments().get(0).getString("password");
                                // store user doc id, name, pasword all basic details in shared prefs
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: got hit");
                        }
                    });
            return strOldPassword;
        }

        private void changePassword(String newPassword) {
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            if (firebaseUser != null) {
                firebaseUser.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Password is updated! LogIn with new password!", Toast.LENGTH_SHORT).show();
                                    logOut();
                                    loadingBar.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Failed to update Password!", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });
            }
        }


        private void deleteAccount() {
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            if (firebaseUser != null) {
                firebaseUser.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Your profile is deleted :( Create an account now!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    Objects.requireNonNull(getActivity()).finish();
                                    loadingBar.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });
            }
        }


        private void logOut() {
            firebaseAuth.signOut();
            // this listener will be called when there is change in firebase fireUser session
            FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // fireUser fireAuth state is changed - fireUser is null launch login activity
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }
            };
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
        private ProgressBar progressBar;
        private ProgressDialog loadingBar;
        private SwipeRefreshLayout swipeRefreshLayout;
        private TextView noInternetText;
        private TextView tvListCount;

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

            recyclerView = view.findViewById(R.id.recycler_person);
            progressBar = view.findViewById(R.id.progress_circular);
            noInternetText = view.findViewById(R.id.tv_no_internet);
            tvListCount = view.findViewById(R.id.tv_list_count);

            loadingBar = new ProgressDialog(getActivity());

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
                    Intent adminIntent = new Intent(getContext(), ProfileActivity.class);
                    adminIntent.putExtra("openAdmin", "ADMIN");
                    startActivity(adminIntent);
                }
            });
            recyclerView.setAdapter(folkGuidesAdapter);
        }

        private void getData(final Context context) {
            if (hasInternet(context)) {
                Log.d(TAG, "hit 1");
                setUpRecyclerView();
                readContactsData();
                noInternetText.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                folkGuidesAdapter.notifyDataSetChanged();
            } else {
                noInternetText.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        }

        // READ
        private void readContactsData() {
            FirebaseFirestore.getInstance().collection("FOLKGuides").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            progressBar.setVisibility(View.GONE);
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                                Log.d(TAG, "docList: " + docList);

                                for (DocumentSnapshot docSnap : docList) {
                                    folkGuideItem = docSnap.toObject(FolkGuideItem.class);
                                    if (folkGuideItem != null) {
                                        Log.d(TAG, "personItem: " + folkGuideItem);
                                        folkGuideItem.setId(docSnap.getId());
                                        folkGuideItem.setStrFirstName(docSnap.getString("Name"));
                                        folkGuideItem.setStrKcExperience(docSnap.getString("Experience in KC(years)"));
                                        folkGuideItem.setStrDepartment(docSnap.getString("Department"));
                                    }
                                    Log.d(TAG, "firedoc id: " + docSnap.getId());
                                    folkGuidesList.add(folkGuideItem);
                                    tvListCount.setText(folkGuidesList.size() + " FOLK Guides");
                                }
                                folkGuidesAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Couldn't get data!", Toast.LENGTH_SHORT).show());
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

        public TeamLeadsFragment() {
        }
    }


    ////////////////////////////////////////////////////// FRAGMENT 4
    public static class ZonalHeadsFragment extends Fragment {

        public ZonalHeadsFragment() {
        }
    }


    ////////////////////////////////////////////////////// FRAGMENT 5
    public static class AllUsersFragment extends Fragment {

        private static final String TAG = "AllUsersFragment";
        private ArrayList<AllUsersItem> allUsersList;
        private RecyclerView recyclerView;
        private AllUsersAdapter allUsersAdapter;
        private AllUsersItem allUsersItem;
        private ProgressBar progressBar;
        private ProgressDialog loadingBar;
        private SwipeRefreshLayout swipeRefreshLayout;
        private TextView noInternetText;
        private TextView tvListCount;

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

            recyclerView = view.findViewById(R.id.recycler_person);
            progressBar = view.findViewById(R.id.progress_circular);
            noInternetText = view.findViewById(R.id.tv_no_internet);
            tvListCount = view.findViewById(R.id.tv_list_count);

            loadingBar = new ProgressDialog(getActivity());

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
                    // Start activity
                    Intent adminIntent = new Intent(getContext(), ProfileActivity.class);
                    adminIntent.putExtra("openAdmin", "ADMIN");
                    startActivity(adminIntent);
                }
            });
            recyclerView.setAdapter(allUsersAdapter);
        }

        private void getData(final Context context) {
            if (hasInternet(context)) {
                Log.d(TAG, "hit 1");
                setUpRecyclerView();
                readAllUsersData();
                noInternetText.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                allUsersAdapter.notifyDataSetChanged();
            } else {
                noInternetText.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        }

        // READ
        private void readAllUsersData() {
            FirebaseFirestore.getInstance().collection("FolkPeople").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        progressBar.setVisibility(View.GONE);
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                            Log.d(TAG, "docList: " + docList);

                            for (DocumentSnapshot docSnap : docList) {
                                allUsersItem = docSnap.toObject(AllUsersItem.class);
                                if (allUsersItem != null) {
                                    Log.d(TAG, "personItem: " + allUsersItem);
                                    allUsersItem.setId(docSnap.getId());
                                    allUsersItem.setStrFirstName(docSnap.getString("firstName"));
                                    allUsersItem.setStrKcExperience(docSnap.getString("kcExperience"));
                                    allUsersItem.setStrMemberType(docSnap.getString("memberType"));
                                }
                                Log.d(TAG, "firedoc id: " + docSnap.getId());
                                allUsersList.add(allUsersItem);
                                tvListCount.setText(allUsersList.size() + " Registered Users");
                            }
                            allUsersAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Couldn't get data!", Toast.LENGTH_SHORT).show());
        }


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_all_users, menu);

            MenuItem searchItem = menu.findItem(R.id.action_all_users_search);
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
