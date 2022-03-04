package com.example.demoapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.ImportLcl;
import com.example.demoapp.services.ImportLclServices;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportLclRepository {
    private MutableLiveData<List<ImportLcl>> mImportList;
    private final ImportLclServices mImportService;

    /**
     * This method used as constructor of Import Repository
     *
     * @param baseURL Url of API
     */
    public ImportLclRepository(String baseURL) {
        mImportService = APIClient.getClient(baseURL).create(ImportLclServices.class);
    }

    /**
     * This method will upload all data of Import table on database
     */
    public void upLoadAllImport() {
        Call<List<ImportLcl>> call = mImportService.getStatusImport();
        call.enqueue(new Callback<List<ImportLcl>>() {
            @Override
            public void onResponse(@NonNull Call<List<ImportLcl>> call, @NonNull Response<List<ImportLcl>> response) {
                if (response.body() != null) {
                    mImportList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ImportLcl>> call, @NonNull Throwable t) {
                mImportList.postValue(null);
            }
        });
    }

    /**
     * This method will store all data which get from Import Table
     *
     * @return list of Import
     */

    public LiveData<List<ImportLcl>> getAllImport() {
        if (mImportList == null) {
            mImportList = new MutableLiveData<>();
        }
        upLoadAllImport();
        return mImportList;
    }

    public Call<ImportLcl> insertImport(String term, String pol, String pod, String cargo, String of, String localPol, String localPod,
                                     String carrier, String schedule, String transitTime,String valid, String note, String month, String continent, String createdDate) {
        return mImportService.addData(term, pol, pod, cargo, of, localPol, localPod, carrier, schedule, transitTime, valid, note, month, continent, createdDate);
    }

    public Call<ImportLcl> updateImport(String stt, String term, String pol, String pod, String cargo, String of, String localPol, String localPod,
                                        String carrier, String schedule, String transitTime, String valid, String note, String month, String continent) {
        return mImportService.updateData(stt, term, pol, pod, cargo, of, localPol, localPod, carrier, schedule, transitTime, valid, note, month, continent);
    }
}
