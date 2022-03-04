package com.example.demoapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomExport;
import com.example.demoapp.model.DomImport;
import com.example.demoapp.services.DomExportService;
import com.example.demoapp.services.DomImportService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DomImportRepository {
    private MutableLiveData<List<DomImport>> listImport;
    private final DomImportService mDomImportService;

    public DomImportRepository(String baseURL) {
        mDomImportService = APIClient.getClient(baseURL).create(DomImportService.class);
    }

    public void loadAllData() {
        mDomImportService.getAllDomImport().enqueue(new Callback<List<DomImport>>() {
            @Override
            public void onResponse(@NonNull Call<List<DomImport>> call, @NonNull Response<List<DomImport>> response) {
                if (response.isSuccessful()) {
                    listImport.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DomImport>> call, @NonNull Throwable t) {
                listImport.postValue(null);
            }
        });
    }

    public MutableLiveData<List<DomImport>> getAllData() {

        if (listImport == null) {
            listImport = new MutableLiveData<>();
        }

        loadAllData();
        return listImport;
    }

    public Call<DomImport> insertData(String name, String weight, String quantity,
                                      String temp, String address, String portReceive, String length,
                                      String height, String width, String type, String month, String continent, String createdDate) {

        return mDomImportService.insertData(name, weight, quantity, temp, address, portReceive, length, height, width, type, month, continent, createdDate);
    }

    public Call<DomImport> updateData(String stt, String name, String weight, String quantity,
                                      String temp, String address, String portReceive, String length,
                                      String height, String width, String type, String month, String continent) {

        return mDomImportService.updateData(stt, name, weight, quantity, temp, address, portReceive, length, height, width, type, month, continent);
    }
}
