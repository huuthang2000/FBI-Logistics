package com.example.demoapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demoapp.model.Fcl;
import com.example.demoapp.repository.FclRepository;
import com.example.demoapp.utilities.Constants;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FclViewModel extends AndroidViewModel {
    private LiveData<List<Fcl>> mFclList;
    private FclRepository mFclRepository;

    public FclViewModel(Application application) {
        super(application);
        init();
    }

    public void init() {
        mFclRepository = new FclRepository(Constants.URL_API);
    }

    public Call<Fcl> insertFcl(String pol, String pod, String of20, String of40, String of45, String su20, String su40,
                               String line, String notes, String valid, String notes2, String month, String type,
                               String continent, String createdDate) {


        return mFclRepository.insertFcl(pol, pod, of20, of40, of45, su20, su40, line, notes, valid, notes2, month, type, continent, createdDate);
    }

    public void loadAllFcl() {
        mFclList = mFclRepository.getAllFcl();
    }

    public LiveData<List<Fcl>> getFclList() {
        loadAllFcl();
        return mFclList;
    }

    public Call<Fcl> updateFcl(String stt, String pol, String pod, String of20, String of40, String of45, String su20, String su40,
                               String line, String notes, String valid, String notes2, String month, String type,
                               String continent) {
        return mFclRepository.updateFcl(stt, pol, pod, of20, of40, of45, su20, su40, line, notes, valid, notes2, month, type, continent);
    }
}
