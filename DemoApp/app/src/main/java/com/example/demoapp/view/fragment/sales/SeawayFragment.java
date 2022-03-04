package com.example.demoapp.view.fragment.sales;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.demoapp.R;
import com.example.demoapp.databinding.FragmentSeawayBinding;
import com.example.demoapp.view.activity.sale.ContainerActivity;
import com.example.demoapp.view.activity.sale.ImportActivity;
import com.example.demoapp.view.activity.sale.ImportLclSaleActivity;
import com.example.demoapp.view.activity.sale.RetailGoodsExportActivity;


public class SeawayFragment extends Fragment implements View.OnClickListener{

    private FragmentSeawayBinding mSeawayBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSeawayBinding = FragmentSeawayBinding.inflate(inflater, container , false);
        View view = mSeawayBinding.getRoot();

        event();
        return view;
    }

    private void event() {

        mSeawayBinding.cvContainerXK.setOnClickListener(this);
        mSeawayBinding.cvContaierNK.setOnClickListener(this);
        mSeawayBinding.cvHangleNK.setOnClickListener(this);
        mSeawayBinding.cvHangleXK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_containerXK:
                Intent intent = new Intent(getContext(), ContainerActivity.class);
                startActivity(intent);
                break;
            case R.id.cv_contaierNK:
                Intent intent1 = new Intent(getContext(), ImportActivity.class);
                startActivity(intent1);
                break;
            case R.id.cv_hangleNK:
                Intent intent2 = new Intent(getContext(), ImportLclSaleActivity.class);
                startActivity(intent2);
                break;
            case R.id.cv_hangleXK:
                Intent intent3 = new Intent(getContext(), RetailGoodsExportActivity.class);
                startActivity(intent3);
                break;
        }

    }
}