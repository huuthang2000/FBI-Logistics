package com.example.demoapp.view.dialog.air.air_import;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentInsertAirImportDialogBinding;
import com.example.demoapp.model.AirImport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.AirImportViewModel;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InsertAirImportDialog extends DialogFragment implements View.OnClickListener {
    private final String[] listStr = new String[2];
    private FragmentInsertAirImportDialogBinding mInsertAirImportDialogBinding;
    private ArrayAdapter<String> adapterItemsMonth, adapterItemsContinent;

    private AirImportViewModel mAirViewModel;
    private List<AirImport> airList = new ArrayList<>();
    private CommunicateViewModel mCommunicateViewModel;
    private Bundle bundle;
    private AirImport mAirImport;
    Calendar calendar;
    public static InsertAirImportDialog insertDiaLogAIRImport(){
        return new InsertAirImportDialog();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInsertAirImportDialogBinding = FragmentInsertAirImportDialogBinding.inflate(inflater, container, false);
        View view = mInsertAirImportDialogBinding.getRoot();

        mAirViewModel = new ViewModelProvider(this).get(AirImportViewModel.class);
        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        bundle = getArguments();
        insertInformationImport();

        initView();
        eventOnclick();
        showDatePicker();
        return view;
    }

    private void insertInformationImport() {
        if(bundle != null){
            mAirImport = (AirImport) bundle.getSerializable(Constants.AIR_IMPORT_UPDATE);

            mInsertAirImportDialogBinding.insertAutoMonth.setText(mAirImport.getMonth());
            mInsertAirImportDialogBinding.insertAutoContinent.setText(mAirImport.getContinent());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfAolAirImport.getEditText()).setText(mAirImport.getAol());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfAodAirImport.getEditText()).setText(mAirImport.getAod());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfDimAirImport.getEditText()).setText(mAirImport.getDim());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfGrossAirImport.getEditText()).setText(mAirImport.getGrossweight());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfTypeofcargoAirImport.getEditText()).setText(mAirImport.getTypeofcargo());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfAirfreightAirImport.getEditText()).setText(mAirImport.getAirfreight());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfSurchargeAirImport.getEditText()).setText(mAirImport.getSurcharge());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfAirlinesAirImport.getEditText()).setText(mAirImport.getAirlines());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfScheduleAirImport.getEditText()).setText(mAirImport.getSchedule());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfTfTransitTimeAirImport.getEditText()).setText(mAirImport.getTransittime());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfValidAirImport.getEditText()).setText(mAirImport.getValid());
            Objects.requireNonNull(mInsertAirImportDialogBinding.tfNotesAirImport.getEditText()).setText(mAirImport.getNote());


        }
    }
    private String getCreatedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }


    private void eventOnclick() {
        mInsertAirImportDialogBinding.btnFunctionAddAirImport.setOnClickListener(this);
        mInsertAirImportDialogBinding.btnFunctionCancelAirImport.setOnClickListener(this);
        mInsertAirImportDialogBinding.tfValidAirImport.setOnClickListener(this);

    }

    private void initView() {
        adapterItemsMonth = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        adapterItemsContinent = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        mInsertAirImportDialogBinding.insertAutoMonth.setAdapter(adapterItemsMonth);
        mInsertAirImportDialogBinding.insertAutoContinent.setAdapter(adapterItemsContinent);


        mInsertAirImportDialogBinding.insertAutoMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listStr[0] = adapterView.getItemAtPosition(i).toString();
            }
        });

        mInsertAirImportDialogBinding.insertAutoContinent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listStr[1] = adapterView.getItemAtPosition(i).toString();
            }
        });

        setCancelable(false);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_function_add_air_import:
                if(isFilled()) {
                    insertAirImport();
                    dismiss();
                }else{
                    Toast.makeText(getContext(), Constants.INSERT_FAILED, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_function_cancel_air_import:
                dismiss();
                break;
        }
    }

    public void showDatePicker() {

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date");

        final MaterialDatePicker<Long> materialDatePicker = builder.build();

        mInsertAirImportDialogBinding.edtValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getParentFragmentManager(), "Date_Picker");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {

                        TimeZone timeZoneUTC = TimeZone.getDefault();
                        // It will be negative, so that's the -1
                        int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                        // Create a date format, then a date object with our offset
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        Date date = new Date(selection + offsetFromUTC);

                        Objects.requireNonNull(mInsertAirImportDialogBinding.tfValidAirImport.getEditText()).setText(simpleFormat.format(date));
                    }
                });
            }
        });

    }

    public boolean isFilled() {
        boolean result = true;

        if (TextUtils.isEmpty(mInsertAirImportDialogBinding.insertAutoContinent.getText())) {
            result = false;
            mInsertAirImportDialogBinding.insertAutoContinent.setError(Constants.ERROR_AUTO_COMPLETE_CONTINENT);
        }

        if (TextUtils.isEmpty(mInsertAirImportDialogBinding.insertAutoMonth.getText())) {
            result = false;
            mInsertAirImportDialogBinding.insertAutoMonth.setError(Constants.ERROR_AUTO_COMPLETE_MONTH);
        }


        if (TextUtils.isEmpty(Objects.requireNonNull(mInsertAirImportDialogBinding.tfAolAirImport.getEditText()).getText().toString())) {
            result = false;
            mInsertAirImportDialogBinding.tfAolAirImport.setError(Constants.ERROR_POL);
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(mInsertAirImportDialogBinding.tfAodAirImport.getEditText()).getText().toString())) {
            result = false;
            mInsertAirImportDialogBinding.tfAodAirImport.setError(Constants.ERROR_POD);
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(mInsertAirImportDialogBinding.tfValidAirImport.getEditText()).getText().toString())) {
            result = false;
            mInsertAirImportDialogBinding.tfValidAirImport.setError(Constants.ERROR_VALID);
        }

        return result;
    }

    private void insertAirImport() {
        String stPol = mInsertAirImportDialogBinding.tfAolAirImport.getEditText().getText().toString();
        String stPod = mInsertAirImportDialogBinding.tfAodAirImport.getEditText().getText().toString();
        String stDim = mInsertAirImportDialogBinding.tfDimAirImport.getEditText().getText().toString();
        String stGross = mInsertAirImportDialogBinding.tfGrossAirImport.getEditText().getText().toString();
        String stType = mInsertAirImportDialogBinding.tfTypeofcargoAirImport.getEditText().getText().toString();
        String stFreight = mInsertAirImportDialogBinding.tfAirfreightAirImport.getEditText().getText().toString();
        String stSurcharge = mInsertAirImportDialogBinding.tfSurchargeAirImport.getEditText().getText().toString();
        String stLines = mInsertAirImportDialogBinding.tfAirlinesAirImport.getEditText().getText().toString();
        String stSchedule = mInsertAirImportDialogBinding.tfScheduleAirImport.getEditText().getText().toString();
        String stTransittime = mInsertAirImportDialogBinding.tfTfTransitTimeAirImport.getEditText().getText().toString();
        String stValid = mInsertAirImportDialogBinding.tfValidAirImport.getEditText().getText().toString();
        String stNote = mInsertAirImportDialogBinding.tfNotesAirImport.getEditText().getText().toString();


        mCommunicateViewModel.makeChanges();
        Call<AirImport> call = mAirViewModel.insertAir(stPol,stPod, stDim, stGross, stType, stFreight,
                stSurcharge, stLines, stSchedule, stTransittime, stValid, stNote, listStr[0], listStr[1], getCreatedDate());
        call.enqueue(new Callback<AirImport>() {
            @Override
            public void onResponse(Call<AirImport> call, Response<AirImport> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Created Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AirImport> call, Throwable t) {

            }
        });
    }
}