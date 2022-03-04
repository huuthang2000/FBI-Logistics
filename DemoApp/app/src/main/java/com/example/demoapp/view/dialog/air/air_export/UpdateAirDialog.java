package com.example.demoapp.view.dialog.air.air_export;

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
import com.example.demoapp.databinding.FragmentUpdateAirDialogBinding;
import com.example.demoapp.model.AirExport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.AirExportViewModel;
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


public class UpdateAirDialog extends DialogFragment implements View.OnClickListener {

    private FragmentUpdateAirDialogBinding mUpdateAir;
    private final String[] listPriceAir = new String[2];
    private AirExportViewModel mAirViewModel;
    private CommunicateViewModel mCommunicateViewModel;
    private Bundle mBundle;
    private AirExport mAir;

    public static UpdateAirDialog getInstance() {
        return new UpdateAirDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUpdateAir = FragmentUpdateAirDialogBinding.inflate(inflater, container,false);
        View view = mUpdateAir.getRoot();

        mAirViewModel = new ViewModelProvider(this).get(AirExportViewModel.class);
        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);

        mBundle = getArguments();
        updateInformation();
        unit();
        buttonEvent();

        return view;
    }

    private void buttonEvent() {
        mUpdateAir.btnFunctionInsertExport.setOnClickListener(this);
        mUpdateAir.btnFunctionUpdateExport.setOnClickListener(this);
        mUpdateAir.btnFunctionCancelExport.setOnClickListener(this);
    }



    private void unit() {
        ArrayAdapter<String> arrayAdapterItemsMonth = new ArrayAdapter<String>(getContext(),
                R.layout.dropdown_item,Constants.ITEMS_MONTH);
        ArrayAdapter<String> arrayAdapterItemsContinent = new ArrayAdapter<String>(getContext(),
                R.layout.dropdown_item,Constants.ITEMS_CONTINENT);

        mUpdateAir.updateAutoMonth.setAdapter(arrayAdapterItemsMonth);
        mUpdateAir.updateAutoContinent.setAdapter(arrayAdapterItemsContinent);

        listPriceAir[0] = mUpdateAir.updateAutoMonth.getText().toString();
        listPriceAir[1] = mUpdateAir.updateAutoContinent.getText().toString();

        mUpdateAir.updateAutoMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listPriceAir[0] = adapterView.getItemAtPosition(i).toString();
            }
        });

        mUpdateAir.updateAutoContinent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listPriceAir[1] = adapterView.getItemAtPosition(i).toString();
            }
        });

        setCancelable(false);

    }

    private void updateInformation() {
        if(mBundle != null){
            mAir = (AirExport) mBundle.getSerializable(Constants.AIR_UPDATE);

            mUpdateAir.updateAutoMonth.setText(mAir.getMonth());
            mUpdateAir.updateAutoContinent.setText(mAir.getContinent());
            Objects.requireNonNull(mUpdateAir.tfAol.getEditText()).setText(mAir.getAol());
            Objects.requireNonNull(mUpdateAir.tfAod.getEditText()).setText(mAir.getAod());
            Objects.requireNonNull(mUpdateAir.tfDim.getEditText()).setText(mAir.getDim());
            Objects.requireNonNull(mUpdateAir.tfGross.getEditText()).setText(mAir.getGrossweight());
            Objects.requireNonNull(mUpdateAir.tfTypeofcargo.getEditText()).setText(mAir.getTypeofcargo());
            Objects.requireNonNull(mUpdateAir.tfAirfreight.getEditText()).setText(mAir.getAirfreight());
            Objects.requireNonNull(mUpdateAir.tfSurcharge.getEditText()).setText(mAir.getSurcharge());
            Objects.requireNonNull(mUpdateAir.tfAirlines.getEditText()).setText(mAir.getAirlines());
            Objects.requireNonNull(mUpdateAir.tfSchedule.getEditText()).setText(mAir.getSchedule());
            Objects.requireNonNull(mUpdateAir.tfTfTransitTime.getEditText()).setText(mAir.getTransittime());
            Objects.requireNonNull(mUpdateAir.tfValid.getEditText()).setText(mAir.getValid());
            Objects.requireNonNull(mUpdateAir.tfNotes.getEditText()).setText(mAir.getNote());
        }
    }

    private void updateAir() {
        String strAol = Objects.requireNonNull(mUpdateAir.tfAol.getEditText()).getText().toString();
        String strAod = Objects.requireNonNull(mUpdateAir.tfAod.getEditText()).getText().toString();
        String strDim = Objects.requireNonNull(mUpdateAir.tfDim.getEditText()).getText().toString();
        String strGross = Objects.requireNonNull(mUpdateAir.tfGross.getEditText()).getText().toString();
        String strType = Objects.requireNonNull(mUpdateAir.tfTypeofcargo.getEditText()).getText().toString();
        String strAirFreight = Objects.requireNonNull(mUpdateAir.tfAirfreight.getEditText()).getText().toString();
        String strSurcharge = Objects.requireNonNull(mUpdateAir.tfSurcharge.getEditText()).getText().toString();
        String strAirLines = Objects.requireNonNull(mUpdateAir.tfAirlines.getEditText()).getText().toString();
        String strSchedule = Objects.requireNonNull(mUpdateAir.tfSchedule.getEditText()).getText().toString();
        String strTransittime = Objects.requireNonNull(mUpdateAir.tfTfTransitTime.getEditText()).getText().toString();
        String strValid = Objects.requireNonNull(mUpdateAir.tfValid.getEditText()).getText().toString();
        String strNote = Objects.requireNonNull(mUpdateAir.tfNotes.getEditText()).getText().toString();

        mCommunicateViewModel.makeChanges();
        Call<AirExport> call = mAirViewModel.updateAir(mAir.getStt(), strAol, strAod, strDim, strGross, strType, strAirFreight,strSurcharge,
                strAirLines, strSchedule, strTransittime, strValid, strNote, listPriceAir[0] , listPriceAir[1]);

        call.enqueue(new Callback<AirExport>() {
            @Override
            public void onResponse(Call<AirExport> call, Response<AirExport> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Update Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AirExport> call, Throwable t) {

            }
        });

    }
    private String getCreatedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public void showDatePicker() {

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date");

        final MaterialDatePicker<Long> materialDatePicker = builder.build();

        mUpdateAir.edtValid.setOnClickListener(new View.OnClickListener() {
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

                        Objects.requireNonNull(mUpdateAir.tfValid.getEditText()).setText(simpleFormat.format(date));
                    }
                });
            }
        });

    }

    private void insertAirExport() {
        String strAol = Objects.requireNonNull(mUpdateAir.tfAol.getEditText()).getText().toString();
        String strAod = Objects.requireNonNull(mUpdateAir.tfAod.getEditText()).getText().toString();
        String strDim = Objects.requireNonNull(mUpdateAir.tfDim.getEditText()).getText().toString();
        String strGross = Objects.requireNonNull(mUpdateAir.tfGross.getEditText()).getText().toString();
        String strType = Objects.requireNonNull(mUpdateAir.tfTypeofcargo.getEditText()).getText().toString();
        String strAirFreight = Objects.requireNonNull(mUpdateAir.tfAirfreight.getEditText()).getText().toString();
        String strSurcharge = Objects.requireNonNull(mUpdateAir.tfSurcharge.getEditText()).getText().toString();
        String strAirLines = Objects.requireNonNull(mUpdateAir.tfAirlines.getEditText()).getText().toString();
        String strSchedule = Objects.requireNonNull(mUpdateAir.tfSchedule.getEditText()).getText().toString();
        String strTransittime = Objects.requireNonNull(mUpdateAir.tfTfTransitTime.getEditText()).getText().toString();
        String strValid = Objects.requireNonNull(mUpdateAir.tfValid.getEditText()).getText().toString();
        String strNote = Objects.requireNonNull(mUpdateAir.tfNotes.getEditText()).getText().toString();

        mCommunicateViewModel.makeChanges();
        Call<AirExport> call = mAirViewModel.insertAir(strAol, strAod, strDim, strGross, strType, strAirFreight,strSurcharge,
                strAirLines, strSchedule, strTransittime, strValid, strNote, listPriceAir[0] , listPriceAir[1], getCreatedDate());
        call.enqueue(new Callback<AirExport>() {
            @Override
            public void onResponse(Call<AirExport> call, Response<AirExport> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Inserr Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AirExport> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_function_update_export:
                updateAir();
                dismiss();
                break;
            case R.id.btn_function_insert_export:
                insertAirExport();
                dismiss();
                break;
            case R.id.btn_function_cancel_export:
                dismiss();
                break;
        }
    }




}