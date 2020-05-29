package com.alnoorinstitute;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParsePush;

public class Teacher extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    Button editWeekTable, newNotificationbtn, allNotificationsbtn;
    Spinner classRoomSpinner;
    //It should be the same sort in this 2 array
    String[] myclassRooms = {" التاسع", " العلمي", " الأدبي"};
    String[] myclassRoomsID = {"9th", "scientific", "literary"};
    String selection;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        editWeekTable = (Button) findViewById(R.id.editWeekTable);
        newNotificationbtn = (Button) findViewById(R.id.newNotificationbtn);
        allNotificationsbtn = (Button) findViewById(R.id.allNotificationsbtn);
        classRoomSpinner = (Spinner) findViewById(R.id.classRoomSpinner);
        classRoomSpinner.setOnItemSelectedListener(this);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.textview, myclassRooms);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_style);
        classRoomSpinner.setAdapter(spinnerAdapter);
        String username = getIntent().getStringExtra("username");
        ParseObject user = (ParseObject) getIntent().getSerializableExtra("ParseObject");
        selection = "9th";

    }

    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.my.finish();
        SplashActivity.my.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (MainActivity.active) MainActivity.my.finish();
        SplashActivity.my.finish();
        finish();
        onDestroy();
    }

    public void logoutfun(View view) {
        SharedPreferences myAccessTokenFile = getSharedPreferences("alnoorPref", Context.MODE_PRIVATE);

        SharedPreferences.Editor myEdit = myAccessTokenFile.edit();
        myEdit.clear();
        myEdit.commit();
        Intent restart = getBaseContext().getPackageManager().
                /*  =>  */        getLaunchIntentForPackage(getBaseContext().getPackageName());
        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //  restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(restart);


    }

    int s = 0;

    public void sendNotification(View view) {
        s++;
        Intent notiIntent = new Intent(this, NewNotification.class);
        notiIntent.putExtra("classRoom", selection);
        startActivity(notiIntent);


    }

    public void editWeekTableActivity(View view) {
        Toast.makeText(getApplicationContext(), "The Selection is " + selection, Toast.LENGTH_LONG).show();
        Intent editTableIntent = new Intent(Teacher.this, editTable.class);
        editTableIntent.putExtra("targetClass", selection);
        editTableIntent.putExtra("classRoom", myclassRooms[i]);
        startActivity(editTableIntent);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selection = myclassRoomsID[position];
        i = position;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        selection = "9th";
    }

    public void addNewPay(View view) {
        Intent newPayIntent = new Intent(Teacher.this, newPay.class);
        newPayIntent.putExtra("targetClass", selection);
        newPayIntent.putExtra("classRoom", myclassRooms[i]);
        startActivity(newPayIntent);

    }

    public void getAllPays(View view) {
        Intent aLLPaysIntent = new Intent(Teacher.this, allPays.class);
        aLLPaysIntent.putExtra("classRoom", myclassRooms[i]);
        startActivity(aLLPaysIntent);

    }

    public void getAllNotifications(View view) {
        Intent Notifications_Sent = new Intent(this, Notifications_Sent.class);
        Notifications_Sent.putExtra("classRoom", selection);
        startActivity(Notifications_Sent);
    }
}

