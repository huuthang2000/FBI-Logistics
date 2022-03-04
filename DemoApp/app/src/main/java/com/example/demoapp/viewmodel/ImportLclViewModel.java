package com.example.demoapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demoapp.model.ImportLcl;
import com.example.demoapp.repository.ImportLclRepository;

import com.example.demoapp.utilities.Constants;

import java.util.List;

import retrofit2.Call;

public class ImportLclViewModel extends AndroidViewModel {

    private LiveData<List<ImportLcl>> mImportList;
    private ImportLclRepository mImportRepository;

    public ImportLclViewModel(Application application) {
        super(application);
        init();

    }

    public void init() {
        mImportRepository = new ImportLclRepository(Constants.URL_API);
    }

    public void loadAllImport() {
        mImportList = mImportRepository.getAllImport();
    }

    public LiveData<List<ImportLcl>> getImportList() {
        loadAllImport();
        return mImportList;
    }

    public Call<ImportLcl> insertImport(String term, String pol, String pod, String cargo, String of, String localPol, String localPod,
                                        String carrier, String schedule, String transitTime, String valid, String note, String month, String continent, String createdDate) {
        return mImportRepository.insertImport(term, pol, pod, cargo, of, localPol, localPod, carrier, schedule, transitTime, valid, note, month, continent, createdDate);
    }

    public Call<ImportLcl> updateImport(String stt, String term, String pol, String pod, String cargo, String of, String localPol, String localPod,
                                        String carrier, String schedule, String transitTime, String valid, String note, String month, String continent) {
        return mImportRepository.updateImport(stt, term, pol, pod, cargo, of, localPol, localPod, carrier, schedule, transitTime, valid, note, month, continent);

    }
}
