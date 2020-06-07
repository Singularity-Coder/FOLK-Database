package com.singularitycoder.folkdatabase.auth.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.model.AuthUserApprovalItem;
import com.singularitycoder.folkdatabase.auth.model.AuthUserItem;
import com.singularitycoder.folkdatabase.database.model.ContactItem;
import com.singularitycoder.folkdatabase.helper.ApiEndPoints;
import com.singularitycoder.folkdatabase.helper.CustomEditText;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.helper.HelperSharedPreference;
import com.singularitycoder.folkdatabase.helper.RetrofitClientInstance;
import com.singularitycoder.folkdatabase.home.view.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static com.singularitycoder.folkdatabase.helper.FolkDatabaseApp.hasInternet;
import static java.lang.String.valueOf;

public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment";

    @Nullable
    @BindView(R.id.tv_signup_terms)
    TextView tvTermsPrivacy;
    @Nullable
    @BindView(R.id.tv_signup_direct_authority_title)
    TextView tvDirectAuthorityTitle;
    @Nullable
    @BindView(R.id.tv_open_gallery)
    TextView tvOpenGallery;
    @Nullable
    @BindView(R.id.tv_show_password)
    TextView tvShowHidePassword;
    @Nullable
    @BindView(R.id.tv_signup_direct_authority)
    TextView tvDirectAuthority;
    @Nullable
    @BindView(R.id.et_signup_zone_type)
    TextView etSignUpZone;
    @Nullable
    @BindView(R.id.et_signup_short_name)
    CustomEditText etShortName;
    @Nullable
    @BindView(R.id.et_signup_first_name)
    CustomEditText etFullName;
    @Nullable
    @BindView(R.id.et_signup_email)
    CustomEditText etEmail;
    @Nullable
    @BindView(R.id.et_signup_gmail)
    CustomEditText etGmail;
    @Nullable
    @BindView(R.id.et_signup_phone)
    CustomEditText etPhone;
    @Nullable
    @BindView(R.id.et_signup_password)
    CustomEditText etPassword;
    @Nullable
    @BindView(R.id.tv_signup_hkm_joining_date_picker)
    TextView tvHkmJoiningDate;
    @Nullable
    @BindView(R.id.btn_create_account)
    Button btnCreateAccount;
    @Nullable
    @BindView(R.id.et_member_type)
    TextView tvSignUpMemberType;
    @Nullable
    @BindView(R.id.iv_open_gallery)
    ImageView ivOpenGallery;
    @Nullable
    @BindView(R.id.iv_set_profile_image)
    ImageView ivSetProfileImage;
    @Nullable
    @BindView(R.id.con_lay_signup)
    ConstraintLayout conLaySignup;

    private final HelperGeneral helperObject = new HelperGeneral();
    private final ArrayList<ContactItem> imageUriArray = new ArrayList<>();
    private final ArrayList<String> imageExtensionStringArray = new ArrayList<>();
    private final ArrayList<String> imageNameStringArray = new ArrayList<>();
    private final ArrayList<String> imageFilePathsStringArray = new ArrayList<>();

    private String adminKey;
    private String lastFourDigits;
    private String newImagePath = null;
    private String profileImageDirectory;
    private String[] zones;
    private String[] teamLeads;

    private Unbinder unbinder;
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
            inits(view);
            authCheck();
            clickListeners();
            showHidePassword();
            helperObject.checkFunctionExecutionTimings();
        }
        return view;
    }


    private void inits(View view) {
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Log.d(TAG, "init: firebase instance: " + firestore + " auth: " + firebaseAuth);
        if ((null) != getActivity()) loadingBar = new ProgressDialog(getActivity());
    }


    private void authCheck() {
        if (null != firebaseAuth.getCurrentUser()) {
            // check if key is false. If ture then send to main activity
            // if shared pref value is not null n if true or false
            if (null != getActivity()) {
                HelperSharedPreference helperSharedPreference = HelperSharedPreference.getInstance(getActivity());
                String signUpStatus = helperSharedPreference.getSignupStatus();
//                    if (null != getActivity()) {
//                        finishAndGoForApproval();
//                    }

                if (null != signUpStatus) {
                    if (("false").equals(signUpStatus)) {
                        if (null != getActivity()) {
                            finishAndGoForApproval();
                        }
                    } else {
                        if (null != getActivity()) {
                            finishAndGoHome();
                        }
                    }
                }
            }
        }
    }


    private void clickListeners() {
        ivOpenGallery.setOnClickListener(view14 -> openGallery());
        tvOpenGallery.setOnClickListener(view15 -> openGallery());
        tvSignUpMemberType.setOnClickListener(view16 -> dialogSignUpMemberType());
        tvDirectAuthority.setOnClickListener(view -> getDirectAuthorityList());
        tvTermsPrivacy.setOnClickListener(view1 -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iskconbangalore.org/privacy-policy/"))));
        etSignUpZone.setOnClickListener(view12 -> AsyncTask.execute(this::getZoneDataFromApi));
        tvHkmJoiningDate.setOnClickListener(view -> helperObject.showDatePickerOldStyle(tvHkmJoiningDate, getActivity()));
        checkForShortNameDuplicates();
        btnCreateAccount.setOnClickListener(view13 -> createMemberAccountOnClick());
    }


    private void getDirectAuthorityList() {
        if (!("").equals(valueOf(etSignUpZone.getText()))) {
            AsyncTask.execute(this::getTeamLeadsFromApi);
        } else {
            etSignUpZone.setError("Select a Zone first!");
            Toast.makeText(getActivity(), "Select a Zone first!", Toast.LENGTH_SHORT).show();
        }
    }


    private void createMemberAccountOnClick() {
        if (hasInternet()) {
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
                            helperObject.currentDateTime(),
                            valueOf(helperObject.getCurrentEpochTime()));
                });
            }
        } else {
            if (null != getActivity()) {
                Toast.makeText(Objects.requireNonNull(getActivity()), "Bad Network!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void checkForShortNameDuplicates() {
        etShortName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                new Handler().postDelayed(() -> {
                    if (null != getActivity()) {
                        if (!("").equals(valueOf(etShortName.getText()).trim().replaceAll("\\s+", ""))) {
                            if (valueOf(etShortName.getText()).trim().replaceAll("\\s+", "").length() >= 4) {
                                Log.d(TAG, "afterTextChanged: " + valueOf(editable));
                                if (!("").equals(valueOf(tvSignUpMemberType.getText()))) {
                                    doesAuthorityShortNameExistApi(valueOf(editable), valueOf(tvSignUpMemberType.getText()).trim().replaceAll("\\s+", ""));
                                } else {
                                    tvSignUpMemberType.setError("Choose a Member Type first!");
                                    Toast.makeText(getActivity(), "Choose a Member Type first!", Toast.LENGTH_SHORT).show();
                                    etShortName.setText("");
                                }
                            }
                        } else {
//                                HelperGeneral.dialogShowMessage(getActivity(), "Short Name cannot be empty!");
                            Toast.makeText(getActivity(), "Short Name cannot be empty!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 4000);
            }
        });
    }


    private void checkForShortNameDuplicatesOnFocusChange() {
        etShortName.setOnFocusChangeListener((view, b) -> {
            if (!view.hasFocus()) {
                if (!("").equals(valueOf(etShortName.getText()).trim().replaceAll("\\s+", ""))) {
                    if (valueOf(etShortName.getText()).trim().replaceAll("\\s+", "").length() >= 4) {
                        doesAuthorityShortNameExistApi(valueOf(etShortName.getText()).trim().replaceAll("\\s+", ""), "TL");
                    }
                } else {
                    new HelperGeneral().dialogActionMessage(getActivity(), null, "Short Name cannot be empty!", "OK", "", null, null, true);
                }
            }
        });
    }


    private void openGallery() {
        if (null != getActivity()) {
            helperObject.checkPermissions(getActivity(), () -> showImagePickerOptions(), Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
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


    private void finishAndGoForApproval() {
        Intent intent = new Intent(getActivity(), AuthApprovalStatusActivity.class);
        intent.putExtra("authType", "SignUp");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    private void finishAndGoHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("authType", "SignUp");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
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
            CustomEditText etShortName,
            TextView etHkmJoiningDate,
            CustomEditText etFirstName,
            CustomEditText etEmail,
            CustomEditText etGmail,
            CustomEditText etPhone,
            CustomEditText etPassword) {

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

//        if (zone.equals("")) {
//            etSignUpZone.setError("Required! Select a Zone.");
//            etSignUpZone.requestFocus();
//            return false;
//        }
//
//        if (memberType.equals("")) {
//            tvSignUpMemberType.setError("Required! Select a Member Type.");
//            tvSignUpMemberType.requestFocus();
//            return false;
//        }
//
//        if (directAuthority.equals("")) {
//            tvDirectAuthority.setError("Direct Authority is Required!");
//            tvDirectAuthority.requestFocus();
//            return false;
//        }
//
//        if (folkGuideAbbr.equals("")) {
//            etShortName.setError("FOLK Guide is Required!");
//            etShortName.requestFocus();
//            return false;
//        }
//
//        if (kcExperience.equals("")) {
//            etHkmJoiningDate.setError("KC Experience is Required!");
//            etHkmJoiningDate.requestFocus();
//            return false;
//        }
//
//        if (firstName.equals("")) {
//            etFirstName.setError("First Name is Required!");
//            etFirstName.requestFocus();
//            return false;
//        }
//
//        if (email.equals("")) {
//            etEmail.setError("Email is Required!");
//            etEmail.requestFocus();
//            return false;
//        }
//
//        if (gmail.equals("")) {
//            etGmail.setError("Gmail is Required!");
//            etGmail.requestFocus();
//            return false;
//        }
//
//        if (!helperObject.hasValidEmail(email)) {
//            etEmail.setError("Invalid Email!");
//            etEmail.requestFocus();
//            return false;
//        }
//
//        if (phone.equals("")) {
//            etPhone.setError("Phone Number is Required!");
//            etPhone.requestFocus();
//            return false;
//        }
//
//        if (phone.length() < 10) {
//            etPhone.setError("Provide a valid Mobile Number!");
//            etPhone.requestFocus();
//            return false;
//        }
//
//        if (password.equals("")) {
//            etPassword.setError("Password is Required!");
//            etPassword.requestFocus();
//            return false;
//        }
//
//        if (!helperObject.hasValidPassword(password)) {
//            etPassword.setError("Password must have at least 8 characters with One Uppercase and One lower case. These Special Characters are allwoed .,#@-_+!?;':*");
//            etPassword.requestFocus();
//            return false;
//        }
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
            String creationTimeStamp,
            String epochTimeStamp) {

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
                                        creationTimeStamp,
                                        epochTimeStamp);
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
                                        creationTimeStamp,
                                        epochTimeStamp);
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
            String creationTimeStamp,
            String currentEpochTime) {

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

        // DEMO
        if (("").equals(memberType)) {
            profileImageDirectory = "Demo/";
        }

        if (("FOLK Guide").equals(memberType)) {
            profileImageDirectory = HelperConstants.DIR_IMAGES_PATH_FOLK_GUIDES;
        }

        if (("Team Lead").equals(memberType)) {
            profileImageDirectory = HelperConstants.DIR_IMAGES_PATH_FOLK_TEAM_LEADS;
        }

        if (("Zonal Head").equals(memberType)) {
            profileImageDirectory = HelperConstants.DIR_IMAGES_PATH_FOLK_ZONAL_HEADS;
        }

        for (int i = 0; i < imgUriArray.size(); i++) {
            final int finalI = i;

            if (null != FirebaseStorage.getInstance().getReference().child(profileImageDirectory)) {
                // First put file in Storage
                FirebaseStorage.getInstance().getReference()
                        .child(profileImageDirectory)
                        .child(imgUriArray.get(i).getImageName())
                        .putFile(imgUriArray.get(i).getIvProfileImage())
                        .addOnCompleteListener(task -> {

                            // Then get the download URL with the filename from Storage
                            if (task.isSuccessful()) {
                                FirebaseStorage.getInstance().getReference()
                                        .child(profileImageDirectory)
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
                                                        creationTimeStamp,
                                                        currentEpochTime);

                                                Log.d(TAG, "task data: " + valueOf(task1.getResult()));
                                            } else {
                                                FirebaseStorage.getInstance().getReference()
                                                        .child(profileImageDirectory)
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
            String creationTimeStamp,
            String epochTimeStamp) {

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
                creationTimeStamp,
                epochTimeStamp
        );

        if (null != getActivity()) {
            // Main Shared Pref
            HelperSharedPreference helperSharedPreference = HelperSharedPreference.getInstance(getActivity());
            helperSharedPreference.setEmail(email);
            helperSharedPreference.setDirectAuthority(directAuthority);
            helperSharedPreference.setFullName(firstName);
        }

        // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
        // Collection name is "AuthUserItem". U can create a new collection this way
        firestore.collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .add(authUserItem)
                .addOnSuccessListener(documentReference -> {

                    // DEMO
                    if (("").equals(memberType)) {
                        if (null != getActivity()) {
                            Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
                        }
                        btnCreateAccount.setEnabled(false);
                        finishAndGoForApproval();
                    }

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
                                creationTimeStamp,
                                epochTimeStamp
                        );

                        // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
                        // Collection name is "AuthUserItem". U can create a new collection this way
                        firestore.collection(HelperConstants.COLL_AUTH_FOLK_GUIDES)
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
                                    approveFolkGuides(documentReference13.getId(), zone, memberType, directAuthority, email, folkGuideAbbr, firstName, profileImage, signUpStatus, "false", helperObject.currentDateTime(), valueOf(helperObject.getCurrentEpochTime()));
                                    approveUsers(documentReference13.getId(), zone, memberType, directAuthority, email, folkGuideAbbr, firstName, profileImage, signUpStatus, "false", helperObject.currentDateTime(), valueOf(helperObject.getCurrentEpochTime()));
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
                                creationTimeStamp,
                                epochTimeStamp
                        );

                        // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
                        // Collection name is "AuthUserItem". U can create a new collection this way
                        firestore.collection(HelperConstants.COLL_AUTH_FOLK_TEAM_LEADS)
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
                                    approveTeamLeads(documentReference12.getId(), zone, memberType, directAuthority, email, folkGuideAbbr, firstName, profileImage, signUpStatus, "false", helperObject.currentDateTime(), valueOf(helperObject.getCurrentEpochTime()));
                                    approveUsers(documentReference12.getId(), zone, memberType, directAuthority, email, folkGuideAbbr, firstName, profileImage, signUpStatus, "false", helperObject.currentDateTime(), valueOf(helperObject.getCurrentEpochTime()));
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
                                creationTimeStamp,
                                epochTimeStamp
                        );

                        // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
                        // Collection name is "AuthUserItem". U can create a new collection this way
                        firestore.collection(HelperConstants.COLL_AUTH_FOLK_ZONAL_HEADS)
                                .add(authUserItem1)
                                .addOnSuccessListener(documentReference1 -> {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference1.getId());
                                    if (null != getActivity()) {
                                        Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                                        authUserItem1.setDocId(documentReference1.getId());
                                        getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                        finishAndGoForApproval();
                                        btnCreateAccount.setEnabled(false);
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


    private void approveUsers(
            String docId,
            String zone,
            String memberType,
            String directAuthority,
            String email,
            String folkGuideAbbr,
            String firstName,
            String profileImage,
            String signUpStatus,
            String redFlagStatus,
            String approveRequestTimeStamp,
            String epochTimeStamp) {

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
                email,
                folkGuideAbbr,
                firstName,
                profileImage,
                signUpStatus,
                redFlagStatus,
                approveRequestTimeStamp,
                epochTimeStamp
        );

        // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
        // Collection name is "AuthUserItem". U can create a new collection this way
        firestore
                .collection(HelperConstants.COLL_AUTH_FOLK_APPROVE_MEMBERS)
                .add(authUserApprovalItem)
                .addOnSuccessListener(documentReference1 -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference1.getId());
                    if (null != getActivity()) {
                        Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                        getActivity().runOnUiThread(() -> loadingBar.dismiss());
                        finishAndGoForApproval();
                        btnCreateAccount.setEnabled(false);
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
            String email,
            String folkGuideAbbr,
            String firstName,
            String profileImage,
            String signUpStatus,
            String redFlagStatus,
            String approveRequestTimeStamp,
            String epochTimeStamp) {

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
                email,
                folkGuideAbbr,
                firstName,
                profileImage,
                signUpStatus,
                redFlagStatus,
                approveRequestTimeStamp,
                epochTimeStamp
        );

        // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
        // Collection name is "AuthUserItem". U can create a new collection this way
        firestore
                .collection(HelperConstants.COLL_AUTH_FOLK_APPROVE_FOLK_GUIDES)
                .add(authUserApprovalItem)
                .addOnSuccessListener(documentReference1 -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference1.getId());
                    if (null != getActivity()) {
                        Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                        getActivity().runOnUiThread(() -> loadingBar.dismiss());
                        finishAndGoForApproval();
                        btnCreateAccount.setEnabled(false);
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
            String email,
            String folkGuideAbbr,
            String firstName,
            String profileImage,
            String signUpStatus,
            String redFlagStatus,
            String approveRequestTimeStamp,
            String epochTimeStamp) {

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
                email,
                folkGuideAbbr,
                firstName,
                profileImage,
                signUpStatus,
                redFlagStatus,
                approveRequestTimeStamp,
                epochTimeStamp
        );

        // Save AuthUserItem obj to Firestore - Add a new document with a generated ID
        // Collection name is "AuthUserItem". U can create a new collection this way
        firestore
                .collection(HelperConstants.COLL_AUTH_FOLK_APPROVE_TEAM_LEADS)
                .add(authUserApprovalItem)
                .addOnSuccessListener(documentReference1 -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference1.getId());
                    if (null != getActivity()) {
                        Toast.makeText(getActivity(), "AuthUserItem Created", Toast.LENGTH_SHORT).show();
                        getActivity().runOnUiThread(() -> loadingBar.dismiss());
                        finishAndGoForApproval();
                        btnCreateAccount.setEnabled(false);
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
            ApiEndPoints apiService = RetrofitClientInstance.getInstance().create(ApiEndPoints.class);
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
                                            new HelperGeneral().dialogActionMessage(getActivity(), null, jsonObject.getString("message"), "OK", "", null, null, true);
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
//                getActivity().runOnUiThread(() -> {
//                    loadingBar = new ProgressDialog(getActivity());
//                    loadingBar.show();
//                    loadingBar.setMessage("Please wait...");
//                    loadingBar.setCanceledOnTouchOutside(false);
//                    loadingBar.setCancelable(false);
//                    loadingBar.show();
//                });
            ApiEndPoints apiService = RetrofitClientInstance.getInstance().create(ApiEndPoints.class);
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
//                                                HelperGeneral.dialogShowMessage(getActivity(), jsonObject.getString("message"));
                                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        }

                                        if (jsonObject.getString("status").equals("Failure")) {
//                                                HelperGeneral.dialogShowMessage(getActivity(), jsonObject.getString("message"));
                                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            etShortName.setText("");
                                            etShortName.setError("Short Name not available. Try a different one!");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
//                            getActivity().runOnUiThread(() -> loadingBar.dismiss());
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
//                                getActivity().runOnUiThread(() -> loadingBar.dismiss());
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
            ApiEndPoints apiService = RetrofitClientInstance.getInstance().create(ApiEndPoints.class);
            if (!("").equals(valueOf(etSignUpZone.getText()).trim())) {
                Call<String> call = apiService.getTeamLeadsBasedOnZone(
                        "https://us-central1-folk-database.cloudfunctions.net/populateTLsByZone?zone=ISKCONBangalore",
                        valueOf(etSignUpZone.getText()).trim().replaceAll("\\s+", "")
                );
//                Call<String> call = apiService.getTeamLeadsBasedOnZone(valueOf(etSignUpZone.getText()).trim());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(() -> {
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
                                                new HelperGeneral().dialogActionMessage(getActivity(), null, jsonObject.getString("message"), "OK", "", null, null, true);
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


    private Void showImagePickerOptions() {
        FilePickerBuilder.getInstance()
                .setSelectedFiles(imageFilePathsStringArray)
                .setActivityTheme(R.style.LibAppTheme)
                .setMaxCount(1)
                .pickPhoto(this);
        return null;
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


    @Override
    public void onPause() {
        super.onPause();
//        dialog.dismiss();
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
            lastFourDigits = "";
            int fileSize = 0;
            String imageName = null;

            // Check if img total length is > 4 including extension n dot
            if (newImagePath.length() > 4) {
                lastFourDigits = newImagePath.substring(newImagePath.length() - 3);
            }

            // Get Image Name
            int nameSlice = newImagePath.lastIndexOf('/');
            if (nameSlice != -1) {
                imageName = newImagePath.substring(nameSlice + 1);
            }

            // Get file size from file path
            if (newImagePath != null) {
                file = new File(newImagePath);
                fileSize = (int) file.length() / (1024 * 1024);
            } else {
                if (null != getActivity()) {
                    Toast.makeText(getActivity(), "File Path is Empty", Toast.LENGTH_SHORT).show();
                }
            }

            if (fileSize <= 2.0) {
                Uri imgUri = Uri.fromFile(new File(newImagePath));
                Bitmap bitmap = null;

                // Compress Image
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    try {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().getContentResolver(), imgUri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imgUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Uri bitmapUri = helperObject.getTempBitmapImageUri(bitmap);

                // Set image data to model
                ContactItem contactItem = new ContactItem();
                contactItem.setIvProfileImage(bitmapUri);
                contactItem.setImageName(imageName);
                contactItem.setImageExtension(lastFourDigits);

                imageUriArray.add(contactItem);
                imageExtensionStringArray.add(lastFourDigits);
                imageNameStringArray.add(imageName);

                ivSetProfileImage.setImageURI(bitmapUri);

                ivOpenGallery.setVisibility(View.GONE);
                tvOpenGallery.setText("Change Profile Picture");
                imageFilePathsStringArray.clear();

                dialog.dismiss();
            } else {
                if (null != getActivity()) {
                    dialog.dismiss();
                    helperObject.showSnack(conLaySignup, "Max file size is 2 MB only!", getResources().getColor(R.color.colorWhite), "OK", null);
                }
            }

            Log.d(TAG, "onActivityResult: Image name: " + newImagePath);
            Log.d(TAG, "onActivityResult: Last 4 digits: " + lastFourDigits);
            Log.d(TAG, "onActivityResult: img path nnn: " + newImagePath);
            Log.d(TAG, "onActivityResult: Image Name: " + imageName);
            Log.d(TAG, "onActivityResult: file info: " + file);
            Log.d(TAG, "onActivityResult: file length: " + (int) file.length());
            Log.d(TAG, "onActivityResult: file size: " + (int) file.length() / (1024 * 1024) + " mb");
            Log.d(TAG, "onActivityResult: new img path 1: " + newImagePath);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}