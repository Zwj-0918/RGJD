package com.example.app1.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.app1.R;
import com.example.app1.databinding.FragmentHomeBinding;
import com.example.app1.ui.Tomato.Clock;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    protected Context mContext;
    private TextView mClock;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = getActivity();
        mClock = binding.tvClock;
        mClock.setOnClickListener(this);
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_clock){
            Intent intent = new Intent(mContext, Clock.class);
            startActivity(intent);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}