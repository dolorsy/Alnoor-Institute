package com.alnoorinstitute;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.service.autofill.ImageTransformation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    ImageView logo;
    EditText emailFiled,passwordFiled;
    Button contactusButton,loginButton,allMarks;
    String password ,username;
    LinearLayout loginWaitLayout;
    public static Activity my;
    public static boolean active;
       SharedPreferences myAccessTokenFile;
       SharedPreferences.Editor myEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginWaitLayout = (LinearLayout) findViewById(R.id.loginWaitLayout);
        loginWaitLayout.setVisibility(View.GONE);
        allMarks = (Button) findViewById(R.id.allMarks);
         emailFiled = (EditText) findViewById(R.id.email_filed);
         passwordFiled = (EditText) findViewById(R.id.password_filed);
         loginButton = (Button) findViewById(R.id.loginButton);
         contactusButton = (Button) findViewById(R.id.contactusButton);
         logo = (ImageView) findViewById(R.id.logo);
         my = MainActivity.this;

        myAccessTokenFile = getSharedPreferences("alnoorPref", Context.MODE_PRIVATE);



    }

    @Override
    protected void onStart() {
        super.onStart();
    active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    active = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public  boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            return cm.getActiveNetworkInfo() != null;
        }
        return false;
    }

    public void parsetest(View view) {
Toast.makeText(getApplicationContext(),"Contact us !",Toast.LENGTH_LONG).show();


    }

private void UX_FOR_LOGIN(boolean choic){
    emailFiled.setEnabled(choic);
    passwordFiled.setEnabled(choic);
    loginButton.setEnabled(choic);
    contactusButton.setEnabled(choic);
}


    public void login(View view) {
        username = emailFiled.getText().toString();
        password = passwordFiled.getText().toString();
        if(username.matches("") && password.matches("")){
            emailFiled.setError("This Filed Can't be Empty");
            passwordFiled.setError("This Filed Can't be Empty");
            return;
        }else if(username.matches("")){
            emailFiled.setError("This Filed Can't be Empty");
            return;

        }else if(password.matches("")){
            passwordFiled.setError("This Filed Can't be Empty");
            return;
        }
        if(!isNetworkConnected()){
            Toast.makeText(getApplicationContext(),"Pleas Check Your Connection",Toast.LENGTH_LONG).show();
            return;
        }
        loginWaitLayout.setVisibility(View.VISIBLE);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                UX_FOR_LOGIN(false);
                if(e != null){
                    loginWaitLayout.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),/*"تأكد من المعلومات المدخلة"*/e.toString(),Toast.LENGTH_LONG).show();
                    UX_FOR_LOGIN(true);
                return;
                }
                if (user != null){
                    //Login Successful
                   // user.get("AccountType");
                    myAccessTokenFile = getSharedPreferences("alnoorPref",Context.MODE_PRIVATE);
                    myEdit = myAccessTokenFile.edit();                 //   myAccessTokenFile.edit().putString("accessToken",user.getString("objectId")).apply();
                    myEdit.putString("accessToken",username);
                    String StudentClass  = user.getString("StudentClass");
                    myEdit.putString("classRoom",StudentClass);
                    myEdit.commit();

                    if(user.getString("AccountType").matches("admin")){
                        Intent adminIntent = new Intent(MainActivity.this, Teacher.class);
                        adminIntent.putExtra("username",user.getString("username"));
                        UX_FOR_LOGIN(true);
                        adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(adminIntent);


                    }else if(user.getString("AccountType").matches("student")){
                        Intent studentIntent = new Intent(MainActivity.this, StudentActivety.class);
                        studentIntent.putExtra("username",user.getString("username"));
                        studentIntent.putExtra("theClass",user.getString("classroom"));
                        studentIntent.putExtra("StudentName",user.getString("StudentName"));
                        studentIntent.putExtra("paidUp",user.getString("paidUp"));
                        studentIntent.putExtra("theRequired",user.getString("required"));
                        UX_FOR_LOGIN(true);
                        studentIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(studentIntent);

                    }


                }else{
                    Toast.makeText(getApplicationContext(),"تأكد من المعلومات المدخلة",Toast.LENGTH_LONG).show();
                    loginWaitLayout.setVisibility(View.GONE);
                    UX_FOR_LOGIN(true);

                    return;
                }
            }
        });



    }




}
