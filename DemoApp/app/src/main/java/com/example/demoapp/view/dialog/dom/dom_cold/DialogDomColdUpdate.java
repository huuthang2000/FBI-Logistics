package com.example.demoapp.view.dialog.dom.dom_cold;

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
import com.example.demoapp.databinding.DialogDomColdUpdateBinding;
import com.example.demoapp.model.DomCold;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomColdViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDomColdUpdate extends DialogFragment {

    private DialogDomColdUpdateBinding binding;

    private CommunicateViewModel communicateViewModel;
    private DomColdViewModel mDomColdViewModel;

    private final String[] listStr = new String[3];
    private DomCold mDomCold;

    private String name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length, height, width;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogDomColdUpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        communicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);
        mDomColdViewModel = new ViewModelProvider(this).get(DomColdViewModel.class);

        setData();
        setUpViews();
        setListenerForButtons();

        return root;
    }

    public static DialogDomColdUpdate getInstance() {
        return new DialogDomColdUpdate();
    }

    public void setData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            mDomCold = (DomCold) bundle.getSerializable(Constants.DOM_COLD_UPDATE);

            binding.domColdUpdateAutoContainer.setText(mDomCold.getType());
            binding.domColdUpdateAutoMonth.setText(mDomCold.getMonth());
            binding.domColdUpdateAutoContinent.setText(mDomCold.getContinent());

            Objects.requireNonNull(binding.updateDomColdName.getEditText()).setText(mDomCold.getName());
            Objects.requireNonNull(binding.updateDomColdWeight.getEditText()).setText(mDomCold.getWeight());
            Objects.requireNonNull(binding.updateDomColdQuantityPallet.getEditText()).setText(mDomCold.getQuantityPallet());
            Objects.requireNonNull(binding.updateDomColdQuantityCarton.getEditText()).setText(mDomCold.getQuantityCarton());
            Objects.requireNonNull(binding.updateDomColdAddressReceive.getEditText()).setText(mDomCold.getAddressReceive());
            Objects.requireNonNull(binding.updateDomColdAddressDelivery.getEditText()).setText(mDomCold.getAddressDelivery());
            Objects.requireNonNull(binding.updateDomColdLength.getEditText()).setText(mDomCold.getLength());
            Objects.requireNonNull(binding.updateDomColdHeight.getEditText()).setText(mDomCold.getHeight());
            Objects.requireNonNull(binding.updateDomColdWidth.getEditText()).setText(mDomCold.getWidth());
        }
    }

    private void setUpViews() {

        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_TYPE_DOM_DRY);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        listStr[0] = binding.domColdUpdateAutoContainer.getText().toString();
        listStr[1] = binding.domColdUpdateAutoMonth.getText().toString();
        listStr[2] = binding.domColdUpdateAutoContinent.getText().toString();

        binding.domColdUpdateAutoContainer.setAdapter(adapterItemsType);
        binding.domColdUpdateAutoMonth.setAdapter(adapterItemsMonth);
        binding.domColdUpdateAutoContinent.setAdapter(adapterItemsContinent);

        binding.domColdUpdateAutoContainer.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.domColdUpdateAutoMonth.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.domColdUpdateAutoContinent.setOnItemClickListener((adapterView, view, i, l) ->
                listStr[2] = adapterView.getItemAtPosition(i).toString());

        setCancelable(false);
    }

    public void setListenerForButtons() {
        binding.btnDomColdUpdate.setOnClickListener(view -> {
            updateData();
            dismiss();
        });
        binding.btnDomColdCancel.setOnClickListener(view -> dismiss());
    }


    public void getDataFromForm() {
        name = Objects.requireNonNull(binding.updateDomColdName.getEditText()).getText().toString();
        weight = Objects.requireNonNull(binding.updateDomColdWeight.getEditText()).getText().toString();
        quantityPallet = Objects.requireNonNull(binding.updateDomColdQuantityPallet.getEditText()).getText().toString();
        quantityCarton = Objects.requireNonNull(binding.updateDomColdQuantityCarton.getEditText()).getText().toString();
        addressReceive = Objects.requireNonNull(binding.updateDomColdAddressReceive.getEditText()).getText().toString();
        addressDelivery = Objects.requireNonNull(binding.updateDomColdAddressDelivery.getEditText()).getText().toString();
        length = Objects.requireNonNull(binding.updateDomColdLength.getEditText()).getText().toString();
        height = Objects.requireNonNull(binding.updateDomColdWeight.getEditText()).getText().toString();
        width = Objects.requireNonNull(binding.updateDomColdWidth.getEditText()).getText().toString();
    }

    public void updateData() {
        getDataFromForm();

        communicateViewModel.makeChanges();

        mDomColdViewModel.updateData(mDomCold.getStt(), name, weight, quantityPallet, quantityCarton, addressReceive, addressDelivery, length,
                height, width, listStr[0], listStr[1], listStr[2]).enqueue(new Callback<DomCold>() {
            @Override
            public void onResponse(@NonNull Call<DomCold> call, @NonNull Response<DomCold> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Update Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DomCold> call, @NonNull Throwable t) {

            }
        });
    }

}