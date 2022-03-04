package com.example.demoapp.view.dialog.dom.dom_door;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.DialogDomDoorDetailBinding;
import com.example.demoapp.model.DomDoor;
import com.example.demoapp.utilities.Constants;

public class DialogDomDoorDetail extends DialogFragment {

    private DialogDomDoorDetailBinding binding;
    private Bundle bundle;

    // TODO: Rename and change types and number of parameters
    public static DialogDomDoorDetail getInstance() {
        return new DialogDomDoorDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomDoorDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setData();
        setListenerForButtons();

        return root;
    }

    public void setListenerForButtons() {
        binding.btnUpdateDomDoor.setOnClickListener(view -> {
            DialogFragment fragment = DialogDomDoorUpdate.getInstance();
            fragment.setArguments(bundle);
            fragment.show(getParentFragmentManager(), "UpdateDomDoor");
        });

        binding.btnAddNewDomDoor.setOnClickListener(view -> {
            DialogFragment fragment2 = DialogDomDoorInsert.getInstance();
            fragment2.setArguments(bundle);
            fragment2.show(getParentFragmentManager(), "AddNewDomDoor");
        });
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomDoor mDomDoor = (DomDoor) bundle.getSerializable(Constants.DOM_DOOR_OBJECT);

            bundle.putSerializable(Constants.DOM_DOOR_UPDATE, mDomDoor);
            bundle.putString(Constants.DOM_DOOR_ADD_NEW, "YES");

            binding.tvDomDoorStationStt.setText(mDomDoor.getStt());
            binding.tvDomDoorStationGo.setText(mDomDoor.getStationGo());
            binding.tvDomDoorStationCome.setText(mDomDoor.getStationCome());
            binding.tvDomDoorAddressReceive.setText(mDomDoor.getAddressReceive());
            binding.tvDomDoorAddressDelivery.setText(mDomDoor.getAddressDelivery());
            binding.tvDomDoorName.setText(mDomDoor.getName());
            binding.tvDomDoorWeight.setText(mDomDoor.getWeight());
            binding.tvDomDoorQuantity.setText(mDomDoor.getQuantity());
            binding.tvDomDoorEtd.setText(mDomDoor.getEtd());
            binding.tvRowDomDoorCreated.setText(mDomDoor.getCreatedDate());
        }
    }
}