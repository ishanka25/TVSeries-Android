package com.sburba.tvdbapi.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Ishanka Ranatunga on 2016-04-30.
 */
public class UpdateReceiver extends BroadcastReceiver {

    private final String TAG="NET";

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            Log.i(TAG, "Internet Connection = " + isConnected);
            Intent updateService=new Intent(context,MyService.class);
            context.startService(updateService);
            Log.i(TAG, "Service Start  = " + isConnected);
        }
        else {
            Log.i(TAG, "Internet Connection = " +isConnected);
            Log.i(TAG, "Service Start  = " + isConnected);
        }

    }
}
