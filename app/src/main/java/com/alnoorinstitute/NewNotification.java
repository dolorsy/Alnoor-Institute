package com.alnoorinstitute;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import java.util.HashMap;

public class NewNotification extends AppCompatActivity {

    EditText body,title;
    Button sendbtn;
    LinearLayout sendNotifyWaitLayout;
    static String ClassRoom;
    ParseObject UPLOADED_NOTIFICATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notification);
        ClassRoom = getIntent().getStringExtra("classRoom");
        body = (EditText) findViewById(R.id.body);
        title = (EditText) findViewById(R.id.title);
        sendbtn = (Button) findViewById(R.id.sendbtn);
        sendNotifyWaitLayout = (LinearLayout) findViewById(R.id.sendNotifyWaitLayout);
        sendNotifyWaitLayout.setVisibility(View.GONE);
    }

    public void Send(View view) {
        sendNotifyWaitLayout.setVisibility(View.VISIBLE);
        sendbtn.setEnabled(false);
        body.setEnabled(false);
        title.setEnabled(false);
        UPLOADED_NOTIFICATION  = new ParseObject("Notifications");
        UPLOADED_NOTIFICATION.put("title",title.getText().toString());
        UPLOADED_NOTIFICATION.put("body",body.getText().toString());
        UPLOADED_NOTIFICATION.put("classRoom",ClassRoom);
        UPLOADED_NOTIFICATION.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                Toast.makeText(getApplicationContext(),"Send Successfully",Toast.LENGTH_LONG).show();
                 finish();
                }else{
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    sendNotifyWaitLayout.setVisibility(View.GONE);
                    sendbtn.setEnabled(true);
                    body.setEnabled(true);
                    title.setEnabled(true);
                    return;
                }

            }
        });


    }
}
