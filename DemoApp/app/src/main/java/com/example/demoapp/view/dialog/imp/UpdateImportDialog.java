package com.example.demoapp.view.dialog.imp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentUpdateImportDialogBinding;
import com.example.demoapp.model.Import;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.ImportViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateImportDialog extends DialogFragment implements View.OnClickListener {

    private FragmentUpdateImportDialogBinding binding;

    private CommunicateViewModel mCommunicateViewModel;
    private ImportViewModel mImportViewModel;

    private final String[] listStr = new String[3];

    private Bundle bundle;
    private Import imp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUpdateImportDialogBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        mImportViewModel = new ViewModelProvider(this).get(ImportViewModel.class);
        mCommunicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);

        showDatePicker();

        bundle = getArguments();
        setInfo();

        initView();

        return root;
    }

    public static UpdateImportDialog getInstance() {
        return new UpdateImportDialog();
    }

    public void setInfo() {

        if (bundle != null) {
            imp = (Import) bundle.getSerializable(Constants.IMPORT_UPDATE);

            binding.updateAutoContainer.setText(imp.getType(), false);
            binding.updateAutoMonth.setText(imp.getMonth(), false);
            binding.updateAutoContinent.setText(imp.getContinent(), false);

            Objects.requireNonNull(binding.tfPol.getEditText()).setText(imp.getPol());
            Objects.requireNonNull(binding.tfPod.getEditText()).setText(imp.getPod());

            Objects.requireNonNull(binding.tfOf20.getEditText()).setText(imp.getOf20());
            Objects.requireNonNull(binding.tfOf40.getEditText()).setText(imp.getOf40());
            Objects.requireNonNull(binding.tfOf45.getEditText()).setText(imp.getOf45());

            Objects.requireNonNull(binding.tfSur20.getEditText()).setText(imp.getSur20());
            Objects.requireNonNull(binding.tfSur40.getEditText()).setText(imp.getSur40());
            Objects.requireNonNull(binding.tfSur45.getEditText()).setText(imp.getSur45());

            Objects.requireNonNull(binding.tfTotalFreight.getEditText()).setText(imp.getTotalFreight());
            Objects.requireNonNull(binding.tfCarrier.getEditText()).setText(imp.getCarrier());
            Objects.requireNonNull(binding.tfSchedule.getEditText()).setText(imp.getSchedule());
            Objects.requireNonNull(binding.tfTransitTime.getEditText()).setText(imp.getTransitTime());
            Objects.requireNonNull(binding.tfFreeTime.getEditText()).setText(imp.getFreeTime());
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
        ArrayAdapter<String> adapterItemsType = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_IMPORT);
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.updateAutoContainer.setAdapter(adapterItemsType);
        binding.updateAutoMonth.setAdapter(adapterItemsMonth);
        binding.updateAutoContinent.setAdapter(adapterItemsContinent);

        listStr[0] = binding.updateAutoContainer.getText().toString();
        listStr[1] = binding.updateAutoMonth.getText().toString();
        listStr[2] = binding.updateAutoContinent.getText().toString();

        // buttons
        binding.importUpdateBtnCancel.setOnClickListener(this);
        binding.importUpdateBtnUpdate.setOnClickListener(this);

        binding.updateAutoContainer.setOnItemClickListener((adapterView, view, i, l) -> {
            listStr[0] = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(getContext(), listStr[0], Toast.LENGTH_LONG).show();
        });

        binding.updateAutoMonth.setOnItemClickListener((adapterView, view, i, l) -> {
            listStr[1] = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(getContext(), listStr[1], Toast.LENGTH_LONG).show();
        });

        binding.updateAutoContinent.setOnItemClickListener((adapterView, view, i, l) -> {
            listStr[2] = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(getContext(), listStr[2], Toast.LENGTH_LONG).show();
        });

        textWatcher();
        setCancelable(false);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.import_update_btn_cancel:
                dismiss();
                break;
            case R.id.import_update_btn_update:
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

        String pol = Objects.requireNonNull(binding.tfPol.getEditText()).getText().toString();
        String pod = Objects.requireNonNull(binding.tfPod.getEditText()).getText().toString();

        String of20 = Objects.requireNonNull(binding.tfOf20.getEditText()).getText().toString();
        String of40 = Objects.requireNonNull(binding.tfOf40.getEditText()).getText().toString();
        String of45 = Objects.requireNonNull(binding.tfOf45.getEditText()).getText().toString();

        String sur20 = Objects.requireNonNull(binding.tfSur20.getEditText()).getText().toString();
        String sur40 = Objects.requireNonNull(binding.tfSur40.getEditText()).getText().toString();
        String sur45 = Objects.requireNonNull(binding.tfSur45.getEditText()).getText().toString();

        String totalFreight = Objects.requireNonNull(binding.tfTotalFreight.getEditText()).getText().toString();
        String carrier = Objects.requireNonNull(binding.tfCarrier.getEditText()).getText().toString();
        String schedule = Objects.requireNonNull(binding.tfSchedule.getEditText()).getText().toString();
        String transit = Objects.requireNonNull(binding.tfTransitTime.getEditText()).getText().toString();
        String free = Objects.requireNonNull(binding.tfFreeTime.getEditText()).getText().toString();
        String valid = Objects.requireNonNull(binding.tfValid.getEditText()).getText().toString();
        String note = Objects.requireNonNull(binding.tfNote.getEditText()).getText().toString();

        mCommunicateViewModel.makeChanges();
        Call<Import> call = mImportViewModel.updateImport(imp.getStt(), pol, pod, of20, of40, of45, sur20, sur40,
                sur45, totalFreight, carrier, schedule, transit, free, valid, note, listStr[0],
                listStr[1], listStr[2]);

        call.enqueue(new Callback<Import>() {
            @Override
            public void onResponse(@NonNull Call<Import> call, @NonNull Response<Import> response) {
            }

            @Override
            public void onFailure(@NonNull Call<Import> call, @NonNull Throwable t) {
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

        Objects.requireNonNull(binding.tfOf20.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {

                    Objects.requireNonNull(binding.tfTotalFreight.getEditText()).setText(
                            totalFreight(binding.tfOf20.getEditText().getText().toString(),
                                    Objects.requireNonNull(binding.tfSur20.getEditText()).getText().toString()));

                } catch (Exception exception) {
                    //Toast.makeText(getContext(), exception.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        Objects.requireNonNull(binding.tfSur20.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Objects.requireNonNull(binding.tfTotalFreight.getEditText()).setText(
                            totalFreight(binding.tfOf20.getEditText().getText().toString(),
                                    Objects.requireNonNull(binding.tfSur20.getEditText()).getText().toString()));

                } catch (Exception e) {
                    //Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Get total of of20 and sur20
     *
     * @param of  of20
     * @param sur sur40
     * @return total of of20 and sur20
     */
    private String totalFreight(String of, String sur) {
        double numOf, numSur;

        if (TextUtils.isEmpty(of)) {
            numOf = 0;
        } else numOf = Double.parseDouble(of);

        if (TextUtils.isEmpty(sur)) {
            numSur = 0;
        } else numSur = Double.parseDouble(sur);

        double total = numOf + numSur;

        return String.valueOf(total);
    }
}