package com.example.demoapp.view.dialog.air.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.databinding.FragmentRetailExportSaleDetailBinding;
import com.example.demoapp.model.RetailGoods;
import com.example.demoapp.utilities.Constants;


public class FragmentRetailExportSaleDetail extends DialogFragment {

    private FragmentRetailExportSaleDetailBinding mRetailExportSaleDetailBinding;
    private Bundle mBundle;

    public static  FragmentRetailExportSaleDetail getInstance(){
        return  new FragmentRetailExportSaleDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRetailExportSaleDetailBinding = FragmentRetailExportSaleDetailBinding.inflate(inflater, container, false);
        View view = mRetailExportSaleDetailBinding.getRoot();

        mBundle = getArguments();
        if(mBundle != null){
            RetailGoods retailGoods = (RetailGoods) mBundle.getSerializable(Constants.RETAIL_GOODS);
            mBundle.putSerializable(Constants.RETAIL_GOODS_UPDATE, retailGoods);
            setDataRetailGoods(retailGoods);
        }
        return view;
    }
    private void setDataRetailGoods(RetailGoods retailGoods) {
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsStt.setText(retailGoods.getStt());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsPol.setText(retailGoods.getPol());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsPod.setText(retailGoods.getPod());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsDim.setText(retailGoods.getDim());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsGrossweight.setText(retailGoods.getGrossweight());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsTypeofcargo.setText(retailGoods.getTypeofcargo());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsOceanFreight.setText(retailGoods.getOceanfreight());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsLocalcharge.setText(retailGoods.getLocalcharge());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsCarrier.setText(retailGoods.getCarrier());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsSchedule.setText(retailGoods.getSchedule());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsTransittime.setText(retailGoods.getTransittime());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsValid.setText(retailGoods.getValid());
        mRetailExportSaleDetailBinding.tvRowPriceRetailGoodsNote.setText(retailGoods.getNote());


    }
}