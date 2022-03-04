package com.example.demoapp.view.dialog.dom.dom_import.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentDialogDomImportSaleDetailBinding;
import com.example.demoapp.model.DomImport;
import com.example.demoapp.utilities.Constants;


public class DialogDomImportSaleDetail extends DialogFragment {

    private FragmentDialogDomImportSaleDetailBinding binding;
    private Bundle bundle;

    public static DialogDomImportSaleDetail getInstance(){
        return new DialogDomImportSaleDetail();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDialogDomImportSaleDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setData();

        return view;
    }
    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomImport domImport = (DomImport) bundle.getSerializable(Constants.DOM_IMPORT_OBJECT);

            bundle.putSerializable(Constants.DOM_IMPORT_UPDATE, domImport);
            bundle.putString(Constants.DOM_IMPORT_ADD_NEW, "YES");

            binding.tvDomImportProductName.setText(domImport.getName());
            binding.tvDomImportWeight.setText(domImport.getWeight());
            binding.tvDomImportQuantity.setText(domImport.getQuantity());
            binding.tvDomImportTemp.setText(domImport.getTemp());
            binding.tvDomImportAddress.setText(domImport.getAddress());
            binding.tvDomImportPortReceive.setText(domImport.getPortReceive());
            binding.tvDomImportLength.setText(domImport.getLength());
            binding.tvRowDomImportHeight.setText(domImport.getHeight());
            binding.tvRowDomImportWidth.setText(domImport.getWidth());
            binding.tvRowDomImportCreated.setText(domImport.getCreatedDate());
        }
    }
}