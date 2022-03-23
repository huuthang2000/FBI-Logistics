package com.example.demoapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.demoapp.R;
import com.example.demoapp.model.Account;
import com.example.demoapp.services.AccountService;
import com.example.demoapp.view.activity.air.AirActivity;
import com.example.demoapp.view.activity.dom.DomActivity;
import com.example.demoapp.view.activity.fcl.FclActivity;
import com.example.demoapp.view.activity.log.LogProActivity;
import com.example.demoapp.view.activity.sale.ImportActivity;
import com.example.demoapp.view.activity.sale.SaleActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity  {
    EditText EditText_username,EditText_password;
    MaterialButton btnLogin;
    FragmentTransaction fragmentTransaction;
    private List<Account>  mlistUser;
    private boolean isHasUser = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText_username = findViewById(R.id.username);
        EditText_password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginbtn);
        mlistUser = new ArrayList<>();

        getListUsers();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLogin();
            }
        });

  }

    private void clickLogin() {
        String strUsername = EditText_username.getText().toString().trim();
        String strPassword = EditText_password.getText().toString().trim();
        if (mlistUser == null || mlistUser.isEmpty()) {
            return;
        }

        for (Account account : mlistUser) {
             if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Air")) {
                Intent intent = new Intent(this, AirActivity.class);
                startActivity(intent);
                isHasUser = true;
                Toast.makeText(getApplicationContext(), "Air Department", Toast.LENGTH_LONG).show();
                break;
            } else if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Log")) {
                Intent intent1 = new Intent(this, LogProActivity.class);
                startActivity(intent1);
                 isHasUser = true;
                Toast.makeText(getApplicationContext(), "Log Department", Toast.LENGTH_LONG).show();
                break;
            } else if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Sale")) {
                Intent intent = new Intent(this, SaleActivity.class);
                startActivity(intent);
                 isHasUser = true;
                Toast.makeText(getApplicationContext(), "Department Sales", Toast.LENGTH_LONG).show();
                break;
            } else if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Fcl")) {
                Intent intent = new Intent(this, FclActivity.class);
                startActivity(intent);
                 isHasUser = true;
                Toast.makeText(getApplicationContext(), "FCL Department", Toast.LENGTH_LONG).show();
                break;
            } else if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Import")) {
                Intent intent = new Intent(this, ImportActivity.class);
                startActivity(intent);
                 isHasUser = true;
                Toast.makeText(getApplicationContext(), "Import Department", Toast.LENGTH_LONG).show();
                break;
            } else if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Dom")) {
                Intent intent = new Intent(this, DomActivity.class);
                startActivity(intent);
                 isHasUser = true;
                Toast.makeText(getApplicationContext(), "Dom Department", Toast.LENGTH_LONG).show();
                break;
            }
        }
        if(!isHasUser){
            Toast.makeText(getApplicationContext(), "Username or password invalid", Toast.LENGTH_LONG).show();
        }

    }


    private void getListUsers() {
        AccountService.apiAccountService.getAccount().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                mlistUser = response.body();
                Log.e("List Users: ", mlistUser.size() +"");
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {

            }
        });
    }



}