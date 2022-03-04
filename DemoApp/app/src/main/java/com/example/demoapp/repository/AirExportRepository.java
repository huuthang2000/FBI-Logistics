package com.example.demoapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.AirExport;
import com.example.demoapp.services.AIRExportService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirExportRepository {

    private MutableLiveData<List<AirExport>> mAirList;
    private AIRExportService mAirService;


    public AirExportRepository(String baseURL) {
        mAirService = APIClient.getClient(baseURL).create(AIRExportService.class);
    }


    public void upLoadAllAir() {
        Call<List<AirExport>> call = mAirService.getpriceAIR();
        call.enqueue(new Callback<List<AirExport>>() {
            @Override
            public void onResponse(Call<List<AirExport>> call, Response<List<AirExport>> response) {
                if (response.body() != null) {
                    mAirList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<AirExport>> call, Throwable t) {
                mAirList.postValue(null);
            }
        });
    }

    /**
     * This method will store data which get from fcl table
     *
     * @return list of Air table
     */

    public LiveData<List<AirExport>> getAllAir() {
        if (mAirList == null) {
            mAirList = new MutableLiveData<>();
        }
        upLoadAllAir();
        return mAirList;
    }

    public Call<AirExport> insertAir(String aol, String aod, String dim, String grossweight, String typeofcargo, String airfreight,
                                     String surcharge, String airlines, String schedule, String transittime, String valid, String note
                               , String month , String continent, String date_created) {
        return mAirService.addAirExportData(aol, aod, dim, grossweight,typeofcargo, airfreight, surcharge, airlines,
                schedule, transittime, valid, note, month, continent, date_created);
    }

    public Call<AirExport> updateAir(String stt, String aol, String aod, String dim, String grossweight, String typeofcargo, String airfreight,
                                     String surcharge, String airlines, String schedule, String transittime, String valid, String note
            , String month , String continent) {
        return mAirService.updateAirExportData(stt, aol, aod, dim, grossweight, typeofcargo, airfreight, surcharge, airlines,
                schedule, transittime, valid, note, month, continent);
    }
}
