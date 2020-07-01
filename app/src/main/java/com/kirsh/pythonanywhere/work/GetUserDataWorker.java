package com.kirsh.pythonanywhere.work;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.kirsh.pythonanywhere.Shared;
import com.kirsh.pythonanywhere.server.Communicator;
import com.kirsh.pythonanywhere.server.PythonAnywhereService;
import com.kirsh.pythonanywhere.server.User;
import com.kirsh.pythonanywhere.server.UserResponse;

import retrofit2.Call;
import retrofit2.Response;

public class GetUserDataWorker extends Worker {


    public GetUserDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        PythonAnywhereService server = Communicator.getInstance().service;
        String token = getInputData().getString(Shared.TOKEN_TAG);
        if (token == null){
            return Result.failure();
        }
        Call<UserResponse> call = server.getUser(token);
        try {
            Response<UserResponse> response = call.execute();
            if (response.isSuccessful()){
                UserResponse body = response.body();
                if (body != null){
                    User user = body.data;
                    Data data = getUserAsData(user);
                    return Result.success(data);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Result.failure();
    }

    private Data getUserAsData(User user){
        return new Data.Builder()
                .putString(Shared.USERNAME_TAG, user.username)
                .putString(Shared.PRETTY_NAME_TAG, user.prettyName)
                .putString(Shared.IMAGE_URL_TAG, user.imageUrl)
                .build();
    }

    public static User getUserFromData(Data data){
        String username = data.getString(Shared.USERNAME_TAG);
        String prettyName = data.getString(Shared.PRETTY_NAME_TAG);
        String imageUrl = data.getString(Shared.IMAGE_URL_TAG);
        return new User(username, prettyName, imageUrl);
    }


    private void updateUserInSP(User user){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String userAsString = gson.toJson(user);
        editor.putString(Shared.USER_TAG, userAsString);
        editor.apply();
    }
}
