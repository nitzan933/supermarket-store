package com.example.finalproject;
import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

    private final Executor backgroundExecutor = Executors.newSingleThreadExecutor();

    public NetworkBroadcastReceiver() {
    }


    @Override
    public void onReceive(Context context, Intent intent) { //the use of on receive to make toast of
                                                            //the network status
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(context, "Network is ON", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Network is ON");
        } else {
            Toast.makeText(context, "Network is OFF", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Network is OFF");
        }
    }


}
