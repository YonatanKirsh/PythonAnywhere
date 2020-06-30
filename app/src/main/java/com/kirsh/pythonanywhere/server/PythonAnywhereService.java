package com.kirsh.pythonanywhere.server;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PythonAnywhereService {
    @GET("users/{user}/token")
    Call<TokenResponse> getToken(@Path("user") String user);

    @GET("user")
    Call<UserResponse> getUser(@Header("Authorization: token") String token);

    @Headers({"Content-Type: application/json"})
    @POST("user/edit/")
    Call<UserResponse> setPrettyName(@Header("Authorization: token") String token, @Body SetUserPrettyNameRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("user/edit/")
    Call<UserResponse> setImageUrl(@Header("Authorization: token") String token, @Body SetUserImageUrlRequest request);
}
