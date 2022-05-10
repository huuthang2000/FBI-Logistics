package com.example.demoapp.view.dialog.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentLogDetailBinding;
import com.example.demoapp.model.Log;
import com.example.demoapp.utilities.Constants;


public class FragmentLogDetail extends DialogFragment implements View.OnClickListener {

    private FragmentLogDetailBinding mDetailBinding;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDetailBinding = FragmentLogDetailBinding.inflate(inflater, container, false);
        View view = mDetailBinding.getRoot();
        bundle = getArguments();
        if(bundle != null){
            Log log = (Log) bundle.getSerializable(Constants.LOG_OBJECT);
            bundle.putSerializable(Constants.LOG_UPDATE, log);
            setDataLog(log);
        }
        mDetailBinding.btnUpdateLog.setOnClickListener(this);
        return view;
    }
    public static FragmentLogDetail getInstance(){

        return new FragmentLogDetail();
    }

    private void setDataLog(Log log){
        mDetailBinding.tvRowPriceLogStt.setText(log.getStt());
        mDetailBinding.tvRowPriceLogTenhang.setText(log.getTenhang());
        mDetailBinding.tvRowPriceLogHscode.setText(log.getHscode());
        mDetailBinding.tvRowPriceLogCongdung.setText(log.getCongdung());
        //mDetailBinding.tvRowPriceLogHinhanh.setText(log.getHinhanh());
        mDetailBinding.tvRowPriceLogCangdi.setText(log.getCangdi());
        mDetailBinding.tvRowPriceLogCangden.setText(log.getCangden());
        mDetailBinding.tvRowPriceLogLoaihang.setText(log.getLoaihang());
        mDetailBinding.tvRowPriceLogSoluongcuthe.setText(log.getSoluongcuthe());
        mDetailBinding.tvRowPriceLogYeucaudacbiet.setText(log.getYeucaudacbiet());
        mDetailBinding.tvRowPriceLogPrice.setText(log.getPrice());
        mDetailBinding.tvRowPriceLogType.setText(log.getType());
        mDetailBinding.tvRowPriceLogDateCreated.setText(log.getDate_created());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_log:
                DialogFragment fragment = UpdateLogFragment.getInstance();
                fragment.setArguments(bundle);
                fragment.show(getParentFragmentManager(), "Update");
                break;
        }
    }
}