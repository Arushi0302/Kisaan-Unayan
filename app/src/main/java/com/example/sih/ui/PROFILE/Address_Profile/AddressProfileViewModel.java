package com.example.sih.ui.PROFILE.Address_Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddressProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddressProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Profile fragment 2");
    }

    public LiveData<String> getText() {
        return mText;
    }
}