package com.example.demoapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.model.Log;
import com.example.demoapp.services.LogService;
import com.example.demoapp.utilities.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogRepository {
    private MutableLiveData<List<Log>> mListLog;
    private LogService mLogService;

    public LogRepository(String baseURL) {
        mLogService = APIClient.getClient(baseURL).create(LogService.class);
    }

    public void uploadAllLog() {
        Call<List<Log>> call = mLogService.getpriceLog();
        call.enqueue(new Callback<List<Log>>() {
            @Override
            public void onResponse(Call<List<Log>> call, Response<List<Log>> response) {
                if (response.body() != null) {
                    mListLog.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Log>> call, Throwable t) {

                mListLog.postValue(null);
            }
        });
    }

    public LiveData<List<Log>> getAllLog() {
        if (mListLog == null) {
            mListLog = new MutableLiveData<>();
        }
        uploadAllLog();
        return mListLog;
    }

    public Call<Log> insertLog(String tenhang, String hscode, String hinhanh, String congdung,
                               String cangdi, String cangden, String loaihang, String soluongcuthe,
                               String yeucaudacbiet, String price, String month, String importorExport,
                               String type, String date_created) {
       return mLogService.addData(tenhang, hscode, hinhanh, congdung, cangdi, cangden, loaihang, soluongcuthe,
                yeucaudacbiet, price, month, importorExport, type, date_created);
    }

    public Call<Log> updateLog(String stt, String tenhang, String hscode, String hinhanh, String congdung,
                               String cangdi, String cangden, String loaihang, String soluongcuthe,
                               String yeucaudacbiet, String price,String month, String importorExport,
                               String type) {
        return mLogService.updateData(stt, tenhang, hscode, hinhanh, congdung, cangdi, cangden, loaihang, soluongcuthe,
                yeucaudacbiet, price, month, importorExport, type);
    }

}
