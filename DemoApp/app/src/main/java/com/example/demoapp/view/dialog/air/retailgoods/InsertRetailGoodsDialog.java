package com.example.demoapp.view.dialog.air.retailgoods;

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
import com.example.demoapp.databinding.FragmentInsertRetailGoodsDialogBinding;
import com.example.demoapp.model.RetailGoods;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.RetailGoodsViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

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


public class InsertRetailGoodsDialog extends DialogFragment implements View.OnClickListener {
    private final String[] listCM = new String[2];
    private FragmentInsertRetailGoodsDialogBinding mRetailGoodsDialogBinding;
    private ArrayAdapter<String> adapterItemsMonth, adapterItemsContinent;
    private RetailGoodsViewModel mRetailGoodsViewModel;
    private List<RetailGoods> retailGoods = new ArrayList<>();
    private CommunicateViewModel mCommunicateViewModel;

    public static  InsertRetailGoodsDialog insertDialogRetailGoods(){
        return  new InsertRetailGoodsDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRetailGoodsDialogBinding = FragmentInsertRetailGoodsDialogBinding.inflate(inflater, container, false);
        View view = mRetailGoodsDialogBinding.getRoot();

        mRetailGoodsViewModel = new ViewModelProvider(this).get(RetailGoodsViewModel.class);
        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);

        initView();
        setUpButtons();
        showDatePicker();
        return view;
    }
    private String getCreatedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
    public void showDatePicker() {

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date");

        final MaterialDatePicker<Long> materialDatePicker = builder.build();

        mRetailGoodsDialogBinding.edtValid.setOnClickListener(new View.OnClickListener() {
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

                        Objects.requireNonNull(mRetailGoodsDialogBinding.tfValidRetailGoods.getEditText()).setText(simpleFormat.format(date));
                    }
                });
            }
        });

    }

    private void setUpButtons() {
        mRetailGoodsDialogBinding.btnFunctionAddRetailGoods.setOnClickListener(this);
        mRetailGoodsDialogBinding.btnFunctionCancelRetailGoods.setOnClickListener(this);
    }

    private void initView() {
        adapterItemsMonth = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        adapterItemsContinent = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        mRetailGoodsDialogBinding.insertAutoContinent.setAdapter(adapterItemsContinent);
        mRetailGoodsDialogBinding.insertAutoMonth.setAdapter(adapterItemsMonth);

        mRetailGoodsDialogBinding.insertAutoMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listCM[0] = adapterView.getItemAtPosition(i).toString();
            }
        });

        mRetailGoodsDialogBinding.insertAutoContinent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listCM[1] = adapterView.getItemAtPosition(i).toString();
            }
        });

        setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_function_add_retail_goods:
                if(isFilled()) {
                    insertRetailGoods();
                    dismiss();
                } else{
                    Toast.makeText(getContext(), Constants.INSERT_FAILED, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_function_cancel_retail_goods:
                dismiss();
                break;
        }
    }

    public boolean isFilled() {
        boolean result = true;

        if (TextUtils.isEmpty(mRetailGoodsDialogBinding.insertAutoContinent.getText())) {
            result = false;
            mRetailGoodsDialogBinding.insertAutoContinent.setError(Constants.ERROR_AUTO_COMPLETE_CONTINENT);
        }

        if (TextUtils.isEmpty(mRetailGoodsDialogBinding.insertAutoMonth.getText())) {
            result = false;
            mRetailGoodsDialogBinding.insertAutoMonth.setError(Constants.ERROR_AUTO_COMPLETE_MONTH);
        }


        if (TextUtils.isEmpty(Objects.requireNonNull(mRetailGoodsDialogBinding.tfPolRetailGoods.getEditText()).getText().toString())) {
            result = false;
            mRetailGoodsDialogBinding.tfPolRetailGoods.setError(Constants.ERROR_POL);
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(mRetailGoodsDialogBinding.tfPodRetailGoods.getEditText()).getText().toString())) {
            result = false;
            mRetailGoodsDialogBinding.tfPodRetailGoods.setError(Constants.ERROR_POD);
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(mRetailGoodsDialogBinding.tfValidRetailGoods.getEditText()).getText().toString())) {
            result = false;
            mRetailGoodsDialogBinding.tfValidRetailGoods.setError(Constants.ERROR_VALID);
        }

        return result;
    }

    private void insertRetailGoods() {
        String strPol = mRetailGoodsDialogBinding.tfPolRetailGoods.getEditText().getText().toString();
        String strPod = mRetailGoodsDialogBinding.tfPodRetailGoods.getEditText().getText().toString();
        String strDim = mRetailGoodsDialogBinding.tfDimRetailGoods.getEditText().getText().toString();
        String strGross = mRetailGoodsDialogBinding.tfGrossRetailGoods.getEditText().getText().toString();
        String strType = mRetailGoodsDialogBinding.tfTypeofcargoRetailGoods.getEditText().getText().toString();
        String strOceanFreight = mRetailGoodsDialogBinding.tfOceanAirfreightRetailGoods.getEditText().getText().toString();
        String strLocalCharge = mRetailGoodsDialogBinding.tfLocalchargeAirImport.getEditText().getText().toString();
        String strCarrier = mRetailGoodsDialogBinding.tfCarrierRetailGoods.getEditText().getText().toString();
        String strSchedule = mRetailGoodsDialogBinding.tfScheduleRetailGoods.getEditText().getText().toString();
        String strTransittime = mRetailGoodsDialogBinding.tfTfTransitTimeRetailGoods.getEditText().getText().toString();
        String strValid = mRetailGoodsDialogBinding.tfValidRetailGoods.getEditText().getText().toString();
        String strNotes = mRetailGoodsDialogBinding.tfNotesRetailGoods.getEditText().getText().toString();

        mCommunicateViewModel.makeChanges();
        Call<RetailGoods> call = mRetailGoodsViewModel.insertRetailGoodsExport(strPol, strPod, strDim, strGross, strType,
                strOceanFreight, strLocalCharge, strCarrier, strSchedule, strTransittime, strValid, strNotes,
                listCM[0], listCM[1], getCreatedDate());
        call.enqueue(new Callback<RetailGoods>() {
            @Override
            public void onResponse(Call<RetailGoods> call, Response<RetailGoods> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Created Successful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RetailGoods> call, Throwable t) {

            }
        });
    }
}