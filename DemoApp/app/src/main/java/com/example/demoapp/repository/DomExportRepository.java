package com.example.demoapp.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomExport;
import com.example.demoapp.services.DomExportService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DomExportRepository {
    private MutableLiveData<List<DomExport>> listExport;
    private final DomExportService mDomExportService;

    public DomExportRepository(String baseURL) {
        mDomExportService = APIClient.getClient(baseURL).create(DomExportService.class);
    }

    public void loadAllData() {
        mDomExportService.getAllDomExport().enqueue(new Callback<List<DomExport>>() {
            @Override
            public void onResponse(Call<List<DomExport>> call, Response<List<DomExport>> response) {
                if (response.isSuccessful()) {
                    listExport.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<DomExport>> call, Throwable t) {
                listExport.postValue(null);
            }
        });
    }

    public MutableLiveData<List<DomExport>> getAllData() {

        if (listExport == null) {
            listExport = new MutableLiveData<>();
        }

        loadAllData();
        return listExport;
    }

    public Call<DomExport> insertData(String name, String weight, String quantity,
                                      String temp, String address, String portExport, String length,
                                      String height, String width, String type, String month, String continent, String createdDate) {

        return mDomExportService.insertData(name, weight, quantity, temp, address, portExport, length, height, width, type, month, continent, createdDate);
    }

    public Call<DomExport> updateData(String stt, String name, String weight, String quantity,
                                      String temp, String address, String portExport, String length,
                                      String height, String width, String type, String month, String continent) {

        return mDomExportService.updateData(stt, name, weight, quantity, temp, address, portExport, length, height, width, type, month, continent);
    }
}
