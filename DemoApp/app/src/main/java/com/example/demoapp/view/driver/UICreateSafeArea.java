package com.example.demoapp.view.driver;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.example.demoapp.R;
import com.example.demoapp.Utils.viewUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;


public class UICreateSafeArea extends AppCompatActivity implements OnMapReadyCallback {
    private final Context context = this;

    private TextInputLayout tilCommonName;
    private LoaderTextView ltvInviteCode;



    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_safe_area);

        initView();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        setActionToView();
    }

    private void setActionToView() {
        ltvInviteCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewUtils.copiedToClipboard(context,ltvInviteCode.getText().toString());
            }
        });
    }

    private void initView() {
        tilCommonName = findViewById(R.id.tilCommonName);
        ltvInviteCode = findViewById(R.id.ltvInviteCode);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Thêm điểm đánh dấu ở Sydney, Úc và di chuyển máy ảnh.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
