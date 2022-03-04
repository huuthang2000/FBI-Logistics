package com.example.demoapp.view.dialog.dom.dom_door_sea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.demoapp.R;
import com.example.demoapp.databinding.DialogDomDoorSeaUpdateBinding;
import com.example.demoapp.model.DomDoorSea;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomDoorSeaViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomDoorSeaUpdate extends DialogFragment {

    private DialogDomDoorSeaUpdateBinding binding;
    private DomDoorSea mDomDoorSea;

    private final String[] listStr = new String[3];

    private String portGo, portCome, addressReceive, addressDelivery, name, weight, quantity, etd;

    private DomDoorSeaViewModel mDomDoorSeaViewModel;
    private CommunicateViewModel communicateViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomDoorSeaUpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);
        mDomDoorSeaViewModel = new ViewModelProvider(this).get(DomDoorSeaViewModel.class);

        setData();
        setUpViews();
        setListenerForButtons();

        return root;
    }

    public static DialogDomDoorSeaUpdate getInstance() {
        return new DialogDomDoorSeaUpdate();
    }

    public void setData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            mDomDoorSea = (DomDoorSea) bundle.getSerializable(Constants.DOM_DOOR_SEA_UPDATE);

            binding.domDoorSeaUpdateAutoContainer.setText(mDomDoorSea.getType());
            binding.domDoorSeaUpdateAutoMonth.setText(mDomDoorSea.getMonth());
            binding.domDoorSeaUpdateAutoContinent.setText(mDomDoorSea.getContinent());

            Objects.requireNonNull(binding.updateDomDoorSeaPortGo.getEditText()).setText(mDomDoorSea.getPortGo());
            Objects.requireNonNull(binding.updateDomDoorSeaPortCome.getEditText()).setText(mDomDoorSea.getPortCome());
            Objects.requireNonNull(binding.updateDomDoorSeaAddressReceive.getEditText()).setText(mDomDoorSea.getAddressReceive());
            Objects.requireNonNull(binding.updateDomDoorSeaAddressDelivery.getEditText()).setText(mDomDoorSea.getAddressDelivery());
            Objects.requireNonNull(binding.updateDomDoorSeaName.getEditText()).setText(mDomDoorSea.getName());
            Objects.requireNonNull(binding.updateDomDoorSeaWeight.getEditText()).setText(mDomDoorSea.getWeight());
            Objects.requireNonNull(binding.updateDomDoorSeaQuantity.getEditText()).setText(mDomDoorSea.getQuantity());
            Objects.requireNonNull(binding.updateDomDoorSeaEtd.getEditText()).setText(mDomDoorSea.getEtd());

        }
    }

    private void setUpViews() {

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_DOM_SEA);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.domDoorSeaUpdateAutoContainer.setAdapter(adapterItemsType);
        binding.domDoorSeaUpdateAutoMonth.setAdapter(adapterItemsMonth);
        binding.domDoorSeaUpdateAutoContinent.setAdapter(adapterItemsContinent);

        listStr[0] = binding.domDoorSeaUpdateAutoContainer.getText().toString();
        listStr[1] = binding.domDoorSeaUpdateAutoMonth.getText().toString();
        listStr[2] = binding.domDoorSeaUpdateAutoContinent.getText().toString();

        binding.domDoorSeaUpdateAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domDoorSeaUpdateAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domDoorSeaUpdateAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[2] = adapterView.getItemAtPosition(i).toString());

        setCancelable(false);
    }

    public void setListenerForButtons() {
        binding.btnDomDoorSeaUpdate.setOnClickListener(view -> {
            updateData();
            dismiss();
        });
        binding.btnDomDoorSeaCancel.setOnClickListener(view -> dismiss());
    }


    public void getDataFromForm() {
        portGo = Objects.requireNonNull(binding.updateDomDoorSeaPortGo.getEditText()).getText().toString();
        portCome = Objects.requireNonNull(binding.updateDomDoorSeaPortCome.getEditText()).getText().toString();
        addressReceive = Objects.requireNonNull(binding.updateDomDoorSeaAddressReceive.getEditText()).getText().toString();
        addressDelivery = Objects.requireNonNull(binding.updateDomDoorSeaAddressDelivery.getEditText()).getText().toString();
        name = Objects.requireNonNull(binding.updateDomDoorSeaName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.updateDomDoorSeaWeight.getEditText()).getText().toString();
        quantity = Objects.requireNonNull(binding.updateDomDoorSeaQuantity.getEditText()).getText().toString();
        etd = Objects.requireNonNull(binding.updateDomDoorSeaEtd.getEditText()).getText().toString();

    }

    public void updateData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomDoorSeaViewModel.updateData(mDomDoorSea.getStt(), portGo, portCome, addressReceive, addressDelivery, name, weight, quantity, etd
                , listStr[0], listStr[1], listStr[2]).enqueue(new Callback<DomDoorSea>() {
            @Override
            public void onResponse(@NonNull Call<DomDoorSea> call, @NonNull Response<DomDoorSea> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Update Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomDoorSea> call, @NonNull Throwable t) {

            }
        });
    }

}