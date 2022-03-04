package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomExport;
import com.example.demoapp.repository.DomExportRepository;
import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;

public class DomExportViewModel extends AndroidViewModel {
    private DomExportRepository mDomExportRepository;

    public DomExportViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init() {
        mDomExportRepository = new DomExportRepository(Constants.URL_API);
    }

    public Call<DomExport> insertData(String name, String weight, String quantity,
                                      String temp, String address, String portExport, String length,
                                      String height, String width, String type, String month, String continent, String createdDate) {

        return mDomExportRepository.insertData(name, weight, quantity, temp, address, portExport, length, height, width, type, month, continent, createdDate);
    }

    public Call<DomExport> updateData(String stt, String name, String weight, String quantity,
                                      String temp, String address, String portExport, String length,
                                      String height, String width, String type, String month, String continent) {

        return mDomExportRepository.updateData(stt, name, weight, quantity, temp, address, portExport, length, height, width, type, month, continent);
    }

    public MutableLiveData<List<DomExport>> getAllData() {
        return mDomExportRepository.getAllData();
    }
}
