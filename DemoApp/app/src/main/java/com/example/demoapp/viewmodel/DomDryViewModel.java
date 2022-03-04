package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomDry;
import com.example.demoapp.repository.DomDryRepository;
import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;

public class DomDryViewModel extends AndroidViewModel {
    private DomDryRepository mDomDryRepository;

    public DomDryViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init() {
        mDomDryRepository = new DomDryRepository(Constants.URL_API);
    }

    public Call<DomDry> insertData(String name, String weight, String quantityPallet,
                                   String quantityCarton, String addressReceive, String addressDelivery, String length,
                                   String height, String width, String type, String month, String continent, String createdDate) {

        return mDomDryRepository.insertData(name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width, type, month, continent, createdDate);
    }

    public Call<DomDry> updateData(String stt, String name, String weight, String quantityPallet,
                                   String quantityCarton, String addressReceive, String addressDelivery, String length,
                                   String height, String width, String type, String month, String continent) {

        return mDomDryRepository.updateData(stt, name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width, type, month, continent);
    }

    public MutableLiveData<List<DomDry>> getAllData() {
        return mDomDryRepository.getAllData();
    }
}
