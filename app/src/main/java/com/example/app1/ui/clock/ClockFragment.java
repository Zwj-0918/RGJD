package com.example.app1.ui.clock;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;

import com.example.app1.R;
import com.example.app1.databinding.FragmentClockBinding;


public class ClockFragment extends Fragment implements View.OnClickListener {
    private FragmentClockBinding binding;
    protected View mView;
    protected Context mContext;

    private EditText met_settime;
    private TextView showtime;
    private Button mbtn_start;
    //以分钟为单位
    int time;
    int hour,minute;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_clock,container,false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        mContext = getActivity();
        showtime=mView.findViewById(R.id.tv_time);
        showtime.setOnClickListener(this);

        mbtn_start=mView.findViewById(R.id.btn_start);
        mbtn_start.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_time){
//            点击设置时间
            setTimeDialog();
//            当开始计时就失效

        }else if(view.getId()==R.id.btn_start){
            //对话窗口
            CountDownTimer();
        }
    }
    public void setTime(int h,int m){
        hour=h;
        minute=m;
    }
    public int getTime(){
        time=hour*60+minute;
        time*=60000;
        return time;
    }
    public void setTimeDialog(){
        TimePickerDialog dialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                showtime.setText((i>=10?i:'0'+i)+":"+(i1>=10?i1:'0'+i1)+":"+"00");
                setTime(i,i1);
            }
        }, 0, 0, true);
        dialog.show();
    }
    public void CountDownTimer(){
        getTime();
        Toast.makeText(mContext,"倒计时："+time,Toast.LENGTH_SHORT).show();
        CountDownTimer timer =new CountDownTimer(time,1000) {
            @Override
            public void onTick(long l) {
                long ll= l/1000;
                long h,m,s;
                h=ll/3600;
                ll-=h*3600;
                m=ll/60;
                ll-=m*60;
                s=ll;

                showtime.setText((h>=10?h:'0'+h)+":"+(m>=10?m:'0'+m)+":"+(s>=10?s:'0'+s));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("CountDown", l + "");
            }

            @Override
            public void onFinish() {
                showtime.setText("芜湖!完成一次专注！");
            }
        };
        timer.start();
    }

}
