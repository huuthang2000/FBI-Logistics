package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.DomImport;
import com.example.demoapp.repository.DomImportRepository;
import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;

public class DomImportViewModel extends AndroidViewModel {
    private DomImportRepository mDomImportRepository;

    public DomImportViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init() {
        mDomImportRepository = new DomImportRepository(Constants.URL_API);
    }

    public Call<DomImport> insertData(String name, String weight, String quantity,
                                      String temp, String address, String portExport, String length,
                                      String height, String width, String type, String month, String continent, String createdDate) {

        return mDomImportRepository.insertData(name, weight, quantity, temp, address, portExport, length, height, width, type, month, continent, createdDate);
    }

    public Call<DomImport> updateData(String stt, String name, String weight, String quantity,
                                      String temp, String address, String portExport, String length,
                                      String height, String width, String type, String month, String continent) {

        return mDomImportRepository.updateData(stt, name, weight, quantity, temp, address, portExport, length, height, width, type, month, continent);
    }

    public MutableLiveData<List<DomImport>> getAllData() {
        return mDomImportRepository.getAllData();
    }
}
