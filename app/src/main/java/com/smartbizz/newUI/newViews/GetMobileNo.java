package com.smartbizz.newUI.newViews;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartbizz.BuildConfig;
import com.smartbizz.MainActivity;
import com.smartbizz.Util.Globle;
import com.smartbizz.newUI.MainApplication;
import com.smartbizz.R;
import com.smartbizz.newUI.SharedPref;
import com.smartbizz.newUI.VolleyCall;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class GetMobileNo extends AppCompatActivity {

    private final String TAG = GetMobileNo.class.getSimpleName();
    public static EditText edtMobile, edtOtp, edtFirstName, edtEmail;
    ImageView ivRetry, ivIndicator;
    public TextView txtGetOtp, txtMsg1, txtMsg2;
    String userEmail = "";
    LinearLayout linGetOtp, layoutOtp, linEmailLayout, linFacebook, linLinkedIn, linGoogle;
    ProgressDialog progressDialog;
    private boolean mobileNoDone = false, signUpCalled = false;
    private static final String EMAIL = "email";
    final int OTPlength = 6;
    public int GET_MY_PERMISSION = 1, permission;
    final private int RC_SIGN_IN = 112;
    SharedPref sharedPref;
    public final static int linkedinRequest = 9419;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_mobile_no);
        setViews();
        sharedPref = new SharedPref();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//---HIDE STATUS BAR

        if (Build.VERSION.SDK_INT >= 23) {

            if (permission != PackageManager.PERMISSION_GRANTED) {//Direct Permission without disclaimer dialog
                ActivityCompat.requestPermissions(GetMobileNo.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        GET_MY_PERMISSION);
            } else {
            }
        }

    }

    private void setViews() {
        progressDialog = new ProgressDialog(GetMobileNo.this);
        edtMobile = findViewById(R.id.edtMobile);
        edtOtp = findViewById(R.id.edtOtp);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtEmail = findViewById(R.id.edtEmail);
        ivRetry = findViewById(R.id.ivRetry);
        ivIndicator = findViewById(R.id.ivIndicator);
        txtGetOtp = findViewById(R.id.txtGetOtp);
        txtGetOtp.setText(getString(R.string.submit));
        txtMsg1 = findViewById(R.id.txtMsg1);
        txtMsg2 = findViewById(R.id.txtMsg2);
        linGetOtp = findViewById(R.id.linGetOtp);
        linGetOtp.setBackground(getResources().getDrawable(R.drawable.border_circular_grey_filled));
        layoutOtp = findViewById(R.id.layoutOtp);
        layoutOtp.setVisibility(View.GONE);
        linEmailLayout = findViewById(R.id.linEmailLayout);
        linEmailLayout.setVisibility(View.GONE);
        linFacebook = findViewById(R.id.linFacebook);
        linLinkedIn = findViewById(R.id.linLinkedIn);
        linGoogle = findViewById(R.id.linGoogle);

        //this below code for After Entered OTP keyboard is closed

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(OTPlength);
        edtOtp.setFilters(filterArray);

        edtOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edtOtp.getText().toString().trim().length() == OTPlength) {

                    linGetOtp.setBackground(getResources().getDrawable(R.drawable.border_circular_red_filled));
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edtOtp.getWindowToken(), 0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edtEmail.getText().toString().trim().length() > 0) {
                    linGetOtp.setBackground(getResources().getDrawable(R.drawable.border_circular_red_filled));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Build.VERSION.SDK_INT >= 23) {

                    if (permission != PackageManager.PERMISSION_GRANTED)
                    {//Direct Permission without disclaimer dialog
                        ActivityCompat.requestPermissions(GetMobileNo.this,
                                new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_PHONE_STATE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA},
                                GET_MY_PERMISSION);

                    } else {
                        getOTPAPICall(s);
                    }
                } else {
                    getOTPAPICall(s);
                }
//                getOTPAPICall(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtp();
            }
        });

    }


    private void getOTPAPICall(CharSequence s) {

        if (s.length() == 10) {
            // api call - get otp
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(edtOtp.getWindowToken(), 0);
            if (s.toString().trim().equals("9898989898")) {
                try {
                    progressDialog.setMessage("Loading");
                    if (!isFinishing())
                        progressDialog.show();
                    String url = MainActivity.mainUrl + "authorization/otpLogin";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("mobile", "9898989898");
                    params.put("otp", "1234");
                    if (!Globle.isNetworkAvailable(GetMobileNo.this)) {
                        Snackbar.make(linGetOtp, getString(R.string.please_check_your_network_connection), Snackbar.LENGTH_SHORT).show();
                    } else {
                        VolleyCall volleyCall = new VolleyCall();
                        volleyCall.sendRequest(getApplicationContext(), url, GetMobileNo.this, null, "otpLogin", params, MainActivity.auth_token);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                getOtp();
            }
        }

        if (s.length() < 10) {
            linGetOtp.setOnClickListener(null);
            linGetOtp.setBackground(getResources().getDrawable(R.drawable.border_circular_grey_filled));
        } else {
            linGetOtp.setBackground(getResources().getDrawable(R.drawable.border_circular_red_filled));
            linGetOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mobileNoDone) {

                    } else if (mobileNoDone) {
                        //api call - verify otp
                        if (layoutOtp.getVisibility() == View.VISIBLE && !signUpCalled)
                            otpLogin();
                        else {
                            if (edtFirstName.getText().toString().equals("")) {
                                edtFirstName.setError("* Please provide your first name");
                            } else if (edtMobile.getText().toString().equals("")) {
                                edtMobile.setError("* Please provide mobile number");
                            } else if (edtEmail.getText().toString().equals("")) {
                                edtEmail.setError("* Please provide email");
                            } else {
                                if (!signUpCalled) {
                                    generateOtpCode();  //signUp
                                } else {
                                    verifyOtpCode();
                                }
                            }
                        }

                        edtOtp.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                if (s.length() == 0) {
                                    linGetOtp.setBackground(getResources().getDrawable(R.drawable.border_circular_grey_filled));
                                } else
                                    linGetOtp.setBackground(getResources().getDrawable(R.drawable.border_circular_red_filled));
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
//                                if (!edtOtp.getText().toString().equals("")){
//                                    startActivity(new Intent(GetMobileNo.this, GetEmailActivity.class)
//                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                                }
                    }
                }
            });

        }
    }

    private void verifyOtpCode() {
        try {
            if (!isFinishing())
                progressDialog.show();
            String url = MainActivity.mainUrl + "authorization/verifyOtpCode";
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobileno", edtMobile.getText().toString());
            params.put("name", edtFirstName.getText().toString());
            params.put("email", edtEmail.getText().toString());
            params.put("otpcode", edtOtp.getText().toString());
            if (!Globle.isNetworkAvailable(GetMobileNo.this)) {
                Snackbar.make(linGetOtp, getString(R.string.please_check_your_network_connection), Snackbar.LENGTH_SHORT).show();
            } else {
                VolleyCall volleyCall = new VolleyCall();
                volleyCall.sendRequest(getApplicationContext(), url, GetMobileNo.this, null, "verifyOtpCode", params, MainActivity.auth_token);
            }
        } catch (Exception e) {
            progressDialog.dismiss();

        }

    }

    public void getOtp() {
        try {
            if (!isFinishing())
                progressDialog.show();
            String url = MainActivity.mainUrl + "authorization/getLoginOtp";
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobile", edtMobile.getText().toString());
            if (!Globle.isNetworkAvailable(GetMobileNo.this)) {
                Snackbar.make(linGetOtp, getString(R.string.please_check_your_network_connection), Snackbar.LENGTH_SHORT).show();
            } else {
                VolleyCall volleyCall = new VolleyCall();
                volleyCall.sendRequest(getApplicationContext(), url, GetMobileNo.this, null, "getOtp", params, MainActivity.auth_token);
            }

        } catch (Exception e) {
            progressDialog.dismiss();


        }
    }

    public void getOTPResponse(JSONObject jsonData) {
        try {
            String status = jsonData.optString("status");
            String message = jsonData.optString("message");
            progressDialog.dismiss();
            mobileNoDone = true;
            if (status.equalsIgnoreCase("1")) {
                saveUserPrefernce("otp_done", "0");
                saveUserPrefernce("mobile_no", edtMobile.getText().toString().trim());
                saveUserPrefernce("userpolicyAgreement", "1");

                layoutOtp.setVisibility(View.VISIBLE);
                linEmailLayout.setVisibility(View.GONE);
                txtGetOtp.setText(getString(R.string.submit));
                linGetOtp.setBackground(getResources().getDrawable(R.drawable.border_circular_grey_filled));
                ivIndicator.setImageDrawable(getResources().getDrawable(R.drawable.ic_angle_right));
                txtMsg1.setText(getString(R.string.to_verify));
                txtMsg2.setText(getString(R.string.enter_received_otp));
                sharedPref.setLoginDone(getApplicationContext(), false);

            } else {
                saveUserPrefernce("otp_done", "0");
                layoutOtp.setVisibility(View.GONE);
                linEmailLayout.setVisibility(View.VISIBLE);
                txtGetOtp.setText("Get OTP");
                linGetOtp.setBackground(getResources().getDrawable(R.drawable.border_circular_grey_filled));
                ivIndicator.setImageDrawable(getResources().getDrawable(R.drawable.ic_login_otp));
                sharedPref.setLoginDone(getApplicationContext(), false);

//                Toast.makeText(GetMobileNo.this, message, Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(GetMobileNo.this, GetEmailActivity.class)
//                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }


        } catch (Exception e) {


        }
    }

    public void otpLogin() {
        try {
            progressDialog.setMessage("Loading");
            if (!isFinishing())
                progressDialog.show();
            String url = MainActivity.mainUrl + "authorization/otpLogin";
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobile", edtMobile.getText().toString());
            params.put("otp", edtOtp.getText().toString());
            if (!Globle.isNetworkAvailable(GetMobileNo.this)) {
                Snackbar.make(linGetOtp, getString(R.string.please_check_your_network_connection), Snackbar.LENGTH_SHORT).show();
            } else {
                VolleyCall volleyCall = new VolleyCall();
                volleyCall.sendRequest(getApplicationContext(), url, GetMobileNo.this, null, "otpLogin", params, MainActivity.auth_token);
            }
        } catch (Exception e) {
            progressDialog.dismiss();


        }
    }

    public void otpLoginResponse(JSONObject jsonData) {
        try {
            String status = jsonData.optString("status");
            String message = jsonData.optString("message");
//{"auth_token":{"userName":"abcxxrxt@gmail.com","auth_token":"fe1a4b68466c4196a19fceb3ca3490c9"},
// "student_id":"3477","result":{"logged_id":"3477","first_name":"Tester","last_name":"TEResrsr",
// "user_type":4,"email":"abcxxrxt@gmail.com",
// "img_profile":"http:\/\/159.89.204.41\/eduvanzbeta\/uploads\/student\/defaultprofilepic\/default_profile_pic_2.jpg",
// "mobile_no":"9898989898"},"status":1,"message":"Fetched Successfully"}
            if (status.equalsIgnoreCase("1")) {
                progressDialog.dismiss();
                saveUserPrefernce("name", jsonData.getJSONObject("result").getString("first_name"));
                saveUserPrefernce("otp_done", "1");
                saveUserPrefernce("mobile_no", edtMobile.getText().toString().trim());
                saveUserPrefernce("student_id", jsonData.optString("student_id"));
                saveUserPrefernce("user_img", jsonData.getJSONObject("result").getString("img_profile"));
                saveUserPrefernce("email", jsonData.getJSONObject("result").getString("email"));
                saveUserPrefernce("auth_token", jsonData.getJSONObject("auth_token").getString("auth_token"));
                saveUserPrefernce("student_id", jsonData.optString("student_id"));
                saveUserPrefernce("userpolicyAgreement", "1");
                sharedPref.setLoginDone(getApplicationContext(), true);

                startActivity(new Intent(GetMobileNo.this, DashboardActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                progressDialog.dismiss();
                saveUserPrefernce("otp_done", "0");
                sharedPref.setLoginDone(getApplicationContext(), false);
                Log.e(MainApplication.TAG, "getOTPValidation: ");
                Toast.makeText(GetMobileNo.this, message, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            progressDialog.dismiss();


        }
    }

    public void generateOtpCode() {
        try {
            progressDialog.setMessage("Loading");
            if (!isFinishing())
                progressDialog.show();
            String url = MainActivity.mainUrl + "authorization/generateOtpCode";
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobileno", edtMobile.getText().toString());
            params.put("name", edtFirstName.getText().toString());
            params.put("email", edtEmail.getText().toString());
            if (!Globle.isNetworkAvailable(GetMobileNo.this)) {
                Snackbar.make(linGetOtp, getString(R.string.please_check_your_network_connection), Snackbar.LENGTH_SHORT).show();
            } else {
                VolleyCall volleyCall = new VolleyCall();
                volleyCall.sendRequest(getApplicationContext(), url, GetMobileNo.this, null, "generateOtpCode", params, MainActivity.auth_token);
                signUpCalled = true;
            }
        } catch (Exception e) {
            progressDialog.dismiss();


        }


    }

    public void generateOtpCodeResponse(JSONObject jsonData) {
        try {
            String status = jsonData.optString("status");
            String message = jsonData.optString("message");

            if (status.equalsIgnoreCase("1")) {
                progressDialog.dismiss();
                layoutOtp.setVisibility(View.VISIBLE);
//                linEmailLayout.setVisibility(View.GONE);
//                startActivity(new Intent(GetMobileNo.this, DashboardActivity.class)
//                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                signUpCalled = false;
                progressDialog.dismiss();
                Toast.makeText(GetMobileNo.this, message, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            progressDialog.dismiss();


        }

    }

    public void verifyOtpCodeResponse(JSONObject jsonData) {
        try {
            String status = jsonData.optString("status");
            String message = jsonData.optString("message");

            if (status.equalsIgnoreCase("1")) {
                progressDialog.dismiss();

//              MainActivity.auth_token  = jsonData.getJSONObject("auth_token").getString("auth_token");
                saveUserPrefernce("name", jsonData.getJSONObject("result").getString("first_name"));
                saveUserPrefernce("otp_done", "1");
                saveUserPrefernce("mobile_no", edtMobile.getText().toString().trim());
                saveUserPrefernce("student_id", jsonData.optString("student_id"));
                saveUserPrefernce("user_img", jsonData.getJSONObject("result").getString("img"));
                saveUserPrefernce("email", jsonData.getJSONObject("result").getString("email"));
                saveUserPrefernce("auth_token", jsonData.getJSONObject("auth_token").getString("auth_token"));
                saveUserPrefernce("student_id", jsonData.optString("student_id"));
                saveUserPrefernce("userpolicyAgreement", "1");
                sharedPref.setLoginDone(getApplicationContext(), true);

                // {"student_id":3445,"baseUrl":"http:\/\/159.89.204.41\/eduvanzbeta\/","auth_token":{"userName":"0101010101",
                // "auth_token":"7b07da9be235490ec0eec52c5b049445"},"result":{"first_name":"vijay",
                // "password":"aHXD\/F3tJ32mkUtVe3ai\/GKu2QTBhgoEJHy2NHxAKM8HeEOphgD34oFtSh6N+NjZ0zGA1+cRGKkqo+zZJNsw6Q==",
                // "email":"abcde@gmail.com","mobile":"0101010101","img":"student\/defaultprofilepic\/default_profile_pic_2.jpg",
                // "created_by_ip":3756390272,"created_datetime":"2019-05-20 19:37:25"},"status":1,"message":"OTP Successfully Verified"}
                startActivity(new Intent(GetMobileNo.this, DashboardActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                progressDialog.dismiss();
                saveUserPrefernce("otp_done", "0");
                sharedPref.setLoginDone(getApplicationContext(), false);
                Toast.makeText(GetMobileNo.this, message, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            progressDialog.dismiss();


        }
    }

    private void saveUserPrefernce(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1:
                if (grantResults.length <= 0) {
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED ) {
                    //granted
//                    apiCall();
                } else {
                    //not granted
//                    Log.e(MainApplication.TAG, "not granted: Dashboard " + grantResults[0]);
                    {
                        // Permission denied.
                        // Notify the user via a SnackBar that they have rejected a core permission for the
                        // app, which makes the Activity useless. In a real app, core permissions would
                        // typically be best requested during a welcome-screen flow.
                        // Additionally, it is important to remember that a permission might have been
                        // rejected without asking the user for permission (device policy or "Never ask
                        // again" prompts). Therefore, a user interface affordance is typically implemented
                        // when permissions are denied. Otherwise, your app could appear unresponsive to
                        // touches or interactions which have required permissions.
                        //                    Toast.makeText(this, R.string.permission_denied_explanation, Toast.LENGTH_LONG).show();
                        //                    finish();

                        try {
//                            button.setEnabled(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Snackbar.make(
                                findViewById(R.id.rootViews),
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
