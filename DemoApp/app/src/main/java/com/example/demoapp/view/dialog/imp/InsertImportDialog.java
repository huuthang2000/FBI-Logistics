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
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.demoapp.R;
import com.example.demoapp.model.Import;
import com.example.demoapp.databinding.FragmentDialogInsertImportBinding;
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

public class InsertImportDialog extends DialogFragment implements View.OnClickListener {

    private final String[] listStr = new String[3];

    private FragmentDialogInsertImportBinding binding;
    private CommunicateViewModel mCommunicateViewModel;
    private ImportViewModel mImportViewModel;

    private Bundle bundle;

    /**
     * This method will set up view for insert dialog
     *
     * @param inflater           inflater
     * @param container          container
     * @param savedInstanceState save
     * @return view of Import insert dialog
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogInsertImportBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        mImportViewModel = new ViewModelProvider(this).get(ImportViewModel.class);
        mCommunicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);

        initView();
        showDatePicker();

        bundle = getArguments();
        setData();

        return root;
    }

    public void setData() {
        if (bundle != null) {
            Import imp = (Import) bundle.getSerializable(Constants.IMPORT_UPDATE);
            if ("YES".equalsIgnoreCase(bundle.getString(Constants.IMPORT_ADD_NEW))) {
                listStr[0] = imp.getType();
                listStr[1] = imp.getMonth();
                listStr[2] = imp.getContinent();

                binding.insertAutoContainer.setText(imp.getType(), false);
                binding.insertAutoMonth.setText(imp.getMonth(), false);
                binding.insertAutoContinent.setText(imp.getContinent(), false);

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

        binding.insertAutoContainer.setAdapter(adapterItemsType);
        binding.insertAutoMonth.setAdapter(adapterItemsMonth);
        binding.insertAutoContinent.setAdapter(adapterItemsContinent);

        // buttons
        binding.btnFunctionAdd.setOnClickListener(this);
        binding.btnFunctionCancel.setOnClickListener(this);

        binding.insertAutoContainer.setOnItemClickListener((adapterView, view, i, l) -> {
            listStr[0] = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(getContext(), listStr[0], Toast.LENGTH_LONG).show();
        });

        binding.insertAutoMonth.setOnItemClickListener((adapterView, view, i, l) -> {
            listStr[1] = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(getContext(), listStr[1], Toast.LENGTH_LONG).show();
        });

        binding.insertAutoContinent.setOnItemClickListener((adapterView, view, i, l) -> {
            listStr[2] = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(getContext(), listStr[2], Toast.LENGTH_LONG).show();
        });

        textWatcher();

        setCancelable(false);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_function_add:
                if (isFilled()) {
                    process();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), Constants.INSERT_FAILED, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_function_cancel:
                dismiss();
                break;
        }
    }

    public static InsertImportDialog insertDialog() {
        return new InsertImportDialog();
    }

    /**
     * This method will get information user typing and insert them into database
     */

    public void process() {

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
        Call<Import> call = mImportViewModel.insertImport(pol, pod, of20, of40, of45, sur20, sur40,
                sur45, totalFreight, carrier, schedule, transit, free, valid, note, listStr[0],
                listStr[1], listStr[2], getCreatedDate());

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
     * get current date
     *
     * @return date
     */
    private String getCreatedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public boolean isFilled() {
        boolean result = true;

        if (TextUtils.isEmpty(binding.insertAutoContainer.getText())) {
            result = false;
            binding.insertAutoContainer.setError(Constants.ERROR_AUTO_COMPLETE_TYPE);
        }

        if (TextUtils.isEmpty(binding.insertAutoMonth.getText())) {
            result = false;
            binding.insertAutoMonth.setError(Constants.ERROR_AUTO_COMPLETE_MONTH);
        }

        if (TextUtils.isEmpty(binding.insertAutoContinent.getText())) {
            result = false;
            binding.insertAutoContinent.setError(Constants.ERROR_AUTO_COMPLETE_CONTINENT);
        }

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
     * watcher for fields
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

        binding.insertAutoMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.insertAutoMonth.getText())) {
                    binding.insertAutoMonth.setError(null);

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.insertAutoContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.insertAutoContainer.getText())) {
                    binding.insertAutoContainer.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.insertAutoContinent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.insertAutoContinent.getText())) {
                    binding.insertAutoContinent.setError(null);
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
                            totalFreight(Objects.requireNonNull(binding.tfOf20.getEditText()).getText().toString(),
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
