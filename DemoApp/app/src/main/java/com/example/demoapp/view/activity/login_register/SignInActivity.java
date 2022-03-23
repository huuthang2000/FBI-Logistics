package com.example.demoapp.view.activity.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.view.driver.DriverActivity;
import com.example.demoapp.databinding.ActivitySignInBinding;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.utilities.PreferenceManager;
import com.example.demoapp.view.activity.air.AirPageActivity;
import com.example.demoapp.view.activity.dom.DomActivity;
import com.example.demoapp.view.activity.fcl.FclPageActivity;
import com.example.demoapp.view.activity.imp.ImportPageActivity;
import com.example.demoapp.view.activity.log.LogPageActivity;
import com.example.demoapp.view.activity.sale.SaleActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;

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
            Intent intent = new Intent(this, DriverActivity.class);
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
}