package com.example.demoapp.view.dialog.dom.dom_cy;

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

import com.example.demoapp.databinding.DialogDomCyUpdateBinding;
import com.example.demoapp.model.DomCy;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomCyViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomCyUpdate extends DialogFragment {

    private DialogDomCyUpdateBinding binding;
    private DomCy domCy;

    private final String[] listStr = new String[3];

    private String stationGo, stationCome, name, weight, quantity, etd;

    private DomCyViewModel mDomCyViewModel;
    private CommunicateViewModel communicateViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomCyUpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);
        mDomCyViewModel = new ViewModelProvider(this).get(DomCyViewModel.class);

        setData();
        setUpViews();
        setListenerForButtons();

        return root;
    }

    public static DialogDomCyUpdate getInstance() {
        return new DialogDomCyUpdate();
    }

    public void setData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            domCy = (DomCy) bundle.getSerializable(Constants.DOM_CY_UPDATE);

            binding.domCyUpdateAutoContainer.setText(domCy.getType());
            binding.domCyUpdateAutoMonth.setText(domCy.getMonth());
            binding.domCyUpdateAutoContinent.setText(domCy.getContinent());

            Objects.requireNonNull(binding.updateDomCyStationGo.getEditText()).setText(domCy.getStationGo());
            Objects.requireNonNull(binding.updateDomCyStationCome.getEditText()).setText(domCy.getStationCome());
            Objects.requireNonNull(binding.updateDomCyName.getEditText()).setText(domCy.getName());
            Objects.requireNonNull(binding.updateDomCyWeight.getEditText()).setText(domCy.getWeight());
            Objects.requireNonNull(binding.updateDomCyQuantity.getEditText()).setText(domCy.getQuantity());
            Objects.requireNonNull(binding.updateDomCyEtd.getEditText()).setText(domCy.getEtd());

        }
    }

    private void setUpViews() {


        mDomCyViewModel = new ViewModelProvider(this).get(DomCyViewModel.class);

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_DOM_CY);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.domCyUpdateAutoContainer.setAdapter(adapterItemsType);
        binding.domCyUpdateAutoMonth.setAdapter(adapterItemsMonth);
        binding.domCyUpdateAutoContinent.setAdapter(adapterItemsContinent);

        listStr[0] = binding.domCyUpdateAutoContainer.getText().toString();
        listStr[1] = binding.domCyUpdateAutoMonth.getText().toString();
        listStr[2] = binding.domCyUpdateAutoContinent.getText().toString();

        binding.domCyUpdateAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domCyUpdateAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domCyUpdateAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[2] = adapterView.getItemAtPosition(i).toString());

        setCancelable(false);
    }

    public void setListenerForButtons() {
        binding.btnDomCyUpdate.setOnClickListener(view -> {
            updateData();
            dismiss();
        });
        binding.btnDomCyCancel.setOnClickListener(view -> dismiss());
    }


    public void getDataFromForm() {
        stationGo = Objects.requireNonNull(binding.updateDomCyStationGo.getEditText()).getText().toString();
        stationCome = Objects.requireNonNull(binding.updateDomCyStationCome.getEditText()).getText().toString();
        name = Objects.requireNonNull(binding.updateDomCyName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.updateDomCyWeight.getEditText()).getText().toString();
        quantity = Objects.requireNonNull(binding.updateDomCyQuantity.getEditText()).getText().toString();
        etd = Objects.requireNonNull(binding.updateDomCyEtd.getEditText()).getText().toString();

    }

    public void updateData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomCyViewModel.updateData(domCy.getStt(), stationGo, stationCome, name, weight, quantity, etd
                , listStr[0], listStr[1], listStr[2]).enqueue(new Callback<DomCy>() {
            @Override
            public void onResponse(@NonNull Call<DomCy> call, @NonNull Response<DomCy> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Update Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomCy> call, @NonNull Throwable t) {

            }
        });
    }

}