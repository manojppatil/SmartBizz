package com.smartbizz.newUI.newViews;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartbizz.newUI.MainApplication;
import com.smartbizz.R;
import com.smartbizz.newUI.SharedPref;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import static com.smartbizz.newUI.MainApplication.TAG;

public class BannerActivity extends AppCompatActivity {

    TextView textViewBannerDetail, textViewBannerTitle;
    ImageView imageViewBanner;
    Button buttonCheckEligiblity, buttonApplyNow, buttonContinue;
    MainApplication mainApplication;
    static Context context;
    String image="", id="", title="", fromDeal="", userId="";
    AppCompatActivity mActivity;
    static String borrower = null, coBorrower = null, coBorrowerDocument = null,
            eligibility = null, borrowerDocument = null, signDocument = null,
            kyc = null, profileDashboardStats = null;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_banner);
            context = this;
            mActivity = this;
            mainApplication = new MainApplication();
            sharedPref = new SharedPref();

            try {
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
                userId = sharedPreferences.getString("logged_id", "");

                Bundle bundle = getIntent().getExtras();
                image = bundle.getString("bannerImage");
                id = bundle.getString("bannerID");
                title = bundle.getString("bannerTitle");
                fromDeal = bundle.getString("from_deal","0");
            }catch (Exception e){
                e.printStackTrace();
            }

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.special_offers);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
            toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

            imageViewBanner = (ImageView) findViewById(R.id.imageView_bannerimage_1);

            textViewBannerDetail = (TextView) findViewById(R.id.textView_bannerDetail);
            mainApplication.applyTypeface(textViewBannerDetail, context);

            textViewBannerTitle = (TextView) findViewById(R.id.textView_bannerTitle);
            mainApplication.applyTypeface(textViewBannerTitle, context);

            buttonCheckEligiblity = (Button) findViewById(R.id.button_bannerdetail_checkeligiblity);
            mainApplication.applyTypeface(buttonCheckEligiblity, context);

            buttonApplyNow = (Button) findViewById(R.id.button_bannerdetail_applynow);
            mainApplication.applyTypeface(buttonApplyNow, context);

            buttonContinue= (Button) findViewById(R.id.button_bannerdetail_continue);
            mainApplication.applyTypeface(buttonContinue, context);

            if (sharedPref.getLoginDone(context)) {
                buttonApplyNow.setVisibility(View.VISIBLE);
                buttonCheckEligiblity.setVisibility(View.GONE);
            }else {

            }

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


    public void setBannerDetail(JSONObject jsonData) {
        try {
            Log.e(MainApplication.TAG, "setBannerDetail: "+ jsonData);
            String status = jsonData.optString("status");
            String message = jsonData.optString("message");

            if (status.equalsIgnoreCase("1")) {

                JSONObject jsonObject = jsonData.getJSONObject("result");

                JSONObject jsonObject1 = jsonObject.getJSONObject("banner");

                textViewBannerTitle.setText(jsonObject1.getString("title"));

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    textViewBannerDetail.setText(Html.fromHtml(jsonObject1.getString("description"),Html.FROM_HTML_MODE_LEGACY));
                } else {
                    textViewBannerDetail.setText(Html.fromHtml(jsonObject1.getString("description")));
                }

                String image = jsonObject1.getString("image");
                Picasso.with(context).load(image).placeholder(context.getResources().getDrawable(R.drawable.bannersplaceholder)).into(imageViewBanner);

            }else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {


        }
    }

    /** RESPONSE OF API CALL DEAL DETAILS */
    public void setDealDetail(JSONObject jsonData) {
        try {
            Log.e(MainApplication.TAG, "setBannerDetail: "+ jsonData);
            String status = jsonData.optString("status");
            String message = jsonData.optString("message");

            if (status.equalsIgnoreCase("1")) {

                JSONObject jsonObject = jsonData.getJSONObject("result");

                JSONObject jsonObject1 = jsonObject.getJSONObject("deal");

                textViewBannerTitle.setText(jsonObject1.getString("title"));

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    textViewBannerDetail.setText(Html.fromHtml(jsonObject1.getString("description"),Html.FROM_HTML_MODE_LEGACY));
                } else {
                    textViewBannerDetail.setText(Html.fromHtml(jsonObject1.getString("description")));
                }

                String image = jsonObject1.getString("image");
                Picasso.with(context).load(image).placeholder(context.getResources().getDrawable(R.drawable.bannersplaceholder)).into(imageViewBanner);

            }else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {


        }
    }

    /** RESPONSE OF API CALL DASHBOARD STATUS */
    public void setProfileDashbBoardStatus(JSONObject jsonDataO) {
        Log.e(TAG, "setProfileDashbBoardStatus: " + jsonDataO);
        try {
            if (jsonDataO.getInt("status") == 1) {

                JSONObject mObject = jsonDataO.optJSONObject("result");
                borrower = mObject.getString("borrower");
                coBorrower = mObject.getString("coBorrower");
                coBorrowerDocument = mObject.getString("coBorrowerDocument");
                borrowerDocument = mObject.getString("borrowerDocument");
                signDocument = mObject.getString("signDocument");
                kyc = mObject.getString("kyc");
                profileDashboardStats = mObject.getString("profileDashboardStats");
                eligibility = mObject.getString("eligibility");

                SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("signed_appstatus", signDocument);
                editor.apply();
                editor.commit();

                if(borrower.equalsIgnoreCase("1")){
                    buttonContinue.setVisibility(View.VISIBLE);
                    buttonApplyNow.setVisibility(View.GONE);
                    buttonCheckEligiblity.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {


        }

    }

}
