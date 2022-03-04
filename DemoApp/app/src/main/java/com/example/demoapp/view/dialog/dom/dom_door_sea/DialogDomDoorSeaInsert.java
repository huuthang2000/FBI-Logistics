package com.example.demoapp.view.dialog.dom.dom_door_sea;

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
import com.example.demoapp.databinding.DialogDomDoorSeaInsertBinding;
import com.example.demoapp.model.DomDoorSea;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomDoorSeaViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomDoorSeaInsert extends DialogFragment implements View.OnClickListener {

    private DialogDomDoorSeaInsertBinding binding;

    private final String[] listStr = new String[3];

    private String portGo, portCome, addressReceive, addressDelivery, name, weight, quantity, etd;

    private DomDoorSeaViewModel mDomDoorSeaViewModel;
    private CommunicateViewModel communicateViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogDomDoorSeaInsertBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);
        mDomDoorSeaViewModel = new ViewModelProvider(this).get(DomDoorSeaViewModel.class);

        setUpViews();
        textWatcher();
        setData();

        return view;
    }

    public void setData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            DomDoorSea mDomDoorSea = (DomDoorSea) bundle.getSerializable(Constants.DOM_DOOR_SEA_UPDATE);
            if ("YES".equalsIgnoreCase(bundle.getString(Constants.DOM_DOOR_SEA_ADD_NEW))) {
                binding.domDoorSeaInsertAutoContainer.setText(mDomDoorSea.getType());
                binding.domDoorSeaInsertAutoMonth.setText(mDomDoorSea.getMonth());
                binding.domDoorSeaInsertAutoContinent.setText(mDomDoorSea.getContinent());

                listStr[0] = binding.domDoorSeaInsertAutoContainer.getText().toString();
                listStr[1] = binding.domDoorSeaInsertAutoMonth.getText().toString();
                listStr[2] = binding.domDoorSeaInsertAutoContinent.getText().toString();

                Objects.requireNonNull(binding.insertDomDoorSeaPortGo.getEditText()).setText(mDomDoorSea.getPortGo());
                Objects.requireNonNull(binding.insertDomDoorSeaPortCome.getEditText()).setText(mDomDoorSea.getPortCome());
                Objects.requireNonNull(binding.insertDomDoorSeaAddressReceive.getEditText()).setText(mDomDoorSea.getAddressReceive());
                Objects.requireNonNull(binding.insertDomDoorSeaAddressDelivery.getEditText()).setText(mDomDoorSea.getAddressDelivery());
                Objects.requireNonNull(binding.insertDomDoorSeaName.getEditText()).setText(mDomDoorSea.getName());
                Objects.requireNonNull(binding.insertDomDoorSeaWeight.getEditText()).setText(mDomDoorSea.getWeight());
                Objects.requireNonNull(binding.insertDomDoorSeaQuantity.getEditText()).setText(mDomDoorSea.getQuantity());
                Objects.requireNonNull(binding.insertDomDoorSeaEtd.getEditText()).setText(mDomDoorSea.getEtd());

            }
        }
    }

    public static DialogDomDoorSeaInsert getInstance() {

        return new DialogDomDoorSeaInsert();
    }

    private void setUpViews() {

        binding.btnDomDoorSeaInsert.setOnClickListener(this);
        binding.btnDomDoorSeaCancel.setOnClickListener(this);

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_DOM_SEA);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.domDoorSeaInsertAutoContainer.setAdapter(adapterItemsType);
        binding.domDoorSeaInsertAutoMonth.setAdapter(adapterItemsMonth);
        binding.domDoorSeaInsertAutoContinent.setAdapter(adapterItemsContinent);

        binding.domDoorSeaInsertAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domDoorSeaInsertAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domDoorSeaInsertAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[2] = adapterView.getItemAtPosition(i).toString());

        setCancelable(false);
    }

    /**
     * Get current date and time
     *
     * @return current date and time
     */
    public String getCreatedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_dom_door_sea_insert:
                if (isFilled()) {
                    insertData();
                    dismiss();
                } else
                    Toast.makeText(getContext(), Constants.INSERT_FAILED, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_dom_door_sea_cancel:
                dismiss();
                break;
        }
    }

    public void getDataFromForm() {
        portGo = Objects.requireNonNull(binding.insertDomDoorSeaPortGo.getEditText()).getText().toString();
        portCome = Objects.requireNonNull(binding.insertDomDoorSeaPortCome.getEditText()).getText().toString();
        addressReceive = Objects.requireNonNull(binding.insertDomDoorSeaAddressReceive.getEditText()).getText().toString();
        addressDelivery = Objects.requireNonNull(binding.insertDomDoorSeaAddressDelivery.getEditText()).getText().toString();
        name = Objects.requireNonNull(binding.insertDomDoorSeaName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.insertDomDoorSeaWeight.getEditText()).getText().toString();
        quantity = Objects.requireNonNull(binding.insertDomDoorSeaQuantity.getEditText()).getText().toString();
        etd = Objects.requireNonNull(binding.insertDomDoorSeaEtd.getEditText()).getText().toString();
    }

    public void insertData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomDoorSeaViewModel.insertData(portGo, portCome, addressReceive, addressDelivery, name, weight, quantity, etd,
                listStr[0], listStr[1], listStr[2], getCreatedDate()).enqueue(new Callback<DomDoorSea>() {
            @Override
            public void onResponse(@NonNull Call<DomDoorSea> call, @NonNull Response<DomDoorSea> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Insert Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomDoorSea> call, @NonNull Throwable t) {

            }
        });
    }

    public boolean isFilled() {
        boolean result = true;

        if (TextUtils.isEmpty(binding.domDoorSeaInsertAutoContainer.getText())) {
            result = false;
            binding.domDoorSeaInsertAutoContinent.setError(Constants.ERROR_AUTO_COMPLETE_TYPE);
        }

        if (TextUtils.isEmpty(binding.domDoorSeaInsertAutoMonth.getText())) {
            result = false;
            binding.domDoorSeaInsertAutoMonth.setError(Constants.ERROR_AUTO_COMPLETE_MONTH);
        }

        if (TextUtils.isEmpty(binding.domDoorSeaInsertAutoContinent.getText())) {
            result = false;
            binding.domDoorSeaInsertAutoContinent.setError(Constants.ERROR_AUTO_COMPLETE_CONTINENT);
        }

        return result;
    }

    /**
     * If this field is not empty, set null for error
     */
    public void textWatcher() {

        binding.domDoorSeaInsertAutoContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domDoorSeaInsertAutoContainer.getText())) {
                    binding.domDoorSeaInsertAutoContainer.setError(null);

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domDoorSeaInsertAutoMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domDoorSeaInsertAutoMonth.getText())) {
                    binding.domDoorSeaInsertAutoMonth.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domDoorSeaInsertAutoContinent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domDoorSeaInsertAutoContinent.getText())) {
                    binding.domDoorSeaInsertAutoContinent.setError(null);
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
