package com.example.demoapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomCy;
import com.example.demoapp.services.DomCyService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DomCyRepository {
    private MutableLiveData<List<DomCy>> listCy;
    private final DomCyService mDomCyService;

    public DomCyRepository(String baseURL) {
        mDomCyService = APIClient.getClient(baseURL).create(DomCyService.class);
    }

    public void loadAllData() {
        mDomCyService.getAllDomCy().enqueue(new Callback<List<DomCy>>() {
            @Override
            public void onResponse(@NonNull Call<List<DomCy>> call, @NonNull Response<List<DomCy>> response) {
                if (response.isSuccessful()) {
                    listCy.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DomCy>> call, @NonNull Throwable t) {
                listCy.postValue(null);
            }
        });
    }

    public MutableLiveData<List<DomCy>> getAllData() {
        if (listCy == null) {
            listCy = new MutableLiveData<>();
        }

        loadAllData();
        return listCy;
    }

    public Call<DomCy> insertData(String stationGo, String stationCome, String name, String weight, String quantity,
                                  String etd, String type, String month, String continent, String createdDate) {

        return mDomCyService.insertData(stationGo, stationCome, name, weight, quantity, etd, type, month, continent, createdDate);
    }

    public Call<DomCy> updateData(String stt, String stationGo, String stationCome, String name, String weight, String quantity,
                                  String etd, String type, String month, String continent) {

        return mDomCyService.updateData(stt, stationGo, stationCome, name, weight, quantity, etd, type, month, continent);
    }
}
