package com.smartbizz.newUI.newViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.smartbizz.R;
import com.smartbizz.Util.CommonUtil;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.newUI.adapter.SmsTemplateAdapter;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.pojo.SMSTemplates;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SmsTemplateActivity extends AppCompatActivity {
    RecyclerView template_recycler;
    List<SMSTemplates> smsTemplates = new ArrayList<>();
    private SmsTemplateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_template);
        template_recycler = findViewById(R.id.template_catg_recycler);
        getTemplateCatgList();
    }

    public void getTemplateCatgList() {
        DialogUtil.showProgressDialog(SmsTemplateActivity.this);
        smsTemplates.clear();
        NetworkManager.getInstance(SmsTemplateActivity.this).getTemplatesCatgList(SmsTemplateActivity.this, "",response -> {
            if (response.isSuccess()) {
                JSONObject jsonObject = response.getResponse();
                if (jsonObject != null) {
                    JSONObject resultObj = jsonObject.optJSONObject(ApiConstants.Keys.RESULT);
                    if (resultObj != null) {

                        JSONArray sms_category = resultObj.optJSONArray("sms_category");

                        if (sms_category != null && sms_category.length() > 0) {
                            int size = sms_category.length();
                            for (int i = 0; i < size; i++) {
                                JSONObject categoryJson = sms_category.optJSONObject(i);
                                if (categoryJson != null) {
                                    SMSTemplates templates = new SMSTemplates(sms_category.optJSONObject(i));
                                    smsTemplates.add(templates);
                                }
                            }
                        }

                        setUpRecyclerView();
                    }
                }
            } else {
                makeToast(response.getMessage());
            }
            dismissProgressBar();
        });
    }

    private void setUpRecyclerView() {
        adapter = new SmsTemplateAdapter(SmsTemplateActivity.this, smsTemplates, "LIST");
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        template_recycler.setLayoutManager(mLayoutManager);
        template_recycler.setItemAnimator(new DefaultItemAnimator());
        template_recycler.setAdapter(adapter);

    }

    private void dismissProgressBar() {
        new Handler().postDelayed(() -> DialogUtil.dismissProgressDialog(), 1000);
    }

    public void makeToast(String message) {
        CommonUtil.makeToast(SmsTemplateActivity.this, message);
    }
}