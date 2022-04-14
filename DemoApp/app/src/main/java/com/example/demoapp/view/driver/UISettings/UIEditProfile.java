package com.example.demoapp.view.driver.UISettings;

import static com.example.demoapp.Utils.directionalController.goToUISelectCountry;
import static com.example.demoapp.Utils.keyUtils.REQUEST_CODE_PICK_IMAGE;
import static com.example.demoapp.Utils.keyUtils.REQUEST_PERMISSION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objAreaCode;
import com.example.demoapp.Models.objApplication.objFamily;
import com.example.demoapp.Presenters.Account.pre_Account;
import com.example.demoapp.Presenters.Account.view_Account;
import com.example.demoapp.Presenters.Group.pre_Family;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_CurrentFamilyID;
import com.example.demoapp.Utils.AreaCode;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.viewUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;



public class UIEditProfile extends AppCompatActivity implements view_Account {
    private final Context context = this;
    public static objAreaCode areaCode;
    public static final String TAG = UIEditProfile.class.getSimpleName();

    private TextView tvAreaCode;

    private EditText edtUsername;
    private EditText edtPhoneNumber;
    private EditText edtFamilyName;
    private RadioButton rdoMale;
    private RadioButton rdoFemale;
    private RadioButton rdoOther;

    private TextView tvErrorPhoneNumber;
    private TextView tvErrorUsername;
    private TextView tvErrorFamilyName;

    private ImageView imvAvatar;

    private Button btnSave;
    private Button btnRemoveCurrentAvatar;

    private Uri uriAvatar;

    private objAccount mAccount;

    private pre_Account preAccount;

    private String currentFamilyID;

    private TextView tvChooseCountry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        viewUtils.setColorStatusBar((AppCompatActivity) context,R.color.colorStartGradientBlue);

        areaCode = null;

        initPermission();

        initView();

        setupData();

        setActionToView();

    }

    private void setActionToView() {

        //Remove current avatar
        btnRemoveCurrentAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriAvatar = null;
                Glide.with(Objects.requireNonNull(context))
                        .load(mAccount.getLocalAvatar())
                        .placeholder(R.color.colorLine)
                        .error(R.drawable.no_avatar)
                        .into(imvAvatar);

                btnRemoveCurrentAvatar.setVisibility(View.INVISIBLE);
            }
        });

        //Choose image from storage
        imvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an Intent with action as ACTION_PICK
                Intent intent=new Intent(Intent.ACTION_PICK);
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.setType("image/*");
                //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                // Launching the Intent
                startActivityForResult(intent,REQUEST_CODE_PICK_IMAGE);
            }
        });

        //Back
        findViewById(R.id.imvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_button_save_profile));

        //Submit data to the server including account information and avatar
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edtUsername.getText().toString();
                String phoneNumber = tvAreaCode.getText().toString() + edtPhoneNumber.getText().toString();
                String gender;

                if(rdoFemale.isChecked())
                    gender = keyUtils.FEMALE;
                else if(rdoMale.isChecked())
                    gender = keyUtils.MALE;
                else
                    gender = keyUtils.OTHER;


                if(preAccount.checkInputEditProfile(name,edtPhoneNumber.getText().toString(),edtFamilyName.getText().toString())){


                    if(uriAvatar != null){
                        preAccount.updateAvatar(uriAvatar, new pre_Account.onResultUpdateAvatar() {
                            @Override
                            public void onResult(boolean isSuccess, String path, String message) {
                                if(isSuccess){

                                    preAccount.updateProfileToDatabase(path, name, phoneNumber, gender, new pre_Account.onResultUpdateProfile() {
                                        @Override
                                        public void onResult(boolean isSuccess, String message) {
                                            if(isSuccess){
                                                //Update family name
                                                if(!edtFamilyName.getText().toString().matches(objFamily.getMyFamily(context).getCommonName())){
                                                    //Edit family name
                                                    pre_Family.editFamilyName(context, currentFamilyID, edtFamilyName.getText().toString(), new pre_Family.onResultEditFamilyName() {
                                                        @Override
                                                        public void onResult(boolean isSuccess, String message) {

                                                            if(isSuccess){
                                                                Toast.makeText(UIEditProfile.this, R.string.Successfully_updated, Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                            //Update family fail
                                                            else {
                                                                Toast.makeText(UIEditProfile.this, R.string.Failed_to_update_family_name, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                                //No need to update family name, display successfully
                                                else{

                                                    Toast.makeText(UIEditProfile.this, R.string.Successfully_updated, Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                            //Fail
                                            else{
                                                Log.e("CheckApp", message);
                                                Toast.makeText(UIEditProfile.this, R.string.Update_failed, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                //Fail
                                else{

                                    Toast.makeText(context, R.string.Uploaded_avatar_failed, Toast.LENGTH_SHORT).show();
                                    Log.e("CheckApp", message);
                                }
                            }
                        });
                    }
                    //No update avatar
                    else{
                        preAccount.updateProfileToDatabase("", name, phoneNumber, gender, new pre_Account.onResultUpdateProfile() {
                            @Override
                            public void onResult(boolean isSuccess, String message) {


                                if(isSuccess){
                                    //Update family name
                                    if(!edtFamilyName.getText().toString().matches(objFamily.getMyFamily(context).getCommonName())){
                                        //Edit family name
                                        pre_Family.editFamilyName(context, currentFamilyID, edtFamilyName.getText().toString(), new pre_Family.onResultEditFamilyName() {
                                            @Override
                                            public void onResult(boolean isSuccess, String message) {

                                                if(isSuccess){
                                                    Toast.makeText(UIEditProfile.this, R.string.Successfully_updated, Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                                //Update family fail
                                                else {
                                                    Toast.makeText(UIEditProfile.this, R.string.Failed_to_update_family_name, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    //No need to update family name, display successfully
                                    else{

                                        Toast.makeText(UIEditProfile.this, R.string.Successfully_updated, Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }else{
                                    Toast.makeText(UIEditProfile.this, R.string.Update_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }

            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void setupData() {
        currentFamilyID = tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID();
        mAccount = objAccount.getAccountFromSQLite(context,objAccount.getCurrentUser().getUid());
        int position = 0;
        for(int i = 0; i < AreaCode.countryAreaCodes.length; i++){
            String areaCode = "+" + AreaCode.countryAreaCodes[i];
            if(mAccount.getPhone().indexOf(areaCode) == 0){
                position = i;
                break;
            }
        }

        tvChooseCountry.setText(AreaCode.countryNames[position]);
        tvAreaCode.setText("+" + AreaCode.countryAreaCodes[position]);

        tvChooseCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUISelectCountry(context,TAG);
            }
        });

        edtPhoneNumber.setText(mAccount.getPhone().replace("+" + AreaCode.countryAreaCodes[position], ""));

        Glide.with(Objects.requireNonNull(context))
                .load(mAccount.getLocalAvatar().matches("") ? mAccount.getAvatar() : mAccount.getLocalAvatar())
                .placeholder(R.color.colorLine)
                .error(R.drawable.no_avatar)
                .dontAnimate().apply(RequestOptions.circleCropTransform())
                .into(imvAvatar);


        edtUsername.setText(mAccount.getName());
        edtFamilyName.setText(objFamily.getMyFamily(context).getCommonName());

        rdoFemale.setChecked(mAccount.getGender().toLowerCase().matches(keyUtils.FEMALE));
        rdoMale.setChecked(mAccount.getGender().toLowerCase().matches(keyUtils.MALE));
        rdoOther.setChecked(mAccount.getGender().toLowerCase().matches(keyUtils.OTHER));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == RESULT_OK)
            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                //data.getData returns the content URI for the selected Image
                assert data != null;
                uriAvatar = data.getData();
                Glide.with(context).load(uriAvatar)
                        .placeholder(R.color.colorLine)
                        .error(R.drawable.no_avatar)
                        .into(imvAvatar);

                btnRemoveCurrentAvatar.setVisibility(View.VISIBLE);
            }
    }


    private void initView() {
        preAccount = new pre_Account(context,this);


        tvAreaCode = findViewById(R.id.tvAreaCode);
        tvChooseCountry = findViewById(R.id.tvChooseCountry);

        edtUsername = findViewById(R.id.edtUsername);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtFamilyName = findViewById(R.id.edtFamilyName);
        tvErrorPhoneNumber = findViewById(R.id.tvErrorPhoneNumber);
        tvErrorUsername = findViewById(R.id.tvErrorUsername);
        tvErrorFamilyName = findViewById(R.id.tvErrorFamilyName);

        rdoMale = findViewById(R.id.rdoMale);
        rdoFemale = findViewById(R.id.rdoFemale);
        rdoOther = findViewById(R.id.rdoOther);

        btnSave = findViewById(R.id.btnSave);
        btnRemoveCurrentAvatar = findViewById(R.id.btnRemoveCurrentAvatar);

        imvAvatar = findViewById(R.id.imvAvatar);

        mAccount = objAccount.getAccountFromSQLite(context, FirebaseAuth.getInstance().getUid());


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length != 3 ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Init permission read storage
     */
    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_PERMISSION);
            }
        }
    }

    @Override
    public void errorEmailOrPassword(String ErrorEmail, String ErrorPassword) {

    }

    @Override
    public void resultOfActionAccount(boolean isSuccess, String message, String type) {

    }

    @Override
    public void errorInputRegister(String email, String password, String confirmPassword) {

    }


    @Override
    public void errorInputSettingProfile(String errorUsername, String errorPhoneNumber) {

    }

    @Override
    public void errorInputEditProfile(String errorUsername, String errorPhoneNumber, String errorFamilyName) {
        tvErrorPhoneNumber.setVisibility(errorPhoneNumber == null ? View.GONE : View.VISIBLE);
        tvErrorUsername.setVisibility(errorUsername == null ? View.GONE : View.VISIBLE);
        tvErrorFamilyName.setVisibility(errorFamilyName == null ? View.GONE : View.VISIBLE);
        tvErrorUsername.setText(errorUsername);
        tvErrorPhoneNumber.setText(errorPhoneNumber);
        tvErrorFamilyName.setText(errorFamilyName);
    }

    @Override
    public void errorInputEditPassword(String errorCurrentPassword, String errorNewPassword, String errorPasswordConfirm) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(areaCode != null){
            tvAreaCode.setText(areaCode.getId());
            tvChooseCountry.setText(areaCode.getCountriesName());
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
