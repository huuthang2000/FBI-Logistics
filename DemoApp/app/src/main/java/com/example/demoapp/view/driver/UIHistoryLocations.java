package com.example.demoapp.view.driver;

import static com.example.demoapp.Utils.keyUtils.areaList;
import static com.example.demoapp.Utils.keyUtils.history;
import static com.example.demoapp.Utils.keyUtils.recentLocations;
import static com.example.demoapp.Utils.patternFormatDateTime.hh_mm_a;
import static com.example.demoapp.Utils.timeUtils.convertMillisecondToStringFormat;
import static com.example.demoapp.Utils.timeUtils.formatCurrentDate;
import static com.example.demoapp.Utils.timeUtils.formatCurrentWeek;
import static com.example.demoapp.Utils.timeUtils.getYearAndWeek;
import static com.firebase.ui.auth.ui.email.EmailLinkFragment.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objectFirebase.account.fb_Location;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_Account;
import com.example.demoapp.SQLite.tb_CurrentFamilyID;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.timeUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;





public class UIHistoryLocations extends AppCompatActivity implements OnMapReadyCallback {
    private final Context context = this;

    private MarkerOptions markerOptions;
    private GoogleMap mMap;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Toolbar toolbar;
    private ProgressDialog mDialog;
    // sb_History_Location_Marker đây là một thanh tìm kiếm của bên thứ ba giúp hiển thị chi tiết số lượng vị trí gps
    // và có thể di chuyển từng vị trí để xem và hiển thị thời gian chi tiết của vị trí đó.
    private BubbleSeekBar sb_History_Location_Marker;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    // UID là biến lưu trữ ID của tài khoản thiết bị này
    // để lấy thông tin từ máy chủ FireBase.
    private String UID;
    // loadMoreTamp là giá trị để lưu trữ vị trí hiện tại của gps được tải
    private int loadMoreTamp = 0;
    // loadMoreCount là giá trị lưu trữ số lượng vị trí gps được tải cho ngày hiện tại
    private int loadMoreCount = 0;
    // LIMIT_LOAD_GPS Giá trị này mỗi khi người dùng nhấn nút để tải xuống thêm lịch sử gps.
    private final int LIMIT_LOAD_GPS = 100;
    LinearLayout ln_Map_Type_History_Select, ln_Loadmore;
    private ImageView img_back_History_Location,img_next_History_Location, img_Map_History_Street_Map,img_Map_History_Terrain,img_Map_History_Satellite;
    private CircleImageView cimg_SelectMapHistory;
    private TextView txt_Time_History_Location, txt_Close_History_SelectMap, txt_Normal_History_Map, txt_Terrain_History_Map,txt_Satellite_History_Add;
    private List<fb_Location> locationList;
    // lastDateSelect is the current time value currently displaying location history
    int numberDateSelect = 100;
    private List<String> stringListDateHistory;
    // Initialize View before initializing google map
    int locationCurrent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.history_marker_theme);
        setContentView(R.layout.activity_history_locations);
        toolbar = findViewById(R.id.toolbar_History_Location);
        stringListDateHistory = new ArrayList<>();

        showProgressDialog();

        setID();
        // setEvent() là một phương pháp để gán các sự kiện cho nút của người dùng.
        setEvent();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference().child(tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID()).child(areaList);
        Log.d("ZXZ",tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID()+" null");
        sharedPreferences = getApplicationContext().getSharedPreferences(keyUtils.KEY_FAMILY_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Tăng bố cục cho phân đoạn này.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_History_Location);
        mapFragment.getMapAsync(this);

    }

    private void setEvent() {

        // img_back_History_Location là nút để quay lại ngày hôm trước của danh sách vị trí.
        // example from day 28 to day 27
        img_back_History_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sb_History_Location_Marker.setProgress(1);
                if(stringListDateHistory.size() !=0)
                {
                    Log.d("numberSelect", numberDateSelect+"");
                    if(numberDateSelect != 30)
                    {
                        if(numberDateSelect>0)
                        {
                            showProgressDialog();
                            if(numberDateSelect == 1)
                            {
                                img_next_History_Location.setVisibility(View.VISIBLE);
                                img_back_History_Location.setVisibility(View.INVISIBLE);
                                getListLocationMarker(UID, stringListDateHistory.get(numberDateSelect-1)+"",numberDateSelect-1);
                            }
                            else {
                                img_back_History_Location.setVisibility(View.VISIBLE);
                                img_next_History_Location.setVisibility(View.VISIBLE);
                                getListLocationMarker(UID, stringListDateHistory.get(numberDateSelect-1)+"",numberDateSelect-1);
                            }
                        }
                        else
                        {
                            img_back_History_Location.setVisibility(View.INVISIBLE);
                            img_next_History_Location.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        // img_next_History_Location là nút xử lý cộng với 1 ngày của danh sách vị trí.
        // example from day 28 to day 29
        img_next_History_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb_History_Location_Marker.setProgress(1);
                // stringListDateHistory is a list of all the days of the week with locations.
                if(stringListDateHistory.size() !=0)
                {
                    Log.d("numberSelect", numberDateSelect+" ; "+ stringListDateHistory.size());
                    if(numberDateSelect != 30 )
                    {
                        if(numberDateSelect >= stringListDateHistory.size()-2)
                        {
                            img_next_History_Location.setVisibility(View.INVISIBLE);
                            img_back_History_Location.setVisibility(View.VISIBLE);
                            getListLocationMarker(UID, stringListDateHistory.get(numberDateSelect+1)+"",numberDateSelect+1);
                            showProgressDialog();
                        }
                        else
                        {
                            getListLocationMarker(UID, stringListDateHistory.get(numberDateSelect+1)+"",numberDateSelect+1);
                            showProgressDialog();
                            img_back_History_Location.setVisibility(View.VISIBLE);
                            img_next_History_Location.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        // ln_Load more là nút xử lý việc tải thêm lịch sử vị trí
        //        // khi danh sách vị trí lớn hơn 40 điểm trong một ngày.
        ln_Loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(loadMoreTamp !=0)
                {
                    showProgressDialog();
                    //WaitingDialog.showDialog(UIHistoryLocations.this);
                    if((locationList.size() - loadMoreCount) >= (LIMIT_LOAD_GPS -1))
                    {
                        loadMoreCount += LIMIT_LOAD_GPS;
                        int tamp = loadMoreTamp -(LIMIT_LOAD_GPS -1);
                        //sb_History_Location_Marker.setProgress(locationList.size());
                        for (int i = tamp; i <= loadMoreTamp; i++)
                        {
                            Log.d("CurrentZas", i+"");
                            LatLng sydney = new LatLng(locationList.get(i).getLatitude(), locationList.get(i).getLongitude());
                            Log.d("CurrentZ", locationList.get(i).getLatitude() + "==" + locationList.get(i).getLongitude());
                            //int numbermarker = i+1;
                            int finalI = i;
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //int numbermarker = i+1;
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(timeUtils.getAddress(sydney.latitude,sydney.longitude,getApplicationContext())+"")

                                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapMarkerFromView(finalI+1+""))));
                                }
                                // your UI code here
                            });
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //int numbermarker = i+1;
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(timeUtils.getAddress(sydney.latitude,sydney.longitude,getApplicationContext())+"")
                                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapMarkerFromView(finalI+1+""))));

                                }
                            });

                            if(i == loadMoreTamp -1)
                            {
                                sb_History_Location_Marker.setVisibility(View.VISIBLE);
                                int sectionCount = loadMoreCount;
                                sb_History_Location_Marker.getConfigBuilder()
                                        .max(locationList.size())
                                        .min((loadMoreTamp - LIMIT_LOAD_GPS) +2)
                                        .sectionCount(sectionCount-2)
                                        .build();
                                sb_History_Location_Marker.setProgress(i);
                                CameraPosition cameraPosition = CameraPosition.builder()
                                        .target(sydney)
                                        .bearing(2.5f)
                                        .tilt(45)
                                        .zoom(16)
                                        .build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                mMap.animateCamera(cameraUpdate, 900, null);
                                hideProgressDialog();
                            }
                        }
                        loadMoreTamp = loadMoreTamp - LIMIT_LOAD_GPS;
                    }
                    else {
                        ln_Loadmore.setVisibility(View.GONE);
                        try {
                            if(locationList.size()>0)
                            {
                                for (int i = 0; i < loadMoreTamp; i++)
                                {

                                    LatLng sydney = new LatLng(locationList.get(i).getLatitude(), locationList.get(i).getLongitude());
                                    Log.d("CurrentZ", locationList.get(i).getLatitude() + "==" + locationList.get(i).getLongitude());

                                    int finalI = i;
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //int numbermarker = i+1;
                                            mMap.addMarker(new MarkerOptions()
                                                    .position(sydney)
                                                    .title(timeUtils.getAddress(sydney.latitude,sydney.longitude,getApplicationContext())+"")
                                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapMarkerFromView(finalI +1+""))));
                                        }
                                        // your UI code here
                                    });
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //int numbermarker = i+1;
                                            mMap.addMarker(new MarkerOptions()
                                                    .position(sydney)
                                                    .title(timeUtils.getAddress(sydney.latitude,sydney.longitude,getApplicationContext())+"")
                                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapMarkerFromView(finalI +1+""))));
                                        }
                                    });

                                    if(i == 0)
                                    {
                                        sb_History_Location_Marker.setVisibility(View.VISIBLE);
                                        sb_History_Location_Marker.getConfigBuilder()
                                                .max(locationList.size())
                                                .min(1)
                                                .sectionCount(locationList.size()-1)
                                                .build();
                                        hideProgressDialog();
                                        CameraPosition cameraPosition = CameraPosition.builder()
                                                .target(sydney)
                                                .bearing(2.5f)
                                                .tilt(45)
                                                .zoom(16)
                                                .build();
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                        mMap.animateCamera(cameraUpdate, 900, null);
                                    }
                                }
                            }

                        }catch (Exception e)
                        {
                            e.getMessage();
                        }

                    }
                }
            }
        });
        txt_Close_History_SelectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_Map_Type_History_Select.setVisibility(View.GONE);
            }
        });

        // cimg_SelectMapHistory is the button that handles the choice of the map type that the user wants to see.
        cimg_SelectMapHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ln_Map_Type_History_Select.setVisibility(View.VISIBLE);
                if(sharedPreferences.getInt(keyUtils.MAPTYPE,1) == 1)
                {
                    txt_Normal_History_Map.setTextColor(getResources().getColor(R.color.borderMap));
                    img_Map_History_Street_Map.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
                    txt_Satellite_History_Add.setTextColor(getResources().getColor(R.color.black));
                    img_Map_History_Satellite.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    txt_Terrain_History_Map.setTextColor(getResources().getColor(R.color.black));
                    img_Map_History_Terrain.setBackgroundColor(getResources().getColor(R.color.mapselect));
                }

                else if(sharedPreferences.getInt(keyUtils.MAPTYPE,1) == 2)
                {
                    txt_Normal_History_Map.setTextColor(getResources().getColor(R.color.black));
                    img_Map_History_Street_Map.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    txt_Satellite_History_Add.setTextColor(getResources().getColor(R.color.borderMap));
                    img_Map_History_Satellite.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
                    txt_Terrain_History_Map.setTextColor(getResources().getColor(R.color.black));
                    img_Map_History_Terrain.setBackgroundColor(getResources().getColor(R.color.mapselect));

                }
                else
                {
                    txt_Normal_History_Map.setTextColor(getResources().getColor(R.color.black));
                    img_Map_History_Street_Map.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    txt_Satellite_History_Add.setTextColor(getResources().getColor(R.color.black));
                    img_Map_History_Satellite.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    txt_Terrain_History_Map.setTextColor(getResources().getColor(R.color.borderMap));
                    img_Map_History_Terrain.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
                }
            }
        });

        img_Map_History_Terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMap!= null)
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    editor.putInt(keyUtils.MAPTYPE,3);
                    editor.apply();
                    ln_Map_Type_History_Select.setVisibility(View.GONE);
                }
            }
        });
        img_Map_History_Street_Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMap!= null)
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    editor.putInt(keyUtils.MAPTYPE,1);
                    editor.apply();
                    ln_Map_Type_History_Select.setVisibility(View.GONE);
                }
            }
        });
        img_Map_History_Satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMap!= null)
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    ln_Map_Type_History_Select.setVisibility(View.GONE);
                    editor.putInt(keyUtils.MAPTYPE,2);
                    editor.apply();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setID() {
        sb_History_Location_Marker = findViewById(R.id.sb_History_Location_Marker);
        //bsb_show_progress_in_float
        sb_History_Location_Marker.setVisibility(View.GONE);
        Intent intent = getIntent();
        UID = intent.getStringExtra("locationUser");
        objAccount objAccount = tb_Account.getInstance(getApplicationContext()).getAccountByID(UID);
        Log.d("uidd", UID+ " "+ objAccount.getName());
        toolbar.setTitle(objAccount.getName());
        setSupportActionBar(toolbar);
        ln_Loadmore = findViewById(R.id.ln_Loadmore);
        cimg_SelectMapHistory = findViewById(R.id.cimg_SelectMapHistory);
        ln_Map_Type_History_Select = findViewById(R.id.ln_Map_Type_History_Select);
        ln_Map_Type_History_Select.setVisibility(View.GONE);
        img_Map_History_Street_Map = findViewById(R.id.img_Map_History_Street_Map);
        img_Map_History_Terrain = findViewById(R.id.img_Map_History_Terrain);
        img_Map_History_Satellite = findViewById(R.id.img_Map_History_Satellite);
        txt_Close_History_SelectMap = findViewById(R.id.txt_Close_History_SelectMap);
        txt_Normal_History_Map = findViewById(R.id.txt_Normal_History_Map);
        txt_Terrain_History_Map = findViewById(R.id.txt_Terrain_History_Map);
        txt_Satellite_History_Add = findViewById(R.id.txt_Satellite_History_Add);
        txt_Time_History_Location = findViewById(R.id.txt_Time_History_Location);
        img_back_History_Location = findViewById(R.id.img_back_History_Location);
        img_next_History_Location = findViewById(R.id.img_next_History_Location);

        sb_History_Location_Marker.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {


            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                //sb_History_Location_Marker.setProgress(progress);
                Log.d("prges", progress+"");
                locationCurrent = progress -1;
                if(locationList.size()>0)
                {
                    LatLng sydney = new LatLng(locationList.get(locationCurrent).getLatitude(), locationList.get(locationCurrent).getLongitude());
                    Log.d("CurrentZ", locationList.get(locationCurrent).getLatitude() + "==" + locationList.get(locationCurrent).getLongitude());
                    //int numbermarker = i+1;

                    CameraPosition cameraPosition = CameraPosition.builder()
                            .target(sydney)
                            .bearing(2.5f)
                            .tilt(45)
                            .zoom(18)
                            .build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    mMap.animateCamera(cameraUpdate, 900, null);

                }

            }
            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                try {
                    String timeLocation = convertMillisecondToStringFormat(locationList.get(locationCurrent).getTimeUpdate(),hh_mm_a);
                    Toast toast=Toast.makeText(context,timeLocation+"", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.BOTTOM, 10, 200);
                    toast.show();
                }catch (Exception e)
                {
                    e.getMessage();
                }

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });

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
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if(!UID.isEmpty())
        {
            // Khi bản đồ đã tải xong và UID không bằng null,
            // nó sẽ tải xuống dữ liệu lịch sử vị trí trong tuần.
            getWeekCurrentInHistoryLocation();

        }
    }

    /**
     *getWeekCurrentInHistoryLocation () Đây là phương thức
     *      * hỗ trợ tải xuống tất cả các ngày với lịch sử vị trí cho tuần hiện tại.
     */
    private void getWeekCurrentInHistoryLocation() {

        List<String> listWeekInYear = new ArrayList<>() ;
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference().child(history).child(UID);
        mFirebaseDatabase.limitToLast(2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.getChildrenCount()<2)
                {
                    img_back_History_Location.setVisibility(View.INVISIBLE);
                    img_next_History_Location.setVisibility(View.INVISIBLE);
                }
                Log.d("lsaas", dataSnapshot.getChildrenCount()+""+ "week"+ getYearAndWeek());
                if(dataSnapshot.getChildrenCount()>=1)
                {
                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {
                        Log.d("lsaas", data.getKey()+" = parent");
                        listWeekInYear.add(data.getKey()+"");

                    }
                    int countGetKeyGPS = 0;

                    if(listWeekInYear.size()>=1)
                    {
                        for (String key : listWeekInYear)
                        {
                            countGetKeyGPS ++;
                            mFirebaseInstance = FirebaseDatabase.getInstance();
                            mFirebaseDatabase = mFirebaseInstance.getReference().child(history).child(UID);
                            int finalCountGetKeyGPS = countGetKeyGPS;
                            Log.d("lsaas", finalCountGetKeyGPS+" = finalCountGetKeyGPS");
                            mFirebaseDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                                    Log.d("lsaas", dataSnapshot.getChildrenCount()+" Driver Count");
                                    if(dataSnapshot.getChildrenCount()>=1)
                                    {
                                        int countChildrenGetKeyGPS = 0;
                                        for (DataSnapshot data : dataSnapshot.getChildren())
                                        {
                                            countChildrenGetKeyGPS ++;
                                            Log.d("lsaas", data.getKey()+"driver");
                                            stringListDateHistory.add(data.getKey()+"");
                                            if(finalCountGetKeyGPS == listWeekInYear.size())
                                            {

                                                if(countChildrenGetKeyGPS == dataSnapshot.getChildrenCount())
                                                {
                                                    Log.d("lsaas", countChildrenGetKeyGPS+" = countChildrenGetKeyGPS");

                                                    Log.d("lsaas", stringListDateHistory.size()+" stringListDateHistory.size()");
                                                    if (stringListDateHistory.size()<2)
                                                    {
                                                        img_back_History_Location.setVisibility(View.INVISIBLE);
                                                        img_next_History_Location.setVisibility(View.INVISIBLE);
                                                    }
                                                    if(stringListDateHistory.size()>=1)
                                                    {
                                                        getListLocationMarker(UID, stringListDateHistory.get(stringListDateHistory.size()-1)+"",stringListDateHistory.size()-1);
                                                    }
                                                    else {
                                                        img_back_History_Location.setVisibility(View.INVISIBLE);
                                                        img_next_History_Location.setVisibility(View.INVISIBLE);
                                                        txt_Time_History_Location.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(context, "Hiện tại không có lịch sử vị trí!", Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });

                        }
                    }
                }
                else {
                    img_back_History_Location.setVisibility(View.INVISIBLE);
                    img_next_History_Location.setVisibility(View.INVISIBLE);
                    txt_Time_History_Location.setVisibility(View.INVISIBLE);

                    Toast.makeText(context, "Hiện tại không có lịch sử vị trí!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * getMarkerBitmapFromView Đây là phương pháp giúp bạn thêm hình ảnh cho điểm đánh dấu biểu tượng của bản đồ
     * @return bitmap no_avatar
     */
    private Bitmap getMarkerBitmapMarkerFromView(String number_marker)
    {
        final Bitmap returnedBitmap;

        View customMarkerView = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_view_custom_marker_history, null);
        TextView txt_number_marker = customMarkerView.findViewById(R.id.txt_number_marker);
        txt_number_marker.setText(number_marker);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    /**
     * getListLocationMarker ()là một phương pháp để xử lý việc tải xuống danh sách vị trí
     *của một ngày chi tiết mà người dùng chọn để xem.
     */
    @SuppressLint("RestrictedApi")
    public void getListLocationMarker(String UID, String lastDate, int dateSelect) {

        // numberDateSelect: là số ngày hiện tại mà người dùng đang chọn
        // để xem lịch sử vị trí
        numberDateSelect  = dateSelect;
        Log.d("lsaas", dateSelect+" = dateSelect ");
        if( numberDateSelect == stringListDateHistory.size()-1)
        {
            img_next_History_Location.setVisibility(View.INVISIBLE);
        }

        txt_Time_History_Location.setText(formatCurrentDate(lastDate));
        String dateFormat = formatCurrentWeek(lastDate);
        Log.d("lsaas", dateFormat+" = dateFormat ");
        final Query mRefChat = mFirebaseInstance.getReference()
                .child(history)
                .child(UID)
                .child(dateFormat)
                .child(lastDate)
                .child(recentLocations);

        mRefChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locationList = new ArrayList<>();
                loadMoreCount = 0;
                for (DataSnapshot dataSnapshotArea : dataSnapshot.getChildren())
                {
                    // add in sqlite
                    fb_Location fb_location = dataSnapshotArea.getValue(fb_Location.class);
                    locationList.add(fb_location);
                    //tb_Location.getInstance(getApplicationContext()).addLocation(dataSnapshotArea.getKey(),tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID(),fb_location);
                }

                // Check for null
                if (locationList == null)
                {

                    Log.d("TAGZ", "User data is null!");
                    return;
                }
                else if(locationList.size() == 0)
                {

                    hideProgressDialog();
                    Toast.makeText(context, "No location!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mMap.clear();
                    if(locationList.size()>1)
                    {


                        if(locationList.size()>LIMIT_LOAD_GPS)
                        {
                            int sectionCount = LIMIT_LOAD_GPS;
                            ln_Loadmore.setVisibility(View.VISIBLE);
                            sb_History_Location_Marker.setVisibility(View.VISIBLE);
                            sb_History_Location_Marker.getConfigBuilder()
                                    .max(locationList.size())
                                    .min((locationList.size() - LIMIT_LOAD_GPS) + 2)
                                    .sectionCount(sectionCount-2)
                                    .build();
                            sb_History_Location_Marker.setProgress(locationList.size());
                        }
                        else {
                            ln_Loadmore.setVisibility(View.INVISIBLE);
                            sb_History_Location_Marker.setVisibility(View.VISIBLE);
                            sb_History_Location_Marker.getConfigBuilder()
                                    .max(locationList.size())
                                    .min(1)
                                    .sectionCount(locationList.size()-5)
                                    .build();
                            sb_History_Location_Marker.setProgress(locationList.size());
                        }
                    }
                    else
                    {

                        hideProgressDialog();
                        sb_History_Location_Marker.setVisibility(View.GONE);
                    }

                    if(locationList.size()>LIMIT_LOAD_GPS)
                    {
                        loadMoreCount += LIMIT_LOAD_GPS;
                    }
                    for (int i = 0 ; i < locationList.size(); i++)
                    {

                        if(locationList.size()>LIMIT_LOAD_GPS)
                        {
                            ln_Loadmore.setVisibility(View.VISIBLE);

                            if(i>(locationList.size()-LIMIT_LOAD_GPS))
                            {
                                Log.d("CurrentZas", locationList.size()-LIMIT_LOAD_GPS+"");
                                loadMoreTamp = locationList.size()-LIMIT_LOAD_GPS;
                                LatLng sydney = new LatLng(locationList.get(i).getLatitude(), locationList.get(i).getLongitude());
                                Log.d("CurrentZ", locationList.get(i).getLatitude() + "==" + locationList.get(i).getLongitude());
                                //int numbermarker = i+1;

                                int finalI = i;
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //int numbermarker = i+1;

                                        mMap.addMarker(new MarkerOptions()
                                                .position(sydney)
                                                .title(timeUtils.getAddress(sydney.latitude,sydney.longitude,getApplicationContext())+"")
                                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapMarkerFromView(finalI +1+""))));
                                    }
                                    // your UI code here
                                });

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMap.addMarker(new MarkerOptions()
                                                .position(sydney)
                                                .title(timeUtils.getAddress(sydney.latitude,sydney.longitude,getApplicationContext())+"")
                                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapMarkerFromView(finalI +1+""))));
                                    }
                                });

                                if(i == locationList.size()-1)
                                {

                                    hideProgressDialog();
                                    CameraPosition cameraPosition = CameraPosition.builder()
                                            .target(sydney)
                                            .bearing(2.5f)
                                            .tilt(45)
                                            .zoom(16)
                                            .build();
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                    mMap.animateCamera(cameraUpdate, 100, null);

                                }
                            }
                        }
                        else
                        {
                            ln_Loadmore.setVisibility(View.GONE);
                            LatLng sydney = new LatLng(locationList.get(i).getLatitude(), locationList.get(i).getLongitude());
                            Log.d("CurrentZ", locationList.get(i).getLatitude() + "==" + locationList.get(i).getLongitude());
                            //int numbermarker = i+1;

                            int finalI = i;
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //int numbermarker = i+1;
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(timeUtils.getAddress(sydney.latitude,sydney.longitude,getApplicationContext())+"")
                                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapMarkerFromView(finalI+1+""))));
                                }
                                // your UI code here
                            });
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(timeUtils.getAddress(sydney.latitude,sydney.longitude,getApplicationContext())+"")
                                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapMarkerFromView(finalI+1+""))));

                                }
                            });

                            if(i == locationList.size()-1)
                            {

                                hideProgressDialog();
                                CameraPosition cameraPosition = CameraPosition.builder()
                                        .target(sydney)
                                        .bearing(2.5f)
                                        .tilt(45)
                                        .zoom(16)
                                        .build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                mMap.animateCamera(cameraUpdate, 100, null);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    void showProgressDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage(getString(R.string.downloading));
            mDialog.setIndeterminate(true);
        }
        this.mDialog.show();
    }

    void hideProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            this.mDialog.dismiss();
            mDialog = null;
        }
    }

}
