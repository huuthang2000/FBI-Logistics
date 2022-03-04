package com.example.demoapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.AirImport;
import com.example.demoapp.services.AIRImportService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirImportRepository {

    private MutableLiveData<List<AirImport>> mListAirImport;
    private AIRImportService mAirImportService;

    public AirImportRepository(String baseURL){
        mAirImportService = APIClient.getClient(baseURL).create(AIRImportService.class);
    }

    public void upLoadAllAir() {
        Call<List<AirImport>> call = mAirImportService.getpriceAIRImport();
        call.enqueue(new Callback<List<AirImport>>() {
            @Override
            public void onResponse(Call<List<AirImport>> call, Response<List<AirImport>> response) {
                if (response.body() != null) {
                    mListAirImport.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<AirImport>> call, Throwable t) {
                mListAirImport.postValue(null);
            }
        });
    }

    /**
     * This method will store data which get from fcl table
     *
     * @return list of Air table
     */

    public LiveData<List<AirImport>> getAllAir() {
        if (mListAirImport == null) {
            mListAirImport = new MutableLiveData<>();
        }
        upLoadAllAir();
        return mListAirImport;
    }

    public Call<AirImport> insertAir(String aol, String aod, String dim, String grossweight, String typeofcargo, String airfreight,
                                     String surcharge, String airlines, String schedule, String transittime, String valid, String note
            , String month , String continent, String date_created) {
        return mAirImportService.addAirImportData(aol, aod, dim, grossweight,typeofcargo, airfreight, surcharge, airlines,
                schedule, transittime, valid, note, month, continent, date_created);
    }

    public Call<AirImport> updateAir(String stt, String aol, String aod, String dim, String grossweight, String typeofcargo, String airfreight,
                                     String surcharge, String airlines, String schedule, String transittime, String valid, String note
            , String month , String continent) {
        return mAirImportService.updateAirImportData(stt, aol, aod, dim, grossweight, typeofcargo, airfreight, surcharge, airlines,
                schedule, transittime, valid, note, month, continent);
    }
}
