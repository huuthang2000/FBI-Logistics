package com.example.demoapp.view.driver.UIWellcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.demoapp.R;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.viewUtils;
import com.example.demoapp.view.driver.UILoadingData;


public class UITutorial extends AppCompatActivity {
    private final Context context = this;
    private int[] layouts;
    private LinearLayout dotsLayout;
    private ViewPager viewPager;

    private Button btnNext;
    private Button btnPrevious;

    private TextView[] dots;

    private int currentItem;

    private boolean isStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        // Making notification bar transparent
        viewUtils.setTransparentStatusBar((AppCompatActivity) context);

        initView();


        //Set action to button, text view...
        setupAction();

        // adding bottom dots
        addBottomDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
                currentItem = position;
                if (currentItem == layouts.length - 1) {
                    btnNext.setText(getResources().getText(R.string.Start));
                } else {
                    btnNext.setText(getResources().getText(R.string.Next));
                }
                btnPrevious.setVisibility(currentItem == 0 ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initView() {
        currentItem = 0;
        isStart = false;

        viewPager = findViewById(R.id.viewPager);

        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        dotsLayout = findViewById(R.id.layoutDots);

        layouts = new int[]{
                R.layout.slide_tutorial_4,
        };
    }

    private void setupAction() {
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem < layouts.length - 1)
                    currentItem++;

                viewPager.setCurrentItem(currentItem);


                if (currentItem == layouts.length - 1) {
                    btnNext.setText(getResources().getText(R.string.Start));

                    if(isStart){
                        saveFlagTutorial();
                    }
                    isStart = true;
                } else {
                    btnNext.setText(getResources().getText(R.string.Next));
                }
                btnPrevious.setVisibility(currentItem == 0 ? View.INVISIBLE : View.VISIBLE);
                //startActivity(new Intent(context, UIMain.class));
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = false;

                if (currentItem > 0)
                    currentItem--;

                if (currentItem == layouts.length - 1) {
                    btnNext.setText(getResources().getText(R.string.Start));
                } else {
                    btnNext.setText(getResources().getText(R.string.Next));
                }

                btnPrevious.setVisibility(currentItem == 0 ? View.INVISIBLE : View.VISIBLE);

                viewPager.setCurrentItem(currentItem);
            }
        });
    }

    private void saveFlagTutorial(){
        SharedPreferences sharedPreferences = getSharedPreferences(keyUtils.KEY_TUTORIAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(keyUtils.FLAG_TUTORIAL,true);
        editor.apply();

        Intent intent = new Intent(context, UILoadingData.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     *
     * @param context context
     * @return true or false
     */
    public static boolean isFlagTutorial(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyUtils.KEY_TUTORIAL, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(keyUtils.FLAG_TUTORIAL,false);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorDots));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorLine));
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {

        MyViewPagerAdapter() {
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
