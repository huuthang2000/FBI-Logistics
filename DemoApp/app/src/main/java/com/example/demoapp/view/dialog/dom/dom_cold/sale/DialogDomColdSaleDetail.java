package com.example.demoapp.view.dialog.dom.dom_cold.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentDialogDomColdSaleDetailBinding;
import com.example.demoapp.model.DomCold;
import com.example.demoapp.utilities.Constants;


public class DialogDomColdSaleDetail extends DialogFragment {

    private Bundle bundle;
    private FragmentDialogDomColdSaleDetailBinding binding;

    public static DialogDomColdSaleDetail getInstance(){
        return new DialogDomColdSaleDetail();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDialogDomColdSaleDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setData();
        return view;
    }


    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomCold domCold = (DomCold) bundle.getSerializable(Constants.DOM_COLD_OBJECT);

            bundle.putSerializable(Constants.DOM_COLD_UPDATE, domCold);
            bundle.putString(Constants.DOM_COLD_ADD_NEW, "YES");

            binding.tvDomColdSaleProductName.setText(domCold.getName());
            binding.tvDomColdSaleWeight.setText(domCold.getWeight());
            binding.tvDomColdSaleQuantityPallet.setText(domCold.getQuantityPallet());
            binding.tvDomColdSaleQuantityCarton.setText(domCold.getQuantityCarton());
            binding.tvDomColdSaleAddressReceive.setText(domCold.getAddressReceive());
            binding.tvDomColdSaleAddressDelivery.setText(domCold.getAddressDelivery());
            binding.tvDomColdSaleLength.setText(domCold.getLength());
            binding.tvRowDomColdSaleHeight.setText(domCold.getHeight());
            binding.tvRowDomColdSaleWidth.setText(domCold.getWidth());
            binding.tvRowDomColdSaleCreated.setText(domCold.getCreatedDate());
        }
    }

}