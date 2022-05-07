package com.example.demoapp.view.activity.login_register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.databinding.ActivitySignUpBinding;
import com.example.demoapp.view.activity.chat.DashboardActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        // enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();

                // validate
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    binding.etEmail.setError("Invalid Email");
                    binding.etEmail.setFocusable(true);
                }else if(password.length() <6){
                    binding.etPassword.setError("Password length at least 6 characters");
                    binding.etPassword.setFocusable(true);
                }else{
                    registerUser(email, password);
                }
            }
        });
        binding.tvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });
    }

    private void registerUser(String email, String password) {
        // email and password pattern is valid, show progress dialog and start register user
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, dismiss dialog and start register activity
                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();
                        // get user email and uid from auth
                        String email1 = user.getEmail();
                        String uid = user.getUid();
                        // when user is registered store user info firebase realtime database  too
                        // using HashMap
                        HashMap<Object, String> hashMap = new HashMap<>();
                        // put info in HashMap
                        hashMap.put("email", email1);
                        hashMap.put("uid", uid);
                        hashMap.put("name", "");
                        hashMap.put("onlineStatus", "online");
                        hashMap.put("typingTo", "noOne");
                        hashMap.put("phone", "");
                        hashMap.put("image", "");
                        hashMap.put("cover", "");
                        hashMap.put("tokensNotification", "");
                        hashMap.put("position", "");
                        // Firebase database isntance
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        // path to store user data name "Users"
                        DatabaseReference reference = database.getReference("Users");
                        // put data within hashmap in database
                        reference.child(uid).setValue(hashMap);

                        Toast.makeText(SignUpActivity.this, "Register..\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.;
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error, dismiss progress dialog and get and show the error message
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        // go previous activity
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}