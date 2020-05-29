package com.alnoorinstitute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Notifications_Sent extends AppCompatActivity {
    Button getMorebtn;
    ListView notificationsList;
    ParseQuery<ParseObject> myQuery;
    boolean serverError = false;
    int limt = 10, i = 0, size = 0;
    LinearLayout allNotificationsWaitLayout;
    String ClassRoom;
    ArrayList<MyNotification> myDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications__sent);
        notificationsList = (ListView) findViewById(R.id.notificationsList);
        getMorebtn = (Button) findViewById(R.id.getMorebtn);
        //ClassRoom want to be sent from Teacher Activity
        ClassRoom = getIntent().getStringExtra("classRoom");
        allNotificationsWaitLayout = (LinearLayout) findViewById(R.id.allNotificationsWaitLayout);

        getNotificationsFromServer();

    }


    public class MyNotification {
        String title, body, notifyDate;

        public MyNotification(String title, String body, String notifyDate) {
            this.body = body;
            this.title = title;
            this.notifyDate = notifyDate;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }

        public String getNotifyDate() {
            return notifyDate;
        }

    }

    private void getNotificationsFromServer() {

        getMorebtn.setEnabled(false);
        myQuery = ParseQuery.getQuery("Notifications");
        myQuery.whereEqualTo("classRoom", ClassRoom);
        myQuery.orderByDescending("createdAt");
        myQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    size = objects.size();
                    if(size ==0){
                        Toast.makeText(getApplicationContext(),"لا يوجد بيانات",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    if (size < limt) {
                        limt = size;
                        Toast.makeText(getApplicationContext(), "لا يوجد مزيد!", Toast.LENGTH_LONG).show();
//                        return;p
                    }
                    for (; i < limt; i++) {
                        String stitle = objects.get(i).getString("title");
                        String sbody = objects.get(i).getString("body");
                        Date myDate = objects.get(i).getCreatedAt();
                        DateFormat df = new SimpleDateFormat("dd-mm-yyyy");
                        String notifyDate = df.format(myDate);
                        myDataList.add(new MyNotification(stitle, sbody, notifyDate));
                    }


                } else {

                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    serverError = true;
                    if (i == 0) finish();

                }

                allPayAdapter adapterList = new allPayAdapter(myDataList, getApplicationContext(), false);
                notificationsList.setAdapter(adapterList);
                allNotificationsWaitLayout.setVisibility(View.GONE);
                getMorebtn.setEnabled(true);
            }
        });

    }


    public void getMore(View view) {
        allNotificationsWaitLayout.setVisibility(View.VISIBLE);
        getMorebtn.setEnabled(false);


        if (serverError) {

            allNotificationsWaitLayout.setVisibility(View.GONE);
            return;
        }
        limt += 10;
        getNotificationsFromServer();
        getMorebtn.setEnabled(true);
        notificationsList.setStackFromBottom(true);
    }
}
