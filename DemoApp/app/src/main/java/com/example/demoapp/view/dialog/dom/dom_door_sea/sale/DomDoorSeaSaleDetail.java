package com.example.demoapp.view.dialog.dom.dom_door_sea.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentDomDoorSeaSaleDetailBinding;
import com.example.demoapp.model.DomDoorSea;
import com.example.demoapp.utilities.Constants;


public class DomDoorSeaSaleDetail extends DialogFragment {
    private FragmentDomDoorSeaSaleDetailBinding binding;
    private Bundle bundle;

    public static DomDoorSeaSaleDetail getInstance(){
        return new DomDoorSeaSaleDetail();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDomDoorSeaSaleDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setData();
        return view;
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomDoorSea mDomDoorSea = (DomDoorSea) bundle.getSerializable(Constants.DOM_DOOR_SEA_OBJECT);

            bundle.putSerializable(Constants.DOM_DOOR_SEA_UPDATE, mDomDoorSea);
            bundle.putString(Constants.DOM_DOOR_SEA_ADD_NEW, "YES");

            binding.tvDomDoorSeaPortStt.setText(mDomDoorSea.getStt());
            binding.tvDomDoorSeaPortGo.setText(mDomDoorSea.getPortGo());
            binding.tvDomDoorSeaPortCome.setText(mDomDoorSea.getPortCome());
            binding.tvDomDoorSeaAddressReceive.setText(mDomDoorSea.getAddressReceive());
            binding.tvDomDoorSeaAddressDelivery.setText(mDomDoorSea.getAddressDelivery());
            binding.tvDomDoorSeaName.setText(mDomDoorSea.getName());
            binding.tvDomDoorSeaWeight.setText(mDomDoorSea.getWeight());
            binding.tvDomDoorSeaQuantity.setText(mDomDoorSea.getQuantity());
            binding.tvDomDoorSeaEtd.setText(mDomDoorSea.getEtd());
            binding.tvRowDomDoorSeaCreated.setText(mDomDoorSea.getCreatedDate());
        }
    }
}