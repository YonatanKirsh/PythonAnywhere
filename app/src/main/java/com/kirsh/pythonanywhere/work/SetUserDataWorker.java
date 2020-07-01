package com.kirsh.pythonanywhere.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.kirsh.pythonanywhere.Shared;
import com.kirsh.pythonanywhere.server.Communicator;
import com.kirsh.pythonanywhere.server.PythonAnywhereService;
import com.kirsh.pythonanywhere.server.SetUserImageUrlRequest;
import com.kirsh.pythonanywhere.server.SetUserPrettyNameRequest;
import com.kirsh.pythonanywhere.server.UserResponse;

import retrofit2.Call;
import retrofit2.Response;

public class SetUserDataWorker extends Worker {

    public SetUserDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // get input
        String token = getInputData().getString(Shared.TOKEN_TAG);
        String prettyInput = getInputData().getString(Shared.PRETTY_NAME_TAG);
        String urlInput = getInputData().getString(Shared.IMAGE_URL_TAG);
        // set prettyName and url
        Result prettyResult = doPrettyWork(token, prettyInput);
        Result urlResult = doUrlWork(token, urlInput);
        // check for failure
        if (prettyResult.equals(Result.failure()) || urlResult.equals(Result.failure())){
            return Result.failure();
        }
        return Result.success();
    }

    private Result doPrettyWork(String token, String prettyName){
        PythonAnywhereService server = Communicator.getInstance().service;
        Call<UserResponse> call = server.setPrettyName(token, new SetUserPrettyNameRequest(prettyName));
        try {
            Response<UserResponse> response = call.execute();
            if (response.isSuccessful()){
                UserResponse body = response.body();
                if (body != null){
                    return Result.success();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Result.failure();
    }

    private Result doUrlWork(String token, String imageUrl){
        PythonAnywhereService server = Communicator.getInstance().service;
        Call<UserResponse> call = server.setImageUrl(token, new SetUserImageUrlRequest(imageUrl));
        try {
            Response<UserResponse> response = call.execute();
            if (response.isSuccessful()){
                UserResponse body = response.body();
                if (body != null){
                    return Result.success();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Result.failure();
    }
}
