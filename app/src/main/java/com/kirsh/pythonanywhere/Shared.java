package com.kirsh.pythonanywhere;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.app.ActivityCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

public class Shared {
    public final static String TOKEN_TAG = "token";
    public final static String USER_TAG = "user";
    public final static String USERNAME_TAG = "username";
    public final static String PRETTY_NAME_TAG = "pretty_name";
    public final static String IMAGE_URL_TAG = "image_url";
    public final static String COMMAND_TAG = "command";

    private final static int REQUEST_CODE_PERMISSION_INTERNET = 1546;

    final static Constraints CONSTRAINTS = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();

    public static void showLoadingUI(View view){
        ProgressBar progressBar = view.findViewById(R.id.spin_kit);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

    }

    public static void removeLoadingUI(){
        // todo remove loading ui
    }

    private static boolean hasInternetPermission(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean givenInternetPermission(Context context){
        // ask for internet permission ONCE
        if (hasInternetPermission(context)){
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    new String[]{Manifest.permission.INTERNET},
                    REQUEST_CODE_PERMISSION_INTERNET);
        }
        return hasInternetPermission(context);
    }
}
