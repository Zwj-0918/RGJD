package com.example.app1.ui.Tomato;

import androidx.annotation.RequiresApi;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
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
//开始与未开始可能需要两个布局
public class Clock extends AppCompatActivity implements View.OnClickListener{
    private TextView showtime,mtv_setTime,mtv_setRest,mtv_setTimes;
    private Button mbtn_start;
    private Button mbtn_exit;
    private SeekBar msb_setTime,msb_setRest,msb_Times;
    //是否在计时状态下
    boolean isOnClock=false;
    CountDown timer;
    //以分钟为单位
    long time=25*60000,rtime=5*60000,ttime;
    //操作数据
    long sHour=0,sMinute=25,sSecond=0;
    //
    long mTime=25,mRest=5,mTimes=1;

    //保留输入
    long mHour=0,mMinute=25,mSecond=0;
    long rHour=0,rMinute=5,rSecond=0;

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

        msb_setTime=findViewById(R.id.sb_setTime);
        msb_setRest=findViewById(R.id.sb_setRest);



        initTime();
        showtime.setText((sHour>=10?sHour:"0"+sHour)+":"+(sMinute>=10?sMinute:"0"+sMinute)+":"+(sSecond>=10?sSecond:"0"+sSecond));
        mtv_setTime.setText("专注时间："+(mHour>=10?mHour:"0"+mHour)+"时"+(mMinute>=10?mMinute:"0"+mMinute)+"分");
        mtv_setRest.setText("休息时间："+(rHour>=10?rHour:"0"+rHour)+"时"+(rMinute>=10?rMinute:"0"+rMinute)+"分");
        msb_setTime.setProgress(250/18);
        msb_setRest.setProgress(50);
        msb_setTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTime=seekBar.getProgress()*3*60/100;
                mHour=mTime/60;
                mMinute=mTime-mHour*60;
                mtv_setTime.setText("专注时间："+(mHour>=10?mHour:"0"+mHour)+"时"+(mMinute>=10?mMinute:"0"+mMinute)+"分");
                showtime.setText((mHour>=10?mHour:"0"+mHour)+":"+(mMinute>=10?mMinute:"0"+mMinute)+":00");

                mRest= mTime/5;
                rHour=mRest/60;
                rMinute=mRest-rHour*60;
                mtv_setRest.setText("休息时间："+(rHour>=10?rHour:"0"+rHour)+"时"+(rMinute>=10?rMinute:"0"+rMinute)+"分");
                msb_setRest.setProgress(50);

                time=mHour*60+mMinute;
                rtime=time/5;
                time*=60000;
                rtime*=60000;

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
    }
    //点击事件监听
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_start){
            //对话窗口
            confirmStartDig();
        }else if(view.getId()==R.id.btn_exit){
            confirmEndDig();
        }
    }
//倒计时器
    public void CountDownTimer(long time){
        initTime();
        timer = new CountDown(time,500) {

            @Override
            public void onTick(long millisUntilFinished) {
                long ll=0;
                if(millisUntilFinished>=rtime){
                    ll=(millisUntilFinished-rtime)/1000;
                }else{
                    ll= millisUntilFinished/1000;
                }
                if (millisUntilFinished==rtime){
                    Toast.makeText(Clock.this,"休息时间到！",Toast.LENGTH_SHORT).show();
                }

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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFinish() {
                Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                Toast.makeText(Clock.this,"芜湖!完成一次专注！", Toast.LENGTH_SHORT).show();
//                sendSimpleNotify("番茄钟","DONE!");
//                initTime();
                isOnClock=false;
                showtime.setText((mHour>=10?mHour:"0"+mHour)+":"+(mMinute>=10?mMinute:"0"+mMinute)+":"+(mSecond>=10?mSecond:"0"+mSecond));
                mbtn_start.setEnabled(true);
                msb_setRest.setEnabled(true);
                msb_setTime.setEnabled(true);
            }
        };
        timer.start();
    }

    //开始计时确认框
    void confirmStartDig() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Clock.this);
        builder.setTitle("勇敢的小英雄");
        builder.setMessage("开始就不能按暂停哦！准备好了吗？");
        builder.setPositiveButton("嗯！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isOnClock=true;
                mbtn_start.setEnabled(false);
                msb_setRest.setEnabled(false);
                msb_setTime.setEnabled(false);
                ttime=time+rtime;
                Log.d("","time:"+time+",rtime:"+rtime+",ttime:"+ttime);
                CountDownTimer(ttime);
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
//结束计时确认框
    void confirmEndDig() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Clock.this);
        builder.setTitle("勇敢的小英雄");
        builder.setMessage("计时还没结束，现在退出记录将会不被保存哦！真的要退出吗？");
        builder.setPositiveButton("嗯，临时有事。", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //结束计时
                timer.cancel();
                mbtn_start.setEnabled(true);
                msb_setRest.setEnabled(true);
                msb_setTime.setEnabled(true);
                initTime();
                isOnClock=false;
                Clock.this.finish();
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
    //初始化操作数据
    void initTime(){
        sHour=0;
        sMinute=25;
        sSecond=0;

//        time=25*60000;
//        rtime=5*60000;
//        ttime=0;
//
//        mTime=25;
//        mRest=5;
//        mTimes=1;

//        mHour=0;
//        mMinute=25;
//        mSecond=0;
//
//        rHour=0;
//        rMinute=5;
//        rSecond=0;

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK||event.getKeyCode()==KeyEvent.KEYCODE_HOME){
            if(isOnClock){
                confirmEndDig();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }
}

