package com.example.demoapp.view.dialog.dom.dom_cold;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.DialogDomColdDetailBinding;
import com.example.demoapp.model.DomCold;
import com.example.demoapp.utilities.Constants;

public class DialogDomColdDetail extends DialogFragment {

    private DialogDomColdDetailBinding binding;
    private Bundle bundle;

    // TODO: Rename and change types and number of parameters
    public static DialogDomColdDetail getInstance() {
        return new DialogDomColdDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomColdDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setData();
        setListenerForButtons();

        return root;
    }

    public void setListenerForButtons() {
        binding.btnUpdateDomCold.setOnClickListener(view -> {
            DialogFragment fragment = DialogDomColdUpdate.getInstance();
            fragment.setArguments(bundle);
            fragment.show(getParentFragmentManager(), "UpdateDomCold");
        });

        binding.btnAddNewDomCold.setOnClickListener(view -> {
            DialogFragment fragment2 = DialogDomColdInsert.getInstance();
            fragment2.setArguments(bundle);
            fragment2.show(getParentFragmentManager(), "AddNewDomCold");
        });
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomCold domCold = (DomCold) bundle.getSerializable(Constants.DOM_COLD_OBJECT);

            bundle.putSerializable(Constants.DOM_COLD_UPDATE, domCold);
            bundle.putString(Constants.DOM_COLD_ADD_NEW, "YES");
            binding.tvDomColdStt.setText(domCold.getStt());
            binding.tvDomColdProductName.setText(domCold.getName());
            binding.tvDomColdWeight.setText(domCold.getWeight());
            binding.tvDomColdQuantityPallet.setText(domCold.getQuantityPallet());
            binding.tvDomColdQuantityCarton.setText(domCold.getQuantityCarton());
            binding.tvDomColdAddressReceive.setText(domCold.getAddressReceive());
            binding.tvDomColdAddressDelivery.setText(domCold.getAddressDelivery());
            binding.tvDomColdLength.setText(domCold.getLength());
            binding.tvRowDomColdHeight.setText(domCold.getHeight());
            binding.tvRowDomColdWidth.setText(domCold.getWidth());
            binding.tvRowDomColdCreated.setText(domCold.getCreatedDate());
        }
    }
}