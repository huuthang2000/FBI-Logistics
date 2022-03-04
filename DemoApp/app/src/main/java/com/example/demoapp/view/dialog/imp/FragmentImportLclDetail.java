package com.example.demoapp.view.dialog.imp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.demoapp.R;
import com.example.demoapp.databinding.DialogImportLclDetailBinding;
import com.example.demoapp.model.ImportLcl;
import com.example.demoapp.utilities.Constants;

public class FragmentImportLclDetail extends DialogFragment implements View.OnClickListener {

    private DialogImportLclDetailBinding binding;
    private Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = DialogImportLclDetailBinding.inflate(inflater, container, false);
       View root = binding.getRoot();

        bundle = getArguments();
        if(bundle != null){
            ImportLcl imp = (ImportLcl) bundle.getSerializable(Constants.IMPORT_LCL_OBJECT);
            setData(imp);
            bundle.putSerializable(Constants.IMPORT_LCL_UPDATE, imp);
            bundle.putString(Constants.IMPORT_LCL_ADD_NEW,"YES");
        }

        binding.btnUpdateImportLcl.setOnClickListener(this);
        binding.btnAddNewImportLcl.setOnClickListener(this);

       return root;
    }

    public void setData(ImportLcl imp){
        binding.tvRowPriceImportLclStt.setText(imp.getStt());
        binding.tvRowPriceImportLclTerm.setText(imp.getTerm());
        binding.tvRowPriceImportLclPol.setText(imp.getPol());
        binding.tvRowPriceImportLclPod.setText(imp.getPod());

        binding.tvRowPriceImportLclCargo.setText(imp.getCargo());
        binding.tvRowPriceImportLclOf.setText(imp.getOf());
        binding.tvRowPriceImportLclLocalPol.setText(imp.getLocalPol());
        binding.tvRowPriceImportLclLocalPod.setText(imp.getLocalPod());

        binding.tvRowPriceImportLclSchedule.setText(imp.getSchedule());
        binding.tvRowPriceImportLclCarrier.setText(imp.getCarrier());

        binding.tvRowPriceImportLclTransit.setText(imp.getTransitTime());

        binding.tvRowPriceImportLclValid.setText(imp.getValid());
        binding.tvRowPriceImportLclNote.setText(imp.getNote());

        binding.tvRowPriceImportLclCreated.setText(imp.getCreatedDate());
    }


    public static FragmentImportLclDetail getInstance(){
        return new FragmentImportLclDetail();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id  = view.getId();
        switch (id){
            case R.id.btn_update_import_lcl:
                DialogFragment dialog = UpdateImportLclDialog.getInstance();
                dialog.setArguments(bundle);
                dialog.show(getParentFragmentManager(), "Update Import Lcl");
                break;
            case R.id.btn_add_new_import_lcl:
                DialogFragment dialog1 = InsertImportLclDialog.getInstance();
                dialog1.setArguments(bundle);
                dialog1.show(getParentFragmentManager(), "Add New Import Lcl");
                break;
        }
    }
}
