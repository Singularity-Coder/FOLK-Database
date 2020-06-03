package com.singularitycoder.folkdatabase.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.helper.HelperSharedPreference;
import com.singularitycoder.folkdatabase.home.HomeActivity;

import java.util.List;
import java.util.Objects;

import static java.lang.String.valueOf;

public class AuthApprovalStatusActivity extends AppCompatActivity {

    private static final String TAG = "AuthApprovalStatusActiv";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private HelperSharedPreference helperSharedPreference;

    // Views
    private ProgressDialog loadingBar;
    private Button btnCallAuthority;
    private Button btnCheckStatus;
    private TextView tvFolkGuideGreetingText;
    private AuthUserItem authUserItem;

    // Vars
    private String strAuthorityPhoneNumber;
    private String strSignUpStatus;
    private String strAuthTypeIntent;
    private HelperGeneral helperObject = new HelperGeneral();

    // This Listener is called when there is change in Firebase user session
    FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
        if (null != firebaseAuth.getCurrentUser()) {
            HelperSharedPreference helperSharedPreference = HelperSharedPreference.getInstance(this);
            String signUpStatus = helperSharedPreference.getSignupStatus();

            if (null != signUpStatus) {
                if (("true").equals(signUpStatus)) {
                    finishAndGoHome();
                }
            }
        } else {
            startActivity(new Intent(this, MainActivity.class));
            Objects.requireNonNull(this).finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helperObject.setStatuBarColor(this, R.color.bg_light);
        setContentView(R.layout.activity_approval_status);
        inits();
        authCheck();
        getIntentData();
        setData();
        clickListeners();
    }


    private void inits() {
        helperSharedPreference = HelperSharedPreference.getInstance(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        loadingBar = new ProgressDialog(this);

        btnCallAuthority = findViewById(R.id.btn_call_authority);
        btnCheckStatus = findViewById(R.id.btn_check_status);
        tvFolkGuideGreetingText = findViewById(R.id.tv_folk_guide_greeting_text);
    }


    private void authCheck() {
        authListener = firebaseAuth -> {
            if (null != firebaseAuth.getCurrentUser()) {
                String signUpStatus = helperSharedPreference.getSignupStatus();

                if (null != signUpStatus) {
                    if (("true").equals(signUpStatus)) {
                        finishAndGoHome();
                    }
                }
            } else {
                startActivity(new Intent(this, MainActivity.class));
                Objects.requireNonNull(this).finish();
            }
        };
    }


    private void finishAndGoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("authType", "SignUp");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    private void getIntentData() {
        Intent intent = getIntent();
        strAuthTypeIntent = intent.getStringExtra("authType");
        if (!("").equals(strAuthTypeIntent)) {
            if (("SignUp").equals(strAuthTypeIntent)) {
                AsyncTask.execute(this::readAuthorityPhoneNumber);
            }
        }
    }


    private void setData() {
        if (!("").equals(strAuthTypeIntent)) {
            if (("SignUp").equals(strAuthTypeIntent)) {
                tvFolkGuideGreetingText.setText(new StringBuilder("Hello ").append(helperSharedPreference.getFullName()).append(","));
            }

            if (("LogIn").equals(strAuthTypeIntent)) {
                tvFolkGuideGreetingText.setText(new StringBuilder("Hello,"));
            }
        }
    }


    private void clickListeners() {
        btnCallAuthority.setOnClickListener(view -> callAuthority());
        btnCheckStatus.setOnClickListener(view -> {
            AsyncTask.execute(this::readSignUpStatus);    // load progress, check db for yes, if yes then jump to home else toast
        });
    }


    private void callAuthority() {
        // TODO: 2020-02-01 -  We can also get authority name from editText dialog and pass it here. Later.
        if (null != strAuthorityPhoneNumber) {
            makePhoneCall(strAuthorityPhoneNumber);
        } else {
            if (null != strAuthTypeIntent) {
                if (("SignUp").equals(strAuthTypeIntent)) {
                    try {
                        helperObject.dialogActionMessage(this, null, "Could not find your Authority's Phone Number. Call Shresta Rupa Dasa (Super Admin) for the details!", "CALL", "CANCEL", () -> makePhoneCall("9342336283"), null, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (("LogIn").equals(strAuthTypeIntent)) {
                    try {
                        helperObject.dialogActionMessage(this, null, "Call Shresta Rupa Dasa (Super Admin) for the details!", "CALL", "CANCEL", () -> makePhoneCall("9342336283"), null, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    helperObject.dialogActionMessage(this, null, "Call Shresta Rupa Dasa (Super Admin) for the details!", "CALL", "CANCEL", () -> makePhoneCall("9342336283"), null, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Void makePhoneCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(callIntent);
        return null;
    }


    // READ - query that searches team leads or direct authority phone number
    private void readAuthorityPhoneNumber() {

        Log.d(TAG, "readAuthorityPhoneNumber: dauth: " + helperSharedPreference.getDirectAuthority());
        runOnUiThread(() -> {
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            if (null != loadingBar && !loadingBar.isShowing()) loadingBar.show();
        });

        FirebaseFirestore
                .getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("shortName", helperSharedPreference.getDirectAuthority())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            authUserItem = docSnap.toObject(AuthUserItem.class);
                            if (authUserItem != null) {
                                Log.d(TAG, "AuthItem: " + authUserItem);

                                if (!("").equals(valueOf(docSnap.getString("phone")))) {
                                    authUserItem.setPhone(valueOf(docSnap.getString("phone")));
                                    strAuthorityPhoneNumber = valueOf(docSnap.getString("phone"));
                                    Log.d(TAG, "readAuthUserData: authorityPhone: " + strAuthorityPhoneNumber);
                                }

                                authUserItem.setDocId(docSnap.getId());
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        Toast.makeText(this, "Got Data", Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> {
                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Couldn't get data!", Toast.LENGTH_SHORT).show();
                    runOnUiThread(() -> {
                        if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                    });
                });
    }

    // READ - checks signup status of user
    private void readSignUpStatus() {
        runOnUiThread(() -> {
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            if (null != loadingBar && !loadingBar.isShowing()) loadingBar.show();
        });

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", helperSharedPreference.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            authUserItem = docSnap.toObject(AuthUserItem.class);
                            if (authUserItem != null) {
                                Log.d(TAG, "AuthItem: " + authUserItem);

                                if (!("").equals(valueOf(docSnap.getString("signUpStatus")))) {
                                    authUserItem.setSignUpStatus(valueOf(docSnap.getString("signUpStatus")));
                                    strSignUpStatus = valueOf(docSnap.getString("signUpStatus"));

                                    if (("true").equals(docSnap.getString("signUpStatus"))) {
                                        runOnUiThread(() -> {
                                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                                        });
                                        helperSharedPreference.setSignupStatus("true");
                                        startActivity(new Intent(this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    } else {
                                        helperSharedPreference.setSignupStatus("false");
                                        helperObject.dialogActionMessage(AuthApprovalStatusActivity.this, null, "Your account is still not verified. Please call your Authority!", "OK", "", null, null, false);
                                    }
                                }

                                authUserItem.setDocId(docSnap.getId());
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        Toast.makeText(AuthApprovalStatusActivity.this, "Got Data", Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> {
                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AuthApprovalStatusActivity.this, "Couldn't get data!", Toast.LENGTH_SHORT).show();
                    runOnUiThread(() -> {
                        if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                    });
                });
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
    protected void onDestroy() {
        super.onDestroy();
        if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
    }
}
