package com.example.demoapp.adapter.chat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demoapp.view.fragment.chat.ChatListFragment;
import com.example.demoapp.view.fragment.chat.HomeChatFragment;
import com.example.demoapp.view.fragment.chat.ProfileFragment;
import com.example.demoapp.view.fragment.chat.UsersFragment;


public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeChatFragment();
            case 1:
                return new ProfileFragment();
            case 2:
                return new UsersFragment();
            case 3:
                return new ChatListFragment();
            default:
                return new HomeChatFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
