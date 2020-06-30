package com.kirsh.pythonanywhere.server;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SetUsernameRequest extends Worker {

    @Nullable
    public String imageUrl;

    public SetUsernameRequest(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        //
        return null;
    }
}
