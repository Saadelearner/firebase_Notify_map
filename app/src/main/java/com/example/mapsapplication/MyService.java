package com.example.mapsapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(01 ,CreateNotification());
        return START_STICKY;
    }

    public Notification CreateNotification(){
        NotificationCompat.Builder  builder=null;
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
            builder= new NotificationCompat.Builder(this);
        }else {
            CreateNotificationChannel();
            builder= new NotificationCompat.Builder(this,getPackageName());
        }
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setAutoCancel(true);
        builder.setContentTitle(getResources().getString(R.string.app_name));
        builder.setContentText(getResources().getString(R.string.app_name));
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        Notification notification =builder.build();
        return notification;
    }
    public  void CreateNotificationChannel(){
        NotificationChannel notificationChannel = new NotificationChannel(getPackageName(),"My Channel", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setLightColor(R.color.black);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(notificationChannel);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}