package com.example.demoapp.view.dialog.dom.dom_cy.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentDialogDomCySaleDetailBinding;
import com.example.demoapp.model.DomCy;
import com.example.demoapp.utilities.Constants;

public class DialogDomCySaleDetail extends DialogFragment {

    private FragmentDialogDomCySaleDetailBinding binding;
    private Bundle bundle;

    public static DialogDomCySaleDetail getInstance(){
        return  new DialogDomCySaleDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDialogDomCySaleDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setData();
        return view;
    }
    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomCy domCy = (DomCy) bundle.getSerializable(Constants.DOM_CY_OBJECT);

            bundle.putSerializable(Constants.DOM_CY_UPDATE, domCy);
            bundle.putString(Constants.DOM_CY_ADD_NEW, "YES");

            binding.tvDomCyStationGo.setText(domCy.getStationGo());
            binding.tvDomCyStationCome.setText(domCy.getStationCome());
            binding.tvDomCyName.setText(domCy.getName());
            binding.tvDomCyWeight.setText(domCy.getWeight());
            binding.tvDomCyQuantity.setText(domCy.getQuantity());
            binding.tvDomCyEtd.setText(domCy.getEtd());
            binding.tvRowDomCyCreated.setText(domCy.getCreatedDate());
        }
    }
}