package com.alnoorinstitute;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alnoorinstitute.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class allPays extends AppCompatActivity {
    String ClassName;
    int limt = 10, i = 0, size = 0;
    ListView payList;
    LinearLayout allPaysWaitLayout;
    Button getMorebtn;
    boolean serverError = false;
    ArrayList<MyRow> myDataList = new ArrayList<MyRow>();
    MyRow[] allData = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pays);
        payList = (ListView) findViewById(R.id.payList);
        allPaysWaitLayout = (LinearLayout) findViewById(R.id.allPaysWaitLayout);
        allPaysWaitLayout.setVisibility(View.VISIBLE);
        getMorebtn = (Button) findViewById(R.id.getMorebtn);
        ClassName = getIntent().getStringExtra("classRoom");

        findObjects();

    }

    class MyRow {
        String StudentName, Date, number;

        MyRow(String StudentName, String Date, String number) {
            this.Date = Date;
            this.number = number;
            this.StudentName = StudentName;
        }

        public String getDate() {
            return Date;
        }

        public String getNumber() {
            return number;
        }

        public String getStudentName() {
            return StudentName;
        }
    }


    private void findObjects() {
        getMorebtn.setEnabled(false);
        payList.setEnabled(false);
        allPaysWaitLayout.setVisibility(View.VISIBLE);
        allData = new MyRow[]{};
        final ListView listView = (ListView) findViewById(R.id.payList);
        // Configure Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AllPays");
        // Query Parameters
        query.whereEqualTo("ClassRoom", ClassName);
        query.orderByAscending("StudentName");
        // Sorts the results in ascending order by the itemName field
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, final ParseException e) {
                if (e == null) {
                    size = objects.size();
                    // Adding objects into the Array
                    if (size < limt) {
                        limt = size;
                        Toast.makeText(getApplicationContext(), "NoMore!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    for (; i < limt; i++) {
                        String element = objects.get(i).getString("StudentName");
                        String number = String.valueOf(objects.get(i).getNumber("ThePaid"));
                        Date myDate = objects.get(i).getCreatedAt();
                        DateFormat df = new SimpleDateFormat("dd-mm-yyyy");
                        String Datee = df.format(myDate);
                        myDataList.add(new MyRow(element, Datee, number));
                    }
                } else {

                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    serverError = true;
                    if (i == 0) finish();
                }
                /**  allData = myDataList.toArray(new MyRow[myDataList.size()]);
                 final ArrayList<MyRow> myList = new ArrayList<MyRow>(Arrays.asList(allData));**/
                //MyAdapter adapterList
                //      = new MyAdapter(getApplicationContext(),myDataList);
                allPayAdapter adapterList = new allPayAdapter(getApplicationContext(), myDataList, true);
                allPaysWaitLayout.setVisibility(View.GONE);
                payList.setEnabled(true);
                getMorebtn.setEnabled(true);
                if (serverError) return;
                listView.setAdapter(adapterList);


            }
        });

    }

    /**
     *
     **/
    public void getMore(View view) {
        if (serverError) {
            return;
        }
        limt += 10;
        findObjects();
        payList.setStackFromBottom(true);

    }
}
