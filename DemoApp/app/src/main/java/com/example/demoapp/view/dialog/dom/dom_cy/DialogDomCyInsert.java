package com.example.demoapp.view.dialog.dom.dom_cy;

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
import com.example.demoapp.databinding.DialogDomCyInsertBinding;
import com.example.demoapp.model.DomCy;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomCyViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomCyInsert extends DialogFragment implements View.OnClickListener {

    private DialogDomCyInsertBinding binding;
    private Bundle bundle;

    private final String[] listStr = new String[3];

    private String stationGo, stationCome, name, weight, quantity, etd;

    private DomCyViewModel mDomCyViewModel;
    private CommunicateViewModel communicateViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogDomCyInsertBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);
        mDomCyViewModel = new ViewModelProvider(this).get(DomCyViewModel.class);

        setUpViews();
        textWatcher();
        setData();

        return view;
    }

    public void setData() {
        bundle = getArguments();
        if (bundle != null) {
            DomCy mDomCy = (DomCy) bundle.getSerializable(Constants.DOM_CY_UPDATE);
            if ("YES".equalsIgnoreCase(bundle.getString(Constants.DOM_CY_ADD_NEW))) {
                binding.domCyInsertAutoContainer.setText(mDomCy.getType());
                binding.domCyInsertAutoMonth.setText(mDomCy.getMonth());
                binding.domCyInsertAutoContinent.setText(mDomCy.getContinent());

                listStr[0] = binding.domCyInsertAutoContainer.getText().toString();
                listStr[1] = binding.domCyInsertAutoMonth.getText().toString();
                listStr[2] = binding.domCyInsertAutoContinent.getText().toString();

                Objects.requireNonNull(binding.insertDomCyStationGo.getEditText()).setText(mDomCy.getStationGo());
                Objects.requireNonNull(binding.insertDomCyStationCome.getEditText()).setText(mDomCy.getStationCome());
                Objects.requireNonNull(binding.insertDomCyName.getEditText()).setText(mDomCy.getName());
                Objects.requireNonNull(binding.insertDomCyWeight.getEditText()).setText(mDomCy.getWeight());
                Objects.requireNonNull(binding.insertDomCyQuantity.getEditText()).setText(mDomCy.getQuantity());
                Objects.requireNonNull(binding.insertDomCyEtd.getEditText()).setText(mDomCy.getEtd());

            }
        }
    }

    public static DialogDomCyInsert getInstance() {

        return new DialogDomCyInsert();
    }

    private void setUpViews() {

        binding.btnDomCyInsert.setOnClickListener(this);
        binding.btnDomCyCancel.setOnClickListener(this);

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_DOM_CY);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.domCyInsertAutoContainer.setAdapter(adapterItemsType);
        binding.domCyInsertAutoMonth.setAdapter(adapterItemsMonth);
        binding.domCyInsertAutoContinent.setAdapter(adapterItemsContinent);

        binding.domCyInsertAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domCyInsertAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domCyInsertAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
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
            case R.id.btn_dom_cy_insert:
                if (isFilled()) {
                    insertData();
                    dismiss();
                } else
                    Toast.makeText(getContext(), Constants.INSERT_FAILED, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_dom_cy_cancel:
                dismiss();
                break;
        }
    }

    public void getDataFromForm() {
        stationGo = Objects.requireNonNull(binding.insertDomCyStationGo.getEditText()).getText().toString();
        stationCome = Objects.requireNonNull(binding.insertDomCyStationCome.getEditText()).getText().toString();
        name = Objects.requireNonNull(binding.insertDomCyName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.insertDomCyWeight.getEditText()).getText().toString();
        quantity = Objects.requireNonNull(binding.insertDomCyQuantity.getEditText()).getText().toString();
        etd = Objects.requireNonNull(binding.insertDomCyEtd.getEditText()).getText().toString();
    }

    public void insertData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomCyViewModel.insertData(stationGo, stationCome, name, weight, quantity, etd,
              listStr[0], listStr[1], listStr[2], getCreatedDate()).enqueue(new Callback<DomCy>() {
            @Override
            public void onResponse(@NonNull Call<DomCy> call, @NonNull Response<DomCy> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Insert Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomCy> call, @NonNull Throwable t) {

            }
        });
    }

    public boolean isFilled() {
        boolean result = true;

        if (TextUtils.isEmpty(binding.domCyInsertAutoContainer.getText())) {
            result = false;
            binding.domCyInsertAutoContainer.setError(Constants.ERROR_AUTO_COMPLETE_TYPE);
        }

        if (TextUtils.isEmpty(binding.domCyInsertAutoMonth.getText())) {
            result = false;
            binding.domCyInsertAutoMonth.setError(Constants.ERROR_AUTO_COMPLETE_MONTH);
        }

        if (TextUtils.isEmpty(binding.domCyInsertAutoContinent.getText())) {
            result = false;
            binding.domCyInsertAutoContinent.setError(Constants.ERROR_AUTO_COMPLETE_CONTINENT);
        }

        return result;
    }

    /**
     * If this field is not empty, set null for error
     */
    public void textWatcher() {

        binding.domCyInsertAutoContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domCyInsertAutoContainer.getText())) {
                    binding.domCyInsertAutoContainer.setError(null);

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domCyInsertAutoMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domCyInsertAutoMonth.getText())) {
                    binding.domCyInsertAutoMonth.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.domCyInsertAutoContinent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.domCyInsertAutoContinent.getText())) {
                    binding.domCyInsertAutoContinent.setError(null);
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
