package com.example.demoapp.view.dialog.dom.dom_cy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.DialogDomCyDetailBinding;
import com.example.demoapp.model.DomCy;
import com.example.demoapp.utilities.Constants;

public class DialogDomCyDetail extends DialogFragment {

    private DialogDomCyDetailBinding binding;
    private Bundle bundle;

    // TODO: Rename and change types and number of parameters
    public static DialogDomCyDetail getInstance() {
        return new DialogDomCyDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomCyDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setData();
        setListenerForButtons();

        return root;
    }

    public void setListenerForButtons() {
        binding.btnUpdateDomCy.setOnClickListener(view -> {
            DialogFragment fragment = DialogDomCyUpdate.getInstance();
            fragment.setArguments(bundle);
            fragment.show(getParentFragmentManager(), "UpdateDomCy");
        });

        binding.btnAddNewDomCy.setOnClickListener(view -> {
            DialogFragment fragment2 = DialogDomCyInsert.getInstance();
            fragment2.setArguments(bundle);
            fragment2.show(getParentFragmentManager(), "AddNewDomCy");
        });
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomCy domCy = (DomCy) bundle.getSerializable(Constants.DOM_CY_OBJECT);

            bundle.putSerializable(Constants.DOM_CY_UPDATE, domCy);
            bundle.putString(Constants.DOM_CY_ADD_NEW, "YES");
            binding.tvDomCyStationStt.setText(domCy.getStt());
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