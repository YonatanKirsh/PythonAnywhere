package com.kirsh.pythonanywhere.server;

import androidx.annotation.Nullable;

public class SetUserPrettyNameRequest {

    public SetUserPrettyNameRequest(String input){
        prettyName = input;
    }

    @Nullable
    public String prettyName;
}
