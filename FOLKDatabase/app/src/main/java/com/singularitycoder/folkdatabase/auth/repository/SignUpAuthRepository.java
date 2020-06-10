package com.singularitycoder.folkdatabase.auth.repository;

public class SignUpAuthRepository {

    private static final String TAG = "SignUpAuthRepository";

    private static SignUpAuthRepository _instance;

    public SignUpAuthRepository() {
        // Initialize Firebase if necessary
    }

    public static SignUpAuthRepository getInstance() {
        if (_instance == null) {
            _instance = new SignUpAuthRepository();
        }
        return _instance;
    }

}
