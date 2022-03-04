package com.example.demoapp.view.dialog.dom.dom_export;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentDialogDomExportDetailBinding;
import com.example.demoapp.model.DomExport;
import com.example.demoapp.utilities.Constants;

public class DialogDomExportDetail extends DialogFragment {

    private FragmentDialogDomExportDetailBinding binding;
    private Bundle bundle;

    // TODO: Rename and change types and number of parameters
    public static DialogDomExportDetail getInstance() {
        return new DialogDomExportDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDialogDomExportDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setData();
        setListenerForButtons();

        return root;
    }

    public void setListenerForButtons() {
        binding.btnUpdateDomExport.setOnClickListener(view -> {
            DialogFragment fragment = DialogDomExportUpdate.getInstance();
            fragment.setArguments(bundle);
            fragment.show(getParentFragmentManager(), "UpdateDomExport");
        });

        binding.btnAddNewDomExport.setOnClickListener(view -> {
            DialogFragment fragment2 = DialogDomExportInsert.getInstance();
            fragment2.setArguments(bundle);
            fragment2.show(getParentFragmentManager(), "AddNewDomExport");
        });
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomExport domExport = (DomExport) bundle.getSerializable(Constants.DOM_EXPORT_OBJECT);

            bundle.putSerializable(Constants.DOM_EXPORT_UPDATE, domExport);
            bundle.putString(Constants.DOM_EXPORT_ADD_NEW, "YES");

            binding.tvDomExportProductStt.setText(domExport.getStt());
            binding.tvDomExportProductName.setText(domExport.getName());
            binding.tvDomExportWeight.setText(domExport.getWeight());
            binding.tvDomExportQuantity.setText(domExport.getQuantity());
            binding.tvDomExportTemp.setText(domExport.getTemp());
            binding.tvDomExportAddress.setText(domExport.getAddress());
            binding.tvDomExportSeaport.setText(domExport.getPortExport());
            binding.tvDomExportLength.setText(domExport.getLength());
            binding.tvRowDomExportHeight.setText(domExport.getHeight());
            binding.tvRowDomExportWidth.setText(domExport.getWidth());
            binding.tvRowDomExportCreated.setText(domExport.getCreatedDate());
        }
    }
}