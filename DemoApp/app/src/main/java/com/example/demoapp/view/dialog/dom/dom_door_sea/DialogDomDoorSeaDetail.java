package com.example.demoapp.view.dialog.dom.dom_door_sea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.DialogDomDoorSeaDetailBinding;
import com.example.demoapp.model.DomDoorSea;
import com.example.demoapp.utilities.Constants;

public class DialogDomDoorSeaDetail extends DialogFragment {

    private DialogDomDoorSeaDetailBinding binding;
    private Bundle bundle;

    public static DialogDomDoorSeaDetail getInstance() {
        return new DialogDomDoorSeaDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomDoorSeaDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setData();
        setListenerForButtons();

        return root;
    }

    public void setListenerForButtons() {
        binding.btnUpdateDomDoorSea.setOnClickListener(view -> {
            DialogFragment fragment = DialogDomDoorSeaUpdate.getInstance();
            fragment.setArguments(bundle);
            fragment.show(getParentFragmentManager(), "UpdateDomDoorSea");
        });

        binding.btnAddNewDomDoorSea.setOnClickListener(view -> {
            DialogFragment fragment2 = DialogDomDoorSeaInsert.getInstance();
            fragment2.setArguments(bundle);
            fragment2.show(getParentFragmentManager(), "AddNewDomDoorSea");
        });
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