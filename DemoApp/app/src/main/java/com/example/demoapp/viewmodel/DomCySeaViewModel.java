package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomCySea;
import com.example.demoapp.repository.DomCySeaRepository;
import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;

public class DomCySeaViewModel extends AndroidViewModel {
    private DomCySeaRepository mDomCySeaRepository;

    public DomCySeaViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init() {
        mDomCySeaRepository = new DomCySeaRepository(Constants.URL_API);
    }

    public Call<DomCySea> insertData(String portGo, String portCome, String name, String weight, String quantity,
                                     String etd, String type, String month, String continent, String createdDate) {

        return mDomCySeaRepository.insertData(portGo, portCome, name, weight, quantity, etd, type, month, continent, createdDate);
    }

    public Call<DomCySea> updateData(String stt, String portGo, String portCome, String name, String weight, String quantity,
                                  String etd, String type, String month, String continent) {

        return mDomCySeaRepository.updateData(stt, portGo, portCome, name, weight, quantity, etd, type, month, continent);
    }

    public MutableLiveData<List<DomCySea>> getAllData() {
        return mDomCySeaRepository.getAllData();
    }
}
