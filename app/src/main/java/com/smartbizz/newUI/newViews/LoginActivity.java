package com.smartbizz.newUI.newViews;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.smartbizz.BuildConfig;
import com.smartbizz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.smartbizz.Util.Constants;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.Util.Validator;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.pojo.Login;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText etPhoneNumber, etEmail, etName;
    private LinearLayout layoutEmail;
    private Button btnSubmit;

    private final static int RESOLVE_HINT = 1011;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        layoutEmail = findViewById(R.id.layoutEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        findViewById(R.id.tvLoginViaEmail).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            boolean isValid = true;
            String mobileNumber = etPhoneNumber.getText().toString().trim(), email = "", name = "";
            TextView labelMobileNumber = findViewById(R.id.labelMobileNumber);
            TextView tvErrorMobileNumber = findViewById(R.id.tvErrorMobileNumber);
            if (TextUtils.isEmpty(mobileNumber)) {
                inValidField(labelMobileNumber, tvErrorMobileNumber, getString(R.string.this_field_is_required));
                isValid = false;
            } else if (!Validator.isValidMobileNumber(mobileNumber)) {
                inValidField(labelMobileNumber, tvErrorMobileNumber, getString(R.string.error_enter_valid_mobile_number));
                isValid = false;
            } else {
                validField(labelMobileNumber, tvErrorMobileNumber);
            }
            if (btnSubmit.getText().toString().equalsIgnoreCase(getString(R.string.login))) {

                name = etName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    inValidField(etName, getString(R.string.this_field_is_required));
                    isValid = false;
                } else {
                    validField(etName);
                }

                email = etEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    inValidField(etEmail, getString(R.string.this_field_is_required));
                    isValid = false;
                } else if (!Validator.isEmailValid(email)) {
                    inValidField(etEmail, getString(R.string.error_enter_valid_email_address));
                    isValid = false;
                } else {
                    validField(etEmail);
                }
            }
            if (isValid) {
                makeGetOtpRequest(mobileNumber, email, name);
            }
        }
    }

    private void makeGetOtpRequest(String mobile, String email, String name) {
        Login login = new Login(mobile, email, name);
        PreferenceManager.saveString(activity, Constants.PrefKeys.FIRST_NAME, name);
        PreferenceManager.saveString(activity, Constants.PrefKeys.MOBILE, mobile);
        PreferenceManager.saveString(activity, Constants.PrefKeys.EMAIL, email);
        DialogUtil.showProgressDialog(activity);
        NetworkManager.getInstance(activity).getOTP(activity, login, response -> {
            DialogUtil.dismissProgressDialog();
            if (response.getStatus() == 1) {
                Intent intent = new Intent(activity, VerifyOTPActivity.class);
                startActivity(intent);
                finish();
            } else if (response.getStatus() == 2) {
                layoutEmail.setVisibility(View.VISIBLE);
                btnSubmit.setText(getString(R.string.login));
                etPhoneNumber.clearFocus();
                etEmail.requestFocus();
            }else {
                try {
                    if (response.getResponse().getString("status").equalsIgnoreCase("OK")) {
                        Intent intent = new Intent(activity, VerifyOTPActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        makeToast(response.getMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void moveToNext(JSONObject jsonObject) {
        Intent intent = new Intent(activity, VerifyOTPActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {

        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
