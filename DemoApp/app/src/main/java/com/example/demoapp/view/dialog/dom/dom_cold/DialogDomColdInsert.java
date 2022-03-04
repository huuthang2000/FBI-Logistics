package com.example.demoapp.view.dialog.dom.dom_cold;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.demoapp.databinding.DialogDomColdInsertBinding;
import com.example.demoapp.model.DomCold;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomColdViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomColdInsert extends DialogFragment implements View.OnClickListener {

    private DialogDomColdInsertBinding binding;
    private Bundle bundle;

    private final String[] listStr = new String[3];

    private String name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width;

    private DomColdViewModel mDomColdViewModel;
    private CommunicateViewModel communicateViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogDomColdInsertBinding.inflate(inflater, container, false);

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
            DomCold mDomCold = (DomCold) bundle.getSerializable(Constants.DOM_COLD_UPDATE);
            if ("YES".equalsIgnoreCase(bundle.getString(Constants.DOM_COLD_ADD_NEW))) {
                binding.domColdInsertAutoContainer.setText(mDomCold.getType());
                binding.domColdInsertAutoMonth.setText(mDomCold.getMonth());
                binding.domColdInsertAutoContinent.setText(mDomCold.getContinent());

                listStr[0] = binding.domColdInsertAutoContainer.getText().toString();
                listStr[1] = binding.domColdInsertAutoMonth.getText().toString();
                listStr[2] = binding.domColdInsertAutoContinent.getText().toString();

                Objects.requireNonNull(binding.insertDomColdName.getEditText()).setText(mDomCold.getName());
                Objects.requireNonNull(binding.insertDomColdWeight.getEditText()).setText(mDomCold.getWeight());
                Objects.requireNonNull(binding.insertDomColdQuantityPallet.getEditText()).setText(mDomCold.getQuantityPallet());
                Objects.requireNonNull(binding.insertDomColdQuantityCarton.getEditText()).setText(mDomCold.getQuantityCarton());
                Objects.requireNonNull(binding.insertDomColdAddressReceive.getEditText()).setText(mDomCold.getAddressReceive());
                Objects.requireNonNull(binding.insertDomColdAddressDelivery.getEditText()).setText(mDomCold.getAddressDelivery());
                Objects.requireNonNull(binding.insertDomColdLength.getEditText()).setText(mDomCold.getLength());
                Objects.requireNonNull(binding.insertDomColdHeight.getEditText()).setText(mDomCold.getHeight());
                Objects.requireNonNull(binding.insertDomColdWidth.getEditText()).setText(mDomCold.getWidth());
            }
        }
    }

    public static DialogDomColdInsert getInstance() {

        return new DialogDomColdInsert();
    }

    private void setUpViews() {

        binding.btnDomColdInsert.setOnClickListener(this);
        binding.btnDomColdCancel.setOnClickListener(this);
        mDomColdViewModel = new ViewModelProvider(this).get(DomColdViewModel.class);

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_TYPE_DOM_DRY);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.domColdInsertAutoContainer.setAdapter(adapterItemsType);
        binding.domColdInsertAutoMonth.setAdapter(adapterItemsMonth);
        binding.domColdInsertAutoContinent.setAdapter(adapterItemsContinent);

        binding.domColdInsertAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domColdInsertAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domColdInsertAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
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
            case R.id.btn_dom_cold_insert:
                if (isFilled()) {
                    insertData();
                    dismiss();
                } else
                    Toast.makeText(getContext(), Constants.INSERT_FAILED, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_dom_cold_cancel:
                dismiss();
                break;
        }
    }

    public void getDataFromForm() {
        name = Objects.requireNonNull(binding.insertDomColdName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.insertDomColdWeight.getEditText()).getText().toString();
        quantityPallet = Objects.requireNonNull(binding.insertDomColdQuantityPallet.getEditText()).getText().toString();
        quantityCarton = Objects.requireNonNull(binding.insertDomColdQuantityCarton.getEditText()).getText().toString();
        addressReceive = Objects.requireNonNull(binding.insertDomColdAddressReceive.getEditText()).getText().toString();
        addressDelivery = Objects.requireNonNull(binding.insertDomColdAddressDelivery.getEditText()).getText().toString();
        length = Objects.requireNonNull(binding.insertDomColdLength.getEditText()).getText().toString();
        height = Objects.requireNonNull(binding.insertDomColdHeight.getEditText()).getText().toString();
        width = Objects.requireNonNull(binding.insertDomColdWidth.getEditText()).getText().toString();
    }

    public void insertData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomColdViewModel.insertData(name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length,
                height, width, listStr[0], listStr[1], listStr[2], getCreatedDate()).enqueue(new Callback<DomCold>() {
            @Override
            public void onResponse(@NonNull Call<DomCold> call, @NonNull Response<DomCold> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Insert Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomCold> call, @NonNull Throwable t) {

            }
        });
    }

    public boolean isFilled() {
        boolean result = true;

        if (TextUtils.isEmpty(binding.domColdInsertAutoContainer.getText())) {
            result = false;
            binding.domColdInsertAutoContainer.setError(Constants.ERROR_AUTO_COMPLETE_TYPE);
        }

        if (TextUtils.isEmpty(binding.domColdInsertAutoMonth.getText())) {
            result = false;
            binding.domColdInsertAutoMonth.setError(Constants.ERROR_AUTO_COMPLETE_MONTH);
        }

        if (TextUtils.isEmpty(binding.domColdInsertAutoContinent.getText())) {
            result = false;
            binding.domColdInsertAutoContinent.setError(Constants.ERROR_AUTO_COMPLETE_CONTINENT);
        }

        return result;
    }

    /**
     * If this field is not empty, set null for error
     */
    public void textWatcher() {

        binding.domColdInsertAutoContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domColdInsertAutoContainer.getText())) {
                    binding.domColdInsertAutoContainer.setError(null);

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domColdInsertAutoMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domColdInsertAutoMonth.getText())) {
                    binding.domColdInsertAutoMonth.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domColdInsertAutoContinent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domColdInsertAutoContinent.getText())) {
                    binding.domColdInsertAutoContinent.setError(null);
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
