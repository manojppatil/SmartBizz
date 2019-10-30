package com.eduvanzapplication.newUI.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
//import com.digio.in.esignsdk.Digio;
//import com.digio.in.esignsdk.DigioConfig;
//import com.digio.in.esignsdk.DigioEnvironment;
import com.digio.in.esign2sdk.Digio;
import com.digio.in.esign2sdk.DigioConfig;
import com.digio.in.esign2sdk.DigioEnvironment;
import com.digio.in.esign2sdk.DigioServiceMode;
import com.eduvanzapplication.MainActivity;
import com.eduvanzapplication.R;
import com.eduvanzapplication.Util.Globle;
import com.eduvanzapplication.Util.Paytm;
import com.eduvanzapplication.Utils;
import com.eduvanzapplication.newUI.VolleyCall;
import com.eduvanzapplication.newUI.adapter.NachAdapter;
import com.eduvanzapplication.newUI.newViews.LoanTabActivity;
import com.eduvanzapplication.newUI.pojo.MNach;
import com.eduvanzapplication.uploaddocs.PathFile;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.eduvanzapplication.MainActivity.TAG;
import static com.eduvanzapplication.R.*;
import static com.eduvanzapplication.newUI.fragments.DashboardFragmentNew.userName;

//https://www.linkedin.com/developers/apps/verification/d122f3bd-f765-41a8-98b7-6aa7706d8fce
public class PostApprovalDocFragment extends Fragment {

    static View view;
    public static Context context;
    public static Fragment mFragment;
    public int SELECT_DOC = 2;
    public static ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    DownloadManager downloadManager;
    private static String uploadFilePath = "";
    StringBuffer sb;

    public static View viewDiag;
    public static ProgressBar progressBarDiag;

    public static onPostFragmentInteractionListener mListener;

    //Manual Diag
    public static LinearLayout linClose, linDownload, linUpload;
    //OTP Diag
    public static LinearLayout linCloseOTP, layoutOtp, linSubmitOtpBo, linSubmitOtpCo, linStep2, linGetOtpBo, linGetOtpCo,layoutOtpBo,layoutOtpCo;
    public static EditText edtOtpBo, edtOtpCo;
    public static WebView webView;
    final int OTPlength = 6;
    public static String city, state, country, postalCode, ipaddress, latitude, longitde;

    public CFAlertDialog cfAlertDialog;

    public static List<MNach> mNachArrayList;
    public static RecyclerView rvNach;
    public static NachAdapter adapter;

    int PERMISSION_ALL = 1;
    LocationManager locationManager;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    public static Animation collapseanimationlinExpand, expandAnimationlinExpand;

    public String lead_id = "", application_loan_id = "", principal_amount = "", down_payment = "", rate_of_interest = "", tenure = "",
            emi_type = "", emi_amount = "", total_amount_to_be_collected = "", amount_to_be_paid_to_institute = "",
            is_moratorium = "", moratorium_months = "", moratorium_type = "", no_emi_paid = "", requested_loan_amount = "",
            requested_tenure = "", requested_roi = "", requested_emi = "",
            offered_amount = "", applicant_id = "", fk_lead_id = "", first_name = "", last_name = "", mobile_number = "",
            email_id = "", kyc_address = "", course_cost = "", paid_on = "", transaction_amount = "", kyc_status = "",
            disbursal_status = "", rate = "", loan_agrement_upload_status = "", paid_emi_on = "", no_of_advance_emi = "";

    public static String mobile_numberCoBo = "",applicant_idCoBo;

    String downloadUrl = "", downloadSignedUrl = "", baseUrl = "";
    public static String paymentOption = "1";

    public static Double totalAmount = 0.0;

    Boolean processing_payment = true, advance_emi = false;
    long downloadReference;

    public static LinearLayout linManualBtn, lineSignBtn, linOTPBtn, linData, linAggSignInBtn,
            linDownloadSignedAgreement, linDownloadNach, linDisbursed, linAgreementSigned, linPayBtn, linPayStatus,
            linNachList, linHasLoan, linNoLoan, linProcessingFeeRb, linEMIFeeRb;

    public static TextView txtTbCourseFee, txtTbRequestedLoanAmount, txtTbOfferedLoanAmount, txtTbSelectTenure, txtTbSelectRateOFInterest,
            txtTbSelectedEMIAmount, txtTbSanctionLoanAmount, txtTbDownpayment, txtTbInterestRate,
            txtTbEMIType, txtTbEmiAmount, txtTbProcessingFee, txtTbNoOfAdvanceEMI, txtTbAdvanceEMIAmount, txtTbGrossLoanTenure,
            txtTbNetLoantenure, txtTbROI, txtTbEMI, txtTbTotalRepaybleAmount, txtTbMoratoriumDuration, txtTbMoratorium,
            txtTbEMIDuringMoratorium, txtTbMoratoriumType;

    public static TextView txtProcessingFeeAmt, txtProcessingFeeDueByDate, txtEMIFeeAmt, txtEMIDueByDate, txtTotalAmt,
            txtApplicationLoanID, txtProcessingFeeDueTitle, txtEMIDueByTitle, txtEMIType;
    public static ImageView ivProcessingFee, ivEMIFee, ivAdavnceFeeIcon, ivProcessingFeesicon;
    public static RelativeLayout relExpandCollapse;
    public static ImageButton btnExpandCollapse;
    public static ImageButton btnNextPostApprovalDoc;

    public static ImageView ivLeadDisbursed, ivAggSigned;

    public static TextView txtLeadDisbursedStatus, txtAggSignedStatus;

    public BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadReference == referenceId) {

//                Button cancelDownload = (Button) findViewById(R.id.cancelDownload);
//                cancelDownload.setEnabled(false);
//file:///storage/emulated/0/Android/data/com.eduvanzemployees/files/Download/SIGNED APPLICATIONEdutesterEduvanz1530095441962.pdf
                int ch;
                ParcelFileDescriptor file;
                StringBuffer strContent = new StringBuffer("");

                //parse the JSON data and display on the screen
                try {
                    file = downloadManager.openDownloadedFile(downloadReference);
                    FileInputStream fileInputStream
                            = new ParcelFileDescriptor.AutoCloseInputStream(file);

                    while ((ch = fileInputStream.read()) != -1)
                        strContent.append((char) ch);

                    progressBar.setVisibility(View.GONE);
                    try {
                        progressBarDiag.setVisibility(viewDiag.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast toast = Toast.makeText(context, R.string.downloading_of_file_just_finished, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 25, 400);
                    toast.show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public PostApprovalDocFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lead_id = "";
        application_loan_id = "";
        principal_amount = "";
        down_payment = "";
        rate_of_interest = "";
        emi_type = "";
        emi_amount = "";
        no_emi_paid = "";
        requested_loan_amount = "";
        no_of_advance_emi = "";
        requested_tenure = "";
        requested_roi = "";
        requested_emi = "";
        offered_amount = "";
        applicant_id = "";
        fk_lead_id = "";
        first_name = "";
        last_name = "";
        mobile_number = "";
        email_id = "";
        kyc_address = "";
        course_cost = "";
        paid_on = "";
        transaction_amount = "";
        kyc_status = "";
        disbursal_status = "";
        loan_agrement_upload_status = "";
        downloadUrl = "";
        downloadSignedUrl = "";
        baseUrl = "";
        paymentOption = "1";
        paid_emi_on = "";
        mobile_numberCoBo = "";
        applicant_idCoBo = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(layout.fragment_postapprovaldoc, container, false);

        context = getContext();
        mFragment = new PostApprovalDocFragment();
        MainActivity.currrentFrag = 4;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressBar = view.findViewById(R.id.progressBar_signsubmit);
        relExpandCollapse = view.findViewById(R.id.relExpandCollapse);
        linManualBtn = view.findViewById(R.id.linManualBtn);
        lineSignBtn = view.findViewById(R.id.lineSignBtn);
        linOTPBtn = view.findViewById(R.id.linOTPBtn);
        linPayBtn = view.findViewById(R.id.linPayBtn);
        linPayStatus = view.findViewById(R.id.linPayStatus);
        linData = view.findViewById(R.id.linData);
        linAggSignInBtn = view.findViewById(R.id.linAggSignInBtn);
        linDownloadSignedAgreement = view.findViewById(R.id.linDownloadSignedAgreement);
        linDownloadNach = view.findViewById(R.id.linDownloadNach);
        btnExpandCollapse = view.findViewById(R.id.btnExpandCollapse);
        ivLeadDisbursed = view.findViewById(R.id.ivLeadDisbursed);
        ivAggSigned = view.findViewById(R.id.ivAggSigned);
        linDisbursed = view.findViewById(R.id.linDisbursed);
        linAgreementSigned = view.findViewById(R.id.linAgreementSigned);
        linNachList = view.findViewById(R.id.linNachList);
        linHasLoan = view.findViewById(R.id.linHasLoan);
        linNoLoan = view.findViewById(R.id.linNoLoan);

        txtApplicationLoanID = view.findViewById(id.txtApplicationLoanID);
        txtTbCourseFee = view.findViewById(id.txtTbCourseFee);

        txtTbGrossLoanTenure = view.findViewById(R.id.txtTbGrossLoanTenure);
        txtTbNetLoantenure = view.findViewById(R.id.txtTbNetLoantenure);
        txtTbROI = view.findViewById(R.id.txtTbROI);
        txtTbEMI = view.findViewById(R.id.txtTbEMI);
        txtTbTotalRepaybleAmount = view.findViewById(R.id.txtTbTotalRepaybleAmount);
        txtTbMoratorium = view.findViewById(R.id.txtTbMoratorium);
        txtTbMoratoriumDuration = view.findViewById(R.id.txtTbMoratoriumDuration);
        txtTbEMIDuringMoratorium = view.findViewById(R.id.txtTbEMIDuringMoratorium);
        txtTbMoratoriumType = view.findViewById(R.id.txtTbMoratoriumType);
        btnNextPostApprovalDoc = view.findViewById(R.id.btnNextPostApprovalDoc);

        txtTbRequestedLoanAmount = view.findViewById(id.txtTbRequestedLoanAmount);
        //  txtTbOfferedLoanAmount = view.findViewById(id.txtTbOfferedLoanAmount);
        //   txtTbSelectTenure = view.findViewById(id.txtTbSelectTenure);
        //  txtTbSelectRateOFInterest = view.findViewById(id.txtTbSelectRateOFInterest);
        txtTbNoOfAdvanceEMI = view.findViewById(id.txtTbNoOfAdvanceEMI);
        txtTbAdvanceEMIAmount = view.findViewById(id.txtTbAdvanceEMIAmount);
        //  txtTbSelectedEMIAmount = view.findViewById(id.txtTbSelectedEMIAmount);
        txtTbSanctionLoanAmount = view.findViewById(id.txtTbSanctionLoanAmount);
        txtTbDownpayment = view.findViewById(id.txtTbDownpayment);
        //   txtTbInterestRate = view.findViewById(id.txtTbInterestRate);
        txtTbEMIType = view.findViewById(id.txtTbEMIType);
        //  txtTbEmiAmount = view.findViewById(id.txtTbEmiAmount);
        txtTbProcessingFee = view.findViewById(id.txtTbProcessingFee);

        txtProcessingFeeAmt = view.findViewById(id.txtProcessingFeeAmt);
        txtProcessingFeeDueByDate = view.findViewById(id.txtProcessingFeeDueByDate);
        txtEMIFeeAmt = view.findViewById(id.txtEMIFeeAmt);
        txtEMIDueByDate = view.findViewById(id.txtEMIDueByDate);
        txtTotalAmt = view.findViewById(id.txtTotalAmt);
        txtProcessingFeeDueTitle = view.findViewById(id.txtProcessingFeeDueTitle);
        txtEMIDueByTitle = view.findViewById(id.txtEMIDueByTitle);
        ivProcessingFee = view.findViewById(id.ivProcessingFee);
        ivEMIFee = view.findViewById(id.ivEMIFee);

        ivAdavnceFeeIcon = view.findViewById(id.ivAdavnceFeeIcon);
        ivProcessingFeesicon = view.findViewById(id.ivProcessingFeesicon);

        linProcessingFeeRb = view.findViewById(id.linProcessingFeeRb);
        linEMIFeeRb = view.findViewById(id.linEMIFeeRb);

        txtLeadDisbursedStatus = view.findViewById(id.txtLeadDisbursedStatus);
        txtAggSignedStatus = view.findViewById(id.txtAggSignedStatus);

        expandAnimationlinExpand = AnimationUtils.loadAnimation(context, R.anim.scale_expand);
        collapseanimationlinExpand = AnimationUtils.loadAnimation(context, R.anim.scale_collapse);

        rvNach = view.findViewById(id.rvNach);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvNach.setLayoutManager(linearLayoutManager);

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            city = sharedPreferences.getString("city", "");
            state = sharedPreferences.getString("state", "");
            country = sharedPreferences.getString("country", "");
            postalCode = sharedPreferences.getString("postalCode", "");
            ipaddress = sharedPreferences.getString("ipaddress", "");
            latitude = sharedPreferences.getString("latitude", "");
            longitde = sharedPreferences.getString("longitde", "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadReceiver, filter);

        linManualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linManualBtn.setBackground(getResources().getDrawable(R.drawable.border_circular_blue_filled));
                lineSignBtn.setBackground(getResources().getDrawable(R.drawable.border_circular));
                linOTPBtn.setBackground(getResources().getDrawable(R.drawable.border_circular));
                manualSignInDialog();//                    int count = borrowerOffStatePersonalPOJOArrayList.size();

            }
        });

        btnNextPostApprovalDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPostApprovalFragment(true, 4);
            }
        });

        lineSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linManualBtn.setBackground(getResources().getDrawable(R.drawable.border_circular));
                lineSignBtn.setBackground(getResources().getDrawable(R.drawable.border_circular_blue_filled));
                linOTPBtn.setBackground(getResources().getDrawable(R.drawable.border_circular));

                try {
                    String ipaddress = Utils.getIPAddress(true);
//                  String url = "http://192.168.1.19/eduvanzapi/laf/getDigioDocumentIdForStudent";
                    String url = MainActivity.mainUrl + "laf/getDigioDocumentIdForStudent";
                    Map<String, String> params = new HashMap<String, String>();
                    if (!Globle.isNetworkAvailable(context)) {
                        Toast.makeText(context, R.string.please_check_your_network_connection, Toast.LENGTH_SHORT).show();

                    } else {
                        VolleyCall volleyCall = new VolleyCall();
                        params.put("logged_id", LoanTabActivity.student_id);
                        params.put("created_by_ip", ipaddress);
                        params.put("lead_id", LoanTabActivity.lead_id);
                        if (!Globle.isNetworkAvailable(context)) {
                            Toast.makeText(context, R.string.please_check_your_network_connection, Toast.LENGTH_SHORT).show();

                        } else {
                            volleyCall.sendRequest(context, url, null, mFragment, "getDigioDocumentIdForStudent", params, MainActivity.auth_token);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        linOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linManualBtn.setBackground(getResources().getDrawable(R.drawable.border_circular));
                lineSignBtn.setBackground(getResources().getDrawable(R.drawable.border_circular));
                linOTPBtn.setBackground(getResources().getDrawable(R.drawable.border_circular_blue_filled));

//                try {
//                    if(sharedPreferences.getString("city", "") != "" && sharedPreferences.getString("state", "") != ""
//                            && sharedPreferences.getString("country", "") != "" && sharedPreferences.getString("postalCode", "") != ""
//                            && sharedPreferences.getString("latitude", "") != "" && sharedPreferences.getString("longitde", "") != ""
//                            && sharedPreferences.getString("ipaddress", "") != "") {
//                        otpSignInDialog(context);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                genrateOTPAgreement();
                otpSignInDialog(context);

            }
        });

        btnExpandCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linData.getVisibility() == VISIBLE) {
                    linData.startAnimation(collapseanimationlinExpand);
                } else {
                    linData.startAnimation(expandAnimationlinExpand);
                }
            }
        });

        relExpandCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linData.getVisibility() == VISIBLE) {
                    linData.startAnimation(collapseanimationlinExpand);
                } else {
                    linData.startAnimation(expandAnimationlinExpand);
                }
            }
        });

        collapseanimationlinExpand.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                linData.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        expandAnimationlinExpand.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                linData.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        linDownloadSignedAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (!linDownloadSignedAgreement.getTag().toString().equalsIgnoreCase("")) {
                    downloadSignedUrl = linDownloadSignedAgreement.getTag().toString();
                    downLoad(downloadSignedUrl, 2, "SignedAgreement");
                } else {
                    Toast.makeText(context, R.string.something_went_wrong_please_try_again_later, Toast.LENGTH_SHORT).show();

                }

            }
        });

        linDownloadNach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (!linDownloadNach.getTag().toString().equalsIgnoreCase("")) {
                    downloadSignedUrl = linDownloadNach.getTag().toString();
                    downLoad(downloadSignedUrl, 2, "Nach");
                } else {
                    Toast.makeText(context, R.string.something_went_wrong_please_try_again_later, Toast.LENGTH_SHORT).show();
                }
            }
        });

        linProcessingFeeRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (processing_payment) {
                    processing_payment = false;
                    totalAmount = 0.0;
                    Drawable bg;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_radio_icon, null);
                        ivProcessingFee.setColorFilter(getResources().getColor(color.colorRed), PorterDuff.Mode.MULTIPLY);
                    } else {
                        bg = ContextCompat.getDrawable(context, R.drawable.ic_radio_icon);
                        DrawableCompat.setTint(bg, getResources().getColor(color.colorRed));
                    }
                    ivProcessingFee.setImageDrawable(bg);

                    if (processing_payment && advance_emi) {
                        totalAmount = Double.valueOf(txtProcessingFeeAmt.getText().toString().trim()) + Double.valueOf(txtTbAdvanceEMIAmount.getText().toString().split("/")[0]);
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "3";
                    } else if (processing_payment) {
                        totalAmount = Double.valueOf(txtProcessingFeeAmt.getText().toString().trim());
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "1";
                    } else if (advance_emi) {
                        totalAmount = Double.valueOf(txtTbAdvanceEMIAmount.getText().toString().split("/")[0]);
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "2";
                    } else {
                        totalAmount = 0.0;
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "0";
                    }
                } else {
                    processing_payment = true;
                    totalAmount = 0.0;
                    Drawable bg;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_radio_icon, null);
                        ivProcessingFee.setColorFilter(getResources().getColor(color.colorGreen), PorterDuff.Mode.MULTIPLY);
                    } else {
                        bg = ContextCompat.getDrawable(context, R.drawable.ic_radio_icon);
                        DrawableCompat.setTint(bg, getResources().getColor(color.colorGreen));
                    }
                    ivProcessingFee.setImageDrawable(bg);

                    if (processing_payment && advance_emi) {
                        totalAmount = Double.valueOf(txtProcessingFeeAmt.getText().toString().trim()) + Double.valueOf(txtTbAdvanceEMIAmount.getText().toString().split("/")[0]);
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "3";
                    } else if (processing_payment) {
                        totalAmount = Double.valueOf(txtProcessingFeeAmt.getText().toString().trim());
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "1";
                    } else if (advance_emi) {
                        totalAmount = Double.valueOf(txtTbAdvanceEMIAmount.getText().toString().split("/")[0]);
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "2";
                    } else {
                        totalAmount = 0.0;
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "0";
                    }

                }
            }
        });

        linEMIFeeRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advance_emi) {
                    advance_emi = false;
                    totalAmount = 0.0;
                    Drawable bg;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_radio_icon_emi, null);
                        ivEMIFee.setColorFilter(getResources().getColor(color.colorRed), PorterDuff.Mode.MULTIPLY);
                    } else {
                        bg = ContextCompat.getDrawable(context, R.drawable.ic_radio_icon_emi);
                        DrawableCompat.setTint(bg, getResources().getColor(color.colorRed));
                    }
                    ivEMIFee.setImageDrawable(bg);

                    if (processing_payment && advance_emi) {
                        totalAmount = Double.valueOf(txtProcessingFeeAmt.getText().toString().trim()) + Double.valueOf(txtTbAdvanceEMIAmount.getText().toString().split("/")[0]);
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "3";
                    } else if (processing_payment) {
                        totalAmount = Double.valueOf(txtProcessingFeeAmt.getText().toString().trim());
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "1";
                    } else if (advance_emi) {
                        totalAmount = Double.valueOf(txtTbAdvanceEMIAmount.getText().toString().split("/")[0]);
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "2";
                    } else {
                        totalAmount = 0.0;
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "0";
                    }
                } else {
                    advance_emi = true;
                    totalAmount = 0.0;
                    Drawable bg;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_radio_icon_emi, null);
                        ivEMIFee.setColorFilter(getResources().getColor(color.colorGreen), PorterDuff.Mode.MULTIPLY);
                    } else {
                        bg = ContextCompat.getDrawable(context, R.drawable.ic_radio_icon_emi);
                        DrawableCompat.setTint(bg, getResources().getColor(color.colorGreen));
                    }
                    ivEMIFee.setImageDrawable(bg);

                    if (processing_payment && advance_emi) {
                        if (linProcessingFeeRb.isEnabled()) {
                            //txtTbAdvanceEMIAmount.getText().toString().split("/")[0]
                            totalAmount = Double.valueOf(txtProcessingFeeAmt.getText().toString().trim()) + Double.valueOf(txtTbAdvanceEMIAmount.getText().toString().split("/")[0]);
                            txtTotalAmt.setText(String.valueOf(totalAmount));
                            paymentOption = "3";
                        } else {
                            totalAmount = Double.valueOf(txtTbAdvanceEMIAmount.getText().toString().split("/")[0]);
                            txtTotalAmt.setText(String.valueOf(totalAmount));
                            paymentOption = "2";
                        }

                    } else if (processing_payment) {
                        totalAmount = Double.valueOf(txtProcessingFeeAmt.getText().toString().trim());
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "1";
                    } else if (advance_emi) {
                        totalAmount = Double.valueOf(txtTbAdvanceEMIAmount.getText().toString().split("/")[0]);
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "2";
                    } else {
                        totalAmount = 0.0;
                        txtTotalAmt.setText(String.valueOf(totalAmount));
                        paymentOption = "0";
                    }

                }
            }
        });

        linPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    try {
                        //Staging
//                        Globle.getInstance().paytm = new Paytm(
//                                "Eduvan80947867008828",
//                                "WAP",
//                                "1",
//                                "APP_STAGING",
////                               "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp",
//                                "https://securegw-stage.paytm.in/theia/paytmCallback",
//                                "Retail"
//                        );
                        //Prod
                        Globle.getInstance().paytm = new Paytm(
                                "Eduvan80947867008828",
                                "WAP",
                                txtTotalAmt.getText().toString().trim(),
                                "APPPROD",
                                "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp",
//                                "https://securegw.paytm.in/theia/paytmCallback",
                                "Retail109"
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String url = "https://eduvanz.com/Paytm/generateChecksum.php/";
                    Map<String, String> params = new HashMap<String, String>();
                    VolleyCall volleyCall = new VolleyCall();
//                        ((LoanApplication)mFragment).mActivity1;
//                      params.put("MID", ((LoanApplication)getActivity()).paytm.getmId());
                    params.put("MID", Globle.getInstance().paytm.getmId());
                    params.put("ORDER_ID", Globle.getInstance().paytm.getOrderId());
                    params.put("CUST_ID", Globle.getInstance().paytm.getCustId());
                    params.put("CHANNEL_ID", Globle.getInstance().paytm.getChannelId());
                    params.put("TXN_AMOUNT", Globle.getInstance().paytm.getTxnAmount());
                    params.put("WEBSITE", Globle.getInstance().paytm.getWebsite());
                    params.put("CALLBACK_URL", Globle.getInstance().paytm.getCallBackUrl());
                    params.put("INDUSTRY_TYPE_ID", Globle.getInstance().paytm.getIndustryTypeId());
                    if (!Globle.isNetworkAvailable(context)) {
                        Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();

                    } else {
                        volleyCall.sendRequest(context, url, null, mFragment, "initializePaytmPayment", params, MainActivity.auth_token);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        getLoanDetails();
    }

    public void initializePaytmPayment(JSONObject jsonData) {

        try {
            //getting paytm service
//            PaytmPGService Service = PaytmPGService.getStagingService();
            PaytmPGService Service = PaytmPGService.getProductionService();
            String checksumHash = jsonData.optString("CHECKSUMHASH");

            //creating a hashmap and adding all the values required
            Map<String, String> paramMap = new HashMap<String, String>();

            paramMap.put("MID", Globle.getInstance().paytm.getmId());
            paramMap.put("ORDER_ID", Globle.getInstance().paytm.getOrderId());
            paramMap.put("CUST_ID", Globle.getInstance().paytm.getCustId());
//            paramMap.put( "MOBILE_NO" , "7777777777");
//            paramMap.put( "EMAIL" , "shuklavijay249@gmail.com");
            paramMap.put("CHANNEL_ID", Globle.getInstance().paytm.getChannelId());
            paramMap.put("TXN_AMOUNT", Globle.getInstance().paytm.getTxnAmount());
            paramMap.put("WEBSITE", Globle.getInstance().paytm.getWebsite());
//        paramMap.put("CALLBACK_URL",Globle.getInstance().paytm.getCallBackUrl().concat("?").concat("ORDER_ID=").concat(Globle.getInstance().paytm.getOrderId()));
            paramMap.put("CALLBACK_URL", Globle.getInstance().paytm.getCallBackUrl());
            paramMap.put("CHECKSUMHASH", checksumHash);
            paramMap.put("INDUSTRY_TYPE_ID", Globle.getInstance().paytm.getIndustryTypeId());

//            0 = {HashMap$HashMapEntry@5648} "MID" -> "Eduvan80947867008828"
//            1 = {HashMap$HashMapEntry@5649} "CALLBACK_URL" -> "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp"
//            2 = {HashMap$HashMapEntry@5650} "TXN_AMOUNT" -> "10.00"
//            3 = {HashMap$HashMapEntry@5651} "ORDER_ID" -> "ORDER200006679"
//            4 = {HashMap$HashMapEntry@5652} "WEBSITE" -> "APPPROD"
//            5 = {HashMap$HashMapEntry@5653} "INDUSTRY_TYPE_ID" -> "Retail109"
//            6 = {HashMap$HashMapEntry@5654} "CHECKSUMHASH" -> "ptMv3qYM2en9Y1TmVwRoax0yr8VgFW77RxYTZOewMFlfemLJ98lk6mVr8BmvFhX5myDKneXZ8scqta/h5FuKTdZihj580RPFiXUZoOgIsbA="
//            7 = {HashMap$HashMapEntry@5655} "CHANNEL_ID" -> "WAP"
//            8 = {HashMap$HashMapEntry@5656} "CUST_ID" -> "CUSTOMER200005319"

//            0 = {HashMap$HashMapEntry@6751} "MID" -> "Eduvan80947867008828"
//            1 = {HashMap$HashMapEntry@6752} "CALLBACK_URL" -> "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp"
//            2 = {HashMap$HashMapEntry@6753} "TXN_AMOUNT" -> "10.00"
//            3 = {HashMap$HashMapEntry@6754} "ORDER_ID" -> "ORDER100002176"
//            4 = {HashMap$HashMapEntry@6755} "WEBSITE" -> "APPPROD"
//            5 = {HashMap$HashMapEntry@6756} "INDUSTRY_TYPE_ID" -> "Retail109"
//            6 = {HashMap$HashMapEntry@6757} "CHECKSUMHASH" -> "Zo0R0dEqby7rSNFfX5wX6SBCAAsxTCZ1xwuzsC4t9Nfjm75puU6mJaJEgmbfggQWjZu3H+Hdn7owdzHw7JYtSsIR/viV415AoWlztCzvc0Q="
//            7 = {HashMap$HashMapEntry@6758} "CHANNEL_ID" -> "WAP"
//            8 = {HashMap$HashMapEntry@6759} "CUST_ID" -> "CUSTOMER100004464"

            PaytmOrder order = new PaytmOrder((HashMap<String, String>) paramMap);

            //intializing the paytm service
            Service.initialize(order, null);

            //finally starting the payment transaction
            Service.startPaymentTransaction(context, true, true, new PaytmPaymentTransactionCallback() {
                @Override
                public void someUIErrorOccurred(String inErrorMessage) {
                    // Some UI Error Occurred in Payment Gateway Activity.
                    // // This may be due to initialization of views in
                    // Payment Gateway Activity or may be due to //
                    // initialization of webview. // Error Message details
                    // the error occurred.
//                    Toast.makeText(context, "Payment Transaction response " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
//                    StringBuilder s = new StringBuilder();//cb 207np 63w 54more text
//                    s.append("inErrorMessage-");
//                    s.append(inErrorMessage);
//                    Globle.appendLog(String.valueOf(s));

                }

                @Override
                public void onTransactionResponse(Bundle inResponse) {
                    Log.d("LOG", "Payment Transaction is successful " + inResponse);
//                    Toast.makeText(context, "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
//                    StringBuilder s = new StringBuilder();//cb 207np 63w 54more text
//                    s.append("inResponse-");
//                    s.append(inResponse);
//                    Globle.appendLog(String.valueOf(s));
                    //"Bundle[{STATUS=TXN_SUCCESS,
                    // CHECKSUMHASH=ENXZLPAIk3AlC/rdD7EfpMnG8Okxe0819nIQvFBjJL+aGnTrGIQfHHtGLFoiI+sWxVEFmOer+UCZiNaRNaRyOGbE4NMF66qRldhhHLJFaUs=,
                    // BANKNAME=Union Bank of India, ORDERID=ORDER100008205, TXNAMOUNT=10.00,
                    // TXNDATE=2018-07-10 14:23:01.0, MID=Eduvan80947867008828, TXNID=20180710111212800110168845030370887,
                    // RESPCODE=01, PAYMENTMODE=DC, BANKTXNID=201819106425560, CURRENCY=INR, GATEWAYNAME=SBIFSS,
                    // RESPMSG=Txn Success}]"
                    if (inResponse.get("STATUS").equals("TXN_SUCCESS"))
                    //                    if(inResponse.get("STATUS").equals("TXN_FAILURE"))
                    {
                        if (inResponse != null) {
                            String message = String.valueOf(inResponse.get("RESPMSG"));
                            String CHECKSUMHASH = String.valueOf(inResponse.get("CHECKSUMHASH"));
                            String BANKNAME = String.valueOf(inResponse.get("BANKNAME"));
                            String ORDERID = String.valueOf(inResponse.get("ORDERID"));
                            String TXNAMOUNT = String.valueOf(inResponse.get("TXNAMOUNT"));
                            String TXNDATE = String.valueOf(inResponse.get("TXNDATE"));
                            String MID = String.valueOf(inResponse.get("MID"));
                            String TXNID = String.valueOf(inResponse.get("TXNID"));
                            String RESPCODE = String.valueOf(inResponse.get("RESPCODE"));
                            String BANKTXNID = String.valueOf(inResponse.get("BANKTXNID"));
                            String PAYMENTMODE = String.valueOf(inResponse.get("PAYMENTMODE"));

                            //                            String[] resKey = data.getStringArrayExtra("responseKeyArray");
                            //                            String[] resValue = data.getStringArrayExtra("responseValueArray");
                            //                            String merTxn = "", bnkTxn = "", amt = "";
                            //                            if (resKey != null && resValue != null) {
                            //                                for (int i = 0; i < resKey.length; i++) {
                            //                                    System.out.println("  " + i + " resKey : " + resKey[i] + " resValue : " + resValue[i]);
                            //
                            //                                    if (resKey[i].equalsIgnoreCase("mer_txn")) {
                            //                                        merTxn = resValue[i];
                            //                                    } else if (resKey[i].equalsIgnoreCase("bank_txn")) {
                            //                                        bnkTxn = resValue[i];
                            //                                    } else if (resKey[i].equalsIgnoreCase("amt")) {
                            //                                        amt = resValue[i];
                            //                                    }
                            //                                }
                            //                                System.out.println(" status " + message);
                            //                            }
                            if (message.equalsIgnoreCase("TXN_SUCCESS")) {
                                Log.e(MainActivity.TAG, "onActivityResult: " + "Transaction Successful!");
                                /** API CALL **/
                                try {

                                    //payment_platform = 2 (1-web, 2-app)
                                    //payment_partner = 1 (1-paytm, 2-atom)

//                                        String ipaddress = Utils.getIPAddress(true);
                                    String url = MainActivity.mainUrl + "epayment/paytmPaymnetReponse";
                                    Map<String, String> params = new HashMap<String, String>();
                                    VolleyCall volleyCall = new VolleyCall();
                                    params.put("lead_id", MainActivity.lead_id);
                                    params.put("TXNAMOUNT", TXNAMOUNT);
                                    params.put("TXNID", TXNID); // merchant ID
//                                  params.put("bankTxnId", BANKTXNID); // Bank ID
                                    params.put("STATUS", "TXN_SUCCESS");
                                    params.put("paymentoption", paymentOption);
//                                        params.put("created_by_ip", ipaddress);
//                                        params.put("payment_partner", payment_partner);
//                                        params.put("payment_platform", payment_platform);
//                                        params.put("gateway_type", "Paytm");
                                    if (!Globle.isNetworkAvailable(context)) {
                                        Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();

                                    } else {
                                        volleyCall.sendRequest(context, url, null, mFragment, "paytmPaymnetReponse", params, MainActivity.auth_token);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e(MainActivity.TAG, "onActivityResult: " + "Transaction Successful!");
                                /** API CALL **/
                                try {
//                                        String ipaddress = Utils.getIPAddress(true);
                                    String url = MainActivity.mainUrl + "epayment/paytmPaymnetReponse";
                                    Map<String, String> params = new HashMap<String, String>();
                                    VolleyCall volleyCall = new VolleyCall();
                                    params.put("lead_id", MainActivity.lead_id);
                                    params.put("TXNAMOUNT", TXNAMOUNT);
                                    params.put("TXNID", TXNID); // merchant ID
//                                  params.put("bankTxnId", BANKTXNID); // Bank ID
                                    params.put("STATUS", "TXN_SUCCESS");
                                    params.put("paymentoption", paymentOption);
//                                        params.put("created_by_ip", ipaddress);
//                                        params.put("payment_partner", payment_partner);
//                                        params.put("payment_platform", payment_platform);
//                                        params.put("gateway_type", "Paytm");
                                    if (!Globle.isNetworkAvailable(context)) {
                                        Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();

                                    } else {
                                        volleyCall.sendRequest(context, url, null, mFragment, "paytmPaymnetReponse", params, MainActivity.auth_token);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            try {
                                progressBar.setVisibility(VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            getLoanDetails();
                        } else {
                            Toast.makeText(context, "Payment not successful ", Toast.LENGTH_LONG).show();
                        }

                    }
                }

                //0 = {HashMap$Node@8742} "STATUS" -> "TXN_SUCCESS"
//1 = {HashMap$Node@8743} "paymentoption" -> "2"
//2 = {HashMap$Node@8744} "TXNAMOUNT" -> "2.00"
//3 = {HashMap$Node@8745} "TXNID" -> "20190723111212800110168530279646077"
//4 = {HashMap$Node@8746} "lead_id" -> "13893"
                @Override
                public void networkNotAvailable() { // If network is not
                    // available, then this
                    // method gets called.
                }

                @Override
                public void clientAuthenticationFailed(String inErrorMessage) {
                    // This method gets called if client authentication
                    // failed. // Failure may be due to following reasons //
                    // 1. Server error or downtime. // 2. Server unable to
                    // generate checksum or checksum response is not in
                    // proper format. // 3. Server failed to authenticate
                    // that client. That is value of payt_STATUS is 2. //
                    // Error Message describes the reason for failure.
                    Toast.makeText(context, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                    //                    Globle.appendLog(inErrorMessage);
                    StringBuilder s = new StringBuilder();//cb 207np 63w 54more text
                    s.append("inErrorMessage-");
                    s.append(inErrorMessage);
                    Globle.appendLog(String.valueOf(s));

                }

                @Override
                public void onErrorLoadingWebPage(int iniErrorCode,
                                                  String inErrorMessage, String inFailingUrl) {
                    Toast.makeText(context, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();

                    StringBuilder s = new StringBuilder();//cb 207np 63w 54more text
                    s.append("inErrorMessage-");
                    s.append(inErrorMessage);
                    s.append(" inFailingUrl-");
                    s.append(inFailingUrl);
                    Globle.appendLog(String.valueOf(s));

                }

                // had to be added: NOTE
                @Override
                public void onBackPressedCancelTransaction() {
                    Toast.makeText(context, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                    Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                    Toast.makeText(context, "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    StringBuilder s = new StringBuilder();//cb 207np 63w 54more text
                    s.append("inErrorMessage-");
                    s.append(inErrorMessage);
                    Globle.appendLog(String.valueOf(s));

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void paytmPaymnetReponse(JSONObject jsonData) {

        String status = jsonData.optString("status");
        String message = jsonData.optString("message");

        if (status.equalsIgnoreCase("1")) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(VISIBLE);
            getLoanDetails();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        }

    }

    public void getLoanDetails() {
        try {
            String url = MainActivity.mainUrl + "laf/getLoanDetails";
            Map<String, String> params = new HashMap<String, String>();
//            params.put("lead_id", "549");
            params.put("lead_id", MainActivity.lead_id);
            if (!Globle.isNetworkAvailable(context)) {
                Toast.makeText(context, R.string.please_check_your_network_connection, Toast.LENGTH_SHORT).show();
            } else {
                VolleyCall volleyCall = new VolleyCall();//http://192.168.0.110/eduvanzapi/dashboard/getStudentDashbBoardStatus
                volleyCall.sendRequest(context, url, null, mFragment, "getLoanDetails", params, MainActivity.auth_token);
            }
        } catch (Exception e) {
            String errorLine = String.valueOf(e.getStackTrace()[0]);
        }

    }

    public void setDigioDocumentIdForStudent(JSONObject jsonData) {//{"result":{"documentId":"DID180627125727122W11P5DAJQX5NZ2","email":"vijay.shukla@eduvanz.in"},"status":1,"message":"Please follow the instructions to esign the form."}
        try {
            Log.e("SERVER CALL", "getDocuments" + jsonData);
            String status = jsonData.optString("status");
            String message = jsonData.optString("message");

            if (status.equalsIgnoreCase("1")) {

                JSONObject jsonObject = jsonData.getJSONObject("result");

                String documentID = jsonObject.getString("documentId");//DID180627125727122W11P5DAJQX5NZ2
                String email = jsonObject.getString("email");//vijay.shukla@eduvanz.in

                // Invoke Esign
//                LoanTabActivity.initDigio(documentID,email);

                final Digio digio = new Digio();
                DigioConfig digioConfig = new DigioConfig();
                digioConfig.setLogo("https://lh3.googleusercontent.com/v6lR_JSsjovEzLBkHPYPbVuw1161rkBjahSxW0d38RT4f2YoOYeN2rQSrcW58MAfuA=w300"); //Your company logo
                digioConfig.setEnvironment(DigioEnvironment.PRODUCTION);   //Stage is sandbox
                digioConfig.setServiceMode(DigioServiceMode.OTP);

                try {
                    digio.init((Activity) context, digioConfig); //
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    digio.esign(documentID, email);
                } catch (Exception e) {
                    String errorLine = String.valueOf(e.getStackTrace()[0]);
                }

            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            String errorLine = String.valueOf(e.getStackTrace()[0]);
        }
    }

    // Callback listener functions for Digio

    public void onSigningSuccess(String documentId, String message) {
        Log.e(MainActivity.TAG, "onSigningSuccessFrg2: ");
//        Toast.makeText(context, documentId +" " + message, Toast.LENGTH_SHORT).show();
        digioSuccess(documentId);//DID180802180658447Q6OOLIITSFR2DJ
    }

    public void onSigningFailure(String documentId, int code, String response) {
        Log.e(MainActivity.TAG, "onSigningFailureFragment: ");
        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
        digioFailure(documentId, code, response);
    }

    public static void digioSuccess(String documentId) {

        /** API CALL **/
        try {
            String ipaddress = Utils.getIPAddress(true);
            String url = MainActivity.mainUrl + "laf/onSuccessfulRegisterStudentESignCase";
            Map<String, String> params = new HashMap<String, String>();
            VolleyCall volleyCall = new VolleyCall();
            if (!Globle.isNetworkAvailable(context)) {
                Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();

            } else {
                params.put("logged_id", LoanTabActivity.student_id);                //1290
                params.put("lead_id", LoanTabActivity.lead_id);//6138
                params.put("created_by_ip", ipaddress);                             //192.168.1.16
                volleyCall.sendRequest(context, url, null, mFragment, "onSuccessfulRegisterStudentESignCase", params, MainActivity.auth_token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onSuccessfulRegisterStudentESignCase(JSONObject jsonData) {
        try {//{"result":{"docPath":"http:\/\/eduvanz.com\/admin\/"},"status":1,"message":"Document Successfully Signed.You Can Now Pay The Processing Fees To Submit Your Application."}
            Log.e("SERVER CALL", "onSuccessfulRegisterStudentESignCase" + jsonData);
            String status = jsonData.optString("status");
            String message = jsonData.optString("message");

            if (status.equalsIgnoreCase("1")) {

                JSONObject jsonObject = jsonData.getJSONObject("result");
//                downloadSignedUrl = jsonObject.getString("docPath");
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                getLoanDetails();
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void digioFailure(String documentId, int code, String response) {
//        radioButtonEsign.setChecked(false);
        Log.e(MainActivity.TAG, "onSigningFailureFragment: ");
    }

    public void setLoanDetails(JSONObject jsonDataO) {
        Log.e(TAG, "setLoanDetails: " + jsonDataO);
        try {
            try {
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jsonDataO.getInt("status") == 1) {

                String message = jsonDataO.getString("message");
                Drawable bg, bg1;
                if (!jsonDataO.get("loanData").equals(null)) {

                    JSONObject jsonloanDataDetails = jsonDataO.getJSONObject("loanData");

                    if (jsonloanDataDetails.has("application_loan_id")) {

                        try {
                            linHasLoan.setVisibility(VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            linNoLoan.setVisibility(GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!jsonloanDataDetails.getString("lead_id").toString().equals("null"))
                            lead_id = jsonloanDataDetails.getString("lead_id");
                        if (!jsonloanDataDetails.getString("application_loan_id").toString().equals("null"))
                            application_loan_id = jsonloanDataDetails.getString("application_loan_id");

                        if (!jsonloanDataDetails.getString("principal_amount").toString().equals("null"))
                            principal_amount = jsonloanDataDetails.getString("principal_amount");
                        if (!jsonloanDataDetails.getString("down_payment").toString().equals("null"))
                            down_payment = jsonloanDataDetails.getString("down_payment");
                        if (!jsonloanDataDetails.getString("rate_of_interest").toString().equals("null"))
                            rate_of_interest = jsonloanDataDetails.getString("rate_of_interest");
                        if (!jsonloanDataDetails.getString("tenure").toString().equals("null"))
                            tenure = jsonloanDataDetails.getString("tenure");
                        if (!jsonloanDataDetails.getString("emi_type").toString().equals("null"))
                            emi_type = jsonloanDataDetails.getString("emi_type");
                        if (!jsonloanDataDetails.getString("emi_amount").toString().equals("null"))
                            emi_amount = jsonloanDataDetails.getString("emi_amount");
                        if (!jsonloanDataDetails.getString("total_amount_to_be_collected").toString().equals("null"))
                            total_amount_to_be_collected = jsonloanDataDetails.getString("total_amount_to_be_collected");
                        if (!jsonloanDataDetails.getString("amount_to_be_paid_to_institute").toString().equals("null"))
                            amount_to_be_paid_to_institute = jsonloanDataDetails.getString("amount_to_be_paid_to_institute");
                        if (!jsonloanDataDetails.getString("is_moratorium").toString().equals("null"))
                            is_moratorium = jsonloanDataDetails.getString("is_moratorium");
                        if (!jsonloanDataDetails.getString("moratorium_months").toString().equals("null"))
                            moratorium_months = jsonloanDataDetails.getString("moratorium_months");
                        if (!jsonloanDataDetails.getString("moratorium_type").toString().equals("null"))
                            moratorium_type = jsonloanDataDetails.getString("moratorium_type");
                        if (!jsonloanDataDetails.getString("no_emi_paid").toString().equals("null"))
                            no_emi_paid = jsonloanDataDetails.getString("no_emi_paid");
                        if (!jsonloanDataDetails.getString("requested_loan_amount").toString().equals("null"))
                            requested_loan_amount = jsonloanDataDetails.getString("requested_loan_amount");
                        if (!jsonloanDataDetails.getString("requested_tenure").toString().equals("null"))
                            requested_tenure = jsonloanDataDetails.getString("requested_tenure");
                        if (!jsonloanDataDetails.getString("requested_roi").toString().equals("null"))
                            requested_roi = jsonloanDataDetails.getString("requested_roi");
                        if (!jsonloanDataDetails.getString("requested_emi").toString().equals("null"))
                            requested_emi = jsonloanDataDetails.getString("requested_emi");
                        if (!jsonloanDataDetails.getString("offered_amount").toString().equals("null"))
                            offered_amount = jsonloanDataDetails.getString("offered_amount");
                        if (!jsonloanDataDetails.getString("applicant_id").toString().equals("null"))
                            applicant_id = jsonloanDataDetails.getString("applicant_id");
                        if (!jsonloanDataDetails.getString("fk_lead_id").toString().equals("null"))
                            fk_lead_id = jsonloanDataDetails.getString("fk_lead_id");
                        if (!jsonloanDataDetails.getString("first_name").toString().equals("null"))
                            first_name = jsonloanDataDetails.getString("first_name");
                        if (!jsonloanDataDetails.getString("last_name").toString().equals("null"))
                            last_name = jsonloanDataDetails.getString("last_name");
                        if (!jsonloanDataDetails.getString("mobile_number").toString().equals("null"))
                            mobile_number = jsonloanDataDetails.getString("mobile_number");
                        if (!jsonloanDataDetails.getString("email_id").toString().equals("null"))
                            email_id = jsonloanDataDetails.getString("email_id");
                        if (!jsonloanDataDetails.getString("kyc_address").toString().equals("null"))
                            kyc_address = jsonloanDataDetails.getString("kyc_address");
                        if (!jsonloanDataDetails.getString("course_cost").toString().equals("null"))
                            course_cost = jsonloanDataDetails.getString("course_cost");
                        if (!jsonloanDataDetails.getString("paid_on").toString().equals("null"))
                            paid_on = jsonloanDataDetails.getString("paid_on");
                        if (!jsonloanDataDetails.getString("transaction_amount").toString().equals("null"))
                            transaction_amount = jsonloanDataDetails.getString("transaction_amount");
                        if (!jsonloanDataDetails.getString("kyc_status").toString().equals("null"))
                            kyc_status = jsonloanDataDetails.getString("kyc_status");
                        if (!jsonloanDataDetails.getString("disbursal_status").toString().equals("null"))
                            disbursal_status = jsonloanDataDetails.getString("disbursal_status");
                        if (!jsonloanDataDetails.getString("rate").toString().equals("null"))
                            rate = jsonloanDataDetails.getString("rate");
                        if (!jsonloanDataDetails.getString("loan_agrement_upload_status").toString().equals("null"))
                            loan_agrement_upload_status = jsonloanDataDetails.getString("loan_agrement_upload_status");
                        if (!jsonloanDataDetails.getString("no_of_advance_emi").toString().equals("null"))
                            no_of_advance_emi = jsonloanDataDetails.getString("no_of_advance_emi");

                        txtApplicationLoanID.setText(application_loan_id);
                        txtProcessingFeeAmt.setText(String.valueOf(Double.valueOf(transaction_amount)));
                        totalAmount = Double.valueOf(transaction_amount);
                        if (!no_of_advance_emi.equals("")) {
                            txtEMIFeeAmt.setText(String.valueOf(Float.parseFloat(emi_amount) * Float.parseFloat(no_of_advance_emi)));
                        } else {
                            txtEMIFeeAmt.setText(String.valueOf(Double.valueOf(emi_amount)));
                        }
                        txtTotalAmt.setText(String.valueOf(Double.valueOf(transaction_amount)));

//                        if (paid_on.length() > 5) {
//                            linPayBtn.setVisibility(GONE);
//                            linPayStatus.setVisibility(VISIBLE);
//                        } else {
//                            linPayBtn.setVisibility(VISIBLE);
//                            linPayStatus.setVisibility(GONE);
//                        }

                        txtTbCourseFee.setText(" " + Globle.decimalFormat.format(Float.parseFloat(course_cost)) + "/-");
                        txtTbRequestedLoanAmount.setText(" " + Globle.decimalFormat.format(Float.parseFloat(requested_loan_amount)) + "/-");
//                        txtTbOfferedLoanAmount.setText(" " + offered_amount + "/-");
//                        txtTbSelectRateOFInterest.setText(requested_roi + " " + "%");
//                        txtTbSelectedEMIAmount.setText(" " + emi_amount + "/-");
                        txtTbSanctionLoanAmount.setText(" " + Globle.decimalFormat.format(Float.parseFloat(principal_amount)) + "/-");
                        txtTbDownpayment.setText(" " + Globle.decimalFormat.format(Float.parseFloat(down_payment)) + "/-");
                        txtTbROI.setText(rate_of_interest + " " + "%");
                        txtTbEMI.setText(" " + Globle.decimalFormat.format(Float.parseFloat(emi_amount)) + "/-");
                        txtTbProcessingFee.setText(" " + Globle.decimalFormat.format(Float.parseFloat(transaction_amount)) + "/-");

                        txtTbTotalRepaybleAmount.setText(" " + Globle.decimalFormat.format(Float.parseFloat(total_amount_to_be_collected)) + "/-");
                        txtTbNoOfAdvanceEMI.setText(no_of_advance_emi);

                        if (moratorium_type.equals("2")) {
                            txtTbMoratoriumType.setText("Total");
                        } else if (moratorium_type.equals("1")) {
                            txtTbMoratoriumType.setText("Principal");
                        }

                        if (emi_type.equals("0")) {
                            txtTbEMIType.setText("Arrear");
                            txtTbAdvanceEMIAmount.setText(" " + "0");

                        } else if (emi_type.equals("1")) {
                            txtTbEMIType.setText("Advance");
                            linEMIFeeRb.setVisibility(VISIBLE);
                            float advEMI;
                            if (!emi_amount.equals("")) {
                                advEMI = Float.parseFloat(emi_amount) * Float.parseFloat(no_of_advance_emi);
                            } else {
                                advEMI = Float.parseFloat(emi_amount);
                            }
                            txtTbAdvanceEMIAmount.setText(" " + advEMI + "/-");

                        } else {
                            txtTbEMIType.setText(" -");
                        }

                        if (!is_moratorium.equals("")) {
                            txtTbMoratorium.setText(" " + (is_moratorium.equals("1") ? "Yes" : "No"));
                        }
                        if (!moratorium_months.equals("")) {
                            txtTbMoratoriumDuration.setText(" " + moratorium_months);
                        }
//                        if (!amount_to_be_paid_to_institute.equals("")) {
//                            txtTbEMIDuringMoratorium.setText(" " + amount_to_be_paid_to_institute);
//                        }
                        try {
                            if (emi_type.equals("0") && is_moratorium.equals("1") && Integer.parseInt(moratorium_months) > 0) {
                                int grossTenure = Integer.parseInt(tenure) - Integer.parseInt(moratorium_months);
                                txtTbGrossLoanTenure.setText(" " + grossTenure);
                            } else {
                                txtTbGrossLoanTenure.setText(" " + Integer.parseInt(tenure));
                            }
                        } catch (NumberFormatException e) {
                            txtTbGrossLoanTenure.setText(" -");
                        }

                        try {
                            if (emi_type.equals("0")) {
                                int grossTenure = Integer.parseInt(tenure);
                                txtTbNetLoantenure.setText(" " + grossTenure);
                            } else if (emi_type.equals("1")) {
                                int grossLoanTenure = Integer.parseInt(tenure) - Integer.parseInt(no_of_advance_emi);
                                txtTbNetLoantenure.setText(" " + grossLoanTenure);
                            }
                        } catch (NumberFormatException e) {
                            txtTbNetLoantenure.setText(" -");
                        }

                        try {
                            Float outstanding_principle = Float.parseFloat(amount_to_be_paid_to_institute);
                            Float Rate = Float.parseFloat(rate) / 1200;
                            Float interest_comp = outstanding_principle * Rate;

                            txtTbEMIDuringMoratorium.setText(" " + (is_moratorium.equals("1") ? Globle.decimalFormat.format(interest_comp) + "/-" : " /-"));

                        } catch (NumberFormatException e) {
                            txtTbEMIDuringMoratorium.setText(" -");
                        }

                        if (kyc_status.equals("1")) {
                            txtProcessingFeeDueTitle.setText(" Paid On");
                            txtProcessingFeeDueByDate.setText(Globle.dateFormater4(paid_on));
                            linPayBtn.setVisibility(GONE);

                            Drawable bg5;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                bg5 = VectorDrawableCompat.create(context.getResources(), drawable.ic_check_circle_green, null);
                                ivProcessingFeesicon.setColorFilter(context.getResources().getColor(color.colorGreen), PorterDuff.Mode.MULTIPLY);
                            } else {
                                bg5 = ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green);
                                DrawableCompat.setTint(bg5, context.getResources().getColor(color.colorGreen));
                            }
                            ivProcessingFeesicon.setImageDrawable(bg5);
//                            linPayStatus.setVisibility(VISIBLE);

                        } else {
                            txtProcessingFeeDueTitle.setText(" Paid on");
                            txtProcessingFeeDueByDate.setText(" --");
                            linPayBtn.setVisibility(VISIBLE);

                            Drawable bg7;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                bg7 = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_borrower_documents_pending, null);
                                ivProcessingFeesicon.setColorFilter(context.getResources().getColor(color.colorYellow), PorterDuff.Mode.MULTIPLY);
                            } else {
                                bg7 = ContextCompat.getDrawable(context, R.drawable.ic_borrower_documents_pending);
                                DrawableCompat.setTint(bg7, context.getResources().getColor(color.colorYellow));
                            }
                            ivProcessingFeesicon.setImageDrawable(bg7);

//                            linPayStatus.setVisibility(GONE);
                        }

                        //Set Default Processing Fee Selected

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_radio_icon, null);
                            ivProcessingFee.setColorFilter(context.getResources().getColor(color.colorGreen), PorterDuff.Mode.MULTIPLY);
                        } else {
                            bg = ContextCompat.getDrawable(context, R.drawable.ic_radio_icon);
                            DrawableCompat.setTint(bg, context.getResources().getColor(color.colorGreen));
                        }
                        ivProcessingFee.setImageDrawable(bg);

                        if (emi_type.equals("1")) {
                            try {
                                linEMIFeeRb.setVisibility(VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (Integer.parseInt(no_emi_paid) >= 1) {

                                if (kyc_status.equals("1")) {
                                    linPayBtn.setVisibility(GONE);
                                    linPayStatus.setVisibility(VISIBLE);
                                    linProcessingFeeRb.setEnabled(false);
                                    linEMIFeeRb.setEnabled(false);

                                    totalAmount = Double.valueOf(txtProcessingFeeAmt.getText().toString().trim()) + Double.valueOf(txtTbAdvanceEMIAmount.getText().toString().split("/")[0]);
                                    txtTotalAmt.setText(String.valueOf(totalAmount));

                                } else {
                                    linPayBtn.setVisibility(VISIBLE);
                                    linPayStatus.setVisibility(GONE);
                                    linProcessingFeeRb.setEnabled(true);
                                }

                                txtEMIDueByTitle.setText(" Paid On");
                                linEMIFeeRb.setVisibility(VISIBLE);
                                linEMIFeeRb.setEnabled(false);
                                txtEMIDueByDate.setText(" - ");

                                try {
                                    if (!jsonDataO.get("advanceEmi").equals(null)) {

                                        JSONObject jsonloanDataadvanceEmi = jsonDataO.getJSONObject("advanceEmi");
                                        if (!jsonloanDataadvanceEmi.getString("paid_emi_date").toString().equals("null")) {

                                            paid_emi_on = jsonloanDataadvanceEmi.getString("paid_emi_date");
                                            txtEMIDueByTitle.setText(" Paid On");
                                            txtEMIDueByDate.setText(Globle.dateFormater5(paid_emi_on));
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                    bg1 = VectorDrawableCompat.create(context.getResources(), drawable.ic_radio_icon_emi, null);
                                    ivEMIFee.setColorFilter(context.getResources().getColor(color.colorGreen), PorterDuff.Mode.MULTIPLY);
                                } else {
                                    bg1 = ContextCompat.getDrawable(context, R.drawable.ic_radio_icon_emi);
                                    DrawableCompat.setTint(bg1, context.getResources().getColor(color.colorGreen));
                                }
                                ivEMIFee.setImageDrawable(bg1);
                                //emi icon set green

                                Drawable bg5;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                    bg5 = VectorDrawableCompat.create(context.getResources(), drawable.ic_check_circle_green, null);
                                    ivAdavnceFeeIcon.setColorFilter(context.getResources().getColor(color.colorGreen), PorterDuff.Mode.MULTIPLY);
                                } else {
                                    bg5 = ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green);
                                    DrawableCompat.setTint(bg5, context.getResources().getColor(color.colorGreen));
                                }
                                ivAdavnceFeeIcon.setImageDrawable(bg5);
                            } else {
                                //emi icon set traingle

                                txtEMIDueByTitle.setText(" Paid On");
                                txtEMIDueByDate.setText(" -- ");
                                linEMIFeeRb.setEnabled(true);
                                linPayBtn.setVisibility(VISIBLE);
                                linPayStatus.setVisibility(GONE);

                                Drawable bg7;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                    bg7 = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_borrower_documents_pending, null);
                                    ivAdavnceFeeIcon.setColorFilter(context.getResources().getColor(color.colorYellow), PorterDuff.Mode.MULTIPLY);
                                } else {
                                    bg7 = ContextCompat.getDrawable(context, R.drawable.ic_borrower_documents_pending);
                                    DrawableCompat.setTint(bg7, context.getResources().getColor(color.colorYellow));
                                }
                                ivAdavnceFeeIcon.setImageDrawable(bg7);

                                if (kyc_status.equals("1")) {

                                    //processing icon green
                                    Drawable bg5;
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                        bg5 = VectorDrawableCompat.create(context.getResources(), drawable.ic_check_circle_green, null);
                                        ivProcessingFeesicon.setColorFilter(context.getResources().getColor(color.colorGreen), PorterDuff.Mode.MULTIPLY);
                                    } else {
                                        bg5 = ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green);
                                        DrawableCompat.setTint(bg5, context.getResources().getColor(color.colorGreen));
                                    }
                                    ivProcessingFeesicon.setImageDrawable(bg5);

                                    totalAmount = Double.valueOf(txtProcessingFeeAmt.getText().toString().trim()) + Double.valueOf(txtTbAdvanceEMIAmount.getText().toString().split("/")[0]);
                                    txtTotalAmt.setText(String.valueOf(totalAmount));
//                                    totalAmount = Double.valueOf(emi_amount);
//                                    txtTotalAmt.setText(String.valueOf(Double.valueOf(emi_amount)));
                                    linProcessingFeeRb.setEnabled(false);
                                } else {
                                    //yellow
                                    linProcessingFeeRb.setEnabled(true);
                                    Drawable bg6;
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                        bg6 = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_borrower_documents_pending, null);
                                        ivProcessingFeesicon.setColorFilter(context.getResources().getColor(color.colorYellow), PorterDuff.Mode.MULTIPLY);
                                    } else {
                                        bg6 = ContextCompat.getDrawable(context, R.drawable.ic_borrower_documents_pending);
                                        DrawableCompat.setTint(bg6, context.getResources().getColor(color.colorYellow));
                                    }
                                    ivProcessingFeesicon.setImageDrawable(bg6);
                                }
                            }
                        } else {
                            //No Advance EMI
                            if (kyc_status.equals("1")) {
                                //procee fee icon set green
                                linPayBtn.setVisibility(GONE);
                                linPayStatus.setVisibility(VISIBLE);
                                linProcessingFeeRb.setEnabled(false);

                                Drawable bg5;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                    bg5 = VectorDrawableCompat.create(context.getResources(), drawable.ic_check_circle_green, null);
                                    ivProcessingFeesicon.setColorFilter(context.getResources().getColor(color.colorGreen), PorterDuff.Mode.MULTIPLY);
                                } else {
                                    bg5 = ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green);
                                    DrawableCompat.setTint(bg5, context.getResources().getColor(color.colorGreen));
                                }
                                ivProcessingFeesicon.setImageDrawable(bg5);


                            } else {

                                linPayBtn.setVisibility(VISIBLE);
                                linPayStatus.setVisibility(GONE);
                                linProcessingFeeRb.setEnabled(true);

                                //procee fee icon set yellow
                                Drawable bg3;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                    bg3 = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_borrower_documents_pending, null);
                                    ivProcessingFeesicon.setColorFilter(context.getResources().getColor(color.colorYellow), PorterDuff.Mode.MULTIPLY);
                                } else {
                                    bg3 = ContextCompat.getDrawable(context, R.drawable.ic_borrower_documents_pending);
                                    DrawableCompat.setTint(bg, context.getResources().getColor(color.colorYellow));
                                }
                                ivProcessingFeesicon.setImageDrawable(bg3);
                            }
                            try {
                                linEMIFeeRb.setVisibility(GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (!jsonDataO.get("result").equals(null)) {

                            baseUrl = new JSONObject(jsonDataO.getJSONObject("result").toString()).getString("baseUrl");
                        }

                        //href="http://159.89.204.41/eduhtmlbeta/agreement/downloadSignedAgreement/415"
                        //href="http://159.89.204.41/eduvanzbeta/download/downloadENach/415">Download NACH</a

                        if (disbursal_status.equals("0") || disbursal_status.equals("1")) //Disbursed
                        {
                            txtLeadDisbursedStatus.setText("Disbursal Pending");
                            txtLeadDisbursedStatus.setTextColor(context.getResources().getColor(color.darkblue));
                            ivLeadDisbursed.setBackground(context.getResources().getDrawable(drawable.ic_borrower_documents_pending));
                            linDisbursed.setBackground(context.getResources().getDrawable(drawable.border_circular_yellow_filled));
                            linDownloadNach.setVisibility(GONE);
                        } else {
                            txtLeadDisbursedStatus.setText("Loan Disbursed");
                            linDisbursed.setBackgroundResource(R.color.colorGreen);
                            txtLeadDisbursedStatus.setTextColor(context.getResources().getColor(color.white));
                            ivLeadDisbursed.setBackground(context.getResources().getDrawable(drawable.ic_check_circle_white));
                            linDisbursed.setBackground(context.getResources().getDrawable(drawable.border_circular_green_filled));
                            linDownloadNach.setTag(baseUrl.concat("download/downloadENach/").concat(MainActivity.lead_id));
//                        linDownloadNach.setTag(MainActivity.mainUrl.toString().replace("eduvanzApi/", "").concat("eduvanzbeta/download/downloadENach/").concat(MainActivity.lead_id));
                        }

                        if (loan_agrement_upload_status.equals("1")) {
                            linAggSignInBtn.setVisibility(GONE);
                            linDownloadSignedAgreement.setTag(VISIBLE);
                            linDownloadSignedAgreement.setVisibility(VISIBLE);
                            txtAggSignedStatus.setText("Agreement Signed");
                            txtAggSignedStatus.setTextColor(context.getResources().getColor(color.white));
                            ivAggSigned.setBackground(context.getResources().getDrawable(drawable.ic_check_circle_white));
                            linAgreementSigned.setBackground(context.getResources().getDrawable(drawable.border_circular_green_filled));
                            genrateSignedAgreementUrl();
//                            linDownloadSignedAgreement.setTag(baseUrl.concat("agreement/downloadSignedAgreement/").concat(MainActivity.lead_id));
//                          linDownloadSignedAgreement.setTag(MainActivity.mainUrl.toString().replace("eduvanzApi/", "").concat("eduhtmlbeta/agreement/downloadSignedAgreement/").concat(MainActivity.lead_id));

                        } else {
                            linAggSignInBtn.setVisibility(VISIBLE);
                            linDownloadSignedAgreement.setVisibility(GONE);
                            txtAggSignedStatus.setText("Agreement Signed Pending");
                            txtAggSignedStatus.setTextColor(context.getResources().getColor(color.darkblue));
                            ivAggSigned.setBackground(context.getResources().getDrawable(drawable.ic_borrower_documents_pending));
                            linAgreementSigned.setBackground(context.getResources().getDrawable(drawable.border_circular_yellow_filled));
                        }
                    } else {
                        try {
                            linHasLoan.setVisibility(GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            linNoLoan.setVisibility(VISIBLE);
//                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(((LoanTabActivity) context));
//                                // ...Irrelevant code for customizing the buttons and title
//                                LayoutInflater inflater = ((LoanTabActivity) context).getLayoutInflater();
//                                View dialogView = inflater.inflate(layout.thanksdialog, null);
//                                dialogBuilder.setView(dialogView);
//                                LinearLayout buttonSave = dialogView.findViewById(R.id.linDashBoard);
//                                final AlertDialog alertDialog = dialogBuilder.create();
//                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                alertDialog.show();
//
//                                buttonSave.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        alertDialog.dismiss();
////                                        LoanTabActivity.super.onBackPressed();
//                                    }
//                                });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    try {
                        linHasLoan.setVisibility(GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        linNoLoan.setVisibility(VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    if (!jsonDataO.get("coApplicant").equals(null)) {

                        JSONObject jsonObjectCoBo = jsonDataO.getJSONObject("coApplicant");

                        if (!jsonObjectCoBo.getString("mobile_number").toString().equals("null"))
                            mobile_numberCoBo = jsonObjectCoBo.getString("mobile_number");

                        if (!jsonObjectCoBo.getString("applicant_id").toString().equals("null"))
                            applicant_idCoBo = jsonObjectCoBo.getString("applicant_id");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if (!jsonDataO.get("nachData").equals(null)) {
                JSONArray jsonArray1 = null;
                try {
                    jsonArray1 = jsonDataO.getJSONArray("nachData");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonArray1 != null) {

                    try {
                        linNachList.setVisibility(VISIBLE);
                        linDownloadNach.setVisibility(VISIBLE);
                        linDownloadNach.setTag(baseUrl.concat("download/downloadENach/").concat(MainActivity.lead_id));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mNachArrayList = new ArrayList<>();

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        MNach mNach = new MNach();
                        JSONObject jsonEmiDetails = jsonArray1.getJSONObject(i);

                        try {
                            if (!jsonEmiDetails.getString("person_name").toString().equals("null"))
                                mNach.person_name = jsonEmiDetails.getString("person_name");

                            if (!jsonEmiDetails.getString("amount").toString().equals("null"))
                                mNach.amount = jsonEmiDetails.getString("amount");

                            if (!jsonEmiDetails.getString("umrn_num").toString().equals("null"))
                                mNach.umrn_num = jsonEmiDetails.getString("umrn_num");

                            if (!jsonEmiDetails.getString("end_date").toString().equals("null"))
                                mNach.end_date = jsonEmiDetails.getString("end_date");

                            if (!jsonEmiDetails.getString("until_cancel").toString().equals("null"))
                                mNach.until_cancel = jsonEmiDetails.getString("until_cancel");

                            if (!jsonEmiDetails.getString("frequency").toString().equals("null"))

                                switch (jsonEmiDetails.getString("frequency").toString()) {
                                    case "1":
                                        mNach.frequency = "Monthly";
                                        break;

                                    case "2":
                                        mNach.frequency = "Quarterly";
                                        break;

                                    case "3":
                                        mNach.frequency = "Half-Yearly";
                                        break;

                                    case "4":
                                        mNach.frequency = "Yearly";
                                        break;

                                    case "5":
                                        mNach.frequency = "As and when presented";
                                        break;
                                }
//                            mNach.frequency = jsonEmiDetails.getString("frequency");

                            if (!jsonEmiDetails.getString("debit_type").toString().equals("null"))
                                mNach.debit_type = jsonEmiDetails.getString("debit_type");

                            if (!jsonEmiDetails.getString("status").toString().equals("null"))
                                mNach.status = jsonEmiDetails.getString("status");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mNachArrayList.add(mNach);
                    }
                    adapter = new NachAdapter(mNachArrayList, context, getActivity());
                    rvNach.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    try {
                        linNachList.setVisibility(GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            try {
                progressBar.setVisibility(View.GONE);
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }

    }

    public void genrateSignedAgreementUrl() {

        try {
            String url = MainActivity.mainUrl + "laf/downloadSignedAgreement";
            Map<String, String> params = new HashMap<String, String>();
            params.put("lead_id", MainActivity.lead_id);
            if (!Globle.isNetworkAvailable(context)) {
                Toast.makeText(context, R.string.please_check_your_network_connection, Toast.LENGTH_SHORT).show();
            } else {
                VolleyCall volleyCall = new VolleyCall();//http://192.168.0.110/eduvanzapi/dashboard/getStudentDashbBoardStatus
                volleyCall.sendRequest(context, url, null, mFragment, "genrateSignedAgreementUrl", params, MainActivity.auth_token);
            }
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String name = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String errorMsg = e.getMessage();
            String errorMsgDetails = e.getStackTrace().toString();
            String errorLine = String.valueOf(e.getStackTrace()[0]);
            Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
        }
    }

    public void setSignedAgreementUrl(JSONObject jsonDataO) {
        Log.e(TAG, "setLoanDetails: " + jsonDataO);
        try {
            String message = jsonDataO.getString("message");
            if (jsonDataO.getInt("status") == 1) {
                JSONObject mData2 = jsonDataO.getJSONObject("result");
                linDownloadSignedAgreement.setTag(mData2.getString("baseUrl").concat(jsonDataO.getString("Url")));
                downloadUrl = mData2.getString("baseUrl").concat(jsonDataO.getString("Url"));
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String name = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String errorMsg = e.getMessage();
            String errorMsgDetails = e.getStackTrace().toString();
            String errorLine = String.valueOf(e.getStackTrace()[0]);
            Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
        }

    }

    public void genrateManualAgreement() {

        try {
            String url = MainActivity.mainUrl + "laf/genrateAgreement";
            Map<String, String> params = new HashMap<String, String>();
            params.put("lead_id", MainActivity.lead_id);
            if (!Globle.isNetworkAvailable(context)) {
                Toast.makeText(context, R.string.please_check_your_network_connection, Toast.LENGTH_SHORT).show();
            } else {
                progressBarDiag.setVisibility(View.VISIBLE);
                VolleyCall volleyCall = new VolleyCall();//http://192.168.0.110/eduvanzapi/dashboard/getStudentDashbBoardStatus
                volleyCall.sendRequest(context, url, null, mFragment, "genrateManualAgreement", params, MainActivity.auth_token);
            }
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String name = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String errorMsg = e.getMessage();
            String errorMsgDetails = e.getStackTrace().toString();
            String errorLine = String.valueOf(e.getStackTrace()[0]);
            Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
        }
    }

    public void setgenrateManualAgreement(JSONObject jsonDataO) {
//        Log.e(TAG, "setLoanDetails: " + jsonDataO);
        try {//{"result":{"baseUrl":"http:\/\/159.89.204.41\/eduvanzbeta\/","Url":"uploads\/lamanualupload\/557\/Eduvanz_Agreement.pdf"},
            // "status":0,"message":"Agreement generated"}
            String message = jsonDataO.getString("message");
            progressBarDiag.setVisibility(View.GONE);
            if (jsonDataO.getInt("status") == 1) {
                JSONObject mData2 = jsonDataO.getJSONObject("result");
                linDownload.setTag(mData2.getString("baseUrl").concat(mData2.getString("Url")));
                downloadUrl = mData2.getString("baseUrl").concat(mData2.getString("Url"));
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String name = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String errorMsg = e.getMessage();
            String errorMsgDetails = e.getStackTrace().toString();
            String errorLine = String.valueOf(e.getStackTrace()[0]);
            Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
        }

    }

    public void genrateOTPAgreement(String mobile,String UserType) {

        try {
            String url = MainActivity.mainUrl + "laf/getAggForOTPSign";
            Map<String, String> params = new HashMap<String, String>();
            params.put("lead_id", MainActivity.lead_id);
            params.put("mobile", mobile);
            if (!Globle.isNetworkAvailable(context)) {
                Toast.makeText(context, R.string.please_check_your_network_connection, Toast.LENGTH_SHORT).show();
            } else {

                if(UserType.equals("1")){
                    edtOtpBo.setEnabled(true);
                    edtOtpCo.setEnabled(false);

                }else{
                    edtOtpBo.setEnabled(false);
                    edtOtpCo.setEnabled(true);
                }
                progressBarDiag.setVisibility(VISIBLE);

                VolleyCall volleyCall = new VolleyCall();//http://192.168.0.110/eduvanzapi/dashboard/getStudentDashbBoardStatus
                volleyCall.sendRequest(context, url, null, mFragment, "genrateOTPAgreement", params, MainActivity.auth_token);
            }
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String name = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String errorMsg = e.getMessage();
            String errorMsgDetails = e.getStackTrace().toString();
            String errorLine = String.valueOf(e.getStackTrace()[0]);
            Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
        }
    }

    public void setgenrateOTPAgreement(JSONObject jsonDataO) {
        try {//{"result":{"baseUrl":"http:\/\/159.89.204.41\/eduvanzbeta\/","Url":"uploads\/lamanualupload\/557\/Eduvanz_Agreement.pdf"},
            // "status":0,"message":"Agreement generated"}
            //{"result":{"baseUrl":"http:\/\/159.89.204.41\/eduvanzbeta\/"},"status":1,"doc_url":"uploads\/ladocumentstore\/634\/Eduvanz_Agreement.pdf",
            // "message":"success"}
            //
            //{"result":{"baseUrl":"http:\/\/159.89.204.41\/eduvanzbeta\/","Url":"uploads\/lamanualupload\/634\/Eduvanz_Agreement.pdf"},
            // "status":1,"message":"All Users Signing Completed"}
//            {"result":{"baseUrl":"http:\/\/159.89.204.41\/eduvanzbeta\/"},"status":1,
//                    "doc_url":"uploads\/ladocumentstore\/13816\/Eduvanz_Agreement.pdf","message":"success"}
            String message = jsonDataO.getString("message");
            progressBarDiag.setVisibility(GONE);
            if (jsonDataO.getInt("status") == 1) {
                JSONObject mData2 = jsonDataO.getJSONObject("result");
                downloadUrl = mData2.getString("baseUrl").concat(jsonDataO.getString("doc_url"));//{"result":{"baseUrl":"http:\/\/159.89.204.41\/eduvanzbeta\/"},"status":1,"doc_url":52618,"message":"success"}

//                String url = myPdfUrl;
//                String url = "http://docs.google.com/gview?embedded=true&url="+myPdfUrl;
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.loadUrl(url);

//                otpSignInDialog(context);

                openPdf(downloadUrl);

            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String name = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String errorMsg = e.getMessage();
            String errorMsgDetails = e.getStackTrace().toString();
            String errorLine = String.valueOf(e.getStackTrace()[0]);
            Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
        }

    }

    public void submitOTPResponse(JSONObject jsonDataO) {
//        Log.e(TAG, "setLoanDetails: " + jsonDataO);
        try {
            String message = jsonDataO.getString("message");
            progressBarDiag.setVisibility(GONE);
            //{"result":{"baseUrl":"http:\/\/159.89.204.41\/eduvanzbeta\/"},"status":4,"message":"Invalid OTP"}
            // {"result":{"baseUrl":"http:\/\/159.89.204.41\/eduvanzbeta\/",
            // "Url":"uploads\/lamanualupload\/13816\/Eduvanz_Agreement.pdf"},"status":1,"message":"All Users Signing Completed"}
            if (jsonDataO.getInt("status") == 1) {
                JSONObject mData2 = jsonDataO.getJSONObject("result");
                downloadUrl = mData2.getString("baseUrl").concat(mData2.getString("Url"));

                linGetOtpBo.setBackground(context.getResources().getDrawable(R.drawable.border_circular_grey_filled));
                linGetOtpBo.setClickable(false);
                edtOtpBo.setEnabled(false);
                linSubmitOtpBo.setBackground(context.getResources().getDrawable(R.drawable.border_circular_grey_filled));
                linSubmitOtpBo.setClickable(false);

//                cfAlertDialog.dismiss();
//                getLoanDetails();

            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String name = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String errorMsg = e.getMessage();
            String errorMsgDetails = e.getStackTrace().toString();
            String errorLine = String.valueOf(e.getStackTrace()[0]);
            Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
        }

    }

    public void submitOTPResponseCo(JSONObject jsonDataO) {
//        Log.e(TAG, "setLoanDetails: " + jsonDataO);
        try {
            String message = jsonDataO.getString("message");
            progressBarDiag.setVisibility(GONE);
//                {"leadDetails":{"fk_institutes_id":"123"},"result":{"baseUrl":"https:\/\/eduvanz.com\/admin\/","Url":"uploads\/ladocumentstore\/21094\/Eduvanz_Agreement.pdf"},"status":1,"message":"All Users Signing Completed"}
            //{"result":{"baseUrl":"http:\/\/159.89.204.41\/eduvanzbeta\/"},"status":4,"message":"Invalid OTP"}
//             {"result":{"baseUrl":"http:\/\/159.89.204.41\/eduvanzbeta\/","Url":"uploads\/lamanualupload\/13816\/Eduvanz_Agreement.pdf"},"status":1,"message":"All Users Signing Completed"}
            if (jsonDataO.getInt("status") == 1) {
                JSONObject mData2 = jsonDataO.getJSONObject("result");
                downloadUrl = mData2.getString("baseUrl").concat(mData2.getString("Url"));

                linGetOtpCo.setBackground(context.getResources().getDrawable(R.drawable.border_circular_grey_filled));
                linGetOtpCo.setClickable(false);
                edtOtpCo.setEnabled(false);
                linSubmitOtpCo.setBackground(context.getResources().getDrawable(R.drawable.border_circular_grey_filled));
                linSubmitOtpCo.setClickable(false);

//                cfAlertDialog.dismiss();
//                getLoanDetails();

            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String name = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String errorMsg = e.getMessage();
            String errorMsgDetails = e.getStackTrace().toString();
            String errorLine = String.valueOf(e.getStackTrace()[0]);
            Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void manualSignInDialog() {

        viewDiag = getLayoutInflater().inflate(layout.signin_options, null);
        linClose = viewDiag.findViewById(R.id.linClose);
        linDownload = viewDiag.findViewById(R.id.linDownload);
        linUpload = viewDiag.findViewById(R.id.linUpload);
        progressBarDiag = viewDiag.findViewById(R.id.progressBar);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadReceiver, filter);

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .setFooterView(viewDiag);

        builder.setCancelable(false);

        cfAlertDialog = builder.show();
        cfAlertDialog.setCancelable(false);
        try {
            cfAlertDialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cfAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                onDialogDismiss();
            }
        });

        genrateManualAgreement();

        linClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfAlertDialog.dismiss();
            }
        });

        linDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
//                    Log.e(MainActivity.TAG, "downloadUrl+++++: " + downloadUrl);
                    downloadUrl = linDownload.getTag().toString();
                    Uri Download_Uri = Uri.parse(downloadUrl);

                    DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                    request.setAllowedOverRoaming(false);
                    request.setTitle("Downloading LAF Document");
                    //request.setDescription("Downloading " + "Sample" + ".png");
                    request.setVisibleInDownloadsUi(true);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Eduvanz/" + "/" + "LAF" + ".pdf");
                    progressBarDiag.setVisibility(VISIBLE);
                    downLoad(downloadUrl, 1, "LAF");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        linUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    galleryDocIntent();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void openPdf(String mPath) {
        Uri path = Uri.parse(mPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setDataAndType(path, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
//            mFragment.startActivityForResult(intent, 1);
            ((LoanTabActivity) context).mActivity.startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), R.string.no_application_available_ro_view_pdf, Toast.LENGTH_SHORT).show();
        }
    }

    public void otpSignInDialog(Context context) {

        viewDiag = getLayoutInflater().inflate(layout.signin_otp_bo_co, null);

        linClose = viewDiag.findViewById(R.id.linClose);
        linStep2 = viewDiag.findViewById(R.id.linStep2);

        linGetOtpBo = viewDiag.findViewById(R.id.linGetOtpBo);
        layoutOtp = viewDiag.findViewById(R.id.layoutOtp);
        linSubmitOtpBo = viewDiag.findViewById(R.id.linSubmitOtpBo);
        layoutOtpBo = viewDiag.findViewById(R.id.layoutOtpBo);
        edtOtpBo = viewDiag.findViewById(id.edtOtpBo);

        linGetOtpCo = viewDiag.findViewById(R.id.linGetOtpCo);
        linSubmitOtpCo = viewDiag.findViewById(R.id.linSubmitOtpCo);
        layoutOtpCo = viewDiag.findViewById(R.id.layoutOtpCo);
        edtOtpCo = viewDiag.findViewById(id.edtOtpCo);

        progressBarDiag = viewDiag.findViewById(R.id.progressBar);

        linSubmitOtpBo.setBackground(context.getResources().getDrawable(R.drawable.border_circular_grey_filled));
        linSubmitOtpBo.setClickable(false);
        edtOtpBo.setEnabled(false);

        linSubmitOtpCo.setBackground(context.getResources().getDrawable(R.drawable.border_circular_grey_filled));
        linSubmitOtpCo.setClickable(false);
        edtOtpCo.setEnabled(false);

        if (mobile_numberCoBo.length() > 1) {
            linStep2.setVisibility(VISIBLE);
        } else {
            linStep2.setVisibility(GONE);
        }

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .setFooterView(viewDiag);

        builder.setCancelable(false);

        cfAlertDialog = builder.show();
        cfAlertDialog.setCancelable(false);
        try {
            cfAlertDialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cfAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                onDialogDismiss();
            }
        });

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(OTPlength);
        edtOtpBo.setFilters(filterArray);

        edtOtpBo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (edtOtpBo.getText().toString().trim().length() == OTPlength) {

                        linSubmitOtpBo.setBackground(context.getResources().getDrawable(drawable.border_circular_red_filled));
                        linSubmitOtpBo.setClickable(true);
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(edtOtpBo.getWindowToken(), 0);
                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        InputFilter[] filterArray1 = new InputFilter[1];
        filterArray1[0] = new InputFilter.LengthFilter(OTPlength);
        edtOtpCo.setFilters(filterArray1);

        edtOtpCo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (edtOtpCo.getText().toString().trim().length() == OTPlength) {

                        linSubmitOtpCo.setBackground(context.getResources().getDrawable(drawable.border_circular_red_filled));
                        linSubmitOtpCo.setClickable(true);
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(edtOtpCo.getWindowToken(), 0);
                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        linClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfAlertDialog.dismiss();
                getLoanDetails();
            }
        });

        linGetOtpBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                genrateOTPAgreement(DashboardFragmentNew.mobile_no,"1");
            }
        });

        linGetOtpCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                genrateOTPAgreement(mobile_numberCoBo,"2");
            }
        });

        linSubmitOtpBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //otp,mobile,lead_id,online,signed_by_id
                    //in online key i want array which stores as following:
                    //IP,city,regioin,country,location,latitute,longitude,ISP_provider,pincode,browser_name,platform

                    JSONObject online = new JSONObject();

                    try {
                        online.put("IP", ipaddress);
                        online.put("city", city);
                        online.put("region", state);
                        online.put("country", country);
                        online.put("location", "location");
                        online.put("latitute", latitude);
                        online.put("longitute", longitde);
                        online.put("ISP_provider", "ISP_provider");
                        online.put("pincode", postalCode);
                        online.put("browser_name", "Android");
                        online.put("platform", "Android");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = MainActivity.mainUrl + "laf/signedByOtpAgg";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("otp", edtOtpBo.getText().toString());
                    params.put("mobile", DashboardFragmentNew.mobile_no);
                    params.put("lead_id", MainActivity.lead_id);
                    params.put("online", String.valueOf(online));
                    params.put("signed_by_id", LoanTabActivity.applicant_id);
                    if (!Globle.isNetworkAvailable(context)) {
                        Toast.makeText(context, R.string.please_check_your_network_connection, Toast.LENGTH_SHORT).show();
                    } else {
                        progressBarDiag.setVisibility(VISIBLE);
                        VolleyCall volleyCall = new VolleyCall();//http://192.168.0.110/eduvanzapi/dashboard/getStudentDashbBoardStatus
                        volleyCall.sendRequest(context, url, null, mFragment, "signedByOtpAgg", params, MainActivity.auth_token);
                    }
                } catch (Exception e) {
                    String className = this.getClass().getSimpleName();
                    String name = new Object() {
                    }.getClass().getEnclosingMethod().getName();
                    String errorMsg = e.getMessage();
                    String errorMsgDetails = e.getStackTrace().toString();
                    String errorLine = String.valueOf(e.getStackTrace()[0]);
                    Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
                }
            }
        });

        linSubmitOtpCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //otp,mobile,lead_id,online,signed_by_id
                    //in online key i want array which stores as following:
                    //IP,city,regioin,country,location,latitute,longitude,ISP_provider,pincode,browser_name,platform

                    JSONObject online = new JSONObject();

                    try {
                        online.put("IP", ipaddress);
                        online.put("city", city);
                        online.put("region", state);
                        online.put("country", country);
                        online.put("location", "location");
                        online.put("latitute", latitude);
                        online.put("longitute", longitde);
                        online.put("ISP_provider", "ISP_provider");
                        online.put("pincode", postalCode);
                        online.put("browser_name", "Android");
                        online.put("platform", "Android");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = MainActivity.mainUrl + "laf/signedByOtpAgg";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("otp", edtOtpCo.getText().toString());
                    params.put("mobile", mobile_numberCoBo);
                    params.put("lead_id", MainActivity.lead_id);
                    params.put("online", String.valueOf(online));
                    params.put("signed_by_id", applicant_idCoBo);
                    if (!Globle.isNetworkAvailable(context)) {
                        Toast.makeText(context, R.string.please_check_your_network_connection, Toast.LENGTH_SHORT).show();
                    } else {
                        progressBarDiag.setVisibility(VISIBLE);
                        VolleyCall volleyCall = new VolleyCall();//http://192.168.0.110/eduvanzapi/dashboard/getStudentDashbBoardStatus
                        volleyCall.sendRequest(context, url, null, mFragment, "signedByOtpAggCo", params, MainActivity.auth_token);
                    }
                } catch (Exception e) {
                    String className = this.getClass().getSimpleName();
                    String name = new Object() {
                    }.getClass().getEnclosingMethod().getName();
                    String errorMsg = e.getMessage();
                    String errorMsgDetails = e.getStackTrace().toString();
                    String errorLine = String.valueOf(e.getStackTrace()[0]);
                    Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
                }
            }
        });

    }

    public static void showDiaThanksDiag() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(((LoanTabActivity) context));
        LayoutInflater inflater = ((LoanTabActivity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.thanksdialog, null);
        dialogBuilder.setView(dialogView);
        LinearLayout buttonSave = dialogView.findViewById(R.id.linDashBoard);
        final AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoanTabActivity) context).finish();
                alertDialog.dismiss();
            }
        });

    }

    private void galleryDocIntent() {
        Intent intent = new Intent();
        intent.setType("application/pdf");  // for all types of file
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_DOC);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_DOC) {

                    Uri selectedImage = data.getData();
                    uploadFilePath = PathFile.getPath(context, selectedImage);
                    Log.e("TAG", "onActivityResult: DOC PATH " + uploadFilePath);
                    if (!uploadFilePath.equalsIgnoreCase("")) {
//                        progressBar.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //creating new thread to handle Http Operations
                                uploadFile(uploadFilePath);
                            }
                        }).start();
                    }
                }
            }
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String name = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String errorMsg = e.getMessage();
            String errorMsgDetails = e.getStackTrace().toString();
            String errorLine = String.valueOf(e.getStackTrace()[0]);
            Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
        }

    }

    public int uploadFile(final String selectedFilePath) {
//        String urlup = MainActivity.mainUrl + "laf/lafDocumentUpload";
        String urlup = MainActivity.mainUrl + "laf/uploadAgreement";
        Log.e(MainActivity.TAG, "urlup++++++: " + selectedFilePath + ", ipaddress : " + "dds");

        int serverResponseCode = 0;
        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);

        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            //dialog.dismiss();
            progressBarDiag.setVisibility(GONE);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "run: " + "Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(urlup);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("Authorization", "Bearer " + MainActivity.auth_token);
                connection.setRequestProperty("document", selectedFilePath);
//              connection.setRequestProperty("user_id", user_id);
                Log.e("TAG", "Server property" + connection.getRequestMethod() + ":property " + connection.getRequestProperties());

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"myfile\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    Log.e("TAG", " here: \n\n" + buffer + "\n" + bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                dataOutputStream.writeBytes(lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//                taOutputStream.writeBytes("Content-Disposition: form-data; name=\"document\";filename=\""
//                        + selectedFilePath + "\"" + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"lead_id\";lead_id=" + LoanTabActivity.lead_id + "" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(LoanTabActivity.lead_id);
                dataOutputStream.writeBytes(lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\";student_id=" + LoanTabActivity.student_id + "" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(LoanTabActivity.student_id);
                dataOutputStream.writeBytes(lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                Log.e("TAG", " here:server response serverResponseCode\n\n" + serverResponseCode);
                String serverResponseMessage = connection.getResponseMessage();
                Log.e("TAG", " here: server message serverResponseMessage \n\n" + serverResponseMessage.toString() + "\n" + bufferSize);
                BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                String output = "";
                sb = new StringBuffer();

                while ((output = br.readLine()) != null) {
                    sb.append(output);
                    Log.e("TAG", "uploadFile: " + br);
                    Log.e("TAG", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
                }
                Log.e("TAG", "uploadFile: " + sb.toString());

                try {
                    JSONObject mJson = new JSONObject(sb.toString());
                    final String mData = mJson.getString("status");
                    final String mData1 = mJson.getString("message");
                    final JSONObject mData2 = mJson.getJSONObject("result");
                    linDownloadSignedAgreement.setTag(mData2.getString("baseUrl").concat(mJson.getString("document_path")));
                    downloadUrl = mData2.getString("baseUrl").concat(mJson.getString("document_path"));

                    Log.e("TAG", "uploadFile: code " + mData);
                    if (mData.equalsIgnoreCase("1")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, mData1, Toast.LENGTH_SHORT).show();

//                                linManual.setVisibility(View.GONE);
//                                btnDownloadSignedApplication.setVisibility(View.VISIBLE);
//                                txtChooseSignType.setVisibility(View.GONE);
//                                rgSignType.setVisibility(View.GONE);
//                                txtSubmitstatus2.setText("Signed");
//                                txtSubmitstatus2.setTextColor(((Activity) context).getResources().getColor(R.color.colorPrimary));
                                cfAlertDialog.dismiss();
                                progressBar.setVisibility(VISIBLE);
                                getLoanDetails();
//                                SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                try {
//                                    editor.putString("signed_application_url",downloadUrl);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                editor.apply();
//                                editor.commit();
//                                try {
//                                    downloadSignedUrl = mData2.getString("docPath");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                                progressBarDiag.setVisibility(GONE);
                            }
                        });

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, mData1, Toast.LENGTH_SHORT).show();
                                progressBarDiag.setVisibility(GONE);
                            }
                        });
//                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("TAG", " here: \n\n" + fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.file_not_found, Toast.LENGTH_SHORT).show();
                        progressBarDiag.setVisibility(View.GONE);
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.url_error, Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.cannot_read_write_file, Toast.LENGTH_SHORT).show();
            }
//            dialog.dismiss();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBarDiag.setVisibility(View.GONE);
                }
            });
            return serverResponseCode;
        }

    }

    public void downLoad(String uri, int status, String fileName) {
        try {
            String fname = "";
            if (status == 1) {
                fname = "LAF" + userName + System.currentTimeMillis() + ".pdf";
            } else if (status == 2) {
                fname = fileName + userName + System.currentTimeMillis() + ".pdf";
            }
            downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
            Uri Download_Uri = Uri.parse(uri);
            DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

            //Restrict the types of networks over which this download may proceed.
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            //Set whether this download may proceed over a roaming connection.
            request.setAllowedOverRoaming(false);
            //Set the title of this download, to be displayed in notifications (if enabled).
            request.setTitle("Your Document is Downloading");
            //Set a description of this download, to be displayed in notifications (if enabled)
            request.setDescription("Android Data download using DownloadManager.");
            //Set the local destination for the downloaded file to a path within the application's external files directory
//        if(isImage) {
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fname);//
//        }else {
//            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, "DATA"+System.currentTimeMillis()+".pdf");
//        }
            //Enqueue a new download and same the referenceId
            downloadReference = downloadManager.enqueue(request);
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String name = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String errorMsg = e.getMessage();
            String errorMsgDetails = e.getStackTrace().toString();
            String errorLine = String.valueOf(e.getStackTrace()[0]);
            Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
        }

    }


    public interface onPostFragmentInteractionListener {
        void onPostApprovalFragment(boolean valid, int next);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PostApprovalDocFragment.onPostFragmentInteractionListener) {
            mListener = (PostApprovalDocFragment.onPostFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onUploadFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
