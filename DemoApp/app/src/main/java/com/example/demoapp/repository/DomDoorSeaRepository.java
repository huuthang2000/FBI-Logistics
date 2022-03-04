package com.example.demoapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomDoorSea;
import com.example.demoapp.services.DomDoorSeaService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DomDoorSeaRepository {
    private MutableLiveData<List<DomDoorSea>> listDoorSea;
    private final DomDoorSeaService mDomDoorSeaService;

    public DomDoorSeaRepository(String baseURL) {
        mDomDoorSeaService = APIClient.getClient(baseURL).create(DomDoorSeaService.class);
    }

    public void loadAllData() {
        mDomDoorSeaService.getAllDomDoorSea().enqueue(new Callback<List<DomDoorSea>>() {
            @Override
            public void onResponse(@NonNull Call<List<DomDoorSea>> call, @NonNull Response<List<DomDoorSea>> response) {
                if (response.isSuccessful()) {
                    listDoorSea.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DomDoorSea>> call, @NonNull Throwable t) {
                listDoorSea.postValue(null);
            }
        });
    }

    public MutableLiveData<List<DomDoorSea>> getAllData() {
        if (listDoorSea == null) {
            listDoorSea = new MutableLiveData<>();
        }

        loadAllData();
        return listDoorSea;
    }

    public Call<DomDoorSea> insertData(String portGo, String portCome, String addressReceive, String addressDelivery, String name, String weight, String quantity,
                                    String etd, String type, String month, String continent, String createdDate) {

        return mDomDoorSeaService.insertData(portGo, portCome, addressReceive, addressDelivery, name, weight, quantity, etd, type, month, continent, createdDate);
    }

    public Call<DomDoorSea> updateData(String stt, String portGo, String portCome, String addressReceive, String addressDelivery, String name, String weight, String quantity,
                                    String etd, String type, String month, String continent) {

        return mDomDoorSeaService.updateData(stt, portGo, portCome, addressReceive, addressDelivery, name, weight, quantity, etd, type, month, continent);
    }
}
