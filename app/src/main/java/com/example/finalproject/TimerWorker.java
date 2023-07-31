package com.example.finalproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;


public class TimerWorker extends Worker {

    public static final String TAG = "TIMERTAG";
    private static final String NOTIFICATION_CHANNEL_ID = "countdown_channel";
    private static final int NOTIFICATION_ID = 101;
    public static boolean isWorking = false;
    private Context context;

    private  ArrayList<Product> cartList;

    private ViewModelCart ViewModel;

    public TimerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        int countdownTime = 30; //30 seconds
        String product = getInputData().getString("product_down_of_cart");

        for (int i = countdownTime; i >= 0; i--) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.cart)
                .setContentTitle("Cart update")
                .setContentText(product + " is off the cart")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Countdown Timer", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        MainActivity.product = product;
        ViewModelCart.productRemove(product);
        Notification notification = builder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);
        MainActivity.workerThread();



        Data outputData = new Data.Builder()
                .putInt("countdown_completed", 1)
                .build();

        return Result.success(outputData);
    }

}