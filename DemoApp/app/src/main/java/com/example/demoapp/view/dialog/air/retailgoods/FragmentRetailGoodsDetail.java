package com.example.demoapp.view.dialog.air.retailgoods;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentRetailGoodsDetailBinding;
import com.example.demoapp.model.RetailGoods;
import com.example.demoapp.utilities.Constants;


public class FragmentRetailGoodsDetail extends DialogFragment implements View.OnClickListener {
    private FragmentRetailGoodsDetailBinding mRetailGoodsDetailBinding;
    private Bundle mBundle;

    public static FragmentRetailGoodsDetail getInstance(){
        return  new FragmentRetailGoodsDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRetailGoodsDetailBinding = FragmentRetailGoodsDetailBinding.inflate(inflater, container, false);
        View view = mRetailGoodsDetailBinding.getRoot();

        mBundle = getArguments();
        if(mBundle != null){
            RetailGoods retailGoods = (RetailGoods) mBundle.getSerializable(Constants.RETAIL_GOODS);
            mBundle.putSerializable(Constants.RETAIL_GOODS_UPDATE, retailGoods);
            setDataRetailGoods(retailGoods);
        }
        mRetailGoodsDetailBinding.btnUpdateRetailGoods.setOnClickListener(this);
        return view;
    }

    private void setDataRetailGoods(RetailGoods retailGoods) {
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsStt.setText(retailGoods.getStt());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsPol.setText(retailGoods.getPol());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsPod.setText(retailGoods.getPod());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsDim.setText(retailGoods.getDim());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsGrossweight.setText(retailGoods.getGrossweight());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsTypeofcargo.setText(retailGoods.getTypeofcargo());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsOceanFreight.setText(retailGoods.getOceanfreight());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsLocalcharge.setText(retailGoods.getLocalcharge());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsCarrier.setText(retailGoods.getCarrier());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsSchedule.setText(retailGoods.getSchedule());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsTransittime.setText(retailGoods.getTransittime());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsValid.setText(retailGoods.getValid());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsNote.setText(retailGoods.getNote());
        mRetailGoodsDetailBinding.tvRowPriceRetailGoodsDateCreated.setText(retailGoods.getDate_created());



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_retail_goods:
                DialogFragment fragment = UpdateRetailGoodsDialog.getInstance();
                fragment.setArguments(mBundle);
                fragment.show(getParentFragmentManager(), "Update");
        }
    }
}