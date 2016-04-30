package com.sburba.tvdbapi.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by Ishanka Ranatunga on 2016-04-30.
 */
public class alarmReceiver extends BroadcastReceiver
{
    private final String TAG="Alarm";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        Intent updateService=new Intent(context,MyService.class);
        context.startService(updateService);

        Log.e(TAG, "Service Triggered by the Alarm");

        wl.release();
    }

}
