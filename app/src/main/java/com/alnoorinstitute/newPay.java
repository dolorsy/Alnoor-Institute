package com.alnoorinstitute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;


public class newPay extends AppCompatActivity {
    //ParseQueryAdapter<ParseObject> studentAdabpter;
    LinearLayout newPayWaitLayout, afterChooseLayout, newPayLayout;
    EditText paid, StudentName;
    Button savePaybtn, checkStudent, backOfPay;
    //Spinner spinnerStudentName;
    ParseQuery<ParseUser> payQuery;
    int required, paidUp;
    ParseQuery<ParseUser> addToStudent;
    List<ParseObject> students;
    String ClassRoom, targetClass, inputName, studentId;
    TextView test, mainText, waitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pay);
        newPayLayout = (LinearLayout) findViewById(R.id.newPayLayout);
        newPayWaitLayout = (LinearLayout) findViewById(R.id.newPayWaitLayout);
        afterChooseLayout = (LinearLayout) findViewById(R.id.afterChooseLayout);
        afterChooseLayout.setVisibility(View.GONE);
        newPayWaitLayout.setVisibility(View.GONE);
//        newPayLayout.setVisibility(View.GONE);
        paid = (EditText) findViewById(R.id.paid);
        savePaybtn = (Button) findViewById(R.id.savePaybtn);
        checkStudent = (Button) findViewById(R.id.checkStudent);
        backOfPay = (Button) findViewById(R.id.backOfPay);
        StudentName = (EditText) findViewById(R.id.StudentName);
        //spinnerStudentName = (Spinner) findViewById(R.id.spinnerStudentName);
        ClassRoom = getIntent().getStringExtra("classRoom");
        targetClass = getIntent().getStringExtra("targetClass");
        waitText = (TextView) findViewById(R.id.waitText);
        test = (TextView) findViewById(R.id.test);
        test.setText("الصف " + ClassRoom);
        makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();


    }

    public void checkStudent(View view) {
        StudentName.setEnabled(false);
        checkStudent.setEnabled(false);
        waitText.setText("جاري التحقق ...");
        newPayWaitLayout.setVisibility(View.VISIBLE);
        inputName = StudentName.getText().toString();
        if (inputName.matches("")) {
            StudentName.setError("This Filed Can't be Empty");
            return;
        }
        payQuery = ParseUser.getQuery();
        payQuery.whereEqualTo("StudentClass", targetClass);
        payQuery.whereEqualTo("StudentName", inputName);
        payQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    studentId = user.getObjectId();
                    required = user.getInt("required");
                    paidUp = user.getInt("paidUp");
                    mainText = (TextView) findViewById(R.id.mainText);
                    mainText.setText("المطلوب منه " + String.valueOf(required) + " وسددسابقا " + String.valueOf(paidUp));
                    newPayWaitLayout.setVisibility(View.GONE);
                    afterChooseLayout.setVisibility(View.VISIBLE);
                } else {
                    makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    afterChooseLayout.setVisibility(View.GONE);
                    newPayWaitLayout.setVisibility(View.GONE);
                    StudentName.setEnabled(true);
                    checkStudent.setEnabled(true);

                    return;
                }
            }
        });

    }

    public void letsPay(View view) {
        addToStudent = ParseUser.getQuery();
        savePaybtn.setEnabled(false);
        backOfPay.setEnabled(false);
        paid.setEnabled(false);
        waitText.setText("جاري التسديد ...");
        newPayWaitLayout.setVisibility(View.VISIBLE);


        addToStudent.getInBackground(studentId, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    object.put("fp", "true");
                    object.put("paidUp", paidUp + Integer.valueOf(paid.getText().toString()));
                    object.saveInBackground();
                    makeText(getApplicationContext(), "تم الدفع بنجاح", Toast.LENGTH_LONG).show();

                } else {
                    makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

            }
        });


        finish();


    }

    public void backOfPay(View view) {
        StudentName.setText("");
        StudentName.setEnabled(true);
        checkStudent.setEnabled(true);
        afterChooseLayout.setVisibility(View.GONE);

    }
}