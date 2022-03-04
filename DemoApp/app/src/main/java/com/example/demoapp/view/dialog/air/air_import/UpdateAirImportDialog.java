package com.example.demoapp.view.dialog.air.air_import;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentUpdateAirImportDialogBinding;
import com.example.demoapp.model.AirImport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.AirImportViewModel;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

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


public class UpdateAirImportDialog extends DialogFragment implements View.OnClickListener {
    private FragmentUpdateAirImportDialogBinding mAirImportDialogBinding;
    private final String[] listPriceAirImport = new String[2];
    private AirImportViewModel mAirImportViewModel;
    private Bundle mBundle;
    private AirImport mAirImport;
    private CommunicateViewModel mCommunicateViewModel;
    public static UpdateAirImportDialog getInstance(){
        return  new UpdateAirImportDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAirImportDialogBinding = FragmentUpdateAirImportDialogBinding.inflate(inflater, container, false);
        View view = mAirImportDialogBinding.getRoot();

        mAirImportViewModel = new ViewModelProvider(this).get(AirImportViewModel.class);
         mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);

        mBundle = getArguments();
        updateInformationImport();
        unit();
        showDatePicker();
        setUpButtons();

        return view;
    }

    public void showDatePicker() {

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date");

        final MaterialDatePicker<Long> materialDatePicker = builder.build();

        mAirImportDialogBinding.edtValid.setOnClickListener(new View.OnClickListener() {
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

                        Objects.requireNonNull(mAirImportDialogBinding.tfValidAirImport.getEditText()).setText(simpleFormat.format(date));
                    }
                });
            }
        });

    }

    private void updateInformationImport() {
        if(mBundle != null){
            mAirImport = (AirImport) mBundle.getSerializable(Constants.AIR_IMPORT_UPDATE);

            mAirImportDialogBinding.insertAutoMonth.setText(mAirImport.getMonth());
            mAirImportDialogBinding.insertAutoContinent.setText(mAirImport.getContinent());
            Objects.requireNonNull(mAirImportDialogBinding.tfPolAirImport.getEditText()).setText(mAirImport.getAol());
            Objects.requireNonNull(mAirImportDialogBinding.tfPodAirImport.getEditText()).setText(mAirImport.getAod());
            Objects.requireNonNull(mAirImportDialogBinding.tfDimAirImport.getEditText()).setText(mAirImport.getDim());
            Objects.requireNonNull(mAirImportDialogBinding.tfGrossAirImport.getEditText()).setText(mAirImport.getGrossweight());
            Objects.requireNonNull(mAirImportDialogBinding.tfTypeofcargoAirImport.getEditText()).setText(mAirImport.getTypeofcargo());
            Objects.requireNonNull(mAirImportDialogBinding.tfAirfreightAirImport.getEditText()).setText(mAirImport.getAirfreight());
            Objects.requireNonNull(mAirImportDialogBinding.tfSurchargeAirImport.getEditText()).setText(mAirImport.getSurcharge());
            Objects.requireNonNull(mAirImportDialogBinding.tfAirlinesAirImport.getEditText()).setText(mAirImport.getAirlines());
            Objects.requireNonNull(mAirImportDialogBinding.tfScheduleAirImport.getEditText()).setText(mAirImport.getSchedule());
            Objects.requireNonNull(mAirImportDialogBinding.tfTfTransitTimeAirImport.getEditText()).setText(mAirImport.getTransittime());
            Objects.requireNonNull(mAirImportDialogBinding.tfValidAirImport.getEditText()).setText(mAirImport.getValid());
            Objects.requireNonNull(mAirImportDialogBinding.tfNotesAirImport.getEditText()).setText(mAirImport.getNote());



        }
    }

    private void unit() {
        ArrayAdapter<String> arrayAdapterItemsMonth = new ArrayAdapter<String>(getContext(),
                R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> arrayAdapterItemsContinent = new ArrayAdapter<String>(getContext(),
                R.layout.dropdown_item,Constants.ITEMS_CONTINENT);


        mAirImportDialogBinding.insertAutoMonth.setAdapter(arrayAdapterItemsMonth);
        mAirImportDialogBinding.insertAutoContinent.setAdapter(arrayAdapterItemsContinent);

        listPriceAirImport[0] = mAirImportDialogBinding.insertAutoMonth.getText().toString();
        listPriceAirImport[1] = mAirImportDialogBinding.insertAutoContinent.getText().toString();

        mAirImportDialogBinding.insertAutoMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listPriceAirImport[0]= adapterView.getItemAtPosition(i).toString();
            }
        });

        mAirImportDialogBinding.insertAutoContinent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listPriceAirImport[1] = adapterView.getItemAtPosition(i).toString();
            }
        });


    }

    private void setUpButtons() {
        mAirImportDialogBinding.btnFunctionAddAirImport.setOnClickListener(this);
        mAirImportDialogBinding.btnFunctionUpdateAirImport.setOnClickListener(this);
        mAirImportDialogBinding.btnFunctionCancelAirImport.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_function_update_air_import:
                updateAirImport();
                dismiss();
                break;
            case R.id.btn_function_add_air_import:
                insertAirImport();
                dismiss();
                break;
            case R.id.btn_function_cancel_air_import:
                dismiss();
                break;
        }
    }

    private String getCreatedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }


    private void insertAirImport() {
        String strAol = Objects.requireNonNull(mAirImportDialogBinding.tfPolAirImport.getEditText()).getText().toString();
        String strAod = Objects.requireNonNull(mAirImportDialogBinding.tfPodAirImport.getEditText()).getText().toString();
        String strDim = Objects.requireNonNull(mAirImportDialogBinding.tfDimAirImport.getEditText()).getText().toString();
        String strGross = Objects.requireNonNull(mAirImportDialogBinding.tfGrossAirImport.getEditText()).getText().toString();
        String strType = Objects.requireNonNull(mAirImportDialogBinding.tfTypeofcargoAirImport.getEditText()).getText().toString();
        String strFreight = Objects.requireNonNull(mAirImportDialogBinding.tfAirfreightAirImport.getEditText()).getText().toString();
        String strSurcharge = Objects.requireNonNull(mAirImportDialogBinding.tfSurchargeAirImport.getEditText()).getText().toString();
        String strLine = Objects.requireNonNull(mAirImportDialogBinding.tfAirlinesAirImport.getEditText()).getText().toString();
        String strSchedule = Objects.requireNonNull(mAirImportDialogBinding.tfScheduleAirImport.getEditText()).getText().toString();
        String strTransittime = Objects.requireNonNull(mAirImportDialogBinding.tfTfTransitTimeAirImport.getEditText()).getText().toString();
        String strValid = Objects.requireNonNull(mAirImportDialogBinding.tfValidAirImport.getEditText()).getText().toString();
        String strNotes = Objects.requireNonNull(mAirImportDialogBinding.tfNotesAirImport.getEditText()).getText().toString();

        mCommunicateViewModel.makeChanges();
        Call<AirImport> call = mAirImportViewModel.insertAir(strAol, strAod, strDim, strGross, strType,
                strFreight, strSurcharge, strLine, strSchedule, strTransittime, strValid, strNotes,
                listPriceAirImport[0], listPriceAirImport[1], getCreatedDate());
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

    private void updateAirImport() {
        String strAol = Objects.requireNonNull(mAirImportDialogBinding.tfPolAirImport.getEditText()).getText().toString();
        String strAod = Objects.requireNonNull(mAirImportDialogBinding.tfPodAirImport.getEditText()).getText().toString();
        String strDim = Objects.requireNonNull(mAirImportDialogBinding.tfDimAirImport.getEditText()).getText().toString();
        String strGross = Objects.requireNonNull(mAirImportDialogBinding.tfGrossAirImport.getEditText()).getText().toString();
        String strType = Objects.requireNonNull(mAirImportDialogBinding.tfTypeofcargoAirImport.getEditText()).getText().toString();
        String strFreight = Objects.requireNonNull(mAirImportDialogBinding.tfAirfreightAirImport.getEditText()).getText().toString();
        String strSurcharge = Objects.requireNonNull(mAirImportDialogBinding.tfSurchargeAirImport.getEditText()).getText().toString();
        String strLine = Objects.requireNonNull(mAirImportDialogBinding.tfAirlinesAirImport.getEditText()).getText().toString();
        String strSchedule = Objects.requireNonNull(mAirImportDialogBinding.tfScheduleAirImport.getEditText()).getText().toString();
        String strTransittime = Objects.requireNonNull(mAirImportDialogBinding.tfTfTransitTimeAirImport.getEditText()).getText().toString();
        String strValid = Objects.requireNonNull(mAirImportDialogBinding.tfValidAirImport.getEditText()).getText().toString();
        String strNotes = Objects.requireNonNull(mAirImportDialogBinding.tfNotesAirImport.getEditText()).getText().toString();

        mCommunicateViewModel.makeChanges();
        Call<AirImport> call = mAirImportViewModel.updateAir(mAirImport.getStt(), strAol, strAod, strDim, strGross, strType,
                strFreight, strSurcharge, strLine, strSchedule, strTransittime, strValid, strNotes,
                listPriceAirImport[0], listPriceAirImport[1]);
        call.enqueue(new Callback<AirImport>() {
            @Override
            public void onResponse(Call<AirImport> call, Response<AirImport> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Update Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AirImport> call, Throwable t) {

            }
        });
    }
}