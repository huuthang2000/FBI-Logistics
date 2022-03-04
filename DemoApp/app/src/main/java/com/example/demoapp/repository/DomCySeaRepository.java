package com.example.demoapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomCy;
import com.example.demoapp.model.DomCySea;
import com.example.demoapp.services.DomCySeaService;
import com.example.demoapp.services.DomCyService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DomCySeaRepository {
    private MutableLiveData<List<DomCySea>> listCySea;
    private final DomCySeaService mDomCySeaService;

    public DomCySeaRepository(String baseURL) {
        mDomCySeaService = APIClient.getClient(baseURL).create(DomCySeaService.class);
    }

    public void loadAllData() {
        mDomCySeaService.getAllDomCySea().enqueue(new Callback<List<DomCySea>>() {
            @Override
            public void onResponse(@NonNull Call<List<DomCySea>> call, @NonNull Response<List<DomCySea>> response) {
                if (response.isSuccessful()) {
                    listCySea.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DomCySea>> call, @NonNull Throwable t) {
                listCySea.postValue(null);
            }
        });
    }

    public MutableLiveData<List<DomCySea>> getAllData() {
        if (listCySea == null) {
            listCySea = new MutableLiveData<>();
        }

        loadAllData();
        return listCySea;
    }

    public Call<DomCySea> insertData(String portGo, String portCome, String name, String weight, String quantity,
                                     String etd, String type, String month, String continent, String createdDate) {

        return mDomCySeaService.insertData(portGo, portCome, name, weight, quantity, etd, type, month, continent, createdDate);
    }

    public Call<DomCySea> updateData(String stt, String portGo, String portCome, String name, String weight, String quantity,
                                     String etd, String type, String month, String continent) {

        return mDomCySeaService.updateData(stt, portGo, portCome, name, weight, quantity, etd, type, month, continent);
    }
}
