package com.singularitycoder.folkdatabase.auth;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.helper.HelperCustomEditText;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.helper.HelperSharedPreference;
import com.singularitycoder.folkdatabase.home.HomeActivity;

import java.util.List;
import java.util.Objects;

import static com.singularitycoder.folkdatabase.auth.MainActivity.authTabLayout;
import static com.singularitycoder.folkdatabase.helper.FolkDatabaseApp.hasInternet;
import static java.lang.String.valueOf;

public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private HelperCustomEditText etEmail;
    private HelperCustomEditText etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    private TextView tvNotMember;
    private TextView tvShowHidePassword;
    private ProgressDialog loadingBar;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private HelperSharedPreference helperSharedPreference;
    private AuthUserItem authUserItem;
    private String strSignUpStatus;
    private HelperGeneral helperObject = new HelperGeneral();


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
        helperSharedPreference = HelperSharedPreference.getInstance(getActivity());
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
        btnLogin.setOnClickListener(view1 -> logIn());
        tvNotMember.setOnClickListener(view13 -> authTabLayout.getTabAt(0).select());
    }


    private void logIn() {
        if (hasInternet()) {
            if (hasValidInput(etEmail, etPassword)) {
                loadingBar.setMessage("Please wait...");
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
    }


    private String readSignUpStatus(String email) {
        // TODO: 2019-12-31 - this also needs checking
        SharedPreferences sp = getActivity().getSharedPreferences("authItem", Context.MODE_PRIVATE);
//        String email = sp.getString("email", "");
        getActivity().runOnUiThread(() -> {
            loadingBar.setMessage("Checking SignUp Status...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        });

        Log.d(TAG, "readSignUpStatus: hit 111");

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    Log.d(TAG, "readSignUpStatus: hit 222");

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            authUserItem = docSnap.toObject(AuthUserItem.class);
                            if (authUserItem != null) {
                                Log.d(TAG, "AuthItem: " + authUserItem);

                                if (!("").equals(valueOf(docSnap.getString("signUpStatus")))) {
                                    authUserItem.setSignUpStatus(valueOf(docSnap.getString("signUpStatus")));
//                                    strSignUpStatus = valueOf(docSnap.getString("signUpStatus"));

                                    if (docSnap.getString("signUpStatus").equals("true")) {
                                        Log.d(TAG, "readSignUpStatus: hit 444");
                                        strSignUpStatus = "true";
                                        Log.d(TAG, "readSignUpStatus: signupstatus: " + strSignUpStatus);
                                        getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                        helperSharedPreference.setSignupStatus("true");
                                        finishAndGoHome();
                                    } else {
                                        Log.d(TAG, "readSignUpStatus: hit 555");
                                        strSignUpStatus = "false";
                                        helperSharedPreference.setSignupStatus("false");
                                        finishAndGoForApproval();
                                    }
                                }

                                authUserItem.setDocId(docSnap.getId());
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
                        getActivity().runOnUiThread(() -> loadingBar.dismiss());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "readSignUpStatus: hit 333");
                    Toast.makeText(getActivity(), "Couldn't get data!", Toast.LENGTH_SHORT).show();
                    getActivity().runOnUiThread(() -> loadingBar.dismiss());
                });
        return strSignUpStatus;
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

        if (!helperObject.hasValidEmail(email)) {
            etEmail.setError("Invalid Email!");
            etEmail.requestFocus();
            return false;
        }

        if (password.equals("")) {
            etPassword.setError("Password is Required!");
            etPassword.requestFocus();
            return false;
        }

        if (!helperObject.hasValidPassword(password)) {
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
            loadingBar.setMessage("Checking credentials...");
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
                                    // Main Shared Pref
                                    HelperSharedPreference helperSharedPreference = HelperSharedPreference.getInstance(getActivity());
                                    helperSharedPreference.setEmail(email);

                                    // Test Shared Pref
                                    SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("authItem", Context.MODE_PRIVATE);
                                    sp.edit().putString("email", email).apply();
                                }

                                AsyncTask.execute(() -> readSignUpStatus(email));
                            }
                        } else {
                            if ((null) != getActivity()) {
                                Toast.makeText(getActivity(), "Failed to login. Please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


    private void finishAndGoHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("authType", "LogIn");
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }


    private void finishAndGoForApproval() {
        Intent intent = new Intent(getActivity(), AuthApprovalStatusActivity.class);
        intent.putExtra("authType", "LogIn");
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }


    private void dialogForgotPassword(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        helperObject.dialogCustomBuild(activity, R.layout.dialog_forgot_password, dialog, false);

        ImageView imgClose = dialog.findViewById(R.id.img_close);
        HelperCustomEditText etResetEmail = dialog.findViewById(R.id.et_reset_email);
        Button btnReset = dialog.findViewById(R.id.btn_reset_password);

        imgClose.setOnClickListener(view -> dialog.dismiss());

        btnReset.setOnClickListener(view -> {
            Log.d(TAG, "dialogForgotPassword: email: " + valueOf(etResetEmail.getText()).trim());
            if (valueOf(etResetEmail.getText()).trim().equals("")) {
                etResetEmail.setError("Email cannot be empty!");
                etResetEmail.requestFocus();
            } else if (!helperObject.hasValidEmail(valueOf(etResetEmail.getText()).trim())) {
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
