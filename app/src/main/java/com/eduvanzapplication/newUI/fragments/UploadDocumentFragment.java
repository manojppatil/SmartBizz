package com.eduvanzapplication.newUI.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eduvanzapplication.BuildConfig;
import com.eduvanzapplication.MainActivity;
import com.eduvanzapplication.R;
import com.eduvanzapplication.Util.Globle;
import com.eduvanzapplication.Util.JavaGetFileSize;
import com.eduvanzapplication.fqform.borrowerdetail.pojo.BorrowerCurrentCountryPersonalPOJO;
import com.eduvanzapplication.newUI.VolleyCall;
import com.eduvanzapplication.newUI.newViews.DashboardActivity;
import com.eduvanzapplication.newUI.newViews.LoanTabActivity;
import com.eduvanzapplication.newUI.pojo.DocumenPOJO;
import com.eduvanzapplication.uploaddocs.PathFile;
import com.eduvanzapplication.uploaddocs.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vijay.createpdf.activity.ImgToPdfActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.eduvanzapplication.MainActivity.TAG;


public class UploadDocumentFragment extends Fragment implements View.OnClickListener {

    public static LinearLayout linKYCblock, linKYCblockBottom, linFinancBlockBottom, linEducationBlockBottom,
            linFinancBlock, linEducationBlock, linKYCDocuments, linFinanceDocuments, linEducationDocuments,
            linDelAadhaarBtn, linDelPanBtn, linDelPhotoBtn, linDelPassportBtn, linDelVoterIdBtn, linDelDrivingLicenseBtn,
            linDelTelephoneBillBtn, linDelElectricityBillBtn, linDelRentAgreementBtn, linDelAddressProofBtn, linDelSalarySlipSixBtn,
            linDelSalarySlipThreeBtn, linDelBankStatementThreeBtn, linDelBankStatementSixBtn, linDelKVPBtn, linDelLICPolicyBtn,
            linDelForm16Btn, linDelForm61Btn, linDelPensionLetterBtn, linDelITRBtn, linDelPNLBtn, linDeltenth_mark_sheetBtn,
            linDeltwelvethMarkSheetBtn, linDellastCompletedMarkSheetBtn, linDellastcompletedDegreeCertificateBtn, linDelothersBtn;

    public static NestedScrollView nestedScrollView;
    public static TextView txtKycDocToggle, txtPhotoToggle, txtAddtionalDocToggle;
    public static LinearLayout linKycDocToggle, linPhotoToggle, linAddtionalDocToggle;
    public static ImageView ivPersonalToggle, ivIdentityToggle, ivCourseToggle, ivcolorphotogratitle, ivkyctitlecheck;
    public static LinearLayout linKycDocBlock, relPhotoBlock, relAddtionalDocBlock;
    public static Animation expandAnimationPersonal, collapseanimationPersonal;
    public static Animation expandAnimationIdentity, collapseAnimationIdentity;
    public static Animation expanAnimationCourse, collapseAnimationCourse;
    public static ImageButton btnNextUploadDetail;

    public static onUploadFragmentInteractionListener mListener;

    /*KYC documents*/
    static LinearLayout profileImage, aadharCard, panCard, passport, voterId, drivingLicense, telephoneBill, electricityBill,
            rentAgreement, addressProof;
    static RelativeLayout profileBckgrnd, aadharBckgrnd, panBckgrnd, passportBckgrnd, voterIdBckgrnd, drivingLicenseBckgrnd,
            telephoneBillBckgrnd, electricityBillBckgrnd, rentAgreementBckgrnd, addressProofBckgrnd, othersBackground;
    /*Financial Documents*/
    static LinearLayout salSlipSix, salSlipThree, bankStmntThree, linSeemoreFinancialBtn, bankStmntSix, kvp, licPolicy,
            form16, form61, pensionLetter, itr, pnl;
    static RelativeLayout salSixBckgrnd, salThreeBckgrnd, bankThreeBckgrnd, bankSixBckgrnd, kvpBckgrnd, licPolicyBckgrnd,
            form16Bckgrnd, form61Bckgrnd, pensionBckgrnd, itrBckgrnd, pnlBckgrnd;
    /*Educational Documents*/
    static LinearLayout tenthMarksheet, twelvethMarksheet, degreeMarkSheet, degreeCertificate;
    static RelativeLayout tenthBckgrnd, twelthBckgrnd, degreeMarksheetBckgrnd, degreeCertificateBckgrnd;
    /*others documents*/
    static LinearLayout others;
    public String userChoosenTask;
    public static ImageView ivKyc, ivFinancial, ivEducational;

    int tap;

    public int REQUEST_CAMERA = 0, SELECT_FILE = 1, SELECT_DOC = 2;
    public int GET_MY_PERMISSION = 1, permission;
    public static Fragment mFragment;
    String uploadFilePath = "";
    StringBuffer sb;

    public String applicantType = "", documentTypeNo = "", userID = "";
    DownloadManager downloadManager;
    static Context context;
    public static ProgressBar progressBar;
    long downloadReference;

    //country
    public static ArrayAdapter arrayAdapter_document;
    public static ArrayList<String> document_arrayList;
    public static ArrayList<DocumenPOJO> documenPOJOArrayList;
    public static Spinner spDocument;
    public static String selecteddocID = "";

    public BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadReference == referenceId) {

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
                        progressBar.setVisibility(View.GONE);
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

    public UploadDocumentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        mFragment = new UploadDocumentFragment();
        MainActivity.currrentFrag = 3;
        View view = inflater.inflate(R.layout.fragment_uploaddocument, container, false);

        expandAnimationPersonal = AnimationUtils.loadAnimation(context, R.anim.scale_expand);
        expandAnimationIdentity = AnimationUtils.loadAnimation(context, R.anim.scale_expand);
        expanAnimationCourse = AnimationUtils.loadAnimation(context, R.anim.scale_expand);
        collapseanimationPersonal = AnimationUtils.loadAnimation(context, R.anim.scale_collapse);
        collapseAnimationIdentity = AnimationUtils.loadAnimation(context, R.anim.scale_collapse);
        collapseAnimationCourse = AnimationUtils.loadAnimation(context, R.anim.scale_collapse);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        ivPersonalToggle = view.findViewById(R.id.ivPersonalToggle);
        ivIdentityToggle = view.findViewById(R.id.ivIdentityToggle);
        ivCourseToggle = view.findViewById(R.id.ivCourseToggle);

        txtKycDocToggle = view.findViewById(R.id.txtKycDocToggle);
        linKycDocBlock = view.findViewById(R.id.linKycDocBlock);
        txtPhotoToggle = view.findViewById(R.id.txtPhotoToggle);
        linKycDocToggle = view.findViewById(R.id.linKycDocToggle);
        linPhotoToggle = view.findViewById(R.id.linPhotoToggle);

        btnNextUploadDetail = view.findViewById(R.id.btnNextUploadDetail);

        linDelAadhaarBtn = view.findViewById(R.id.linDelAadhaarBtn);
        linDelPanBtn = view.findViewById(R.id.linDelPanBtn);
        linDelPhotoBtn = view.findViewById(R.id.linDelPhotoBtn);
        linDelPassportBtn = view.findViewById(R.id.linDelPassportBtn);
        linDelVoterIdBtn = view.findViewById(R.id.linDelVoterIdBtn);
        linDelDrivingLicenseBtn = view.findViewById(R.id.linDelDrivingLicenseBtn);
        linDelTelephoneBillBtn = view.findViewById(R.id.linDelTelephoneBillBtn);
        linDelElectricityBillBtn = view.findViewById(R.id.linDelElectricityBillBtn);
        linDelRentAgreementBtn = view.findViewById(R.id.linDelRentAgreementBtn);
        linDelAddressProofBtn = view.findViewById(R.id.linDelAddressProofBtn);
        linDelSalarySlipSixBtn = view.findViewById(R.id.linDelSalarySlipSixBtn);
        linDelSalarySlipThreeBtn = view.findViewById(R.id.linDelSalarySlipThreeBtn);
        linDelBankStatementThreeBtn = view.findViewById(R.id.linDelBankStatementThreeBtn);
        linDelBankStatementSixBtn = view.findViewById(R.id.linDelBankStatementSixBtn);
        linDelKVPBtn = view.findViewById(R.id.linDelKVPBtn);
        linDelLICPolicyBtn = view.findViewById(R.id.linDelLICPolicyBtn);
        linDelForm16Btn = view.findViewById(R.id.linDelForm16Btn);
        linDelForm61Btn = view.findViewById(R.id.linDelForm61Btn);
        linDelPensionLetterBtn = view.findViewById(R.id.linDelPensionLetterBtn);
        linDelITRBtn = view.findViewById(R.id.linDelITRBtn);
        linDelPNLBtn = view.findViewById(R.id.linDelPNLBtn);
        linDeltenth_mark_sheetBtn = view.findViewById(R.id.linDeltenth_mark_sheetBtn);
        linDeltwelvethMarkSheetBtn = view.findViewById(R.id.linDeltwelvethMarkSheetBtn);
        linDellastCompletedMarkSheetBtn = view.findViewById(R.id.linDellastCompletedMarkSheetBtn);
        linDellastcompletedDegreeCertificateBtn = view.findViewById(R.id.linDellastcompletedDegreeCertificateBtn);
        linDelothersBtn = view.findViewById(R.id.linDelothersBtn);

        ivcolorphotogratitle = view.findViewById(R.id.ivcolorphotogratitle);
        ivkyctitlecheck = view.findViewById(R.id.ivkyctitlecheck);
        linAddtionalDocToggle = view.findViewById(R.id.linAddtionalDocToggle);
        relPhotoBlock = view.findViewById(R.id.relPhotoBlock);
        txtAddtionalDocToggle = view.findViewById(R.id.txtAddtionalDocToggle);
        relAddtionalDocBlock = view.findViewById(R.id.relAddtionalDocBlock);

        //personal details
        profileBckgrnd = view.findViewById(R.id.profileBack);
        panBckgrnd = view.findViewById(R.id.panBackground);
        aadharBckgrnd = view.findViewById(R.id.aadharBackground);
        passportBckgrnd = view.findViewById(R.id.passportBackground);
        voterIdBckgrnd = view.findViewById(R.id.voterIdBackground);
        drivingLicenseBckgrnd = view.findViewById(R.id.drivingLicenseBackground);
        telephoneBillBckgrnd = view.findViewById(R.id.telephoneBillBackground);
        electricityBillBckgrnd = view.findViewById(R.id.electricityBillBackground);
        rentAgreementBckgrnd = view.findViewById(R.id.rentAgreementBackground);
        addressProofBckgrnd = view.findViewById(R.id.addressProofBackground);
        othersBackground = view.findViewById(R.id.othersBackground);
        //financial details
        salSixBckgrnd = view.findViewById(R.id.salSixBackground);
        salThreeBckgrnd = view.findViewById(R.id.salThreeBackground);
        bankSixBckgrnd = view.findViewById(R.id.bankSixBackground);
        bankThreeBckgrnd = view.findViewById(R.id.bankThreeBackground);
        kvpBckgrnd = view.findViewById(R.id.kvpBackground);
        licPolicyBckgrnd = view.findViewById(R.id.licPolicyBackground);
        form16Bckgrnd = view.findViewById(R.id.form16Background);
        form61Bckgrnd = view.findViewById(R.id.from61Background);
        pensionBckgrnd = view.findViewById(R.id.pensionBackground);
        itrBckgrnd = view.findViewById(R.id.itrBackground);
        pnlBckgrnd = view.findViewById(R.id.pnlBackground);
        //educational
        tenthBckgrnd = view.findViewById(R.id.tenthMarksheetBackground);
        twelthBckgrnd = view.findViewById(R.id.twelvethMarksheetBackground);
        degreeMarksheetBckgrnd = view.findViewById(R.id.lastMarksheetBackground);
        degreeCertificateBckgrnd = view.findViewById(R.id.lastCertificateBackground);

        spDocument = view.findViewById(R.id.spDocument);
        progressBar = view.findViewById(R.id.progressBar_docupload);
        selecteddocID = "";

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadReceiver, filter);

        btnNextUploadDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUploadInfoFragment(true, 3);
            }
        });

        linDelAadhaarBtn.setOnClickListener(this);
        linDelPanBtn.setOnClickListener(this);
        linDelPhotoBtn.setOnClickListener(this);
        linDelPassportBtn.setOnClickListener(this);
        linDelVoterIdBtn.setOnClickListener(this);
        linDelDrivingLicenseBtn.setOnClickListener(this);
        linDelTelephoneBillBtn.setOnClickListener(this);
        linDelElectricityBillBtn.setOnClickListener(this);
        linDelRentAgreementBtn.setOnClickListener(this);
        linDelAddressProofBtn.setOnClickListener(this);
        linDelSalarySlipSixBtn.setOnClickListener(this);
        linDelSalarySlipThreeBtn.setOnClickListener(this);
        linDelBankStatementThreeBtn.setOnClickListener(this);
        linDelBankStatementSixBtn.setOnClickListener(this);
        linDelKVPBtn.setOnClickListener(this);
        linDelLICPolicyBtn.setOnClickListener(this);
        linDelForm16Btn.setOnClickListener(this);
        linDelForm61Btn.setOnClickListener(this);
        linDelPensionLetterBtn.setOnClickListener(this);
        linDelITRBtn.setOnClickListener(this);
        linDelPNLBtn.setOnClickListener(this);
        linDeltenth_mark_sheetBtn.setOnClickListener(this);
        linDeltwelvethMarkSheetBtn.setOnClickListener(this);
        linDellastCompletedMarkSheetBtn.setOnClickListener(this);
        linDellastcompletedDegreeCertificateBtn.setOnClickListener(this);
        linDelothersBtn.setOnClickListener(this);

        try {
            //============================KYC profile image========================
            profileImage = view.findViewById(R.id.linPhoto);
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tap++;

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    profileBckgrnd.setVisibility(GONE);
                                    applicantType = "1";
                                    documentTypeNo = "1";

                                    if (Build.VERSION.SDK_INT >= 23) {
                                        permission = ContextCompat.checkSelfPermission(context,
                                                Manifest.permission.CAMERA);
                                        if (permission != PackageManager.PERMISSION_GRANTED) {//Direct Permission without disclaimer dialog
                                            ActivityCompat.requestPermissions((Activity) context,
                                                    new String[]{
                                                            Manifest.permission.CAMERA},
                                                    GET_MY_PERMISSION);
                                        } else {
                                            selectImage();
                                        }
                                    }
//                                imageToPdf(documentTypeNo, getString(R.string.upload_profile_picture), getString(R.string.applicant_single_picture_required_to_be_uploaded), LoanTabActivity.applicant_id, "1");
                                    tap = 0;

                                }
                            } else if (tap == 2) {
                                if (profileImage.getTag() != null) {
                                    String strFileName = profileImage.getTag().toString().substring(profileImage.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(profileImage.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(profileImage.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(profileImage.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }

                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);


                }
            });

            /*=================================profile photo finished=============================*/
            /*===================================Pan card=========================================*/
            panCard = view.findViewById(R.id.linPan);
            panCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "2";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_pan_card), getString(R.string.applicant_pan_card), LoanTabActivity.applicant_id, "1");

                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (panCard.getTag() != null) {
                                    String strFileName = panCard.getTag().toString().substring(panCard.getTag().toString().lastIndexOf("/") + 1);
                                    File filePath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);
                                    String fileExtension = strFileName.substring(strFileName.lastIndexOf('.') + 1);
                                    if (fileExtension.equals("pdf")) {
                                        openPdf(String.valueOf(panCard.getTag()));
                                    } else if (fileExtension.equals("zip") || fileExtension.equals("rar")) {
                                        if (filePath.exists()) {
                                            Toast.makeText(getActivity(), "File has already downloaded", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(panCard.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(panCard.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }


                                tap = 0;
                            }
                        }
                    }, 700);

                }
            });


            /*======================pan card done===============*/

            /*==============================================adhar card=====================================*/
            aadharCard = view.findViewById(R.id.linAadhaar);
            aadharCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "3";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_adhaar_card), getString(R.string.applicant_adhaar_card_front_and_backside), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (aadharCard.getTag() != null) {
                                    String strFileName = aadharCard.getTag().toString().substring(aadharCard.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(aadharCard.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(aadharCard.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(aadharCard.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }


                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });
            /*========================end of aadha card==========================*/

            /*================================passport===============================*/
            passport = view.findViewById(R.id.linPassport);
            passport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "4";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_passport), getString(R.string.applicant_passport), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (passport.getTag() != null) {
                                    String strFileName = passport.getTag().toString().substring(passport.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(passport.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(passport.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(passport.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }


                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);


                }
            });

            /* ====================================end of passport=========================*/

            /*==============================voter id===================================*/
            voterId = view.findViewById(R.id.linVoterId);
            voterId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "5";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_voterID), getString(R.string.applicant_voterID), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (voterId.getTag() != null) {
                                    String strFileName = voterId.getTag().toString().substring(voterId.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(voterId.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(voterId.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(voterId.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }


                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });
            /*=================================end of voter id==========================*/
            /*==========================================driving License========================*/

            drivingLicense = view.findViewById(R.id.linDrivingLicense);
            drivingLicense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "6";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_driving_license), getString(R.string.applicant_driving_license), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (drivingLicense.getTag() != null) {
                                    String strFileName = drivingLicense.getTag().toString().substring(drivingLicense.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(drivingLicense.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(drivingLicense.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(drivingLicense.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }


                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);
                }
            });

            /*============================end of driving license===============================*/
            /*===========================================Telephone Bill==============================*/

            telephoneBill = view.findViewById(R.id.linTelephoneBill);
            telephoneBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "7";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_telephone_bill), getString(R.string.applicant_telephone_bill), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (telephoneBill.getTag() != null) {
                                    String strFileName = telephoneBill.getTag().toString().substring(telephoneBill.getTag().toString().lastIndexOf("/") + 1);
                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);
                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(telephoneBill.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(telephoneBill.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(telephoneBill.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);


                }
            });


            /*========================end of telephone Bill================================*/
            /*==========================================Electricity Bill============================*/
            electricityBill = view.findViewById(R.id.linElectricityBill);
            electricityBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "8";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_electricity_bill), getString(R.string.applicant_electricity_bill), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (electricityBill.getTag() != null) {
                                    String strFileName = electricityBill.getTag().toString().substring(electricityBill.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(electricityBill.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(electricityBill.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(electricityBill.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }


                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);


                }
            });

            /*====================end of electricity Bill===========================*/
            /*=================================Rent Agreement=================================*/
            rentAgreement = view.findViewById(R.id.linRentAgreement);
            rentAgreement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "9";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_rent_agreement), getString(R.string.applicant_rent_agreement), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (rentAgreement.getTag() != null) {
                                    String strFileName = rentAgreement.getTag().toString().substring(rentAgreement.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(rentAgreement.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(rentAgreement.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(rentAgreement.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }


                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);


                }
            });


            /*==================================end of rent agreement===========================*/
            /*=====================================Address Proof=======================================*/
            addressProof = view.findViewById(R.id.linAddressProof);
            addressProof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "30";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_address_proof), getString(R.string.applicant_address_proof), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (addressProof.getTag() != null) {
                                    String strFileName = addressProof.getTag().toString().substring(addressProof.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(addressProof.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(addressProof.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(addressProof.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }


                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });

            /*=======================End Address Proof & End Of KYC================================*/
            /*=====================================Financial Documents & salary slip six months===============================*/
            salSlipSix = view.findViewById(R.id.linSalarySlipSix);
            salSlipSix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "17";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_income_proof_6), getString(R.string.salary_slip_of_applicant_latest_6_months_if_not_available_salary_certificate_stating_the_same_details_would_be_accepted), LoanTabActivity.applicant_id, "1");

                                    tap = 0;
                                }
                            } else if (tap == 2) {


                                if (salSlipSix.getTag() != null) {
                                    String strFileName = salSlipSix.getTag().toString().substring(salSlipSix.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(salSlipSix.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(salSlipSix.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(salSlipSix.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);
                }
            });

            /*end sal slip six months*/

            /*==========================================sal slip three months=============================*/
            salSlipThree = view.findViewById(R.id.linSalarySlipThree);
            salSlipThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "18";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_income_proof), getString(R.string.salary_slip_of_applicant_latest_3_months_if_not_available_salary_certificate_stating_the_same_details_would_be_accepted), LoanTabActivity.applicant_id, "1");

                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (salSlipThree.getTag() != null) {
                                    String strFileName = salSlipThree.getTag().toString().substring(salSlipThree.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(salSlipThree.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(salSlipThree.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(salSlipThree.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }


                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });

            /*================================end of sal slip three=============================*/
            /*===========================================bank statement three months=========================*/

            bankStmntThree = view.findViewById(R.id.linBankStatementThree);
            bankStmntThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "19";
                                    imageToPdfBankStmt(documentTypeNo, getString(R.string.upload_bank_statement), getString(R.string.current_3_months_bank_statement_of_applicant_reflecting_salary_along_with_the_front_page), LoanTabActivity.applicant_id, "1");

                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (bankStmntThree.getTag() != null) {
                                    String strFileName = bankStmntThree.getTag().toString().substring(bankStmntThree.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(bankStmntThree.getTag()));

                                    }
//                                    else if (FileExtn.equalsIgnoreCase("xls") || FileExtn.equalsIgnoreCase("xlsx")) {
//                                        openAnyFile(String.valueOf(bankStmntThree.getTag()));
//                                    }
                                    else if (FileExtn.equals("zip") || FileExtn.equals("rar") || FileExtn.equalsIgnoreCase("xls") || FileExtn.equalsIgnoreCase("xlsx")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(bankStmntThree.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(bankStmntThree.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });

            /*end if bank statement three*/

            /*===================================bank statement six=================================*/

            bankStmntSix = view.findViewById(R.id.linBankStatementSix);
            bankStmntSix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "20";
                                    imageToPdfBankStmt(documentTypeNo, getString(R.string.upload_bank_statement_6), getString(R.string.current_6_months_bank_statement_of_applicant_reflecting_salary_along_with_the_front_page), LoanTabActivity.applicant_id, "1");

                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (bankStmntSix.getTag() != null) {
                                    String strFileName = bankStmntSix.getTag().toString().substring(bankStmntSix.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(bankStmntSix.getTag()));

                                    }
//                                    else if (FileExtn.equalsIgnoreCase("xls") || FileExtn.equalsIgnoreCase("xlsx")) {
//                                        openAnyFile(String.valueOf(bankStmntSix.getTag()));
//                                    }
                                    else if (FileExtn.equalsIgnoreCase("zip") || FileExtn.equalsIgnoreCase("rar") || FileExtn.equalsIgnoreCase("xls") || FileExtn.equalsIgnoreCase("xlsx")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(bankStmntSix.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(bankStmntSix.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }

                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });

            /*end of bank statement 6*/

            /*=======================================KVP============================*/

            kvp = view.findViewById(R.id.linKVP);
            kvp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "10";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_KVP), getString(R.string.current_KVP), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (kvp.getTag() != null) {
                                    String strFileName = kvp.getTag().toString().substring(kvp.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(kvp.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(kvp.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(kvp.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });

            /*=================end of kvp==================*/
            /*========================================lic policy================================================*/

            licPolicy = view.findViewById(R.id.linLICPolicy);
            licPolicy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "11";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_lic_policy), getString(R.string.current_applicant_lic_policy), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (licPolicy.getTag() != null) {
                                    String strFileName = licPolicy.getTag().toString().substring(licPolicy.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(licPolicy.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(licPolicy.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(licPolicy.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);


                }
            });

            /*=================================end of lic policy==============================*/
            /*===========================================form 16=========================================*/

            form16 = view.findViewById(R.id.linForm16);
            form16.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "12";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_form_16), getString(R.string.current_applicant_form_16), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (form16.getTag() != null) {
                                    String strFileName = form16.getTag().toString().substring(form16.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(form16.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(form16.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(form16.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }


                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });


            /*============================================end of form 16=====================================*/
            /*===================================================form 61=================================================*/

            form61 = view.findViewById(R.id.linForm61);
            form61.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "13";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_form_61), getString(R.string.current_applicant_form_61), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (form61.getTag() != null) {
                                    String strFileName = form61.getTag().toString().substring(form61.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(form61.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(form61.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(form61.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });

            /*end of form 61*/
            /*=======================================Pension Letter================================*/
            pensionLetter = view.findViewById(R.id.linPensionLetter);
            pensionLetter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "14";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_pension_letter), getString(R.string.current_applicant_pension_letter), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (pensionLetter.getTag() != null) {
                                    String strFileName = pensionLetter.getTag().toString().substring(pensionLetter.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(pensionLetter.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(pensionLetter.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(pensionLetter.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);


                }
            });


            /*================================end of pension letter===========================*/
            /*=========================================ITR=============================================*/

            itr = view.findViewById(R.id.linITR);
            itr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "15";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_itr), getString(R.string.current_itr), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (itr.getTag() != null) {
                                    String strFileName = itr.getTag().toString().substring(itr.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(itr.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(itr.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(itr.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });

            /*end of itr*/

            /*====================================================PNL===================================*/
            pnl = view.findViewById(R.id.linPNL);
            pnl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "16";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_pnl), getString(R.string.current_applicant_pnl), LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (pnl.getTag() != null) {
                                    String strFileName = pnl.getTag().toString().substring(pnl.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(pnl.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(pnl.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(pnl.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);


                }
            });

            /* end of pnl & financial documents*/
            /*==========================================Educational Documents================================*/

            tenthMarksheet = view.findViewById(R.id.lintenth_mark_sheet);
            tenthMarksheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "21";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_latest_marksheet), getString(R.string.latest_marksheet_of_the_applicant), LoanTabActivity.applicant_id, "1");

                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (tenthMarksheet.getTag() != null) {
                                    String strFileName = tenthMarksheet.getTag().toString().substring(tenthMarksheet.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(tenthMarksheet.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(tenthMarksheet.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(tenthMarksheet.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });

            /*========================end of tenth marksheet==========================*/
            /*========================================twelveth marksheet======================================*/

            twelvethMarksheet = view.findViewById(R.id.lintwelvethMarkSheet);
            twelvethMarksheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {
                                    applicantType = "1";
                                    documentTypeNo = "22";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_latest_marksheet), getString(R.string.latest_marksheet_of_the_applicant), LoanTabActivity.applicant_id, "1");

                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (twelvethMarksheet.getTag() != null) {
                                    String strFileName = twelvethMarksheet.getTag().toString().substring(twelvethMarksheet.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(twelvethMarksheet.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(twelvethMarksheet.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(twelvethMarksheet.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });

            /*end of twelve marksheet*/
            /*========================================degree marksheet============================================*/
            degreeMarkSheet = view.findViewById(R.id.linlastCompletedMarkSheet);
            degreeMarkSheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {

                                    applicantType = "1";
                                    documentTypeNo = "23";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_latest_marksheet), getString(R.string.latest_marksheet_of_the_applicant), LoanTabActivity.applicant_id, "1");

                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (degreeMarkSheet.getTag() != null) {
                                    String strFileName = degreeMarkSheet.getTag().toString().substring(degreeMarkSheet.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(degreeMarkSheet.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(degreeMarkSheet.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(degreeMarkSheet.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }


                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });

            /*end of degree marksheet*/
            /*==================================degree certificate==========================*/

            degreeCertificate = view.findViewById(R.id.linlastcompletedDegreeCertificate);
            degreeCertificate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {

                                    applicantType = "1";
                                    documentTypeNo = "24";
                                    imageToPdf(documentTypeNo, getString(R.string.upload_latest_certificate), getString(R.string.latest_certificate_of_the_applicant), LoanTabActivity.applicant_id, "1");

                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (degreeCertificate.getTag() != null) {
                                    String strFileName = degreeCertificate.getTag().toString().substring(degreeCertificate.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(degreeCertificate.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(degreeCertificate.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(degreeCertificate.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });
            /*============================================end of educational documents=================================================================*/
            /*===============================================Others====================================================*/

            others = view.findViewById(R.id.others);
            others.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tap++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tap == 1) {
                                if (LoanTabActivity.isEditaDocble) {

                                    applicantType = "1";
                                    documentTypeNo = "31";
                                    imageToPdf(documentTypeNo, "others", "others", LoanTabActivity.applicant_id, "1");
                                    tap = 0;
                                }
                            } else if (tap == 2) {

                                if (others.getTag() != null) {
                                    String strFileName = others.getTag().toString().substring(others.getTag().toString().lastIndexOf("/") + 1);

                                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.eduvanzapplication/files/Download/Eduvanz/UploadedDoc/" + strFileName);

                                    String FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        openPdf(String.valueOf(others.getTag()));

                                    } else if (FileExtn.equals("zip") || FileExtn.equals("rar")) {
                                        if (filepath.exists()) {
                                            Toast.makeText(getActivity(), "File is already downloaded: ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                downLoadClick(String.valueOf(others.getTag()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            openImage(String.valueOf(others.getTag()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload document first to preview", Toast.LENGTH_SHORT).show();
                                }
                                tap = 0;
                            }
                            tap = 0;
                        }
                    }, 700);

                }
            });
            /*===========================================end of others document==========================================*/

            spDocument.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        String text = spDocument.getSelectedItem().toString();
                        int count = documenPOJOArrayList.size();
                        for (int i = 0; i < count; i++) {
                            if (documenPOJOArrayList.get(i).document_name.equalsIgnoreCase(text)) {
                                selecteddocID = documenPOJOArrayList.get(i).document_type_id;
                                break;
                            }
                        }
                        ShowDocument();
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            getUploadDocumentsApiCall();
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String name = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String errorMsg = e.getMessage();
            String errorMsgDetails = e.getStackTrace().toString();
            String errorLine = String.valueOf(e.getStackTrace()[0]);
            Globle.ErrorLog(getActivity(), className, name, errorMsg, errorMsgDetails, errorLine);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.linDelAadhaarBtn:
                deleteFileAPI("3", linDelAadhaarBtn.getTag().toString());
                break;
            case R.id.linDelPanBtn:
                deleteFileAPI("2", linDelPanBtn.getTag().toString());
                break;
            case R.id.linDelPhotoBtn:
                deleteFileAPI("1", linDelPhotoBtn.getTag().toString());
                break;
            case R.id.linDelPassportBtn:
                deleteFileAPI("4", linDelPassportBtn.getTag().toString());
                break;
            case R.id.linDelVoterIdBtn:
                deleteFileAPI("5", linDelVoterIdBtn.getTag().toString());
                break;
            case R.id.linDelDrivingLicenseBtn:
                deleteFileAPI("6", linDelDrivingLicenseBtn.getTag().toString());
                break;
            case R.id.linDelTelephoneBillBtn:
                deleteFileAPI("7", linDelTelephoneBillBtn.getTag().toString());
                break;
            case R.id.linDelElectricityBillBtn:
                deleteFileAPI("8", linDelElectricityBillBtn.getTag().toString());
                break;
            case R.id.linDelRentAgreementBtn:
                deleteFileAPI("9", linDelRentAgreementBtn.getTag().toString());
                break;
            case R.id.linDelAddressProofBtn:
                deleteFileAPI("30", linDelAddressProofBtn.getTag().toString());
                break;
            case R.id.linDelSalarySlipSixBtn:
                deleteFileAPI("17", linDelSalarySlipSixBtn.getTag().toString());
                break;
            case R.id.linDelSalarySlipThreeBtn:
                deleteFileAPI("18", linDelSalarySlipThreeBtn.getTag().toString());
                break;
            case R.id.linDelBankStatementThreeBtn:
                deleteFileAPI("19", linDelBankStatementThreeBtn.getTag().toString());
                break;
            case R.id.linDelBankStatementSixBtn:
                deleteFileAPI("20", linDelBankStatementSixBtn.getTag().toString());
                break;
            case R.id.linDelKVPBtn:
                deleteFileAPI("10", linDelKVPBtn.getTag().toString());
                break;
            case R.id.linDelLICPolicyBtn:
                deleteFileAPI("11", linDelLICPolicyBtn.getTag().toString());
                break;
            case R.id.linDelForm16Btn:
                deleteFileAPI("12", linDelForm16Btn.getTag().toString());
                break;
            case R.id.linDelForm61Btn:
                deleteFileAPI("13", linDelForm61Btn.getTag().toString());
                break;
            case R.id.linDelPensionLetterBtn:
                deleteFileAPI("14", linDelPensionLetterBtn.getTag().toString());
                break;
            case R.id.linDelITRBtn:
                deleteFileAPI("15", linDelITRBtn.getTag().toString());
                break;
            case R.id.linDelPNLBtn:
                deleteFileAPI("16", linDelPNLBtn.getTag().toString());
                break;
            case R.id.linDeltenth_mark_sheetBtn:
                deleteFileAPI("21", linDeltenth_mark_sheetBtn.getTag().toString());
                break;
            case R.id.linDeltwelvethMarkSheetBtn:
                deleteFileAPI("22", linDeltwelvethMarkSheetBtn.getTag().toString());
                break;
            case R.id.linDellastCompletedMarkSheetBtn:
                deleteFileAPI("23", linDellastCompletedMarkSheetBtn.getTag().toString());
                break;
            case R.id.linDellastcompletedDegreeCertificateBtn:
                deleteFileAPI("24", linDellastcompletedDegreeCertificateBtn.getTag().toString());
                break;
            case R.id.linDelothersBtn:
                deleteFileAPI("31", linDelothersBtn.getTag().toString());
                break;
        }
    }

    public void deleteFileAPI(String docTypeId, String docId) {

        try {
            String url = MainActivity.mainUrl + "document/deleteDocument";
            Map<String, String> params = new HashMap<String, String>();
            params.put("lead_id", MainActivity.lead_id);
            params.put("applicant_id", LoanTabActivity.applicant_id);
            params.put("document_type_id", docTypeId);//19
            params.put("docid", docId);//52692
            if (!Globle.isNetworkAvailable(context)) {
                Toast.makeText(context, R.string.please_check_your_network_connection, Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                VolleyCall volleyCall = new VolleyCall();
                volleyCall.sendRequest(context, url, null, mFragment, "deletedocument", params, MainActivity.auth_token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setdeleteFileStatus(JSONObject jsonDataO) {
        try {
            String message = jsonDataO.getString("message");
            progressBar.setVisibility(View.GONE);
            if (jsonDataO.getInt("status") == 1) {
                getUploadDocumentsApiCall();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
        }

    }

    public static void ShowDocument() {

        switch (selecteddocID) {

            case "":
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }

                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }

                break;

            case "4":
                //passport
                if (passport.getVisibility() == VISIBLE) {
                } else {
                    passport.setVisibility(VISIBLE);
                    passport.requestFocus();
                }

                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "5":
                //voterID
                if (voterId.getVisibility() == VISIBLE) {
                } else {
                    voterId.setVisibility(VISIBLE);
                    voterId.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "6":
                //DrivingLicense
                if (drivingLicense.getVisibility() == VISIBLE) {
                } else {
                    drivingLicense.setVisibility(VISIBLE);
                    drivingLicense.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "7":
                //TelephoneBill
                if (telephoneBill.getVisibility() == VISIBLE) {
                } else {
                    telephoneBill.setVisibility(VISIBLE);
                    telephoneBill.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "8":
                //ElectricBill
                if (electricityBill.getVisibility() == VISIBLE) {
                } else {
                    electricityBill.setVisibility(VISIBLE);
                    electricityBill.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "9":
                //RentAgreement
                if (rentAgreement.getVisibility() == VISIBLE) {
                } else {
                    rentAgreement.setVisibility(VISIBLE);
                    rentAgreement.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "10":
                //kvp
                if (kvp.getVisibility() == VISIBLE) {
                } else {
                    kvp.setVisibility(VISIBLE);
                    kvp.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "11":
                //licpolicy
                if (licPolicy.getVisibility() == VISIBLE) {
                } else {
                    licPolicy.setVisibility(VISIBLE);
                    licPolicy.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "12":
                //Form-16
                if (form16.getVisibility() == VISIBLE) {
                } else {
                    form16.setVisibility(VISIBLE);
                    form16.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "13":
                // Form-61
                if (form61.getVisibility() == VISIBLE) {
                } else {
                    form61.setVisibility(VISIBLE);
                    form61.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "14":
                //pensionLetter
                if (pensionLetter.getVisibility() == VISIBLE) {
                } else {
                    pensionLetter.setVisibility(VISIBLE);
                    pensionLetter.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "15":
                //ITR
                if (itr.getVisibility() == VISIBLE) {
                } else {
                    itr.setVisibility(VISIBLE);
                    itr.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "16":
                //PNL
                if (pnl.getVisibility() == VISIBLE) {
                } else {
                    pnl.setVisibility(VISIBLE);
                    linSeemoreFinancialBtn.setVisibility(GONE);
                    linSeemoreFinancialBtn.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "17":
                //salary slip for 6 months
                if (salSlipSix.getVisibility() == VISIBLE) {
                } else {
                    twelvethMarksheet.setVisibility(VISIBLE);
                    twelvethMarksheet.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "18":
                //salary slip for 3 months
                if (salSlipThree.getVisibility() == VISIBLE) {
                } else {
                    twelvethMarksheet.setVisibility(VISIBLE);
                    twelvethMarksheet.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "19":
                //Bank satetment 3months
                if (bankStmntThree.getVisibility() == VISIBLE) {
                } else {
                    bankStmntThree.setVisibility(VISIBLE);
                    bankStmntThree.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }

                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "20":
                //Bank satetment 6months
                if (bankStmntSix.getVisibility() == VISIBLE) {
                } else {
                    bankStmntSix.setVisibility(VISIBLE);
                    bankStmntSix.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }

                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "21":
                if (tenthMarksheet.getVisibility() == VISIBLE) {
                } else {
                    tenthMarksheet.setVisibility(VISIBLE);
                    panCard.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }

                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "22":
                //12thMarkSheet
                if (twelvethMarksheet.getVisibility() == VISIBLE) {
                } else {
                    twelvethMarksheet.setVisibility(VISIBLE);
                    twelvethMarksheet.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }

                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "23":
                //LastCompletedDegreeMarksheet
                if (degreeMarkSheet.getVisibility() == VISIBLE) {
                } else {
                    degreeMarkSheet.setVisibility(VISIBLE);
                    degreeMarkSheet.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }

                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "24":
                if (degreeCertificate.getVisibility() == VISIBLE) {
                } else {
                    degreeCertificate.setVisibility(VISIBLE);
                    degreeCertificate.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }

                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "30":
                //AddressProof
                if (addressProof.getVisibility() == VISIBLE) {
                } else {
                    addressProof.setVisibility(VISIBLE);
                    addressProof.requestFocus();
                }
                if (others.getTag() == null) {
                    others.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }

                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

            case "31":
                //AddressProof
                if (others.getVisibility() == VISIBLE) {
                } else {
                    others.setVisibility(VISIBLE);
                    others.requestFocus();
                }
                if (drivingLicense.getTag() == null) {
                    drivingLicense.setVisibility(GONE);
                }
                if (passport.getTag() == null) {
                    passport.setVisibility(GONE);
                }
                if (voterId.getTag() == null) {
                    voterId.setVisibility(GONE);
                }
                if (telephoneBill.getTag() == null) {
                    telephoneBill.setVisibility(GONE);
                }
                if (electricityBill.getTag() == null) {
                    electricityBill.setVisibility(GONE);
                }
                if (rentAgreement.getTag() == null) {
                    rentAgreement.setVisibility(GONE);
                }
                if (addressProof.getTag() == null) {
                    addressProof.setVisibility(GONE);
                }
                if (bankStmntSix.getTag() == null) {
                    bankStmntSix.setVisibility(GONE);
                }
                if (bankStmntThree.getTag() == null) {
                    bankStmntThree.setVisibility(GONE);
                }
                if (kvp.getTag() == null) {
                    kvp.setVisibility(GONE);
                }
                if (licPolicy.getTag() == null) {
                    licPolicy.setVisibility(GONE);
                }
                if (form16.getTag() == null) {
                    form16.setVisibility(GONE);
                }
                if (form61.getTag() == null) {
                    form61.setVisibility(GONE);
                }
                if (pensionLetter.getTag() == null) {
                    pensionLetter.setVisibility(GONE);
                }
                if (itr.getTag() == null) {
                    itr.setVisibility(GONE);
                }
                if (pnl.getTag() == null) {
                    pnl.setVisibility(GONE);
                }
                if (salSlipSix.getTag() == null) {
                    salSlipSix.setVisibility(GONE);
                }

                if (salSlipThree.getTag() == null) {
                    salSlipThree.setVisibility(GONE);
                }
                if (degreeCertificate.getTag() == null) {
                    degreeCertificate.setVisibility(GONE);
                }
                if (tenthMarksheet.getTag() == null) {
                    tenthMarksheet.setVisibility(GONE);
                }
                if (twelvethMarksheet.getTag() == null) {
                    twelvethMarksheet.setVisibility(GONE);
                }
                if (degreeMarkSheet.getTag() == null) {
                    degreeMarkSheet.setVisibility(GONE);
                }
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                break;

        }

    }

    public interface onUploadFragmentInteractionListener {
        void onUploadInfoFragment(boolean valid, int next);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UploadDocumentFragment.onUploadFragmentInteractionListener) {
            mListener = (UploadDocumentFragment.onUploadFragmentInteractionListener) context;
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


    private void getUploadDocumentsApiCall() {
        /** API CALL **/
        try {
            String url = MainActivity.mainUrl + "document/getDocumentsDetails";
            Map<String, String> params = new HashMap<String, String>();
            if (!Globle.isNetworkAvailable(context)) {
                Toast.makeText(context, R.string.please_check_your_network_connection, Toast.LENGTH_SHORT).show();

            } else {
                VolleyCall volleyCall = new VolleyCall();//http://159.89.204.41/eduvanzApi/document/getapplicantDocumentDetails
                params.put("lead_id", MainActivity.lead_id);//"studentId" -> "2953"
                params.put("fk_applicant_id", LoanTabActivity.applicant_id);//"studentId" -> "2953"
                params.put("applicant_type", "1");//"studentId" -> "2953"
                volleyCall.sendRequest(context, url, null, mFragment, "getDocumentsBorrower", params, MainActivity.auth_token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Personal details
        txtKycDocToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linKycDocBlock.getVisibility() == VISIBLE) {
                    linKycDocBlock.startAnimation(collapseanimationPersonal);
                } else {
                    linKycDocBlock.startAnimation(expandAnimationPersonal);

                }
            }
        });
//Identity details
        txtPhotoToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relPhotoBlock.getVisibility() == VISIBLE) {
                    relPhotoBlock.startAnimation(collapseAnimationIdentity);
                } else {
                    relPhotoBlock.startAnimation(expandAnimationIdentity);
                }
            }
        });
//course details.
        txtAddtionalDocToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relAddtionalDocBlock.getVisibility() == VISIBLE) {
                    relAddtionalDocBlock.startAnimation(collapseAnimationCourse);
                } else {
                    relAddtionalDocBlock.startAnimation(expanAnimationCourse);
                }
            }
        });

//Personal details
        linKycDocToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linKycDocBlock.getVisibility() == VISIBLE) {
                    linKycDocBlock.startAnimation(collapseanimationPersonal);
                } else {
                    linKycDocBlock.startAnimation(expandAnimationPersonal);
                }
            }
        });
//Identity details
        linPhotoToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relPhotoBlock.getVisibility() == VISIBLE) {
                    relPhotoBlock.startAnimation(collapseAnimationIdentity);
                } else {
                    relPhotoBlock.startAnimation(expandAnimationIdentity);
                }
            }
        });
//course details.
        linAddtionalDocToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relAddtionalDocBlock.getVisibility() == VISIBLE) {
                    relAddtionalDocBlock.startAnimation(collapseAnimationCourse);
                } else {
                    relAddtionalDocBlock.startAnimation(expanAnimationCourse);
                }
            }
        });

        /*================================personal details==========================================*/
        collapseanimationPersonal.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linKycDocBlock.setVisibility(GONE);
                Drawable bg;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_angle_down, null);
                    ivPersonalToggle.setColorFilter(context.getResources().getColor(R.color.darkblue), PorterDuff.Mode.MULTIPLY);
                } else {
                    bg = ContextCompat.getDrawable(context, R.drawable.ic_angle_down);
                    DrawableCompat.setTint(bg, context.getResources().getColor(R.color.darkblue));
                }
                ivPersonalToggle.setImageDrawable(bg);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        expandAnimationPersonal.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                linKycDocBlock.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                if(relPhotoBlock.getVisibility() == VISIBLE) {
//                    relPhotoBlock.startAnimation(collapseAnimationCourse);
//                }
//                if(relAddtionalDocBlock.getVisibility() == VISIBLE){
//                    relAddtionalDocBlock.startAnimation(collapseanimationPersonal);
//                }
                Drawable bg;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_angle_up, null);
                    ivPersonalToggle.setColorFilter(context.getResources().getColor(R.color.darkblue), PorterDuff.Mode.MULTIPLY);
                } else {
                    bg = ContextCompat.getDrawable(context, R.drawable.ic_angle_up);
                    DrawableCompat.setTint(bg, context.getResources().getColor(R.color.darkblue));
                }
                ivPersonalToggle.setImageDrawable(bg);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        /*=============================================identity details===================================*/
        collapseAnimationIdentity.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                relPhotoBlock.setVisibility(GONE);
                Drawable bg;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_angle_down, null);
                    ivIdentityToggle.setColorFilter(context.getResources().getColor(R.color.darkblue), PorterDuff.Mode.MULTIPLY);
                } else {
                    bg = ContextCompat.getDrawable(context, R.drawable.ic_angle_down);
                    DrawableCompat.setTint(bg, context.getResources().getColor(R.color.darkblue));
                }
                ivIdentityToggle.setImageDrawable(bg);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        expandAnimationIdentity.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                relPhotoBlock.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                if(linKycDocBlock.getVisibility() == VISIBLE) {
//                    linKycDocBlock.startAnimation(collapseAnimationCourse);
//                }
//                if(relAddtionalDocBlock.getVisibility() == VISIBLE){
//                    relAddtionalDocBlock.startAnimation(collapseanimationPersonal);
//                }
                Drawable bg;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_angle_up, null);
                    ivIdentityToggle.setColorFilter(context.getResources().getColor(R.color.darkblue), PorterDuff.Mode.MULTIPLY);
                } else {
                    bg = ContextCompat.getDrawable(context, R.drawable.ic_angle_up);
                    DrawableCompat.setTint(bg, context.getResources().getColor(R.color.darkblue));
                }
                ivIdentityToggle.setImageDrawable(bg);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        /*===========================================course details=================================*/
        collapseAnimationCourse.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                relAddtionalDocBlock.setVisibility(GONE);
                Drawable bg;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_angle_down, null);
                    ivCourseToggle.setColorFilter(context.getResources().getColor(R.color.darkblue), PorterDuff.Mode.MULTIPLY);
                } else {
                    bg = ContextCompat.getDrawable(context, R.drawable.ic_angle_down);
                    DrawableCompat.setTint(bg, context.getResources().getColor(R.color.darkblue));
                }
                ivCourseToggle.setImageDrawable(bg);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        expanAnimationCourse.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                relAddtionalDocBlock.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                if(linKycDocBlock.getVisibility() == VISIBLE) {
//                    linKycDocBlock.startAnimation(collapseAnimationCourse);
//                }
//                if(relPhotoBlock.getVisibility() == VISIBLE){
//                    relPhotoBlock.startAnimation(collapseanimationPersonal);
//                }
                Drawable bg;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_angle_up, null);
                    ivCourseToggle.setColorFilter(context.getResources().getColor(R.color.darkblue), PorterDuff.Mode.MULTIPLY);
                } else {
                    bg = ContextCompat.getDrawable(context, R.drawable.ic_angle_up);
                    DrawableCompat.setTint(bg, context.getResources().getColor(R.color.darkblue));
                }
                ivCourseToggle.setImageDrawable(bg);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    //====================changes by yash=========================
    private void imageToPdf(String documentTypeNo, String toolbarTitle, String note, String strapplicantId, String strapplicantType) {

        Intent intent = new Intent(getActivity(), ImgToPdfActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("strapplicantId", strapplicantId);
        bundle.putString("strapplicantType", strapplicantType);
        bundle.putString("documentTypeNo", documentTypeNo);
        bundle.putString("toolbarTitle", toolbarTitle);
        bundle.putString("note", note);
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);// Activity is started with requestCode 2
    }

    private void imageToPdfBankStmt(String documentTypeNo, String toolbarTitle, String note, String strapplicantId, String strapplicantType) {

        Intent intent = new Intent(getActivity(), ImgToPdfActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("strapplicantId", strapplicantId);
        bundle.putString("strapplicantType", strapplicantType);
        bundle.putString("documentTypeNo", documentTypeNo);
        bundle.putString("toolbarTitle", toolbarTitle);
        bundle.putString("note", note);
        bundle.putString("lead_id", MainActivity.lead_id);
        bundle.putString("auth_token", MainActivity.auth_token);
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);// Activity is started with requestCode 2
    }

    private void openPdf(String mPath) {

        Uri path = Uri.parse(mPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setDataAndType(path, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), R.string.no_application_available_ro_view_pdf, Toast.LENGTH_SHORT).show();
        }
    }

    private void openAnyFile(String mPath) {

        Uri path = Uri.parse(mPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/vnd.ms-excel");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), R.string.no_application_available_ro_view_excel, Toast.LENGTH_SHORT).show();
        }
    }

    public void downLoadClick(String uri) {
        try {
            Handler handler = new Handler();

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    // your code here
                    handler.post(new Runnable() {
                        public void run() {
                            Log.e(TAG, "downloadUrl+++++: " + uri);
                            Uri Download_Uri = Uri.parse(String.valueOf(uri));

                            try {
                                String fname = "";
                                fname = uri.toString().substring(uri.toString().lastIndexOf("/") + 1);

                                downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);

                                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                request.setAllowedOverRoaming(false);
                                request.setTitle("Your Document is Downloading");
                                request.setDescription("Android Data download using DownloadManager.");

                                request.setVisibleInDownloadsUi(true);

                                request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "Eduvanz" + "/" + "UploadedDoc" + "/" + fname);//

                                progressBar.setVisibility(View.VISIBLE);

                                downloadReference = downloadManager.enqueue(request);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            };

            Thread t = new Thread(r);
            t.start();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //solve this
    private void openImage(String mPath) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.create();
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.imagedialog, null);
            dialog.setView(dialogLayout);
            ImageView image = (ImageView) dialogLayout.findViewById(R.id.imgDialogImage);
            Picasso.with(context).load(mPath).into(image);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.show();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface d) {

                }
            });
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == 2) {
                String message = data.getStringExtra("PATH");
                String doctypeno = data.getStringExtra("documentTypeNo");
                String strapplicantType = data.getStringExtra("strapplicantType");
                String strapplicantId = data.getStringExtra("strapplicantId");

                String FileExtn = null;
                Double FileSize = null;

                uploadFilePath = message;

                String filesz = JavaGetFileSize.getFileSizeMegaBytes(new File(message)).substring(0, JavaGetFileSize.getFileSizeMegaBytes(new File(message)).length() - 3);
                FileSize = Double.valueOf(filesz);

                FileExtn = uploadFilePath.substring(uploadFilePath.lastIndexOf(".") + 1);// Without dot jpg, png

                if (FileExtn.equals("jpg") || FileExtn.equals("jpeg") || FileExtn.equals("png") || FileExtn.equals("pdf") ||
                        FileExtn.equals("bmp") || FileExtn.equals("webp") || FileExtn.equals("zip") || FileExtn.equals("rar")) {

                    if (FileSize < 30) {
                        Log.e("TAG", "onActivityResult: DOC PATH " + uploadFilePath);

                        if (uploadFilePath != null) {
                            // dialog = ProgressDialog.show(MainActivity.this,"","Uploading File...",true);
                            progressBar.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String doctype = "";
                                        int selectUrl = 0;
                                        if (doctypeno.length() > 2) {
                                            doctype = doctypeno.substring(0, 1) + "_SD_PhotoDoc";
                                            selectUrl = 1;
                                        } else {
                                            doctype = doctypeno + "_SD_PhotoDoc";
                                            selectUrl = 0;
                                        }
                                        //creating new thread to handle Http Operations
//                            Log.e("TAG", "File:Path absolute : new" + uploadFilePath);
                                        if (!Globle.isNetworkAvailable(context)) {
                                            //uploadFileOffline(uploadFilePath, doctype, doctypeno, selectUrl);
                                            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                        } else {
                                            uploadFile(uploadFilePath, doctypeno, strapplicantType, strapplicantId);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else {
                            Toast.makeText(context, R.string.please_choose_a_file_first, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, R.string.file_size_exceeds_limits_of_30_mb, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, R.string.file_is_not_in_supported_format, Toast.LENGTH_LONG).show();
                }

            }
            if (resultCode == 1) {
                String message = data.getStringExtra("BACK");

                getUploadDocumentsApiCall();
            }

        }
        if (requestCode == 1) {
            if (resultCode == 1) {
                String message = data.getStringExtra("BACK");

            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
                String FileExtn = null;
                String doctypeno = applicantType;
                String strapplicantType = documentTypeNo;
                String strapplicantId = LoanTabActivity.applicant_id;
                Double FileSize = null;

                String filesz = JavaGetFileSize.getFileSizeMegaBytes(new File(uploadFilePath)).substring(0, JavaGetFileSize.getFileSizeMegaBytes(new File(uploadFilePath)).length() - 3);
                FileSize = Double.valueOf(filesz);

                FileExtn = uploadFilePath.substring(uploadFilePath.lastIndexOf(".") + 1);// Without dot jpg, png

                if (FileExtn.equals("jpg") || FileExtn.equals("jpeg") || FileExtn.equals("png") || FileExtn.equals("pdf") ||
                        FileExtn.equals("bmp") || FileExtn.equals("webp") || FileExtn.equals("zip") || FileExtn.equals("rar")) {

                    if (FileSize < 30) {
                        Log.e("TAG", "onActivityResult: DOC PATH " + uploadFilePath);

                        if (uploadFilePath != null) {
                            // dialog = ProgressDialog.show(MainActivity.this,"","Uploading File...",true);
                            progressBar.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String doctype = "";
                                        int selectUrl = 0;
                                        if (doctypeno.length() > 2) {
                                            doctype = doctypeno.substring(0, 1) + "_SD_PhotoDoc";
                                            selectUrl = 1;
                                        } else {
                                            doctype = doctypeno + "_SD_PhotoDoc";
                                            selectUrl = 0;
                                        }
                                        //creating new thread to handle Http Operations
//                            Log.e("TAG", "File:Path absolute : new" + uploadFilePath);
                                        if (!Globle.isNetworkAvailable(context)) {
                                            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                        } else {
                                            uploadFile(uploadFilePath, doctypeno, strapplicantType, strapplicantId);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else {
                            Toast.makeText(context, R.string.please_choose_a_file_first, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, R.string.file_size_exceeds_limits_of_30_mb, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, R.string.file_is_not_in_supported_format, Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
                String FileExtn = null;
                String doctypeno = applicantType;
                String strapplicantType = documentTypeNo;
                String strapplicantId = LoanTabActivity.applicant_id;
                Double FileSize = null;

                String filesz = JavaGetFileSize.getFileSizeMegaBytes(new File(uploadFilePath)).substring(0, JavaGetFileSize.getFileSizeMegaBytes(new File(uploadFilePath)).length() - 3);
                FileSize = Double.valueOf(filesz);

                FileExtn = uploadFilePath.substring(uploadFilePath.lastIndexOf(".") + 1);// Without dot jpg, png

                if (FileExtn.equals("jpg") || FileExtn.equals("jpeg") || FileExtn.equals("png") || FileExtn.equals("pdf") ||
                        FileExtn.equals("bmp") || FileExtn.equals("webp") || FileExtn.equals("zip") || FileExtn.equals("rar")) {

                    if (FileSize < 30) {
                        Log.e("TAG", "onActivityResult: DOC PATH " + uploadFilePath);

                        if (uploadFilePath != null) {
                            // dialog = ProgressDialog.show(MainActivity.this,"","Uploading File...",true);
                            progressBar.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String doctype = "";
                                        int selectUrl = 0;
                                        if (doctypeno.length() > 2) {
                                            doctype = doctypeno.substring(0, 1) + "_SD_PhotoDoc";
                                            selectUrl = 1;
                                        } else {
                                            doctype = doctypeno + "_SD_PhotoDoc";
                                            selectUrl = 0;
                                        }
                                        //creating new thread to handle Http Operations
//                            Log.e("TAG", "File:Path absolute : new" + uploadFilePath);
                                        if (!Globle.isNetworkAvailable(context)) {
                                            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                        } else {
                                            uploadFile(uploadFilePath, doctypeno, strapplicantType, strapplicantId);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else {
                            Toast.makeText(context, R.string.please_choose_a_file_first, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, R.string.file_size_exceeds_limits_of_30_mb, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, R.string.file_is_not_in_supported_format, Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == SELECT_DOC) {
                Bitmap bm = null;
                String FileExtn = null;
                Long FileSize = null;
                try {//mDensity = 440 mHeight = 375 mWidth = 500
                    bm = decodeUri(data.getData(), context);//5383513
//                    bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri selectedImage = data.getData();
                uploadFilePath = PathFile.getPath(context, selectedImage);

                try {
                    Cursor returnCursor =
                            context.getContentResolver().query(selectedImage, null, null, null, null);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();

                    FileSize = returnCursor.getLong(sizeIndex);//5383513 //26435143
//                    Long fsize = returnCursor.getLong(sizeIndex);//5383513 //26435143
//                    String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
//                    int digitGroups = (int) (Math.log10(fsize) / Math.log10(1024));
//                    FileSize = new DecimalFormat("#,##0.##").format(fsize / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FileExtn = uploadFilePath.substring(uploadFilePath.lastIndexOf(".") + 1);// Without dot jpg, png

                if (FileExtn.equals("jpg") || FileExtn.equals("jpeg") || FileExtn.equals("png") || FileExtn.equals("pdf") ||
                        FileExtn.equals("bmp") || FileExtn.equals("webp") || FileExtn.equals("zip") || FileExtn.equals("rar")) {

                    if (FileSize < 30000000) {
                        Log.e("TAG", "onActivityResult: DOC PATH " + uploadFilePath);
//                imgAadhaar3.setImageDrawable(getResources().getDrawable(R.drawable.pdf_image));
                        if (documentTypeNo.equalsIgnoreCase("1")) {
                            aadharCard.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelAadhaarBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("2")) {
                            panCard.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelPanBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("3")) {
                            passport.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelPassportBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("4")) {
                            voterId.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelVoterIdBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("5")) {
                            drivingLicense.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelDrivingLicenseBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("6")) {
                            telephoneBill.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelTelephoneBillBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("7")) {
                            electricityBill.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelElectricityBillBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("8")) {
                            rentAgreement.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelRentAgreementBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("9")) {
                            addressProof.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelAddressProofBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("10")) {
                            salSlipSix.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelSalarySlipThreeBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("11")) {
                            salSlipThree.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelSalarySlipThreeBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("12")) {
                            bankStmntThree.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelBankStatementThreeBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("13")) {
                            bankStmntSix.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelBankStatementSixBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("14")) {
                            kvp.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelKVPBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("15")) {
                            licPolicy.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelLICPolicyBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("16")) {
                            form16.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelForm16Btn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("17")) {
                            form61.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelForm61Btn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("18")) {
                            pensionLetter.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelPensionLetterBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("19")) {
                            itr.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelITRBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("20")) {
                            pnl.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelPNLBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("21")) {
                            tenthMarksheet.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDeltenth_mark_sheetBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("22")) {
                            twelvethMarksheet.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDeltwelvethMarkSheetBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("23")) {
                            degreeMarkSheet.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDellastCompletedMarkSheetBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("24")) {
                            degreeCertificate.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDellastcompletedDegreeCertificateBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (documentTypeNo.equalsIgnoreCase("31")) {
                            others.setVisibility(View.VISIBLE);
                            if (LoanTabActivity.isEditaDocble) {
                                linDelothersBtn.setVisibility(View.VISIBLE);
                            }

                        }
                    } else {
                        Toast.makeText(context, R.string.file_size_exceeds_limit_of_30_mb, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, R.string.file_is_not_in_supported_format, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private Bitmap decodeUri(Uri selectedImage, Context context) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(selectedImage), null, o);

        int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(selectedImage), null, o2);
    }


//    public int uploadFile(final String selectedFilePath, String doctypeno, String strapplicantType, String strapplicantId) {
//        String urlup = "http://192.168.1.19/eduvanzapi/" + "document/documentUpload";
//
//        Log.e(TAG, "urlup++++++: " + urlup);
//
//        int serverResponseCode = 0;
//        documentTypeNo = doctypeno;
//        Log.e(TAG, "applicantType: " + strapplicantType + "documentTypeNo: " + doctypeno);
//        HttpURLConnection connection;
//        DataOutputStream dataOutputStream;
//        String lineEnd = "\r\n";
//        String twoHyphens = "--";
//        String boundary = "*****";
//
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 1 * 1024 * 1024;
//        File selectedFile = new File(selectedFilePath);
//
//
//        String[] parts = selectedFilePath.split("/");
//        final String fileName = parts[parts.length - 1];
//        String[] fileExtn = fileName.split(".");
//
//
//        if (!selectedFile.isFile()) {
//            //dialog.dismiss();
//            try {
//                progressBar.setVisibility(View.GONE);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e("TAG", "run: " + "Source File Doesn't Exist: " + selectedFilePath);
//                }
//            });
//            return 0;
//        } else {
//            try {
//                FileInputStream fileInputStream = new FileInputStream(selectedFile);
//                URL url = new URL(urlup);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setDoInput(true);
//                connection.setDoOutput(true);
//                connection.setUseCaches(false);
//                connection.setChunkedStreamingMode(1024);
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Connection", "Keep-Alive");
//                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
//                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                connection.setRequestProperty("Authorization", "Bearer " + "8b662812e6e3a599a70223eae684a2af");
//                connection.setRequestProperty("document", selectedFilePath);
//                Log.e("TAG", "Server property" + connection.getRequestMethod() + ":property " + connection.getRequestProperties());
//                dataOutputStream = new DataOutputStream(connection.getOutputStream());
//
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"myfile\";filename=\""
//                        + selectedFilePath + "\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//
//                bytesAvailable = fileInputStream.available();
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                buffer = new byte[bufferSize];
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                while (bytesRead > 0) {
//                    dataOutputStream.write(buffer, 0, bufferSize);
//                    Log.e("TAG", " here: \n\n" + buffer + "\n" + bufferSize);
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                }
//                dataOutputStream.writeBytes(lineEnd);
//
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"fk_lead_id\";fk_lead_id=" + "6126" + "" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes("6126");
//                dataOutputStream.writeBytes(lineEnd);
//
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"page_id\";page_id=" + "1" + "" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes("1");
//                dataOutputStream.writeBytes(lineEnd);
//
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"fk_applicant_id\";fk_applicant_id=" + "9502" + "" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes("9502");
//                dataOutputStream.writeBytes(lineEnd);
//
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\";student_id=" + "6280" + "" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes("6280");
//                dataOutputStream.writeBytes(lineEnd);
//
//                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"doucment_status\";doucment_status=" + "add" + "" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes("add");
//                dataOutputStream.writeBytes(lineEnd);
//
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"fk_document_type_id\";fk_document_type_id=" + documentTypeNo + "" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(documentTypeNo);
//                dataOutputStream.writeBytes(lineEnd);
//
//
//                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//                serverResponseCode = connection.getResponseCode();
//                Log.e("TAG", " here:server response serverResponseCode\n\n" + serverResponseCode);
//                String serverResponseMessage = connection.getResponseMessage();
//                Log.e("TAG", " here: server message serverResponseMessage \n\n" + serverResponseMessage.toString() + "\n" + bufferSize);
//                BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
//                String output = "";
//                sb = new StringBuffer();
//
//                while ((output = br.readLine()) != null) {
//                    sb.append(output);
//                    Log.e("TAG", "uploadFile: " + br);
//                    Log.e("TAG", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
//                }
//                Log.e("TAG", "uploadFile: " + sb.toString());
//                try {
//                    JSONObject mJson = new JSONObject(sb.toString());
//                    final String mData = mJson.getString("status");
//                    final String mData1 = mJson.getString("message");
//
//                    Log.e("TAG", " 2252: " + new Date().toLocaleString());//1538546658896.jpg/
//                    if (mData.equalsIgnoreCase("1")) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                uploadFilePath = "";
//
//                                getUploadDocumentsApiCall();
//
//                                progressBar.setVisibility(View.GONE);
//                                Log.e("TAG", "uploadFile: code 1 " + mData);
//                                Toast.makeText(context, mData1, Toast.LENGTH_SHORT).show();
//
//                                //UnComment this for offline
////                                        try {
////                                            String sSql = "Update DocumentUpload set ISUploaded = '" + true + "' WHERE FilePath = '"+selectedFilePath+"'" ;
////                                            ExecuteSql(sSql);
////                                        } catch (Exception e) {
////                                            e.printStackTrace();
////                                        }
////
////                                    uploadFilePath = "";
////                                    progressBar.setVisibility(View.GONE);
////                                Log.e(TAG, "uploadFile 2267: " + selectUrl + "doctype  " + doctype + "  doctypeno " + doctypeno + " selectedFilePath " + selectedFilePath + " coBorrowerID   " + coBorrowerID);
////                                Toast.makeText(context, mData1, Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//
//                    } else {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressBar.setVisibility(View.GONE);
//                                Log.e("TAG", " 2285: " + new Date().toLocaleString());//1538546658896.jpg/
//                                Toast.makeText(context, mData1+" "+mData, Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                if (serverResponseCode == 200) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.e("TAG", " 2303: " + new Date().toLocaleString());//1538546658896.jpg/
//                        }
//                    });
//                }
//
//                fileInputStream.close();
//                dataOutputStream.flush();
//                dataOutputStream.close();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("TAG", " 2318: " + new Date().toLocaleString());//1538546658896.jpg/
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        Log.e("TAG", " 2335: " + new Date().toLocaleString());//1538546658896.jpg/
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Log.e("TAG", " 2342: " + new Date().toLocaleString());//1538546658896.jpg/
//
//            return serverResponseCode;
//        }
//
//    }

    public int uploadFile(String selectedFilePath, String doctypeno, String strapplicantType, String strapplicantId) {
        String urlup = MainActivity.mainUrl + "document/documentUpload";

        int serverResponseCode = 0;
        documentTypeNo = doctypeno;
        Log.e(TAG, "applicantType: " + strapplicantType + "documentTypeNo: " + doctypeno);
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
        String fileName = parts[parts.length - 1];
        String[] fileExtn = fileName.split(".");

        if (!selectedFile.isFile()) {
            //dialog.dismiss();
            try {
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((LoanTabActivity) context).runOnUiThread(new Runnable() {
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
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setChunkedStreamingMode(1024);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("Authorization", "Bearer " + MainActivity.auth_token);
                connection.setRequestProperty("document", selectedFilePath);
                Log.e("TAG", "Server property" + connection.getRequestMethod() + ":property " + connection.getRequestProperties());
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"myfile\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dataOutputStream.write(buffer, 0, bufferSize);
                    Log.e("TAG", " here: \n\n" + buffer + "\n" + bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                dataOutputStream.writeBytes(lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"fk_lead_id\";fk_lead_id=" + MainActivity.lead_id + "" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(MainActivity.lead_id);
                dataOutputStream.writeBytes(lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"page_id\";page_id=" + "1" + "" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("1");
                dataOutputStream.writeBytes(lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"fk_applicant_id\";fk_applicant_id=" + strapplicantId + "" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(strapplicantId);
                dataOutputStream.writeBytes(lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\";student_id=" + LoanTabActivity.student_id + "" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(LoanTabActivity.student_id);
                dataOutputStream.writeBytes(lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"doucment_status\";doucment_status=" + "add" + "" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("add");
                dataOutputStream.writeBytes(lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"fk_document_type_id\";fk_document_type_id=" + documentTypeNo + "" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(documentTypeNo);
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
                }
                Log.e("TAG", "uploadFile: " + sb.toString());
                try {
                    JSONObject mJson = new JSONObject(sb.toString());
                    String mData = mJson.getString("status");
                    String mData1 = mJson.getString("message");

//                    Log.e("TAG", " 2252: " + new Date().toLocaleString());//1538546658896.jpg/

                    if (mData.equalsIgnoreCase("1")) {
                        ((LoanTabActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadFilePath = "";
                                //delete file path.

                                getUploadDocumentsApiCall();

                                try {
                                    if (selectedFilePath.toString().contains("Ocr")) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            Files.deleteIfExists(Paths.get(selectedFilePath));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                progressBar.setVisibility(GONE);
                                Log.e("TAG", "uploadFile: code 1 " + mData);
                                Toast.makeText(context, mData1, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        ((LoanTabActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                Log.e("TAG", " 2285: " + new Date().toLocaleString());//1538546658896.jpg/
                                Toast.makeText(context, mData1 + " " + mData, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }

                if (serverResponseCode == 200) {
                    ((LoanTabActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Log.e("TAG", " 2303: " + new Date().toLocaleString());//1538546658896.jpg/
                        }
                    });
                }
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                ((LoanTabActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Log.e("TAG", " 2318: " + new Date().toLocaleString());//1538546658896.jpg/
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ((LoanTabActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
//                        Log.e("TAG", " 2335: " + new Date().toLocaleString());//1538546658896.jpg/
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return serverResponseCode;
        }

    }

    private void galleryDocIntent() {
        Intent intent = new Intent();
        intent.setType("*/*");  // for all types of file
//        intent.setType("application/pdf"); // for pdf
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_DOC);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void selectImage() {
        CharSequence[] items = {"Take a Picture", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(context);

                if (items[item].equals("Take a Picture")) {
                    userChoosenTask = "Take a Picture";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                Uri selectedFileUri = data.getData();
                uploadFilePath = PathFile.getPath(context, selectedFileUri);
                Log.e("TAG", "onSelectFromGalleryResult: " + uploadFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        uploadFilePath = destination.toString();
        Log.e("TAG", "onCaptureImageResult: " + uploadFilePath);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getBorrowerDocuments(JSONObject jsonData) {
        try {
            Log.e("SERVER CALL", "getDocuments" + jsonData);
            String status = jsonData.optString("status");
            String message = jsonData.optString("message");

            profileImage.setTag("");
            profileBckgrnd.setVisibility(View.GONE);
            aadharCard.setTag("");
            aadharBckgrnd.setVisibility(View.GONE);
            panCard.setTag("");
            panBckgrnd.setVisibility(View.GONE);
            passport.setTag("");
            passportBckgrnd.setVisibility(View.GONE);
            voterId.setTag("");
            voterIdBckgrnd.setVisibility(View.GONE);
            drivingLicense.setTag("");
            drivingLicenseBckgrnd.setVisibility(View.GONE);
            telephoneBill.setTag("");
            telephoneBillBckgrnd.setVisibility(View.GONE);
            electricityBill.setTag("");
            electricityBillBckgrnd.setVisibility(View.GONE);
            degreeCertificate.setTag("");
            degreeCertificateBckgrnd.setVisibility(View.GONE);
            others.setTag("");
            othersBackground.setVisibility(View.GONE);
            rentAgreement.setTag("");
            rentAgreementBckgrnd.setVisibility(View.GONE);
            addressProof.setTag("");
            addressProofBckgrnd.setVisibility(View.GONE);
            salSlipSix.setTag("");
            salSixBckgrnd.setVisibility(View.GONE);
            salSlipThree.setTag("");
            salThreeBckgrnd.setVisibility(View.GONE);
            bankStmntThree.setTag("");
            bankThreeBckgrnd.setVisibility(View.GONE);
            bankStmntSix.setTag("");
            bankSixBckgrnd.setVisibility(View.GONE);
            kvp.setTag("");
            kvpBckgrnd.setVisibility(View.GONE);
            licPolicy.setTag("");
            licPolicyBckgrnd.setVisibility(View.GONE);
            form16.setTag("");
            form16Bckgrnd.setVisibility(View.GONE);
            form61.setTag("");
            form61Bckgrnd.setVisibility(View.GONE);
            pensionLetter.setTag("");
            pensionBckgrnd.setVisibility(View.GONE);
            itr.setTag("");
            itrBckgrnd.setVisibility(View.GONE);
            pnl.setTag("");
            pnlBckgrnd.setVisibility(View.GONE);
            degreeMarkSheet.setTag("");
            degreeMarksheetBckgrnd.setVisibility(View.GONE);
            tenthMarksheet.setTag("");
            tenthBckgrnd.setVisibility(View.GONE);
            twelvethMarksheet.setTag("");
            twelthBckgrnd.setVisibility(View.GONE);

            String baseUrl = String.valueOf(jsonData.getJSONObject("result").get("baseUrl"));
            if (status.equalsIgnoreCase("1")) {
                String strFileName, FileExtn;

                Boolean bPhoto = true, bAadhaar = true, bPan = true, bAddress = true, bPassport = true, bVoterId = true, bDrivingLicense = true,
                        bTelephoneBill = true, bElectricityBill = true, bRentAgreemnet = true,

                        bSalSlip6 = true, bSalSlip3 = true, bBankStmt3 = true, bBankStmt6 = true, bKVP = true, bLic = true, bForm16 = true,
                        bForm61 = true, bPension = true, bITR = true, bPNL = true, bDregreeMarkSheet = true, bDegreeCerti = true,
                        bOtherDoc = true, btenthmark = true, btwelvethmark = true;

                document_arrayList = new ArrayList<>();
                documenPOJOArrayList = new ArrayList<>();

                JSONArray jsonDocArray = jsonData.getJSONArray("extra_document");
                document_arrayList.add("Select document to upload");
                for (int i = 0; i < jsonDocArray.length(); i++) {
                    JSONObject jsonObject1 = jsonDocArray.getJSONObject(i);

                    DocumenPOJO documenPOJO = new DocumenPOJO();
                    if (jsonObject1.getString("document_type_id").equals("1") || jsonObject1.getString("document_type_id").equals("2") || jsonObject1.getString("document_type_id").equals("3") ||
                            jsonObject1.getString("document_type_id").equals("25") || jsonObject1.getString("document_type_id").equals("26") || jsonObject1.getString("document_type_id").equals("27") ||
                            jsonObject1.getString("document_type_id").equals("28") || jsonObject1.getString("document_type_id").equals("29") || jsonObject1.getString("document_type_id").equals("32") ||
                            jsonObject1.getString("document_type_id").equals("33") || jsonObject1.getString("document_type_id").equals("34")) {  //student
                        continue;
                    }

                    documenPOJO.document_type_id = jsonObject1.getString("document_type_id");//fk_document_type_id  document_type_id
                    documenPOJO.document_name = jsonObject1.getString("document_name");
                    documenPOJO.document_category = jsonObject1.getString("document_category");
                    documenPOJO.document_purpose = jsonObject1.getString("document_purpose");
                    document_arrayList.add(jsonObject1.getString("document_name"));
                    documenPOJOArrayList.add(documenPOJO);
                }

                arrayAdapter_document = new ArrayAdapter(context, R.layout.custom_layout_spinner, document_arrayList);
                spDocument.setAdapter(arrayAdapter_document);
                arrayAdapter_document.notifyDataSetChanged();

                //uploaded document

                try {
                    JSONArray jsonArray = jsonData.getJSONArray("uploaded_files");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String s = jsonObject1.getString("document_type_id");
                        String image = jsonObject1.getString("doc_path");
                        String doc_upload_id = jsonObject1.getString("doc_upload_id");
                        String verification_status = jsonObject1.getString("verification_status");
                        String document_name = jsonObject1.getString("document_name");

                        switch (s) {

                            case "1":
                                if (bPhoto) {

                                    if (profileImage.getVisibility() == VISIBLE) {
                                    } else {
                                        profileImage.setVisibility(VISIBLE);
                                    }
                                    profileImage.setTag(baseUrl + image);
                                    profileBckgrnd.setVisibility(View.VISIBLE);
                                    ivcolorphotogratitle.setVisibility(VISIBLE);
                                    linDelPhotoBtn.setTag(doc_upload_id);
                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelPhotoBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelPhotoBtn.setVisibility(GONE);
                                    }

                                /*imgPhotoUploadTick.setVisibility(View.VISIBLE);
                                txtPhoto1.setVisibility(View.GONE);*/
//                              imgPhoto1.setBackgroundResource(R.drawable.pdf_image);
                                    /*Picasso.with(context).load(baseUrl + image).into(imgPhoto1);
                                     */

                                    bPhoto = false;
                                }
                                break;

                            case "3":
                                if (bAadhaar) {
                                    if (aadharCard.getVisibility() == VISIBLE) {
                                    } else {
                                        aadharCard.setVisibility(VISIBLE);
                                    }
                                    aadharCard.setTag(baseUrl + image);
                                    aadharBckgrnd.setVisibility(VISIBLE);
                                    linDelAadhaarBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelAadhaarBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelAadhaarBtn.setVisibility(GONE);
                                    }
                               /* imgAadhaarUploadTick3.setVisibility(View.VISIBLE);
                                txtAadhaar3.setVisibility(View.GONE);
*/
                                    strFileName = aadharCard.getTag().toString().substring(aadharCard.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnAadhar.setText(R.string.preview);*/
                                        /*imgAadhaar3.setBackgroundResource(R.drawable.pdf_image);
                                         */
                                    } else {
                                        /*btnAadhar.setText(R.string.download);*/
                                    }
                                    //this below condition apply for green icon sign dispaly on KYC title

                                    if (bAadhaar) {
                                        ivkyctitlecheck.setVisibility(VISIBLE);
                                    }

                                    bAadhaar = false;


                                }
                                break;

                            case "2":
                                if (bPan) {
                                    if (panCard.getVisibility() == VISIBLE) {
                                    } else {
                                        panCard.setVisibility(VISIBLE);
                                    }
                                    panCard.setTag(baseUrl + image);
                                    panBckgrnd.setVisibility(View.VISIBLE);
                                    linDelPanBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelPanBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelPanBtn.setVisibility(GONE);
                                    }
                                /*imgPanUploadTick2.setVisibility(View.VISIBLE);
                                txtPan2.setVisibility(View.GONE);*/
                                    strFileName = panCard.getTag().toString().substring(panCard.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnPanCard.setText(R.string.preview);*/
                                        /*imgPan2.setBackgroundResource(R.drawable.pdf_image);*/
                                    } else {
                                        /*btnPanCard.setText(R.string.download);*/
                                        /* imgPan2.setBackgroundResource(R.drawable.zip_image);*/
                                    }

                                    //required two condition for if aadhar and pan both uploaded then only display check sign
                                    if (bPan) {
                                        ivkyctitlecheck.setVisibility(VISIBLE);
                                    }

//                        Picasso.with(context).load(String.valueOf(bm)).into(imgPan2);
                                    bPan = false;
                                }
                                break;

                            case "4":
                                if (bPassport) {
                                    if (passport.getVisibility() == VISIBLE) {
                                    } else {
                                        passport.setVisibility(VISIBLE);
                                    }

                                    passport.setTag(baseUrl + image);
                                    passportBckgrnd.setVisibility(VISIBLE);
                                    linDelPassportBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelPassportBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelPassportBtn.setVisibility(GONE);
                                    }
                                /*imgAddressUploadTick38.setVisibility(View.VISIBLE);
                                txtAddress38.setVisibility(View.GONE);*/
                                    strFileName = passport.getTag().toString().substring(passport.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnPassport.setText(R.string.preview);*/
                                        /*imgAddress38.setBackgroundResource(R.drawable.pdf_image);*/
                                    } else {
                                        /*btnPassport.setText(R.string.download);*/
                                        /*imgAddress38.setBackgroundResource(R.drawable.zip_image);*/
                                    }

//                        Picasso.with(context).load(String.valueOf(bm)).into(imgAddress38);
                                    bAddress = false;
                                }
                                break;

                            case "5":

                                if (bVoterId) {

                                    if (voterId.getVisibility() == VISIBLE) {
                                    } else {
                                        voterId.setVisibility(VISIBLE);
                                    }

                                    voterId.setTag(baseUrl + image);
                                    voterIdBckgrnd.setVisibility(VISIBLE);
                                    linDelVoterIdBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelVoterIdBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelVoterIdBtn.setVisibility(GONE);
                                    }
                                /*imgSalarySlipUploadTick18.setVisibility(View.VISIBLE);
                                txtSalarySlip18.setVisibility(View.GONE);*/
                                    strFileName = voterId.getTag().toString().substring(voterId.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /* btnVoterId.setText(R.string.preview);*/
                                        /*imgSalarySlip18.setBackgroundResource(R.drawable.pdf_image);*/
                                    } else {
                                        /*btnVoterId.setText(R.string.download);*/
                                        /* imgSalarySlip18.setBackgroundResource(R.drawable.zip_image);*/
                                    }
//                        Picasso.with(context).load(baseUrl + image).into(imgSalarySlip18);
                                    bVoterId = false;
                                }
                                break;

                            case "6":

                                if (bDrivingLicense) {
                                    if (drivingLicense.getVisibility() == VISIBLE) {
                                    } else {
                                        drivingLicense.setVisibility(VISIBLE);
                                    }
                                    drivingLicense.setTag(baseUrl + image);
                                    drivingLicenseBckgrnd.setVisibility(VISIBLE);
                                    linDelDrivingLicenseBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelDrivingLicenseBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelDrivingLicenseBtn.setVisibility(GONE);
                                    }
                               /* imgBankStmtUploadTick19.setVisibility(View.VISIBLE);
                                txtBankStmt19.setVisibility(View.GONE);*/
                                    strFileName = drivingLicense.getTag().toString().substring(drivingLicense.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*drivingLicense.setText(R.string.preview);*/
                                        //imgBankStmt19.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnDrivingLicense.setText(R.string.download);*/
                                        //imgBankStmt19.setBackgroundResource(R.drawable.zip_image);

                                    }
//                        Picasso.with(context).load(baseUrl + image).into(imgBankStmt19);
                                    bDrivingLicense = false;
                                }

                                break;

                            case "7":

                                if (bTelephoneBill) {
                                    if (telephoneBill.getVisibility() == VISIBLE) {
                                    } else {
                                        telephoneBill.setVisibility(VISIBLE);
                                    }
                                    telephoneBill.setTag(baseUrl + image);
                                    telephoneBillBckgrnd.setVisibility(VISIBLE);
                                    linDelTelephoneBillBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelTelephoneBillBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelTelephoneBillBtn.setVisibility(GONE);
                                    }
                                /*imgDegreeMarkSheetUploadTick23.setVisibility(View.VISIBLE);
                                txtDegreeMarkSheet23.setVisibility(View.GONE);*/
                                    strFileName = telephoneBill.getTag().toString().substring(telephoneBill.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnTelephoneBill.setText(R.string.preview);*/
                                        //      imgDegreeMarkSheet23.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnTelephoneBill.setText(R.string.download);*/
                                        //  imgDegreeMarkSheet23.setBackgroundResource(R.drawable.zip_image);

                                    }
                                    //                        Picasso.with(context).load(baseUrl + image).into(imgDegreeMarkSheet23);
                                    bTelephoneBill = false;
                                }
                                break;

                            case "8":

                                if (bElectricityBill) {
                                    if (electricityBill.getVisibility() == VISIBLE) {
                                    } else {
                                        electricityBill.setVisibility(VISIBLE);
                                    }
                                    electricityBill.setTag(baseUrl + image);
                                    electricityBillBckgrnd.setVisibility(VISIBLE);
                                    linDelElectricityBillBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelElectricityBillBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelElectricityBillBtn.setVisibility(GONE);
                                    }
                               /* imgDegreeMarkSheetUploadTick23.setVisibility(View.VISIBLE);
                                txtDegreeMarkSheet23.setVisibility(View.GONE);*/
                                    strFileName = electricityBill.getTag().toString().substring(electricityBill.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*electricityBill.setText(R.string.preview);*/
                                        //imgDegreeMarkSheet23.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnTelephoneBill.setText(R.string.download);*/
                                        //imgDegreeMarkSheet23.setBackgroundResource(R.drawable.zip_image);

                                    }
                                    //                        Picasso.with(context).load(baseUrl + image).into(imgDegreeMarkSheet23);
                                    bElectricityBill = false;
                                }
                                break;

                            case "24":

                                if (bDegreeCerti) {
                                    if (degreeCertificate.getVisibility() == VISIBLE) {
                                    } else {
                                        degreeCertificate.setVisibility(VISIBLE);
                                    }
                                    degreeCertificate.setTag(baseUrl + image);
                                    degreeCertificateBckgrnd.setVisibility(VISIBLE);
                                    linDellastcompletedDegreeCertificateBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDellastcompletedDegreeCertificateBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDellastcompletedDegreeCertificateBtn.setVisibility(GONE);
                                    }
                                /*imgDegreeCertiUploadTick24.setVisibility(View.VISIBLE);
                                txtDegreeCerti24.setVisibility(View.GONE);*/
                                    strFileName = degreeCertificate.getTag().toString().substring(degreeCertificate.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnDegreeCertificate.setText(R.string.preview);*/
                                        // imgDegreeCerti24.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnDegreeCertificate.setText(R.string.download);*/
                                        //imgDegreeCerti24.setBackgroundResource(R.drawable.zip_image);
                                    }
//                        Picasso.with(context).load(baseUrl + image).into(imgDegreeCerti24);
                                    bDegreeCerti = false;
                                }

                                break;

                            case "31":

                                if (bOtherDoc) {

                                    if (others.getVisibility() == VISIBLE) {
                                    } else {
                                        others.setVisibility(VISIBLE);
                                    }
                                    others.setTag(baseUrl + image);
                                    othersBackground.setVisibility(VISIBLE);
                                    linDelothersBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelothersBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelothersBtn.setVisibility(GONE);
                                    }
                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = others.getTag().toString().substring(others.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnOthers.setText(R.string.preview);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnOthers.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bOtherDoc = false;
                                }
                                break;

                            case "9":

                                if (bRentAgreemnet) {

                                    if (rentAgreement.getVisibility() == VISIBLE) {
                                    } else {
                                        rentAgreement.setVisibility(VISIBLE);
                                    }
                                    rentAgreement.setTag(baseUrl + image);
                                    rentAgreementBckgrnd.setVisibility(VISIBLE);
                                    linDelRentAgreementBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelRentAgreementBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelRentAgreementBtn.setVisibility(GONE);
                                    }
                               /* imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = rentAgreement.getTag().toString().substring(rentAgreement.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnRentAgreement.setText(R.string.preview);*/
                                        // imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnRentAgreement.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bRentAgreemnet = false;
                                }
                                break;

                            case "30":

                                if (bAddress) {

                                    if (addressProof.getVisibility() == VISIBLE) {
                                    } else {
                                        addressProof.setVisibility(VISIBLE);
                                    }
                                    addressProof.setTag(baseUrl + image);
                                    addressProofBckgrnd.setVisibility(VISIBLE);
                                    linDelAddressProofBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelAddressProofBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelAddressProofBtn.setVisibility(GONE);
                                    }
                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = addressProof.getTag().toString().substring(addressProof.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnAddressProof.setText(R.string.preview);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /* btnAddressProof.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bAddress = false;
                                }
                                break;

                            case "17":

                                if (bSalSlip6) {
                                    if (salSlipSix.getVisibility() == VISIBLE) {
                                    } else {
                                        salSlipSix.setVisibility(VISIBLE);
                                    }
                                    salSlipSix.setTag(baseUrl + image);
                                    salSixBckgrnd.setVisibility(VISIBLE);
                                    linDelSalarySlipSixBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelSalarySlipSixBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelSalarySlipSixBtn.setVisibility(GONE);
                                    }
                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = salSlipSix.getTag().toString().substring(salSlipSix.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnSalSlipSix.setText(R.string.preview);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnSalSlipSix.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bSalSlip6 = false;
                                }
                                break;

                            case "18":

                                if (bSalSlip3) {
                                    if (salSlipThree.getVisibility() == VISIBLE) {
                                    } else {
                                        salSlipThree.setVisibility(VISIBLE);
                                    }
                                    salSlipThree.setTag(baseUrl + image);
                                    salThreeBckgrnd.setVisibility(VISIBLE);
                                    linDelSalarySlipThreeBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelSalarySlipThreeBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelSalarySlipThreeBtn.setVisibility(GONE);
                                    }

                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = salSlipThree.getTag().toString().substring(salSlipThree.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnSalSlipThree.setText(R.string.preview);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnSalSlipThree.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bSalSlip3 = false;
                                }
                                break;

                            case "19":

                                if (bBankStmt3) {

                                    if (bankStmntThree.getVisibility() == VISIBLE) {
                                    } else {
                                        bankStmntThree.setVisibility(VISIBLE);
                                    }
                                    bankStmntThree.setTag(baseUrl + image);
                                    bankThreeBckgrnd.setVisibility(VISIBLE);
                                    linDelBankStatementThreeBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelBankStatementThreeBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelBankStatementThreeBtn.setVisibility(GONE);
                                    }
                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = bankStmntThree.getTag().toString().substring(bankStmntThree.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnBankStmntThree.setText(R.string.preview);*/
                                        // imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnBankStmntThree.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bBankStmt3 = false;
                                }
                                break;

                            case "20":

                                if (bBankStmt6) {

                                    if (bankStmntSix.getVisibility() == VISIBLE) {

                                    } else {
                                        bankStmntSix.setVisibility(VISIBLE);
                                    }
                                    bankStmntSix.setTag(baseUrl + image);
                                    bankSixBckgrnd.setVisibility(VISIBLE);
                                    linDelBankStatementSixBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelBankStatementSixBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelBankStatementSixBtn.setVisibility(GONE);
                                    }
                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = bankStmntSix.getTag().toString().substring(bankStmntSix.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*bankStmntSix.setText(R.string.preview);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnBankStmntSix.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bBankStmt6 = false;
                                }
                                break;

                            case "10":

                                if (bKVP) {
                                    if (kvp.getVisibility() == VISIBLE) {

                                    } else {
                                        kvp.setVisibility(VISIBLE);
                                    }
                                    kvp.setTag(baseUrl + image);
                                    kvpBckgrnd.setVisibility(VISIBLE);
                                    linDelKVPBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelKVPBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelKVPBtn.setVisibility(GONE);
                                    }
                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = kvp.getTag().toString().substring(kvp.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnKVP.setText(R.string.preview);*/
                                        // imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnKVP.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bKVP = false;
                                }
                                break;

                            case "11":

                                if (bLic) {
                                    if (licPolicy.getVisibility() == VISIBLE) {

                                    } else {
                                        licPolicy.setVisibility(VISIBLE);
                                    }
                                    licPolicy.setTag(baseUrl + image);
                                    licPolicyBckgrnd.setVisibility(VISIBLE);
                                    linDelLICPolicyBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelLICPolicyBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelLICPolicyBtn.setVisibility(GONE);
                                    }

                               /* imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = licPolicy.getTag().toString().substring(licPolicy.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnLICPolicy.setText(R.string.preview);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnLICPolicy.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bLic = false;
                                }
                                break;

                            case "12":

                                if (bForm16) {

                                    if (form16.getVisibility() == VISIBLE) {

                                    } else {
                                        form16.setVisibility(VISIBLE);
                                    }
                                    form16.setTag(baseUrl + image);
                                    form16Bckgrnd.setVisibility(VISIBLE);
                                    linDelForm16Btn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelForm16Btn.setVisibility(VISIBLE);
                                    } else {
                                        linDelForm16Btn.setVisibility(GONE);
                                    }

                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = form16.getTag().toString().substring(form16.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /* btnForm16.setText(R.string.preview);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnForm16.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bForm16 = false;
                                }
                                break;

                            case "13":

                                if (bForm61) {
                                    if (form61.getVisibility() == VISIBLE) {

                                    } else {
                                        form61.setVisibility(VISIBLE);
                                    }
                                    form61.setTag(baseUrl + image);
                                    form61Bckgrnd.setVisibility(VISIBLE);
                                    linDelForm61Btn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelForm61Btn.setVisibility(VISIBLE);
                                    } else {
                                        linDelForm61Btn.setVisibility(GONE);
                                    }
                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = form61.getTag().toString().substring(form61.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnForm61.setText(R.string.preview);*/
                                        // imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnForm61.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bForm61 = false;
                                }
                                break;

                            case "14":

                                if (bPension) {

                                    if (pensionLetter.getVisibility() == VISIBLE) {

                                    } else {
                                        pensionLetter.setVisibility(VISIBLE);
                                    }
                                    pensionLetter.setTag(baseUrl + image);
                                    pensionBckgrnd.setVisibility(VISIBLE);
                                    linDelPensionLetterBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelPensionLetterBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelPensionLetterBtn.setVisibility(GONE);
                                    }
                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = pensionLetter.getTag().toString().substring(pensionLetter.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnPensionLetter.setText(R.string.preview);*/
                                        //   imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnPensionLetter.setText(R.string.download);*/
                                        //  imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bPension = false;
                                }
                                break;

                            case "15":

                                if (bITR) {

                                    if (itr.getVisibility() == VISIBLE) {

                                    } else {
                                        itr.setVisibility(VISIBLE);
                                    }
                                    itr.setTag(baseUrl + image);
                                    itrBckgrnd.setVisibility(VISIBLE);
                                    linDelITRBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelITRBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelITRBtn.setVisibility(GONE);
                                    }
                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = itr.getTag().toString().substring(itr.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnITR.setText(R.string.preview);*/
                                        //   imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnITR.setText(R.string.download);*/
                                        //  imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bITR = false;
                                }
                                break;

                            case "16":

                                if (bPNL) {

                                    if (pnl.getVisibility() == VISIBLE) {

                                    } else {
                                        pnl.setVisibility(VISIBLE);
                                    }
                                    pnl.setTag(baseUrl + image);
                                    pnlBckgrnd.setVisibility(VISIBLE);
                                    linDelPNLBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDelPNLBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDelPNLBtn.setVisibility(GONE);
                                    }
                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = pnl.getTag().toString().substring(pnl.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnPNL.setText(R.string.preview);*/
                                        // imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnPNL.setText(R.string.download);*/
                                        // imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bPNL = false;
                                }
                                break;

                            case "23":

                                if (bDregreeMarkSheet) {

                                    if (degreeMarkSheet.getVisibility() == VISIBLE) {

                                    } else {
                                        degreeMarkSheet.setVisibility(VISIBLE);
                                    }
                                    degreeMarkSheet.setTag(baseUrl + image);
                                    degreeMarksheetBckgrnd.setVisibility(VISIBLE);
                                    linDellastCompletedMarkSheetBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDellastCompletedMarkSheetBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDellastCompletedMarkSheetBtn.setVisibility(GONE);
                                    }
                               /* imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = degreeMarkSheet.getTag().toString().substring(degreeMarkSheet.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btnDegreeMarksheet.setText(R.string.preview);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btnDegreeMarksheet.setText(R.string.download);*/
                                        //imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    bDregreeMarkSheet = false;
                                }
                                break;

                            case "21":

                                if (btenthmark) {
                                    if (tenthMarksheet.getVisibility() == VISIBLE) {

                                    } else {
                                        tenthMarksheet.setVisibility(VISIBLE);
                                    }
                                    tenthMarksheet.setTag(baseUrl + image);
                                    tenthBckgrnd.setVisibility(VISIBLE);
                                    linDeltenth_mark_sheetBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDeltenth_mark_sheetBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDeltenth_mark_sheetBtn.setVisibility(GONE);
                                    }
                                /*imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = tenthMarksheet.getTag().toString().substring(tenthMarksheet.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /* btntenthMarksheet.setText(R.string.preview);*/
                                        // imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btntenthMarksheet.setText(R.string.download);*/
                                        // imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    btenthmark = false;
                                }
                                break;

                            case "22":

                                if (btwelvethmark) {

                                    if (twelvethMarksheet.getVisibility() == VISIBLE) {

                                    } else {
                                        twelvethMarksheet.setVisibility(VISIBLE);
                                    }
                                    twelvethMarksheet.setTag(baseUrl + image);
                                    twelthBckgrnd.setVisibility(VISIBLE);
                                    linDeltwelvethMarkSheetBtn.setTag(doc_upload_id);

                                    if (LoanTabActivity.isEditaDocble) {
                                        linDeltwelvethMarkSheetBtn.setVisibility(VISIBLE);
                                    } else {
                                        linDeltwelvethMarkSheetBtn.setVisibility(GONE);
                                    }
                               /* imgOtherDocUploadTick31.setVisibility(View.VISIBLE);
                                txtOtherDoc31.setVisibility(View.GONE);*/
                                    strFileName = twelvethMarksheet.getTag().toString().substring(twelvethMarksheet.getTag().toString().lastIndexOf("/") + 1);

                                    FileExtn = strFileName.substring(strFileName.lastIndexOf('.') + 1);

                                    if (FileExtn.equals("pdf")) {
                                        /*btntwelvethMarksheet.setText(R.string.preview);*/
                                        // imgOtherDoc31.setBackgroundResource(R.drawable.pdf_image);
                                    } else {
                                        /*btntwelvethMarksheet.setText(R.string.download);*/
                                        // imgOtherDoc31.setBackgroundResource(R.drawable.zip_image);
                                    }
//                      Picasso.with(context).load(baseUrl + image).into(imgOtherDoc31);
                                    btwelvethmark = false;
                                }
                                break;
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Add new code for auto upload document of  aadhar and pan if file is exist.

                if (aadharCard.getTag() == null) {

                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/PDFfiles");

                    if (filepath.exists()) {
                        File file = null;
                        File[] files = filepath.listFiles();
                        for (int i = 0; i < files.length; ++i) {
                            if (files[i].getName().contains("AadhaarOcr.pdf")) {
                                file = files[i];
                            }
                        }

                        if (file != null) {
                            if (file.getName().contains("AadhaarOcr.pdf")) {
                                String message1 = file.getPath();
                                String doctypeno = "3";
                                String strapplicantType = "1";
                                String strapplicantId = LoanTabActivity.applicant_id;

                                String FileExtn1 = null;
                                Double FileSize = null;

                                uploadFilePath = message1;

                                String filesz = JavaGetFileSize.getFileSizeMegaBytes(new File(message)).substring(0, JavaGetFileSize.getFileSizeMegaBytes(new File(message)).length() - 3);
                                FileSize = Double.valueOf(filesz);

                                FileExtn1 = uploadFilePath.substring(uploadFilePath.lastIndexOf(".") + 1);// Without dot jpg, png

                                if (FileExtn1.equals("jpg") || FileExtn1.equals("jpeg") || FileExtn1.equals("png") || FileExtn1.equals("pdf") ||
                                        FileExtn1.equals("bmp") || FileExtn1.equals("webp") || FileExtn1.equals("zip") || FileExtn1.equals("rar")) {

                                    if (FileSize < 30) {
                                        Log.e("TAG", "onActivityResult: DOC PATH " + uploadFilePath);

                                        if (uploadFilePath != null) {
                                            // dialog = ProgressDialog.show(MainActivity.this,"","Uploading File...",true);
                                            // progressBar.setVisibility(View.VISIBLE);
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {

                                                        String doctype = "";
                                                        int selectUrl = 0;
                                                        if (doctypeno.length() > 2) {
                                                            doctype = doctypeno.substring(0, 1) + "_SD_PhotoDoc";
                                                            selectUrl = 1;
                                                        } else {
                                                            doctype = doctypeno + "_SD_PhotoDoc";
                                                            selectUrl = 0;
                                                        }
                                                        //creating new thread to handle Http Operations
                                                        if (!Globle.isNetworkAvailable(context)) {
                                                            //uploadFileOffline(uploadFilePath, doctype, doctypeno, selectUrl);
                                                            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            uploadFile(uploadFilePath, doctypeno, strapplicantType, strapplicantId);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();
                                        } else {
                                            Toast.makeText(context, R.string.please_choose_a_file_first, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, R.string.file_size_exceeds_limits_of_30_mb, Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(context, R.string.file_is_not_in_supported_format, Toast.LENGTH_LONG).show();
                                }

                            }
                        }

                    }

                } else if (panCard.getTag() == null) {

                    File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/PDFfiles");

                    if (filepath.exists()) {
                        File file = null;
                        File[] files = filepath.listFiles();
                        for (int i = 0; i < files.length; ++i) {
                            if (files[i].getName().contains("PanOcr.pdf")) {
                                file = files[i];
                            }
                        }

                        if (file != null) {

                            if (file.getName().contains("PanOcr.pdf")) {
                                {
                                    String message1 = file.getPath();
                                    String doctypeno = "2";
                                    String strapplicantType = "1";
                                    String strapplicantId = LoanTabActivity.applicant_id;

                                    String FileExtn1 = null;
                                    Double FileSize = null;

                                    uploadFilePath = message1;

                                    String filesz = JavaGetFileSize.getFileSizeMegaBytes(new File(message)).substring(0, JavaGetFileSize.getFileSizeMegaBytes(new File(message)).length() - 3);
                                    FileSize = Double.valueOf(filesz);

                                    FileExtn1 = uploadFilePath.substring(uploadFilePath.lastIndexOf(".") + 1);// Without dot jpg, png

                                    if (FileExtn1.equals("jpg") || FileExtn1.equals("jpeg") || FileExtn1.equals("png") || FileExtn1.equals("pdf") ||
                                            FileExtn1.equals("bmp") || FileExtn1.equals("webp") || FileExtn1.equals("zip") || FileExtn1.equals("rar")) {

                                        if (FileSize < 30) {
                                            Log.e("TAG", "onActivityResult: DOC PATH " + uploadFilePath);

                                            if (uploadFilePath != null) {
                                                // dialog = ProgressDialog.show(MainActivity.this,"","Uploading File...",true);
                                                // progressBar.setVisibility(View.VISIBLE);
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            //note add sleep.thread if take time to find path

                                                            String doctype = "";
                                                            int selectUrl = 0;
                                                            if (doctypeno.length() > 2) {
                                                                doctype = doctypeno.substring(0, 1) + "_SD_PhotoDoc";
                                                                selectUrl = 1;
                                                            } else {
                                                                doctype = doctypeno + "_SD_PhotoDoc";
                                                                selectUrl = 0;
                                                            }
                                                            //creating new thread to handle Http Operations
                                                            if (!Globle.isNetworkAvailable(context)) {
                                                                //uploadFileOffline(uploadFilePath, doctype, doctypeno, selectUrl);
                                                                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                uploadFile(uploadFilePath, doctypeno, strapplicantType, strapplicantId);
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }).start();
                                            } else {
                                                Toast.makeText(context, R.string.please_choose_a_file_first, Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(context, R.string.file_size_exceeds_limits_of_30_mb, Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(context, R.string.file_is_not_in_supported_format, Toast.LENGTH_LONG).show();
                                    }

                                }
                            }
                        }
                    }

                }

//                if (profileImage.getTag() != null || aadharCard.getTag() != null || panCard.getTag() != null) {//kyc
//                    Drawable bg;
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                        bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_check_circle_green, null);
//                        ivKyc.setColorFilter(context.getResources().getColor(R.color.colorGreen), PorterDuff.Mode.MULTIPLY);
//                    } else {
//                        bg = ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green);
//                        DrawableCompat.setTint(bg, context.getResources().getColor(R.color.colorGreen));
//                    }
//                    ivKyc.setImageDrawable(bg);
//                    linKYCblock.setBackground(context.getResources().getDrawable(R.drawable.border_green));
//                    linKYCDocuments.setBackground(context.getResources().getDrawable(R.drawable.border_green));
//                    linKYCblockBottom.setBackground(context.getResources().getDrawable(R.drawable.border_green));
//                }

//                if (salSlipSix.getTag() != null || salSlipThree.getTag() != null || bankStmntThree.getTag() != null || bankStmntSix.getTag() != null || kvp.getTag() != null || licPolicy.getTag() != null ||
//                        form16.getTag() != null || form61.getTag() != null || pensionLetter.getTag() != null || itr.getTag() != null || pnl.getTag() != null) {//Fin
//
//                    Drawable bg;
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                        bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_check_circle_green, null);
//                        ivFinancial.setColorFilter(context.getResources().getColor(R.color.colorGreen), PorterDuff.Mode.MULTIPLY);
//                    } else {
//                        bg = ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green);
//                        DrawableCompat.setTint(bg, context.getResources().getColor(R.color.colorGreen));
//                    }
//                    ivFinancial.setImageDrawable(bg);
//                    linFinancBlock.setBackground(context.getResources().getDrawable(R.drawable.border_green));
//                    linFinanceDocuments.setBackground(context.getResources().getDrawable(R.drawable.border_green));
//                    linFinancBlockBottom.setBackground(context.getResources().getDrawable(R.drawable.border_green));
//                }

//                if (degreeMarkSheet.getTag() != null || degreeCertificate.getTag() != null || tenthMarksheet.getTag() != null || twelvethMarksheet.getTag() != null) {//Edu
//                    Drawable bg;
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                        bg = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_check_circle_green, null);
//                        ivEducational.setColorFilter(context.getResources().getColor(R.color.colorGreen), PorterDuff.Mode.MULTIPLY);
//                    } else {
//                        bg = ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green);
//                        DrawableCompat.setTint(bg, context.getResources().getColor(R.color.colorGreen));
//                    }
//                    ivEducational.setImageDrawable(bg);
//                    linEducationBlock.setBackground(context.getResources().getDrawable(R.drawable.border_green));
//                    linEducationDocuments.setBackground(context.getResources().getDrawable(R.drawable.border_green));
//                    linEducationBlockBottom.setBackground(context.getResources().getDrawable(R.drawable.border_green));
//                }

            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

           /* if(aadharCard.getTag() == null)
            {
                // String strFileName1 = aadharCard.getTag().toString().substring(aadharCard.getTag().toString().lastIndexOf("/") + 1);

                    File filepath = new File(Environment.getExternalStorageDirectory().getPath()+"/PDFfiles");

                if (filepath.exists()) {
                    File file=null;
                    File[] files = filepath.listFiles();
                    for (int i = 0; i < files.length; ++i) {
                         file = files[i];
                    }

                    if (file.getName().contains("AadhaarOcr.pdf")) {
                        imageToPdf(documentTypeNo, getString(R.string.upload_adhaar_card), getString(R.string.applicant_adhaar_card_front_and_backside), LoanTabActivity.applicant_id, "1");
                    } else {
                        // do something here with the file
                    }
                }
            //new File(Environment.getExternalStorageDirectory().getPath()+"/PDFfiles").exists()
                   *//* String FileExtn1 = strFileName1.substring(strFileName1.lastIndexOf('.') + 1);

                    if (FileExtn1.equals("pdf")) {
                        openPdf(String.valueOf(aadharCard.getTag()));

                    }*//*
            }*/
            String.valueOf(e.getStackTrace()[0]);

        }
    }

    public static void traverse(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    traverse(file);
                } else {
                    // do something here with the file
                }
            }
        }
    }

    public String copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
//        File dst = new File(destFile, sourceFile.getName());
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
        return String.valueOf(destFile);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1:
                if (grantResults.length <= 0) {
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                    //granted
                } else {
                    //not granted
                    {
                        Snackbar.make(
                                getView().findViewById(R.id.relDocUpload),
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

    /*===============================TILL HERE========================================*/
}
