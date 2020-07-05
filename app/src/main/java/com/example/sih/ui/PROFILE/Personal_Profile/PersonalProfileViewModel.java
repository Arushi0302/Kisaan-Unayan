package com.example.sih.ui.PROFILE.Personal_Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonalProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PersonalProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}