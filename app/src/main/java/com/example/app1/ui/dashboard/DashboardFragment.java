package com.example.app1.ui.dashboard;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.app1.R;
import com.example.app1.databinding.FragmentDashboardBinding;


//问题1：页面跳转计时器有问题
//问题2：计时器的时间选择有问题
public class DashboardFragment extends Fragment implements View.OnClickListener{

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    protected View mView;
    protected Context mContext;

    private TextView showtime;
    private Button mbtn_start;
    //以分钟为单位
    long time;
    long hour=0,minute=0,second;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mContext = getActivity();
        showtime=binding.tvTime;
        showtime.setOnClickListener(this);

        mbtn_start=binding.btnStart;
        mbtn_start.setOnClickListener(this);


        showtime.setText((hour>=10?hour:"0"+hour)+":"+(minute>=10?minute:"0"+minute)+":"+(second>=10?second:"0"+second));

        return root;
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_time){
//            点击设置时间
            setTimeDialog();

        }else if(view.getId()==R.id.btn_start){
            //对话窗口
            confirmDig();
        }
    }
    public void setTime(int h,int m){
        hour=h;
        minute=m;
    }
    public long getTime(){
        time=hour*60+minute;
        time*=60000;
        return time;
    }

    public void setTimeDialog(){
        TimePickerDialog dialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                showtime.setText((i>=10?i:"0"+i)+":"+(i1>=10?i1:"0"+i1)+":"+"00");
//                showtime.setText(i+"时"+i1+"分");
                setTime(i,i1);
            }
        }, 0, 0, true);
        dialog.show();
    }
    public void CountDownTimer(){
        getTime();
//        Toast.makeText(mContext,"倒计时："+time,Toast.LENGTH_SHORT).show();
        CountDownTimer timer =new CountDownTimer(time,500) {
            @Override
            public void onTick(long l) {
                long ll= l/1000;
                hour=ll/3600;
                ll-=hour*3600;
                minute=ll/60;
                ll-=minute*60;
                second=ll;

                showtime.setText((hour>=10?hour:"0"+hour)+":"+(minute>=10?minute:"0"+minute)+":"+(second>=10?second:"0"+second));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("CountDown", l + "");
            }

            @Override
            public void onFinish() {
                Toast.makeText(mContext,"芜湖!完成一次专注！",Toast.LENGTH_SHORT).show();
                hour=0;
                minute=0;
                second=0;
                showtime.setText((hour>=10?hour:"0"+hour)+":"+(minute>=10?minute:"0"+minute)+":"+(second>=10?second:"0"+second));
                mbtn_start.setEnabled(true);
                showtime.setEnabled(true);
            }
        };
        timer.start();
    }
    void confirmDig(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("勇敢的小英雄");
        builder.setMessage("开始就不能按暂停哦！准备好了吗？");
        builder.setPositiveButton("嗯！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mbtn_start.setEnabled(false);
                showtime.setEnabled(false);
                CountDownTimer();
            }
        });
        builder.setNegativeButton("再等等...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
//    @Override
//    public void onDestroyView() {
//
//        super.onDestroyView();
//        binding = null;
//    }
}