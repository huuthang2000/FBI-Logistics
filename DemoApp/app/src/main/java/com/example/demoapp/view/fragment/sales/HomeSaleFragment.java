package com.example.demoapp.view.fragment.sales;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.demoapp.R;
import com.example.demoapp.view.slide_image.DepthPageTransformer;
import com.example.demoapp.view.slide_image.Photo;
import com.example.demoapp.view.slide_image.PhotoViewPager2Adapter;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;


public class HomeSaleFragment extends Fragment {
    private DrawerLayout mDrawerLayout;
    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private List<Photo> mListPhoto;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager2.getCurrentItem() == mListPhoto.size() - 1) {
                mViewPager2.setCurrentItem(0);
            } else {
                mViewPager2.setCurrentItem(mViewPager2.getCurrentItem() + 1);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homesale, container, false);

        mViewPager2 = (ViewPager2) view.findViewById(R.id.view_pager_2);
        mCircleIndicator3 = (CircleIndicator3) view.findViewById(R.id.circle_indicator_3);

        mListPhoto = getListPhoto();

        PhotoViewPager2Adapter adapter = new PhotoViewPager2Adapter(mListPhoto);

        mViewPager2.setAdapter(adapter);
        mCircleIndicator3.setViewPager(mViewPager2);

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 4000);
            }
        });
        mViewPager2.setPageTransformer(new DepthPageTransformer());

        return view;
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.quangcaosale));
        list.add(new Photo(R.drawable.quangcaosale3));
        list.add(new Photo(R.drawable.quangcaosale2));
        list.add(new Photo(R.drawable.quangcaosale5));
        return  list;
    }
}