package com.singularitycoder.folkdatabase.auth;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.database.ContactItem;
import com.singularitycoder.folkdatabase.helper.CustomEditText;
import com.singularitycoder.folkdatabase.helper.Helper;
import com.singularitycoder.folkdatabase.home.HomeActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private static TabLayout authTabLayout;
    private Toolbar toolbar;
    private ImageView headerImage;

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
        authTabLayout = findViewById(R.id.tablayout_main);
        authTabLayout.setupWithViewPager(viewPager);

        // Do something on selecting each tab of tab layout
        authTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
                    authTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    //Expanded
//                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorWhite));
//                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorWhite));
                    toolbar.setBackgroundColor(Color.TRANSPARENT);
                    authTabLayout.setBackgroundColor(Color.TRANSPARENT);
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
        CustomEditText etEmail;
        CustomEditText etPassword;
        Button btnLogin;
        TextView tvForgotPassword;

        // Declare an instance of Firestore
        FirebaseFirestore db;

        TextView tvNotMember;

        private String parentDbName = "AuthUserItem";

        private ProgressDialog loadingBar;

        private FirebaseAuth firebaseAuth;

        public LoginFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_login, container, false);

            firebaseAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            loadingBar = new ProgressDialog(getActivity());

            etEmail = view.findViewById(R.id.et_login_email);
            etPassword = view.findViewById(R.id.et_login_password);
            tvForgotPassword = view.findViewById(R.id.tv_login_forgot_password);
            btnLogin = view.findViewById(R.id.btn_create_event_invite_people);
            tvNotMember = view.findViewById(R.id.tv_login_create_account);

            tvForgotPassword.setOnClickListener(view12 -> dialogForgotPassword(Objects.requireNonNull(getActivity())));

            btnLogin.setOnClickListener(view1 -> {
                if (Helper.hasInternet(Objects.requireNonNull(getContext()))) {
                    if (hasValidInput(etEmail, etPassword)) {
                        loadingBar.setTitle("Login Account");
                        loadingBar.setMessage("Please wait, while we are checking the credentials!");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        String email = etEmail.getText().toString().trim();
                        String password = etPassword.getText().toString();

                        loginUser(email, password);
                    }
                } else {
                    Toast.makeText(Objects.requireNonNull(getActivity()), "Bad Network!", Toast.LENGTH_SHORT).show();
                }
            });

            tvNotMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    authTabLayout.getTabAt(0).select();
                }
            });

            return view;
        }


        private boolean hasValidInput(CustomEditText etEmail, CustomEditText etPassword) {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

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

        // Login using Firebase Auth
        private void loginUser(String email, String password) {
            loadingBar.show();
            loadingBar.setMessage("Please wait, while we are checking the credentials!");
            loadingBar.setCanceledOnTouchOutside(false);
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), task -> {
                        loadingBar.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            Objects.requireNonNull(getActivity()).finish();
                        } else {
                            // there was an error
                            Toast.makeText(getActivity(), "Failed to login. Please try again", Toast.LENGTH_LONG).show();
                        }
                    });
        }


        private void dialogForgotPassword(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_forgot_password);

            Rect displayRectangle = new Rect();
            Window window = activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            Objects.requireNonNull(dialog.getWindow()).setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ImageView imgClose = dialog.findViewById(R.id.img_close);
            CustomEditText etResetEmail = dialog.findViewById(R.id.et_reset_email);
            Button btnReset = dialog.findViewById(R.id.btn_reset_password);

            imgClose.setOnClickListener(view -> dialog.dismiss());

            btnReset.setOnClickListener(view -> {
                Log.d(TAG, "dialogForgotPassword: email: " + etResetEmail.getText().toString().trim());
                if (etResetEmail.getText().toString().trim().equals("")) {
                    etResetEmail.setError("Email cannot be empty!");
                    etResetEmail.requestFocus();
                } else if (!Helper.hasValidEmail(etResetEmail.getText().toString().trim())) {
                    etResetEmail.setError("Invalid Email!");
                    etResetEmail.requestFocus();
                } else {
                    resetPassword(etResetEmail.getText().toString().trim());
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        private void resetPassword(String email) {
            loadingBar.show();
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                        loadingBar.dismiss();
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
        CustomEditText etDepartment;
        CustomEditText etKcExperience;
        Button createAccount;
        TextView tvSignUpMemberType;

        ImageView ivOpenGallery;
        ImageView ivSetProfileImage;
        TextView tvOpenGallery;

        String adminKey;

        private String lastFourDigits;
        private String newImagePath = null;
        private final ArrayList<ContactItem> imageUriArray = new ArrayList<>();
        private final ArrayList<String> imageExtensionStringArray = new ArrayList<>();
        private final ArrayList<String> imageNameStringArray = new ArrayList<>();
        private final ArrayList<String> imageFilePathsStringArray = new ArrayList<>();

        private ProgressDialog dialog;

        private ProgressDialog loadingBar;

        private FirebaseAuth firebaseAuth;
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

            firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
            db = FirebaseFirestore.getInstance();
            loadingBar = new ProgressDialog(getActivity());

            adminKey();
            Log.d(TAG, "adminKey: " + adminKey());

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
            etDepartment = view.findViewById(R.id.et_signup_department);
            etKcExperience = view.findViewById(R.id.et_signup_kc_experience);

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

            tvOpenGallery.setOnClickListener(view15 -> {
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

            tvSignUpMemberType.setOnClickListener(view16 -> dialogSignUpMemberType());

            tvTermsPrivacy.setOnClickListener(view1 -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iskconbangalore.org/privacy-policy/"))));

            etSignUpZone.setOnClickListener(view12 -> dialogSignUpZone());

            createAccount.setOnClickListener(view13 -> {
                if (Helper.hasInternet(Objects.requireNonNull(getContext()))) {
                    if (hasValidInput(
                            etSignUpZone,
                            tvSignUpMemberType,
                            etAdminNumber,
                            etFolkGuideAbbr,
                            etDepartment,
                            etKcExperience,
                            etFirstName,
                            etLastName,
                            etEmail,
                            etPhone,
                            etPassword)) {

                        loadingBar.setMessage("Creating account...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        String zone = etSignUpZone.getText().toString();
                        String memberType = tvSignUpMemberType.getText().toString();
                        String adminNumber = etAdminNumber.getText().toString();
                        String folkGuideAbbr = etFolkGuideAbbr.getText().toString().trim();
                        String department = etDepartment.getText().toString().trim();
                        String kcExperience = etKcExperience.getText().toString().trim();
                        String firstName = etFirstName.getText().toString();
                        String lastName = etLastName.getText().toString();
                        String email = etEmail.getText().toString().trim();
                        String phone = etPhone.getText().toString().trim();
                        String password = etPassword.getText().toString();

                        // 1. First firebase auth
                        createAccount(zone, memberType, adminNumber, folkGuideAbbr, department, kcExperience, firstName, lastName, email, phone, password, Helper.currentDateTime());
                    }
                } else {
                    Toast.makeText(Objects.requireNonNull(getActivity()), "Bad Network!", Toast.LENGTH_SHORT).show();
                }

            });

            return view;
        }

        private String adminKey() {
            FirebaseFirestore
                    .getInstance()
                    .collection("AdminKey")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                adminKey = queryDocumentSnapshots.getDocuments().get(0).getString("key");
                                Log.d(TAG, "onSuccess: adminkey: " + queryDocumentSnapshots.getDocuments().get(0).get("key").toString());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: got hit");
                        }
                    });
            return adminKey;
        }

        private boolean hasValidInput(
                TextView etSignUpZone,
                TextView tvSignUpMemberType,
                CustomEditText etAdminNumber,
                CustomEditText etFolkGuideAbbr,
                CustomEditText etDepartment,
                CustomEditText etKcExperience,
                CustomEditText etFirstName,
                CustomEditText etLastName,
                CustomEditText etEmail,
                CustomEditText etPhone,
                CustomEditText etPassword) {

            String zone = etSignUpZone.getText().toString();
            String memberType = tvSignUpMemberType.getText().toString();
            String adminNumber = etAdminNumber.getText().toString();
            String folkGuideAbbr = etFolkGuideAbbr.getText().toString().trim();
            String department = etDepartment.getText().toString().trim();
            String kcExperience = etKcExperience.getText().toString().trim();
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

            if (!adminNumber.equals("inf1n1teM3rcy")) {
                etAdminNumber.setError("Wrong Admin Number");
                etAdminNumber.requestFocus();
                return false;
            }

            if (folkGuideAbbr.equals("")) {
                etFolkGuideAbbr.setError("FOLK Guide is Required!");
                etFolkGuideAbbr.requestFocus();
                return false;
            }

            if (department.equals("")) {
                etDepartment.setError("Department is Required!");
                etDepartment.requestFocus();
                return false;
            }

            if (kcExperience.equals("")) {
                etKcExperience.setError("KC Experience is Required!");
                etKcExperience.requestFocus();
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


        private void createAccount(String zone, String memberType, String adminNumber, String folkGuideAbbr, String department, String kcExperience, String firstName, String lastName, String email, String phone, String password, String creationTimeStamp) {
            loadingBar.show();
            //create user
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(getActivity(), "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            if (task.isSuccessful()) {
                                // 2 If success then store image in storeage, on success of storage create firestore credentials
                                uploadProfileImage(imageUriArray, imageExtensionStringArray, imageNameStringArray, zone, memberType, adminNumber, folkGuideAbbr, department, kcExperience, firstName, lastName, email, phone, password, creationTimeStamp);
                                loadingBar.dismiss();
                            } else {
                                loadingBar.dismiss();
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getActivity(), "Email already exists! Login or use different Email!", Toast.LENGTH_SHORT).show();
                                    etEmail.setError("Email already exists! Login or use different Email!");
                                } else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                Toast.makeText(getActivity(), "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


        private void uploadProfileImage(
                ArrayList<ContactItem> imgUriArray,
                ArrayList<String> imgExtArray,
                ArrayList<String> imgNameArray,
                String zone,
                String memberType,
                String adminNumber,
                String folkGuideAbbr,
                String department,
                String kcExperience,
                String firstName,
                String lastName,
                String email,
                String phone,
                String password,
                String creationTimeStamp) {
            // if adding in storage is successful then add the entry of the url in Firestore
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

            for (int i = 0; i < imgUriArray.size(); i++) {
                final int finalI = i;

                if ((null) != FirebaseStorage.getInstance().getReference().child("ProfileImages/")) {
                    // First put file in Storage
                    FirebaseStorage.getInstance().getReference()
                            .child("ProfileImages/")
                            .child(imgUriArray.get(i).getImageName())
                            .putFile(imgUriArray.get(i).getIvProfileImage())
                            .addOnCompleteListener(task -> {

                                // Then get the download URL with the filename from Storage
                                if (task.isSuccessful()) {
                                    FirebaseStorage.getInstance().getReference()
                                            .child("ProfileImages/")
                                            .child(imgUriArray.get(finalI).getImageName())
                                            .getDownloadUrl()
                                            .addOnCompleteListener(task1 -> {

                                                progressDialog.setMessage("Uploading");
                                                if (task1.isSuccessful()) {

                                                    // 3. Create user using Firestore DB image storage, get Profile image uri from storage
                                                    createUserFirestore(
                                                            zone,
                                                            memberType,
                                                            adminNumber,
                                                            folkGuideAbbr,
                                                            department,
                                                            kcExperience,
                                                            firstName,
                                                            lastName,
                                                            email,
                                                            phone,
                                                            password,
                                                            Objects.requireNonNull(task1.getResult()).toString(),
                                                            "false",
                                                            creationTimeStamp);

                                                    Log.d(TAG, "task data: " + task1.getResult().toString());
                                                    progressDialog.dismiss();

                                                } else {
                                                    FirebaseStorage.getInstance().getReference()
                                                            .child("ProfileImages/")
                                                            .child(imageUriArray.get(finalI).getImageName())
                                                            .delete();
                                                    Toast.makeText(getActivity(), "Couldn't upload Image", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            });
                                } else {
                                    Toast.makeText(getActivity(), "Some Error. Couldn't Uplaod", Toast.LENGTH_SHORT).show();
                                }

                            });
                }
            }
        }


        private void createUserFirestore(
                String zone,
                String memberType,
                String adminNumber,
                String folkGuideAbbr,
                String department,
                String kcExperience,
                String firstName,
                String lastName,
                String email,
                String phone,
                String password,
                String profileImage,
                String signUpStatus,
                String creationTimeStamp) {

            // AuthUserItem obj
            AuthUserItem authUserItem = new AuthUserItem(
                    zone,
                    memberType,
                    adminNumber,
                    folkGuideAbbr,
                    department,
                    kcExperience,
                    firstName,
                    lastName,
                    phone,
                    email,
                    password,
                    profileImage,
                    signUpStatus,
                    creationTimeStamp
            );

            SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("authItem", Context.MODE_PRIVATE);
            sp.edit().putString("profileImage", profileImage).commit();
            sp.edit().putString("firstName", firstName).commit();
            sp.edit().putString("lastName", lastName).commit();
            sp.edit().putString("memberType", memberType).commit();
            sp.edit().putString("phone", phone).commit();
            sp.edit().putString("email", email).commit();


            // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
            // Collection name is "AuthUserItem". U can create a new collection this way
                db.collection("AllFolkPeople")
                        .add(authUserItem)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                if (("FOLK Guide").equals(memberType)) {
                                    // AuthUserItem obj
                                    AuthUserItem authUserItem = new AuthUserItem(
                                            zone,
                                            memberType,
                                            adminNumber,
                                            folkGuideAbbr,
                                            department,
                                            kcExperience,
                                            firstName,
                                            lastName,
                                            phone,
                                            email,
                                            password,
                                            profileImage,
                                            signUpStatus,
                                            creationTimeStamp
                                    );

                                    // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
                                    // Collection name is "AuthUserItem". U can create a new collection this way
                                        db.collection("AllFolkGuides")
                                                .add(authUserItem)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                        Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getActivity(), HomeActivity.class));
                                                        Objects.requireNonNull(getActivity()).finish();
                                                        loadingBar.dismiss();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding document", e);
                                                        Toast.makeText(getActivity(), "Failed to create AuthUserItem", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }
                                                });
                                }

                                if (("Team Lead").equals(memberType)) {
                                // AuthUserItem obj
                                    AuthUserItem authUserItem = new AuthUserItem(
                                            zone,
                                            memberType,
                                            adminNumber,
                                            folkGuideAbbr,
                                            department,
                                            kcExperience,
                                            firstName,
                                            lastName,
                                            phone,
                                            email,
                                            password,
                                            profileImage,
                                            signUpStatus,
                                            creationTimeStamp
                                    );

                                    // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
                                    // Collection name is "AuthUserItem". U can create a new collection this way
                                    db.collection("AllTeamLeads")
                                            .add(authUserItem)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                                    Objects.requireNonNull(getActivity()).finish();
                                                    loadingBar.dismiss();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                    Toast.makeText(getActivity(), "Failed to create AuthUserItem", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                }
                                            });
                                }

                                if (("Zonal Head").equals(memberType)) {
                                    // AuthUserItem obj
                                    AuthUserItem authUserItem = new AuthUserItem(
                                            zone,
                                            memberType,
                                            adminNumber,
                                            folkGuideAbbr,
                                            department,
                                            kcExperience,
                                            firstName,
                                            lastName,
                                            phone,
                                            email,
                                            password,
                                            profileImage,
                                            signUpStatus,
                                            creationTimeStamp
                                    );

                                    // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
                                    // Collection name is "AuthUserItem". U can create a new collection this way
                                    db.collection("AllZonalHeads")
                                            .add(authUserItem)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                                    Objects.requireNonNull(getActivity()).finish();
                                                    loadingBar.dismiss();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                    Toast.makeText(getActivity(), "Failed to create AuthUserItem", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(getActivity(), "Failed to create AuthUserItem", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        });
        }

        private void dialogSignUpMemberType() {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            builder.setTitle("I am a");
            String[] selectArray = {"FOLK Guide", "Team Lead", "Zonal Head"};
            builder.setItems(selectArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            tvSignUpMemberType.setText("FOLK Guide");
                            break;
                        case 1:
                            tvSignUpMemberType.setText("Team Lead");
                            break;
                        case 2:
                            tvSignUpMemberType.setText("Zonal Head");
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

        private void dialogSignUpZone() {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(Objects.requireNonNull(getActivity())));
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
                    Log.d(TAG, "onActivityResult: uri imz: " + imgUri);

                    ContactItem contactItem = new ContactItem();
                    contactItem.setIvProfileImage(imgUri);
                    contactItem.setImageName(imageName);
                    contactItem.setImageExtension(lastFourDigits);

                    imageUriArray.add(contactItem);
                    imageExtensionStringArray.add(lastFourDigits);
                    imageNameStringArray.add(imageName);

                    ivSetProfileImage.setImageURI(imgUri);

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