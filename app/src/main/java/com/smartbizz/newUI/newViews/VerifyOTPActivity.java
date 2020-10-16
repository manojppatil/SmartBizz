package com.smartbizz.newUI.newViews;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.smartbizz.R;
import com.smartbizz.Util.Constants;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.Util.GenericTextWatcher;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.view.OTPPinEntry;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyOTPActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvEdit, tvResend, tvTimer;
    private OTPPinEntry editTextPinEntry;
    private CountDownTimer countDownTimer;
    private final int TIMER_SMS = 60000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        editTextPinEntry = findViewById(R.id.editTextPinEntry);
        tvEdit = findViewById(R.id.tvEdit);
        tvTimer = findViewById(R.id.tvTimer);
        tvResend = findViewById(R.id.tvResend);
        tvEdit.setText(Html.fromHtml(getString(R.string.edit_underline)));
        String mobileNumber = PreferenceManager.getString(activity, Constants.PrefKeys.MOBILE);
        TextView tvSubTitle = findViewById(R.id.tvSubTitle);
        tvSubTitle.setText(getString(R.string.otp_sent_to, mobileNumber));
        registerListeners();
        showTimer();

        editTextPinEntry.addTextChangedListener(new GenericTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 6) {
                    verifyOTP(s.toString());
                }
            }
        });

    }

    private void registerListeners() {
        tvResend.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvEdit:
                startActivity(new Intent(activity, LoginActivity.class));
                finish();
                break;
            case R.id.tvResend:
                resendOTP();
                break;
        }
    }

    private void showTimer() {
        tvResend.setVisibility(View.GONE);
        tvTimer.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(TIMER_SMS, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText((millisUntilFinished / 1000) + "");
            }

            public void onFinish() {
                enableResend();
            }

        }.start();
    }

    private void enableResend() {
        tvTimer.setVisibility(View.GONE);
        tvResend.setVisibility(View.VISIBLE);
    }

    private void removeTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void verifyOTP(String otp) {
        DialogUtil.showProgressDialog(activity);
        NetworkManager.getInstance(activity).verifyOTP(activity, otp, response -> {
            DialogUtil.dismissProgressDialog();
            if (response.getStatus() == 1) {
                JSONObject jsonObject = response.getResponse();
                if (jsonObject == null)
                    return;
                try {
                    JSONObject result = jsonObject.optJSONObject("result");
                    try {
                        PreferenceManager.saveString(activity, Constants.PrefKeys.FIRST_NAME, result.optString(ApiConstants.Keys.FIRST_NAME));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        PreferenceManager.saveString(activity, Constants.PrefKeys.EMAIL, result.optString(ApiConstants.Keys.EMAIL));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        PreferenceManager.saveString(activity, Constants.PrefKeys.LOGGED_ID, result.optString(ApiConstants.Keys.LOGGED_ID));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        PreferenceManager.saveString(activity, Constants.PrefKeys.LOGO, jsonObject.optString("image_path"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        PreferenceManager.saveString(activity, Constants.PrefKeys.BRANDNAME, jsonObject.optString("brand_name"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        PreferenceManager.saveString(activity, Constants.PrefKeys.WHATSAPPLINK, jsonObject.optString("whatsapp_link"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                PreferenceManager.saveBoolean(activity, Constants.AppStage.MOBILE_VERIFIED, true);
                Intent intent = new Intent(activity, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                makeToast(response.getMessage());
            }
        });
    }


    private void resendOTP() {
        DialogUtil.showProgressDialog(activity);
        NetworkManager.getInstance(activity).resendOTP(activity, response -> {
            DialogUtil.dismissProgressDialog();
            editTextPinEntry.setText("");
            showTimer();
        });
    }

    @Override
    protected void onDestroy() {
        removeTimer();
        super.onDestroy();
    }

}
