package com.example.demoapp.view.driver.UISafety;

import static com.example.demoapp.Utils.keyUtils.REQUEST_PERMISSION_CALL_PHONE;
import static com.example.demoapp.Utils.patternFormatDateTime.MMM_dd_yyyy_hh_mm_a;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objDrivingDetail;
import com.example.demoapp.Models.objApplication.objSpeed;
import com.example.demoapp.Presenters.Safety.pre_Safety;
import com.example.demoapp.Presenters.Safety.view_Safety;
import com.example.demoapp.R;
import com.example.demoapp.Utils.directionalController;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.timeUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class SafetyFragment extends Fragment implements view_Safety {
    public static final String TAG = SafetyFragment.class.getSimpleName();

    private pre_Safety preSafety;

    private View viewParent;

    private ImageView imvCar;


    private RecyclerView rclvEmergencyContact;

    private TextView tvAverageSpeed;
    private TextView tvTopSpeed;
    private TextView tvTimeUpdateAverageSpeed;
    private TextView tvTimeUpdateTopSpeed;
    private TextView tvWeek;
    private TextView tvYear;

    private LinearLayout lnlSeeMoreDriving;

    private ImageView imvAvatar;
    private ImageView imvGender;
    private ImageView imvStatusGPS;
    private ImageView imvBattery;
    private TextView tvPercentBattery;
    private TextView tvPhoneNumber;
    private RelativeLayout lnlProfile;

    private TextView tvUsername;

    private objAccount mAccount;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewParent = inflater.inflate(R.layout.fragment_safety, container, false);

//        UIMain.setListener(new UIMain.onSelectedChangeFamily() {
//            @Override
//            public void onChange(String familyID) {
//                preSafety.getEmergencyContactsByFamilyID();
//            }
//        });

        initView();

        setActionToView();

        setDataToView();



        return viewParent;
    }

    @SuppressLint("SetTextI18n")
    private void setDataToView() {

        Glide.with(requireContext())
                .load(mAccount.getLocalAvatar().matches("") ? mAccount.getAvatar() : mAccount.getLocalAvatar())
                .placeholder(R.color.colorLine)
                .error(R.drawable.no_avatar)
                .dontAnimate().apply(RequestOptions.circleCropTransform())
                .into(imvAvatar);

        tvUsername.setText(mAccount.getName());
        tvPercentBattery.setText(mAccount.getBatteryPercent());
        tvPhoneNumber.setText(mAccount.getPhone());
        Drawable icon_gender;
        if(mAccount.getGender().toLowerCase().matches(keyUtils.MALE))
            icon_gender = getResources().getDrawable(R.drawable.ic_male);
        else if(mAccount.getGender().toLowerCase().matches(keyUtils.FEMALE))
            icon_gender = getResources().getDrawable(R.drawable.ic_female);
        else
            icon_gender = getResources().getDrawable(R.drawable.ic_sexual);

        imvGender.setImageDrawable(icon_gender);

        imvStatusGPS.setImageDrawable(getResources().getDrawable(mAccount.getGps().getStatus() ? R.drawable.ic_location_on_red_18dp : R.drawable.ic_location_off_red_18dp));

        imvBattery.setImageDrawable(objAccount.getBatteryIcon(getActivity(),mAccount.getBatteryPercent(),R.color.colorThemeBlue));

        String week = String.valueOf(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        if(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) < 10){
            week = "0" + week;
        }

        tvWeek.setText(week);
        tvYear.setText(year);

    }

    private void setActionToView() {

        lnlSeeMoreDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUIAllDrivingInsights(getContext());
            }
        });

        lnlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUIEditProfile(getContext());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        preSafety = new pre_Safety(getContext(),this);

        imvCar = viewParent.findViewById(R.id.imvCar);
        //setAnimationToView
        imvCar.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_car));

        mAccount = objAccount.getAccountFromSQLite(getContext(),objAccount.getCurrentUser().getUid());

        rclvEmergencyContact = viewParent.findViewById(R.id.rclvEmergencyContact);
        tvAverageSpeed = viewParent.findViewById(R.id.tvAverageSpeed);
        tvTopSpeed = viewParent.findViewById(R.id.tvTopSpeed);
        tvTimeUpdateAverageSpeed = viewParent.findViewById(R.id.tvTimeUpdateAverageSpeed);
        tvTimeUpdateTopSpeed = viewParent.findViewById(R.id.tvTimeUpdateTopSpeed);
        lnlSeeMoreDriving = viewParent.findViewById(R.id.lnlSeeMoreDriving);

        imvAvatar = viewParent.findViewById(R.id.imvAvatar);
        tvUsername = viewParent.findViewById(R.id.tvUsername);
        tvPhoneNumber = viewParent.findViewById(R.id.tvPhoneNumber);
        tvPercentBattery = viewParent.findViewById(R.id.tvPercentBattery);
        imvGender = viewParent.findViewById(R.id.imvGender);
        imvStatusGPS = viewParent.findViewById(R.id.imvStatusGPS);
        imvBattery = viewParent.findViewById(R.id.imvBattery);
        lnlProfile = viewParent.findViewById(R.id.lnlProfile);

        tvWeek = viewParent.findViewById(R.id.tvWeek);
        tvYear = viewParent.findViewById(R.id.tvYear);

        preSafety.getEmergencyContactsByFamilyID();
        preSafety.getCurrentSpeedOfUser(objAccount.getCurrentUser().getUid());

        tvTimeUpdateTopSpeed.setText("("+timeUtils.convertMillisecondToStringFormat(Calendar.getInstance().getTimeInMillis(),MMM_dd_yyyy_hh_mm_a)+")");
        tvTimeUpdateAverageSpeed.setText("("+timeUtils.convertMillisecondToStringFormat(Calendar.getInstance().getTimeInMillis(),MMM_dd_yyyy_hh_mm_a) +")");

    }

    @Override
    public void onResume() {
        super.onResume();
        mAccount = objAccount.getAccountFromSQLite(getContext(), FirebaseAuth.getInstance().getUid());
        setDataToView();
    }

    @Override
    public void resultOfAction(boolean isSuccess, String message, String type) {
        Log.d(TAG, message);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void drivingDetails(objDrivingDetail drivingDetail) {

        //Calculate the average speed
        List<Double> listAverageSpeed = drivingDetail.getAverageSpeed().getListAverageSpeed();
        final int sizeAverageSpeed = listAverageSpeed.size();
        double averageSpeed = 0;
        for(double avg : listAverageSpeed){
            averageSpeed += avg;
        }

        averageSpeed = objSpeed.round(averageSpeed / sizeAverageSpeed,2);

        tvAverageSpeed.setText(averageSpeed + " km/hr");
        tvTopSpeed.setText(drivingDetail.getTopSpeed());
        tvTimeUpdateTopSpeed.setText("("+timeUtils.convertMillisecondToStringFormat(drivingDetail.getTimeUpdateTopSpeed(),MMM_dd_yyyy_hh_mm_a)+")");
        tvTimeUpdateAverageSpeed.setText("("+timeUtils.convertMillisecondToStringFormat(drivingDetail.getAverageSpeed().getTimeUpdateAverageSpeed(),MMM_dd_yyyy_hh_mm_a) +")");

    }

    @Override
    public void allDrivingDetailOfUser(ArrayList<objDrivingDetail> drivingDetailList) {

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CALL_PHONE) {
            if (grantResults.length != 1 ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
