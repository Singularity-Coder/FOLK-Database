package com.singularitycoder.folkdatabase.auth;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.singularitycoder.folkdatabase.helper.CustomEditText;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.helper.Helper;
import com.singularitycoder.folkdatabase.home.HomeActivity;
import com.singularitycoder.folkdatabase.home.PersonModel;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

import static com.singularitycoder.folkdatabase.helper.ImageHelper.showSettingsDialog;

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
        adapter.addFrag(new LoginFragment(), "LOGIN");
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
                        getSupportActionBar().setTitle("FOLK Database");
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
        CustomEditText etPhoneNumber;
        CustomEditText etPassword;
        Button btnLogin;

        // Declare an instance of Firestore
        FirebaseFirestore db;

        TextView tvNotAMember;

        private String parentDbName = "User";

        private ProgressDialog loadingBar;

        public LoginFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_login, container, false);

            loadingBar = new ProgressDialog(getActivity());

            db = FirebaseFirestore.getInstance();

            etPhoneNumber = view.findViewById(R.id.et_login_phone);
            etPassword = view.findViewById(R.id.et_login_password);
            btnLogin = view.findViewById(R.id.btn_create_event_invite_people);
            btnLogin.setOnClickListener(view1 -> {
                if (hasValidInput(etPhoneNumber, etPassword)) {
                    startActivity(new Intent(getActivity(), HomeActivity.class));

                    loadingBar.setTitle("Login Account");
                    loadingBar.setMessage("Please wait, while we are checking the credentials!");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    String phoneNumber = etPhoneNumber.getText().toString().trim();
                    String password = etPassword.getText().toString();

                    // Login user using Firestore DB
//                loginUserFirestore(phoneNumber, password);
                }
            });

            tvNotAMember = view.findViewById(R.id.tv_login_create_account);
            tvNotAMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    new MainActivity().tabLayout.getTabAt(0);
                }
            });

            return view;
        }

        private boolean hasValidInput(CustomEditText etPhoneNumber, CustomEditText etPassword) {
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (phoneNumber.equals("")) {
                etPhoneNumber.setError("Phone Number is Required!");
                etPhoneNumber.requestFocus();
                return false;
            }

            if (phoneNumber.length() < 10) {
                etPhoneNumber.setError("Provide a valid Mobile Number!");
                etPhoneNumber.requestFocus();
                return false;
            }

            if (password.equals("")) {
                etPassword.setError("Password is Required!");
                etPassword.requestFocus();
                return false;
            }

            if (!Helper.hasValidPassword(password)) {
                etPassword.setError("Password must have at least 8 characters with One Uppercase and One lower case. These Special Characters are allwoed .,#@-_+!?;':*");
                etPassword.requestFocus();
                return false;
            }
            return true;
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
    }

    public static class SignUpFragment extends Fragment {
        int color;
        TextView tvTermsPrivacy;
        TextView tvMemberType;
        TextView tvAdminNumber;
        CustomEditText etAdminNumber;
        CustomEditText etFolkGuideAbbr;
        TextView etSignUpZone;
        CustomEditText etFirstName;
        CustomEditText etLastName;
        CustomEditText etEmail;
        CustomEditText etPhone;
        CustomEditText etPassword;
        Button createAccount;
        TextView tvSignUpMemberType;

        ImageView ivOpenGallery;
        ImageView ivSetProfileImage;
        TextView tvOpenGallery;

        private String lastFourDigits;
        private String newImagePath = null;
        private final ArrayList<PersonModel> imageUriArray = new ArrayList<>();
        private final ArrayList<String> imageExtensionStringArray = new ArrayList<>();
        private final ArrayList<String> imageNameStringArray = new ArrayList<>();
        private final ArrayList<String> imageFilePathsStringArray = new ArrayList<>();

        private ProgressDialog dialog;

        private ProgressDialog loadingBar;

        private FirebaseAuth fireAuth;
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
            etSignUpZone = view.findViewById(R.id.et_signup_zone_type);
            tvAdminNumber = view.findViewById(R.id.tv_signup_admin_number);
            etAdminNumber = view.findViewById(R.id.et_signup_admin_number);
            etFirstName = view.findViewById(R.id.et_signup_first_name);
            etLastName = view.findViewById(R.id.et_signup_last_name);
            etEmail = view.findViewById(R.id.et_signup_email);
            etPhone = view.findViewById(R.id.et_signup_phone);
            etPassword = view.findViewById(R.id.et_login_password);
            etFolkGuideAbbr = view.findViewById(R.id.et_signup_folk_guide);
            createAccount = view.findViewById(R.id.btn_create_account);
            ivOpenGallery = view.findViewById(R.id.iv_open_gallery);
            ivSetProfileImage = view.findViewById(R.id.iv_set_profile_image);
            tvOpenGallery = view.findViewById(R.id.tv_open_gallery);
            tvSignUpMemberType = view.findViewById(R.id.et_member_type);

            ivOpenGallery.setOnClickListener(view14 -> {
                Dexter.withActivity(getActivity())
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog(getActivity());
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            });

            tvOpenGallery.setOnClickListener(view15 -> showImagePickerOptions());

            tvSignUpMemberType.setOnClickListener(view16 -> dialogSignUpMemberType());

            tvTermsPrivacy.setOnClickListener(view1 -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iskconbangalore.org/privacy-policy/"))));

            etSignUpZone.setOnClickListener(view12 -> dialogSignUpZone());

            createAccount.setOnClickListener(view13 -> {
                if (hasValidInput(
                        etSignUpZone,
                        tvSignUpMemberType,
                        etAdminNumber,
                        etFolkGuideAbbr,
                        etFirstName,
                        etLastName,
                        etEmail,
                        etPhone,
                        etPassword)) {

                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Please wait, while we are checking the credentials!");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    String zone = etSignUpZone.getText().toString();
                    String memberType = tvSignUpMemberType.getText().toString();
                    String adminNumber = etAdminNumber.getText().toString();
                    String folkGuideAbbr = etFolkGuideAbbr.getText().toString().trim();
                    String firstName = etFirstName.getText().toString();
                    String lastName = etLastName.getText().toString();
                    String email = etEmail.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    String password = etPassword.getText().toString();

                    // Create user using Firestore DB after validating input
                    createUserFirestore(
                            zone,
                            memberType,
                            adminNumber,
                            folkGuideAbbr,
                            firstName,
                            lastName,
                            email,
                            phone,
                            password);
                }
            });

            return view;
        }

        private boolean hasValidInput(
                TextView etSignUpZone,
                TextView tvSignUpMemberType,
                CustomEditText etAdminNumber,
                CustomEditText etFolkGuideAbbr,
                CustomEditText etFirstName,
                CustomEditText etLastName,
                CustomEditText etEmail,
                CustomEditText etPhone,
                CustomEditText etPassword) {

            String zone = etSignUpZone.getText().toString();
            String memberType = tvSignUpMemberType.getText().toString();
            String adminNumber = etAdminNumber.getText().toString();
            String folkGuideAbbr = etFolkGuideAbbr.getText().toString().trim();
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (zone.equals("")) {
                etSignUpZone.setError("Required! Select a Zone.");
                etSignUpZone.requestFocus();
                return false;
            }

            if (memberType.equals("")) {
                tvSignUpMemberType.setError("Required! Select a Member Type.");
                tvSignUpMemberType.requestFocus();
                return false;
            }

            if (adminNumber.equals("")) {
                etAdminNumber.setError("Admin Number is Required!");
                etAdminNumber.requestFocus();
                return false;
            }

            if (folkGuideAbbr.equals("")) {
                etFolkGuideAbbr.setError("FOLK Guide is Required!");
                etFolkGuideAbbr.requestFocus();
                return false;
            }

            if (firstName.equals("")) {
                etFirstName.setError("First Name is Required!");
                etFirstName.requestFocus();
                return false;
            }

            if (lastName.equals("")) {
                etLastName.setError("Last Name is Required!");
                etLastName.requestFocus();
                return false;
            }

            if (email.equals("")) {
                etEmail.setError("Email is Required!");
                etEmail.requestFocus();
                return false;
            }

            if (!Helper.hasValidEmail(email)) {
                etEmail.setError("Invalid Email!");
                etEmail.requestFocus();
                return false;
            }

            if (phone.equals("")) {
                etPhone.setError("Phone Number is Required!");
                etPhone.requestFocus();
                return false;
            }

            if (phone.length() < 10) {
                etPhone.setError("Provide a valid Mobile Number!");
                etPhone.requestFocus();
                return false;
            }

            if (password.equals("")) {
                etPassword.setError("Password is Required!");
                etPassword.requestFocus();
                return false;
            }

            if (!Helper.hasValidPassword(password)) {
                etPassword.setError("Password must have at least 8 characters with One Uppercase and One lower case. These Special Characters are allwoed .,#@-_+!?;':*");
                etPassword.requestFocus();
                return false;
            }
            return true;
        }

        private void createUserFirestore(
                String zone,
                String memberType,
                String adminNumber,
                String folkGuideAbbr,
                String firstName,
                String lastName,
                String email,
                String phone,
                String password) {

            // User obj
            User user = new User(
                    zone,
                    memberType,
                    adminNumber,
                    folkGuideAbbr,
                    firstName,
                    lastName,
                    phone,
                    email,
                    password
            );

            // Save User obj to Firestore - Add a new document with a generated ID
            // Collection name is "User". U can create a new collection this way
            db.collection("FolkPeople")
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

        public void dialogSignUpMemberType() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("I am a");
            String[] selectArray = {"Team Lead", "FOLK Guide"};
            builder.setItems(selectArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            tvSignUpMemberType.setText("Team Lead");
                            break;
                        case 1:
                            tvSignUpMemberType.setText("FOLK Guide");
                            break;
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void showImagePickerOptions() {
            FilePickerBuilder.getInstance()
                    .setSelectedFiles(imageFilePathsStringArray)
                    .setActivityTheme(R.style.LibAppTheme)
                    .setMaxCount(1)
                    .pickPhoto(this);
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

        private void uploadImages(ArrayList<PersonModel> imgUriArray, ArrayList<String> imgExtArray, ArrayList<String> imgNameArray) {
            // if adding in storage is successful then add the entry of the url in Firestore
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

            for (int i = 0; i < imgUriArray.size(); i++) {
                final int finalI = i;

                // First put file in Storage
                FirebaseStorage.getInstance().getReference()
                        .child("chatImages/")
                        .child(imgUriArray.get(i).getImageName())
                        .putFile(imgUriArray.get(i).getIvProfileImage())
                        .addOnCompleteListener(task -> {

                            // Then get the download URL with the filename from Storage
                            if (task.isSuccessful()) {
                                FirebaseStorage.getInstance().getReference()
                                        .child("chatImages/")
                                        .child(imgUriArray.get(finalI).getImageName())
                                        .getDownloadUrl()
                                        .addOnCompleteListener(task1 -> {

                                            progressDialog.setMessage("Uploading");
                                            if (task1.isSuccessful()) {

                                                // Here u must add the Uri to Firestore
                                                Log.d(TAG, "task data: " + task1.getResult().toString());
//                                                sendImageMessage(progressDialog, "U got an Image message", task1.getResult().toString(), imgNameArray.get(finalI), imgExtArray.get(finalI));
                                                progressDialog.dismiss();

                                            } else {
                                                FirebaseStorage.getInstance().getReference()
                                                        .child("chatImages/")
                                                        .child(imageUriArray.get(finalI).getImageName())
                                                        .delete();
                                                Toast.makeText(getActivity(), "Couldn't upload Image", Toast.LENGTH_SHORT).show();
                                            }

                                        });
                            } else {
                                Toast.makeText(getActivity(), "Some Error. Couldn't Uplaod", Toast.LENGTH_SHORT).show();
                            }

                        });
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // On Back press
            if (resultCode == RESULT_CANCELED) {
                return;
            }

            // Gallery Result
            if (data != null && requestCode == FilePickerConst.REQUEST_CODE_PHOTO && resultCode == Activity.RESULT_OK) {

                imageFilePathsStringArray.addAll(Objects.requireNonNull(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)));
                newImagePath = imageFilePathsStringArray.get(0);

                dialog = new ProgressDialog(getActivity());
                dialog.show();

                File file = null;
                byte[] bytesArray = new byte[0];
                final ByteArrayOutputStream[] stream = {null};
                lastFourDigits = "";     //substring containing last 4 characters
                int fileSize = 0;
                String imageName = null;

                // Check if img total length is > 4 including extension n dot
                if (newImagePath.length() > 4) {
                    lastFourDigits = newImagePath.substring(newImagePath.length() - 3);
                }
                System.out.println("Image name: " + newImagePath);
                System.out.println("Last 4 digits: " + lastFourDigits);
                System.out.println("img path nnn: " + newImagePath);

                // Get Image Name
                int slice = newImagePath.lastIndexOf('/');
                if (slice != -1) {
                    imageName = newImagePath.substring(slice + 1);
                    Log.d(TAG, "Image Name: " + imageName);
                }

                Log.d(TAG, "new img path 1: " + newImagePath);

                // Get byte array from file path
                if (newImagePath != null) {
                    file = new File(newImagePath);
                    Log.d(TAG, "file info: " + file);
                    Log.d(TAG, "file length: " + (int) file.length());

                    bytesArray = new byte[(int) file.length()];


//                    RandomAccessFile f = null;
//                    try {
//                        f = new RandomAccessFile(newImagePath, "r");
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    byte[] bytes = new byte[0];
//                    try {
//                        bytes = new byte[(int) f.length()];
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        f.read(bytes);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        f.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    System.out.println("bytesArray 1: " + Arrays.toString(bytesArray));
                    System.out.println("file size: " + (int) file.length() / (1024 * 1024) + " mb");
                    fileSize = (int) file.length() / (1024 * 1024);
                } else {
                    Toast.makeText(getActivity(), "File Path is Empty", Toast.LENGTH_SHORT).show();
                }

                if (fileSize <= 5.0) {

                    // Get Uri from file
                    Uri imgUri = Uri.fromFile(new File(newImagePath));

                    PersonModel personModel = new PersonModel();
                    personModel.setIvProfileImage(imgUri);
                    personModel.setImageName(imageName);
                    personModel.setImageExtension(lastFourDigits);

                    imageUriArray.add(personModel);
                    imageExtensionStringArray.add(lastFourDigits);
                    imageNameStringArray.add(imageName);

                    Log.d(TAG, "onActivityResult: uri imz: " + imgUri);

                    // All green then upload to Firestore
//                    uploadImages(imageUriArray, imageExtensionStringArray, imageNameStringArray);

                    ivSetProfileImage.setImageURI(imgUri);
                    ivSetProfileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    ivOpenGallery.setVisibility(View.GONE);
                    tvOpenGallery.setText("Change Profile Picture");
                    imageFilePathsStringArray.clear();

                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Max file size is 5 MB only!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}