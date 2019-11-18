package com.singularitycoder.folkdatabase.auth;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.home.HomeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CoordinatorLayout mCoordinatorLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ImageView headerImage;

    // Create a firebase auth object
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatuBarColor();
        setContentView(R.layout.activity_main);
        inits();
        setUpViewPager();
        setUpTabLayout();
        setUpToolbar();
        setUpAppbarLayout();
        setUpCollapsingToolbar();

        // Initialize Firebase Auth.
        mAuth = FirebaseAuth.getInstance();
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
        mCoordinatorLayout = findViewById(R.id.coordinator_main);
        headerImage = findViewById(R.id.img_main_header);
        headerImage.setImageDrawable(getResources().getDrawable(R.drawable.blink));
        AnimationDrawable frameAnimation = (AnimationDrawable) headerImage.getDrawable();
        frameAnimation.start();
    }


    private void setUpViewPager() {
        // Set ViewPager
        viewPager = findViewById(R.id.viewpager_main);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SignUpFragment(ContextCompat.getColor(this, R.color.colorWhite)), "SIGN UP");
        adapter.addFrag(new LoginFragment(ContextCompat.getColor(this, R.color.colorWhite)), "LOGIN");
        viewPager.setAdapter(adapter);
    }


    private void setUpTabLayout() {
        // Set TabLayout
        tabLayout = findViewById(R.id.tablayout_main);
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
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Folk Caller");
    }


    private void setUpAppbarLayout() {
        AppBarLayout appBarLayout = findViewById(R.id.appbar_main);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle("FOLK Caller");
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
                    //  Collapsed
//                    Toast.makeText(getApplicationContext(), "Collapsed", Toast.LENGTH_LONG).show();
//                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
//                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorBlack));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    //Expanded
//                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorWhite));
//                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorWhite));
                    toolbar.setBackgroundColor(Color.TRANSPARENT);
                    tabLayout.setBackgroundColor(Color.TRANSPARENT);
//                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
                }
            }
        });
    }


    private void setUpCollapsingToolbar() {
        // Set CollapsingToolbar
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_main);

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


    public static class LoginFragment extends Fragment {
        int color;
        EditText etPhoneNumber;
        EditText etPassword;
        Button btnLogin;

        // Declare an instance of Firestore
        FirebaseFirestore db;

        Button btnUseFingerPrint;
        TextView tvUsePasswordToLogin;
        TextView tvNotAMember;

        TextView tvLoginMemberType;
        TextView tvFolkIdLogin;
        EditText etFolkIdLogin;
        TextView tvAdminNumber;
        EditText etAdminNumber;

        private String parentDbName = "User";

        private ProgressDialog loadingBar;

        public LoginFragment() {
        }

        @SuppressLint("ValidFragment")
        public LoginFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_login, container, false);

            loadingBar = new ProgressDialog(getActivity());

            db = FirebaseFirestore.getInstance();

//            Button btnLogin = view.findViewById(R.id.btn_create_event_invite_people);
//            btnLogin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    startActivity(new Intent(getActivity(), HomeActivity.class));
//                }
//            });

            tvFolkIdLogin = view.findViewById(R.id.tv_login_folkid);
            etFolkIdLogin = view.findViewById(R.id.et_login_folkid);

            tvNotAMember = view.findViewById(R.id.tv_login_create_account);
            tvNotAMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    new MainActivity().tabLayout.getTabAt(0);
                }
            });

            etPhoneNumber = view.findViewById(R.id.et_login_phone);
            etPassword = view.findViewById(R.id.et_login_password);
            btnLogin = view.findViewById(R.id.btn_create_event_invite_people);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateLoginUser();
                }
            });

            return view;
        }

        public void validateLoginUser() {
            String phone = etPhoneNumber.getText().toString();
            String password = etPassword.getText().toString();

            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(getActivity(), "Please write your phone number...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "Please write your password...", Toast.LENGTH_SHORT).show();
            } else {
                loadingBar.setTitle("Login Account");
                loadingBar.setMessage("Please wait, while we are checking the credentials!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                // Login user using Realtime DB
                loginUser(phone, password);

                // Login user using Firestore DB
//                loginUserFirestore(phone, password);
            }
        }

        // Login user using Firestore DB
        private void loginUserFirestore(String phone, String password) {
            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }

        // Login user using RealTime DB
        private void loginUser(final String phone, final String password) {

            // If checkbox is checked. Before we allow access we need to store the variables phone n pass key. When u check the checkbox it will remember phone n pass key just like shared prefs in one sense. Whenever user logs in it will store in user phone memory. Just like shared prefs.
//            if (checkBoxRemeberMe.isChecked()) {
//                Paper.book().write(Prevalent.userPhoneKey, phone);
//                Paper.book().write(Prevalent.userPasswordKey, password);
//            }

            // Get reference to DB
            final DatabaseReference rootRef;
            rootRef = FirebaseDatabase.getInstance().getReference();

            // Check if user is available or not
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // User node contains children like phone etc which is the obj that contains all info abt a user. If that exists then we will allow user to login
                    if (dataSnapshot.child(parentDbName).child(phone).exists()) {
                        // Retrieve the pass and phone of the user and add it to the User model. We added the DB data to the User model. get the data using getters
                        User userData = dataSnapshot.child(parentDbName).child(phone).getValue(User.class);

                        // Check if phone n pass entered in the edittext match the user n pass u got from the DB using the data added to User model. If data matches then allow user access to his/her account
                        if (userData.getPhone().equals(phone)) {
                            if (userData.getPassword().equals(password)) {

                                // If login successful then show toast n jump them to the Home activity
                                loadingBar.dismiss();
                                Toast.makeText(getActivity(), "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), HomeActivity.class));
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(getActivity(), "Wrong Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "Account with this " + phone + " number does not exist! Create a new Account!", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void dialogSignUpMemberType() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("I am a");
            String[] selectArray = {"Folk Member", "Admin"};
            builder.setItems(selectArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            tvLoginMemberType.setText("Folk Member");

                            tvFolkIdLogin.setVisibility(View.VISIBLE);
                            etFolkIdLogin.setVisibility(View.VISIBLE);

                            tvAdminNumber.setVisibility(View.GONE);
                            etAdminNumber.setVisibility(View.GONE);
                            break;
                        case 1:
                            tvLoginMemberType.setText("Admin");

                            tvAdminNumber.setVisibility(View.VISIBLE);
                            etAdminNumber.setVisibility(View.VISIBLE);

                            tvFolkIdLogin.setVisibility(View.GONE);
                            etFolkIdLogin.setVisibility(View.GONE);
                            break;
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public static class SignUpFragment extends Fragment {
        int color;
        TextView tvTermsPrivacy;
        TextView tvMemberType;
        TextView tvFolkIdLogin;
        EditText etFolkIdLogin;
        TextView tvAdminNumber;
        EditText etAdminNumber;
        TextView etSignUpZone;
        EditText etFirstName;
        EditText etLastName;
        EditText etEmail;
        EditText etPhone;
        EditText etPassword;
        Button createAccount;

        private ProgressDialog loadingBar;

        private FirebaseAuth fireAuth;

        // Declare an instance of Firestore
        FirebaseFirestore db;

        public SignUpFragment() {
        }

        @SuppressLint("ValidFragment")
        public SignUpFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_signup, container, false);

            fireAuth = FirebaseAuth.getInstance();

            db = FirebaseFirestore.getInstance();

            loadingBar = new ProgressDialog(getActivity());

            tvTermsPrivacy = view.findViewById(R.id.tv_signup_terms);
            tvTermsPrivacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iskconbangalore.org/privacy-policy/")));
                }
            });

            etSignUpZone = view.findViewById(R.id.et_signup_zone_type);
            etSignUpZone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogSignUpZone();
                }
            });

            tvMemberType = view.findViewById(R.id.et_signup_member_type);
            tvMemberType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogSignUpMemberType();
                }
            });

            tvFolkIdLogin = view.findViewById(R.id.tv_login_folkid);
            etFolkIdLogin = view.findViewById(R.id.et_login_folkid);
            tvAdminNumber = view.findViewById(R.id.tv_signup_admin_number);
            etAdminNumber = view.findViewById(R.id.et_signup_admin_number);

            etFirstName = view.findViewById(R.id.et_signup_first_name);
            etLastName = view.findViewById(R.id.et_signup_last_name);
            etEmail = view.findViewById(R.id.et_signup_email);
            etPhone = view.findViewById(R.id.et_signup_phone);
            etPassword = view.findViewById(R.id.et_login_password);

            createAccount = view.findViewById(R.id.btn_create_account);
            createAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateSignUpUser();
                }
            });

            return view;
        }

        public void validateSignUpUser() {
            // Validate input
            String zone = etSignUpZone.getText().toString();
            String memberType = tvMemberType.getText().toString();
            String folkId = etFolkIdLogin.getText().toString();
            String adminNumber = etAdminNumber.getText().toString();
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String password = etPassword.getText().toString();

            if (TextUtils.isEmpty(memberType)) {
                Toast.makeText(getActivity(), "Please write your member type...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(firstName)) {
                Toast.makeText(getActivity(), "Please write your first name...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(lastName)) {
                Toast.makeText(getActivity(), "Please write your last name...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(phone)) {
                Toast.makeText(getActivity(), "Please write your phone number...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(zone)) {
                Toast.makeText(getActivity(), "Please write your zone...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(folkId)) {
                Toast.makeText(getActivity(), "Please write your folk Id...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(adminNumber)) {
                Toast.makeText(getActivity(), "Please write your Admin Number...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(getActivity(), "Please write your email...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "Please write your password...", Toast.LENGTH_SHORT).show();
            } else {
                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("Please wait, while we are checking the credentials!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                // Create user using RealTime DB in DB after validaing input
//                createUser(zone, memberType, folkId, adminNumber, firstName, lastName, phone, email, password);

                // Create user using Firestore DB after validating input
                createUserFirestore(zone, memberType, folkId, adminNumber, firstName, lastName, email, phone, password);

            }
        }

        private void createUserFirestore(String zone, String memberType, String folkId, String adminNumber, String firstName, String lastName, String email, String phone, String password) {
            // Create Collection reference
            CollectionReference members = db.collection("members");

            // User obj
            User user = new User(
                    zone,
                    memberType,
                    folkId,
                    adminNumber,
                    firstName,
                    lastName,
                    phone,
                    email,
                    password
            );

            // Save User obj to Firestore - Add a new document with a generated ID
            // Collection name is "User". U can create a new collection this way
            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(getActivity(), "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                            loadingBar.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            Toast.makeText(getActivity(), "Failed to create User", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    });
        }


        // RealTime DB stuff
        private void createUser(String zone, final String memberType, String folkId, String adminNumber, final String firstName, final String lastName, final String phone, final String email, final String password) {
            // Get reference to the root
            final DatabaseReference rootRef;
            rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // If user doesnt exist then create new
                    if (!(dataSnapshot.child("User").child(phone).exists())) {
                        // Using Hash Map u put the data into the DB
                        HashMap<String, Object> userDataMap = new HashMap<>();
                        userDataMap.put("zone", zone);
                        userDataMap.put("memberType", memberType);
                        userDataMap.put("folkId", folkId);
                        userDataMap.put("adminNumber", adminNumber);
                        userDataMap.put("firstName", firstName);
                        userDataMap.put("lastName", lastName);
                        userDataMap.put("phone", phone);
                        userDataMap.put("email", email);
                        userDataMap.put("password", password);

                        // Update children
                        rootRef.child("User").child(phone).updateChildren(userDataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // If task or uploading the data is successful do this
                                        if (task.isSuccessful()) {
                                            loadingBar.dismiss();
                                            Toast.makeText(getActivity(), "Congrates, ur acc has been created!", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(getActivity(), LoginActivity.class));
                                        } else {
                                            loadingBar.dismiss();
                                            Toast.makeText(getActivity(), "Network error. Please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        // If User exists jump them to main activity to create new account
                        loadingBar.dismiss();
                        Toast.makeText(getActivity(), "This " + phone + " already exists! Please try again with another phone number!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void dialogSignUpMemberType() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("I am a");
            String[] selectArray = {"Folk Member", "Admin"};
            builder.setItems(selectArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            tvMemberType.setText("Folk Member");

                            tvFolkIdLogin.setVisibility(View.VISIBLE);
                            etFolkIdLogin.setVisibility(View.VISIBLE);

                            tvAdminNumber.setVisibility(View.GONE);
                            etAdminNumber.setVisibility(View.GONE);
                            etAdminNumber.setText("Empty");
                            break;
                        case 1:
                            tvMemberType.setText("Admin");

                            tvAdminNumber.setVisibility(View.VISIBLE);
                            etAdminNumber.setVisibility(View.VISIBLE);

                            tvFolkIdLogin.setVisibility(View.GONE);
                            etFolkIdLogin.setVisibility(View.GONE);
                            etFolkIdLogin.setText("Empty");
                            break;
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        public void dialogSignUpZone() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Which Zone?");
            String[] selectArray = {"Bengaluru South", "Bengaluru North", "Ahemadabad South", "Ahemadabad North"};
            builder.setItems(selectArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            etSignUpZone.setText("Bengaluru South");
                            break;
                        case 1:
                            etSignUpZone.setText("Bengaluru North");
                            break;
                        case 2:
                            etSignUpZone.setText("Ahemadabad South");
                            break;
                        case 3:
                            etSignUpZone.setText("Ahemadabad North");
                            break;
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}