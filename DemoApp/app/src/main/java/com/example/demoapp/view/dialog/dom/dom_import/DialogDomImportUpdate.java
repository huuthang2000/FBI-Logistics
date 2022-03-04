package com.example.demoapp.view.dialog.dom.dom_import;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.demoapp.R;
import com.example.demoapp.databinding.DialogDomImportUpdateBinding;
import com.example.demoapp.model.DomImport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomImportViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomImportUpdate extends DialogFragment {

    private DialogDomImportUpdateBinding binding;

    private CommunicateViewModel communicateViewModel;
    private DomImportViewModel mDomImportViewModel;

    private final String[] listStr = new String[3];
    private DomImport mDomImport;

    private String name, weight, quantity, temp, address, portReceive, length, height, width;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomImportUpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);
        mDomImportViewModel = new ViewModelProvider(this).get(DomImportViewModel.class);

        setData();
        setUpViews();
        setListenerForButtons();

        return root;
    }

    public static DialogDomImportUpdate getInstance() {
        return new DialogDomImportUpdate();
    }

    public void setData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            mDomImport = (DomImport) bundle.getSerializable(Constants.DOM_IMPORT_UPDATE);

            binding.domImportUpdateAutoContainer.setText(mDomImport.getType());
            binding.domImportUpdateAutoMonth.setText(mDomImport.getMonth());
            binding.domImportUpdateAutoContinent.setText(mDomImport.getContinent());

            Objects.requireNonNull(binding.updateDomImportName.getEditText()).setText(mDomImport.getName());
            Objects.requireNonNull(binding.updateDomImportWeight.getEditText()).setText(mDomImport.getWeight());
            Objects.requireNonNull(binding.updateDomImportQuantity.getEditText()).setText(mDomImport.getQuantity());
            Objects.requireNonNull(binding.updateDomImportTemp.getEditText()).setText(mDomImport.getTemp());
            Objects.requireNonNull(binding.updateDomImportAddress.getEditText()).setText(mDomImport.getAddress());
            Objects.requireNonNull(binding.updateDomImportPort.getEditText()).setText(mDomImport.getPortReceive());
            Objects.requireNonNull(binding.updateDomImportLength.getEditText()).setText(mDomImport.getLength());
            Objects.requireNonNull(binding.updateDomImportHeight.getEditText()).setText(mDomImport.getHeight());
            Objects.requireNonNull(binding.updateDomImportWidth.getEditText()).setText(mDomImport.getWidth());
        }
    }

    private void setUpViews() {

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_DOM);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        listStr[0] = binding.domImportUpdateAutoContainer.getText().toString();
        listStr[1] = binding.domImportUpdateAutoMonth.getText().toString();
        listStr[2] = binding.domImportUpdateAutoContinent.getText().toString();

        binding.domImportUpdateAutoContainer.setAdapter(adapterItemsType);
        binding.domImportUpdateAutoMonth.setAdapter(adapterItemsMonth);
        binding.domImportUpdateAutoContinent.setAdapter(adapterItemsContinent);

        binding.domImportUpdateAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domImportUpdateAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domImportUpdateAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[2] = adapterView.getItemAtPosition(i).toString());

        setCancelable(false);
    }

    public void setListenerForButtons() {
        binding.btnDomImportUpdate.setOnClickListener(view -> {
            updateData();
            dismiss();
        });
        binding.btnDomImportCancel.setOnClickListener(view -> dismiss());
    }


    public void getDataFromForm() {
        name = Objects.requireNonNull(binding.updateDomImportName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.updateDomImportWeight.getEditText()).getText().toString();
        quantity = Objects.requireNonNull(binding.updateDomImportQuantity.getEditText()).getText().toString();
        temp = Objects.requireNonNull(binding.updateDomImportTemp.getEditText()).getText().toString();
        address = Objects.requireNonNull(binding.updateDomImportAddress.getEditText()).getText().toString();
        portReceive = Objects.requireNonNull(binding.updateDomImportPort.getEditText()).getText().toString();
        length = Objects.requireNonNull(binding.updateDomImportLength.getEditText()).getText().toString();
        height = Objects.requireNonNull(binding.updateDomImportHeight.getEditText()).getText().toString();
        width = Objects.requireNonNull(binding.updateDomImportWidth.getEditText()).getText().toString();
    }

    public void updateData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomImportViewModel.updateData(mDomImport.getStt(), name, weight, quantity, temp, address, portReceive, length,
                height, width, listStr[0], listStr[1], listStr[2]).enqueue(new Callback<DomImport>() {
            @Override
            public void onResponse(@NonNull Call<DomImport> call, @NonNull Response<DomImport> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Update Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomImport> call, @NonNull Throwable t) {

            }
        });
    }

}