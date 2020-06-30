package com.kirsh.pythonanywhere.server;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Communicator {

    // url for real server
    public final static String BASE_URL = "https://hujipostpc2019.pythonanywhere.com/";
    // url for local debugging server
//    private final static String BASE_URL = "http://192.168.5.2:5678/"; //todo delete me

    private OkHttpClient okhClient;
    private Retrofit retrofit;
    public PythonAnywhereService service;
    private static Communicator communicator = null;

    private Communicator(){
        okhClient = new OkHttpClient.Builder().build();
        retrofit = new Retrofit.Builder()
                .client(okhClient)
                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PythonAnywhereService.class);
        communicator = this;
    }

    public static Communicator getInstance(){
        if (communicator == null){
            communicator = new Communicator();
        }
        return communicator;
    }

}
