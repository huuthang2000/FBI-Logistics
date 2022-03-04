package com.example.demoapp.view.dialog.air.air_export;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentDialogInsertAirBinding;
import com.example.demoapp.model.AirExport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.AirExportViewModel;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InsertAirExportDialog extends DialogFragment implements  View.OnClickListener {

    private final String[] listStr = new String[2];
    private FragmentDialogInsertAirBinding insertAirBinding;
    private ArrayAdapter<String>  adapterItemsMonth, adapterItemsContinent;

    private AirExportViewModel mAirViewModel;
    private List<AirExport> airList = new ArrayList<>();
    private CommunicateViewModel mCommunicateViewModel;

    public static InsertAirExportDialog insertDiaLogAIR(){
        return new InsertAirExportDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        insertAirBinding = FragmentDialogInsertAirBinding.inflate(inflater, container, false);

        View view = insertAirBinding.getRoot();

        mAirViewModel = new ViewModelProvider(this).get(AirExportViewModel.class);
        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);

        initView();
        showDatePicker();

        return view;

    }

    private String getCreatedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    private void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date");

        final MaterialDatePicker<Long> materialDatePicker = builder.build();

        insertAirBinding.edtValid.setOnClickListener(view -> {
            materialDatePicker.show(getParentFragmentManager(), "Date_Picker");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {

                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                Date date = new Date(selection + offsetFromUTC);

                Objects.requireNonNull(insertAirBinding.tfValid.getEditText()).setText(simpleFormat.format(date));
            });
        });
    }

    public boolean isFilled() {
        boolean result = true;

        if (TextUtils.isEmpty(insertAirBinding.insertAutoContinent.getText())) {
            result = false;
            insertAirBinding.insertAutoContinent.setError(Constants.ERROR_AUTO_COMPLETE_CONTINENT);
        }

        if (TextUtils.isEmpty(insertAirBinding.insertAutoMonth.getText())) {
            result = false;
            insertAirBinding.insertAutoMonth.setError(Constants.ERROR_AUTO_COMPLETE_MONTH);
        }


        if (TextUtils.isEmpty(Objects.requireNonNull(insertAirBinding.tfAol.getEditText()).getText().toString())) {
            result = false;
            insertAirBinding.tfAol.setError(Constants.ERROR_POL);
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(insertAirBinding.tfAod.getEditText()).getText().toString())) {
            result = false;
            insertAirBinding.tfAol.setError(Constants.ERROR_POD);
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(insertAirBinding.tfAod.getEditText()).getText().toString())) {
            result = false;
            insertAirBinding.tfValid.setError(Constants.ERROR_VALID);
        }

        return result;
    }

    private void initView() {
        adapterItemsMonth = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        adapterItemsContinent = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        insertAirBinding.insertAutoMonth.setAdapter(adapterItemsMonth);
        insertAirBinding.insertAutoContinent.setAdapter(adapterItemsContinent);

        insertAirBinding.btnFunctionAdd.setOnClickListener(this);
        insertAirBinding.btnFunctionCancel.setOnClickListener(this);

        insertAirBinding.insertAutoMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listStr[0] = adapterView.getItemAtPosition(i).toString();
            }
        });

        insertAirBinding.insertAutoContinent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listStr[1] = adapterView.getItemAtPosition(i).toString();
            }
        });

        setCancelable(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_function_add:
                if(isFilled()) {
                    insertAIR();
                    dismiss();
                }else{
                    Toast.makeText(getContext(), Constants.INSERT_FAILED, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_function_cancel:
                dismiss();
                break;
        }
    }

    private void insertAIR() {
        String stAol = insertAirBinding.tfAol.getEditText().getText().toString();
        String stAod = insertAirBinding.tfAod.getEditText().getText().toString();
        String stDim = insertAirBinding.tfDim.getEditText().getText().toString();
        String stGross = insertAirBinding.tfGross.getEditText().getText().toString();
        String stType = insertAirBinding.tfTypeofcargo.getEditText().getText().toString();
        String stFreight = insertAirBinding.tfAirfreight.getEditText().getText().toString();
        String stSurcharge = insertAirBinding.tfSurcharge.getEditText().getText().toString();
        String stLines = insertAirBinding.tfAirlines.getEditText().getText().toString();
        String stSchedule = insertAirBinding.tfSchedule.getEditText().getText().toString();
        String stTransittime = insertAirBinding.tfTfTransitTime.getEditText().getText().toString();
        String stValid = insertAirBinding.tfValid.getEditText().getText().toString();
        String stNote = insertAirBinding.tfNotes.getEditText().getText().toString();


        mCommunicateViewModel.makeChanges();
        Call<AirExport> call = mAirViewModel.insertAir(stAol,stAod, stDim, stGross, stType, stFreight,
                stSurcharge, stLines, stSchedule, stTransittime, stValid, stNote, listStr[0], listStr[1], getCreatedDate());
        call.enqueue(new Callback<AirExport>() {
            @Override
            public void onResponse(Call<AirExport> call, Response<AirExport> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Created Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AirExport> call, Throwable t) {

            }
        });

    }

//    public void resetEditText(){
//        Objects.requireNonNull(insertAirBinding.tfAol.getEditText()).setText("");
//        Objects.requireNonNull(insertAirBinding.tfAod.getEditText()).setText("");
//        Objects.requireNonNull(insertAirBinding.tfDim.getEditText()).setText("");
//        Objects.requireNonNull(insertAirBinding.tfGross.getEditText()).setText("");
//        Objects.requireNonNull(insertAirBinding.tfTypeofcargo.getEditText()).setText("");
//        Objects.requireNonNull(insertAirBinding.tfAirfreight.getEditText()).setText("");
//        Objects.requireNonNull(insertAirBinding.tfSurcharge.getEditText()).setText("");
//        Objects.requireNonNull(insertAirBinding.tfAirlines.getEditText()).setText("");
//        Objects.requireNonNull(insertAirBinding.tfSchedule.getEditText()).setText("");
//        Objects.requireNonNull(insertAirBinding.tfTfTransitTime.getEditText()).setText("");
//        Objects.requireNonNull(insertAirBinding.tfValid.getEditText()).setText("");
//        Objects.requireNonNull(insertAirBinding.tfNotes.getEditText()).setText("");
//

}