package com.example.demoapp.adapter.viewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demoapp.view.fragment.fcl.FCLFragment;
import com.example.demoapp.view.fragment.home.HomeFragment;
import com.example.demoapp.view.fragment.imp.ImportFragment;
import com.example.demoapp.view.fragment.imp.ImportLclFragment;

public class ViewPagerAdapterImport extends FragmentStateAdapter {


    public ViewPagerAdapterImport(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new ImportFragment();
        }else if(position == 2){
            return new ImportLclFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
