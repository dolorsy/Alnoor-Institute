package com.alnoorinstitute;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ListenToServerService extends Service {
    private  int LAST_CHECK = 0,THIS_CHECK =0;
    private boolean onRestart = false;
    private String nTitle="Empty Title",nBody = "Empty Body";
    ParseQuery<ParseObject> notificationQuary ;
    public ListenToServerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public  boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            return cm.getActiveNetworkInfo() != null;
        }
        return false;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {


        new CountDownTimer(3600000*24,10000){

            @Override
            public void onTick(long millisUntilFinished) {
                LAST_CHECK = THIS_CHECK;
                notificationQuary = ParseQuery.getQuery("Notifications");
                String test = intent.getStringExtra("classRoom");
                notificationQuary.whereEqualTo("classRoom",test);
                notificationQuary.orderByDescending("createdAt");
                notificationQuary.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            THIS_CHECK = objects.size();
                            onRestart = true;
                            nTitle = objects.get(0).getString("title");
                            nBody = objects.get(0).getString("body");

                            if (LAST_CHECK < THIS_CHECK) {
                                NotificationBuilder.showNotification(getApplicationContext(), nTitle, nBody);
                            }

                        } else {
                            Log.d("Error in reciving ",e.toString());
                        }
                    }
                });



            }

            @Override
            public void onFinish() {

            }
        }.start();

        return Service.START_STICKY;
    }


}
