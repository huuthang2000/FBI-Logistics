package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demoapp.model.Account;
import com.example.demoapp.repository.AccountRepository;
import com.example.demoapp.utilities.Constants;

import java.util.List;

public class AccoutViewModel extends AndroidViewModel {
    private LiveData<List<Account>> mAccountList;
    private AccountRepository mAccountRepository;

    public AccoutViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        mAccountRepository = new AccountRepository(Constants.URL_API);
    }

    public void loadAllAir() {
        mAccountList = mAccountRepository.getAllAccout();
    }

    public LiveData<List<Account>> getAccountList() {
        loadAllAir();
        return mAccountList;
    }

}


