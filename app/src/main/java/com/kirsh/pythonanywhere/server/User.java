package com.kirsh.pythonanywhere.server;

public class User {

    public User(){}

    public User(String username, String prettyName, String imageUrl){
        this.username = username;
        this.prettyName = prettyName;
        this.imageUrl = imageUrl;
    }

    public String username;
    public String prettyName;
    public String imageUrl;

//    public String getUsername(){
//        return username;
//    }
//
//    public String getPrettyName(){
//        return prettyName;
//    }
//
//    public String getImageUrl(){
//        return imageUrl;
//    }
}
