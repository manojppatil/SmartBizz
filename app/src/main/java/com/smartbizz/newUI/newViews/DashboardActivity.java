package com.smartbizz.newUI.newViews;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.smartbizz.Util.Constants;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.newUI.network.ApiConstants;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartbizz.BuildConfig;
import com.smartbizz.CustomTypefaceSpan;
import com.smartbizz.MainActivity;
import com.smartbizz.R;
import com.smartbizz.newUI.SharedPref;
import com.smartbizz.newUI.fragments.DashboardFragmentNew;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;

import static com.smartbizz.newUI.newViews.EditImageActivity.FILE_PROVIDER_AUTHORITY;

public class DashboardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static NavigationView navigationView;
    public static DrawerLayout drawer;
    public static Context context;
    public static TextView textViewName, textView_mobileNo, textViewEmail;
    FrameLayout frameLayoutDashboard;
    LinearLayout linearLayoutUserDetail, editProfile;
    static String student_id = "", appInstallationTimeStamp = "", userFirst = "", userLast = "", userEmail = "", userPic = "",
                  isDataSync = "false";
    public static AppCompatActivity mActivity;
    public static String userMobileNo ="";
    SharedPreferences sharedPreferences;
    public int GET_MY_PERMISSION = 1, permission;
    public static de.hdodenhof.circleimageview.CircleImageView ivUserPic;
    public static Bitmap bitmapLogo = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_dashboard);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            context = getApplicationContext();
            toolbar.setBackgroundColor(Color.parseColor("#0ABAB5"));
            mActivity = this;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//---HIDE STATUS BAR

            try {
                sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
                userFirst = sharedPreferences.getString("name", "");
                userEmail = sharedPreferences.getString("email", "");
                userMobileNo = sharedPreferences.getString("mobile_no", "");
                MainActivity.auth_token = sharedPreferences.getString("auth_token", "");
                student_id = sharedPreferences.getString("student_id", "");
                userPic = sharedPreferences.getString("user_img", "");
                appInstallationTimeStamp = sharedPreferences.getString("appInstallationTimeStamp", "null");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            //**To change the hamburger color on dashboard **/
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

            navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View header = navigationView.getHeaderView(0);

            editProfile = header.findViewById(R.id.linearLayout_userdetail_dashboard);
            editProfile.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), EditProfile.class)));
            hideMenuOptions();

            textViewName = header.findViewById(R.id.textView_name);
            textViewName.setText(PreferenceManager.getString(context, Constants.PrefKeys.FIRST_NAME));
            textView_mobileNo = header.findViewById(R.id.textView_mobileNo);
            textView_mobileNo.setText(PreferenceManager.getString(context, Constants.PrefKeys.MOBILE));
            textViewEmail = header.findViewById(R.id.textView_emailID);
            textViewEmail.setText(PreferenceManager.getString(context, Constants.PrefKeys.EMAIL));

            ivUserPic = header.findViewById(R.id.ivUserPic);
                Picasso.with(context)
                        .load(ApiConstants.BASE_URL + "/" + PreferenceManager.getString(context, Constants.PrefKeys.LOGO))
                        .placeholder(getResources().getDrawable(R.drawable.ic_building))
                        .into(ivUserPic);

            linearLayoutUserDetail = header.findViewById(R.id.linearLayout_userdetail_dashboard);

            frameLayoutDashboard = findViewById(R.id.framelayout_dashboard);
            Menu m = navigationView.getMenu();
            for (int i = 0; i < m.size(); i++) {
                MenuItem mi = m.getItem(i);

                //for applying a font to subMenu ...
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0) {
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem);
                    }
                }

                //the method we have create in activity
                applyFontToMenuItem(mi);

            }

            getSupportFragmentManager().beginTransaction().add(R.id.framelayout_dashboard, new DashboardFragmentNew()).commit();

            if (Build.VERSION.SDK_INT >= 23) {

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DashboardActivity.this,
                            new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA},
                            GET_MY_PERMISSION);
                } else {
                }
            }

//            try {
////                PreferenceManager.saveString(activity, Constants.PrefKeys.LOGGED_ID, "8");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        } catch (Exception e) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_promotion) {
            if(askRequiredPermissions()) {
                Intent intent = new Intent(context, PostCardTabActivity.class);
                startActivity(intent);
            }
        }
//        else if (id == R.id.nav_designer) {
////            Intent intent = new Intent(context, DesignActivity.class);
////            startActivity(intent);
//        }
        else if (id == R.id.nav_whatsappGrp) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
//            String url = "https://chat.whatsapp.com/KBlcPlJb78cAonD6CeWZCx";
            String url = PreferenceManager.getString(context, Constants.PrefKeys.WHATSAPPLINK);
            browserIntent.setData(Uri.parse(url));
            startActivity(browserIntent);

        }
//        else if (id == R.id.nav_balanceData) {
//            if(askRequiredPermissions()) {
//                Intent intent = new Intent(context, SMSBalanceActivity.class);
//                startActivity(intent);
//            }
//        } else if (id == R.id.nav_smsReport) {
//            if(askRequiredPermissions()) {
//                Intent intent = new Intent(context, SMSReportActivity.class);
//                startActivity(intent);
//            }
//        }
        else if (id == R.id.nav_myproject) {
            if(askRequiredPermissions()) {
                Intent intent = new Intent(context, MyProjectActivity.class);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_shareappp) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                String shareMessage = "\nYou have been refered to Digital Marketing.\n\n\n";
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        } else if (id == R.id.nav_rateappp) {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }
        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.are_you_sure_you_waqnt_to_exit)
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id1) -> {

                        SharedPref sharedPref = new SharedPref();
                        sharedPref.clearSharedPreference(DashboardActivity.this);
                        PreferenceManager.saveBoolean(activity, Constants.AppStage.MOBILE_VERIFIED, false);

                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", (dialog, id12) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (id == R.id.nav_more) {
//            drawer.openDrawer(GravityCompat.START);
//        } else {
//            drawer.closeDrawer(GravityCompat.START);
//        }
        return true;
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/sourcesanspro_italic.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void hideMenuOptions() {
        Menu nav_Menu = navigationView.getMenu();
//        nav_Menu.findItem(R.id.nav_blog).setVisible(false);
//        nav_Menu.findItem(R.id.nav_logout).setVisible(false);
    }

    private void showMenuOptions() {
        Menu nav_Menu = navigationView.getMenu();
//        nav_Menu.findItem(R.id.nav_more).setVisible(false);
//        nav_Menu.findItem(R.id.nav_blog).setVisible(true);
//        if (sharedPref.getLoginDone(context)) {
//            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1:
                if (grantResults.length <= 0) {
                }
                else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED ) {
                    //granted
                } else {
                    //not granted
//                    Log.e(MainApplication.TAG, "not granted: Dashboard " + grantResults[0]);
                    {
                        Snackbar.make(
                                findViewById(R.id.framelayout_dashboard),
                                R.string.permission_denied_explanation,
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.settings, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Build intent that displays the App settings screen.
                                        Intent intent = new Intent();
                                        intent.setAction(
                                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package",
                                                BuildConfig.APPLICATION_ID, null);
                                        intent.setData(uri);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }
                }
                break;
        }

    }

}
