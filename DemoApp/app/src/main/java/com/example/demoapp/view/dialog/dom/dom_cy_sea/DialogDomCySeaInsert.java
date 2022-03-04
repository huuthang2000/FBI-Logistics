package com.example.demoapp.view.dialog.dom.dom_cy_sea;

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
import com.example.demoapp.databinding.DialogDomCySeaInsertBinding;
import com.example.demoapp.model.DomCySea;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomCySeaViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomCySeaInsert extends DialogFragment implements View.OnClickListener {

    private DialogDomCySeaInsertBinding binding;
    private Bundle bundle;

    private final String[] listStr = new String[3];

    private String portGo, portCome, name, weight, quantity, etd;

    private DomCySeaViewModel mDomCySeaViewModel;
    private CommunicateViewModel communicateViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogDomCySeaInsertBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);
        mDomCySeaViewModel = new ViewModelProvider(this).get(DomCySeaViewModel.class);

        setUpViews();
        textWatcher();
        setData();

        return view;
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomCySea mDomCySea = (DomCySea) bundle.getSerializable(Constants.DOM_CY_SEA_UPDATE);
            if ("YES".equalsIgnoreCase(bundle.getString(Constants.DOM_CY_SEA_ADD_NEW))) {
                binding.domCySeaInsertAutoContainer.setText(mDomCySea.getType());
                binding.domCySeaInsertAutoMonth.setText(mDomCySea.getMonth());
                binding.domCySeaInsertAutoContinent.setText(mDomCySea.getContinent());

                listStr[0] = binding.domCySeaInsertAutoContainer.getText().toString();
                listStr[1] = binding.domCySeaInsertAutoMonth.getText().toString();
                listStr[2] = binding.domCySeaInsertAutoContinent.getText().toString();

                Objects.requireNonNull(binding.insertDomCySeaPortGo.getEditText()).setText(mDomCySea.getPortGo());
                Objects.requireNonNull(binding.insertDomCySeaPortCome.getEditText()).setText(mDomCySea.getPortCome());
                Objects.requireNonNull(binding.insertDomCySeaName.getEditText()).setText(mDomCySea.getName());
                Objects.requireNonNull(binding.insertDomCySeaWeight.getEditText()).setText(mDomCySea.getWeight());
                Objects.requireNonNull(binding.insertDomCySeaQuantity.getEditText()).setText(mDomCySea.getQuantity());
                Objects.requireNonNull(binding.insertDomCySeaEtd.getEditText()).setText(mDomCySea.getEtd());

            }
        }
    }

    public static DialogDomCySeaInsert getInstance() {

        return new DialogDomCySeaInsert();
    }

    private void setUpViews() {

        binding.btnDomCySeaInsert.setOnClickListener(this);
        binding.btnDomCySeaCancel.setOnClickListener(this);

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_DOM_SEA);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.domCySeaInsertAutoContainer.setAdapter(adapterItemsType);
        binding.domCySeaInsertAutoMonth.setAdapter(adapterItemsMonth);
        binding.domCySeaInsertAutoContinent.setAdapter(adapterItemsContinent);

        binding.domCySeaInsertAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domCySeaInsertAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domCySeaInsertAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
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
            case R.id.btn_dom_cy_sea_insert:
                if (isFilled()) {
                    insertData();
                    dismiss();
                } else
                    Toast.makeText(getContext(), Constants.INSERT_FAILED, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_dom_cy_sea_cancel:
                dismiss();
                break;
        }
    }

    public void getDataFromForm() {
        portGo = Objects.requireNonNull(binding.insertDomCySeaPortGo.getEditText()).getText().toString();
        portCome = Objects.requireNonNull(binding.insertDomCySeaPortCome.getEditText()).getText().toString();
        name = Objects.requireNonNull(binding.insertDomCySeaName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.insertDomCySeaWeight.getEditText()).getText().toString();
        quantity = Objects.requireNonNull(binding.insertDomCySeaQuantity.getEditText()).getText().toString();
        etd = Objects.requireNonNull(binding.insertDomCySeaEtd.getEditText()).getText().toString();
    }

    public void insertData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomCySeaViewModel.insertData(portGo, portCome, name, weight, quantity, etd,
                listStr[0], listStr[1], listStr[2], getCreatedDate()).enqueue(new Callback<DomCySea>() {
            @Override
            public void onResponse(@NonNull Call<DomCySea> call, @NonNull Response<DomCySea> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Insert Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomCySea> call, @NonNull Throwable t) {

            }
        });
    }

    public boolean isFilled() {
        boolean result = true;

        if (TextUtils.isEmpty(binding.domCySeaInsertAutoContainer.getText())) {
            result = false;
            binding.domCySeaInsertAutoContainer.setError(Constants.ERROR_AUTO_COMPLETE_TYPE);
        }

        if (TextUtils.isEmpty(binding.domCySeaInsertAutoMonth.getText())) {
            result = false;
            binding.domCySeaInsertAutoMonth.setError(Constants.ERROR_AUTO_COMPLETE_MONTH);
        }

        if (TextUtils.isEmpty(binding.domCySeaInsertAutoContinent.getText())) {
            result = false;
            binding.domCySeaInsertAutoContinent.setError(Constants.ERROR_AUTO_COMPLETE_CONTINENT);
        }

        return result;
    }

    /**
     * If this field is not empty, set null for error
     */
    public void textWatcher() {

        binding.domCySeaInsertAutoContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domCySeaInsertAutoContainer.getText())) {
                    binding.domCySeaInsertAutoContainer.setError(null);

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domCySeaInsertAutoMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domCySeaInsertAutoMonth.getText())) {
                    binding.domCySeaInsertAutoMonth.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domCySeaInsertAutoContinent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domCySeaInsertAutoContinent.getText())) {
                    binding.domCySeaInsertAutoContinent.setError(null);
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
