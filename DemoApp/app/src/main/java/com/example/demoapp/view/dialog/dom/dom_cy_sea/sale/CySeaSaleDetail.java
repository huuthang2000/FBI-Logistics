package com.example.demoapp.view.dialog.dom.dom_cy_sea.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentCySeaSaleDetailBinding;
import com.example.demoapp.model.DomCySea;
import com.example.demoapp.utilities.Constants;

public class CySeaSaleDetail extends DialogFragment {
    private FragmentCySeaSaleDetailBinding binding;
    private Bundle bundle;

    public static CySeaSaleDetail getInstance(){
        return  new CySeaSaleDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCySeaSaleDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setData();

        return view;
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