package com.example.demoapp.view.driver.autoStarService;

import static com.example.demoapp.Models.objApplication.objAccount.getCurrentUser;
import static com.example.demoapp.Utils.keyUtils.IDFAMILY;
import static com.example.demoapp.Utils.keyUtils.IDFamilyList;
import static com.example.demoapp.Utils.keyUtils.LATITUDEOLDACCOUNT;
import static com.example.demoapp.Utils.keyUtils.LONGTITUDEOLDACCOUNT;
import static com.example.demoapp.Utils.keyUtils.Safe_Area_Notification;
import static com.example.demoapp.Utils.keyUtils.accountList;
import static com.example.demoapp.Utils.keyUtils.history;
import static com.example.demoapp.Utils.keyUtils.location;
import static com.example.demoapp.Utils.keyUtils.name;
import static com.example.demoapp.Utils.keyUtils.recentLocations;
import static com.example.demoapp.Utils.notificationUtils.buildNotificationService;
import static com.example.demoapp.Utils.timeUtils.getCurrentDate;
import static com.example.demoapp.Utils.timeUtils.getYearAndWeek;
import static com.example.demoapp.Utils.timeUtils.isNetworkAvailable;
import static com.example.demoapp.view.driver.UISafety.SpeedService.isDriving;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objArea;
import com.example.demoapp.Models.objApplication.objFamily;
import com.example.demoapp.Models.objApplication.objLocation;
import com.example.demoapp.Models.objectFirebase.account.fb_Location;
import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;
import com.example.demoapp.Models.objectFirebase.chat.fb_Message;
import com.example.demoapp.Models.objectFirebase.family.fb_Safe_Area_Notification;
import com.example.demoapp.Presenters.Chat.pre_Chat;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_Area;
import com.example.demoapp.SQLite.tb_CurrentFamilyID;
import com.example.demoapp.SQLite.tb_Family;
import com.example.demoapp.SQLite.tb_Location;
import com.example.demoapp.SQLite.tb_Message;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.notificationUtils;
import com.example.demoapp.view.driver.UIMain;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class GPSService extends Service {

    private static final String TAG = GPSService.class.getSimpleName();
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    // mLastLocation is the location value sent to the server
    public static Location mLastLocation;
    private Location mLocation = null;
    private static final int LOCATION_REQUEST = 1000;
    private LocationRequest locationRequest;
    private long UPDATE_INTERVAL = 1;
    // GPSFirst is the first time taken.
    private boolean GPSFirst = false;
    // safeArea is a boolean value to remember the current location in a safe zone
    // or out of a safe area
    fb_Location fb_location ;
    List<objArea>  objAreaList;
    List<objArea>  objAreaListTamp;
    List<objFamily>  objFamilyList;

    private int areaCount;
    private int checkCountArea;
    private boolean checkPushnotification = false;

    // locationSafe được sử dụng để lưu trữ vị trí hiện tại trong khu vực an toàn của locationList này
    private Location locationSafe = null;
    // safeArea là một giá trị boolean để ghi nhớ vị trí hiện tại trong vùng an toàn
    // hoặc ra khỏi vùng an toàn
    private boolean safeArea = false;

    // lưu tên hiện tại của vùng an toàn enter
    String nameArea;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private objAccount AccountName;
    private String nameAccount;
    List<String> listFamilyID;
    Calendar calendar;
    private boolean checkStartCommand = false;

    public GPSService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        objFamilyList = new ArrayList<>();
        objAreaList = new ArrayList<>();
        objAreaListTamp = new ArrayList<>();
        calendar = Calendar.getInstance();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'accountList' node
        fb_location = new fb_Location();
        //locationSafe = new Location("");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Start service GPSService");

        if(com.example.demoapp.view.driver.autoStarService.mAlarm.checkDevice()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(3, buildNotificationService(this));
            }
        }

        try {

            //addAreaChangeListener();
            if(!checkStartCommand)
            {
                getNotificationOfArea();
                checkStartCommand = true;
            }

            // thiết bị bị xóa
            Log.d("notification", "action"+ " check Push notification = "+ checkPushnotification);

            Log.d("notification", "START_LOCATION_ACTION"+ " check safe = "+safeArea);

            pre_Chat.getNewMessageOfUid(this, new pre_Chat.onResultNotification() {
                @Override
                public void onResult(fb_Message message, fb_Chat chatDetail, String idChat, int position) {

                    final String currentUid = objAccount.getCurrentUser().getUid();
                    if(!UIMain.isFocusActivityCHAT &&
                            !message.getMembersListReceived().contains(currentUid))
                    {

                        String title;
                        String content;
                        String nameAuth;
                        try {
                            nameAuth = objAccount.getAccountFromSQLite(GPSService.this,message.getAuth()).getName();

                        }catch (Exception e)
                        {
                            Log.d("error", e.getMessage()+"");
                            nameAuth = "unKnow";
                        }

                        if(chatDetail.getType().matches(keyUtils.PRIVATE)){
                            title = "Message from \"" + nameAuth + "\"";
                            if(message.getType().matches(keyUtils.PICTURE) || message.getType().matches(keyUtils.MULTI_PICTURE))
                                content = nameAuth + " " + getResources().getString(R.string.sent_photos);
                            else
                                content =  message.getContent();
                        }
                        else{
                            title = "Message from \""+ chatDetail.getChatName() + "\"";
                            if(message.getType().matches(keyUtils.PICTURE) || message.getType().matches(keyUtils.MULTI_PICTURE))
                                content = nameAuth + getResources().getString(R.string.sent_photos);
                            else
                                content = nameAuth + ": " + message.getContent();

                        }

                        notificationUtils.buildNotificationMessage(GPSService.this,
                                (int)chatDetail.getNotificationID(),
                                title,
                                content,
                                idChat,
                                chatDetail);

                        //set the list of members received
                        if(!message.getMembersListReceived().contains(currentUid)){

                            List<String> memberReceived = message.getMembersListReceived();
                            memberReceived.add(currentUid);
                            FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child(keyUtils.messageList)
                                    .child(idChat)
                                    .child(String.valueOf(position))
                                    .child(keyUtils.membersListReceived)
                                    .setValue(memberReceived)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            tb_Message.getInstance(GPSService.this).updateMessage(position,idChat,message);
                                        }
                                    });
                        }
                    }
                }
            });


        // Thông báo mỗi khi một thành viên trong nhóm nhấp vào nút cần trợ giúp

            if(GPSFirst)
            {
                // Check if this is the first time taking the position or not.
                mFusedLocationClient.removeLocationUpdates(locationCallback);
                getLocation(UPDATE_INTERVAL*10000,UPDATE_INTERVAL*10000);
            }
            else
            {
                GPSFirst = true;

                getLocation(UPDATE_INTERVAL*10000,UPDATE_INTERVAL*10000);
            }
        }
        catch(Exception e)
        {
            Log.e(TAG,e.getMessage());
        }
        return START_STICKY;
    }

    /**
     * whenever there is a new position it will call the method displayLocation()
     * This method handles sending location to the FireBase server
     * */
    private void displayLocation() {

        if (mLastLocation != null)
        {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(keyUtils.KEY_FAMILY_ID, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            mLocation = mLastLocation;

            try {

                // locationNew là vị trí mới nhất vừa được truy xuất
                // từ thiết bị và sẽ so sánh nó với danh sách các vị trí được chọn trước
                // để xem nó nằm trong vùng an toàn hay đã thoát ra khỏi vùng an toàn
                Location locationOldCurrent = new Location("");
                locationOldCurrent.setLatitude(Double.parseDouble(sharedPreferences.getString(keyUtils.LATITUDEOLD,"0.0")));
                locationOldCurrent.setLongitude(Double.parseDouble(sharedPreferences.getString(keyUtils.LONGTITUDEOLD,"0.0")));
                String time_LocationOld = sharedPreferences.getString(keyUtils.TIMELOCATION,"null");
                Log.d("checkdisance"," locationOldLAT = "+locationOldCurrent.getLatitude()+ " locationOldLONG"+ locationOldCurrent.getLongitude());

                Location locationOldAccount = new Location("");
                locationOldAccount.setLatitude(Double.parseDouble(sharedPreferences.getString(keyUtils.LATITUDEOLDACCOUNT,"0.0")));
                locationOldAccount.setLongitude(Double.parseDouble(sharedPreferences.getString(keyUtils.LONGTITUDEOLDACCOUNT,"0.0")));

                Location locationNew = new Location("");
                locationNew.setLatitude(mLocation.getLatitude());
                locationNew.setLongitude(mLocation.getLongitude());

                // if locationOldCurent = 0.0 means that the first time to get the location, so there's no need to compare and upload the location to FireBase.
                if(locationOldCurrent.getLongitude() != 0.0)
                {

                    // kiểm tra gps nếu internet ngoại tuyến lưu vị trí vào sqlite đợi internet trực tuyến tải lên firebase
                    // tạo khóa cho vị trí hàng
                    if(isNetworkAvailable(getApplicationContext()))
                    {
                        // List<fb_Location> getLocationsListByUID (String UID)
                        List<objLocation> locationList= tb_Location.getInstance(getApplicationContext()).getLocationsListByUID(objAccount.getCurrentUser().getUid());
                        try {
                            if(locationList.size()>0)
                            {
                                Log.d("locationList", "locationList = "+locationList.size()+"");
                                for (objLocation objLocation :locationList) {
                                    updateListLocationFromSQLite(objLocation);
                                    // delete location
                                }
                            }
                        }catch (Exception e)
                        {
                            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                        }
                    }

                    float distanceInMetersHistory = locationOldCurrent.distanceTo(locationNew);
                    Log.d("checkdisance","distanceInMeters = "+distanceInMetersHistory);
                    fb_location.setLatitude(mLocation.getLatitude());
                    fb_location.setLongitude(mLocation.getLongitude());
                    fb_location.setTimeUpdate(mLocation.getTime());

                    if(locationOldAccount.getLongitude() != 0.0)
                    {
                        float distanceInMetersAccount = locationOldAccount.distanceTo(locationNew);
                        if(distanceInMetersAccount > 9)
                        {


                // Kiểm tra nếu vị trí lớn hơn 30 mét, nó sẽ cập nhật vị trí mới - cứ 1 giây lấy lại vị trí
                            Log.d("checkdisance","updateCurrentLocation distanceInMetersAccount");
                            //updateListLocation(fb_location);
                            updateCurrentLocation(fb_location);
                            editor.putString(keyUtils.LATITUDEOLDACCOUNT,mLocation.getLatitude()+"");
                            editor.putString(keyUtils.LONGTITUDEOLDACCOUNT,mLocation.getLongitude()+"");
                            editor.apply();
                        }
                    }
                    else
                    {
                        // since there's no location for the first time, this location is automatically taken first
                        editor.putString(LATITUDEOLDACCOUNT,mLocation.getLatitude()+"");
                        editor.putString(LONGTITUDEOLDACCOUNT,mLocation.getLongitude()+"");
                        editor.apply();
                        fb_location.setLatitude(mLocation.getLatitude());
                        fb_location.setLongitude(mLocation.getLongitude());
                        fb_location.setTimeUpdate(mLocation.getTime());
                        updateCurrentLocation(fb_location);
                    }
                    if(!(time_LocationOld.equals(getCurrentDate())))
                    {
                        // Kiểm tra nếu vị trí lớn hơn 30 mét, nó sẽ cập nhật vị trí mới - cứ 1 giây lấy lại vị trí
                        Log.d("checkdisance","push location data due to different dates");
                        fb_location.setLatitude(mLocation.getLatitude());
                        fb_location.setLongitude(mLocation.getLongitude());
                        fb_location.setTimeUpdate(mLocation.getTime());
                        // check gps if internet offline save location to sqlite wait internet online upload to firebase
                        // create key for row location
                        if(!(isNetworkAvailable(getApplicationContext())))
                        {
                            String timeUseKeyID = String.valueOf(System.currentTimeMillis());
                            tb_Location.getInstance(getApplicationContext()).addLocation(timeUseKeyID,objAccount.getCurrentUser().getUid(),fb_location);
                        }
                        else {
                            updateListLocation(fb_location);
                        }

                        editor.putString(keyUtils.LATITUDEOLD,locationNew.getLatitude()+"");
                        editor.putString(keyUtils.LONGTITUDEOLD,locationNew.getLongitude()+"");
                        editor.putString(keyUtils.TIMELOCATION,getCurrentDate());
                        editor.apply();
                    }
                    else {

                        if(isDriving)
                        {
                            if(distanceInMetersHistory > 100)
                            {

                                // Check if the position is greater than 30 meters, it will update the new position - take the position every 1 second
                                Log.d("checkdisance","updateCurrentLocation distanceInMetersHistory ");
                                fb_location.setLatitude(mLocation.getLatitude());
                                fb_location.setLongitude(mLocation.getLongitude());
                                fb_location.setTimeUpdate(mLocation.getTime());
                                // check gps if internet offline save location to sqlite wait internet online upload to firebase
                                // create key for row location
                                if(!(isNetworkAvailable(getApplicationContext())))
                                {
                                    String timeUseKeyID = String.valueOf(System.currentTimeMillis());
                                    tb_Location.getInstance(getApplicationContext()).addLocation(timeUseKeyID,objAccount.getCurrentUser().getUid(),fb_location);
                                }
                                else {
                                    updateListLocation(fb_location);
                                }

                                editor.putString(keyUtils.LATITUDEOLD,locationNew.getLatitude()+"");
                                editor.putString(keyUtils.LONGTITUDEOLD,locationNew.getLongitude()+"");
                                editor.putString(keyUtils.TIMELOCATION,getCurrentDate());
                                editor.apply();
                            }
                        }
                        else
                        {
                            if(distanceInMetersHistory > 25)
                            {
                                // Kiểm tra nếu vị trí lớn hơn 30 mét, nó sẽ cập nhật vị trí mới - cứ 1 giây lấy lại vị trí
                                Log.d("checkdisance","updateCurrentLocation distanceInMetersHistory ");
                                fb_location.setLatitude(mLocation.getLatitude());
                                fb_location.setLongitude(mLocation.getLongitude());
                                fb_location.setTimeUpdate(mLocation.getTime());
                                // kiểm tra gps nếu internet ngoại tuyến lưu vị trí vào sqlite đợi internet trực tuyến tải lên firebase
                                // tạo khóa cho vị trí hàng
                                if(!(isNetworkAvailable(getApplicationContext())))
                                {
                                    String timeUseKeyID = String.valueOf(System.currentTimeMillis());
                                    tb_Location.getInstance(getApplicationContext()).addLocation(timeUseKeyID,objAccount.getCurrentUser().getUid(),fb_location);
                                }
                                else {
                                    updateListLocation(fb_location);
                                }

                                editor.putString(keyUtils.LATITUDEOLD,locationNew.getLatitude()+"");
                                editor.putString(keyUtils.LONGTITUDEOLD,locationNew.getLongitude()+"");
                                editor.putString(keyUtils.TIMELOCATION,getCurrentDate());
                                editor.apply();
                            }
                        }
                    }
                }
                else
                {

                    // vì không có vị trí lần đầu tiên, vị trí này sẽ tự động được lấy trước
                    editor.putString(keyUtils.LATITUDEOLD,locationNew.getLatitude()+"");
                    editor.putString(keyUtils.LONGTITUDEOLD,locationNew.getLongitude()+"");
                    editor.putString(keyUtils.TIMELOCATION,getCurrentDate());
                    editor.apply();
                    fb_location.setLatitude(mLocation.getLatitude());
                    fb_location.setLongitude(mLocation.getLongitude());
                    fb_location.setTimeUpdate(mLocation.getTime());
                    updateCurrentLocation(fb_location);
                    // Sai vì đây là vị trí đẩy lên khi có internet chứ không phải từ SQLite.
                    updateListLocation(fb_location);
                }

                // mRadius để kiểm tra xem bạn phải đo bao nhiêu vùng an toàn này
                int mRadius = 20;

                if(tb_CurrentFamilyID.getInstance(getApplicationContext()).getCurrentFamilyID()!= null)
                {
                    //locationSafe = new Location("");
                    if(objAccount.getAccountFromSQLite(getApplicationContext(),getCurrentUser().getUid())!=null)
                    {
                        AccountName = objAccount.getAccountFromSQLite(getApplicationContext(),getCurrentUser().getUid());
                        nameAccount = AccountName.getName();
                    }
                    else {
                        mFirebaseDatabase = mFirebaseInstance.getReference(accountList).child(getCurrentUser().getUid());
                        mFirebaseDatabase.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                nameAccount = dataSnapshot.getValue(String.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    mFirebaseDatabase = mFirebaseInstance.getReference(Safe_Area_Notification);

                    // Retrieves all area lists of UID
                    objFamilyList = tb_Family.getInstance(getApplicationContext()).getAllFamilyByUid(getCurrentUser().getUid());
                    for (objFamily objFamilyID : objFamilyList)
                    {
                        objAreaListTamp = tb_Area.getInstance(getApplicationContext()).getAreaListByIDFamily(objFamilyID.getId());
                        for (objArea objAreaID : objAreaListTamp)
                        {
                            objArea objArea = new objArea();
                            objArea.setIdFamily(objAreaID.getIdFamily());
                            objArea.setLatitude(objAreaID.getLatitude());
                            objArea.setRegionName(objAreaID.getRegionName());
                            objArea.setLongitude(objAreaID.getLongitude());
                            objArea.setRadius(objAreaID.getRadius());
                            objAreaList.add(objArea);
                        }
                    }


                    /**
                     * There is currently no location list from the server, so I got it from the RemoteAccessSyntax class
                     */

                    if(objAreaList.size()>0)
                    {
                        // safeArea = false by default
                        // Khi so sánh vị trí với tất cả các vị trí trong đình, nếu vị trí nào trong đình được đặt,
                        // then set safeArea = true
                        if(!safeArea)
                        {
                            for (objArea objArea: objAreaList)
                            {

                                Location locationOld = new Location("");
                                locationOld.setLatitude(objArea.getLatitude());
                                locationOld.setLongitude(objArea.getLongitude());
                                mRadius = objArea.getRadius();
                                float distanceInMeters = locationOld.distanceTo(locationNew);

                                //So sánh vị trí hiện tại với tất cả vị trí của gia đình.
                                if(distanceInMeters < mRadius)
                                {
                                    // When the current position has entered the safe area
                                    String distanceIn = String.valueOf(distanceInMeters);
                                    safeArea = true;
                                    //editor.putBoolean(keyUtils.SAFE_AREA,true);
                                    editor.putString(keyUtils.IDFAMILY,objArea.getIdFamily());
                                    editor.putString(keyUtils.LATITUDE,locationNew.getLatitude()+"");
                                    editor.putString(keyUtils.LONGTITUDE,locationNew.getLongitude()+"");
                                    editor.apply();
                                    nameArea = objArea.getRegionName();
                                    editor.putString(keyUtils.NAME_AREA,nameArea);
                                    editor.putInt(keyUtils.RADIUS_AREA,objArea.getRadius());
                                    editor.apply();
                                    fb_Safe_Area_Notification fb_safe_area_notification = new fb_Safe_Area_Notification();
                                    fb_safe_area_notification.setName_area(objArea.getRegionName());
                                    fb_safe_area_notification.setName_device(nameAccount);
                                    fb_safe_area_notification.setStatus("vào");
                                    mFirebaseDatabase.child(objArea.getIdFamily().replace("/familyList/","")).child(getCurrentUser().getUid()).setValue(fb_safe_area_notification);
                                    break;
                                }
                            }
                        }else
                        {
                            // safeArea = true Khi vào trong khu vực an toàn, chúng tôi sẽ kiểm tra mỗi khi có vị trí mới
                            // để xem liệu họ có di chuyển ra khỏi khu vực an toàn này hay không.
                            locationSafe = new Location("");
                            locationSafe.setLatitude(Double.parseDouble(sharedPreferences.getString(keyUtils.LATITUDE,"0.0")));
                            locationSafe.setLongitude(Double.parseDouble(sharedPreferences.getString(keyUtils.LONGTITUDE,"0.0")));
                            Log.d("checkSafe", locationSafe.getLatitude()+" Longtitude = "+ locationSafe.getLongitude());

                            if(locationSafe.getLongitude()!= 0.0)
                            {

                                if(locationSafe.distanceTo(locationNew) > sharedPreferences.getInt(keyUtils.RADIUS_AREA,20))
                                {
                                    fb_Safe_Area_Notification fb_safe_area_notification = new fb_Safe_Area_Notification();
                                    fb_safe_area_notification.setName_area(sharedPreferences.getString(keyUtils.NAME_AREA,"Unknown"));
                                    fb_safe_area_notification.setName_device(nameAccount);

                                    fb_safe_area_notification.setStatus("ra");
                                    safeArea = false;
                                    /*editor.putBoolean(keyUtils.SAFE_AREA,false);
                                    editor.apply();*/
                                    mFirebaseDatabase.child(sharedPreferences.getString(IDFAMILY,listFamilyID.get(0)).replace("/familyList/","")).child(getCurrentUser().getUid()).setValue(fb_safe_area_notification);

                                }
                            }
                        }
                    }
                }

            }catch (Exception e)
            {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    /**
     * this is method use update current location in the device
     * @param fb_location
     */
    private void updateCurrentLocation(fb_Location fb_location)
    {
        mFirebaseDatabase = mFirebaseInstance.getReference(accountList).child(getCurrentUser().getUid());
        mFirebaseDatabase.child(location).setValue(fb_location);
    }

    /**
     * this is method use update list location for a week in the device
     * @param fb_location
     */
    private void updateListLocation(fb_Location fb_location) {

        mFirebaseDatabase = mFirebaseInstance.getReference(history).child(getCurrentUser().getUid()).child(getYearAndWeek()).child(getCurrentDate()).child(recentLocations);
        String keyInRecentLocations = mFirebaseDatabase.push().getKey();
        assert keyInRecentLocations != null;
        mFirebaseDatabase.child(keyInRecentLocations).setValue(fb_location);

    }
    /**
     * this is method use update list location for a week in the device
     * @param objLocation
     */
    private void updateListLocationFromSQLite(objLocation objLocation) {
        // updating the user via child nodes
        // UID/01012020/11022020
        mFirebaseDatabase = mFirebaseInstance.getReference(history).child(getCurrentUser().getUid()).child(getYearAndWeek()).child(getCurrentDate()).child(recentLocations);
        String keyInRecentLocations = mFirebaseDatabase.push().getKey();
        assert keyInRecentLocations != null;
        mFirebaseDatabase.child(keyInRecentLocations).setValue(objLocation.getFb_location());

        tb_Location.getInstance(getApplicationContext()).deleteLocationByKeyID(objLocation.getKeyID());
    }

    /**
     * getLocation is the method to be called every time the user update location changes from the server
     */
    private void getLocation(Long interval, Long fastestInterval) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST);
        }
        else
        {

            locationRequest = LocationRequest.create();
            // Used with setPriority(int) to request "block" level accuracy.
            // convert PRIORITY_HIGH_ACCURACY to PRIORITY_BALANCED_POWER_ACCURACY
            // to not display the GPS icon in the phone's messageNotification bar

            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d(TAG,"interval = "+interval+"fastTestInterval = "+fastestInterval);
            // Set the desired interval for active location updates, in milliseconds.
            locationRequest.setSmallestDisplacement(20);
            locationRequest.setInterval(interval); // 10 seconds
            // Explicitly set the fastest interval for location updates, in milliseconds.
            locationRequest.setFastestInterval(interval);

            // Sets the maximum wait time in milliseconds for location updates.
            //locationRequest.setMaxWaitTime(interval*2);
            // Set the minimum displacement between location updates in meters
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null)
                        {
                            mLastLocation = location;
                            displayLocation();
                        }
                    }
                }
            };
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    /**
     * getNotificationOfArea(): this is the method handle whenever there is a change from the "Safe_Area_Notification" button
     * to create a notification to let users know who in the home has moved away or into a safe area
     * DEV: Lucas walker
     */
    public void getNotificationOfArea()
    {

        listFamilyID = new ArrayList<>();
        mFirebaseDatabase = mFirebaseInstance.getReference(IDFamilyList).child(getCurrentUser().getUid());
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                areaCount = 1;
                checkCountArea = 0;
                for (DataSnapshot dataSnapshotFamilyID : dataSnapshot.getChildren())
                {
                    //Toast.makeText(GPSService.this, "getNotificationOfArea", Toast.LENGTH_SHORT).show();
                    String familyID = dataSnapshotFamilyID.getValue(String.class);
                    listFamilyID.add(familyID);
                    // Example: /familyList/3WJ0NJTeOYcAodxGQWZtVrfgL952/aaiaiweq
                }
                if(listFamilyID != null)
                {
                    for (String familyID : listFamilyID)
                    {
                        getListSafeAreaNotificationByFamilyID(familyID.substring(12));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Add FamilyID Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Nhận danh sách các khu vực an toàn mà bạn đã tạo bởi FamilyID
     * @param familyID id of family
     */
    public void getListSafeAreaNotificationByFamilyID(String familyID)
    {
        mFirebaseDatabase = mFirebaseInstance.getReference(Safe_Area_Notification).child(familyID);
        // Listen once so it doesn't get updated every time the location changes in an area
        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                checkCountArea =  checkCountArea+ (int)dataSnapshot.getChildrenCount();
                for (DataSnapshot dataSnapshotUIDByFamilyID : dataSnapshot.getChildren())
                {
                    //Toast.makeText(GPSService.this, "getListSafeAreaNotificationByFamilyID", Toast.LENGTH_SHORT).show();
                    getNotificationOfUID(dataSnapshotUIDByFamilyID.getKey(),familyID);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Thông báo cho bạn khi bạn đã vào khu vực an toàn hoặc ra khỏi khu vực an toàn
     * it will always run to listen for changes
     * @param Key key
     * @param familyID id of family
     */
    public void getNotificationOfUID(String Key, String familyID)
    {

        mFirebaseDatabase = mFirebaseInstance.getReference(Safe_Area_Notification).child(familyID).child(Key);
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                Log.d("CheckPush","checkCountArea= "+checkCountArea+ "areaCount = " +areaCount);
                if(areaCount <= checkCountArea)
                {
                    areaCount++;
                }
                else
                {
                    checkPushnotification = true;
                }
                if(checkPushnotification)
                {
                    AccountName = objAccount.getAccountFromSQLite(getApplicationContext(),Key);
                    // Thông báo mỗi khi một nút khu vực được thay đổi có nghĩa là nó đi vào hoặc thoát khỏi vị trí hiện tại
                    fb_Safe_Area_Notification area_notification = dataSnapshot.getValue(fb_Safe_Area_Notification.class);
                    if(!(Objects.requireNonNull(dataSnapshot.getKey()).equals(getCurrentUser().getUid())))
                    {

                        Log.d("notification","Notification "+AccountName.getName()+" Bạn đã  "+ area_notification.getStatus()+ " vào trong địa điểm "+ area_notification.getName_area());
                        //Returns current time in millis
                        notificationUtils.buildNotificationGPS(getApplicationContext(),(int)System.currentTimeMillis(),"Xe di chuyển",AccountName.getName()+" đã "+ area_notification.getStatus() + " địa điểm " + area_notification.getName_area());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG,"onTaskRemoved");
        Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);
        //mAlarm.startAlarm(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}
