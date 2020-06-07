package com.singularitycoder.folkdatabase.auth.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.singularitycoder.folkdatabase.auth.repository.AuthRepository;
import com.singularitycoder.folkdatabase.helper.RequestStateMediator;

import io.reactivex.disposables.CompositeDisposable;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    private MutableLiveData<RequestStateMediator> mutableLiveData;
    private AuthRepository authRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<RequestStateMediator> resetPasswordFromRepository(String email) throws IllegalArgumentException {
        authRepository = AuthRepository.getInstance();
        mutableLiveData = authRepository.resetPassword(email);
        return mutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
