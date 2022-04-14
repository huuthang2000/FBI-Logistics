package com.example.demoapp.view.activity.login_register;

import static com.example.demoapp.Utils.keyUtils.REQUEST_LOGIN;
import static com.example.demoapp.Utils.keyUtils.REQUEST_PERMISSION;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.example.demoapp.Presenters.Account.pre_Account;
import com.example.demoapp.Presenters.Account.view_Account;
import com.example.demoapp.Presenters.Group.pre_Family;
import com.example.demoapp.R;
import com.example.demoapp.Utils.directionalController;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.viewUtils;
import com.example.demoapp.databinding.ActivitySignInBinding;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.utilities.PreferenceManager;
import com.example.demoapp.view.activity.air.AirPageActivity;
import com.example.demoapp.view.activity.dom.DomActivity;
import com.example.demoapp.view.activity.fcl.FclPageActivity;
import com.example.demoapp.view.activity.imp.ImportPageActivity;
import com.example.demoapp.view.activity.log.LogPageActivity;
import com.example.demoapp.view.activity.sale.SaleActivity;
import com.example.demoapp.view.driver.DriverActivity;
import com.example.demoapp.view.driver.UISetupProfile;
import com.example.demoapp.view.driver.UISplashScreen;
import com.example.demoapp.view.driver.UIWellcome.UITutorial;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity implements view_Account {
    private final Context context = this;
    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    private static final int LOCATION_REQUEST = 1000;

    private pre_Account mLogin;
    private static final String TAG = "InAppBilling";
    // In-app products. Currently only selling "ad removal

    private BillingClient mBillingClient;
    Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(getApplicationContext());
        login();
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();

    }

    private void login() {
        if (preferenceManager.getBoolean(Constants.KEY_SALE)) {
            Intent intent = new Intent(this, SaleActivity.class);
            startActivity(intent);
            finish();
        } else if (preferenceManager.getBoolean(Constants.KEY_AIR)) {
            Intent intent = new Intent(this, AirPageActivity.class);
            startActivity(intent);
            finish();
        } else if (preferenceManager.getBoolean(Constants.KEY_DOM)) {
            Intent intent = new Intent(this, DomActivity.class);
            startActivity(intent);
            finish();
        } else if (preferenceManager.getBoolean(Constants.KEY_LOG)) {
            Intent intent = new Intent(this, LogPageActivity.class);
            startActivity(intent);
            finish();
        }else if(preferenceManager.getBoolean(Constants.KEY_FCL)){
            Intent intent = new Intent(this, FclPageActivity.class);
            startActivity(intent);
            finish();
        }else if(preferenceManager.getBoolean(Constants.KEY_IMPORT)){
            Intent intent = new Intent(this, ImportPageActivity.class);
            startActivity(intent);
            finish();
        }
        else if(preferenceManager.getBoolean(Constants.KEY_DRIVER)){
            Intent intent = new Intent(this, UISplashScreen.class);
            startActivity(intent);
            finish();
        }
    }

    private void setListeners() {
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        binding.buttonSignIn.setOnClickListener(v -> {
            if (isValidSignInDetails()) {
                signIn();
            }
        });
    }

    private void signIn() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        if (documentSnapshot.getString(Constants.KEY_POSITION).toString().trim().equals("Sale")) {
                            preferenceManager.putBoolean(Constants.KEY_SALE, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                            preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                            preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                            Intent intent = new Intent(getApplicationContext(), SaleActivity.class);
                            startActivity(intent);
                        } else if (documentSnapshot.getString(Constants.KEY_POSITION).equals("Air")) {
                            preferenceManager.putBoolean(Constants.KEY_AIR, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                            preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                            preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                            Intent intent = new Intent(getApplicationContext(), AirPageActivity.class);
                            startActivity(intent);
                        } else if (documentSnapshot.getString(Constants.KEY_POSITION).toString().trim().equals("Dom")) {
                            preferenceManager.putBoolean(Constants.KEY_DOM, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                            preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                            preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                            Intent intent = new Intent(getApplicationContext(), DomActivity.class);
                            startActivity(intent);
                        } else if (documentSnapshot.getString(Constants.KEY_POSITION).toString().trim().equals("Log")) {
                            preferenceManager.putBoolean(Constants.KEY_LOG, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                            preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                            preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                            Intent intent = new Intent(getApplicationContext(), LogPageActivity.class);
                            startActivity(intent);
                        }else if (documentSnapshot.getString(Constants.KEY_POSITION).toString().trim().equals("Fcl")) {
                            preferenceManager.putBoolean(Constants.KEY_FCL, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                            preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                            preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                            Intent intent = new Intent(getApplicationContext(), FclPageActivity.class);
                            startActivity(intent);
                        }else if (documentSnapshot.getString(Constants.KEY_POSITION).toString().trim().equals("Import")) {
                            preferenceManager.putBoolean(Constants.KEY_IMPORT, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                            preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                            preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                            Intent intent = new Intent(getApplicationContext(), ImportPageActivity.class);
                            startActivity(intent);
                        }
                        else if (documentSnapshot.getString(Constants.KEY_POSITION).toString().trim().equals("Driver")) {
                            preferenceManager.putBoolean(Constants.KEY_DRIVER, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                            preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                            preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                            Intent intent = new Intent(getApplicationContext(), DriverActivity.class);
                            startActivity(intent);
                            viewUtils.setTransparentStatusBar((AppCompatActivity) context);
                            initPermission();
                            setAction();
                            checkAutoStart();
                        }


                    } else {
                        loading(false);
                        showToast("Unable to sign in");
                    }
                });
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    private boolean isValidSignInDetails() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
        }
    }
    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_EXTERNAL_STORAGE ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                
                Intent myIntent = new Intent();
                myIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                myIntent.setData(Uri.parse("package:" + SignInActivity.this.getPackageName()));
                startActivity(myIntent);
            }
        }

        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_EXTERNAL_STORAGE ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

                Log.d("packageName",SignInActivity.this.getPackageName());
                Intent myIntent = new Intent();
                myIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                myIntent.setData(Uri.parse("package:" + SignInActivity.this.getPackageName()));
                startActivity(myIntent);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (requestCode == REQUEST_PERMISSION) {
                if (grantResults.length != 7 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[2] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[3] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[4] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[5] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[6] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

        else
        {
            if (requestCode == REQUEST_PERMISSION) {
                if (grantResults.length != 5 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[2] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[3] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[4] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    /**
     * checkAutoStart đây là phương pháp yêu cầu người dùng bật chức năng tự động khởi động
     *      * để ứng dụng có thể chạy nền ổn định
     */
    public void checkAutoStart()
    {
        String manufacturer = Build.MANUFACTURER;
        Log.d("manufacturer",manufacturer);
        try {
            Intent intent = new Intent();
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autoStart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAction() {


        //Action login
       binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // tvError.setVisibility(View.GONE);
                binding.buttonSignIn.setEnabled(false);



                mLogin.actionSignIn(binding.inputEmail.getText().toString(), binding.inputPassword.getText().toString());
            }
        });



//        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputDialog.show((AppCompatActivity) context,
//                        getResources().getString(R.string.Reset_Password),
//                        null, getResources().getString(R.string.ok),
//                        getResources().getString(R.string.Cancel))
//                        .setHintText(getResources().getString(R.string.Email))
//                        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
//                            @Override
//                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
//                                if (!inputStr.matches("")) {
//                                    WaitingDialog.showDialog(context);
//                                    return false;
//                                }
//                                //Invalid email
//                                else {
//                                    Toast.makeText(context, R.string.Email_is_not_empty, Toast.LENGTH_SHORT).show();
//                                    return true;
//                                }
//                            }
//                        });
//            }
//        });


    }

    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                REQUEST_LOGIN);
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                mLogin.signInWithGoogle(FirebaseAuth.getInstance().getCurrentUser());
            } else {
                Log.d("CheckApp", "RESULT CODE NOT OK");
                Toast.makeText(context, getResources().getString(R.string.Login_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void initView() {
//        mLogin = new pre_Account(context,this);
//
//        binding.inputEmail = findViewById(R.id.tilEmail);
//        binding.inputp = findViewById(R.id.tilPassword);
//
//        buttonSignIn = findViewById(R.id.btnLogin);
//
//      //  tvError = findViewById(R.id.tvError);
//
//    }


    @Override
    public void errorEmailOrPassword(String errorEmail, String errorPassword) {
        //after results
        binding.buttonSignIn.setEnabled(true);




//        tilEmail.setErrorEnabled(errorEmail != null);
//        tilPassword.setErrorEnabled(errorPassword != null);

        binding.inputEmail.setError(errorEmail);
        binding.inputPassword.setError(errorPassword);
    }

    @Override
    public void resultOfActionAccount(boolean isSuccess, String message, String type) {

        if(type.matches(keyUtils.ResultLogin)){



            //after result
            binding.buttonSignIn.setEnabled(true);

            if(isSuccess) {
                pre_Family.checkFamilyID(new pre_Family.onResultCheckFamilyID() {
                    @Override
                    public void onResult(boolean isSuccess, long size, String message) {
                        if(isSuccess && size > 0){
                            if(!UITutorial.isFlagTutorial(context))
                                startActivity(new Intent(context, UITutorial.class));
                            else{
                                directionalController.goToUILoadingData(context);
                            }

                            finish();
                        }
                        else{
                            Intent intent = new Intent(context, UISetupProfile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }else{
//                tvError.setVisibility(View.VISIBLE);
//                tvError.setText(message);
            }
        }

        if(type.matches(keyUtils.forgetPassword)){

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}