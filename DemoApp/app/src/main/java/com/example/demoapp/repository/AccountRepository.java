package com.example.demoapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.Account;
import com.example.demoapp.services.AccountService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository {
    private MutableLiveData<List<Account>> mAccountList;
    private AccountService mAccountService;

    public AccountRepository(String baseURL) {
        mAccountService = APIClient.getClient(baseURL).create(AccountService.class);
    }

    public void upLoadAllAccout() {
        Call<List<Account>> call = mAccountService.getAccount();
        call.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.body() != null) {
                    mAccountList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                mAccountList.postValue(null);
            }
        });
    }

    public LiveData<List<Account>> getAllAccout() {
        if (mAccountList == null) {
            mAccountList = new MutableLiveData<>();
        }
        upLoadAllAccout();
        return mAccountList;
    }
}
