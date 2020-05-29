package com.alnoorinstitute;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StudentTable extends AppCompatActivity {
    TextView weekTableTiltie,tableLastUpdate;
    SharedPreferences toStoreLastUpdateDate;
    SharedPreferences.Editor myEdit;
    boolean wihOutInternet = true;
    TextView[] theTable;
    LinearLayout theTopLayer,studentTableMainLayout;
    String[] days = {"sunday","monday","tusday","wednesday","Thursday"},subjects;
    ParseQuery<ParseObject> stdTableQuery;
String ClassRoom,theClass,temp="",atDay="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_table);
        toStoreLastUpdateDate = getSharedPreferences("alnoorPref", Context.MODE_PRIVATE);
        theTopLayer = (LinearLayout) findViewById(R.id.theTopLayer);
        tableLastUpdate = (TextView) findViewById(R.id.tableLastUpdate);
        studentTableMainLayout = (LinearLayout) findViewById(R.id.studentTableMainLayout);
        studentTableMainLayout.setVisibility(View.GONE);
        theTable = new TextView[] {(TextView) findViewById(R.id.stdTableSunday),
                (TextView) findViewById(R.id.stdTableMonday),
                (TextView) findViewById(R.id.stdTableTuesday),
                (TextView) findViewById(R.id.stdTableWednesday),
                (TextView) findViewById(R.id.stdTableThursday)};
        ClassRoom = getIntent().getStringExtra("ClassRoom");
        theClass = getIntent().getStringExtra("theClass");
        weekTableTiltie = (TextView) findViewById(R.id.weekTableTitle);
        weekTableTiltie.setText("جدول الحصص الأسبوعية للصف ال" + theClass);
        stdTableQuery = ParseQuery.getQuery("WeekTable");
        getTable();


    }

    public void getTable(){

        stdTableQuery.whereEqualTo("classRoom",ClassRoom);
        stdTableQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject table, ParseException e) {
                if (e == null) {
                    String lastUpdate = "آخر تحديث " + String.valueOf(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));
                    tableLastUpdate.setText(lastUpdate);
                    myEdit = toStoreLastUpdateDate.edit();
                    myEdit.putString("lasUpdate",lastUpdate);

                    for (int i = 0; i < 5; i++) {
                        temp += table.getString(days[i]);
                    }
                    refctor();
                    myEdit.putString("table",temp);
                    studentTableMainLayout.setVisibility(View.VISIBLE);
                    theTopLayer.setVisibility(View.GONE);
                    myEdit.commit();


                } else {

                    Toast.makeText(getApplicationContext(),"حدثت مشكلة في جلب البيانات، هناك صورة في المعرض!",Toast.LENGTH_LONG).show();
                    finish();


                }
            }
        });

    }
    public void refctor(){

        subjects = temp.split("@");
        int k = 0;
        atDay = "   " + subjects[1];
        for (int i = 2; i < subjects.length; i++) {

            atDay += ("     " + subjects[i]);
            if (i % 3 == 0) {
                theTable[k].setText(atDay);
                if (i == 15) break;
                atDay = "   " + subjects[++i];
                //i++;
                k++;
            }
        }
    }


}
