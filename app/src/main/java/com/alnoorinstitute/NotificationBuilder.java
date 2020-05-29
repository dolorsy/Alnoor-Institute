package com.alnoorinstitute;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import org.jetbrains.annotations.Nullable;

public class NotificationBuilder {
    private static final int NID = 0;

    private static final int PENDING_INTENT_ID = 1;
    private static final String NotificationChannel  = "empty";
    private static NotificationCompat.Builder myNotificationBuilder;
    private static NotificationManager myNotificationManger;
    private static boolean isNew = true;

    public NotificationBuilder(  ){
    }

    public  static  void showNotification(Context context , String title , String body){
        if (isNew == false){
            updateNotification(title,body);
return;
        }
        myNotificationManger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String name = "Oreo Channel";
            String description = "This channel shows only Oreo notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            android.app.NotificationChannel channel = new NotificationChannel(NotificationChannel, name, importance);
            channel.setShowBadge(true);
            channel.setDescription(description);
            myNotificationManger.createNotificationChannel(channel);



        }

   myNotificationBuilder = new NotificationCompat.Builder(context,NotificationChannel)
           .setGroup(NotificationCompat.CATEGORY_REMINDER)
           .setGroupSummary(true)
           .setSmallIcon(R.drawable.second)
           .setLargeIcon(largeIcon(context))
           .setContentTitle(title)
           .setContentText(body)
           .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
           .setContentIntent(contentIntent(context))
           .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

           .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            myNotificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        }
        myNotificationManger.notify(NID, myNotificationBuilder.build());
        isNew = false;

    }

    public static void updateNotification(String title, String body) {
        myNotificationBuilder
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setOnlyAlertOnce(true)
        ;

        myNotificationManger.notify(NID, myNotificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, SplashActivity.class);
        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.push_icon);
        return largeIcon;
    }

    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
    public static void shareNotification(Context context, String title, String body){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(title);
        stringBuilder.append("\n");
        stringBuilder.append(body);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        context.startActivity(intent.createChooser(intent, "Choose the app you want to share on it"));
    }





}
