package com.example.demoapp.view.dialog.log.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentLogSaleDetailBinding;
import com.example.demoapp.model.Log;
import com.example.demoapp.utilities.Constants;


public class FragmentLogSaleDetail extends DialogFragment {

  private FragmentLogSaleDetailBinding mDetailBinding;
  private Bundle bundle;

    public static   FragmentLogSaleDetail getInstance(){
        return  new FragmentLogSaleDetail();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDetailBinding = FragmentLogSaleDetailBinding.inflate(inflater, container, false);
        View view = mDetailBinding.getRoot();
        bundle = getArguments();
        if(bundle != null){
            Log log = (Log) bundle.getSerializable(Constants.LOG_OBJECT);
            bundle.putSerializable(Constants.LOG_UPDATE, log);
            setDataLog(log);
        }

        return view;
    }

    private void setDataLog(Log log) {
        mDetailBinding.tvRowPriceLogStt.setText(log.getStt());
        mDetailBinding.tvRowPriceLogTenhang.setText(log.getTenhang());
        mDetailBinding.tvRowPriceLogHscode.setText(log.getHscode());
        mDetailBinding.tvRowPriceLogCongdung.setText(log.getCongdung());
        //mDetailBinding.tvRowPriceLogHinhanh.setText(log.getHinhanh());
        mDetailBinding.tvRowPriceLogCangdi.setText(log.getCangdi());
        mDetailBinding.tvRowPriceLogCangden.setText(log.getCangden());
        mDetailBinding.tvRowPriceLogLoaihang.setText(log.getLoaihang());
        mDetailBinding.tvRowPriceLogSoluongcuthe.setText(log.getSoluongcuthe());
        mDetailBinding.tvRowPriceLogYeucaudacbiet.setText(log.getYeucaudacbiet());
        mDetailBinding.tvRowPriceLogValid.setText(log.getPrice());
        mDetailBinding.tvRowPriceLogType.setText(log.getType());
    }
}