package com.example.app1;

import static android.widget.Toast.LENGTH_LONG;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.app1.ui.dashboard.DashboardFragment;
import com.example.app1.ui.home.HomeFragment;
import com.example.app1.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.app1.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationItemView mhome, mdash, mperson;
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";
    private FragmentManager fragmentManager;
    private androidx.fragment.app.Fragment currentFragment = new androidx.fragment.app.Fragment();
    private List<androidx.fragment.app.Fragment> fragmentList = new ArrayList<androidx.fragment.app.Fragment>();

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


//        Button pageone,pagetwo,pagethree;
//        pageone=findViewById(R.id.page1);
//        pagetwo=findViewById(R.id.page2);
//        pagethree=findViewById(R.id.page3);
//
//        pageone.setOnClickListener(this);
//        pagetwo.setOnClickListener(this);
//        pagethree.setOnClickListener(this);
//
//
//        fragmentManager = getSupportFragmentManager();
//
//        if (savedInstanceState != null) {
//            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);
//            fragmentList.removeAll(fragmentList);
//            fragmentList.add(fragmentManager.findFragmentByTag(0 + ""));
//            fragmentList.add(fragmentManager.findFragmentByTag(1 + ""));
//            fragmentList.add(fragmentManager.findFragmentByTag(2 + ""));
//            restoreFragment();
//        } else {
//            fragmentList.add(new HomeFragment());
//            fragmentList.add(new DashboardFragment());
//            fragmentList.add(new NotificationsFragment());
//
//            showFragment();
//        }
//    }
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putInt(CURRENT_FRAGMENT, currentIndex);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()){
//
//            case R.id.page1:
//
//                currentIndex = 0;
//
//                break;
//            case R.id.page2:
//
//                currentIndex = 1;
//
//                break;
//            case R.id.page3:
//
//                currentIndex = 2;
//
//                break;
//
//        }
//
//        showFragment();
//
//    }
//
//    private void showFragment() {
//
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//        //如果之前没有添加过
//        if (!fragmentList.get(currentIndex).isAdded()) {
//            transaction
//                    .hide(currentFragment)
//                    .add(R.id.page1, fragmentList.get(currentIndex), "" + currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag
//
//        } else {
//            transaction
//                    .hide(currentFragment)
//                    .show(fragmentList.get(currentIndex));
//        }
//
//        currentFragment = fragmentList.get(currentIndex);
//
//        transaction.commit();
//
//    }
//
//    /**
//     * 恢复fragment
//     */
//    private void restoreFragment() {
//
//
//        FragmentTransaction mBeginTreansaction = fragmentManager.beginTransaction();
//
//        for (int i = 0; i < fragmentList.size(); i++) {
//
//            if (i == currentIndex) {
//                mBeginTreansaction.show(fragmentList.get(i));
//            } else {
//                mBeginTreansaction.hide(fragmentList.get(i));
//            }
//
//        }
//
//        mBeginTreansaction.commit();
//
//        //把当前显示的fragment记录下来
//        currentFragment = fragmentList.get(currentIndex);
//
//    }
//

    }
}