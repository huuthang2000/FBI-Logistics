package com.example.demoapp.view.dialog.air.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentAirImportSaleDetailBinding;
import com.example.demoapp.model.AirImport;
import com.example.demoapp.utilities.Constants;


public class FragmentAirImportSaleDetail extends DialogFragment {

    private FragmentAirImportSaleDetailBinding mAirImportSaleDetailBinding;
    private Bundle mBundle;

    public static FragmentAirImportSaleDetail getInstance(){
        return new FragmentAirImportSaleDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAirImportSaleDetailBinding = FragmentAirImportSaleDetailBinding.inflate(inflater, container, false);
        View view = mAirImportSaleDetailBinding.getRoot();
        mBundle = getArguments();
        if(mBundle != null){
            AirImport airImport = (AirImport) mBundle.getSerializable(Constants.AIR_IMPORT);
            mBundle.putSerializable(Constants.AIR_IMPORT_UPDATE, airImport);
            setDataAirImport(airImport);
        }
        return view;
    }
    private void setDataAirImport(AirImport air) {
        mAirImportSaleDetailBinding.tvRowPriceAirSttImport.setText(air.getStt());
        mAirImportSaleDetailBinding.tvRowPriceAirPolImport.setText(air.getAol());
        mAirImportSaleDetailBinding.tvRowPriceAirPodImport.setText(air.getAod());
        mAirImportSaleDetailBinding.tvRowPriceAirDimImport.setText(air.getDim());
        mAirImportSaleDetailBinding.tvRowPriceAirGrossweightImport.setText(air.getGrossweight());
        mAirImportSaleDetailBinding.tvRowPriceAirTypeofcargoImport.setText(air.getTypeofcargo());
        mAirImportSaleDetailBinding.tvRowPriceAirFreightImport.setText(air.getAirfreight());
        mAirImportSaleDetailBinding.tvRowPriceAirSurchargeImport.setText(air.getSurcharge());
        mAirImportSaleDetailBinding.tvRowPriceAirAirlinesImport.setText(air.getAirlines());
        mAirImportSaleDetailBinding.tvRowPriceAirScheduleImport.setText(air.getSchedule());
        mAirImportSaleDetailBinding.tvRowPriceAirTransittimeImport.setText(air.getTransittime());
        mAirImportSaleDetailBinding.tvRowPriceAirValidImport.setText(air.getValid());
        mAirImportSaleDetailBinding.tvRowPriceAirNoteImport.setText(air.getNote());


    }
}