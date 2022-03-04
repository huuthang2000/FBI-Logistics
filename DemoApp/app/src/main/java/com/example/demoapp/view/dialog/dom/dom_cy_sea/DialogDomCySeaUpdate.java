package com.example.demoapp.view.dialog.dom.dom_cy_sea;

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
import com.example.demoapp.databinding.DialogDomCySeaUpdateBinding;
import com.example.demoapp.model.DomCySea;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomCySeaViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomCySeaUpdate extends DialogFragment {

    private DialogDomCySeaUpdateBinding binding;
    private DomCySea domCySea;

    private final String[] listStr = new String[3];

    private String portGo, portCome, name, weight, quantity, etd;

    private DomCySeaViewModel mDomCySeaViewModel;
    private CommunicateViewModel communicateViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomCySeaUpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);
        mDomCySeaViewModel = new ViewModelProvider(this).get(DomCySeaViewModel.class);

        setData();
        setUpViews();
        setListenerForButtons();

        return root;
    }

    public static DialogDomCySeaUpdate getInstance() {
        return new DialogDomCySeaUpdate();
    }

    public void setData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            domCySea = (DomCySea) bundle.getSerializable(Constants.DOM_CY_SEA_UPDATE);

            binding.domCySeaUpdateAutoContainer.setText(domCySea.getType());
            binding.domCySeaUpdateAutoMonth.setText(domCySea.getMonth());
            binding.domCySeaUpdateAutoContinent.setText(domCySea.getContinent());

            Objects.requireNonNull(binding.updateDomCySeaStationGo.getEditText()).setText(domCySea.getPortGo());
            Objects.requireNonNull(binding.updateDomCySeaStationCome.getEditText()).setText(domCySea.getPortCome());
            Objects.requireNonNull(binding.updateDomCySeaName.getEditText()).setText(domCySea.getName());
            Objects.requireNonNull(binding.updateDomCySeaWeight.getEditText()).setText(domCySea.getWeight());
            Objects.requireNonNull(binding.updateDomCySeaQuantity.getEditText()).setText(domCySea.getQuantity());
            Objects.requireNonNull(binding.updateDomCySeaEtd.getEditText()).setText(domCySea.getEtd());

        }
    }

    private void setUpViews() {

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_DOM_SEA);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.domCySeaUpdateAutoContainer.setAdapter(adapterItemsType);
        binding.domCySeaUpdateAutoMonth.setAdapter(adapterItemsMonth);
        binding.domCySeaUpdateAutoContinent.setAdapter(adapterItemsContinent);

        listStr[0] = binding.domCySeaUpdateAutoContainer.getText().toString();
        listStr[1] = binding.domCySeaUpdateAutoMonth.getText().toString();
        listStr[2] = binding.domCySeaUpdateAutoContinent.getText().toString();

        binding.domCySeaUpdateAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domCySeaUpdateAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domCySeaUpdateAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[2] = adapterView.getItemAtPosition(i).toString());

        setCancelable(false);
    }

    public void setListenerForButtons() {
        binding.btnDomCySeaUpdate.setOnClickListener(view -> {
            updateData();
            dismiss();
        });
        binding.btnDomCySeaCancel.setOnClickListener(view -> dismiss());
    }


    public void getDataFromForm() {
        portGo = Objects.requireNonNull(binding.updateDomCySeaStationGo.getEditText()).getText().toString();
        portCome = Objects.requireNonNull(binding.updateDomCySeaStationCome.getEditText()).getText().toString();
        name = Objects.requireNonNull(binding.updateDomCySeaName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.updateDomCySeaWeight.getEditText()).getText().toString();
        quantity = Objects.requireNonNull(binding.updateDomCySeaQuantity.getEditText()).getText().toString();
        etd = Objects.requireNonNull(binding.updateDomCySeaEtd.getEditText()).getText().toString();

    }

    public void updateData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomCySeaViewModel.updateData(domCySea.getStt(), portGo, portCome, name, weight, quantity, etd
                , listStr[0], listStr[1], listStr[2]).enqueue(new Callback<DomCySea>() {
            @Override
            public void onResponse(@NonNull Call<DomCySea> call, @NonNull Response<DomCySea> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Update Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomCySea> call, @NonNull Throwable t) {

            }
        });
    }

}