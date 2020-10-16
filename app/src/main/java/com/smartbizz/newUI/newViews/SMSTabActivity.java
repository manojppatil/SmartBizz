package com.smartbizz.newUI.newViews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.smartbizz.R;
import com.smartbizz.Util.Constants;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.newUI.adapter.SMSTabAdapter;
import com.smartbizz.newUI.adapter.TabAdapter;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.pojo.Category;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.smartbizz.Util.DialogUtil.dismissProgressDialog;

public class SMSTabActivity extends BaseActivity {

    private Toolbar toolbar;
    private TabLayout tab;
    private ViewPager viewPager;
    private TextView tvRegisterSMSService,tvNumber;
    ClickableSpan clickableSpanTerms;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_sms_tab);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar = findViewById(R.id.toolbar);

        tvRegisterSMSService = findViewById(R.id.tvRegisterSMSService);
//        tvNumber = findViewById(R.id.tvNumber);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SMS Promotion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.white));
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        viewPager = findViewById(R.id.viewPager);
        tab = findViewById(R.id.tabLayout);
        showTabs();

        tvRegisterSMSService.setOnClickListener(v -> {
            Intent intent = new Intent(activity, WebViewSMSService.class);
            startActivity(intent);
        });

//        tvNumber.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            intent.setData(Uri.parse("+91 95952 41238"));
//            startActivity(intent);
//        });
    }

    private void setupViewPager() {

        tab.addTab(tab.newTab().setText("SMS Sender"));
        tab.addTab(tab.newTab().setText("Create Group"));
        tab.addTab(tab.newTab().setText("Sender Id"));
        tab.addTab(tab.newTab().setText("SMS Report"));
        tab.setTabGravity(TabLayout.GRAVITY_FILL);

        final SMSTabAdapter adapter = new SMSTabAdapter(this,getSupportFragmentManager(), tab.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void showTabs(){
        DialogUtil.showProgressDialog(activity);
        NetworkManager.getInstance(activity).getSenderId(activity, response -> {
            dismissProgressDialog();
            if (response.isSuccess()) {
                viewPager.setVisibility(View.VISIBLE);
//                tvRegisterSMSService.setVisibility(View.GONE);
//                tvNumber.setVisibility(View.GONE);

                String senderid = null, api_key = null;
                try {
                    senderid = String.valueOf(((JSONObject)response.getResponse().getJSONArray("result").get(0)).get("sender_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                if(senderid.equalsIgnoreCase("null"))
//                {
//                    //Hey, Sachin looks you are not registered with SMS service, please click here for self activation or call on +91 95952 41238 for more help
////                    tvRegisterSMSService.setVisibility(View.VISIBLE);
////                    tvNumber.setVisibility(View.VISIBLE);
////                    viewPager.setVisibility(View.GONE);
////                    tvRegisterSMSService.setPaintFlags(tvRegisterSMSService.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//
//                    String message1 = "click here";
//
//                    SpannableString spannableString = new SpannableString(message1);
//
//                    spannableString.setSpan(spannableString, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                    String message = "Hey, " + PreferenceManager.getString(activity, Constants.PrefKeys.FIRST_NAME)+ " looks you are not registered with SMS service please," +spannableString +" for self activation";
//                    tvRegisterSMSService.setText(message);
//
////                    String message1 = "call on +91 95952 41238 for more help";
////
////                    SpannableString spannableString = new SpannableString(message1);
////                    clickableSpanTerms = new ClickableSpan() {
////                        @Override
////                        public void onClick(@NonNull View textView) {
////                            Intent intent = new Intent(Intent.ACTION_DIAL);
////                            intent.setData(Uri.parse("+91 95952 41238"));
////                            startActivity(intent);
////                        }
////                    };
////
////                    spannableString.setSpan(clickableSpanTerms, 8, spannableString.length() - 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////                    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 8, spannableString.length() - 14,
////                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////
////                    tvNumber.setText(spannableString);
////                    tvRegisterSMSService.setText("Hey, " + PreferenceManager.getString(activity, Constants.PrefKeys.FIRST_NAME)+ " looks you are not registered with SMS service, please click here for self activation or call on +91 95952 41238 for more help");
//                    return;
//                }

                try {
                    api_key = String.valueOf(((JSONObject)response.getResponse().getJSONArray("result").get(0)).get("api_key"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    PreferenceManager.saveString(activity, Constants.PrefKeys.SENDERID, senderid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    PreferenceManager.saveString(activity, Constants.PrefKeys.API_KEY, api_key);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                setupViewPager();

            }else{
                makeToast(response.getMessage());
                setupViewPager();
//                viewPager.setVisibility(View.GONE);
//                tvRegisterSMSService.setVisibility(View.VISIBLE);
//                tvRegisterSMSService.setPaintFlags(tvRegisterSMSService.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                tvRegisterSMSService.setText("Hey, " + PreferenceManager.getString(activity, Constants.PrefKeys.FIRST_NAME)+ " Please click here to register for our SMS Service");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
