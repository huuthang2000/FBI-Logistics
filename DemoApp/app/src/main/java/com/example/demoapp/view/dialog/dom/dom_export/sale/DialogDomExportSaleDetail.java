package com.example.demoapp.view.dialog.dom.dom_export.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentDialogDomExportSaleDetailBinding;
import com.example.demoapp.model.DomExport;
import com.example.demoapp.utilities.Constants;

public class DialogDomExportSaleDetail extends DialogFragment {
    private FragmentDialogDomExportSaleDetailBinding binding;
    private Bundle bundle;

    public static  DialogDomExportSaleDetail getInstance(){
        return new DialogDomExportSaleDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDialogDomExportSaleDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setData();
        return view;
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomExport domExport = (DomExport) bundle.getSerializable(Constants.DOM_EXPORT_OBJECT);

            bundle.putSerializable(Constants.DOM_EXPORT_UPDATE, domExport);
            bundle.putString(Constants.DOM_EXPORT_ADD_NEW, "YES");


            binding.tvDomExportProductName.setText(domExport.getName());
            binding.tvDomExportWeight.setText(domExport.getWeight());
            binding.tvDomExportQuantity.setText(domExport.getQuantity());
            binding.tvDomExportTemp.setText(domExport.getTemp());
            binding.tvDomExportAddress.setText(domExport.getAddress());
            binding.tvDomExportSeaport.setText(domExport.getPortExport());
            binding.tvDomExportLength.setText(domExport.getLength());
            binding.tvRowDomExportHeight.setText(domExport.getHeight());
            binding.tvRowDomExportWidth.setText(domExport.getWidth());
            binding.tvRowDomExportCreated.setText(domExport.getCreatedDate());
        }
    }
}