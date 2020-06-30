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

public class GetTokenWorker extends Worker {

    public GetTokenWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        PythonAnywhereService server = Communicator.getInstance().service;
        String username = getInputData().getString(Shared.USERNAME_TAG);
        Call<TokenResponse> call = server.getToken(username);
        try {
            Response<TokenResponse> response = call.execute();
            if (response.isSuccessful()){
                TokenResponse body = response.body();
                if (body != null){
                    String token = body.data;
                    Data data = new Data.Builder()
                            .putString(Shared.TOKEN_TAG, token)
                            .build();
                    return Result.success(data);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Result.failure();
    }


    //todo remove
//    static String getExistingToken(Context context){
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        return sp.getString(Constants.TOKEN_TAG, "");
//    }
}
