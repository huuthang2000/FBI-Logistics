package com.example.demoapp.view.driver;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.Models.objectFirebase.family.fb_area;
import com.example.demoapp.R;

import java.util.List;

public class UIDirect extends AppCompatActivity {
    private List<fb_area> areaListFB;
    WebView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uidirect);
        try {
            Intent intent = getIntent();
            String to = "";
            String from = intent.getStringExtra("location1");
           Uri uri = Uri.parse("http://www.google.co.in/maps/dir/" + from + "/" + to + "?hl=vi");
            map = findViewById(R.id.webview);
            map.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            map.getSettings().setBuiltInZoomControls(true);
            map.setWebViewClient(new WebViewClient());
            // Below required for geolocation
            map.getSettings().setJavaScriptEnabled(true);
            map.getSettings().setGeolocationEnabled(true);
            map.loadUrl("http://www.google.co.in/maps/dir/" + from + "/" + to + "?hl=vi");
            
            intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }catch (ActivityNotFoundException e ){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }



    @Override
    public void onBackPressed() {
        if (map.canGoBack()) {
            map.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}