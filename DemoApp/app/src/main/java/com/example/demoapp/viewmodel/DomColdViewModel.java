package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomCold;
import com.example.demoapp.repository.DomColdRepository;
import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;

public class DomColdViewModel extends AndroidViewModel {
    private DomColdRepository mDomColdRepository;

    public DomColdViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init() {
        mDomColdRepository = new DomColdRepository(Constants.URL_API);
    }

    public Call<DomCold> insertData(String name, String weight, String quantityPallet,
                                    String quantityCarton, String addressReceive, String addressDelivery, String length,
                                    String height, String width, String type, String month, String continent, String createdDate) {

        return mDomColdRepository.insertData(name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width, type, month, continent, createdDate);
    }

    public Call<DomCold> updateData(String stt, String name, String weight, String quantityPallet,
                                    String quantityCarton, String addressReceive, String addressDelivery, String length,
                                    String height, String width, String type, String month, String continent) {

        return mDomColdRepository.updateData(stt, name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width, type, month, continent);
    }

    public MutableLiveData<List<DomCold>> getAllData() {
        return mDomColdRepository.getAllData();
    }
}
