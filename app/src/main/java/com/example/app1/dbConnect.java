package com.example.app1;

import android.util.Log;
import android.widget.Toast;

import com.example.app1.DB.UserInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
//192.168.43.80
//?useSSL=false&serverTimezone=GMT
public class dbConnect {
    private final static String diver = "com.mysql.jdbc.Driver";
    private final static String url = "jdbc:mysql://192.168.43.80:3306/rgjd?useSSL=false&useUnicode=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final static String username = "RGJD";
    private final static String password = "rgjd2021";

    private static final String TAG = "dbConnect";

    private static Connection getConnect() {
        Connection mconnection = null;
        try {
            Class.forName(diver);
            mconnection = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return mconnection;
    }

    public static HashMap<String, String> getUserInfoByPhone(String phone) {
        HashMap<String, String> map = new HashMap<>();
        Connection conn = getConnect();
        try {
            Statement st = conn.createStatement();
            String sql = "select * from userinfo where phone = " + phone;
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                int cnt = res.getMetaData().getColumnCount();
                while(res.next()) {
                    for (int i = 1; i <= cnt; i++) {
                        String field = res.getMetaData().getColumnName(i);
                        map.put(field, res.getString(field));
                    }
                }
                conn.close();
                st.close();
                res.close();
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "数据操作异常");
            return null;
        }
    }

    public static Boolean addUserInfo(UserInfo userInfo) {
        Connection conn = getConnect();
        Log.d(TAG,"conn:"+conn);
        try {
            Statement st = conn.createStatement();
            String sql = "insert into userInfo (phone,password,username) values('" + userInfo.getPhone() + "','" + userInfo.getPassword() + "','" + userInfo.getUsername() + "');";
            int res = st.executeUpdate(sql);
            Log.d(TAG,"res:"+res);
            if (res == 1) {
                return true;
            }
            conn.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
