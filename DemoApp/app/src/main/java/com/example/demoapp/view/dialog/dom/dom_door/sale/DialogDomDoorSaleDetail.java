package com.example.demoapp.view.dialog.dom.dom_door.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentDialogDomDoorSaleDetailBinding;
import com.example.demoapp.model.DomDoor;
import com.example.demoapp.utilities.Constants;


public class DialogDomDoorSaleDetail extends DialogFragment {

    private FragmentDialogDomDoorSaleDetailBinding binding;
    private Bundle bundle;

    public static DialogDomDoorSaleDetail getInstance(){
        return new DialogDomDoorSaleDetail();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDialogDomDoorSaleDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setData();
        return view;
    }
    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomDoor mDomDoor = (DomDoor) bundle.getSerializable(Constants.DOM_DOOR_OBJECT);

            bundle.putSerializable(Constants.DOM_DOOR_UPDATE, mDomDoor);
            bundle.putString(Constants.DOM_DOOR_ADD_NEW, "YES");

            binding.tvDomDoorStationGo.setText(mDomDoor.getStationGo());
            binding.tvDomDoorStationCome.setText(mDomDoor.getStationCome());
            binding.tvDomDoorAddressReceive.setText(mDomDoor.getStationGo());
            binding.tvDomDoorAddressDelivery.setText(mDomDoor.getStationCome());
            binding.tvDomDoorName.setText(mDomDoor.getName());
            binding.tvDomDoorWeight.setText(mDomDoor.getWeight());
            binding.tvDomDoorQuantity.setText(mDomDoor.getQuantity());
            binding.tvDomDoorEtd.setText(mDomDoor.getEtd());
            binding.tvRowDomDoorCreated.setText(mDomDoor.getCreatedDate());
        }
    }
}