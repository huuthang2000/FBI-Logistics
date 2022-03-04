package com.example.demoapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomDoor;
import com.example.demoapp.services.DomDoorService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DomDoorRepository {
    private MutableLiveData<List<DomDoor>> listDoor;
    private final DomDoorService mDomDoorService;

    public DomDoorRepository(String baseURL) {
        mDomDoorService = APIClient.getClient(baseURL).create(DomDoorService.class);
    }

    public void loadAllData() {
        mDomDoorService.getAllDomDoor().enqueue(new Callback<List<DomDoor>>() {
            @Override
            public void onResponse(@NonNull Call<List<DomDoor>> call, @NonNull Response<List<DomDoor>> response) {
                if (response.isSuccessful()) {
                    listDoor.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DomDoor>> call, @NonNull Throwable t) {
                listDoor.postValue(null);
            }
        });
    }

    public MutableLiveData<List<DomDoor>> getAllData() {
        if (listDoor == null) {
            listDoor = new MutableLiveData<>();
        }

        loadAllData();
        return listDoor;
    }

    public Call<DomDoor> insertData(String stationGo, String stationCome, String addressReceive, String addressDelivery, String name, String weight, String quantity,
                                    String etd, String type, String month, String continent, String createdDate) {

        return mDomDoorService.insertData(stationGo, stationCome, addressReceive, addressDelivery, name, weight, quantity, etd, type, month, continent, createdDate);
    }

    public Call<DomDoor> updateData(String stt, String stationGo, String stationCome, String addressReceive, String addressDelivery, String name, String weight, String quantity,
                                    String etd, String type, String month, String continent) {

        return mDomDoorService.updateData(stt, stationGo, stationCome, addressReceive, addressDelivery, name, weight, quantity, etd, type, month, continent);
    }
}
