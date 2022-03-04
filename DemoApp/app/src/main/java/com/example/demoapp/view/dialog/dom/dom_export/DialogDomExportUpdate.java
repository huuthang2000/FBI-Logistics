package com.example.demoapp.view.dialog.dom.dom_export;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentDialogDomExportUpdateBinding;
import com.example.demoapp.model.DomExport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomExportViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomExportUpdate extends DialogFragment {

    private FragmentDialogDomExportUpdateBinding binding;

    private CommunicateViewModel communicateViewModel;
    private DomExportViewModel mDomExportViewModel;

    private final String[] listStr = new String[3];
    private DomExport mDomExport;

    private String name, weight, quantity, temp, address, portExport, length, height, width;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDialogDomExportUpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);
        mDomExportViewModel = new ViewModelProvider(this).get(DomExportViewModel.class);

        setData();
        setUpViews();
        setListenerForButtons();

        return root;
    }

    public static DialogDomExportUpdate getInstance() {
        return new DialogDomExportUpdate();
    }

    public void setData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            mDomExport = (DomExport) bundle.getSerializable(Constants.DOM_EXPORT_UPDATE);

            binding.domExportUpdateAutoContainer.setText(mDomExport.getType());
            binding.domExportUpdateAutoMonth.setText(mDomExport.getMonth());
            binding.domExportUpdateAutoContinent.setText(mDomExport.getContinent());

            Objects.requireNonNull(binding.updateDomExportName.getEditText()).setText(mDomExport.getName());
            Objects.requireNonNull(binding.updateDomExportWeight.getEditText()).setText(mDomExport.getWeight());
            Objects.requireNonNull(binding.updateDomExportQuantity.getEditText()).setText(mDomExport.getQuantity());
            Objects.requireNonNull(binding.updateDomExportTemp.getEditText()).setText(mDomExport.getTemp());
            Objects.requireNonNull(binding.updateDomExportAddress.getEditText()).setText(mDomExport.getAddress());
            Objects.requireNonNull(binding.updateDomExportPort.getEditText()).setText(mDomExport.getPortExport());
            Objects.requireNonNull(binding.updateDomExportLength.getEditText()).setText(mDomExport.getLength());
            Objects.requireNonNull(binding.updateDomExportHeight.getEditText()).setText(mDomExport.getHeight());
            Objects.requireNonNull(binding.updateDomExportWidth.getEditText()).setText(mDomExport.getWidth());
        }
    }

    private void setUpViews() {

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_DOM);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        listStr[0] = binding.domExportUpdateAutoContainer.getText().toString();
        listStr[1] = binding.domExportUpdateAutoMonth.getText().toString();
        listStr[2] = binding.domExportUpdateAutoContinent.getText().toString();

        binding.domExportUpdateAutoContainer.setAdapter(adapterItemsType);
        binding.domExportUpdateAutoMonth.setAdapter(adapterItemsMonth);
        binding.domExportUpdateAutoContinent.setAdapter(adapterItemsContinent);

        binding.domExportUpdateAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domExportUpdateAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domExportUpdateAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[2] = adapterView.getItemAtPosition(i).toString());

        setCancelable(false);
    }

    public void setListenerForButtons() {
        binding.btnDomExportUpdateUpdate.setOnClickListener(view -> {
            updateData();
            dismiss();
        });
        binding.btnDomExportUpdateCancel.setOnClickListener(view -> dismiss());
    }


    public void getDataFromForm() {
        name = Objects.requireNonNull(binding.updateDomExportName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.updateDomExportWeight.getEditText()).getText().toString();
        quantity = Objects.requireNonNull(binding.updateDomExportQuantity.getEditText()).getText().toString();
        temp = Objects.requireNonNull(binding.updateDomExportTemp.getEditText()).getText().toString();
        address = Objects.requireNonNull(binding.updateDomExportAddress.getEditText()).getText().toString();
        portExport = Objects.requireNonNull(binding.updateDomExportPort.getEditText()).getText().toString();
        length = Objects.requireNonNull(binding.updateDomExportLength.getEditText()).getText().toString();
        height = Objects.requireNonNull(binding.updateDomExportHeight.getEditText()).getText().toString();
        width = Objects.requireNonNull(binding.updateDomExportWidth.getEditText()).getText().toString();
    }

    public void updateData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomExportViewModel.updateData(mDomExport.getStt(), name, weight, quantity, temp, address, portExport, length,
                height, width, listStr[0], listStr[1], listStr[2]).enqueue(new Callback<DomExport>() {
            @Override
            public void onResponse(@NonNull Call<DomExport> call, @NonNull Response<DomExport> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Update Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomExport> call, @NonNull Throwable t) {

            }
        });
    }

}