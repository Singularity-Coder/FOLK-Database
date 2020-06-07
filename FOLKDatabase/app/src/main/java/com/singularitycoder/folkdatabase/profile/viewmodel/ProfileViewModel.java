package com.singularitycoder.folkdatabase.profile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.singularitycoder.folkdatabase.helper.AllCallbacks;
import com.singularitycoder.folkdatabase.profile.repository.ProfileRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {

    private static final String TAG = "ProfileViewModel";

    private MutableLiveData<AllCallbacks> mutableLiveData;
    private ProfileRepository profileRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

//    public LiveData<AllCallbacks> getBasicInfoFromRepositoryRx(String emailKey) throws IllegalArgumentException {
//        profileRepository = ProfileRepository.getInstance();
//        compositeDisposable.add(
//                profileRepository.getBasicInfoObservable(emailKey)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn()
//        );
//        return mutableLiveData;
//    }

    public LiveData<AllCallbacks> getBasicInfoFromRepository(String emailKey) throws IllegalArgumentException {
        profileRepository = ProfileRepository.getInstance();
        mutableLiveData = profileRepository.getBasicInfo(emailKey);
        return mutableLiveData;
    }

    public LiveData<AllCallbacks> getAllUsersFromRepository(String emailKey) throws IllegalArgumentException {
        profileRepository = ProfileRepository.getInstance();
        mutableLiveData = profileRepository.getAllUsersData(emailKey);
        return mutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
