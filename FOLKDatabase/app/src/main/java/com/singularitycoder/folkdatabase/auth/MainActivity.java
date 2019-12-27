package com.singularitycoder.folkdatabase.auth;


import android.Manifest;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.SplashActivity;
import com.singularitycoder.folkdatabase.database.ContactItem;
import com.singularitycoder.folkdatabase.helper.ApiEndPoints;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.helper.HelperCustomEditText;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.helper.HelperSharedPreference;
import com.singularitycoder.folkdatabase.helper.RetrofitClientInstance;
import com.singularitycoder.folkdatabase.home.HomeActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.singularitycoder.folkdatabase.helper.HelperImage.showSettingsDialog;
import static java.lang.String.valueOf;

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
        adapter.addFrag(new SignUpFragment(), "SIGN UP");
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
//                        Toast.makeText(getApplicationContext(), "You clciked this 1", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
//                        Toast.makeText(getApplicationContext(), "You clciked this 2", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
//                        Snackbar.make(mCoordinatorLayout, "1 got away", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
//                        Snackbar.make(mCoordinatorLayout, "2 got away", Snackbar.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
//                        Toast.makeText(getApplicationContext(), "You clciked 1 again", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
//                        Toast.makeText(getApplicationContext(), "You clciked 2 again", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }


    private void setUpToolbar() {
        // Set Toolbar
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
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
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (Math.abs(verticalOffset) - appBarLayout1.getTotalScrollRange() == 0) {
                //  Collapsed
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
            }
        });
    }


    private void setUpCollapsingToolbar() {
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

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public static class LoginFragment extends Fragment {
        private HelperCustomEditText etEmail;
        private HelperCustomEditText etPassword;
        private Button btnLogin;
        private TextView tvForgotPassword;
        private TextView tvNotMember;
        private TextView tvShowHidePassword;
        private ProgressDialog loadingBar;
        private FirebaseFirestore firestore;
        private FirebaseAuth firebaseAuth;


        public LoginFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_login, container, false);
            if ((null) != getActivity()) {
                init(view);
                clickListeners();
                showHidePassword();
            }
            return view;
        }


        private void init(View view) {
            firebaseAuth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();
            if ((null) != getActivity()) loadingBar = new ProgressDialog(getActivity());
            etEmail = view.findViewById(R.id.et_login_email);
            etPassword = view.findViewById(R.id.et_login_password);
            tvForgotPassword = view.findViewById(R.id.tv_login_forgot_password);
            btnLogin = view.findViewById(R.id.btn_login);
            tvNotMember = view.findViewById(R.id.tv_login_create_account);
            tvShowHidePassword = view.findViewById(R.id.tv_show_password);
        }


        private void clickListeners() {
            tvForgotPassword.setOnClickListener(view12 -> {
                if ((null) != getActivity()) {
                    dialogForgotPassword(Objects.requireNonNull(getActivity()));
                }
            });

            btnLogin.setOnClickListener(view1 -> {
                if (HelperGeneral.hasInternet(Objects.requireNonNull(getContext()))) {
                    if (hasValidInput(etEmail, etPassword)) {
                        loadingBar.setTitle("Login Account");
                        loadingBar.setMessage("Please wait, while we are checking the credentials!");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        String email = valueOf(etEmail.getText()).trim();
                        String password = valueOf(etPassword.getText());

                        AsyncTask.execute(() -> loginUser(email, password));
                    }
                } else {
                    if ((null) != getActivity()) {
                        Toast.makeText(Objects.requireNonNull(getActivity()), "Bad Network!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            tvNotMember.setOnClickListener(view13 -> authTabLayout.getTabAt(0).select());
        }


        private void showHidePassword() {
            etPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!etPassword.getText().toString().equals("")) {
                        tvShowHidePassword.setVisibility(View.VISIBLE);
                    } else {
                        tvShowHidePassword.setVisibility(View.GONE);
                    }
                }
            });


            tvShowHidePassword.setOnClickListener(view -> {
                if (tvShowHidePassword.getText().toString().trim().equals("SHOW")) {
                    Log.d(TAG, "showHidePassword: got hit 1");
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    tvShowHidePassword.setText("HIDE");
                } else {
                    Log.d(TAG, "showHidePassword: got hit 2");
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    tvShowHidePassword.setText("SHOW");
                }
            });
        }


        private boolean hasValidInput(HelperCustomEditText etEmail, HelperCustomEditText etPassword) {
            String email = valueOf(etEmail.getText()).trim();
            String password = valueOf(etPassword.getText());

            if (email.equals("")) {
                etEmail.setError("Email is Required!");
                etEmail.requestFocus();
                return false;
            }

            if (!HelperGeneral.hasValidEmail(email)) {
                etEmail.setError("Invalid Email!");
                etEmail.requestFocus();
                return false;
            }

            if (password.equals("")) {
                etPassword.setError("Password is Required!");
                etPassword.requestFocus();
                return false;
            }

            if (!HelperGeneral.hasValidPassword(password)) {
                etPassword.setError("Password must have at least 8 characters with One Uppercase and One lower case. These Special Characters are allwoed .,#@-_+!?;':*");
                etPassword.requestFocus();
                return false;
            }
            return true;
        }


        // Login using Firebase Auth
        private void loginUser(String email, String password) {
            if ((null) != getActivity()) {
                loadingBar.show();
                loadingBar.setMessage("Please wait, while we are checking the credentials!");
                loadingBar.setCanceledOnTouchOutside(false);
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                            loadingBar.dismiss();
                            if (task.isSuccessful()) {
                                if ((null) != getActivity()) {
                                    // get data of document with the email id
                                    // pass that data through serializable intent or
                                    // store it in shared prefs
                                    // or get the doc id and load the details in the home page live

                                    if (null != getActivity()) {
                                        SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("authItem", Context.MODE_PRIVATE);
                                        sp.edit().putString("email", email).apply();
                                    }

                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    startActivity(intent);
                                    Objects.requireNonNull(getActivity()).finish();
                                }
                            } else {
                                if ((null) != getActivity()) {
                                    Toast.makeText(getActivity(), "Failed to login. Please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
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
            HelperCustomEditText etResetEmail = dialog.findViewById(R.id.et_reset_email);
            Button btnReset = dialog.findViewById(R.id.btn_reset_password);

            imgClose.setOnClickListener(view -> dialog.dismiss());

            btnReset.setOnClickListener(view -> {
                Log.d(TAG, "dialogForgotPassword: email: " + valueOf(etResetEmail.getText()).trim());
                if (valueOf(etResetEmail.getText()).trim().equals("")) {
                    etResetEmail.setError("Email cannot be empty!");
                    etResetEmail.requestFocus();
                } else if (!HelperGeneral.hasValidEmail(valueOf(etResetEmail.getText()).trim())) {
                    etResetEmail.setError("Invalid Email!");
                    etResetEmail.requestFocus();
                } else {
                    AsyncTask.execute(() -> resetPassword(valueOf(etResetEmail.getText()).trim()));
                    dialog.dismiss();
                }
            });

            dialog.show();
        }


        private void resetPassword(String email) {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    loadingBar.show();
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                });
            }
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if ((null) != getActivity()) {
                                Toast.makeText(getActivity(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if ((null) != getActivity()) {
                                Toast.makeText(getActivity(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            if ((null) != getActivity()) {
                getActivity().runOnUiThread(() -> loadingBar.dismiss());
            }
        }
    }


    public static class SignUpFragment extends Fragment {
        private TextView tvTermsPrivacy;
        private TextView tvMemberType;
        private TextView tvDirectAuthorityTitle;
        private TextView tvOpenGallery;
        private TextView tvShowHidePassword;
        private TextView tvDirectAuthority;
        private HelperCustomEditText etShortName;
        private TextView etSignUpZone;
        private HelperCustomEditText etFullName;
        private HelperCustomEditText etEmail;
        private HelperCustomEditText etGmail;
        private HelperCustomEditText etPhone;
        private HelperCustomEditText etPassword;
        private TextView tvHkmJoiningDate;
        private Button btnCreateAccount;
        private TextView tvSignUpMemberType;
        private ImageView ivOpenGallery;
        private ImageView ivSetProfileImage;

        private String adminKey;
        private String lastFourDigits;
        private String newImagePath = null;
        private String[] zones;
        private String[] teamLeads;

        private final ArrayList<ContactItem> imageUriArray = new ArrayList<>();
        private final ArrayList<String> imageExtensionStringArray = new ArrayList<>();
        private final ArrayList<String> imageNameStringArray = new ArrayList<>();
        private final ArrayList<String> imageFilePathsStringArray = new ArrayList<>();

        private ProgressDialog dialog;
        private ProgressDialog loadingBar;
        private FirebaseAuth firebaseAuth;
        private FirebaseFirestore firestore;

        public SignUpFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_signup, container, false);
            if ((null) != getActivity()) {
                init(view);
                authCheck();
                clickListeners();
                showHidePassword();
            }
            return view;
        }


        private void init(View view) {
            firebaseAuth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();
            Log.d(TAG, "init: firebase instance: " + firestore + " auth: " + firebaseAuth);
            if ((null) != getActivity()) loadingBar = new ProgressDialog(getActivity());
            tvTermsPrivacy = view.findViewById(R.id.tv_signup_terms);
            etSignUpZone = view.findViewById(R.id.et_signup_zone_type);
            tvDirectAuthorityTitle = view.findViewById(R.id.tv_signup_direct_authority_title);
            tvDirectAuthority = view.findViewById(R.id.tv_signup_direct_authority);
            etFullName = view.findViewById(R.id.et_signup_first_name);
            etEmail = view.findViewById(R.id.et_signup_email);
            etGmail = view.findViewById(R.id.et_signup_gmail);
            etPhone = view.findViewById(R.id.et_signup_phone);
            etPassword = view.findViewById(R.id.et_signup_password);
            etShortName = view.findViewById(R.id.et_signup_short_name);
            btnCreateAccount = view.findViewById(R.id.btn_create_account);
            ivOpenGallery = view.findViewById(R.id.iv_open_gallery);
            ivSetProfileImage = view.findViewById(R.id.iv_set_profile_image);
            tvOpenGallery = view.findViewById(R.id.tv_open_gallery);
            tvSignUpMemberType = view.findViewById(R.id.et_member_type);
            tvHkmJoiningDate = view.findViewById(R.id.tv_signup_hkm_joining_date_picker);
            tvShowHidePassword = view.findViewById(R.id.tv_show_password);
        }


        private void authCheck() {
            if (firebaseAuth.getCurrentUser() != null) {
                // check if key is false. If ture then send to main activity
                // if shared pref value is not null n if true or false

                Log.d(TAG, "authCheck: isAuthing");

                if (null != getActivity()) {
                    HelperSharedPreference helperSharedPreference = HelperSharedPreference.getInstance(getActivity());
                    String signUpStatus = helperSharedPreference.getSignupStatus();
                    if (null != getActivity()) {
                        startActivity(new Intent(getActivity(), AuthApprovalStatusActivity.class));
                        Objects.requireNonNull(getActivity()).finish();
                    }

                    if (null != signUpStatus) {
                        if (("false").equals(signUpStatus)) {
                            if (null != getActivity()) {
                                startActivity(new Intent(getActivity(), AuthApprovalStatusActivity.class));
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        } else {
                            if (null != getActivity()) {
                                startActivity(new Intent(getActivity(), HomeActivity.class));
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        }
                    }
                }
            }
        }


        private void clickListeners() {
            ivOpenGallery.setOnClickListener(view14 -> {
                if (null != getActivity()) {
                    Dexter.withActivity(getActivity())
                            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        showImagePickerOptions();
                                    }
                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        if (null != getActivity()) {
                                            showSettingsDialog(getActivity());
                                        }
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();
                }
            });


            tvOpenGallery.setOnClickListener(view15 -> {
                if (null != getActivity()) {
                    Dexter.withActivity(getActivity())
                            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        showImagePickerOptions();
                                    }
                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        if (null != getActivity()) {
                                            showSettingsDialog(getActivity());
                                        }
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();
                }
            });

            tvSignUpMemberType.setOnClickListener(view16 -> dialogSignUpMemberType());

            tvDirectAuthority.setOnClickListener(view -> {
                if (!("").equals(valueOf(etSignUpZone.getText()))) {
                    AsyncTask.execute(this::getTeamLeadsFromApi);
                } else {
                    etSignUpZone.setError("Select a Zone first!");
                    Toast.makeText(getActivity(), "Select a Zone first!", Toast.LENGTH_SHORT).show();
                }
            });

            tvTermsPrivacy.setOnClickListener(view1 -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iskconbangalore.org/privacy-policy/"))));

            etSignUpZone.setOnClickListener(view12 -> {
                AsyncTask.execute(this::getZoneDataFromApi);
            });

            tvHkmJoiningDate.setOnClickListener(view -> HelperGeneral.showDatePickerOldStyle(tvHkmJoiningDate, getActivity()));

//            etShortName.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    new Handler().postDelayed((Runnable) () -> {
//                        if (!("").equals(valueOf(etShortName.getText()).trim().replaceAll("\\s+", ""))) {
//                            if (valueOf(etShortName.getText()).trim().replaceAll("\\s+", "").length() >= 4) {
//                                Log.d(TAG, "afterTextChanged: " + valueOf(editable));
//                                doesAuthorityShortNameExistApi(valueOf(editable), "TL");
//                            }
//                        } else {
//                            HelperGeneral.dialogShowMessage(getActivity(), "Short Name cannot be empty!");
//                        }
//                    }, 2000);
//                }
//            });


            etShortName.setOnFocusChangeListener((view, b) -> {
                if (!view.hasFocus()) {
                    if (!("").equals(valueOf(etShortName.getText()).trim().replaceAll("\\s+", ""))) {
                        if (valueOf(etShortName.getText()).trim().replaceAll("\\s+", "").length() >= 4) {
                            doesAuthorityShortNameExistApi(valueOf(etShortName.getText()).trim().replaceAll("\\s+", ""), "TL");
                        }
                    } else {
                        HelperGeneral.dialogShowMessage(getActivity(), "Short Name cannot be empty!");
                    }
                }
            });

            btnCreateAccount.setOnClickListener(view13 -> {
                if (HelperGeneral.hasInternet(Objects.requireNonNull(getContext()))) {
                    if (hasValidInput(
                            etSignUpZone,
                            tvSignUpMemberType,
                            tvDirectAuthority,
                            etShortName,
                            tvHkmJoiningDate,
                            etFullName,
                            etEmail,
                            etGmail,
                            etPhone,
                            etPassword)) {

                        loadingBar.setMessage("Creating account...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        String zone = valueOf(etSignUpZone.getText());
                        String memberType = valueOf(tvSignUpMemberType.getText());
                        String directAuthority = valueOf(tvDirectAuthority.getText());
                        String folkGuideAbbr = valueOf(etShortName.getText()).trim();
                        String kcExperience = valueOf(tvHkmJoiningDate.getText()).trim();
                        String firstName = valueOf(etFullName.getText());
                        String email = valueOf(etEmail.getText()).trim();
                        String gmail = valueOf(etGmail.getText()).trim();
                        String phone = valueOf(etPhone.getText()).trim();
                        String password = valueOf(etPassword.getText());

                        // 1. First firebase auth
                        AsyncTask.execute(() -> {
                            createAccount(
                                    zone,
                                    memberType,
                                    directAuthority,
                                    folkGuideAbbr,
                                    kcExperience,
                                    firstName,
                                    email,
                                    gmail,
                                    phone,
                                    password,
                                    HelperGeneral.currentDateTime());
                        });
                    }
                } else {
                    if (null != getActivity()) {
                        Toast.makeText(Objects.requireNonNull(getActivity()), "Bad Network!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        private void showHidePassword() {
            etPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!etPassword.getText().toString().equals("")) {
                        tvShowHidePassword.setVisibility(View.VISIBLE);
                    } else {
                        tvShowHidePassword.setVisibility(View.GONE);
                    }
                }
            });

            tvShowHidePassword.setOnClickListener(view -> {
                if (tvShowHidePassword.getText().toString().trim().equals("SHOW")) {
                    Log.d(TAG, "showHidePassword: got hit 1");
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    tvShowHidePassword.setText("HIDE");
                } else {
                    Log.d(TAG, "showHidePassword: got hit 2");
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    tvShowHidePassword.setText("SHOW");
                }
            });
        }


        private String adminKey() {
            FirebaseFirestore
                    .getInstance()
                    .collection("AdminKey")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            adminKey = queryDocumentSnapshots.getDocuments().get(0).getString("key");
                            Log.d(TAG, "onSuccess: adminkey: " + valueOf(queryDocumentSnapshots.getDocuments().get(0).get("key")));
                        }
                    })
                    .addOnFailureListener(e -> Log.d(TAG, "onFailure: got hit"));
            return adminKey;
        }


        private boolean hasValidInput(
                TextView etSignUpZone,
                TextView tvSignUpMemberType,
                TextView tvDirectAuthority,
                HelperCustomEditText etShortName,
                TextView etHkmJoiningDate,
                HelperCustomEditText etFirstName,
                HelperCustomEditText etEmail,
                HelperCustomEditText etGmail,
                HelperCustomEditText etPhone,
                HelperCustomEditText etPassword) {

            String zone = valueOf(etSignUpZone.getText());
            String memberType = valueOf(tvSignUpMemberType.getText());
            String directAuthority = valueOf(tvDirectAuthority.getText());
            String folkGuideAbbr = valueOf(etShortName.getText()).trim();
            String kcExperience = valueOf(etHkmJoiningDate.getText()).trim();
            String firstName = valueOf(etFirstName.getText());
            String email = valueOf(etEmail.getText()).trim();
            String gmail = valueOf(etGmail.getText()).trim();
            String phone = valueOf(etPhone.getText()).trim();
            String password = valueOf(etPassword.getText());

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

            if (directAuthority.equals("")) {
                tvDirectAuthority.setError("Direct Authority is Required!");
                tvDirectAuthority.requestFocus();
                return false;
            }

            if (folkGuideAbbr.equals("")) {
                etShortName.setError("FOLK Guide is Required!");
                etShortName.requestFocus();
                return false;
            }

            if (kcExperience.equals("")) {
                etHkmJoiningDate.setError("KC Experience is Required!");
                etHkmJoiningDate.requestFocus();
                return false;
            }

            if (firstName.equals("")) {
                etFirstName.setError("First Name is Required!");
                etFirstName.requestFocus();
                return false;
            }

            if (email.equals("")) {
                etEmail.setError("Email is Required!");
                etEmail.requestFocus();
                return false;
            }

            if (gmail.equals("")) {
                etGmail.setError("Gmail is Required!");
                etGmail.requestFocus();
                return false;
            }

            if (!HelperGeneral.hasValidEmail(email)) {
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

            if (!HelperGeneral.hasValidPassword(password)) {
                etPassword.setError("Password must have at least 8 characters with One Uppercase and One lower case. These Special Characters are allwoed .,#@-_+!?;':*");
                etPassword.requestFocus();
                return false;
            }
            return true;
        }


        private void createAccount(
                String zone,
                String memberType,
                String directAuthority,
                String folkGuideAbbr,
                String kcExperience,
                String firstName,
                String email,
                String gmail,
                String phone,
                String password,
                String creationTimeStamp) {

            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    loadingBar.show();
                    loadingBar.setMessage("Creating Firebase Auth Account...");
                });
            }

            //create user
            if (null != getActivity()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                            if (null != getActivity()) {
                                Toast.makeText(getActivity(), "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            }
                            if (task.isSuccessful()) {
                                if (null != getActivity()) {
                                    getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                }
                                // 2 If success then store image in storeage, on success of storage create firestore credentials
                                // If profile pic exists
                                if (imageUriArray.size() != 0) {
                                    uploadProfileImage(
                                            imageUriArray,
                                            imageExtensionStringArray,
                                            imageNameStringArray,
                                            zone,
                                            memberType,
                                            directAuthority,
                                            folkGuideAbbr,
                                            kcExperience,
                                            firstName,
                                            email,
                                            gmail,
                                            phone,
                                            password,
                                            creationTimeStamp);
                                } else {
                                    // If profile pic does not exist
                                    // 3. Create user using Firestore DB image storage, get Profile image uri from storage
                                    createUserFirestore(
                                            zone,
                                            memberType,
                                            directAuthority,
                                            folkGuideAbbr,
                                            kcExperience,
                                            firstName,
                                            email,
                                            gmail,
                                            phone,
                                            password,
                                            "",
                                            "false",
                                            creationTimeStamp);
                                }
                            } else {
                                if (null != getActivity()) {
                                    getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                }
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    if (null != getActivity()) {
                                        Toast.makeText(getActivity(), "Email already exists! Login or use different Email!", Toast.LENGTH_SHORT).show();
                                    }
                                    etEmail.setError("Email already exists! Login or use different Email!");
                                } else {
                                    if (null != getActivity()) {
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (null != getActivity()) {
                                    Toast.makeText(getActivity(), "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }


        private void uploadProfileImage(
                ArrayList<ContactItem> imgUriArray,
                ArrayList<String> imgExtArray,
                ArrayList<String> imgNameArray,
                String zone,
                String memberType,
                String directAuthority,
                String folkGuideAbbr,
                String kcExperience,
                String firstName,
                String email,
                String gmail,
                String phone,
                String password,
                String creationTimeStamp) {

            // if adding in storage is successful then add the entry of the url in Firestore
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    loadingBar = new ProgressDialog(getActivity());
                    loadingBar.show();
                    loadingBar.setMessage("Uploading Profile Image...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);
                    loadingBar.show();
                });
            }

            for (int i = 0; i < imgUriArray.size(); i++) {
                final int finalI = i;

                if ((null) != FirebaseStorage.getInstance().getReference().child(HelperConstants.FOLK_PROFILE_IMAGES_PATH)) {
                    // First put file in Storage
                    FirebaseStorage.getInstance().getReference()
                            .child(HelperConstants.FOLK_PROFILE_IMAGES_PATH)
                            .child(imgUriArray.get(i).getImageName())
                            .putFile(imgUriArray.get(i).getIvProfileImage())
                            .addOnCompleteListener(task -> {

                                // Then get the download URL with the filename from Storage
                                if (task.isSuccessful()) {
                                    FirebaseStorage.getInstance().getReference()
                                            .child(HelperConstants.FOLK_PROFILE_IMAGES_PATH)
                                            .child(imgUriArray.get(finalI).getImageName())
                                            .getDownloadUrl()
                                            .addOnCompleteListener(task1 -> {
                                                if (null != getActivity()) {
                                                    getActivity().runOnUiThread(() -> loadingBar.setMessage("Uploading"));
                                                }
                                                if (task1.isSuccessful()) {
                                                    if (null != getActivity()) {
                                                        getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                                    }
                                                    // 3. Create user using Firestore DB image storage, get Profile image uri from storage
                                                    createUserFirestore(
                                                            zone,
                                                            memberType,
                                                            directAuthority,
                                                            folkGuideAbbr,
                                                            kcExperience,
                                                            firstName,
                                                            email,
                                                            gmail,
                                                            phone,
                                                            password,
                                                            valueOf(task1.getResult()),
                                                            "false",
                                                            creationTimeStamp);

                                                    Log.d(TAG, "task data: " + valueOf(task1.getResult()));
                                                } else {
                                                    FirebaseStorage.getInstance().getReference()
                                                            .child(HelperConstants.FOLK_PROFILE_IMAGES_PATH)
                                                            .child(imageUriArray.get(finalI).getImageName())
                                                            .delete();
                                                    if (null != getActivity()) {
                                                        Toast.makeText(getActivity(), "Couldn't upload Image", Toast.LENGTH_SHORT).show();
                                                        getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                                    }
                                                }
                                            });
                                } else {
                                    if (null != getActivity()) {
                                        Toast.makeText(getActivity(), "Some Error. Couldn't Uplaod", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        }


        // 1. Create user
        // 2. Jump to verification screen. Every user jumps to this screen
        // 3. If Zonal Head then turn the key to true
        // 4. Make Zonal Heads get into the app on pressing the check status button - firebase key for that doc == true allow inside app
        // 5. Big issue is sending request. Maintain list of requests in firebase. Send request to zonal head if team lead, if folkguide then to teamlead
        // 5.1 Check if zonal head exits. then send a notification. Populate his list - accept means make value true, else reject. Get doc ids n store under a zonal head.
        private void createUserFirestore(
                String zone,
                String memberType,
                String directAuthority,
                String folkGuideAbbr,
                String kcExperience,
                String firstName,
                String email,
                String gmail,
                String phone,
                String password,
                String profileImage,
                String signUpStatus,
                String creationTimeStamp) {

            getActivity().runOnUiThread(() -> {
                loadingBar = new ProgressDialog(getActivity());
                loadingBar.show();
                loadingBar.setMessage("Setting Firestore Values...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.setCancelable(false);
                loadingBar.show();
            });

            // AuthUserItem obj
            AuthUserItem authUserItem = new AuthUserItem(
                    zone,
                    memberType,
                    directAuthority,
                    folkGuideAbbr,
                    kcExperience,
                    firstName,
                    phone,
                    email,
                    gmail,
                    password,
                    profileImage,
                    signUpStatus,
                    creationTimeStamp
            );

            if (null != getActivity()) {
                SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("authItem", Context.MODE_PRIVATE);
                sp.edit().putString("profileImage", profileImage).apply();
                sp.edit().putString("firstName", firstName).apply();
                sp.edit().putString("memberType", memberType).apply();
                sp.edit().putString("phone", phone).apply();
                sp.edit().putString("email", email).apply();
                sp.edit().putString("gmail", gmail).apply();
                sp.edit().putString("folkGuideAbbr", folkGuideAbbr).apply();
                sp.edit().putString("zone", zone).apply();
            }

            // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
            // Collection name is "AuthUserItem". U can create a new collection this way
            firestore.collection(HelperConstants.AUTH_FOLK_PEOPLE)
                    .add(authUserItem)
                    .addOnSuccessListener(documentReference -> {

                        if (("FOLK Guide").equals(memberType)) {
                            Log.d(TAG, "createUserFirestore: got hittt 1");
                            // AuthUserItem obj
                            AuthUserItem authUserItem1 = new AuthUserItem(
                                    zone,
                                    memberType,
                                    directAuthority,
                                    folkGuideAbbr,
                                    kcExperience,
                                    firstName,
                                    phone,
                                    email,
                                    gmail,
                                    password,
                                    profileImage,
                                    signUpStatus,
                                    creationTimeStamp
                            );

                            // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
                            // Collection name is "AuthUserItem". U can create a new collection this way
                            firestore.collection(HelperConstants.AUTH_FOLK_GUIDES)
                                    .add(authUserItem1)
                                    .addOnSuccessListener(documentReference13 -> {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference13.getId());
                                        if (null != getActivity()) {
                                            Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                        }
                                        // Create another collection for FolkGuideApprovals -> to Team Leads
                                        authUserItem1.setDocId(documentReference13.getId());
                                        btnCreateAccount.setEnabled(false);
                                        approveFolkGuides(documentReference13.getId(), zone, memberType, directAuthority, folkGuideAbbr, firstName, profileImage, signUpStatus, "false", HelperGeneral.currentDateTime());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error adding document", e);
                                        if (null != getActivity()) {
                                            Toast.makeText(getActivity(), "Failed to create AuthUserItem", Toast.LENGTH_SHORT).show();
                                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                        }
                                    });
                        }

                        if (("Team Lead").equals(memberType)) {
                            // AuthUserItem obj
                            AuthUserItem authUserItem1 = new AuthUserItem(
                                    zone,
                                    memberType,
                                    directAuthority,
                                    folkGuideAbbr,
                                    kcExperience,
                                    firstName,
                                    phone,
                                    email,
                                    gmail,
                                    password,
                                    profileImage,
                                    signUpStatus,
                                    creationTimeStamp
                            );

                            // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
                            // Collection name is "AuthUserItem". U can create a new collection this way
                            firestore.collection(HelperConstants.AUTH_FOLK_TEAM_LEADS)
                                    .add(authUserItem1)
                                    .addOnSuccessListener(documentReference12 -> {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference12.getId());
                                        if (null != getActivity()) {
                                            Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                        }
                                        // Collection for Team Lead approvals -> to Zonal Heads
                                        authUserItem1.setDocId(documentReference12.getId());
                                        btnCreateAccount.setEnabled(false);
                                        approveTeamLeads(documentReference12.getId(), zone, memberType, directAuthority, folkGuideAbbr, firstName, profileImage, signUpStatus, "false", HelperGeneral.currentDateTime());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error adding document", e);
                                        if (null != getActivity()) {
                                            Toast.makeText(getActivity(), "Failed to create AuthUserItem", Toast.LENGTH_SHORT).show();
                                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                        }
                                    });
                        }

                        if (("Zonal Head").equals(memberType)) {
                            // AuthUserItem obj
                            AuthUserItem authUserItem1 = new AuthUserItem(
                                    zone,
                                    memberType,
                                    directAuthority,
                                    folkGuideAbbr,
                                    kcExperience,
                                    firstName,
                                    phone,
                                    email,
                                    gmail,
                                    password,
                                    profileImage,
                                    signUpStatus,
                                    creationTimeStamp
                            );

                            // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
                            // Collection name is "AuthUserItem". U can create a new collection this way
                            firestore.collection(HelperConstants.AUTH_FOLK_ZONAL_HEADS)
                                    .add(authUserItem1)
                                    .addOnSuccessListener(documentReference1 -> {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference1.getId());
                                        if (null != getActivity()) {
                                            Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                                            authUserItem1.setDocId(documentReference1.getId());
                                            startActivity(new Intent(getActivity(), AuthApprovalStatusActivity.class));
                                            Objects.requireNonNull(getActivity()).finish();
                                            btnCreateAccount.setEnabled(false);
                                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error adding document", e);
                                        if (null != getActivity()) {
                                            Toast.makeText(getActivity(), "Failed to create AuthUserItem", Toast.LENGTH_SHORT).show();
                                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                        if (null != getActivity()) {
                            Toast.makeText(getActivity(), "Failed to create AuthUserItem", Toast.LENGTH_SHORT).show();
                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                        }
                    });
        }


        private void approveFolkGuides(
                String docId,
                String zone,
                String memberType,
                String directAuthority,
                String folkGuideAbbr,
                String firstName,
                String profileImage,
                String signUpStatus,
                String redFlagStatus,
                String approveRequestTimeStamp) {

            loadingBar = new ProgressDialog(getActivity());
            loadingBar.show();
            loadingBar.setMessage("Setting Approval Values...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.setCancelable(false);
            loadingBar.show();

            AuthUserApprovalItem authUserApprovalItem = new AuthUserApprovalItem(
                    docId,
                    zone,
                    memberType,
                    directAuthority,
                    folkGuideAbbr,
                    firstName,
                    profileImage,
                    signUpStatus,
                    redFlagStatus,
                    approveRequestTimeStamp
            );

            // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
            // Collection name is "AuthUserItem". U can create a new collection this way
            firestore.collection(HelperConstants.AUTH_FOLK_GUIDES).document(docId).collection(HelperConstants.AUTH_FOLK_GUIDE_APPROVALS)
                    .add(authUserApprovalItem)
                    .addOnSuccessListener(documentReference1 -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference1.getId());
                        if (null != getActivity()) {
                            Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), AuthApprovalStatusActivity.class));
                            Objects.requireNonNull(getActivity()).finish();
                            btnCreateAccount.setEnabled(false);
                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                        if (null != getActivity()) {
                            Toast.makeText(getActivity(), "Failed to create AuthUserItem", Toast.LENGTH_SHORT).show();
                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                        }
                    });
        }


        private void approveTeamLeads(
                String docId,
                String zone,
                String memberType,
                String directAuthority,
                String folkGuideAbbr,
                String firstName,
                String profileImage,
                String signUpStatus,
                String redFlagStatus,
                String approveRequestTimeStamp) {

            loadingBar = new ProgressDialog(getActivity());
            loadingBar.show();
            loadingBar.setMessage("Setting Approval Values...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.setCancelable(false);
            loadingBar.show();

            AuthUserApprovalItem authUserApprovalItem = new AuthUserApprovalItem(
                    docId,
                    zone,
                    memberType,
                    directAuthority,
                    folkGuideAbbr,
                    firstName,
                    profileImage,
                    signUpStatus,
                    redFlagStatus,
                    approveRequestTimeStamp
            );

            // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
            // Collection name is "AuthUserItem". U can create a new collection this way
            firestore.collection(HelperConstants.AUTH_FOLK_TEAM_LEADS).document(docId).collection(HelperConstants.AUTH_FOLK_TEAM_LEAD_APPROVALS)
                    .add(authUserApprovalItem)
                    .addOnSuccessListener(documentReference1 -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference1.getId());
                        if (null != getActivity()) {
                            Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), AuthApprovalStatusActivity.class));
                            Objects.requireNonNull(getActivity()).finish();
                            btnCreateAccount.setEnabled(false);
                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                        if (null != getActivity()) {
                            Toast.makeText(getActivity(), "Failed to create AuthUserItem", Toast.LENGTH_SHORT).show();
                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                        }
                    });
        }


        private void getZoneDataFromApi() {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    loadingBar = new ProgressDialog(getActivity());
                    loadingBar.show();
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);
                    loadingBar.show();
                });
                ApiEndPoints apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiEndPoints.class);
                Call<String> call = apiService.getZones();
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(() -> {
                                Log.d(TAG, "onResponse: hit 1");
                                Log.e("TAG", "String Response: " + new Gson().toJson(response.body()));
                                Log.d("Raw Response: ", valueOf(response.raw()));
                                Log.d("Real Response: ", String.valueOf(response.body()));
                                Toast.makeText(getActivity(), " " + response.body(), Toast.LENGTH_LONG).show();

                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.body());
                                            Log.d(TAG, "onResponse: status: " + jsonObject.getString("status"));

                                            if (jsonObject.getString("status").equals("Success")) {
                                                JSONArray jsonArray = jsonObject.getJSONArray("List of Zones");
                                                zones = new String[jsonArray.length()];
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    zones[i] = valueOf(jsonArray.get(i));
                                                    System.out.println("zones: " + valueOf(jsonArray.get(i)));
                                                }
                                                dialogSignUpZone();
                                            }

                                            if (jsonObject.getString("status").equals("Failure")) {
                                                HelperGeneral.dialogShowMessage(getActivity(), jsonObject.getString("message"));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(() -> {
                                Log.d(TAG, "onResponse: hit 2");
                                Log.d(TAG, "onFailure: " + t.getMessage());
                                Log.d(TAG, "onFailure: " + valueOf(call));
                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                                getActivity().runOnUiThread(() -> loadingBar.dismiss());
                            });
                        }
                    }
                });
            }
        }


        private void doesAuthorityShortNameExistApi(String shortName, String memberType) {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    loadingBar = new ProgressDialog(getActivity());
                    loadingBar.show();
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);
                    loadingBar.show();
                });
                ApiEndPoints apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiEndPoints.class);
                Call<String> call = apiService.doesShortNameExist(shortName, memberType);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(() -> {
                                Log.d(TAG, "onResponse: hit 1");
                                Log.e("TAG", "String Response: " + new Gson().toJson(response.body()));
                                Log.d("Raw Response: ", valueOf(response.raw()));
                                Log.d("Real Response: ", valueOf(response.body()));
                                Toast.makeText(getActivity(), " " + response.body(), Toast.LENGTH_LONG).show();

                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.body());
                                            Log.d(TAG, "onResponse: status: " + jsonObject.getString("status"));

                                            if (jsonObject.getString("status").equals("Success")) {
                                                HelperGeneral.dialogShowMessage(getActivity(), jsonObject.getString("message"));
                                            }

                                            if (jsonObject.getString("status").equals("Failure")) {
                                                HelperGeneral.dialogShowMessage(getActivity(), jsonObject.getString("message"));
                                                etShortName.setText("");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(() -> {
                                Log.d(TAG, "onResponse: hit 2");
                                Log.d(TAG, "onFailure: " + t.getMessage());
                                Log.d(TAG, "onFailure: " + valueOf(call));
                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                                getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                etShortName.setText("");
                            });
                        }
                    }
                });
            }
        }


        private void getTeamLeadsFromApi() {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    loadingBar = new ProgressDialog(getActivity());
                    loadingBar.show();
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);
                    loadingBar.show();
                });
                ApiEndPoints apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiEndPoints.class);
                if (!("").equals(valueOf(etSignUpZone.getText()).trim())) {
                    Call<String> call = apiService.getTeamLeadsBasedOnZone("https://us-central1-folk-database.cloudfunctions.net/populateTLsByZone?zone=ISKCONBangalore", valueOf(etSignUpZone.getText()).trim().replaceAll("\\s+", ""));
//                Call<String> call = apiService.getTeamLeadsBasedOnZone(valueOf(etSignUpZone.getText()).trim());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if (null != getActivity()) {
                                getActivity().runOnUiThread(() -> {
                                    Log.d(TAG, "onResponse: hit 1");
                                    Log.e("TAG", "String Response: " + new Gson().toJson(response.body()));
                                    Log.d("Raw Response: ", valueOf(response.raw()));
                                    Log.d("Real Response: ", valueOf(response.body()));
                                    Toast.makeText(getActivity(), " " + response.body(), Toast.LENGTH_LONG).show();

                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response.body());
                                                Log.d(TAG, "onResponse: h0");
                                                Log.d(TAG, "onResponse: status: " + jsonObject.getString("status"));

                                                if (jsonObject.getString("status").equals("Success")) {
                                                    Log.d(TAG, "onResponse: h1");
                                                    // show progress
                                                    JSONArray jsonArray = jsonObject.getJSONArray("TeamLeads");
                                                    teamLeads = new String[jsonArray.length()];
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        teamLeads[i] = valueOf(jsonArray.get(i));
                                                        System.out.println("TeamLeads: " + valueOf(jsonArray.get(i)));
                                                    }
                                                    dialogSignUpAuthorities();
                                                }

                                                if (jsonObject.getString("status").equals("Failure")) {
                                                    Log.d(TAG, "onResponse: h2");
                                                    HelperGeneral.dialogShowMessage(getActivity(), jsonObject.getString("message"));
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                                getActivity().runOnUiThread(() -> loadingBar.dismiss());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            if (null != getActivity()) {
                                getActivity().runOnUiThread(() -> {
                                    Log.d(TAG, "onResponse: hit 2");
                                    Log.d(TAG, "onFailure: " + t.getMessage());
                                    Log.d(TAG, "onFailure: " + valueOf(call));
                                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                                    getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                });
                            }
                        }
                    });
                }
            }
        }


        private void dialogSignUpMemberType() {
            if (null != getActivity()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                builder.setTitle("I am a");
                String[] selectArray = {"FOLK Guide", "Team Lead", "Zonal Head"};
                builder.setItems(selectArray, (dialog, which) -> {
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
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }


        private void dialogSignUpAuthorities() {
            if (null != getActivity()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                builder.setTitle("Direct Authority");
                String[] selectArray = teamLeads;
                builder.setItems(selectArray, (dialog, which) -> {
                    for (int j = 0; j < teamLeads.length; j++) {
                        if (which == j) {
                            tvDirectAuthority.setText(teamLeads[j]);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }


        private void showImagePickerOptions() {
            FilePickerBuilder.getInstance()
                    .setSelectedFiles(imageFilePathsStringArray)
                    .setActivityTheme(R.style.LibAppTheme)
                    .setMaxCount(1)
                    .pickPhoto(this);
        }


        private void dialogSignUpZone() {
            if (null != getActivity()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(Objects.requireNonNull(getActivity())));
                builder.setTitle("Which Zone?");
                String[] selectArray = zones;
                builder.setItems(selectArray, (dialog, which) -> {
                    for (int k = 0; k < zones.length; k++) {
                        if (which == k) {
                            etSignUpZone.setText(zones[k]);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }


        private byte[] getBytes(InputStream inputStream) throws IOException {
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
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

                if ((null) != getActivity()) {
                    dialog = new ProgressDialog(getActivity());
                    dialog.show();
                }

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

                    System.out.println("bytesArray 1: " + Arrays.toString(bytesArray));
                    System.out.println("file size: " + (int) file.length() / (1024 * 1024) + " mb");
                    fileSize = (int) file.length() / (1024 * 1024);
                } else {
                    if ((null) != getActivity()) {
                        Toast.makeText(getActivity(), "File Path is Empty", Toast.LENGTH_SHORT).show();
                    }
                }


                // Get byte data - but not byte Array
                byte[] byteData = new byte[0];
                InputStream iStream = null;
                try {
                    if (null != getActivity()) {
                        iStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(Uri.fromFile(new File(newImagePath)));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    byteData = getBytes(Objects.requireNonNull(iStream));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        iStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    if ((null) != getActivity()) {
                        Toast.makeText(getActivity(), "Max file size is 5 MB only!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}