package com.singularitycoder.folkdatabase.auth.repository;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.singularitycoder.folkdatabase.helper.RequestStateMediator;
import com.singularitycoder.folkdatabase.helper.Status;

public class AuthRepository {

    private static final String TAG = "AuthRepository";

    private static AuthRepository _instance;

    public AuthRepository() {
        // Initialize Firebase if necessary
    }

    public static AuthRepository getInstance() {
        if (_instance == null) {
            _instance = new AuthRepository();
        }
        return _instance;
    }

    public MutableLiveData<RequestStateMediator> resetPassword(String email) {
        final MutableLiveData<RequestStateMediator> resetPasswordLiveData = new MutableLiveData<>();
        final RequestStateMediator requestStateMediator = new RequestStateMediator();

        requestStateMediator.set(null, Status.LOADING, "Please wait...", null);
        resetPasswordLiveData.postValue(requestStateMediator);

        FirebaseAuth
                .getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        requestStateMediator.set(null, Status.SUCCESS, "We have sent you instructions to reset your password!", "RESET PASSWORD");
                        resetPasswordLiveData.postValue(requestStateMediator);
                    }
                })
                .addOnFailureListener(e -> {
                    requestStateMediator.set(null, Status.ERROR, e.getMessage(), null);
                    resetPasswordLiveData.postValue(requestStateMediator);
                });
        return resetPasswordLiveData;
    }
}
