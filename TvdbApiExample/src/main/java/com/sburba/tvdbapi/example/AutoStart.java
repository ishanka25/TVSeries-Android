package com.sburba.tvdbapi.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by DILSHAN FERNANDO on 4/29/2016.
 */
public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MyNotificationService.class));
    }
}
