package com.singularitycoder.folkdatabase.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private ProgressDialog loadingBar;
    private Button btnCallAuthority;
    private Button btnCheckStatus;
    private TextView tvFolkGuideGreetingText;
    private AuthUserItem authUserItem;
    private HelperSharedPreference helperSharedPreference;
    private String strAuthorityPhoneNumber;
    private String strSignUpStatus;

    // this listener is called when there is change in firebase fireUser session
    FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            // if key is false
            // fireUser fireAuth state is changed - fireUser is null launch login activity
            startActivity(new Intent(this, MainActivity.class));
            Objects.requireNonNull(this).finish();
        } else {
            Toast.makeText(this, "AuthUserItem: " + user.getEmail(), Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatuBarColor();
        setContentView(R.layout.activity_approval_status);
        inits();
        authCheck();
        setData();
        AsyncTask.execute(() -> readAuthUserData());
        clickListeners();
    }


    private void setStatuBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  // clear FLAG_TRANSLUCENT_STATUS flag:
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.bg_light));   // change the color
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // fireUser fireAuth state is changed - fireUser is null launch login activity
                startActivity(new Intent(this, MainActivity.class));
                Objects.requireNonNull(this).finish();
            }
        };
    }


    private void setData() {
        SharedPreferences sp = getSharedPreferences("authItem", Context.MODE_PRIVATE);
        tvFolkGuideGreetingText.setText("Hello " + sp.getString("firstName", "") + ",");
    }


    private void clickListeners() {
        btnCallAuthority.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", strAuthorityPhoneNumber, null));
            callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            startActivity(callIntent);
        });

        btnCheckStatus.setOnClickListener(view -> {
            // load progress
            // check db for yes
            // if yes then jump to home else toast
            AsyncTask.execute(() -> readSignUpStatus());
        });
    }


    // READ - query that searches team leads or direct authority phone number
    private void readAuthUserData() {
        SharedPreferences sp = getSharedPreferences("authItem", Context.MODE_PRIVATE);
        String email = sp.getString("email", "");
        String directAuthority = sp.getString("directAuthority", "");
        Log.d(TAG, "readAuthUserData: directAuthority: " + directAuthority);
        runOnUiThread(() -> {
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        });

        Log.d(TAG, "readAuthUserData: hitz 0");

        FirebaseFirestore
                .getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("shortName", directAuthority)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    Log.d(TAG, "readAuthUserData: hitz 1");

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        Log.d(TAG, "readAuthUserData: hitz 2");

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
                        Toast.makeText(AuthApprovalStatusActivity.this, "Got Data", Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> loadingBar.dismiss());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AuthApprovalStatusActivity.this, "Couldn't get data!", Toast.LENGTH_SHORT).show();
                    runOnUiThread(() -> loadingBar.dismiss());
                });
    }

    // READ - query that searches team leads or direct authority phone number
    private void readSignUpStatus() {
        SharedPreferences sp = getSharedPreferences("authItem", Context.MODE_PRIVATE);
        String email = sp.getString("email", "");
        runOnUiThread(() -> {
            loadingBar.setMessage("Please wait...");
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
                                Log.d(TAG, "AuthItem: " + authUserItem);

                                if (!("").equals(valueOf(docSnap.getString("signUpStatus")))) {
                                    authUserItem.setSignUpStatus(valueOf(docSnap.getString("signUpStatus")));
                                    strSignUpStatus = valueOf(docSnap.getString("signUpStatus"));

                                    if (docSnap.getString("signUpStatus").equals("true")) {
                                        runOnUiThread(() -> loadingBar.dismiss());
                                        helperSharedPreference.setSignupStatus("true");
                                        startActivity(new Intent(this, HomeActivity.class));
                                        finish();
                                    } else {
                                        helperSharedPreference.setSignupStatus("false");
                                        HelperGeneral.dialogShowMessage(AuthApprovalStatusActivity.this, "Your account is still not verified. Please call your Authority!");
                                    }
                                }

                                authUserItem.setDocId(docSnap.getId());
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        Toast.makeText(AuthApprovalStatusActivity.this, "Got Data", Toast.LENGTH_SHORT).show();
                        runOnUiThread(() -> loadingBar.dismiss());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AuthApprovalStatusActivity.this, "Couldn't get data!", Toast.LENGTH_SHORT).show();
                    runOnUiThread(() -> loadingBar.dismiss());
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
}
