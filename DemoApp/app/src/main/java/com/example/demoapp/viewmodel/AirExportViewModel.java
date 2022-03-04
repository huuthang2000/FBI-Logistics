package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demoapp.model.AirExport;
import com.example.demoapp.repository.AirExportRepository;
import com.example.demoapp.utilities.Constants;


import java.util.List;

import retrofit2.Call;

public class AirExportViewModel extends AndroidViewModel {
    private LiveData<List<AirExport>> mAirList;
    private AirExportRepository mAirRepository;

    public AirExportViewModel(Application application) {
        super(application);

        init();

    }

    public void init() {
        mAirRepository = new AirExportRepository(Constants.URL_API);
    }

    public Call<AirExport> insertAir(String aol, String aod, String dim, String grossweight, String typeofcargo, String airfreight,
                                     String surcharge, String airlines, String schedule, String transittime, String valid, String note
            , String month , String continent, String date_created) {
        return  mAirRepository.insertAir(aol, aod, dim, grossweight,typeofcargo, airfreight, surcharge, airlines,
                schedule, transittime, valid, note, month, continent, date_created);
    }

    public Call<AirExport> updateAir(String stt, String aol, String aod, String dim, String grossweight, String typeofcargo, String airfreight,
                                     String surcharge, String airlines, String schedule, String transittime, String valid, String note
            , String month , String continent) {
        return mAirRepository.updateAir(stt, aol, aod, dim, grossweight, typeofcargo, airfreight, surcharge, airlines,
                schedule, transittime, valid, note, month, continent);
    }

    public void loadAllAir() {
        mAirList = mAirRepository.getAllAir();
    }

    public LiveData<List<AirExport>> getLclList() {
        loadAllAir();
        return mAirList;
    }


}
