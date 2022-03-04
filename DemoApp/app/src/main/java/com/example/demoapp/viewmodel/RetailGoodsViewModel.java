package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demoapp.model.RetailGoods;
import com.example.demoapp.repository.RetailGoodsRepository;
import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;

public class RetailGoodsViewModel extends AndroidViewModel {
    private LiveData<List<RetailGoods>> mRetailGoodList;
    private RetailGoodsRepository mRetailGoodsRepository;

    public RetailGoodsViewModel(@NonNull Application application) {
        super(application);
        unit();
    }

    private void unit() {
        mRetailGoodsRepository = new RetailGoodsRepository(Constants.URL_API);
    }

    public Call<RetailGoods> insertRetailGoodsExport(String pol, String pod, String dim, String grossweight, String typeofcargo, String oceanfreight,
                                                     String localcharge, String carrier, String schedule, String transittime, String valid, String note
            , String month , String continent, String date_careated) {
        return  mRetailGoodsRepository.insertRetailGoods(pol, pod, dim, grossweight,typeofcargo, oceanfreight, localcharge, carrier,
                schedule, transittime, valid, note, month, continent, date_careated);
    }

    public Call<RetailGoods> updateRetailGoods(String stt, String pol, String pod, String dim, String grossweight, String typeofcargo, String oceanfreight,
                                     String localcharge, String carrier, String schedule, String transittime, String valid, String note
            , String month , String continent) {
        return mRetailGoodsRepository.updateRetailGoods(stt, pol, pod, dim, grossweight, typeofcargo, oceanfreight, localcharge, carrier,
                schedule, transittime, valid, note, month, continent);
    }

    public void loadAllRetailGoods() {

        mRetailGoodList = mRetailGoodsRepository.getAllRetailGoods();
    }

    public LiveData<List<RetailGoods>> getRetailGoodsList() {
        loadAllRetailGoods();
        return mRetailGoodList;
    }
}
