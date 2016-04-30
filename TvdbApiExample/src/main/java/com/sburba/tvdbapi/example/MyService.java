package com.sburba.tvdbapi.example;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private final String TAG="tvdbapi service";

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
                handler.postDelayed(refreshDBThread,60000); //starting after 1 min = 60000 milisec
            }
        };

        Thread thread=new Thread(r);
        thread.start();

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
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
