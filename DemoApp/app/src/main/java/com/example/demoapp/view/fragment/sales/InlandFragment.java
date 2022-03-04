package com.example.demoapp.view.fragment.sales;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentInlandBinding;
import com.example.demoapp.view.activity.sale.rail.RailSaleActivity;
import com.example.demoapp.view.activity.sale.road.RoadSaleActivity;
import com.example.demoapp.view.activity.sale.waterway.WaterWayActivity;


public class InlandFragment extends Fragment implements View.OnClickListener {

    private FragmentInlandBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInlandBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setEventClick();
        return view;
    }

    private void setEventClick() {
        binding.cvRail.setOnClickListener(this);
        binding.cvRoad.setOnClickListener(this);
        binding.cvWaterway.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cv_rail:
                Intent intent = new Intent(getContext(), RailSaleActivity.class);
                startActivity(intent);
                break;
            case R.id.cv_road:
                Intent intent1 = new Intent(getContext(), RoadSaleActivity.class);
                startActivity(intent1);
                break;
            case R.id.cv_waterway:
                Intent intent2 = new Intent(getContext(), WaterWayActivity.class);
                startActivity(intent2);
                break;
        }
    }
}