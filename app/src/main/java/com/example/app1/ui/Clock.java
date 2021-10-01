package com.example.app1.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.app1.MainActivity;
import com.example.app1.R;

//通知推送
public class Clock extends AppCompatActivity implements View.OnClickListener{
    private TextView showtime;
    private Button mbtn_start;
    private Button mbtn_exit;
    private Button msetTime,msetRest,msetTimes;
    boolean isExit=false;
    CountDown timer;
    //以分钟为单位
    long time;
    long sHour=0,sMinute=0,sSecond;

    String mTime,mRest,mTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        showtime=findViewById(R.id.tv_time);

        mbtn_start=findViewById(R.id.btn_start);
        mbtn_start.setOnClickListener(this);

        mbtn_exit=findViewById(R.id.btn_exit);
        mbtn_exit.setOnClickListener(this);

        msetTime=findViewById(R.id.btn_settime);
        msetTime.setOnClickListener(this);

        msetRest=findViewById(R.id.btn_setrest);
        msetRest.setOnClickListener(this);

        msetTimes=findViewById(R.id.btn_settimes);
        msetTimes.setOnClickListener(this);

        showtime.setText((sHour>=10?sHour:"0"+sHour)+":"+(sMinute>=10?sMinute:"0"+sMinute)+":"+(sSecond>=10?sSecond:"0"+sSecond));

    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_settime){
//            点击设置时间
            setTimeDialog();
        }else if(view.getId()==R.id.btn_setrest){

        }else if(view.getId()==R.id.btn_settimes){

        }else if(view.getId()==R.id.btn_start){
            //对话窗口
            confirmStartDig();
        }else if(view.getId()==R.id.btn_exit){
            confirmEndDig();
        }
    }
    public void setTime(int h,int m){
        sHour=h;
        sMinute=m;
    }
    public long getTime(){
        time=sHour*60+sMinute;
        time*=60000;
        return time;
    }

    public void setTimeDialog(){
        TimePickerDialog dialog = new TimePickerDialog(Clock.this, new TimePickerDialog.OnTimeSetListener() {
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

        timer = new CountDown(time,500) {
            @Override
            public void onTick(long millisUntilFinished) {
                long ll= millisUntilFinished/1000;
                sHour=ll/3600;
                ll-=sHour*3600;
                sMinute=ll/60;
                ll-=sMinute*60;
                sSecond=ll;

                showtime.setText((sHour>=10?sHour:"0"+sHour)+":"+(sMinute>=10?sMinute:"0"+sMinute)+":"+(sSecond>=10?sSecond:"0"+sSecond));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("CountDown", millisUntilFinished + "");
            }
            @Override
            public void onFinish() {
//                Toast.makeText(Clock.this,"芜湖!完成一次专注！",Toast.LENGTH_SHORT).show();
                sendSimpleNotify("番茄钟","DONE!");
                initTime();
                showtime.setText((sHour>=10?sHour:"0"+sHour)+":"+(sMinute>=10?sMinute:"0"+sMinute)+":"+(sSecond>=10?sSecond:"0"+sSecond));
                mbtn_start.setEnabled(true);
                showtime.setEnabled(true);
                msetRest.setEnabled(true);
                msetTimes.setEnabled(true);
                msetTime.setEnabled(true);
            }
        };
        timer.start();


    }
    void confirmStartDig() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Clock.this);
        builder.setTitle("勇敢的小英雄");
        builder.setMessage("开始就不能按暂停哦！准备好了吗？");
        builder.setPositiveButton("嗯！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mbtn_start.setEnabled(false);
                msetTime.setEnabled(false);
                msetRest.setEnabled(false);
                msetTimes.setEnabled(false);
                CountDownTimer();
            }
        });
        builder.setNegativeButton("再等等...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    void confirmEndDig() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Clock.this);
        builder.setTitle("勇敢的小英雄");
        builder.setMessage("计时还没结束，现在退出记录将会不被保存哦！真的要退出吗？");
        builder.setPositiveButton("嗯，临时有事。", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //结束计时
                isExit=true;
                timer.cancel();
                initTime();
                Clock.this.finish();
                mbtn_start.setEnabled(true);
                msetTime.setEnabled(true);
                msetRest.setEnabled(true);
                msetTimes.setEnabled(true);
            }
        });
        builder.setNegativeButton("手误", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    void initTime(){
        sHour=0;
        sMinute=0;
        sSecond=0;
    }
    public void sendSimpleNotify(String title,String message){
        Intent clickIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,R.string.app_name,clickIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(contentIntent)
                .setAutoCancel(true).setSmallIcon(R.drawable.icon_username)
                .setTicker("芜湖！完成一次专注！").setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon_username))
                .setContentTitle(title).setContentText(message);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.string.app_name,notification);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }
}

