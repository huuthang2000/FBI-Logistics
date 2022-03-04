package com.example.demoapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.RetailGoods;
import com.example.demoapp.services.RetailGoodsService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetailGoodsRepository {
    private MutableLiveData<List<RetailGoods>> mListRetailGoods;
    private RetailGoodsService mRetailGoodsService;

    public RetailGoodsRepository(String baseURL){
        mRetailGoodsService = APIClient.getClient(baseURL).create(RetailGoodsService.class);
    }

    public void upLoadAllRetailGoods() {
        Call<List<RetailGoods>> call = mRetailGoodsService.getPriceRetailGoods();
        call.enqueue(new Callback<List<RetailGoods>>() {
            @Override
            public void onResponse(Call<List<RetailGoods>> call, Response<List<RetailGoods>> response) {
                if (response.body() != null) {
                    mListRetailGoods.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<RetailGoods>> call, Throwable t) {
                mListRetailGoods.postValue(null);
            }
        });
    }


    public LiveData<List<RetailGoods>> getAllRetailGoods() {
        if (mListRetailGoods == null) {
            mListRetailGoods = new MutableLiveData<>();
        }
        upLoadAllRetailGoods();
        return mListRetailGoods;
    }

    public Call<RetailGoods> insertRetailGoods(String pol, String pod, String dim, String grossweight, String typeofcargo, String oceanfreight,
                                     String localcharge, String carrier, String schedule, String transittime, String valid, String note
            , String month , String continent, String date_created) {
        return mRetailGoodsService.addRetailGoodsData(pol, pod, dim, grossweight,typeofcargo, oceanfreight, localcharge, carrier,
                schedule, transittime, valid, note, month, continent, date_created);
    }

    public Call<RetailGoods> updateRetailGoods(String stt, String pol, String pod, String dim, String grossweight, String typeofcargo, String oceanfreight,
                                               String localcharge, String carrier, String schedule, String transittime, String valid, String note
            , String month , String continent) {
        return mRetailGoodsService.updateRetailGoodsData(stt, pol, pod, dim, grossweight,typeofcargo, oceanfreight, localcharge, carrier,
                schedule, transittime, valid, note, month, continent);
    }
}
