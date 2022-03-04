package com.example.demoapp.view.fragment.sales;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentAirlinesBinding;
import com.example.demoapp.view.activity.sale.TablePriceAirActivity;
import com.example.demoapp.view.activity.sale.air.AirImportSaleActivity;

public class AirlinesSaleFragment extends Fragment implements  View.OnClickListener{

    private FragmentAirlinesBinding airlinesBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        airlinesBinding = FragmentAirlinesBinding.inflate(inflater, container, false);
        View view = airlinesBinding.getRoot();

        event();
        return  view;
    }

    /**
     * Event handler for cardview
     */
    private void event(){
        airlinesBinding.cvCargoNK.setOnClickListener(this);
        airlinesBinding.cvCargoXK.setOnClickListener(this);
//        airlinesBinding.cvExpressNK.setOnClickListener(this);
//        airlinesBinding.cvExpressXK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_cargoXK:
                Intent intent = new Intent(getContext(), TablePriceAirActivity.class);
                startActivity(intent);
                break;
            case R.id.cv_cargoNK:
                Intent intent1 = new Intent(getContext(), AirImportSaleActivity.class);
                startActivity(intent1);
                break;
        }

    }
}