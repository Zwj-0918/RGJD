package com.example.app1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class UserInfo {
    private DatabaseHelper dbHelper;


//    增
    public void addInfo(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
    }
//    删
    public void delInfo(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("UserInfo","username=?",new String[]{});
    }
//    改
    public void changeInfo(){

    }
//    查
    public void searchInfo(){

    }
}
