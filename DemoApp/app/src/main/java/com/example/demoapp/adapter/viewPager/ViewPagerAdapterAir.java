package com.example.demoapp.adapter.viewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demoapp.view.fragment.air.AirExportFragment;
import com.example.demoapp.view.fragment.air.AirImportFragment;
import com.example.demoapp.view.fragment.air.RetailGoodsExportFragment;
import com.example.demoapp.view.fragment.home.HomeFragment;

public class ViewPagerAdapterAir extends FragmentStateAdapter {


    public ViewPagerAdapterAir(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
         switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new AirImportFragment();
            case 2:
                return new AirExportFragment();
             case 3:
                 return new RetailGoodsExportFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
