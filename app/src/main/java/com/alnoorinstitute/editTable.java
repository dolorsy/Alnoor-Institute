package com.alnoorinstitute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class editTable extends AppCompatActivity {
    String[] days = {"sunday","monday","tusday","wednesday","Thursday"};
    char[] chars;
    EditText[][] myTableIds = new EditText[5][3];
    TextView editTableWaitText;
    String[] strArray ;
    LinearLayout editTableWaitLayout,editTableMainLayout;

    int i=0,j=0;
    TextView classRoom;
String targetClass,temp="1",objectId;
    String[][] subjects;
    ParseQuery<ParseObject> tableQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_table);
        editTableWaitLayout = (LinearLayout) findViewById(R.id.editTableWaitLayout);
        editTableMainLayout = (LinearLayout) findViewById(R.id.editTableMainLayout);
        editTableMainLayout.setVisibility(View.GONE);
        strArray = new String[16];
         classRoom = (TextView) findViewById(R.id.classRoom);
        tableQuery = ParseQuery.getQuery("WeekTable");
         editTableWaitText = (TextView) findViewById(R.id.editTableWaitText);
        targetClass = getIntent().getStringExtra("targetClass");
        classRoom.setText("الصف: " + getIntent().getStringExtra("classRoom"));
                myTableIds = new EditText[][]  {{(EditText)findViewById(R.id.sundayfirst),(EditText)findViewById(R.id.sundaysecond),(EditText)findViewById(R.id.sundaythird)},
                {(EditText)findViewById(R.id.mondayfirst),(EditText)findViewById(R.id.mondaysecond),(EditText)findViewById(R.id.mondaythird)},
                {(EditText)findViewById(R.id.tusdayfirst),(EditText)findViewById(R.id.tusdaysecond),(EditText)findViewById(R.id.tusdaythird)},
                {(EditText)findViewById(R.id.wednesdayfirst),(EditText)findViewById(R.id.wednesdaysecond),(EditText)findViewById(R.id.wednesdaythird)},
                {(EditText)findViewById(R.id.thursdayfirst),(EditText)findViewById(R.id.thursdaysecond),(EditText)findViewById(R.id.thursdaythird)}};

        fillTheArray();

    }


    public void saveTable(View view) {
        editTableWaitText.setText("جاري الحفظ ...");
        editTableWaitLayout.setVisibility(View.VISIBLE);
        editTableMainLayout.setVisibility(View.GONE);

        boolean isEmpty = false;
        //code To save Change
        for (int i = 0 ; i < 5; i++){
            for (int j = 0 ; j < 3; j++){

                if(myTableIds[i][j].getText().toString().matches("")){
                    myTableIds[i][j].setError("لا يمكن تركه فارغاً");
                isEmpty = true;
                }

            }

        }
        if(isEmpty)return;

        tableQuery.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject edit, ParseException e) {

                if(e == null){
                    for (int i = 0 ; i < 5; i++){
                        temp = "";
                        for (int j = 0 ; j < 3; j++)
                        {
                            temp += "@" + myTableIds[i][j].getText().toString();
                        }
                        edit.put(days[i],temp);

                    }
                    edit.saveInBackground();
                    Toast.makeText(getApplicationContext(),"تم الحفظ بنجاح",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Error in save the new table",Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    public void fillTheArray(){
        //not finished
        tableQuery.whereEqualTo("classRoom",targetClass);
        tableQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject table, ParseException e) {
                if(e == null){
                    objectId = table.getObjectId();
                    for (String day : days) temp += table.getString(day);
                    strArray = temp.split("@");
                    /****/
                    int j=0,k=0;
                    for (int i =1; i<strArray.length ;i++){
                        if(k==3){
                            j++;
                            k=0;
                        }
                        if(j == 6){
                            break;
                        }
                        myTableIds[j][k].setText(strArray[i]);
                        k++;

                    }
                    editTableMainLayout.setVisibility(View.VISIBLE);
                    editTableWaitLayout.setVisibility(View.GONE);

                    /***/



                }else{
                    Log.d("Error",temp);
                    Toast.makeText(getApplicationContext(),"Error with Server!!"  ,Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


    }

}
