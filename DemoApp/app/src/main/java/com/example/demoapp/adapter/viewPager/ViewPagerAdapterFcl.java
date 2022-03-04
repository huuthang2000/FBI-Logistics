package com.example.demoapp.adapter.viewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demoapp.view.fragment.fcl.FCLFragment;
import com.example.demoapp.view.fragment.home.HomeFragment;

public class ViewPagerAdapterFcl extends FragmentStateAdapter {


    public ViewPagerAdapterFcl(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new FCLFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
