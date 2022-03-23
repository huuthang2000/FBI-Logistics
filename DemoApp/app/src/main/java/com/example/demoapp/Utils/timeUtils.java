
package com.example.demoapp.Utils;

import static com.example.demoapp.Utils.keyUtils.DEFAULT_DATETIME_FORMAT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;



public class timeUtils {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    /**
     * function convert millisecond to string time ago
     * @param time millisecond
     * @return String time ago. ex: 3 a minutes ago
     */
    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = Calendar.getInstance().getTimeInMillis();

        if (time <= 0) {
            return "";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    /**
     * function convert millisecond to string time
     * @param time millisecond
     * @return String time ago. ex: 3 a minutes
     */
    public static String getTimeNoAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = Calendar.getInstance().getTimeInMillis();

        if (time > now || time <= 0) {
            return "";
        }


        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days";
        }
    }


    /**
     * this is method convert date time to date
     * @param time milliseconds
     * @return milliseconds type date (dd/MM/yyyy)
     */
    public static long convertDateTimeToDate(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        try {
            calendar.setTime(Objects.requireNonNull(com.example.demoapp.Utils.patternFormatDateTime.dd_MM_yyyy.parse(com.example.demoapp.Utils.patternFormatDateTime.dd_MM_yyyy.format(calendar.getTime()))));
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * this is method get current week
     * @param time milliseconds
     * @return week in time
     */
    public static int getTheCurrentWeek(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(convertDateTimeToDate(time));
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * this is method get list time zone
     * @return list string time zone
     */
    @SuppressLint("DefaultLocale")
    public static ArrayList<String> getListTimeZone(){
        ArrayList<String> listTimeZone = new ArrayList<>();

        String[] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            TimeZone d = TimeZone.getTimeZone(id);

            if (!id.matches(".*/.*")) {
                continue;
            }

            String region = id.replaceAll(".*/", "").replaceAll("_", " ");
            int hours = Math.abs(d.getRawOffset()) / 3600000;
            int minutes = Math.abs(d.getRawOffset() / 60000) % 60;
            String sign = d.getRawOffset() >= 0 ? "+" : "-";

            listTimeZone.add(String.format("(UTC %s %02d:%02d) %s", sign, hours, minutes, region));

        }

        return listTimeZone;
    }

    /**
     * this is method get time day no ago
     * @param time milliseconds
     * @return string time ex: Today, Yesterday
     */
    public static String getTimeDayNoAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = Calendar.getInstance().getTimeInMillis();
        if (time > now || time <= 0) {
            return "";
        }
        final long diff = now - time;
        if (diff < 24 * HOUR_MILLIS) {
            return "Today";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Yesterday";
        } else {
            return diff / DAY_MILLIS + " days";
        }
    }


    /**
     * this is method convert Millisecond To String Format
     * @param time milliseconds time
     * @param dateFormat date formatter
     * @return Date strings formatted according to SimpleDateFormat
     */
    public static String convertMillisecondToStringFormat(long time, SimpleDateFormat dateFormat){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat.format(calendar.getTime());
    }


    /**
     * getAddress is method convert gps to address in android
     * use for method setOnCameraMoveStartedListener()
     * @param latitude latitude
     * @param longitude longitude
     * @return Address
     */
    public static String getAddress(Double latitude, Double longitude, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String addressName = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() != 0 ) {
                String address = addresses.get(0).getAddressLine(0);
                String area = addresses.get(0).getLocality();
                String city = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();

                addressName = address ;
                //addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName()
                //addressName = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
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

    /**
     * isNetworkAvailable is method check internet connect.
     */
    public static boolean isNetworkAvailable(Context mContext) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            //return activeNetworkInfo != null;

        }
        catch (Exception e)
        {
            Log.d("error", e.getMessage()+"");
        }
        return false;
    }

    /**
     * isNetworkAvailable is method check internet connect.
     */
    public static String getWeekInYear() {
        String weekYear ="";
        try {
            Calendar calender = Calendar.getInstance();
            weekYear = (calender.get(Calendar.WEEK_OF_YEAR)<10 ? "0" + calender.get(Calendar.WEEK_OF_YEAR) : calender.get(Calendar.WEEK_OF_YEAR)) + ""+calender.get((Calendar.YEAR));
            //weekYear = calender.get(Calendar.)
            //return activeNetworkInfo != null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

            //You can change "yyyyMMdd_HHmmss as per your requirement

            String currentDateandTime = sdf.format(new Date());

        }
        catch (Exception e)
        {
            Log.d("error", e.getMessage()+"");
        }
        return weekYear;
    }

    /**
     * isNetworkAvailable is method check internet connect.
     */
    public static String getYearAndWeek() {
        String weekYear ="";
        try {
            Calendar calender = Calendar.getInstance();
            weekYear = calender.get((Calendar.YEAR))+ "" + (calender.get(Calendar.WEEK_OF_YEAR)<10 ? "0" + calender.get(Calendar.WEEK_OF_YEAR) : calender.get(Calendar.WEEK_OF_YEAR)) ;
            //weekYear = calender.get(Calendar.)
            //return activeNetworkInfo != null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

            //You can change "yyyyMMdd_HHmmss as per your requirement

            String currentDateandTime = sdf.format(new Date());

        }
        catch (Exception e)
        {
            Log.d("error", e.getMessage()+"");
        }
        return weekYear;
    }
    /**
     * getCurrentDate is method get current date
     * return YYMMDD
     */
    public static String getCurrentDate() {
        String currentDateandTime ="";
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());

            //You can change "yyyyMMdd_HHmmss as per your requirement

            currentDateandTime = sdf.format(new Date());

        }
        catch (Exception e)
        {
            Log.d("error", e.getMessage()+"");
        }
        return currentDateandTime;
    }

    /**
     * getCurrentDate is method get current date
     * return YYMMDD
     */
    public static String formatCurrentWeek(String dated) {
        String currentDateandTime ="";
        try {

            Date d1 = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT).parse(dated);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyww", Locale.getDefault());

            //You can change "yyyyMMdd_HHmmss as per your requirement

            currentDateandTime = sdf.format(d1);

        }
        catch (Exception e)
        {
            Log.d("error", e.getMessage()+"");
        }
        return currentDateandTime;
    }
    /**
     * getCurrentDate is method get current date
     * return YYMMDD
     */
    public static String formatCurrentDate(String dated) {
        String currentDateandTime ="";
        try {

            Date d1 = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT).parse(dated);
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

            //You can change "yyyyMMdd_HHmmss as per your requirement

            currentDateandTime = sdf.format(d1);

        }
        catch (Exception e)
        {
            Log.d("error", e.getMessage()+"");
        }
        return currentDateandTime;
    }
}
