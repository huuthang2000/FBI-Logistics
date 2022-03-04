package com.example.demoapp.view.dialog.dom.dom_import;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.DialogDomImportDetailBinding;
import com.example.demoapp.model.DomImport;
import com.example.demoapp.utilities.Constants;


public class DialogDomImportDetail extends DialogFragment {

    private DialogDomImportDetailBinding binding;
    private Bundle bundle;

    // TODO: Rename and change types and number of parameters
    public static DialogDomImportDetail getInstance() {
        return new DialogDomImportDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomImportDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setData();
        setListenerForButtons();

        return root;
    }

    public void setListenerForButtons() {
        binding.btnUpdateDomImport.setOnClickListener(view -> {
            DialogFragment fragment = DialogDomImportUpdate.getInstance();
            fragment.setArguments(bundle);
            fragment.show(getParentFragmentManager(), "UpdateDomImport");
        });

        binding.btnAddNewDomImport.setOnClickListener(view -> {
            DialogFragment fragment2 = DialogDomImportInsert.getInstance();
            fragment2.setArguments(bundle);
            fragment2.show(getParentFragmentManager(), "AddNewDomImport");
        });
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomImport domImport = (DomImport) bundle.getSerializable(Constants.DOM_IMPORT_OBJECT);

            bundle.putSerializable(Constants.DOM_IMPORT_UPDATE, domImport);
            bundle.putString(Constants.DOM_IMPORT_ADD_NEW, "YES");

            binding.tvDomImportStt.setText(domImport.getStt());
            binding.tvDomImportProductName.setText(domImport.getName());
            binding.tvDomImportWeight.setText(domImport.getWeight());
            binding.tvDomImportQuantity.setText(domImport.getQuantity());
            binding.tvDomImportTemp.setText(domImport.getTemp());
            binding.tvDomImportAddress.setText(domImport.getAddress());
            binding.tvDomImportPortReceive.setText(domImport.getPortReceive());
            binding.tvDomImportLength.setText(domImport.getLength());
            binding.tvRowDomImportHeight.setText(domImport.getHeight());
            binding.tvRowDomImportWidth.setText(domImport.getWidth());
            binding.tvRowDomImportCreated.setText(domImport.getCreatedDate());
        }
    }
}