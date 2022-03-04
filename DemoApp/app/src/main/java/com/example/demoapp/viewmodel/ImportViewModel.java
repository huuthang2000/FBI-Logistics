package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demoapp.model.Fcl;
import com.example.demoapp.model.Import;
import com.example.demoapp.repository.ImportRepository;
import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;

public class ImportViewModel extends AndroidViewModel {
    private LiveData<List<Import>> mImportList;
    private ImportRepository mImportRepository;

    public ImportViewModel(Application application) {
        super(application);
        init();

    }

    public void init() {
        mImportRepository = new ImportRepository(Constants.URL_API);
    }

    public void loadAllImport() {
        mImportList = mImportRepository.getAllImport();
    }

    public LiveData<List<Import>> getImportList() {
        loadAllImport();
        return mImportList;
    }

    public Call<Import> insertImport(String pol, String pod, String of20, String of40, String of45,String sur20, String sur40, String sur45, String totalFreight,
                                     String carrier, String schedule, String transitTime, String freeTime, String valid, String note,
                                     String type, String month, String continent, String createdDate) {


        return mImportRepository.insertImport(pol, pod, of20, of40, of45, sur20, sur40, sur45, totalFreight, carrier, schedule, transitTime, freeTime, valid, note, type, month, continent, createdDate);
    }

    public Call<Import> updateImport(String stt, String pol, String pod, String of20, String of40, String of45, String sur20, String sur40, String sur45, String totalFreight,
                                     String carrier, String schedule, String transitTime, String freeTime, String valid, String note,
                                     String type, String month, String continent) {
        return mImportRepository.updateImport(stt, pol, pod, of20, of40, of45, sur20, sur40, sur45, totalFreight, carrier, schedule, transitTime, freeTime, valid, note, type, month, continent);
    }
}
