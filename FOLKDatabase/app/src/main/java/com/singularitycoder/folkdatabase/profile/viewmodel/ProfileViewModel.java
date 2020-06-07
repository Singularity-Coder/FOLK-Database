package com.singularitycoder.folkdatabase.profile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.singularitycoder.folkdatabase.helper.RequestStateMediator;
import com.singularitycoder.folkdatabase.profile.repository.ProfileRepository;

import io.reactivex.disposables.CompositeDisposable;

public class ProfileViewModel extends ViewModel {

    private static final String TAG = "ProfileViewModel";

    private MutableLiveData<RequestStateMediator> mutableLiveData;
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

    public LiveData<RequestStateMediator> getAuthUserInfoFromRepository(String emailKey) throws IllegalArgumentException {
        profileRepository = ProfileRepository.getInstance();
        mutableLiveData = profileRepository.getAuthUserData(emailKey);
        return mutableLiveData;
    }

    public LiveData<RequestStateMediator> getFolkGuideInfoFromRepository(String emailKey) throws IllegalArgumentException {
        profileRepository = ProfileRepository.getInstance();
        mutableLiveData = profileRepository.getFolkGuideData(emailKey);
        return mutableLiveData;
    }

    public LiveData<RequestStateMediator> getTeamLeadInfoFromRepository(String emailKey) throws IllegalArgumentException {
        profileRepository = ProfileRepository.getInstance();
        mutableLiveData = profileRepository.getTeamLeadData(emailKey);
        return mutableLiveData;
    }

    public LiveData<RequestStateMediator> getContactInfoFromRepository(String emailKey) throws IllegalArgumentException {
        profileRepository = ProfileRepository.getInstance();
        mutableLiveData = profileRepository.getContactData(emailKey);
        return mutableLiveData;
    }

    public LiveData<RequestStateMediator> getAllUsersInfoFromRepository(String emailKey) throws IllegalArgumentException {
        profileRepository = ProfileRepository.getInstance();
        mutableLiveData = profileRepository.getAllUsersData(emailKey);
        return mutableLiveData;
    }

    public LiveData<RequestStateMediator> getBasicInfoFromRepository(String emailKey) throws IllegalArgumentException {
        profileRepository = ProfileRepository.getInstance();
        mutableLiveData = profileRepository.getBasicInfo(emailKey);
        return mutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
