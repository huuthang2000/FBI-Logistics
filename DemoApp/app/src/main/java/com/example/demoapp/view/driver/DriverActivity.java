package com.example.demoapp.view.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.databinding.ActivityDriverBinding;
import com.example.demoapp.databinding.ActivityImportPageBinding;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.utilities.PreferenceManager;
import com.example.demoapp.view.activity.login_register.SignInActivity;
import com.example.demoapp.view.activity.message.MainMessageActivity;
import com.example.demoapp.view.fragment.Driver.DriverTaskFragment;
import com.example.demoapp.view.fragment.Driver.HomeDriverFragment;
import com.example.demoapp.view.fragment.home.HomeFragment;;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DriverActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private  ActivityDriverBinding binding;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_Task= 1;
    private static final int ACTIVITY_MESSAGE = 2;
    private static final int LOG_OUT =3;
    private static final int ACCESS_LOCATION_PERMISSIONS_REQUEST = 1;
    private PreferenceManager preferenceManager;

    private int mCurrentFragment = FRAGMENT_HOME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            binding = ActivityDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M  ) {// > android 6
            if (ContextCompat.checkSelfPermission(DriverActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getPermissionToAccessLocation();
            }
        }else{
            Toast.makeText ( this,"Nhận diện thiết bị dưới anroid 6", Toast.LENGTH_SHORT).show ();
        }
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationView.setNavigationItemSelectedListener(this);
//        Intent in = new Intent(getApplicationContext(),new HomeDriverFragment().getClass());
//        startActivity(in);

        replaceFragment(new HomeFragment());
        binding.navigationView.getMenu().findItem(R.id.tab_home_driver).setChecked(true);
        preferenceManager = new PreferenceManager(this);
        initHearderView();
    }

    public void getPermissionToAccessLocation() {

        new AlertDialog.Builder(this)
                .setTitle( R.string.permissionLocation)
                .setMessage( R.string.whotoOnGPS)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    ACCESS_LOCATION_PERMISSIONS_REQUEST);
                        }
                    }
                }).setNegativeButton( R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }
    //yêu cầu quyền truy cập Localtion
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == ACCESS_LOCATION_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();
            } else {

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tab_home_driver:
                if (mCurrentFragment != FRAGMENT_HOME) {
                    Intent in = new Intent(getApplicationContext(), HomeDriverFragment.class);
                    startActivity(in);
                    break;
//                    mCurrentFragment = FRAGMENT_HOME;
//                    binding.toolbar.setTitle("Home");
                }
                break;
            case R.id.tab_task_driver:
                if (mCurrentFragment != FRAGMENT_Task ){
                    replaceFragment(new DriverTaskFragment());
                    mCurrentFragment = FRAGMENT_Task;
                    binding.toolbar.setTitle("Task");
                }
                break;

            case R.id.tab_chat_driver:
                if (mCurrentFragment != ACTIVITY_MESSAGE) {
                    Intent intent = new Intent(this, MainMessageActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tab_logout_driver:
                if (mCurrentFragment != LOG_OUT) {
                    signOut();
                }
                finish();
                break;

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
    private void signOut() {
        showToast("Sign out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> update = new HashMap<>();
        update.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(update)
                .addOnSuccessListener(undates -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initHearderView(){
        View header = binding.navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.tv_name_header);
        ImageView imageView = header.findViewById(R.id.image_header);
        tvName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}