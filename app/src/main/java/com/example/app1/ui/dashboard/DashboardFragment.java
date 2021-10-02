package com.example.app1.ui.dashboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.app1.DB.DatabaseHelper;
import com.example.app1.databinding.FragmentDashboardBinding;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    protected View mView;
    protected Context mContext;
    Button deltab,newtab;
    private DatabaseHelper dbHelper;


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


        return root;
    }

//    @Override
//    public void onDestroyView() {
//
//        super.onDestroyView();
//        binding = null;
//    }
}