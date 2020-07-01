package com.kirsh.pythonanywhere.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.kirsh.pythonanywhere.Shared;
import com.kirsh.pythonanywhere.server.Communicator;
import com.kirsh.pythonanywhere.server.PythonAnywhereService;
import com.kirsh.pythonanywhere.server.TokenResponse;

import retrofit2.Call;
import retrofit2.Response;

public class SetNewUserWorker extends Worker {

    public SetNewUserWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        return null;
    }

}
