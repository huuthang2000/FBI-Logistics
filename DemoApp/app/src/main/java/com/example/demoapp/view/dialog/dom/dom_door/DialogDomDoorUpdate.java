package com.example.demoapp.view.dialog.dom.dom_door;

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
import com.example.demoapp.databinding.DialogDomDoorUpdateBinding;
import com.example.demoapp.model.DomDoor;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomDoorViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomDoorUpdate extends DialogFragment {

    private DialogDomDoorUpdateBinding binding;
    private DomDoor mDomDoor;

    private final String[] listStr = new String[3];

    private String stationGo, stationCome, addressReceive, addressDelivery, name, weight, quantity, etd;

    private DomDoorViewModel mDomDoorViewModel;
    private CommunicateViewModel communicateViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomDoorUpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);
        mDomDoorViewModel = new ViewModelProvider(this).get(DomDoorViewModel.class);

        setData();
        setUpViews();
        setListenerForButtons();

        return root;
    }

    public static DialogDomDoorUpdate getInstance() {
        return new DialogDomDoorUpdate();
    }

    public void setData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            mDomDoor = (DomDoor) bundle.getSerializable(Constants.DOM_DOOR_UPDATE);

            binding.domDoorUpdateAutoContainer.setText(mDomDoor.getType());
            binding.domDoorUpdateAutoMonth.setText(mDomDoor.getMonth());
            binding.domDoorUpdateAutoContinent.setText(mDomDoor.getContinent());

            Objects.requireNonNull(binding.updateDomDoorStationGo.getEditText()).setText(mDomDoor.getStationGo());
            Objects.requireNonNull(binding.updateDomDoorStationCome.getEditText()).setText(mDomDoor.getStationCome());
            Objects.requireNonNull(binding.updateDomDoorAddressReceive.getEditText()).setText(mDomDoor.getAddressReceive());
            Objects.requireNonNull(binding.updateDomDoorAddressDelivery.getEditText()).setText(mDomDoor.getAddressDelivery());
            Objects.requireNonNull(binding.updateDomDoorName.getEditText()).setText(mDomDoor.getName());
            Objects.requireNonNull(binding.updateDomDoorWeight.getEditText()).setText(mDomDoor.getWeight());
            Objects.requireNonNull(binding.updateDomDoorQuantity.getEditText()).setText(mDomDoor.getQuantity());
            Objects.requireNonNull(binding.updateDomDoorEtd.getEditText()).setText(mDomDoor.getEtd());

        }
    }

    private void setUpViews() {


        mDomDoorViewModel = new ViewModelProvider(this).get(DomDoorViewModel.class);

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_DOM_CY);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.domDoorUpdateAutoContainer.setAdapter(adapterItemsType);
        binding.domDoorUpdateAutoMonth.setAdapter(adapterItemsMonth);
        binding.domDoorUpdateAutoContinent.setAdapter(adapterItemsContinent);

        listStr[0] = binding.domDoorUpdateAutoContainer.getText().toString();
        listStr[1] = binding.domDoorUpdateAutoMonth.getText().toString();
        listStr[2] = binding.domDoorUpdateAutoContinent.getText().toString();

        binding.domDoorUpdateAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domDoorUpdateAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domDoorUpdateAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[2] = adapterView.getItemAtPosition(i).toString());

        setCancelable(false);
    }

    public void setListenerForButtons() {
        binding.btnDomDoorUpdate.setOnClickListener(view -> {
            updateData();
            dismiss();
        });
        binding.btnDomDoorCancel.setOnClickListener(view -> dismiss());
    }


    public void getDataFromForm() {
        stationGo = Objects.requireNonNull(binding.updateDomDoorStationGo.getEditText()).getText().toString();
        stationCome = Objects.requireNonNull(binding.updateDomDoorStationCome.getEditText()).getText().toString();
        addressReceive = Objects.requireNonNull(binding.updateDomDoorAddressReceive.getEditText()).getText().toString();
        addressDelivery = Objects.requireNonNull(binding.updateDomDoorAddressDelivery.getEditText()).getText().toString();
        name = Objects.requireNonNull(binding.updateDomDoorName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.updateDomDoorWeight.getEditText()).getText().toString();
        quantity = Objects.requireNonNull(binding.updateDomDoorQuantity.getEditText()).getText().toString();
        etd = Objects.requireNonNull(binding.updateDomDoorEtd.getEditText()).getText().toString();

    }

    public void updateData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomDoorViewModel.updateData(mDomDoor.getStt(), stationGo, stationCome, addressReceive, addressDelivery, name, weight, quantity, etd
                , listStr[0], listStr[1], listStr[2]).enqueue(new Callback<DomDoor>() {
            @Override
            public void onResponse(@NonNull Call<DomDoor> call, @NonNull Response<DomDoor> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Update Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomDoor> call, @NonNull Throwable t) {

            }
        });
    }

}