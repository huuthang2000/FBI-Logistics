package com.example.demoapp.view.driver.UISettings;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Presenters.Account.pre_Account;
import com.example.demoapp.Presenters.Account.view_Account;
import com.example.demoapp.R;
import com.example.demoapp.Utils.directionalController;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.view.driver.UILogin;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;



public class SettingsFragment extends Fragment implements view_Account {

    private View viewParent;

    private objAccount mAccount;

    private pre_Account preAccount;

    private ImageView imvAvatar;
    private ImageView imvGender;
    private ImageView imvStatusGPS;
    private ImageView imvBattery;
    private TextView tvPercentBattery;
    private TextView tvPhoneNumber;

    private TextView tvUsername;

    private LinearLayout lnlSignOut;
    private LinearLayout lnlInviteFriends;
    private LinearLayout lnlEditProfile;
    private LinearLayout lnlJoinOtherFamilies;
    private LinearLayout lnlProfile;
    private LinearLayout lnlUpdatePassword;
    private LinearLayout lnlUpgradePremium;
    private LinearLayout lnlRateApp;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewParent = inflater.inflate(R.layout.fragment_settings, container, false);

        initView();

        setDataToView();

        setActionToView();


        return viewParent;
    }

    private void setActionToView() {
        lnlSignOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MessageDialog.build((AppCompatActivity) getActivity())
                        .setStyle(DialogSettings.STYLE.STYLE_MATERIAL)
                        .setTheme(DialogSettings.THEME.LIGHT)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.You_want_to_sign_out)
                        .setOkButton(R.string.YES, new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                preAccount.signOut();
                                return false;
                            }
                        })
                        .setCancelButton(R.string.NO, new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                baseDialog.doDismiss();
                                return false;
                            }
                        })
                        .show();

            }
        });

        lnlInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUIInviteFriends(getContext());
            }
        });

        lnlJoinOtherFamilies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUIJoinOtherFamilies(getContext());
            }
        });

        lnlEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUIEditProfile(getContext());
            }
        });

        lnlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUIEditProfile(getContext());
            }
        });

        lnlUpdatePassword.setVisibility(mAccount.getType().matches(keyUtils.GOOGLE) ? View.GONE : View.VISIBLE);
        lnlUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdatePassword();
            }
        });




  }

    private void showDialogUpdatePassword(){
        Dialog updatePwdDialog = new Dialog(getContext());
        updatePwdDialog.setContentView(R.layout.dialog_update_password);
        updatePwdDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        updatePwdDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        final TextInputLayout tilCurrentPassword = updatePwdDialog.findViewById(R.id.tilCurrentPassword);
        final TextInputLayout tilNewPassword = updatePwdDialog.findViewById(R.id.tilNewPassword);
        final TextInputLayout tilPasswordConfirm = updatePwdDialog.findViewById(R.id.tilPasswordConfirm);
        final Button btnUpdatePassword = updatePwdDialog.findViewById(R.id.btnUpdatePassword);

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pre_Account preAccount = new pre_Account(getContext(), new view_Account() {
                    @Override
                    public void errorEmailOrPassword(String errorEmail, String errorPassword) {

                    }

                    @Override
                    public void resultOfActionAccount(boolean isSuccess, String message, String type) {
                        if(type.matches(keyUtils.updatePassword)){
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            if(isSuccess)
                                updatePwdDialog.dismiss();
                        }
                    }

                    @Override
                    public void errorInputRegister(String email, String password, String confirmPassword) {

                    }


                    @Override
                    public void errorInputSettingProfile(String errorUsername, String errorPhoneNumber) {

                    }

                    @Override
                    public void errorInputEditProfile(String errorUsername, String errorPhoneNumber, String errorFamilyName) {

                    }

                    @Override
                    public void errorInputEditPassword(String errorCurrentPassword, String errorNewPassword, String errorPasswordConfirm) {
                        tilCurrentPassword.setError(errorCurrentPassword);
                        tilNewPassword.setError(errorNewPassword);
                        tilPasswordConfirm.setError(errorPasswordConfirm);

                        tilCurrentPassword.setErrorEnabled(errorCurrentPassword != null);
                        tilNewPassword.setErrorEnabled(errorNewPassword != null);
                        tilPasswordConfirm.setErrorEnabled(errorPasswordConfirm != null);
                    }


                });

             //   preAccount.updatePassword(tilCurrentPassword.getEditText().getText().toString(),tilNewPassword.getEditText().getText().toString(),tilPasswordConfirm.getEditText().getText().toString());

            }
        });

        updatePwdDialog.show();
    }

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

        imvBattery.setImageDrawable(objAccount.getBatteryIcon(getActivity(),mAccount.getBatteryPercent(),R.color.white));
    }

    private void initView() {
        lnlSignOut = viewParent.findViewById(R.id.lnlSignOut);
        lnlEditProfile = viewParent.findViewById(R.id.lnlEditProfile);
        lnlJoinOtherFamilies = viewParent.findViewById(R.id.lnlJoinOtherFamilies);
        lnlInviteFriends = viewParent.findViewById(R.id.lnlInviteFriends);

        preAccount = new pre_Account(getContext(),this);
        mAccount = objAccount.getAccountFromSQLite(getContext(), FirebaseAuth.getInstance().getUid());
        imvAvatar = viewParent.findViewById(R.id.imvAvatar);
        tvUsername = viewParent.findViewById(R.id.tvUsername);
        tvPhoneNumber = viewParent.findViewById(R.id.tvPhoneNumber);
        tvPercentBattery = viewParent.findViewById(R.id.tvPercentBattery);
        imvGender = viewParent.findViewById(R.id.imvGender);
        imvStatusGPS = viewParent.findViewById(R.id.imvStatusGPS);
        imvBattery = viewParent.findViewById(R.id.imvBattery);
        lnlProfile = viewParent.findViewById(R.id.lnlProfile);
        lnlUpdatePassword = viewParent.findViewById(R.id.lnlUpdatePassword);

    }

    @Override
    public void onResume() {
        super.onResume();
        mAccount = objAccount.getAccountFromSQLite(getContext(), FirebaseAuth.getInstance().getUid());
        setDataToView();
    }

    @Override
    public void errorEmailOrPassword(String errorEmail, String errorPassword) {

    }

    @Override
    public void resultOfActionAccount(boolean isSuccess, String message, String type) {
        if(type.matches(keyUtils.ResultLogout)){
            if(isSuccess)
            {
                Intent intent = new Intent(getContext(), UILogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            else
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void errorInputRegister(String email, String password, String confirmPassword) {

    }


    @Override
    public void errorInputSettingProfile(String errorUsername, String errorPhoneNumber) {

    }

    @Override
    public void errorInputEditProfile(String errorUsername, String errorPhoneNumber, String errorFamilyName) {

    }

    @Override
    public void errorInputEditPassword(String errorCurrentPassword, String errorNewPassword, String errorPasswordConfirm) {

    }


}
