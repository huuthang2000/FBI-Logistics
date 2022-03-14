package com.example.demoapp.view.activity;

import static com.example.demoapp.view.activity.sale.SaleActivity.nameu;
import static com.example.demoapp.view.activity.sale.SaleActivity.posi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    Button btnLogin;
    TextView tvTrangchu;
    FragmentTransaction fragmentTransaction;
    private List<Account>  mlistUser;
    private boolean isHasUser = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
     String username;
     String position;
    TextView tk,mk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Anhxa ();
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
//        username = sharedPreferences.getString(nameu,"");
        position = sharedPreferences.getString("posi","");
        Log.d("VV",position);
        checklogin ();
       loadweb ();
        getListUsers();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                batloinhap ();
                getListUsers();
                clickLogin();
            }
        });
//
  }
    public void batloinhap(){
        String usernamee= EditText_username.getText ().toString ();
        String passwordd = EditText_password.getText ().toString ();
        if (usernamee.isEmpty () || usernamee.length () == 0 || usernamee.equals ( "" ) || usernamee == null) {
            tk.setVisibility(View.VISIBLE);
            tk.setText ( "Bạn chưa nhập tài khoản" );
        }else {
            tk.setVisibility(View.INVISIBLE);
        }
        if (passwordd.isEmpty () || passwordd.length () == 0 || passwordd.equals ( "" ) || passwordd == null) {
            mk.setVisibility(View.VISIBLE);
            mk.setText ( "Bạn chưa nhập mật khẩu" );
        }
        else {
            mk.setVisibility(View.INVISIBLE);
        }

    }
    void loadweb(){
        tvTrangchu.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://fbilogistics.vn/vi/trang-chu/")));
            }
        } );
    }
    void Anhxa(){
        EditText_username = findViewById(R.id.username);
        EditText_password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginbtn);
        mlistUser = new ArrayList<>();
        tk = findViewById ( R.id.tk );
        mk = findViewById ( R.id.mk );
        tvTrangchu = findViewById ( R.id.tvTrangchu );
    }
    private void checklogin(){
        if(position.trim ().equals("Sale")){
            Intent intent = new Intent(LoginActivity.this, SaleActivity.class);
            //Intent.putExtra("Keyusername", EditText_username.getText().toString().trim());
            intent.putExtra ( "Keyposition","Sale" );
            startActivity(intent);
            isHasUser = true;
        }else if(position.trim().equals (  "Air")){
            Intent intent = new Intent(this, AirActivity.class);
            intent.putExtra ( "Keyposition","Air" );
            startActivity(intent);
            isHasUser = true;
        }
        else if(position.trim().equals (  "Log")){
            Intent intent = new Intent(this, LogProActivity.class);
            intent.putExtra ( "Keyposition","Log" );
            startActivity(intent);

            isHasUser = true;
        }
        else if(position.trim().equals (  "Fcl")){
            Intent intent = new Intent(this, FclActivity.class);
            intent.putExtra ( "Keyposition","Fcl" );
            startActivity(intent);
            isHasUser = true;
        }
        else if(position.trim().equals (  "Import")){
            Intent intent = new Intent(this, ImportActivity.class);
            intent.putExtra ( "Keyposition","Import" );
            startActivity(intent);
            isHasUser = true;
        }
        else if(position.trim().equals (  "Dom")){
            Intent intent = new Intent(this, DomActivity.class);
            intent.putExtra ( "Keyposition","Dom" );
            startActivity(intent);
            isHasUser = true;
        }

        else{
            Log.d("VVV","K load dc");

        }
    }
    private void clickLogin() {
        String strUsername = EditText_username.getText().toString().trim();
        String strPassword = EditText_password.getText().toString().trim();
        if (mlistUser == null || mlistUser.isEmpty()) {
            return;
        }
//        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (Account account : mlistUser) {
             if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Air")) {

                Intent intent = new Intent(this, AirActivity.class);
                 intent.putExtra("Keyusername", EditText_username.getText().toString().trim());
                 intent.putExtra ( "Keyposition","Air" );
                startActivity(intent);
                isHasUser = true;

                break;
            } else if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Log")) {
                Intent intent = new Intent(this, LogProActivity.class);
                 intent.putExtra("Keyusername", EditText_username.getText().toString().trim());
                 intent.putExtra ( "Keyposition","Log" );
                startActivity(intent);
                 isHasUser = true;

                break;
            } else if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Sale")) {
                Intent intent = new Intent(this, SaleActivity.class);
                 intent.putExtra("Keyusername", EditText_username.getText().toString().trim());
                 intent.putExtra ( "Keyposition","Sale" );

                startActivity(intent);
                 isHasUser = true;
                break;
            } else if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Fcl")) {
                Intent intent = new Intent(this, FclActivity.class);
                 intent.putExtra("Keyusername", EditText_username.getText().toString().trim());
                 intent.putExtra ( "Keyposition","Fcl" );
                startActivity(intent);
                 isHasUser = true;

                break;
            } else if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Import")) {
                Intent intent = new Intent(this, ImportActivity.class);
                 intent.putExtra("Keyusername", EditText_username.getText().toString().trim());
                 intent.putExtra ( "Keyposition","Import" );
                startActivity(intent);
                 isHasUser = true;

                break;
            } else if (strUsername.equals(account.getUsername()) && strPassword.equals(account.getPassword())
                    && account.getPosition().equalsIgnoreCase("Dom")) {
                Intent intent = new Intent(this, DomActivity.class);
                 intent.putExtra("Keyusername", EditText_username.getText().toString().trim());
                 intent.putExtra ( "Keyposition","Dom" );
                startActivity(intent);
                 isHasUser = true;

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
            Log.d("ER",t.getMessage ());
            }
        });
    }



}