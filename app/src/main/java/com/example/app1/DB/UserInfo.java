package com.example.app1.DB;

import android.content.ContentValues;

public class UserInfo {
    String phone;
    String password;
    String username;
//    String userheadimg;
//    String registerTime;
    public UserInfo(){

    }
    public UserInfo(String phone, String password, String username){
        this.phone=phone;
        this.password=password;
        this.username=username;
    }
    public String getPhone(){
        return phone;
    }
    public String getPassword(){
        return password;
    }
    public String getUsername(){
        return username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
