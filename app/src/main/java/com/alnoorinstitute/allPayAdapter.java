package com.alnoorinstitute;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class allPayAdapter extends BaseAdapter {
    ArrayList<allPays.MyRow> myList;
    ArrayList<Notifications_Sent.MyNotification> notifyList;
    private Context context;
    boolean isPay = true;

    allPayAdapter(Context context, ArrayList<allPays.MyRow> myList, boolean isPay) {
        this.isPay = isPay;
        this.myList = myList;
        this.context = context;
    }

    allPayAdapter(ArrayList<Notifications_Sent.MyNotification> notifyList, Context context, boolean isPay) {
        this.isPay = isPay;
        this.notifyList = notifyList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (isPay) return myList.size();
        return notifyList.size();

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recycleview_row, parent, false);
        }

//        LayoutInflater myInflater = getLayoutInflater();
        convertView = LayoutInflater.from(context).inflate(R.layout.recycleview_row, parent, false);
        if (isPay) {

            TextView name, date, money;
            name = (TextView) convertView.findViewById(R.id.studentName);
            date = (TextView) convertView.findViewById(R.id.payDate);
            money = (TextView) convertView.findViewById(R.id.money);
            name.setText(myList.get(position).getStudentName());
            date.setText(myList.get(position).getDate());
            money.setText(myList.get(position).getNumber() + " ل.س");
        } else {
            TextView title, body, notifyDate;
            title = (TextView) convertView.findViewById(R.id.studentName);
            notifyDate = (TextView) convertView.findViewById(R.id.money);
            body = (TextView) convertView.findViewById(R.id.payDate);
            title.setText(notifyList.get(position).getTitle());
            body.setText(notifyList.get(position).getBody());
            notifyDate.setText(notifyList.get(position).getNotifyDate());

        }
        return convertView;
    }
}
