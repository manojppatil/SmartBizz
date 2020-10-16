package com.smartbizz.newUI.newViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.smartbizz.R;
import com.smartbizz.Util.CommonUtil;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.newUI.adapter.SmsTemplateAdapter;
import com.smartbizz.newUI.adapter.TabAdapter;
import com.smartbizz.newUI.adapter.TemplateMsgAdapter;
import com.smartbizz.newUI.adapter.TemplateTabAdapter;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.pojo.Category;
import com.smartbizz.newUI.pojo.SMSTemplates;
import com.smartbizz.newUI.pojo.Templatemsg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TemplateActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    List<SMSTemplates> smsTemplatesList = new ArrayList<>();
    TemplateTabAdapter adapter;
    private TabLayout tab;
    private ViewPager viewPager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SMS Templates");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.white));
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        tab = findViewById(R.id.temptabLayout);
        viewPager = findViewById(R.id.tempviewPager);

        getTemplateCatgList();
    }

    public void getTemplateCatgList() {
        DialogUtil.showProgressDialog(TemplateActivity.this);
        smsTemplatesList.clear();
        NetworkManager.getInstance(TemplateActivity.this).getTemplatesCatgList(TemplateActivity.this, "", response -> {
            if (response.isSuccess()) {
                JSONObject jsonObject = response.getResponse();
                if (jsonObject != null) {
                    JSONObject resultObj = jsonObject.optJSONObject(ApiConstants.Keys.RESULT);
                    if (resultObj != null) {

                        JSONArray sms_category = resultObj.optJSONArray("sms_category");

                        if (sms_category != null && sms_category.length() > 0) {
                            int size = sms_category.length();
                            SMSTemplates smsTemplates1 = new SMSTemplates("All", "", "", "");

                            smsTemplatesList.add(smsTemplates1);
                            for (int i = 0; i < size; i++) {
                                JSONObject categoryJson = sms_category.optJSONObject(i);
                                if (categoryJson != null) {
                                    SMSTemplates templates = new SMSTemplates(sms_category.optJSONObject(i));
                                    smsTemplatesList.add(templates);
                                }
                            }
                        }

                        setUpViewPager();
                    }
                }
            } else {
                makeToast(response.getMessage());
            }
            dismissProgressBar();
        });
    }

    private void setUpViewPager() {
        for (int k = 0; k < smsTemplatesList.size(); k++) {
            tab.addTab(tab.newTab().setText("" + smsTemplatesList.get(k).getCategory().toString()));
        }

        adapter = new TemplateTabAdapter(getSupportFragmentManager(), tab.getTabCount(), smsTemplatesList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tabs) {
                //do stuff here
                viewPager.setCurrentItem(tabs.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        adapter.notifyDataSetChanged();
    }


//    public void getTemplateMsgList() {
//        DialogUtil.showProgressDialog(TemplateActivity.this);
//        smsTemplates.clear();
//        NetworkManager.getInstance(TemplateActivity.this).getTemplatesCatgList(TemplateActivity.this, response -> {
//            if (response.isSuccess()) {
//                JSONObject jsonObject = response.getResponse();
//                if (jsonObject != null) {
//                    JSONObject resultObj = jsonObject.optJSONObject(ApiConstants.Keys.RESULT);
//                    if (resultObj != null) {
//
//                        JSONArray sms_category = resultObj.optJSONArray("sms_category");
//
//                        if (sms_category != null && sms_category.length() > 0) {
//                            int size = sms_category.length();
//                            for (int i = 0; i < size; i++) {
//                                JSONObject categoryJson = sms_category.optJSONObject(i);
//                                if (categoryJson != null) {
//                                    Templatemsg templatemsg = new Templatemsg(sms_category.optJSONObject(i));
//                                    templatemsgs.add(templatemsg);
//                                }
//                            }
//                        }
//
//                        setUpMsgRecyclerView();
//                    }
//                }
//            } else {
//                makeToast(response.getMessage());
//            }
//            dismissProgressBar();
//        });
//    }
//
//    private void setUpMsgRecyclerView() {
//        templateMsgAdapter = new TemplateMsgAdapter(TemplateActivity.this, templatemsgs);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TemplateActivity.this, LinearLayoutManager.VERTICAL, false);
//        template_msg.setLayoutManager(mLayoutManager);
//        template_msg.setItemAnimator(new DefaultItemAnimator());
//        template_msg.setAdapter(templateMsgAdapter);
//    }

    private void dismissProgressBar() {
        new Handler().postDelayed(() -> DialogUtil.dismissProgressDialog(), 1000);
    }

    public void makeToast(String message) {
        CommonUtil.makeToast(TemplateActivity.this, message);
    }
}