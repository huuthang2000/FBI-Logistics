package com.example.demoapp.view.dialog.dom.dom_dry;

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
import com.example.demoapp.databinding.DialogDomDryUpdateBinding;
import com.example.demoapp.model.DomDry;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomDryViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomDryUpdate extends DialogFragment {

    private DialogDomDryUpdateBinding binding;

    private CommunicateViewModel communicateViewModel;
    private DomDryViewModel mDomDryViewModel;

    private final String[] listStr = new String[3];
    private DomDry mDomDry;

    private String name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomDryUpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);
        mDomDryViewModel = new ViewModelProvider(this).get(DomDryViewModel.class);

        setData();
        setUpViews();
        setListenerForButtons();

        return root;
    }

    public static DialogDomDryUpdate getInstance() {
        return new DialogDomDryUpdate();
    }

    public void setData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            mDomDry = (DomDry) bundle.getSerializable(Constants.DOM_DRY_UPDATE);

            binding.domDryUpdateAutoContainer.setText(mDomDry.getType());
            binding.domDryUpdateAutoMonth.setText(mDomDry.getMonth());
            binding.domDryUpdateAutoContinent.setText(mDomDry.getContinent());

            Objects.requireNonNull(binding.updateDomDryName.getEditText()).setText(mDomDry.getName());
            Objects.requireNonNull(binding.updateDomDryWeight.getEditText()).setText(mDomDry.getWeight());
            Objects.requireNonNull(binding.updateDomDryQuantityPallet.getEditText()).setText(mDomDry.getQuantityPallet());
            Objects.requireNonNull(binding.updateDomDryQuantityCarton.getEditText()).setText(mDomDry.getQuantityCarton());
            Objects.requireNonNull(binding.updateDomDryAddressReceive.getEditText()).setText(mDomDry.getAddressReceive());
            Objects.requireNonNull(binding.updateDomDryAddressDelivery.getEditText()).setText(mDomDry.getAddressDelivery());
            Objects.requireNonNull(binding.updateDomDryLength.getEditText()).setText(mDomDry.getLength());
            Objects.requireNonNull(binding.updateDomDryHeight.getEditText()).setText(mDomDry.getHeight());
            Objects.requireNonNull(binding.updateDomDryWidth.getEditText()).setText(mDomDry.getWidth());
        }
    }

    private void setUpViews() {

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_TYPE_DOM_DRY);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        listStr[0] = binding.domDryUpdateAutoContainer.getText().toString();
        listStr[1] = binding.domDryUpdateAutoMonth.getText().toString();
        listStr[2] = binding.domDryUpdateAutoContinent.getText().toString();

        binding.domDryUpdateAutoContainer.setAdapter(adapterItemsType);
        binding.domDryUpdateAutoMonth.setAdapter(adapterItemsMonth);
        binding.domDryUpdateAutoContinent.setAdapter(adapterItemsContinent);

        binding.domDryUpdateAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domDryUpdateAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domDryUpdateAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[2] = adapterView.getItemAtPosition(i).toString());

        setCancelable(false);
    }

    public void setListenerForButtons() {
        binding.btnDomDryUpdate.setOnClickListener(view -> {
            updateData();
            dismiss();
        });
        binding.btnDomDryCancel.setOnClickListener(view -> dismiss());
    }


    public void getDataFromForm() {
        name = Objects.requireNonNull(binding.updateDomDryName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.updateDomDryWeight.getEditText()).getText().toString();
        quantityPallet = Objects.requireNonNull(binding.updateDomDryQuantityPallet.getEditText()).getText().toString();
        quantityCarton = Objects.requireNonNull(binding.updateDomDryQuantityCarton.getEditText()).getText().toString();
        addressReceive = Objects.requireNonNull(binding.updateDomDryAddressReceive.getEditText()).getText().toString();
        addressDelivery = Objects.requireNonNull(binding.updateDomDryAddressDelivery.getEditText()).getText().toString();
        length = Objects.requireNonNull(binding.updateDomDryLength.getEditText()).getText().toString();
        height = Objects.requireNonNull(binding.updateDomDryHeight.getEditText()).getText().toString();
        width = Objects.requireNonNull(binding.updateDomDryWidth.getEditText()).getText().toString();
    }

    public void updateData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomDryViewModel.updateData(mDomDry.getStt(), name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length,
                height, width, listStr[0], listStr[1], listStr[2]).enqueue(new Callback<DomDry>() {
            @Override
            public void onResponse(@NonNull Call<DomDry> call, @NonNull Response<DomDry> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Update Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomDry> call, @NonNull Throwable t) {

            }
        });
    }

}