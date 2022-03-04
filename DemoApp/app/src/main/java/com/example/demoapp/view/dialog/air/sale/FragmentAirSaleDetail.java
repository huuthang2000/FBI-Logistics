package com.example.demoapp.view.dialog.air.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentAirSaleDetailBinding;
import com.example.demoapp.model.AirExport;
import com.example.demoapp.utilities.Constants;


public class FragmentAirSaleDetail extends DialogFragment {

    private FragmentAirSaleDetailBinding mAirSaleDetailBinding;
    private Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAirSaleDetailBinding = FragmentAirSaleDetailBinding.inflate(inflater, container, false);
        View view = mAirSaleDetailBinding.getRoot();

        bundle = getArguments();
        if (bundle != null) {
            AirExport air = (AirExport) bundle.getSerializable(Constants.AIR_OBJECT);
            bundle.putSerializable(Constants.AIR_UPDATE, air);
            setDataAir(air);
        }

        return view;
    }

    public static FragmentAirSaleDetail getInstance(){
        return new FragmentAirSaleDetail();
    }

    private void setDataAir(AirExport air) {
        mAirSaleDetailBinding.tvRowPriceAsiaAirStt.setText(air.getStt());
        mAirSaleDetailBinding.tvRowPriceAsiaAirAol.setText(air.getAol());
        mAirSaleDetailBinding.tvRowPriceAsiaAirAod.setText(air.getAod());
        mAirSaleDetailBinding.tvRowPriceAsiaAirDim.setText(air.getDim());
        mAirSaleDetailBinding.tvRowPriceAsiaAirGrossweight.setText(air.getGrossweight());
        mAirSaleDetailBinding.tvRowPriceAsiaAirTypeofcargo.setText(air.getTypeofcargo());
        mAirSaleDetailBinding.tvRowPriceAsiaAirFreight.setText(air.getAirfreight());
        mAirSaleDetailBinding.tvRowPriceAsiaAirSurcharge.setText(air.getSurcharge());
        mAirSaleDetailBinding.tvRowPriceAsiaAirAirlines.setText(air.getAirlines());
        mAirSaleDetailBinding.tvRowPriceAsiaAirSchedule.setText(air.getSchedule());
        mAirSaleDetailBinding.tvRowPriceAsiaAirTransittime.setText(air.getTransittime());
        mAirSaleDetailBinding.tvRowPriceAsiaAirValid.setText(air.getValid());
        mAirSaleDetailBinding.tvRowPriceAsiaAirNote.setText(air.getNote());
    }
}