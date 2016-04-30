package com.sburba.tvdbapi.example;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Ishanka Ranatunga on 2016-04-30.
 */
public class MyService extends Service {

    private final String TAG="tvdbapi_service";

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "Service Start");

        final Handler handler=new Handler();

        Runnable r=new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,"refreshZipThread Start");
                refreshZip();
                handler.postDelayed(refreshDBThread,1000 * 60 * 2); //starting after 2 min (Milisec*sec*min)
            }
        };

        Thread thread=new Thread(r);
        thread.start();

        stopSelf();

        return Service.START_NOT_STICKY;
    }

    private void refreshZip(){
        RefreshZips rezip=new RefreshZips(this);
        rezip.RefreshAllZips();
    }

    private void refreshDB(){
        RefreshAirDatesDB redb=new RefreshAirDatesDB(this);
        redb.RefreshAllDates();
    }

    private Runnable refreshDBThread = new Runnable(){

        public void run() {
            Log.e(TAG, "refreshDBThread Start");
            refreshDB();
        }
    };

    @Override
    public void onDestroy() {
        //super.onDestroy();
        Log.e(TAG,"Service Destroy");

        /*AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + (1000 * 60),//starting after 60 min (Milisec*sec*min)
                PendingIntent.getService(this, 0, new Intent(this, MyService.class),PendingIntent.FLAG_UPDATE_CURRENT)
        );

        Log.e(TAG, "Service Alarm Created");*/
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
