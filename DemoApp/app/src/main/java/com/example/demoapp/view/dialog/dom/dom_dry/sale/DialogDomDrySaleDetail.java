package com.example.demoapp.view.dialog.dom.dom_dry.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentDialogDomDrySaleDetailBinding;
import com.example.demoapp.model.DomDry;
import com.example.demoapp.utilities.Constants;


public class DialogDomDrySaleDetail extends DialogFragment {

    private FragmentDialogDomDrySaleDetailBinding binding;
    private Bundle bundle;

    public static DialogDomDrySaleDetail getInstance(){
        return new DialogDomDrySaleDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDialogDomDrySaleDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setData();
        return view;
    }
    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomDry domDry = (DomDry) bundle.getSerializable(Constants.DOM_DRY_OBJECT);

            bundle.putSerializable(Constants.DOM_DRY_UPDATE, domDry);
            bundle.putString(Constants.DOM_DRY_ADD_NEW, "YES");

            binding.tvDomDryProductName.setText(domDry.getName());
            binding.tvDomDryWeight.setText(domDry.getWeight());
            binding.tvDomDryQuantityPallet.setText(domDry.getQuantityPallet());
            binding.tvDomDryQuantityCarton.setText(domDry.getQuantityCarton());
            binding.tvDomDryAddressReceive.setText(domDry.getAddressReceive());
            binding.tvDomDryAddressDelivery.setText(domDry.getAddressDelivery());
            binding.tvDomDryLength.setText(domDry.getLength());
            binding.tvRowDomDryHeight.setText(domDry.getHeight());
            binding.tvRowDomDryWidth.setText(domDry.getWidth());
            binding.tvRowDomDryCreated.setText(domDry.getCreatedDate());
        }
    }
}