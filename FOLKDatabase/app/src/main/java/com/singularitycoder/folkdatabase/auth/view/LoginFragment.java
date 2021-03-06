package com.singularitycoder.folkdatabase.auth.view;

import android.app.ProgressDialog;
import android.content.Intent;
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
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.model.AuthUserItem;
import com.singularitycoder.folkdatabase.auth.viewmodel.AuthViewModel;
import com.singularitycoder.folkdatabase.helper.CustomEditText;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.helper.HelperSharedPreference;
import com.singularitycoder.folkdatabase.helper.RequestStateMediator;
import com.singularitycoder.folkdatabase.helper.UiState;
import com.singularitycoder.folkdatabase.home.view.HomeActivity;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.singularitycoder.folkdatabase.auth.view.MainActivity.authTabLayout;
import static com.singularitycoder.folkdatabase.BaseApplication.hasInternet;
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
        init(view);
        clickListeners();
        showHidePassword();
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        if (null != getActivity())
            helperSharedPreference = HelperSharedPreference.getInstance(getActivity());
        if (null != getActivity()) loadingBar = new ProgressDialog(getActivity());
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
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

                authViewModel.loginUserFromRepository(email, password).observe(getViewLifecycleOwner(), liveDataObserver(email));
            }
        } else {
            if ((null) != getActivity()) {
                Toast.makeText(Objects.requireNonNull(getActivity()), "No Internet!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Observer liveDataObserver(String email) {
        Observer<RequestStateMediator> observer = null;
        if (hasInternet()) {
            observer = requestStateMediator -> {

                if (UiState.LOADING == requestStateMediator.getStatus()) {
                    if (null != getActivity()) {
                        getActivity().runOnUiThread(() -> {
                            loadingBar.setMessage(valueOf(requestStateMediator.getMessage()));
                            loadingBar.setCanceledOnTouchOutside(false);
                            if (null != loadingBar && !loadingBar.isShowing())
                                loadingBar.show();
                        });
                    }
                }

                if (UiState.SUCCESS == requestStateMediator.getStatus()) {

                    if (("RESET PASSWORD").equals(requestStateMediator.getKey())) {
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(() -> {
                                authUserItem = (AuthUserItem) requestStateMediator.getData();
                                if (null != loadingBar && loadingBar.isShowing())
                                    loadingBar.dismiss();
                                Toast.makeText(getContext(), valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }

                    if (("LOGIN USER").equals(requestStateMediator.getKey())) {
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(() -> {
                                // Main Shared Pref
                                HelperSharedPreference helperSharedPreference = HelperSharedPreference.getInstance(getActivity());
                                helperSharedPreference.setEmail(email);
                                authViewModel.getSignUpStatusFromRepository(email).observe(getViewLifecycleOwner(), liveDataObserver(email));
                            });
                        }
                    }

                    if (("SIGNUP STATUS").equals(requestStateMediator.getKey())) {
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(() -> {
                                QuerySnapshot queryDocumentSnapshots = (QuerySnapshot) requestStateMediator.getData();
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                                    Log.d(TAG, "docList: " + docList);
                                    for (DocumentSnapshot docSnap : docList) {
                                        AuthUserItem authUserItem = docSnap.toObject(AuthUserItem.class);

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
                                    Toast.makeText(getContext(), "Got Data", Toast.LENGTH_SHORT).show();
                                    if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                                }
                            });
                        }
                    }
                }

                if (UiState.EMPTY == requestStateMediator.getStatus()) {
                    if (null != getActivity()) {
                        getActivity().runOnUiThread(() -> {
                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                            Toast.makeText(getContext(), valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                if (UiState.ERROR == requestStateMediator.getStatus()) {
                    if (null != getActivity()) {
                        getActivity().runOnUiThread(() -> {
                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                            Toast.makeText(getActivity(), valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();
//                                helperObject.showSnackBar(conLayRootLogin, "Failed to send reset email!", getResources().getColor(R.color.colorWhite), "RELOAD", view -> getTopHalfAuthUserProfile(email));
                        });
                    }
                }
            };
        }
        return observer;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
