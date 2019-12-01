package com.singularitycoder.folkdatabase.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.AuthUserItem;
import com.singularitycoder.folkdatabase.auth.MainActivity;
import com.singularitycoder.folkdatabase.helper.HelperSharedPreference;
import com.singularitycoder.folkdatabase.home.HomeActivity;

import java.util.Objects;

public class AuthApprovalStatusActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private Button btnCallAuthority;
    private Button btnCheckStatus;
    private TextView tvFolkGuideGreetingText;
    private AuthUserItem authUserItem;
    private HelperSharedPreference helperSharedPreference;

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
        clickListeners();
        getStatus();
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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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
        tvFolkGuideGreetingText.setText("Hello " + sp.getString("firstName", "") + " " + sp.getString("lastName", "") + ",");
    }


    private void clickListeners() {
        btnCallAuthority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // query that searches team leads number
            }
        });

        btnCheckStatus.setOnClickListener(view -> {
            // load progress
            // check db for yes
            // if yes then jump to home else toast
            startActivity(new Intent(this, HomeActivity.class));
        });
    }


    private boolean getStatus() {
//        firestore.collection("AllFolkGuides").document(authUserItem.getDocId()).get()
//                .addOnSuccessListener((OnSuccessListener<Void>) aVoid -> {
//                    Toast.makeText(AuthApprovalStatusActivity.this, "Product Updated", Toast.LENGTH_LONG).show();
//                });

        helperSharedPreference.setSignupStatus("true");
        return false;
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
