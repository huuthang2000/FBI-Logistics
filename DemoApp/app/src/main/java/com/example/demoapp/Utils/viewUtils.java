/*
 *  Date created: 12/04/2019
 *  Last updated: 12/02/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */

package com.example.demoapp.Utils;

import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.demoapp.R;

import java.util.Objects;

public class viewUtils {


    /**
     * Set transparent status bar
     * @param context context (AppCompatActivity)
     */
    public static void setTransparentStatusBar(AppCompatActivity context){
        // Making messageNotification bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void removeTransparentStatusBar(AppCompatActivity context){
        // Making messageNotification bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * Set color status bar
     * @param context context
     * @param color color
     */
    public static void setColorStatusBar(AppCompatActivity context, int color){
        context.getWindow().setStatusBarColor(context.getResources().getColor(color));
    }

    /**
     * Set up toolbar
     * @param context context (AppCompatActivity)
     * @param colorButtonBack color of button back
     * @param title title of toolbar
     * @param colorStatusBar color of status bar
     */
    public static void setupToolbar(final Context context, int idToolbar, int colorButtonBack, String title, int colorStatusBar){
        if(colorStatusBar != -1)
            setColorStatusBar((AppCompatActivity)context,colorStatusBar);
        else
            setTransparentStatusBar((AppCompatActivity) context);

        Toolbar toolbar = ((AppCompatActivity)context).findViewById(idToolbar);
        ((AppCompatActivity)context).setSupportActionBar(toolbar);

        Objects.requireNonNull(((AppCompatActivity)context).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setTint(context.getResources().getColor(colorButtonBack));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity) context).finish();
            }
        });

        if(title!=null)
            toolbar.setTitle(title);
    }


    /**
     *
     * @param context context
     * @param content content text
     */
    public static void copiedToClipboard(Context context, String content){
        ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setText(content);
        }
        Toast.makeText(context, context.getResources().getString(R.string.Copied_to_Clipboard), Toast.LENGTH_SHORT).show();
    }

    public static int getStatusBarHeight(AppCompatActivity context){
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     *
     * @param context context
     * @param content content
     */
    public static void shareWith(Context context, String content){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my family's invitation code: ");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(sharingIntent, "Share with"));
    }

    public static void rateApp(Context context){
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
}
