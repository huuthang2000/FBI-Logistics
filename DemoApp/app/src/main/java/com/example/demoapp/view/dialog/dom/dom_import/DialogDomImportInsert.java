package com.example.demoapp.view.dialog.dom.dom_import;

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
import com.example.demoapp.databinding.DialogDomImportInsertBinding;
import com.example.demoapp.model.DomImport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomImportViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomImportInsert extends DialogFragment{

    private DialogDomImportInsertBinding binding;
    private Bundle bundle;

    private final String[] listStr = new String[3];

    private String name, weight, quantity, temp, address, portReceive, length, height, width;

    private DomImportViewModel mDomImportViewModel;
    private CommunicateViewModel communicateViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogDomImportInsertBinding.inflate(inflater, container, false);

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
            DomImport mDomImport = (DomImport) bundle.getSerializable(Constants.DOM_IMPORT_UPDATE);
            if ("YES".equalsIgnoreCase(bundle.getString(Constants.DOM_IMPORT_ADD_NEW))) {

                binding.domImportInsertAutoContainer.setText(mDomImport.getType());
                binding.domImportInsertAutoMonth.setText(mDomImport.getMonth());
                binding.domImportInsertAutoContinent.setText(mDomImport.getContinent());

                listStr[0] = binding.domImportInsertAutoContainer.getText().toString();
                listStr[1] = binding.domImportInsertAutoMonth.getText().toString();
                listStr[2] = binding.domImportInsertAutoContinent.getText().toString();

                Objects.requireNonNull(binding.insertDomImportName.getEditText()).setText(mDomImport.getName());
                Objects.requireNonNull(binding.insertDomImportWeight.getEditText()).setText(mDomImport.getWeight());
                Objects.requireNonNull(binding.insertDomImportQuantity.getEditText()).setText(mDomImport.getQuantity());
                Objects.requireNonNull(binding.insertDomImportTemp.getEditText()).setText(mDomImport.getTemp());
                Objects.requireNonNull(binding.insertDomImportAddress.getEditText()).setText(mDomImport.getAddress());
                Objects.requireNonNull(binding.insertDomImportPort.getEditText()).setText(mDomImport.getPortReceive());
                Objects.requireNonNull(binding.insertDomImportLength.getEditText()).setText(mDomImport.getLength());
                Objects.requireNonNull(binding.insertDomImportHeight.getEditText()).setText(mDomImport.getHeight());
                Objects.requireNonNull(binding.insertDomImportWidth.getEditText()).setText(mDomImport.getWidth());
            }
        }
    }

    public static DialogDomImportInsert getInstance() {

        return new DialogDomImportInsert();
    }

    private void setUpViews() {

        binding.btnDomImportInsert.setOnClickListener(view -> {
            if (isFilled()) {
                insertData();
                dismiss();
            } else
                Toast.makeText(getContext(), Constants.INSERT_FAILED, Toast.LENGTH_LONG).show();
        });

        binding.btnDomImportCancel.setOnClickListener(view -> dismiss());


        mDomImportViewModel = new ViewModelProvider(this).get(DomImportViewModel.class);

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_DOM);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.domImportInsertAutoContainer.setAdapter(adapterItemsType);
        binding.domImportInsertAutoMonth.setAdapter(adapterItemsMonth);
        binding.domImportInsertAutoContinent.setAdapter(adapterItemsContinent);

        binding.domImportInsertAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domImportInsertAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domImportInsertAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
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

    public void getDataFromForm() {
        name = Objects.requireNonNull(binding.insertDomImportName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.insertDomImportWeight.getEditText()).getText().toString();
        quantity = Objects.requireNonNull(binding.insertDomImportQuantity.getEditText()).getText().toString();
        temp = Objects.requireNonNull(binding.insertDomImportTemp.getEditText()).getText().toString();
        address = Objects.requireNonNull(binding.insertDomImportAddress.getEditText()).getText().toString();
        portReceive = Objects.requireNonNull(binding.insertDomImportPort.getEditText()).getText().toString();
        length = Objects.requireNonNull(binding.insertDomImportLength.getEditText()).getText().toString();
        height = Objects.requireNonNull(binding.insertDomImportHeight.getEditText()).getText().toString();
        width = Objects.requireNonNull(binding.insertDomImportWidth.getEditText()).getText().toString();
    }

    public void insertData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomImportViewModel.insertData(name, weight, quantity, temp, address, portReceive, length,
                height, width, listStr[0], listStr[1], listStr[2], getCreatedDate()).enqueue(new Callback<DomImport>() {
            @Override
            public void onResponse(@NonNull Call<DomImport> call, @NonNull Response<DomImport> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Insert Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomImport> call, @NonNull Throwable t) {

            }
        });
    }

    public boolean isFilled() {
        boolean result = true;

        if (TextUtils.isEmpty(binding.domImportInsertAutoContainer.getText())) {
            result = false;
            binding.domImportInsertAutoContainer.setError(Constants.ERROR_AUTO_COMPLETE_TYPE);
        }

        if (TextUtils.isEmpty(binding.domImportInsertAutoMonth.getText())) {
            result = false;
            binding.domImportInsertAutoMonth.setError(Constants.ERROR_AUTO_COMPLETE_MONTH);
        }

        if (TextUtils.isEmpty(binding.domImportInsertAutoContinent.getText())) {
            result = false;
            binding.domImportInsertAutoContinent.setError(Constants.ERROR_AUTO_COMPLETE_CONTINENT);
        }

        return result;
    }

    /**
     * If this field is not empty, set null for error
     */
    public void textWatcher() {

        binding.domImportInsertAutoContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domImportInsertAutoContainer.getText())) {
                    binding.domImportInsertAutoContainer.setError(null);

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domImportInsertAutoMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domImportInsertAutoMonth.getText())) {
                    binding.domImportInsertAutoMonth.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domImportInsertAutoContinent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domImportInsertAutoContinent.getText())) {
                    binding.domImportInsertAutoContinent.setError(null);
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
