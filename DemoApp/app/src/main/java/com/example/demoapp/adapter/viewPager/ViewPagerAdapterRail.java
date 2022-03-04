package com.example.demoapp.adapter.viewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demoapp.view.fragment.sales.CyCyFragment;
import com.example.demoapp.view.fragment.sales.DoorToDoorFragment;

public class ViewPagerAdapterRail extends FragmentStateAdapter {
    public ViewPagerAdapterRail(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CyCyFragment();
            case 1:
                return new DoorToDoorFragment();
            default:
                return new CyCyFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
