package com.example.demoapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommunicateViewModel extends ViewModel {
    private MutableLiveData<Boolean> _needReloading = new MutableLiveData<>();
    public LiveData<Boolean> needReloading = _needReloading;

    public CommunicateViewModel() {
        this._needReloading.postValue(false);
    }

    public LiveData<Boolean> needReloading() {
        return needReloading;
    }

    public void openDialog() {
        _needReloading.postValue(false);
    }

    public void makeChanges() {
        _needReloading.postValue(true);
    }
}
