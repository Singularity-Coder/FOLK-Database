package com.singularitycoder.folkdatabase.auth.view;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.model.AuthUserItem;
import com.singularitycoder.folkdatabase.auth.viewmodel.AuthViewModel;
import com.singularitycoder.folkdatabase.helper.CustomEditText;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.helper.HelperSharedPreference;
import com.singularitycoder.folkdatabase.helper.RequestStateMediator;
import com.singularitycoder.folkdatabase.helper.Status;
import com.singularitycoder.folkdatabase.home.view.HomeActivity;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.singularitycoder.folkdatabase.auth.view.MainActivity.authTabLayout;
import static com.singularitycoder.folkdatabase.helper.FolkDatabaseApp.hasInternet;
import static java.lang.String.valueOf;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    @Nullable
    @BindView(R.id.et_login_email)
    CustomEditText etEmail;
    @Nullable
    @BindView(R.id.et_login_password)
    CustomEditText etPassword;
    @Nullable
    @BindView(R.id.btn_login)
    Button btnLogin;
    @Nullable
    @BindView(R.id.tv_login_forgot_password)
    TextView tvForgotPassword;
    @Nullable
    @BindView(R.id.tv_login_create_account)
    TextView tvNotMember;
    @Nullable
    @BindView(R.id.tv_show_password)
    TextView tvShowHidePassword;
    @Nullable
    @BindView(R.id.con_lay_login)
    ConstraintLayout conLayRootLogin;

    private final HelperGeneral helperObject = new HelperGeneral();

    private AuthViewModel authViewModel;
    private Unbinder unbinder;
    private ProgressDialog loadingBar;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private HelperSharedPreference helperSharedPreference;
    private AuthUserItem authUserItem;
    private String strSignUpStatus;

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
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        helperSharedPreference = HelperSharedPreference.getInstance(getActivity());
        if ((null) != getActivity()) loadingBar = new ProgressDialog(getActivity());
    }


    private void clickListeners() {
        tvForgotPassword.setOnClickListener(view12 -> {
            if ((null) != getActivity()) {
//                dialogForgotPassword(Objects.requireNonNull(getActivity()));
                showResetPasswordDialog();
            }
        });
        btnLogin.setOnClickListener(view1 -> logIn());
        tvNotMember.setOnClickListener(view13 -> authTabLayout.getTabAt(0).select());
    }

    @UiThread
    private void showResetPasswordDialog() {
        Bundle bundle = new Bundle();
        bundle.putString("DIALOG_TYPE", "custom");

        DialogFragment dialogFragment = new ForgotPasswordDialogFragment();
        dialogFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment previousFragment = getActivity().getSupportFragmentManager().findFragmentByTag("TAG_ForgotPasswordDialogFragment");
        if (previousFragment != null) {
            fragmentTransaction.remove(previousFragment);
        }
        fragmentTransaction.addToBackStack(null);
        dialogFragment.show(fragmentTransaction, "TAG_ForgotPasswordDialogFragment");
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
        getActivity().runOnUiThread(() -> {
            loadingBar.setMessage("Checking SignUp Status...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        });

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            authUserItem = docSnap.toObject(AuthUserItem.class);

                            if (authUserItem != null) {
                                if (!("").equals(valueOf(docSnap.getString("signUpStatus")))) {
                                    authUserItem.setSignUpStatus(valueOf(docSnap.getString("signUpStatus")));
//                                    strSignUpStatus = valueOf(docSnap.getString("signUpStatus"));

                                    if (docSnap.getString("signUpStatus").equals("true")) {
                                        strSignUpStatus = "true";
                                        Log.d(TAG, "readSignUpStatus: signupstatus: " + strSignUpStatus);
                                        getActivity().runOnUiThread(() -> loadingBar.dismiss());
                                        helperSharedPreference.setSignupStatus("true");
                                        finishAndGoHome();
                                    } else {
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
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                tvShowHidePassword.setText("HIDE");
            } else {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                tvShowHidePassword.setText("SHOW");
            }
        });
    }

    private boolean hasValidInput(CustomEditText etEmail, CustomEditText etPassword) {
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


    private void loginUser(String email, String password) {
        if ((null) != getActivity()) {
            loadingBar.show();
            loadingBar.setMessage("Checking credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                        loadingBar.dismiss();
                        if (task.isSuccessful()) {
                            if (null != getActivity()) {
                                // Main Shared Pref
                                HelperSharedPreference helperSharedPreference = HelperSharedPreference.getInstance(getActivity());
                                helperSharedPreference.setEmail(email);
                                AsyncTask.execute(() -> readSignUpStatus(email));
                            }
                        } else {
                            if (null != getActivity()) {
                                Toast.makeText(getActivity(), "Failed to login. Please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


    private void finishAndGoHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("authType", "LogIn");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }


    private void finishAndGoForApproval() {
        Intent intent = new Intent(getActivity(), AuthApprovalStatusActivity.class);
        intent.putExtra("authType", "LogIn");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }


//    private void dialogForgotPassword(Activity activity) {
//        final Dialog dialog = new Dialog(activity);
//        helperObject.dialogCustomBuild(activity, R.layout.dialog_fragment_forgot_password, dialog, false);
//
//        ImageView imgClose = dialog.findViewById(R.id.img_close);
//        CustomEditText etResetEmail = dialog.findViewById(R.id.et_reset_email);
//        Button btnReset = dialog.findViewById(R.id.btn_reset_password);
//
//        imgClose.setOnClickListener(view -> dialog.dismiss());
//
//        btnReset.setOnClickListener(view -> {
//            Log.d(TAG, "dialogForgotPassword: email: " + valueOf(etResetEmail.getText()).trim());
//            if (valueOf(etResetEmail.getText()).trim().equals("")) {
//                etResetEmail.setError("Email cannot be empty!");
//                etResetEmail.requestFocus();
//            } else if (!helperObject.hasValidEmail(valueOf(etResetEmail.getText()).trim())) {
//                etResetEmail.setError("Invalid Email!");
//                etResetEmail.requestFocus();
//            } else {
//                authViewModel.resetPasswordFromRepository(valueOf(etResetEmail.getText()).trim()).observe(getViewLifecycleOwner(), liveDataObserver(valueOf(etResetEmail.getText()).trim()));
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }

    private Observer liveDataObserver(String email) {
        Observer<RequestStateMediator> observer = null;
        if (hasInternet()) {
            observer = requestStateMediator -> {

                if (Status.LOADING == requestStateMediator.getStatus()) {
                    if (null != getActivity()) {
                        getActivity().runOnUiThread(() -> {
                            loadingBar.setMessage(valueOf(requestStateMediator.getMessage()));
                            loadingBar.setCanceledOnTouchOutside(false);
                            if (null != loadingBar && !loadingBar.isShowing()) loadingBar.show();
                        });
                    }
                }

                if (Status.SUCCESS == requestStateMediator.getStatus()) {
                    if (null != getActivity()) {
                        getActivity().runOnUiThread(() -> {
                            if (("RESET PASSWORD").equals(requestStateMediator.getKey())) {
                                authUserItem = (AuthUserItem) requestStateMediator.getData();
                            }

                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();

                            Toast.makeText(getContext(), valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                if (Status.EMPTY == requestStateMediator.getStatus()) {
                    if (null != getActivity()) {
                        getActivity().runOnUiThread(() -> {
                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                            Toast.makeText(getContext(), valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                if (Status.ERROR == requestStateMediator.getStatus()) {
                    if (null != getActivity()) {
                        getActivity().runOnUiThread(() -> {
                            if (("RESET PASSWORD").equals(requestStateMediator.getKey())) {
//                                helperObject.showSnackBar(conLayRootLogin, "Failed to send reset email!", getResources().getColor(R.color.colorWhite), "RELOAD", view -> getTopHalfAuthUserProfile(email));
                            }

                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();

                            Toast.makeText(getActivity(), valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            };
        }
        return observer;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
