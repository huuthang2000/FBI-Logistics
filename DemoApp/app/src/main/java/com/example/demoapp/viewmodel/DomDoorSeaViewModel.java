package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomDoorSea;
import com.example.demoapp.repository.DomDoorSeaRepository;
import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;

public class DomDoorSeaViewModel extends AndroidViewModel {
    private DomDoorSeaRepository mDomDoorSeaRepository;

    public DomDoorSeaViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init() {
        mDomDoorSeaRepository = new DomDoorSeaRepository(Constants.URL_API);
    }

    public Call<DomDoorSea> insertData(String portGo, String portCome, String addressReceive, String addressDelivery, String name, String weight, String quantity,
                                       String etd, String type, String month, String continent, String createdDate) {

        return mDomDoorSeaRepository.insertData(portGo, portCome, addressReceive, addressDelivery, name, weight, quantity, etd, type, month, continent, createdDate);
    }

    public Call<DomDoorSea> updateData(String stt, String portGo, String portCome, String addressReceive, String addressDelivery, String name, String weight, String quantity,
                                       String etd, String type, String month, String continent) {

        return mDomDoorSeaRepository.updateData(stt, portGo, portCome, addressReceive, addressDelivery, name, weight, quantity, etd, type, month, continent);
    }

    public MutableLiveData<List<DomDoorSea>> getAllData() {
        return mDomDoorSeaRepository.getAllData();
    }
}
