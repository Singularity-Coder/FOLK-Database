package com.singularitycoder.folkdatabase.auth.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.singularitycoder.folkdatabase.auth.repository.LoginAuthRepository;
import com.singularitycoder.folkdatabase.helper.RequestStateMediator;

import io.reactivex.disposables.CompositeDisposable;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    private MutableLiveData<RequestStateMediator> mutableLiveData;
    private LoginAuthRepository loginAuthRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<RequestStateMediator> loginUserFromRepository(String email, String password) throws IllegalArgumentException {
        loginAuthRepository = LoginAuthRepository.getInstance();
        mutableLiveData = loginAuthRepository.loginUser(email, password);
        return mutableLiveData;
    }

    public LiveData<RequestStateMediator> getSignUpStatusFromRepository(String email) throws IllegalArgumentException {
        loginAuthRepository = LoginAuthRepository.getInstance();
        mutableLiveData = loginAuthRepository.readSignUpStatus(email);
        return mutableLiveData;
    }

    public LiveData<RequestStateMediator> resetPasswordFromRepository(String email) throws IllegalArgumentException {
        loginAuthRepository = LoginAuthRepository.getInstance();
        mutableLiveData = loginAuthRepository.resetPassword(email);
        return mutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
