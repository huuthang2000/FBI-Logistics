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
import com.example.demoapp.databinding.DialogImportDetailBinding;
import com.example.demoapp.model.Import;
import com.example.demoapp.utilities.Constants;

public class FragmentImportDetail extends DialogFragment implements View.OnClickListener {
    private DialogImportDetailBinding binding;
    private Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = DialogImportDetailBinding.inflate(inflater, container, false);
       View root = binding.getRoot();

        bundle = getArguments();
        if(bundle != null){
            Import imp = (Import) bundle.getSerializable(Constants.IMPORT_OBJECT);
            setData(imp);
            bundle.putSerializable(Constants.IMPORT_UPDATE, imp);
            bundle.putString(Constants.IMPORT_ADD_NEW,"YES");
        }

        binding.btnUpdateImport.setOnClickListener(this);
        binding.btnAddNewImport.setOnClickListener(this);

       return root;
    }

    public void setData(Import imp){
        binding.tvRowPriceImportStt.setText(imp.getStt());
        binding.tvRowPriceImportPol.setText(imp.getPol());
        binding.tvRowPriceImportPod.setText(imp.getPod());

        binding.tvRowPriceImportOf20.setText(imp.getOf20());
        binding.tvRowPriceImportOf40.setText(imp.getOf40());
        binding.tvRowPriceImportOf45.setText(imp.getOf40());

        binding.tvRowPriceImportSur20.setText(imp.getSur20());
        binding.tvRowPriceImportSur40.setText(imp.getSur40());
        binding.tvRowPriceImportSur45.setText(imp.getSur45());

        binding.tvRowPriceImportSchedule.setText(imp.getSchedule());
        binding.tvRowPriceImportTotal.setText(imp.getTotalFreight());
        binding.tvRowPriceImportCarrier.setText(imp.getCarrier());
        binding.tvRowPriceImportSchedule.setText(imp.getSchedule());
        binding.tvRowPriceImportTransit.setText(imp.getTransitTime());
        binding.tvRowPriceImportFree.setText(imp.getFreeTime());
        binding.tvRowPriceImportValid.setText(imp.getValid());
        binding.tvRowPriceImportNote.setText(imp.getNote());

        binding.tvRowPriceImportCreated.setText(imp.getCreatedDate());
    }


    public static FragmentImportDetail getInstance(){
        return new FragmentImportDetail();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id  = view.getId();
        switch (id){
            case R.id.btn_update_import:
                DialogFragment dialog = UpdateImportDialog.getInstance();
                dialog.setArguments(bundle);
                dialog.show(getParentFragmentManager(), "Update Import");
                break;
            case R.id.btn_add_new_import:
                DialogFragment dialog1 = InsertImportDialog.insertDialog();
                dialog1.setArguments(bundle);
                dialog1.show(getParentFragmentManager(), "Add New Import");
                break;
        }
    }
}
