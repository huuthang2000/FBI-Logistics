package com.example.demoapp.services;

import com.example.demoapp.model.Account;
import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface AccountService {
    AccountService apiAccountService = new Retrofit.Builder().
            baseUrl(Constants.URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(AccountService.class);
    @GET("GetDataAccount.php")
    Call<List<Account>> getAccount();
}
