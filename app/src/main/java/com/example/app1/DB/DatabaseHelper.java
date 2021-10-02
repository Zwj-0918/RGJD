package com.example.app1.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public  static final String CREATE_USERINFO="Create table UserInfo("
            +"id integer primary key autoincrement,"
            +"username text not null,"
            +"userimg text ,"
            +"phone text not null,"
            +"password text not null)";

    private  Context mContext;
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_USERINFO);


        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

//    对数据库进行升级，很重要，不能忽视！
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        将存在的表删除
        sqLiteDatabase.execSQL("drop table if exists UserInfo");
//        再调用onCreate方法创建新的表格
        onCreate(sqLiteDatabase);
    }
}
