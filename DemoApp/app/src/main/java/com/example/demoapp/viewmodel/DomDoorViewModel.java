package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomDoor;
import com.example.demoapp.repository.DomDoorRepository;
import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;

public class DomDoorViewModel extends AndroidViewModel {
    private DomDoorRepository mDomDoorRepository;

    public DomDoorViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init() {
        mDomDoorRepository = new DomDoorRepository(Constants.URL_API);
    }

    public Call<DomDoor> insertData(String stationGo, String stationCome, String addressReceive, String addressDelivery, String name, String weight, String quantity,
                                    String etd, String type, String month, String continent, String createdDate) {

        return mDomDoorRepository.insertData(stationGo, stationCome, addressReceive, addressDelivery, name, weight, quantity, etd, type, month, continent, createdDate);
    }

    public Call<DomDoor> updateData(String stt, String stationGo, String stationCome, String addressReceive, String addressDelivery, String name, String weight, String quantity,
                                    String etd, String type, String month, String continent) {

        return mDomDoorRepository.updateData(stt, stationGo, stationCome, addressReceive, addressDelivery, name, weight, quantity, etd, type, month, continent);
    }

    public MutableLiveData<List<DomDoor>> getAllData() {
        return mDomDoorRepository.getAllData();
    }
}
