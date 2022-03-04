package com.example.demoapp.adapter.viewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demoapp.view.fragment.sales.truck.ContainerExportFragment;
import com.example.demoapp.view.fragment.sales.truck.ContainerImportFragment;
import com.example.demoapp.view.fragment.sales.truck.TruckColdFragment;
import com.example.demoapp.view.fragment.sales.truck.TruckDryFragment;

public class ViewPagerAdapterRoad extends FragmentStateAdapter {
    public ViewPagerAdapterRoad(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new TruckColdFragment();
            case 1:
                return new TruckDryFragment();
            case 2:
                return new ContainerImportFragment();
            case 3:
                return new ContainerExportFragment();
            default:
                return new TruckColdFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
