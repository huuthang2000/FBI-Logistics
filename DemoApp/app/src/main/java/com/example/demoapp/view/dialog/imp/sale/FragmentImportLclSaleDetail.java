package com.example.demoapp.view.dialog.imp.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentImportLclSaleDetailBinding;
import com.example.demoapp.model.ImportLcl;
import com.example.demoapp.utilities.Constants;


public class FragmentImportLclSaleDetail extends DialogFragment {

    private FragmentImportLclSaleDetailBinding binding;
    private Bundle bundle;

    public static  FragmentImportLclSaleDetail getInstance(){
        return  new FragmentImportLclSaleDetail();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImportLclSaleDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        bundle = getArguments();
        if(bundle != null){
            ImportLcl imp = (ImportLcl) bundle.getSerializable(Constants.IMPORT_LCL_OBJECT);
            setData(imp);
            bundle.putSerializable(Constants.IMPORT_LCL_UPDATE, imp);
            bundle.putString(Constants.IMPORT_LCL_ADD_NEW,"YES");
        }
        return view;
    }
    public void setData(ImportLcl imp){
        binding.tvRowPriceImportLclStt.setText(imp.getStt());
        binding.tvRowPriceImportLclTerm.setText(imp.getTerm());
        binding.tvRowPriceImportLclPol.setText(imp.getPol());
        binding.tvRowPriceImportLclPod.setText(imp.getPod());

        binding.tvRowPriceImportLclCargo.setText(imp.getCargo());
        binding.tvRowPriceImportLclOf.setText(imp.getOf());
        binding.tvRowPriceImportLclLocalPol.setText(imp.getLocalPol());
        binding.tvRowPriceImportLclLocalPod.setText(imp.getLocalPod());

        binding.tvRowPriceImportLclSchedule.setText(imp.getSchedule());
        binding.tvRowPriceImportLclCarrier.setText(imp.getCarrier());

        binding.tvRowPriceImportLclTransit.setText(imp.getTransitTime());

        binding.tvRowPriceImportLclValid.setText(imp.getValid());
        binding.tvRowPriceImportLclNote.setText(imp.getNote());

        binding.tvRowPriceImportLclCreated.setText(imp.getCreatedDate());
    }
}