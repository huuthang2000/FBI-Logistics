package com.example.demoapp.view.driver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
        Intent intent = getIntent();
        String to = "";
        String from =intent.getStringExtra("location1");
        map = findViewById(R.id.webview);
        map.setWebViewClient(new WebViewClient());
        map.getSettings().setJavaScriptEnabled(true);
        map.loadUrl("http://www.google.co.in/maps/dir/"+from+"/"+to+"?hl=vi");



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