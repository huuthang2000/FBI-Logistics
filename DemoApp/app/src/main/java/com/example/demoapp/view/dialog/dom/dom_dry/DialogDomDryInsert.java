package com.example.demoapp.view.dialog.dom.dom_dry;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.demoapp.R;
import com.example.demoapp.databinding.DialogDomDryInsertBinding;
import com.example.demoapp.model.DomDry;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomDryViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomDryInsert extends DialogFragment implements View.OnClickListener {

    private DialogDomDryInsertBinding binding;
    private Bundle bundle;

    private final String[] listStr = new String[3];

    private String name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width;

    private DomDryViewModel mDomDryViewModel;
    private CommunicateViewModel communicateViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogDomDryInsertBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);

        setUpViews();
        textWatcher();
        setData();

        return view;
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomDry mDomDry = (DomDry) bundle.getSerializable(Constants.DOM_DRY_UPDATE);
            if ("YES".equalsIgnoreCase(bundle.getString(Constants.DOM_DRY_ADD_NEW))) {
                binding.domDryInsertAutoContainer.setText(mDomDry.getType());
                binding.domDryInsertAutoMonth.setText(mDomDry.getMonth());
                binding.domDryInsertAutoContinent.setText(mDomDry.getContinent());

                listStr[0] = binding.domDryInsertAutoContainer.getText().toString();
                listStr[1] = binding.domDryInsertAutoMonth.getText().toString();
                listStr[2] = binding.domDryInsertAutoContinent.getText().toString();

                Objects.requireNonNull(binding.insertDomDryName.getEditText()).setText(mDomDry.getName());
                Objects.requireNonNull(binding.insertDomDryWeight.getEditText()).setText(mDomDry.getWeight());
                Objects.requireNonNull(binding.insertDomDryQuantityPallet.getEditText()).setText(mDomDry.getQuantityPallet());
                Objects.requireNonNull(binding.insertDomDryQuantityCarton.getEditText()).setText(mDomDry.getQuantityCarton());
                Objects.requireNonNull(binding.insertDomDryAddressReceive.getEditText()).setText(mDomDry.getAddressReceive());
                Objects.requireNonNull(binding.insertDomDryAddressDelivery.getEditText()).setText(mDomDry.getAddressDelivery());
                Objects.requireNonNull(binding.insertDomDryLength.getEditText()).setText(mDomDry.getLength());
                Objects.requireNonNull(binding.insertDomDryHeight.getEditText()).setText(mDomDry.getHeight());
                Objects.requireNonNull(binding.insertDomDryWidth.getEditText()).setText(mDomDry.getWidth());
            }
        }
    }

    public static DialogDomDryInsert getInstance() {

        return new DialogDomDryInsert();
    }

    private void setUpViews() {

        binding.btnDomDryInsert.setOnClickListener(this);
        binding.btnDomDryCancel.setOnClickListener(this);
        mDomDryViewModel = new ViewModelProvider(this).get(DomDryViewModel.class);

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_TYPE_DOM_DRY);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.domDryInsertAutoContainer.setAdapter(adapterItemsType);
        binding.domDryInsertAutoMonth.setAdapter(adapterItemsMonth);
        binding.domDryInsertAutoContinent.setAdapter(adapterItemsContinent);

        binding.domDryInsertAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domDryInsertAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domDryInsertAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[2] = adapterView.getItemAtPosition(i).toString());

        setCancelable(false);
    }

    /**
     * Get current date and time
     *
     * @return current date and time
     */
    private String getCreatedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_dom_dry_insert:
                if (isFilled()) {
                    insertData();
                    dismiss();
                } else
                    Toast.makeText(getContext(), Constants.INSERT_FAILED, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_dom_dry_cancel:
                dismiss();
                break;
        }
    }

    public void getDataFromForm() {
        name = Objects.requireNonNull(binding.insertDomDryName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.insertDomDryWeight.getEditText()).getText().toString();
        quantityPallet = Objects.requireNonNull(binding.insertDomDryQuantityPallet.getEditText()).getText().toString();
        quantityCarton = Objects.requireNonNull(binding.insertDomDryQuantityCarton.getEditText()).getText().toString();
        addressReceive = Objects.requireNonNull(binding.insertDomDryAddressReceive.getEditText()).getText().toString();
        addressDelivery = Objects.requireNonNull(binding.insertDomDryAddressDelivery.getEditText()).getText().toString();
        length = Objects.requireNonNull(binding.insertDomDryLength.getEditText()).getText().toString();
        height = Objects.requireNonNull(binding.insertDomDryHeight.getEditText()).getText().toString();
        width = Objects.requireNonNull(binding.insertDomDryWidth.getEditText()).getText().toString();
    }

    public void insertData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomDryViewModel.insertData(name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length,
                height, width, listStr[0], listStr[1], listStr[2], getCreatedDate()).enqueue(new Callback<DomDry>() {
            @Override
            public void onResponse(@NonNull Call<DomDry> call, @NonNull Response<DomDry> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Insert Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomDry> call, @NonNull Throwable t) {

            }
        });
    }

    public boolean isFilled() {
        boolean result = true;

        if (TextUtils.isEmpty(binding.domDryInsertAutoContainer.getText())) {
            result = false;
            binding.domDryInsertAutoContainer.setError(Constants.ERROR_AUTO_COMPLETE_TYPE);
        }

        if (TextUtils.isEmpty(binding.domDryInsertAutoMonth.getText())) {
            result = false;
            binding.domDryInsertAutoMonth.setError(Constants.ERROR_AUTO_COMPLETE_MONTH);
        }

        if (TextUtils.isEmpty(binding.domDryInsertAutoContinent.getText())) {
            result = false;
            binding.domDryInsertAutoContinent.setError(Constants.ERROR_AUTO_COMPLETE_CONTINENT);
        }

        return result;
    }

    /**
     * If this field is not empty, set null for error
     */
    public void textWatcher() {

        binding.domDryInsertAutoContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domDryInsertAutoContainer.getText())) {
                    binding.domDryInsertAutoContainer.setError(null);

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domDryInsertAutoMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domDryInsertAutoMonth.getText())) {
                    binding.domDryInsertAutoMonth.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domDryInsertAutoContinent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domDryInsertAutoContinent.getText())) {
                    binding.domDryInsertAutoContinent.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
