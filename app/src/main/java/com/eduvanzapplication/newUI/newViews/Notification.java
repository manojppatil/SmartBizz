package com.eduvanzapplication.newUI.newViews;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eduvanzapplication.Util.Globle;
import com.eduvanzapplication.newUI.MainApplication;
import com.eduvanzapplication.R;
import com.eduvanzapplication.newUI.adapter.NotificationAdapter;
import com.eduvanzapplication.newUI.pojo.NotificationData;
import com.eduvanzapplication.newUI.VolleyCallNew;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/*
* D)Notifications API
	1)http://localhost/eduvanzApi/dashboard/getDashBoardNotifications_post
	 studentId optional (if seession is set then provide this parameter)
     139.59.32.234
* */
public class Notification extends AppCompatActivity {

    Typeface typeface;
    TextView textViewArrowDown;
    TextView aaa;
    public static ListView mListOfNotification;
    NotificationAdapter adapter;
    public static Context context;
    public static Fragment mFragment;
    String userID;
    AppCompatActivity mActivity;

//    getSupportActionBar().setTitle("Notifications");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        context = this;
        mActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("logged_id", "null");
        mListOfNotification = (ListView) findViewById(R.id.notification_listview);
        /**API CALL**/
        try {
            String url = MainApplication.mainUrl + "dashboard/getDashBoardNotifications";
            Map<String, String> params = new HashMap<String, String>();
            params.put("studentId",userID);
//            params.put("studentId","548");
            Log.e(MainApplication.TAG, "onCreate: "+params );
            if(!Globle.isNetworkAvailable(Notification.this))
            {
                Toast.makeText(Notification.this, "Please check your network connection", Toast.LENGTH_SHORT).show();

            } else {
                VolleyCallNew volleyCall = new VolleyCallNew();
                volleyCall.sendRequest(context, url, mActivity, null, "Notifications", params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getNotificationContent(JSONObject jsonDataO)
    {
        Log.e("", "getNotificationContent: "+jsonDataO );
        ArrayList<NotificationData> mDataList= new ArrayList<>();
        try {
            String status = jsonDataO.optString("status");
            String message = jsonDataO.optString("message");

            if (status.equalsIgnoreCase("1"))
            {
                JSONArray notificationArray=jsonDataO.getJSONArray("result");
                for (int i=0 ;i<notificationArray.length();i++){
                    NotificationData nData= new NotificationData();
                    JSONObject mObject= notificationArray.getJSONObject(i);
                    nData.textOfMessage=mObject.getString("notificationMessage");
                    nData.timeOfMessage=mObject.getString("notificationDateTime");
                    String isNewMessage=mObject.getString("isNew");
                    if(isNewMessage.equalsIgnoreCase("1"))
                    {
                        nData.isNew=true;
                    }else {
                        nData.isNew=false;
                    }
                    mDataList.add(nData);
                }
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
            adapter = new NotificationAdapter(context,mDataList);
            mListOfNotification.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
