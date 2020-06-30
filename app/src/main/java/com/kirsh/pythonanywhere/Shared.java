package com.kirsh.pythonanywhere;

import androidx.work.Constraints;
import androidx.work.NetworkType;

public class Shared {
    public final static String TOKEN_TAG = "token";
    public final static String USER_TAG = "user";
    public final static String USERNAME_TAG = "username";
    public final static String PRETTY_NAME_TAG = "pretty_name";
    public final static String IMAGE_URL_TAG = "image_url";

    final static Constraints CONSTRAINTS = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();

    public static void showLoadingUI(){
        // todo show loading ui
    }

    public static void removeLoadingUI(){
        // todo remove loading ui
    }
}
