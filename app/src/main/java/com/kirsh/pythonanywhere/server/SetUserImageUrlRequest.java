package com.kirsh.pythonanywhere.server;

import androidx.annotation.Nullable;

public class SetUserImageUrlRequest {

    public SetUserImageUrlRequest(String input){
        imageUrl = input;
    }

    @Nullable
    public String imageUrl;
}
