package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomCy;
import com.example.demoapp.repository.DomCyRepository;
import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;

public class DomCyViewModel extends AndroidViewModel {
    private DomCyRepository mDomCyRepository;

    public DomCyViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init() {
        mDomCyRepository = new DomCyRepository(Constants.URL_API);
    }

    public Call<DomCy> insertData(String stationGo, String stationCome, String name, String weight, String quantity,
                                  String etd, String type, String month, String continent, String createdDate) {

        return mDomCyRepository.insertData(stationGo, stationCome, name, weight, quantity, etd, type, month, continent, createdDate);
    }

    public Call<DomCy> updateData(String stt, String stationGo, String stationCome, String name, String weight, String quantity,
                                  String etd, String type, String month, String continent) {

        return mDomCyRepository.updateData(stt, stationGo, stationCome, name, weight, quantity, etd, type, month, continent);
    }

    public MutableLiveData<List<DomCy>> getAllData() {
        return mDomCyRepository.getAllData();
    }
}
