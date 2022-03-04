package com.example.demoapp.view.dialog.imp.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentImportSaleDetailBinding;
import com.example.demoapp.model.Import;
import com.example.demoapp.utilities.Constants;

public class FragmentImportSaleDetail extends DialogFragment {

    private FragmentImportSaleDetailBinding binding;
    private  Bundle bundle;

    public static FragmentImportSaleDetail getInstance(){
        return new FragmentImportSaleDetail();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImportSaleDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        bundle = getArguments();
        if(bundle != null){
            Import imp = (Import) bundle.getSerializable(Constants.IMPORT_OBJECT);
            setData(imp);
            bundle.putSerializable(Constants.IMPORT_UPDATE, imp);
            bundle.putString(Constants.IMPORT_ADD_NEW,"YES");
        }
        return view;
    }
    public void setData(Import imp){
        binding.tvRowPriceImportStt.setText(imp.getStt());
        binding.tvRowPriceImportPol.setText(imp.getPol());
        binding.tvRowPriceImportPod.setText(imp.getPod());

        binding.tvRowPriceImportOf20.setText(imp.getOf20());
        binding.tvRowPriceImportOf40.setText(imp.getOf40());
        binding.tvRowPriceImportOf45.setText(imp.getOf40());

        binding.tvRowPriceImportSur20.setText(imp.getSur20());
        binding.tvRowPriceImportSur40.setText(imp.getSur40());
        binding.tvRowPriceImportSur45.setText(imp.getSur45());

        binding.tvRowPriceImportSchedule.setText(imp.getSchedule());
        binding.tvRowPriceImportTotal.setText(imp.getTotalFreight());
        binding.tvRowPriceImportCarrier.setText(imp.getCarrier());
        binding.tvRowPriceImportSchedule.setText(imp.getSchedule());
        binding.tvRowPriceImportTransit.setText(imp.getTransitTime());
        binding.tvRowPriceImportFree.setText(imp.getFreeTime());
        binding.tvRowPriceImportValid.setText(imp.getValid());
        binding.tvRowPriceImportNote.setText(imp.getNote());

        binding.tvRowPriceImportCreated.setText(imp.getCreatedDate());
    }
}