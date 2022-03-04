package com.example.demoapp.view.dialog.fcl;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.demoapp.R;
import com.example.demoapp.databinding.DialogFclDetailBinding;
import com.example.demoapp.model.Fcl;
import com.example.demoapp.utilities.Constants;

public class FragmentFclDetail extends DialogFragment implements View.OnClickListener {
    private DialogFclDetailBinding binding;
    private Bundle bundle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFclDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        bundle = getArguments();
        if (bundle != null) {
            Fcl fcl = (Fcl) bundle.getSerializable(Constants.FCL_OBJECT);
            bundle.putSerializable(Constants.FCL_UPDATE, fcl);
            bundle.putString(Constants.FCL_ADD_NEW, "YES");
            setData(fcl);
        }
        binding.btnUpdateFcl.setOnClickListener(this);
        binding.btnAddNewFcl.setOnClickListener(this);
        return view;

    }

    /**
     * This method will set text for all text view
     * @param fcl model
     */
    public void setData(Fcl fcl) {
        binding.tvRowPriceAsiaStt.setText(fcl.getStt());
        binding.tvRowPriceAsiaPol.setText(fcl.getPol());
        binding.tvRowPriceAsiaPod.setText(fcl.getPod());
        binding.tvRowPriceAsiaOf20.setText(fcl.getOf20());
        binding.tvRowPriceAsiaOf40.setText(fcl.getOf40());
        binding.tvRowPriceAsiaOf45.setText(fcl.getOf45());
        binding.tvRowPriceAsiaSu20.setText(fcl.getSu20());
        binding.tvRowPriceAsiaSu40.setText(fcl.getSu40());
        binding.tvRowPriceAsiaLine.setText(fcl.getLinelist());
        binding.tvRowPriceAsiaNotes1.setText(fcl.getNotes());
        binding.tvRowPriceAsiaValid.setText(fcl.getValid());
        binding.tvRowPriceAsiaNotes2.setText(fcl.getNotes2());
        binding.tvRowPriceAsiaCreated.setText(fcl.getCreatedDate());

        String tvOf20 = getString(R.string.col_of2);
        String tvOf40 = getString(R.string.col_of4);
        String tvOf45 = getString(R.string.col_of45);

        binding.textView5.setText(tvOf20.concat("_" + fcl.getType()));
        binding.textView6.setText(tvOf40.concat("_" + fcl.getType()));
        binding.textView45.setText(tvOf45.concat("_" + fcl.getType()));
    }

    /**
     *
     * @return instance of this fragment
     */
    public static FragmentFclDetail getInstance() {
        return new FragmentFclDetail();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_fcl:
                DialogFragment fragment = UpdateFclDialog.getInstance();
                fragment.setArguments(bundle);
                fragment.show(getParentFragmentManager(), "Update");
                break;

            case R.id.btn_add_new_fcl:
                DialogFragment fragment1 = InsertFclDialog.insertDialog();
                fragment1.setArguments(bundle);
                fragment1.show(getParentFragmentManager(), "AddNew");
                break;

        }
    }

}
