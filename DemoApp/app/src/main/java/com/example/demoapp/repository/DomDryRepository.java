package com.example.demoapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomDry;
import com.example.demoapp.model.DomImport;
import com.example.demoapp.services.DomDryService;
import com.example.demoapp.services.DomImportService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DomDryRepository {
    private MutableLiveData<List<DomDry>> listDry;
    private DomDryService mDomDryService;

    public DomDryRepository(String baseURL) {
        mDomDryService = APIClient.getClient(baseURL).create(DomDryService.class);
    }

    public void loadAllData() {
        mDomDryService.getAllDomDry().enqueue(new Callback<List<DomDry>>() {
            @Override
            public void onResponse(@NonNull Call<List<DomDry>> call, @NonNull Response<List<DomDry>> response) {
                if (response.isSuccessful()) {
                    listDry.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DomDry>> call, @NonNull Throwable t) {
                listDry.postValue(null);
            }
        });
    }

    public MutableLiveData<List<DomDry>> getAllData() {

        if (listDry == null) {
            listDry = new MutableLiveData<>();
        }

        loadAllData();
        return listDry;
    }

    public Call<DomDry> insertData(String name, String weight, String quantityPallet,
                                   String quantityCarton, String addressReceive, String addressDelivery, String length,
                                   String height, String width, String type, String month, String continent, String createdDate) {

        return mDomDryService.insertData(name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width, type, month, continent, createdDate);
    }

    public Call<DomDry> updateData(String stt, String name, String weight, String quantityPallet,
                                   String quantityCarton, String addressReceive, String addressDelivery, String length,
                                   String height, String width, String type, String month, String continent) {

        return mDomDryService.updateData(stt, name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width, type, month, continent);
    }
}
