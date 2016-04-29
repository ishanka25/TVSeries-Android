package com.sburba.tvdbapi.example;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

/**
 * Created by DILSHAN FERNANDO on 4/30/2016.
 */
public class MyNotificationService extends Service {

    Context context=MyNotificationService.this;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            TvDbDBAdapter dbadapter = new TvDbDBAdapter(context);
            List<EpisodeAirdateModel> data;
            data = dbadapter.getTodayEpisodes();
            String SeriesName="";
            String[] strarry={};
            EpisodeAirdateModel current=new EpisodeAirdateModel();
            for(int i=0;i<data.size();i++){
                current=(EpisodeAirdateModel)data.toArray()[i];
                strarry=dbadapter.getSeriesTitle(current.sid);
                SeriesName=strarry[0];
                Intent myintent=new Intent();
                PendingIntent pIntent=PendingIntent.getActivity(context,0,myintent,0);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(SeriesName + " New Episode Available")
                                .setContentText(current.getSesNo()+"x"+current.getEpno()+" - "+current.getEptitle())
                        ;

                mBuilder.setContentIntent(pIntent);
                int mNotificationId = i;
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());


            }


        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        this.onDestroy();

        return START_REDELIVER_INTENT ;
    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        //super.onDestroy();
        Log.e("MyNotificationService", "Service Destroy");

        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + (1000 * 60*60*24),//starting after 60 min (Milisec*sec*min)
                PendingIntent.getService(this, 0, new Intent(this, MyNotificationService.class), PendingIntent.FLAG_UPDATE_CURRENT)
        );

        Log.e("MyNotificationService", "Service Alarm Created");
    }


}
