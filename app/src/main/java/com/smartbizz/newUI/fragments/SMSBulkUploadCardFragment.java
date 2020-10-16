package com.smartbizz.newUI.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.smartbizz.R;
import com.smartbizz.Util.CommonUtil;
import com.smartbizz.Util.Constants;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.Util.FileUtil;
import com.smartbizz.Util.GenericTextWatcher;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.Util.VerticalLineDecorator;
import com.smartbizz.newUI.adapter.PostCardAdapter;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.newViews.PostCardTabActivity;
import com.smartbizz.newUI.pojo.Category;
import com.smartbizz.newUI.pojo.Requests;
import com.smartbizz.newUI.view.SMSSuccessBottomSheet;
import com.smartbizz.newUI.view.SenderIdBottomSheet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SMSBulkUploadCardFragment extends BaseFragment implements View.OnClickListener {
    View view;
    String val;
    public static final int REQUEST = 112;
    public int SELECT_FILE = 1;
    private String uploadFilePath = "";
    public String userChoosenTask;

    private EditText etMesage;
    private TextView txtNoData, txtCharCount;
    private Button btnFilePicker, btnSubmit, btnReset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bulk_upload, container, false);
        etMesage = view.findViewById(R.id.etMesage);
        txtNoData = view.findViewById(R.id.txtNoData);
        btnFilePicker = view.findViewById(R.id.btnFilePicker);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnReset = view.findViewById(R.id.btnReset);
        txtCharCount = view.findViewById(R.id.txtCharCount);

        btnFilePicker.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        RegisterListner();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        new AlertDialog.Builder(activity)
//                .setMessage("SMS Sent Successfully")
//                .setCancelable(true)
//                .setIcon(R.drawable.success)
//                .show();

    }

    private void RegisterListner() {


        TextWatcher pinCodeTextWatcher = new GenericTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (s.length() == 150) {
                    makeToast("You have reached max character");
                    CommonUtil.hideKeyboard(activity, etMesage);
                }
                txtCharCount.setText("Char count: " + s.length());
            }
        };

        etMesage.addTextChangedListener(pinCodeTextWatcher);

    }

    private void galleryIntent() {
//        Intent intent = new Intent();
//        intent.setType("Application/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv|application/*");
//        intent.setType("text/csv|application/excel|application/x-msexcel|application/x-excel");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"text/*", "application/*"};
//          String[] mimeTypes = {"text/csv", "application/excel", "application/x-msexcel", "application/x-excel"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {

            try {
                uploadFilePath = FileUtil.getPath(activity, data.getData());

                if (FileUtil.isValidFileExtension(activity, uploadFilePath)) {

                } else {
                    uploadFilePath = "";
                }
//                uploadFilePath = PathFile.getPath(context, selectedFileUri);
                Log.e("TAG", "onSelectFromGalleryResult: " + uploadFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnFilePicker:
                if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                    return;
                }
                galleryIntent();
                break;

            case R.id.btnReset:
                if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                    return;
                }
                etMesage.setText("");
                uploadFilePath = "";
                break;

            case R.id.btnSubmit:
                if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                    return;
                }
                DialogUtil.showProgressDialog(activity);
                NetworkManager.getInstance(activity).putSMSBulkUpload(activity, etMesage.getText().toString(), uploadFilePath, response -> {
                    DialogUtil.dismissProgressDialog();
                    if (response.isSuccess()) {
                        etMesage.setText("");
                        uploadFilePath = "";
                        makeToast(response.getMessage());
                        callSMSSuccess();
                    } else {
                        makeToast(response.getMessage());
                    }
                });

                break;
        }
    }

    private void callsenderIdBottomSheet() {
        SenderIdBottomSheet senderIdBottomSheet = new SenderIdBottomSheet();
        senderIdBottomSheet.show(getChildFragmentManager(), senderIdBottomSheet.getTag());
    }

    private void callSMSSuccess() {
        SMSSuccessBottomSheet senderIdBottomSheet = new SMSSuccessBottomSheet();
        senderIdBottomSheet.show(getChildFragmentManager(), senderIdBottomSheet.getTag());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                } else {
                    Toast.makeText(activity, "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}