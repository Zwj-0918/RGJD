package com.example.app1.ui.Tomato;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.app1.MainActivity;
import com.example.app1.R;
import com.example.app1.tools.CircleAnimation;
import com.example.app1.tools.CountDown;

//问题：通知推送
public class Clock extends AppCompatActivity implements View.OnClickListener{
    private TextView showtime,mtv_setTime,mtv_setRest,mtv_setTimes;
    private Button mbtn_start;
    private Button mbtn_exit;
    private SeekBar msb_setTime,msb_setRest,msb_Times;

    boolean isExit=false;
    CountDown timer;
    //以分钟为单位
    long time,rtime;
    //操作数据
    long sHour=0,sMinute=0,sSecond=0;

    long mTime=0,mRest=0,mTimes=1;

    //保留输入
    long mHour=0,mMinute=25,mSecond=0;
    long rHour=0,rMinute=0,rSecond=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        showtime=findViewById(R.id.tv_time);

        mbtn_start=findViewById(R.id.btn_start);
        mbtn_start.setOnClickListener(this);

        mbtn_exit=findViewById(R.id.btn_exit);
        mbtn_exit.setOnClickListener(this);

        mtv_setTime=findViewById(R.id.tv_setTime);
        mtv_setRest=findViewById(R.id.tv_setRest);
        mtv_setTimes=findViewById(R.id.tv_setTimes);
        msb_setTime=findViewById(R.id.sb_setTime);
        msb_setRest=findViewById(R.id.sb_setRest);
        msb_Times=findViewById(R.id.sb_setTimes);
        mtv_setTime.setText("专注时间："+(mHour>=10?mHour:"0"+mHour)+"时"+(mMinute>=10?mMinute:"0"+mMinute)+"分");
        mtv_setRest.setText("休息时间："+(rHour>=10?rHour:"0"+rHour)+"时"+(rMinute>=10?rMinute:"0"+rMinute)+"分");
        msb_setTime.setProgress(250/18);
        msb_setRest.setProgress(100);
        msb_setTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTime=seekBar.getProgress()*3*60/100;
                mHour=mTime/60;
                mMinute=mTime-mHour*60;
                mtv_setTime.setText("专注时间："+(mHour>=10?mHour:"0"+mHour)+"时"+(mMinute>=10?mMinute:"0"+mMinute)+"分");

                mRest= mTime*2/5;
                rHour=mRest/60;
                rMinute=mRest-rHour*60;
                mtv_setRest.setText("休息时间："+(rHour>=10?rHour:"0"+rHour)+"时"+(rMinute>=10?rMinute:"0"+rMinute)+"分");

                time=mHour*60+mMinute;
                time*=60000;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("setTime","seekBar:"+seekBar.getProgress());
            }
        });

        msb_setRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mRest= mTime*seekBar.getProgress()/250;
                rHour=mRest/60;
                rMinute=mRest-rHour*60;
                mtv_setRest.setText("休息时间："+(rHour>=10?rHour:"0"+rHour)+"时"+(rMinute>=10?rMinute:"0"+rMinute)+"分");

                rtime=rHour*60+rMinute;
                rtime*=60000;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("setRest","seekBar:"+seekBar.getProgress());

            }
        });

        msb_Times.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTimes = seekBar.getProgress()/10+1;
                mtv_setTimes.setText("每轮专注次数："+(mTimes>=10?mTimes:"0"+mTimes)+"次");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("setTimes","seekBar:"+seekBar.getProgress());
            }
        });


        showtime.setText((sHour>=10?sHour:"0"+sHour)+":"+(sMinute>=10?sMinute:"0"+sMinute)+":"+(sSecond>=10?sSecond:"0"+sSecond));


    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_start){
            //对话窗口
            confirmStartDig();
        }else if(view.getId()==R.id.btn_exit){
            confirmEndDig();
        }
    }
//    public void setTime(long h,long m){
//        sHour=h;
//        sMinute=m;
//    }
//    public long getTime(long time){
//        time=sHour*60+sMinute;
//        time*=60000;
//        return time;
//    }

    public void CountDownTimer(long time,String type){
//        getTime(time);
        initTime();
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
                Toast.makeText(Clock.this,"芜湖!完成一次专注！", Toast.LENGTH_SHORT).show();
//                sendSimpleNotify("番茄钟","DONE!");
                initTime();
                showtime.setText((sHour>=10?sHour:"0"+sHour)+":"+(sMinute>=10?sMinute:"0"+sMinute)+":"+(sSecond>=10?sSecond:"0"+sSecond));
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
                msb_setRest.setEnabled(false);
                msb_setTime.setEnabled(false);
                msb_Times.setEnabled(false);
                for(int j=1;j<=mTimes;j++){
                    CountDownTimer(time,"Clock");
                    CountDownTimer(rtime,"Rest");
                }
//                mbtn_start.setEnabled(true);
//                msb_setRest.setEnabled(true);
//                msb_setTime.setEnabled(true);
//                msb_Times.setEnabled(true);
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
                msb_setRest.setEnabled(true);
                msb_setTime.setEnabled(true);
                msb_Times.setEnabled(true);
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

