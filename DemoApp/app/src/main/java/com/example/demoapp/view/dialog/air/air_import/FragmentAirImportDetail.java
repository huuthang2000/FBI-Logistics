package com.example.demoapp.view.dialog.air.air_import;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentAirImportDetailBinding;
import com.example.demoapp.model.AirImport;
import com.example.demoapp.utilities.Constants;


public class FragmentAirImportDetail extends DialogFragment implements View.OnClickListener {
    private FragmentAirImportDetailBinding mAirImportDetailBinding;
    private Bundle mBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAirImportDetailBinding = FragmentAirImportDetailBinding.inflate(inflater, container, false);
        View view = mAirImportDetailBinding.getRoot();

        mBundle = getArguments();
        if(mBundle != null){
            AirImport airImport = (AirImport) mBundle.getSerializable(Constants.AIR_IMPORT);
            mBundle.putSerializable(Constants.AIR_IMPORT_UPDATE, airImport);
            mBundle.putSerializable(Constants.AIR_IMPORT_INSERT, airImport);
            setDataAirImport(airImport);
        }
        mAirImportDetailBinding.btnUpdateAirImport.setOnClickListener(this);
        return view;
    }

    private void setDataAirImport(AirImport air) {
        mAirImportDetailBinding.tvRowPriceAirSttImport.setText(air.getStt());
        mAirImportDetailBinding.tvRowPriceAirPolImport.setText(air.getAol());
        mAirImportDetailBinding.tvRowPriceAirPodImport.setText(air.getAod());
        mAirImportDetailBinding.tvRowPriceAirDimImport.setText(air.getDim());
        mAirImportDetailBinding.tvRowPriceAirGrossweightImport.setText(air.getGrossweight());
        mAirImportDetailBinding.tvRowPriceAirTypeofcargoImport.setText(air.getTypeofcargo());
        mAirImportDetailBinding.tvRowPriceAirFreightImport.setText(air.getAirfreight());
        mAirImportDetailBinding.tvRowPriceAirSurchargeImport.setText(air.getSurcharge());
        mAirImportDetailBinding.tvRowPriceAirAirlinesImport.setText(air.getAirlines());
        mAirImportDetailBinding.tvRowPriceAirScheduleImport.setText(air.getSchedule());
        mAirImportDetailBinding.tvRowPriceAirTransittimeImport.setText(air.getTransittime());
        mAirImportDetailBinding.tvRowPriceAirValidImport.setText(air.getValid());
        mAirImportDetailBinding.tvRowPriceAirNoteImport.setText(air.getNote());
        mAirImportDetailBinding.tvRowPriceAirDateCreated.setText(air.getDate_created());


    }

    public static FragmentAirImportDetail getInstance(){
        return new FragmentAirImportDetail();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_air_import:
                DialogFragment fragment = UpdateAirImportDialog.getInstance();
                fragment.setArguments(mBundle);
                fragment.show(getParentFragmentManager(),"Update");
                break;
//            case R.id.btn_insert_air_import:
//                DialogFragment fragment1 = InsertAirImportDialog.insertDiaLogAIRImport();
//                fragment1.setArguments(mBundle);
//                fragment1.show(getParentFragmentManager(), "insert");
//                break;

        }
    }
}