package com.smartbizz.newUI.newViews;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartbizz.newUI.MainApplication;
import com.smartbizz.R;

import java.util.List;

public class ContactUs extends AppCompatActivity {

    TextView  textView1, textView2, textView3, textView4, textView5, textView6,
            textView7, textView8;
    MainApplication mainApplication;
    Context context;
    ImageView imageViewOfficeLocation;
    LinearLayout linearLayoutNumber, linearLayoutEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        try {
            context = this;
            mainApplication = new MainApplication();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.title_contact_us);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
            toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

            textView1 = (TextView) findViewById(R.id.textview1_contactus);
            mainApplication.applyTypeface(textView1, context);
            textView2 = (TextView) findViewById(R.id.textview2_contactus);
            mainApplication.applyTypeface(textView2, context);
            textView3 = (TextView) findViewById(R.id.textview3_contactus);
            mainApplication.applyTypeface(textView3, context);
            textView4 = (TextView) findViewById(R.id.textview4_contactus);
            mainApplication.applyTypeface(textView4, context);
            textView5 = (TextView) findViewById(R.id.textview5_contactus);
            mainApplication.applyTypeface(textView5, context);
            textView6 = (TextView) findViewById(R.id.textview6_contactus);
            mainApplication.applyTypeface(textView6, context);
            textView7 = (TextView) findViewById(R.id.textview7_contactus);
            mainApplication.applyTypeface(textView7, context);
            textView8 = (TextView) findViewById(R.id.textview8_contactus);
            mainApplication.applyTypeface(textView8, context);
            imageViewOfficeLocation = (ImageView) findViewById(R.id.imageView_officelocation);
            linearLayoutEmail = (LinearLayout) findViewById(R.id.linearLayout_email_contactus);
            linearLayoutNumber = (LinearLayout) findViewById(R.id.linearLayout_number_contactus);

            imageViewOfficeLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String map = "";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                    context.startActivity(intent);

                }
            });

            linearLayoutNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(getString(R.string.tel)));
                    startActivity(intent);
                }
            });

            linearLayoutEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareToGMail(new String[]{"shreesamarthservices@gmail.com"}, "", "");
                }
            });
        } catch (Exception e) {


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void shareToGMail(String[] email, String subject, String content) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_TEXT, content);
            final PackageManager pm = context.getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            ResolveInfo best = null;
            for(final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                    best = info;
            if (best != null)
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            context.startActivity(emailIntent);

        } catch (Exception e) {


        }
    }
}
