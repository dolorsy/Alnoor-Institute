package com.alnoorinstitute;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class SplashActivity extends AppCompatActivity {
    ProgressBar myProgressbar;
    TextView pleaseWait;
    SharedPreferences myAccessTokenFile;
    public static Activity my;
//    SharedPreferences.Editor myEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        myProgressbar = (ProgressBar) findViewById(R.id.myProgressbar);
        pleaseWait = (TextView) findViewById(R.id.pleaseWait);
        myAccessTokenFile = getSharedPreferences("alnoorPref", Context.MODE_PRIVATE);
        my = SplashActivity.this;
        cheakIntent();


    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            return cm.getActiveNetworkInfo() != null;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cheakIntent();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    public void cheakIntent() {
//        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url)).build());
        ParsePush.subscribeInBackground("cc");
        ParseInstallation.getCurrentInstallation().saveInBackground();



        if (myAccessTokenFile.contains("accessToken")) {
            if (!isNetworkConnected()) {
                pleaseWait.setText("No Internet Connections");
                return;
            }
            //here the code for loging in account
            ParseQuery<ParseUser> myQuery = ParseUser.getQuery();
            myQuery.whereEqualTo("username", myAccessTokenFile.getString("accessToken", " "));
            pleaseWait.setText("Initializing");

            ParseUser user = ParseUser.getCurrentUser();
            if (user.getString("AccountType").matches("student")) {
                //if The Account For Student
                Intent studentIntent = new Intent(SplashActivity.this, StudentActivety.class);
                studentIntent.putExtra("username", user.getString("username"));
                studentIntent.putExtra("StudentName", user.getString("StudentName"));
                studentIntent.putExtra("theClass", user.getString("classroom"));
                studentIntent.putExtra("ClassRoom", myAccessTokenFile.getString("classRoom", " "));
                Toast.makeText(getApplicationContext(), "splash " + myAccessTokenFile.getString("classRoom", " "), Toast.LENGTH_LONG).show();
                startActivity(studentIntent);
                finish();
            } else if (user.getString("AccountType").matches("admin")) {
                Intent adminIntent = new Intent(SplashActivity.this, Teacher.class);
                adminIntent.putExtra("username", user.getString("username"));
                startActivity(adminIntent);
                finish();
            }
            } else {
                Toast.makeText(getApplicationContext(), "There are no sharedPreferance", Toast.LENGTH_LONG).show();
                Intent loginIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(loginIntent);

            }


        }
    }

