package com.example.demoapp.adapter.viewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demoapp.view.fragment.sales.domwaterway.CyWaterWayFragment;
import com.example.demoapp.view.fragment.sales.domwaterway.DoorWaterWayFragment;

public class ViewPagerAdapterWaterWay extends FragmentStateAdapter {
    public ViewPagerAdapterWaterWay(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CyWaterWayFragment();
            case 1:
                return  new DoorWaterWayFragment();
            default:
                return new CyWaterWayFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
