package com.example.demoapp.view.dialog.fcl;

import android.annotation.SuppressLint;
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
import com.example.demoapp.databinding.FragmentDialogUpdateFclBinding;
import com.example.demoapp.model.Fcl;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.FclViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateFclDialog extends DialogFragment implements View.OnClickListener {

    private final String[] listStr = new String[3];

    private Fcl fcl;

    private FragmentDialogUpdateFclBinding binding;

    private FclViewModel mFclViewModel;
    private CommunicateViewModel mCommunicateViewModel;
    private Bundle bundle;


    public static UpdateFclDialog getInstance() {
        return new UpdateFclDialog();
    }

    /**
     * This method will set a view for insert dialog
     *
     * @param inflater           inflater
     * @param container          container
     * @param savedInstanceState save
     * @return view of this fragment dialog
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogUpdateFclBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        mFclViewModel = new ViewModelProvider(this).get(FclViewModel.class);
        mCommunicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);

        bundle = getArguments();
        setInfo();


        initView();
        showDatePicker();

        initView();


        return view;
    }

    public void setInfo() {

        if (bundle != null) {
            fcl = (Fcl) bundle.getSerializable(Constants.FCL_UPDATE);

            binding.updateAutoMonth.setText(fcl.getMonth());
            binding.updateAutoContainer.setText(fcl.getType());
            binding.updateAutoContinent.setText(fcl.getContinent());
            Objects.requireNonNull(binding.tfPol.getEditText()).setText(fcl.getPol());
            Objects.requireNonNull(binding.tfPod.getEditText()).setText(fcl.getPod());
            Objects.requireNonNull(binding.tfOf20.getEditText()).setText(fcl.getOf20());
            Objects.requireNonNull(binding.tfOf40.getEditText()).setText(fcl.getOf40());
            Objects.requireNonNull(binding.tfOf45.getEditText()).setText(fcl.getOf45());
            Objects.requireNonNull(binding.tfSu20.getEditText()).setText(fcl.getSu20());
            Objects.requireNonNull(binding.tfSu40.getEditText()).setText(fcl.getSu40());
            Objects.requireNonNull(binding.tfLines.getEditText()).setText(fcl.getLinelist());
            Objects.requireNonNull(binding.tfNotes.getEditText()).setText(fcl.getNotes());
            Objects.requireNonNull(binding.tfValid.getEditText()).setText(fcl.getValid());
            Objects.requireNonNull(binding.tfNotes2.getEditText()).setText(fcl.getNotes2());
        } else {
            Toast.makeText(getContext(), "GetData Failed", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * this method will init for all views and get a item of auto complete textview
     */
    public void initView() {

        // auto complete textview
        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_FCL);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.updateAutoContainer.setAdapter(adapterItemsType);
        binding.updateAutoMonth.setAdapter(adapterItemsMonth);
        binding.updateAutoContinent.setAdapter(adapterItemsContinent);

        listStr[0] = binding.updateAutoContainer.getText().toString();
        listStr[1] = binding.updateAutoMonth.getText().toString();
        listStr[2] = binding.updateAutoContinent.getText().toString();

        // buttons
        binding.btnFunctionUpdate.setOnClickListener(this);
        binding.btnFunctionCancel.setOnClickListener(this);

        binding.updateAutoContainer.setOnItemClickListener((adapterView, view, i, l) -> listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.updateAutoMonth.setOnItemClickListener((adapterView, view, i, l) -> listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.updateAutoContinent.setOnItemClickListener((adapterView, view, i, l) -> listStr[2] = adapterView.getItemAtPosition(i).toString());

        textWatcher();

        setCancelable(false);

    }

    /**
     * If this field is not empty, set null for error
     */
    public void textWatcher() {
        Objects.requireNonNull(binding.tfPol.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.tfPol.getEditText().getText().toString())) {
                    binding.tfPol.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Objects.requireNonNull(binding.tfPod.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.tfPod.getEditText().getText().toString())) {
                    binding.tfPod.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Objects.requireNonNull(binding.tfValid.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.tfValid.getEditText().getText().toString())) {
                    binding.tfValid.setErrorEnabled(false);
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

    /**
     * This method set event for and and cancel buttons
     *
     * @param v view
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_function_update:
                if (isFilled()) {
                    updateFcl();
                    dismiss();
                } else
                    Toast.makeText(getContext(), Constants.UPDATE_FAILED, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_function_cancel:
                dismiss();
                break;
        }
    }

    public void showDatePicker() {

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date");

        final MaterialDatePicker<Long> materialDatePicker = builder.build();

        binding.edtValid.setOnClickListener(view -> {
            materialDatePicker.show(getParentFragmentManager(), "Date_Picker");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {

                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                Date date = new Date(selection + offsetFromUTC);

                Objects.requireNonNull(binding.tfValid.getEditText()).setText(simpleFormat.format(date));
            });
        });

    }

    /**
     * This method used to get data user typing and insert them into database
     */
    public void updateFcl() {

        String pol = Objects.requireNonNull(binding.tfPol.getEditText()).getText().toString();
        String pod = Objects.requireNonNull(binding.tfPod.getEditText()).getText().toString();
        String of20 = Objects.requireNonNull(binding.tfOf20.getEditText()).getText().toString();
        String of40 = Objects.requireNonNull(binding.tfOf40.getEditText()).getText().toString();
        String of45 = Objects.requireNonNull(binding.tfOf45.getEditText()).getText().toString();
        String su20 = Objects.requireNonNull(binding.tfSu20.getEditText()).getText().toString();
        String su40 = Objects.requireNonNull(binding.tfSu40.getEditText()).getText().toString();
        String line = Objects.requireNonNull(binding.tfLines.getEditText()).getText().toString();
        String notes = Objects.requireNonNull(binding.tfNotes.getEditText()).getText().toString();
        String valid = Objects.requireNonNull(binding.tfValid.getEditText()).getText().toString();
        String note2 = Objects.requireNonNull(binding.tfNotes2.getEditText()).getText().toString();

        mCommunicateViewModel.makeChanges();
        Call<Fcl> call = mFclViewModel.updateFcl(fcl.getStt(), pol, pod, of20, of40, of45, su20, su40, line, notes, valid, note2, listStr[1], listStr[0], listStr[2]);

        call.enqueue(new Callback<Fcl>() {
            @Override
            public void onResponse(@NonNull Call<Fcl> call, @NonNull Response<Fcl> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Created Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Fcl> call, @NonNull Throwable t) {

            }
        });
    }

    public boolean isFilled() {
        boolean result = true;

        if (TextUtils.isEmpty(Objects.requireNonNull(binding.tfPol.getEditText()).getText().toString())) {
            result = false;
            binding.tfPol.setError(Constants.ERROR_POL);
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(binding.tfValid.getEditText()).getText().toString())) {
            result = false;
            binding.tfValid.setError(Constants.ERROR_VALID);
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(binding.tfPod.getEditText()).getText().toString())) {
            result = false;
            binding.tfPod.setError(Constants.ERROR_POD);
        }

        return result;
    }
}
