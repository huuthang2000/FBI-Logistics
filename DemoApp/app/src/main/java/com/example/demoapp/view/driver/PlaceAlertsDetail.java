package com.example.demoapp.view.driver;

import static com.example.demoapp.Utils.keyUtils.areaList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.demoapp.Models.objApplication.objArea;
import com.example.demoapp.Models.objectFirebase.family.fb_area;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_Area;
import com.example.demoapp.SQLite.tb_CurrentFamilyID;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.viewUtils;
import com.example.demoapp.view.driver.autoStarService.GPSService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xw.repo.BubbleSeekBar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class PlaceAlertsDetail extends AppCompatActivity implements OnMapReadyCallback {
    private final Context context = this;


    private MarkerOptions markerOptions;
    private GoogleMap mMap;
    private TextInputLayout textInputLayout, textInputLayoutName;
    private Button btn_AddPlaceAlert, btn_Delete_PlaceAlert, btn_Edit_PlaceAlert;
    private LinearLayout ln_Add_Delete;
    private BubbleSeekBar sb_Zone;
    private LatLng latLngPlaceAlert;
    private LatLng latLngPlaceAlertTamp;
    private DatabaseReference mFirebaseDatabase;
    private ImageView cimg_SelectAddMap;
    private LinearLayout ln_Map_Type_Add_Select;
    private ImageView img_Map_Add_Street_Map, img_Map_Add_Terrain, img_Map_Add_Satellite;
    private TextView txt_Close_Add_SelectMap, txt_Normal_Add_Map, txt_Terrain_Add_Map, txt_Satellite_Map_Add;
    private ImageView img_place;
    private FirebaseDatabase mFirebaseInstance;
    private boolean checkEditPlace = false;
    private Toolbar toolbar;
    private int count;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int radius_Place;
    boolean chekcAddPlace = false;
    private boolean checkEditOrAdd = false;
    objArea fb_area_Place;

    // Initialize View before initializing google map
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_alerts);
        viewUtils.setColorStatusBar((AppCompatActivity) context, R.color.colorThemeBlue);

        radius_Place = 20;

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference().child(tb_CurrentFamilyID.getInstance(getApplicationContext()).getCurrentFamilyID()).child(areaList);
        Log.d("ZXZ", tb_CurrentFamilyID.getInstance(getApplicationContext()).getCurrentFamilyID() + " null");
        sharedPreferences = getApplicationContext().getSharedPreferences(keyUtils.KEY_FAMILY_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        setID();
        setEvent();

        // Tăng bố cục cho phân đoạn này
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPlaceAlerts);
        mapFragment.getMapAsync(this);


    }


    @SuppressLint("SetTextI18n")
    private void setID() {
        toolbar = findViewById(R.id.toolbar_Place_Alerts);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textInputLayout = findViewById(R.id.ti_DetailPlace);
        btn_AddPlaceAlert = findViewById(R.id.btn_AddPlaceAlert);
        btn_Delete_PlaceAlert = findViewById(R.id.btn_Delete_PlaceAlert);
        btn_Edit_PlaceAlert = findViewById(R.id.btn_Edit_PlaceAlert);
        ln_Add_Delete = findViewById(R.id.ln_Add_Delete);
        textInputLayoutName = findViewById(R.id.ti_NamePlace);

        // set ID
        cimg_SelectAddMap = findViewById(R.id.cimg_SelectAddMap);
        ln_Map_Type_Add_Select = findViewById(R.id.ln_Map_Type_Add_Select);
        ln_Map_Type_Add_Select.setVisibility(View.GONE);
        img_Map_Add_Street_Map = findViewById(R.id.img_Map_Add_Street_Map);
        img_Map_Add_Terrain = findViewById(R.id.img_Map_Add_Terrain);
        img_Map_Add_Satellite = findViewById(R.id.img_Map_Add_Satellite);
        txt_Close_Add_SelectMap = findViewById(R.id.txt_Close_Add_SelectMap);
        txt_Normal_Add_Map = findViewById(R.id.txt_Normal_Add_Map);
        txt_Terrain_Add_Map = findViewById(R.id.txt_Terrain_Add_Map);
        txt_Satellite_Map_Add = findViewById(R.id.txt_Satellite_Map_Add);

        sb_Zone = findViewById(R.id.sb_Place);

        img_place = findViewById(R.id.img_place);
        Intent intent = getIntent();
        chekcAddPlace = intent.getBooleanExtra("Place_Item_Add", false);
        fb_area_Place = (objArea) intent.getSerializableExtra("Place_Item");

        if (chekcAddPlace) {
            toolbar.setTitle(getResources().getString(R.string.Add_place));
            checkEditOrAdd = false;
            btn_AddPlaceAlert.setVisibility(View.VISIBLE);
            ln_Add_Delete.setVisibility(View.GONE);
        } else {
            toolbar.setTitle(getResources().getString(R.string.Edit_place));
            checkEditOrAdd = true;
            latLngPlaceAlertTamp = new LatLng(fb_area_Place.getLatitude(), fb_area_Place.getLongitude());
            btn_AddPlaceAlert.setVisibility(View.GONE);
            ln_Add_Delete.setVisibility(View.VISIBLE);
        }
    }

    private void setEvent() {

        // cimg_SelectAddMap là nút xử lý giúp người dùng chọn loại bản đồ muốn xem.
        cimg_SelectAddMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ln_Map_Type_Add_Select.setVisibility(View.VISIBLE);
                if (sharedPreferences.getInt(keyUtils.MAPTYPE, 1) == 1) {
                    txt_Normal_Add_Map.setTextColor(getResources().getColor(R.color.borderMap));
                    img_Map_Add_Street_Map.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
                    txt_Satellite_Map_Add.setTextColor(getResources().getColor(R.color.black));
                    img_Map_Add_Satellite.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    txt_Terrain_Add_Map.setTextColor(getResources().getColor(R.color.black));
                    img_Map_Add_Terrain.setBackgroundColor(getResources().getColor(R.color.mapselect));
                } else if (sharedPreferences.getInt(keyUtils.MAPTYPE, 1) == 2) {
                    txt_Normal_Add_Map.setTextColor(getResources().getColor(R.color.black));
                    img_Map_Add_Street_Map.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    txt_Satellite_Map_Add.setTextColor(getResources().getColor(R.color.borderMap));
                    img_Map_Add_Satellite.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
                    txt_Terrain_Add_Map.setTextColor(getResources().getColor(R.color.black));
                    img_Map_Add_Terrain.setBackgroundColor(getResources().getColor(R.color.mapselect));

                } else {
                    txt_Normal_Add_Map.setTextColor(getResources().getColor(R.color.black));
                    img_Map_Add_Street_Map.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    txt_Satellite_Map_Add.setTextColor(getResources().getColor(R.color.black));
                    img_Map_Add_Satellite.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    txt_Terrain_Add_Map.setTextColor(getResources().getColor(R.color.borderMap));
                    img_Map_Add_Terrain.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
                }
            }
        });

        img_Map_Add_Terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMap != null) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    editor.putInt(keyUtils.MAPTYPE, 3);
                    editor.apply();
                    ln_Map_Type_Add_Select.setVisibility(View.GONE);
                }
            }
        });
        img_Map_Add_Street_Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMap != null) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    editor.putInt(keyUtils.MAPTYPE, 1);
                    editor.apply();
                    ln_Map_Type_Add_Select.setVisibility(View.GONE);
                }
            }
        });
        img_Map_Add_Satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMap != null) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    ln_Map_Type_Add_Select.setVisibility(View.GONE);
                    editor.putInt(keyUtils.MAPTYPE, 2);
                    editor.apply();
                }
            }
        });

        txt_Close_Add_SelectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ln_Map_Type_Add_Select.setVisibility(View.GONE);
            }
        });

        // btn_AddPlaceAlert handles adding place alert to the family group's place list.
        btn_AddPlaceAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                savePlaceAlert();
            }
        });

        // btn_Edit_PlaceAlert xử lý thông báo chỉnh sửa địa điểm vào danh sách địa điểm của nhóm.
        btn_Edit_PlaceAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fb_area_Place != null) {
                    if (latLngPlaceAlert != null && fb_area_Place != null) {
                        if (!(latLngPlaceAlertTamp.equals(new LatLng(fb_area_Place.getLatitude(), fb_area_Place.getLongitude())))) {
                            checkEditPlace = true;

                        }
                    } else if (!textInputLayoutName.getEditText().getText().toString().equals(fb_area_Place.getRegionName())) {
                        checkEditPlace = true;
                    } else if (!(sb_Zone.getProgress() == fb_area_Place.getRadius())) {
                        checkEditPlace = true;
                    } else {
                        checkEditPlace = false;
                    }
                }
                if (checkEditPlace) {

                    savePlaceAlert();
                } else {
                    finish();
                }
            }
        });
        // btn_Delete_PlaceAlert xử lý xóa thông báo địa điểm vào danh sách địa điểm của nhóm.
        btn_Delete_PlaceAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deletePlaceAlert();
            }
        });

        sb_Zone.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {

            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                radius_Place = progress;
                if (progress >= 20) {
                    float dpCalculation = getResources().getDisplayMetrics().density;
                    int heght = 20 + progress / 10;
                    img_place.getLayoutParams().height = (int) (heght * dpCalculation);
                    img_place.getLayoutParams().width = (int) (heght * dpCalculation);
                    img_place.requestLayout();
                }
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
            }
        });

    }

    /**
     * deletePlaceAlert (): xóa arlert vị trí trong
     */
    private void deletePlaceAlert() {
        Log.d("KeyPlcae", fb_area_Place.getId() + "");
        mFirebaseDatabase.child(fb_area_Place.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PlaceAlertsDetail.this, "Delete Place Alert Successfully :)", Toast.LENGTH_SHORT).show();

                tb_Area.getInstance(getApplicationContext()).deleteArea(tb_CurrentFamilyID.getInstance(getApplicationContext()).getCurrentFamilyID(), fb_area_Place.getId());

                tb_Area.getInstance(getApplicationContext()).deleteArea(tb_CurrentFamilyID.getInstance(getApplicationContext()).getCurrentFamilyID(), fb_area_Place.getId());

                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Write failed
                Toast.makeText(PlaceAlertsDetail.this, "Delete Place Alert Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * đây là phương pháp lưu cảnh báo địa điểm vào firebase từ Model fb_area
     */
    private void savePlaceAlert() {


        if (checkEditOrAdd) {
            if (fb_area_Place != null && checkEditPlace) {

                // Double latitude, Double longitude, Integer radius, String regionName
                if (textInputLayoutName.getEditText().getText().toString().equals("")) {
                    Toast.makeText(this, "Please enter the place name!", Toast.LENGTH_SHORT).show();
                } else {
                    fb_area fb_area_alert;

                    if (latLngPlaceAlert == null) {

                        fb_area_alert = new fb_area(fb_area_Place.getLatitude(), fb_area_Place.getLongitude(), radius_Place, textInputLayoutName.getEditText().getText().toString());
                    } else {
                        fb_area_alert = new fb_area(latLngPlaceAlert.latitude, latLngPlaceAlert.longitude, radius_Place, textInputLayoutName.getEditText().getText().toString());
                    }
                    mFirebaseDatabase.child(fb_area_Place.getId()).setValue(fb_area_alert).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PlaceAlertsDetail.this, "Edit Place Alert Successfully.", Toast.LENGTH_SHORT).show();
                            tb_Area.getInstance(context).updateArea(fb_area_Place.getId(), tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID(), fb_area_alert);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            Toast.makeText(PlaceAlertsDetail.this, "Edit Place Alert Error!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        } else {
            if (latLngPlaceAlert == null) {
                Toast.makeText(this, "No Location!", Toast.LENGTH_SHORT).show();
            } else {
                fb_area fb_area_alert = new fb_area(latLngPlaceAlert.latitude, latLngPlaceAlert.longitude, radius_Place, textInputLayoutName.getEditText().getText().toString());
                String keyInPlaceAlert = mFirebaseDatabase.push().getKey();
                assert keyInPlaceAlert != null;
                Log.d("KeyPlcae", "KeyInFamily: " + tb_CurrentFamilyID.getInstance(getApplicationContext()).getCurrentFamilyID() + " Longtidue: " + latLngPlaceAlert.longitude + " lattidue: " + latLngPlaceAlert.latitude + " Name Place: " + textInputLayoutName.getEditText().getText().toString());
                // Double latitude, Double longitude, Integer radius, String regionName
                if (textInputLayoutName.getEditText().getText().toString().equals("")) {
                    Toast.makeText(this, "Please enter the place name!", Toast.LENGTH_SHORT).show();
                } else {

                    mFirebaseDatabase.child(String.valueOf(keyInPlaceAlert))
                            .setValue(fb_area_alert)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(PlaceAlertsDetail.this, "Thêm thông báo địa điểm thành công.", Toast.LENGTH_SHORT).show();
                                    tb_Area.getInstance(context).addOrUpdateArea(keyInPlaceAlert, tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID(), fb_area_alert);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            Toast.makeText(PlaceAlertsDetail.this, "Thêm địa điểm báo lỗi!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        if (chekcAddPlace)
        {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PlaceAlertsDetail.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{

                LocationManager lm = (LocationManager) PlaceAlertsDetail.this.getSystemService(Context.LOCATION_SERVICE);
                assert lm != null;
               // Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location myLocation = GPSService.mLastLocation;

                if(myLocation!=null){
                    LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 19), 800, null);
                }
            }
        }
        else {
            if (fb_area_Place != null)
            {
                Objects.requireNonNull(textInputLayout.getEditText()).setText(getAddress(fb_area_Place.getLatitude(), fb_area_Place.getLongitude()));
                Objects.requireNonNull(textInputLayoutName.getEditText()).setText(fb_area_Place.getRegionName());
                sb_Zone.setProgress(fb_area_Place.getRadius());
                if(fb_area_Place.getRadius() >=20)
                {
                    float dpCalculation = getResources().getDisplayMetrics().density;
                    int height = 20 + fb_area_Place.getRadius()/10;
                    img_place.getLayoutParams().height = (int) (height* dpCalculation);
                    img_place.getLayoutParams().width = (int) (height* dpCalculation);
                    img_place.requestLayout();
                }
                LatLng userLocation = new LatLng(fb_area_Place.getLatitude(), fb_area_Place.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 19), 800, null);
            }
        }


        // Đây là phương pháp giúp người dùng di chuyển camera và chọn vị trí của Cảnh báo địa điểm
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCameraMoveStarted(int i) {
                mMap.clear();
                // assign detailed address values to the detail box of the location
                latLngPlaceAlert = new LatLng(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude);
                latLngPlaceAlertTamp = latLngPlaceAlert;
                Log.d("placz","Lattidiue = "+mMap.getCameraPosition().target.latitude+"; "+ "Longtidue = "+ mMap.getCameraPosition().target.longitude );
                textInputLayout.getEditText().setText(" "+getAddress(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude));
                 //objFamily.getCurrentFamilyID(context);

            }
        });
    }

    /**
     * getAddress là phương thức chuyển đổi gps thành địa chỉ trong android
     *      * sử dụng cho phương thức setOnCameraMoveStartedListener ()
     * @param latitude latitude
     * @param longitude longitude
     * @return Address
     */
    public String getAddress(Double latitude, Double longitude) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String addressName = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() != 0 ) {
                String address = addresses.get(0).getAddressLine(0);
                String area = addresses.get(0).getLocality();
                String city = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                addressName = address + ", " + area + ", " + city + ", " + country + ".";
            }
        } catch (IOException e) {
            Log.d("address", e.getMessage() + "");
            e.printStackTrace();
        }
        if (addressName == null) {
            addressName = "Location not found!";
        }
        return addressName;
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}