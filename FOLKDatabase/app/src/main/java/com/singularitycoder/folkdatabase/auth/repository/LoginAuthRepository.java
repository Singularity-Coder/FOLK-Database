package com.singularitycoder.folkdatabase.auth.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.helper.RequestStateMediator;
import com.singularitycoder.folkdatabase.helper.UiState;

public class LoginAuthRepository {

    private static final String TAG = "AuthRepository";

    private static LoginAuthRepository _instance;

    public LoginAuthRepository() {
        // Initialize Firebase if necessary
    }

    public static LoginAuthRepository getInstance() {
        if (_instance == null) {
            _instance = new LoginAuthRepository();
        }
        return _instance;
    }

    public MutableLiveData<RequestStateMediator> loginUser(String email, String password) {

        final MutableLiveData<RequestStateMediator> loginUserLiveData = new MutableLiveData<>();
        final RequestStateMediator requestStateMediator = new RequestStateMediator();

        requestStateMediator.set(null, UiState.LOADING, "Checking credentials...", null);
        loginUserLiveData.postValue(requestStateMediator);

        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        requestStateMediator.set(null, UiState.SUCCESS, "Logged In Successfully!", "LOGIN USER");
                        loginUserLiveData.postValue(requestStateMediator);
                    }
                })
                .addOnFailureListener(e -> {
                    requestStateMediator.set(null, UiState.ERROR, "Failed to login. Please try again", null);
                    loginUserLiveData.postValue(requestStateMediator);
                });
        return loginUserLiveData;
    }

    public MutableLiveData<RequestStateMediator> readSignUpStatus(String email) {

        final MutableLiveData<RequestStateMediator> signUpStatusLiveData = new MutableLiveData<>();
        final RequestStateMediator requestStateMediator = new RequestStateMediator();

        requestStateMediator.set(null, UiState.LOADING, "Checking SignUp Status...", null);
        signUpStatusLiveData.postValue(requestStateMediator);

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    requestStateMediator.set(queryDocumentSnapshots, UiState.SUCCESS, "Got Data!", "SIGNUP STATUS");
                    signUpStatusLiveData.postValue(requestStateMediator);
                })
                .addOnFailureListener(e -> {
                    requestStateMediator.set(null, UiState.ERROR, "Couldn't get data!", null);
                    signUpStatusLiveData.postValue(requestStateMediator);
                });
        return signUpStatusLiveData;
    }

    public MutableLiveData<RequestStateMediator> resetPassword(String email) {
        final MutableLiveData<RequestStateMediator> resetPasswordLiveData = new MutableLiveData<>();
        final RequestStateMediator requestStateMediator = new RequestStateMediator();

        requestStateMediator.set(null, UiState.LOADING, "Please wait...", null);
        resetPasswordLiveData.postValue(requestStateMediator);

        FirebaseAuth
                .getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        requestStateMediator.set(null, UiState.SUCCESS, "We have sent you instructions to reset your password!", "RESET PASSWORD");
                        resetPasswordLiveData.postValue(requestStateMediator);
                    }
                })
                .addOnFailureListener(e -> {
                    requestStateMediator.set(null, UiState.ERROR, e.getMessage(), null);
                    resetPasswordLiveData.postValue(requestStateMediator);
                });
        return resetPasswordLiveData;
    }
}
