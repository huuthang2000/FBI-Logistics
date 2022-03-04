package com.example.demoapp.view.dialog.air.air_export;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentAirDialogBinding;
import com.example.demoapp.model.AirExport;
import com.example.demoapp.utilities.Constants;


public class FragmentAirDetail extends DialogFragment implements View.OnClickListener {

    private FragmentAirDialogBinding mAirDialogBinding;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAirDialogBinding = FragmentAirDialogBinding.inflate(inflater, container, false);
        View view = mAirDialogBinding.getRoot();
        bundle = getArguments();
        if (bundle != null) {
            AirExport air = (AirExport) bundle.getSerializable(Constants.AIR_OBJECT);
            bundle.putSerializable(Constants.AIR_UPDATE, air);
            setDataAir(air);
        }
        mAirDialogBinding.btnUpdateAir.setOnClickListener(this);
        return view;
    }
    public  void setDataAir(AirExport air){
        mAirDialogBinding.tvRowPriceAsiaAirStt.setText(air.getStt());
        mAirDialogBinding.tvRowPriceAsiaAirAol.setText(air.getAol());
        mAirDialogBinding.tvRowPriceAsiaAirAod.setText(air.getAod());
        mAirDialogBinding.tvRowPriceAsiaAirDim.setText(air.getDim());
        mAirDialogBinding.tvRowPriceAsiaAirGrossweight.setText(air.getGrossweight());
        mAirDialogBinding.tvRowPriceAsiaAirTypeofcargo.setText(air.getTypeofcargo());
        mAirDialogBinding.tvRowPriceAsiaAirFreight.setText(air.getAirfreight());
        mAirDialogBinding.tvRowPriceAsiaAirSurcharge.setText(air.getSurcharge());
        mAirDialogBinding.tvRowPriceAsiaAirAirlines.setText(air.getAirlines());
        mAirDialogBinding.tvRowPriceAsiaAirSchedule.setText(air.getSchedule());
        mAirDialogBinding.tvRowPriceAsiaAirTransittime.setText(air.getTransittime());
        mAirDialogBinding.tvRowPriceAsiaAirValid.setText(air.getValid());
        mAirDialogBinding.tvRowPriceAsiaAirNote.setText(air.getNote());
        mAirDialogBinding.tvRowPriceAsiaAirCreateDate.setText(air.getDate_created());
    }
    public static FragmentAirDetail getInstance(){

        return new FragmentAirDetail();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_air:
                DialogFragment fragment = UpdateAirDialog.getInstance();
                fragment.setArguments(bundle);
                fragment.show(getParentFragmentManager(), "Update");
                break;
        }
    }
}