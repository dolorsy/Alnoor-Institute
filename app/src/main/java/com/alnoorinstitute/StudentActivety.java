package com.alnoorinstitute;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

public class StudentActivety extends AppCompatActivity {
    TextView theName, theClass,paidUp,theRequired;
    ParseObject user;
    String username, ClassRoom, stdClass;
    Button WeekTable_Student_btn, notes_student_btn, std_info_btn, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_activety);
        theName = (TextView) findViewById(R.id.theName);
        theClass = (TextView) findViewById(R.id.theClass);
        WeekTable_Student_btn = (Button) findViewById(R.id.WeekTable_Student_btn);
        notes_student_btn = (Button) findViewById(R.id.notes_student_btn);
        std_info_btn = (Button) findViewById(R.id.std_info_btn);
        paidUp = (TextView) findViewById(R.id.paidUp);
        theRequired = (TextView) findViewById(R.id.theRequired);
        logout = (Button) findViewById(R.id.logout);
        stdClass = getIntent().getStringExtra("theClass");
        username = getIntent().getStringExtra("username");
        ClassRoom = getIntent().getStringExtra("ClassRoom");
        if(!isMyServiceRunning(ListenToServerService.class)){
            Toast.makeText(getApplicationContext(),"Service is Stopped",Toast.LENGTH_LONG).show();
            Intent serviceIntent = new Intent(this, ListenToServerService.class);
            serviceIntent.putExtra("classRoom", ClassRoom);
            startService(serviceIntent);

        }


        // user = (ParseObject) getIntent().getSerializableExtra("ParseObject");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (MainActivity.active) MainActivity.my.finish();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        theName.setText(getIntent().getStringExtra("StudentName"));
        theClass.setText(stdClass);
        theRequired.setText(getIntent().getStringExtra("theRequired"));
        paidUp.setText(getIntent().getStringExtra("paidUp"));
        if (MainActivity.active) MainActivity.my.finish();

    }

    public void logoutfun(View view) {
        SharedPreferences myAccessTokenFile = getSharedPreferences("alnoorPref", Context.MODE_PRIVATE);

        SharedPreferences.Editor myEdit = myAccessTokenFile.edit();
        myEdit.clear();
        myEdit.commit();
        Intent restart = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(restart);
    }

    public void getWeekTable(View view) {

        Intent stdTableIntent = new Intent(StudentActivety.this, StudentTable.class);
        stdTableIntent.putExtra("theClass", stdClass);
        stdTableIntent.putExtra("ClassRoom", ClassRoom);
        startActivity(stdTableIntent);


    }

    public void getAllMarks(View view) {
        Toast.makeText(getApplicationContext(), "<<< Coming Soon >>>", Toast.LENGTH_LONG).show();

    }

    public void AllNotificationRecived(View view) {
        Intent AllRecived = new Intent(this, Notifications_Sent.class);
        AllRecived.putExtra("ClassRoom",ClassRoom);
        startActivity(AllRecived);
    }

    public void getAllSelfNote(View view) {
        Intent AllRecived = new Intent(this, Notifications_Sent.class);
        Toast.makeText(getApplicationContext(),getIntent().getStringExtra("username"),Toast.LENGTH_LONG).show();
        AllRecived.putExtra("ClassRoom",getIntent().getStringExtra("username"));
        startActivity(AllRecived);
    }

    private boolean isMyServiceRunning(Class serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
