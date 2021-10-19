package com.example.app1.ui.dashboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.app1.DB.DatabaseHelper;
import com.example.app1.MainActivity;
import com.example.app1.databinding.FragmentDashboardBinding;
import com.example.app1.dbConnect;

import java.util.HashMap;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    protected View mView;
    protected Context mContext;
    Button deltab,newtab;
    private DatabaseHelper dbHelper;

    private static final String TAG = "DashboardFragment";
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            binding.showUser.setText((String)message.obj);
            String str = "查询不存在";
            if(message.what==1) str = "查询成功";
            Toast.makeText(mContext,str,Toast.LENGTH_SHORT).show();
            return false;
        }
    });


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mContext = getActivity();
        deltab=binding.delTab;
        newtab=binding.newTab;
        dbHelper = new DatabaseHelper(getContext(),"UserInfo.dp",null,2);

        deltab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String sq ="drop table if exists UserInfo";
                db.execSQL(sq);
            }
        });
        newtab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String CREATE_USERINFO="Create table UserInfo("
                        +"id integer primary key autoincrement,"
                        +"username text not null,"
                        +"userimg text ,"
                        +"phone text not null,"
                        +"password text not null)";
                db.execSQL(CREATE_USERINFO);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                TextView tv_showUser = binding.showUser;
                HashMap<String,String> mp = dbConnect.getUserInfoByPhone("13650713642");
                Message msg = new Message();
                if(mp == null){
                    msg.what = 0;
                    msg.obj = "查询结果空空如也";
                }else{
                    String ss=new String();
                    for (String key : mp.keySet()){
                        ss = ss + key +":"+ mp.get(key)+";";
                    }
                    msg.what = 1;
                    msg.obj = ss;
                }
                handler.sendMessage(msg);
            }
        }).start();


        return root;
    }

//    @Override
//    public void onDestroyView() {
//
//        super.onDestroyView();
//        binding = null;
//    }
}