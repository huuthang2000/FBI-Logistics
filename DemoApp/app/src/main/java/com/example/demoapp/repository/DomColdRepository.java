package com.example.demoapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomCold;
import com.example.demoapp.model.DomDry;
import com.example.demoapp.services.DomColdService;
import com.example.demoapp.services.DomDryService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DomColdRepository {
    private MutableLiveData<List<DomCold>> listCold;
    private final DomColdService mDomColdService;

    public DomColdRepository(String baseURL) {
        mDomColdService = APIClient.getClient(baseURL).create(DomColdService.class);
    }

    public void loadAllData() {
        mDomColdService.getAllDomCold().enqueue(new Callback<List<DomCold>>() {
            @Override
            public void onResponse(@NonNull Call<List<DomCold>> call, @NonNull Response<List<DomCold>> response) {
                if (response.isSuccessful()) {
                    listCold.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DomCold>> call, @NonNull Throwable t) {
                listCold.postValue(null);
            }
        });
    }

    public MutableLiveData<List<DomCold>> getAllData() {

        if (listCold == null) {
            listCold = new MutableLiveData<>();
        }

        loadAllData();
        return listCold;
    }

    public Call<DomCold> insertData(String name, String weight, String quantityPallet,
                                    String quantityCarton, String addressReceive, String addressDelivery, String length,
                                    String height, String width, String type, String month, String continent, String createdDate) {

        return mDomColdService.insertData(name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width, type, month, continent, createdDate);
    }

    public Call<DomCold> updateData(String stt, String name, String weight, String quantityPallet,
                                    String quantityCarton, String addressReceive, String addressDelivery, String length,
                                    String height, String width, String type, String month, String continent) {

        return mDomColdService.updateData(stt, name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width, type, month, continent);
    }
}
