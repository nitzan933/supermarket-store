package com.example.finalproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Frag.OnClickListenerFrag {

    private static Context context;
    static Handler mHandler;

    private NetworkBroadcastReceiver br;

    public static String product = "";

    private static final int NOTIFICATION_ID = 101;
    private static final String NOTIFICATION_CHANNEL_ID = "countdown_channel";


    // to do: match layout for landscape

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        br = new NetworkBroadcastReceiver();
        IntentFilter i = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(br,i);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        changeTheme(prefs.getBoolean("switch_dark_theme", false));
        DetailsFragment fragB = (DetailsFragment) getSupportFragmentManager().findFragmentByTag("FRAGB");
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            if (fragB != null) {
                getSupportFragmentManager().beginTransaction()
                        .show(fragB)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentDetailContainerView2, DetailsFragment.class, null, "FRAGB")
                        .commit();
            }
            getSupportFragmentManager().executePendingTransactions();
        }

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Toast.makeText(getApplicationContext(), product + " is off the cart", Toast.LENGTH_LONG).show();
            }
        };
    }

    static void workerThread() {
        int command = 1;
        String parameter = "Finished!";
        Message message = mHandler.obtainMessage(command, parameter);
        message.sendToTarget();
    }

    @Override
    public void clickOnProduct() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, DetailsFragment.class, null,"FRAGB")
                    .addToBackStack("BBB")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }



    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView, new mySettings())
                        .addToBackStack(null)
                        .commit();
                getSupportFragmentManager().executePendingTransactions();
                return true;
            case R.id.exit:
                showAlertDialogForExit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAlertDialogForExit() {
        FragmentManager fm = getSupportFragmentManager();
        ExitDialog alertDialog = new ExitDialog();
        alertDialog.show(fm, "fragment_alert");
    }

    public static class mySettings extends PreferenceFragmentCompat{

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.settings, rootKey);
            SharedPreferences.OnSharedPreferenceChangeListener listener;
            //Loads Shared preferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            //Setup a shared preference listener for hpwAddress and restart transport
            prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    if (key.equals("switch_dark_theme")) {
                        changeTheme(prefs.getBoolean(key, false));
                    }
                }
            });
        }
    }



    public static void changeTheme(boolean dark){
        if(dark)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}