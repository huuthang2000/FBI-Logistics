package com.example.demoapp.view.dialog.dom.dom_cy_sea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.DialogDomCySeaDetailBinding;
import com.example.demoapp.model.DomCySea;
import com.example.demoapp.utilities.Constants;

public class DialogDomCySeaDetail extends DialogFragment {

    private DialogDomCySeaDetailBinding binding;
    private Bundle bundle;

    public static DialogDomCySeaDetail getInstance() {
        return new DialogDomCySeaDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomCySeaDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setData();
        setListenerForButtons();

        return root;
    }

    public void setListenerForButtons() {
        binding.btnUpdateDomCySea.setOnClickListener(view -> {
            DialogFragment fragment = DialogDomCySeaUpdate.getInstance();
            fragment.setArguments(bundle);
            fragment.show(getParentFragmentManager(), "UpdateDomCySea");
        });

        binding.btnAddNewDomCySea.setOnClickListener(view -> {
            DialogFragment fragment2 = DialogDomCySeaInsert.getInstance();
            fragment2.setArguments(bundle);
            fragment2.show(getParentFragmentManager(), "AddNewDomCySea");
        });
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomCySea domCySea = (DomCySea) bundle.getSerializable(Constants.DOM_CY_SEA_OBJECT);

            bundle.putSerializable(Constants.DOM_CY_SEA_UPDATE, domCySea);
            bundle.putString(Constants.DOM_CY_SEA_ADD_NEW, "YES");

            binding.tvDomCySeaStationStt.setText(domCySea.getStt());
            binding.tvDomCySeaStationGo.setText(domCySea.getPortGo());
            binding.tvDomCySeaStationCome.setText(domCySea.getPortCome());
            binding.tvDomCySeaName.setText(domCySea.getName());
            binding.tvDomCySeaWeight.setText(domCySea.getWeight());
            binding.tvDomCySeaQuantity.setText(domCySea.getQuantity());
            binding.tvDomCySeaEtd.setText(domCySea.getEtd());
            binding.tvRowDomCySeaCreated.setText(domCySea.getCreatedDate());
        }
    }
}