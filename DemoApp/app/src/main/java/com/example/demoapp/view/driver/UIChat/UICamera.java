package com.example.demoapp.view.driver.UIChat;

import static com.example.demoapp.Utils.keyUtils.REQUEST_CAMERA;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.demoapp.BuildConfig;
import com.example.demoapp.R;
import com.example.demoapp.Utils.viewUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;


public class UICamera extends AppCompatActivity {
    public static final String TAG = "UICamera";

    private static final String AUTHORITY=
            BuildConfig.APPLICATION_ID+".provider";

    private File file = null;

    private final Context context = this;

    private Bitmap bmImage;

    private Button btnClose;
    private Button btnDone;
    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        viewUtils.setColorStatusBar((AppCompatActivity) context,R.color.black);

        initView();

        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = new File(Environment.getExternalStorageDirectory(), Calendar.getInstance().getTimeInMillis() + ".jpg");
            Uri photoPath = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                    AUTHORITY, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);   //--> here
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), REQUEST_CAMERA);


        }catch (Exception e){
            Log.e("CheckApp", e.toString());
            Toast.makeText(context, R.string.Error_taking_photos, Toast.LENGTH_SHORT).show();
            finish();
        }

        setActionToView();


    }

    private void setActionToView() {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.example.demoapp.view.driver.UIChat.UIChat.bmImageCamera = bmImage;
                com.example.demoapp.view.driver.UIChat.UIChat.previousActivity = TAG;
                finish();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        btnClose = findViewById(R.id.btnClose);
        btnDone = findViewById(R.id.btnDone);
        photoView = findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK)
        {
            switch (requestCode){
                case REQUEST_CAMERA:
                    Uri outputUri=FileProvider.getUriForFile(this, AUTHORITY, file);
                    try {
                        bmImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputUri);
                        photoView.setImageBitmap(bmImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, R.string.Error_taking_photos, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    break;
            }
        }else{
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
