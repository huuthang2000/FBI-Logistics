package com.example.demoapp.view.dialog.imp;

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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentUpdateImportLclDialogBinding;
import com.example.demoapp.model.ImportLcl;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.ImportLclViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateImportLclDialog extends DialogFragment implements View.OnClickListener {

    private FragmentUpdateImportLclDialogBinding binding;

    private CommunicateViewModel mCommunicateViewModel;
    private ImportLclViewModel mImportViewModel;

    private final String[] listStr = new String[3];

    private Bundle bundle;
    private ImportLcl imp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUpdateImportLclDialogBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        mImportViewModel = new ViewModelProvider(this).get(ImportLclViewModel.class);
        mCommunicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);

        showDatePicker();

        bundle = getArguments();
        setInfo();

        initView();

        return root;
    }

    public static UpdateImportLclDialog getInstance() {
        return new UpdateImportLclDialog();
    }

    public void setInfo() {

        if (bundle != null) {
            imp = (ImportLcl) bundle.getSerializable(Constants.IMPORT_LCL_UPDATE);

            binding.insertAutoCargo.setText(imp.getCargo(), false);
            binding.insertAutoMonth.setText(imp.getMonth(), false);
            binding.insertAutoContinent.setText(imp.getContinent(), false);

            Objects.requireNonNull(binding.tfTerm.getEditText()).setText(imp.getTerm());
            Objects.requireNonNull(binding.tfPol.getEditText()).setText(imp.getPol());
            Objects.requireNonNull(binding.tfPod.getEditText()).setText(imp.getPod());

            Objects.requireNonNull(binding.tfOf.getEditText()).setText(imp.getOf());
            Objects.requireNonNull(binding.tfLocalPol.getEditText()).setText(imp.getLocalPol());
            Objects.requireNonNull(binding.tfLocalPod.getEditText()).setText(imp.getLocalPod());

            Objects.requireNonNull(binding.tfCarrier.getEditText()).setText(imp.getCarrier());
            Objects.requireNonNull(binding.tfSchedule.getEditText()).setText(imp.getSchedule());
            Objects.requireNonNull(binding.tfTransitTime.getEditText()).setText(imp.getTransitTime());
            Objects.requireNonNull(binding.tfValid.getEditText()).setText(imp.getValid());
            Objects.requireNonNull(binding.tfNote.getEditText()).setText(imp.getNote());
        }
    }

    public void showDatePicker() {

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date");

        final MaterialDatePicker<Long> materialDatePicker = builder.build();

        binding.importEdtValid.setOnClickListener(view -> {
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
     * This method will init for views and get item from auto complete text view
     */
    public void initView() {

        // auto complete textview
        ArrayAdapter<String> adapterItemsCargo = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CARGO);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.insertAutoCargo.setAdapter(adapterItemsCargo);
        binding.insertAutoMonth.setAdapter(adapterItemsMonth);
        binding.insertAutoContinent.setAdapter(adapterItemsContinent);

        listStr[0] = binding.insertAutoCargo.getText().toString();
        listStr[1] = binding.insertAutoMonth.getText().toString();
        listStr[2] = binding.insertAutoContinent.getText().toString();

        // buttons
        binding.btnAddImportLcl.setOnClickListener(this);
        binding.btnCancelImportLcl.setOnClickListener(this);

        binding.insertAutoCargo.setOnItemClickListener((adapterView, view, i, l) -> listStr[0] = adapterView.getItemAtPosition(i).toString());

        binding.insertAutoMonth.setOnItemClickListener((adapterView, view, i, l) -> listStr[1] = adapterView.getItemAtPosition(i).toString());

        binding.insertAutoContinent.setOnItemClickListener((adapterView, view, i, l) -> listStr[2] = adapterView.getItemAtPosition(i).toString());

        textWatcher();

        setCancelable(false);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_cancel_import_lcl:
                dismiss();
                break;
            case R.id.btn_add_import_lcl:
                if (isFilled()) {
                    process();
                    dismiss();
                } else
                    Toast.makeText(getContext(), Constants.UPDATE_FAILED, Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * get data of fields and update
     */
    private void process() {

        String term = Objects.requireNonNull(binding.tfTerm.getEditText()).getText().toString();
        String pol = Objects.requireNonNull(binding.tfPol.getEditText()).getText().toString();
        String pod = Objects.requireNonNull(binding.tfPod.getEditText()).getText().toString();

        String of = Objects.requireNonNull(binding.tfOf.getEditText()).getText().toString();
        String localPol = Objects.requireNonNull(binding.tfLocalPol.getEditText()).getText().toString();
        String localPod = Objects.requireNonNull(binding.tfLocalPod.getEditText()).getText().toString();

        String carrier = Objects.requireNonNull(binding.tfCarrier.getEditText()).getText().toString();
        String schedule = Objects.requireNonNull(binding.tfSchedule.getEditText()).getText().toString();
        String transit = Objects.requireNonNull(binding.tfTransitTime.getEditText()).getText().toString();
        String valid = Objects.requireNonNull(binding.tfValid.getEditText()).getText().toString();
        String note = Objects.requireNonNull(binding.tfNote.getEditText()).getText().toString();

        mCommunicateViewModel.makeChanges();
        Call<ImportLcl> call = mImportViewModel.updateImport(imp.getStt(),term, pol, pod, listStr[0], of, localPol, localPod,
                carrier, schedule, transit, valid, note,
                listStr[1], listStr[2]);

        call.enqueue(new Callback<ImportLcl>() {
            @Override
            public void onResponse(@NonNull Call<ImportLcl> call, @NonNull Response<ImportLcl> response) {
            }

            @Override
            public void onFailure(@NonNull Call<ImportLcl> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     *
     * @return false if one of these fields is not filled
     */
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


    /**
     * Using text watcher to check fields
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
}