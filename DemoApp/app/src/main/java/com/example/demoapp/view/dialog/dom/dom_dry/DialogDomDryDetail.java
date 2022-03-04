package com.example.demoapp.view.dialog.dom.dom_dry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.DialogDomDryDetailBinding;
import com.example.demoapp.model.DomDry;
import com.example.demoapp.utilities.Constants;

public class DialogDomDryDetail extends DialogFragment {

    private DialogDomDryDetailBinding binding;
    private Bundle bundle;

    // TODO: Rename and change types and number of parameters
    public static DialogDomDryDetail getInstance() {
        return new DialogDomDryDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomDryDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setData();
        setListenerForButtons();

        return root;
    }

    public void setListenerForButtons() {
        binding.btnUpdateDomDry.setOnClickListener(view -> {
            DialogFragment fragment = DialogDomDryUpdate.getInstance();
            fragment.setArguments(bundle);
            fragment.show(getParentFragmentManager(), "UpdateDomDry");
        });

        binding.btnAddNewDomDry.setOnClickListener(view -> {
            DialogFragment fragment2 = DialogDomDryInsert.getInstance();
            fragment2.setArguments(bundle);
            fragment2.show(getParentFragmentManager(), "AddNewDomDry");
        });
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomDry domDry = (DomDry) bundle.getSerializable(Constants.DOM_DRY_OBJECT);

            bundle.putSerializable(Constants.DOM_DRY_UPDATE, domDry);
            bundle.putString(Constants.DOM_DRY_ADD_NEW, "YES");

            binding.tvDomDryProductStt.setText(domDry.getStt());
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