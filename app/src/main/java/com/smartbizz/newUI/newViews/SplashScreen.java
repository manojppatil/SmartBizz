package com.smartbizz.newUI.newViews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartbizz.R;
import com.smartbizz.Util.Constants;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.newUI.SharedPref;


public class SplashScreen extends BaseActivity {
    View view1;
    public TextView textViewCustomer;
    Thread splashTread;
    public RelativeLayout relativeLayoutCustomer;
    public String checkOTPDone = "", checkForImageSlider = "";
    SharedPref sharedPref = new SharedPref();
    static Context context;
    int policyAgreementStatus = 0;
    AppCompatActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            view1 = getLayoutInflater().inflate(R.layout.activity_splash_screen_customer, null);
            setContentView(view1);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//---HIDE STATUS BAR
            context = this;
            mActivity = this;

            SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
            checkOTPDone = sharedPreferences.getString("otp_done", "0");
            checkForImageSlider = sharedPreferences.getString("checkForImageSlider", "0");

            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = getWindow().getDecorView();
                // Hide Status Bar.
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
            textViewCustomer = view1.findViewById(R.id.splash_text);
            relativeLayoutCustomer = view1.findViewById(R.id.rel_lay);

            StartAnimationsCustomer();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void StartAnimationsCustomer() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        relativeLayoutCustomer.clearAnimation();
        relativeLayoutCustomer.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
//        imageViewCustomer.clearAnimation();
//        imageViewCustomer.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 4000) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = null;

                    if (!PreferenceManager.getBoolean(activity, Constants.AppStage.MOBILE_VERIFIED)) {
                        intent = new Intent(activity, LoginActivity.class);
                    }else{
                        intent = new Intent(activity, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    }
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashScreen.this.finish();
                }
            }
        };
        splashTread.start();
    }

}
