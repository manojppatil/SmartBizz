package com.smartbizz.newUI.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.smartbizz.R;
import com.smartbizz.Util.CommonUtil;
import com.smartbizz.Util.Constants;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.Util.GenericTextWatcher;
import com.smartbizz.Util.PermissionUtil;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.Util.VerticalLineDecorator;
import com.smartbizz.newUI.adapter.PostCardAdapter;
import com.smartbizz.newUI.adapter.SpinnerAdapter;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.newViews.Contact;
import com.smartbizz.newUI.newViews.ContactsPickerActivity;
import com.smartbizz.newUI.newViews.PostCardTabActivity;
import com.smartbizz.newUI.pojo.Category;
import com.smartbizz.newUI.pojo.ContactGroup;
import com.smartbizz.newUI.pojo.MCategory;
import com.smartbizz.newUI.pojo.Requests;
import com.smartbizz.newUI.view.NachInfoBottomSheet;
import com.smartbizz.newUI.view.SMSSuccessBottomSheet;
import com.smartbizz.newUI.view.SenderIdBottomSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SMSSenderFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    View view;
    final int CONTACT_PICK_REQUEST = 1000;
    final int CONTACT_GRP_REQUEST = 1001;
    public static final int REQUEST = 112;
    private EditText etMesage, etContactGrpTitle, etContacts, etContactGrp;
    private TextView txtNoData, txtCharCount;
    private ImageView btnContactPicker, btnContactGrpPicker;
    private Button btnSubmit, btnReset, btnCreateGrp;
    private AppCompatSpinner spinnerContactGrp;
    private String contactgroupId = null;

    protected String[] permissionsStorage = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sms_sender, container, false);
        etMesage = view.findViewById(R.id.etMesage);
        txtCharCount = view.findViewById(R.id.txtCharCount);
        txtNoData = view.findViewById(R.id.txtNoData);
        etContacts = view.findViewById(R.id.etContacts);
        btnContactPicker = view.findViewById(R.id.btnContactPicker);

        etContactGrp = view.findViewById(R.id.etContactGrp);
        btnContactGrpPicker = view.findViewById(R.id.btnContactGrpPicker);
        etContactGrpTitle = view.findViewById(R.id.etContactGrpTitle);

        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnReset = view.findViewById(R.id.btnReset);
        btnCreateGrp = view.findViewById(R.id.btnCreateGrp);

        spinnerContactGrp = view.findViewById(R.id.spinnerContactGrp);

        btnContactPicker.setOnClickListener(this);
        btnContactGrpPicker.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnCreateGrp.setOnClickListener(this);
        RegisterListner();
        spinnerContactGrp.setOnItemSelectedListener(this);
        getContactGrp();
        return view;
    }

    public static SMSSenderFragment addfrag(int val, Category category) {
        SMSSenderFragment fragment = new SMSSenderFragment();
        return fragment;
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

    private void callsenderIdBottomSheet() {
        SenderIdBottomSheet senderIdBottomSheet = new SenderIdBottomSheet();
        senderIdBottomSheet.show(getChildFragmentManager(), senderIdBottomSheet.getTag());
    }

    private void callSMSSuccess() {
        SMSSuccessBottomSheet senderIdBottomSheet = new SMSSuccessBottomSheet();
        senderIdBottomSheet.show(getChildFragmentManager(), senderIdBottomSheet.getTag());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_OK) {

            ArrayList<Contact> selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");

            String display = "", mobile = "", name = "";
            for (int i = 0; i < selectedContacts.size(); i++) {

                display += (i + 1) + ". " + selectedContacts.get(i).toString() + "\n";
                if (i == 0) {
                    name += selectedContacts.get(i).name;
                    mobile += selectedContacts.get(i).phone;
                } else {
                    name += "," + selectedContacts.get(i).name;
                    mobile += "," + selectedContacts.get(i).phone;
                }
            }

            etContacts.setText(name);
            etContacts.setTag(mobile);

//            if (requestCode == 1) {
//                etContacts.setText(mobile);
////                if (!etContacts.getText().toString().trim().equalsIgnoreCase("")) {
////                    String names = etContacts.getText().toString().trim() + "," + phoneNumber1;
////                    etContacts.setText(names);
////                } else {
////                    etContacts.setText(phoneNumber1);
////                }
//            } else {
//                etContactGrp.setText(mobile);
////                if (!etContactGrp.getText().toString().trim().equalsIgnoreCase("")) {
////                    String names = etContactGrp.getText().toString().trim() + "," + phoneNumber1;
////                    etContactGrp.setText(names);
////                } else {
////                    etContactGrp.setText(phoneNumber1);
////                }
//            }

        }
        if (requestCode == CONTACT_GRP_REQUEST && resultCode == RESULT_OK) {

            ArrayList<Contact> selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");

            String display = "", mobile = "", name = "";
            for (int i = 0; i < selectedContacts.size(); i++) {

                display += (i + 1) + ". " + selectedContacts.get(i).toString() + "\n";
                if (i == 0) {
                    name += selectedContacts.get(i).name;
                    mobile += selectedContacts.get(i).phone;
                } else {
                    name += "," + selectedContacts.get(i).name;
                    mobile += "," + selectedContacts.get(i).phone;
                }
            }
//            etContacts.setText("Selected Contacts : \n\n"+display);

            etContactGrp.setText(name);
            etContactGrp.setTag(mobile);

        }

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            Uri contactData = data.getData();
//            Cursor c = activity.getContentResolver().query(contactData, null, null, null, null);
//            if (c.moveToFirst()) {
//
//                String phoneNumber1 = "", phoneNumber2 = "", emailAddress = "", companyName = "";
//                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
//
//                String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//
//                if (hasPhone.equalsIgnoreCase("1"))
//                    hasPhone = "true";
//                else
//                    hasPhone = "false";
//                //9691971102
//                if (Boolean.parseBoolean(hasPhone)) {
//                    Cursor phones = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//                    while (phones.moveToNext()) {
//                        if (!phoneNumber1.equalsIgnoreCase("") && !phoneNumber1.equals(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", ""))) {
////                        if (!phoneNumber1.equals(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))) {
//                            phoneNumber2 = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
//                        } else {
//                            phoneNumber1 = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
//                        }
//                    }
//                    phones.close();
//                }
//
//                // Find Email Addresses
//                Cursor emails = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
//                while (emails.moveToNext()) {
//                    emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//                }
//                emails.close();
//
//                // Find Company
//                Cursor cursor = activity.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
//                        null, ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?", new String[]{contactId,
//                                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE}, null);
//
//                if (cursor != null) {
//                    if (cursor.moveToFirst()) {
//                        companyName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
//                    }
//                }
//                cursor.close();
//
//                if (requestCode == 1) {
//                    if (!etContacts.getText().toString().trim().equalsIgnoreCase("")) {
//                        String names = etContacts.getText().toString().trim() + "," + phoneNumber1;
//                        etContacts.setText(names);
//                    } else {
//                        etContacts.setText(phoneNumber1);
//                    }
//                } else {
//                    if (!etContactGrp.getText().toString().trim().equalsIgnoreCase("")) {
//                        String names = etContactGrp.getText().toString().trim() + "," + phoneNumber1;
//                        etContactGrp.setText(names);
//                    } else {
//                        etContactGrp.setText(phoneNumber1);
//                    }
//                }
//
//            }
//            c.close();
//        }
//
//    }

    public boolean checkPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || (ActivityCompat.checkSelfPermission(getContext(), permission) == PackageManager
                .PERMISSION_GRANTED);
    }

    protected boolean askPermissionStorage() {

        boolean isAllPermissionsGranted = true;

        for (String p : permissionsStorage) {
            isAllPermissionsGranted &= checkPermission(p);
            if (!isAllPermissionsGranted)
                break;
        }

        if (!isAllPermissionsGranted) {

            requestPermissions(permissionsStorage, Constants.RequestCode.CHECK_ALL_PERMISSIONS);
        }
        return isAllPermissionsGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {

            boolean allPermissionsSuccess = true;
            final List<String> failedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsSuccess = false;
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                        failedPermissions.add(permissions[i]);
                    }
                }
            }

            if (!failedPermissions.isEmpty()) {
                PermissionUtil.requestPermissions(activity,
                        PermissionUtil.listToArray(failedPermissions),
                        Constants.RequestCode.CHECK_ALL_PERMISSIONS);
            } else if (!allPermissionsSuccess) {
                DialogUtil.showSettingsDialog(activity, "Please enable location permission to proceed further!");
            } else {
                //Handle success case
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnContactPicker:

                if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                    return;
                }

                if (askPermissionStorage()) {
//                    intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                    startActivityForResult(intent, 1);

                    intent = new Intent(activity, ContactsPickerActivity.class);
                    startActivityForResult(intent, CONTACT_PICK_REQUEST);
                }
                break;

            case R.id.btnContactGrpPicker:
                if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                    return;
                }
                if (askPermissionStorage()) {
//                    intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                    startActivityForResult(intent, 2);

                    intent = new Intent(activity, ContactsPickerActivity.class);
                    startActivityForResult(intent, CONTACT_GRP_REQUEST);
                }
                break;

            case R.id.btnReset:
                if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                    return;
                }
                etMesage.setText("");
                etContacts.setText("");
                break;

            case R.id.btnCreateGrp:
                if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                    return;
                }
                if (TextUtils.isEmpty(etContactGrpTitle.getText().toString())) {
                    makeToast("Please add Group Name");
                    return;
                }
                if (TextUtils.isEmpty(etContactGrp.getText().toString())) {
                    makeToast("Please add Contacts");
                    return;
                }

                DialogUtil.showProgressDialog(activity);
                NetworkManager.getInstance(activity).createContactGrp(activity, etContactGrpTitle.getText().toString(), etContactGrp.getTag().toString(), response -> {
                    DialogUtil.dismissProgressDialog();
                    if (response.isSuccess()) {
                        etContactGrpTitle.setText("");
                        etContactGrp.setText("");
                        makeToast(response.getMessage());
                        getContactGrp();
                    } else {
                        makeToast(response.getMessage());
                    }
                });

                break;

            case R.id.btnSubmit:
                if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                    return;
                }
                if (TextUtils.isEmpty(etMesage.getText().toString())) {
                    makeToast("Please add SMS Content");
                    return;
                }

                if (contactgroupId == null) {

                    if (TextUtils.isEmpty(etContacts.getTag().toString())) {
                        makeToast("Please add Contacts");
                        return;
                    }

                    DialogUtil.showProgressDialog(activity);
                    NetworkManager.getInstance(activity).putSMSContactList(activity, etMesage.getText().toString(), etContacts.getTag().toString(), response -> {
                        DialogUtil.dismissProgressDialog();
                        if (response.isSuccess()) {
                            etMesage.setText("");
                            etContacts.setText("");
                            makeToast(response.getMessage());
                            callSMSSuccess();
                        } else {
                            makeToast(response.getMessage());
                        }
                    });
                } else {

                    DialogUtil.showProgressDialog(activity);
                    NetworkManager.getInstance(activity).putSMSContactGrp(activity, etMesage.getText().toString(), contactgroupId, response -> {
                        DialogUtil.dismissProgressDialog();
                        if (response.isSuccess()) {
                            etMesage.setText("");
                            spinnerContactGrp.setSelection(0);
                            makeToast(response.getMessage());
                        } else {
                            makeToast(response.getMessage());
                        }
                    });
                }

                break;

        }

    }

    private void getContactGrp() {
        DialogUtil.showProgressDialog(activity);
        NetworkManager.getInstance(activity).getContactGrp(activity, response -> {
            DialogUtil.dismissProgressDialog();
            if (response.isSuccess()) {
                JSONObject jsonObject = response.getResponse();
                if (jsonObject != null) {
                    JSONArray results = jsonObject.optJSONArray(ApiConstants.Keys.RESULT);
                    if (results != null) {
                        List<ContactGroup> loanTypeList = new ArrayList<>();
                        loanTypeList.add(new ContactGroup("-1", "Select Group"));
                        int size = results.length();
                        int selectedPosition = 0;
                        for (int i = 0; i < size; i++) {
                            ContactGroup loanTypes = new ContactGroup(results.optJSONObject(i));
                            loanTypeList.add(loanTypes);
//                            if (!TextUtils.isEmpty(loanTypeId) && loanTypes.getId().equalsIgnoreCase(loanTypeId)) {
//                                selectedPosition = i+1;
//                            }
                        }
                        setLoanTypeAdapter(loanTypeList);
                        spinnerContactGrp.setSelection(selectedPosition);
                    }
                }

            } else {
                makeToast(response.getMessage());
            }
        });
    }

    private void setLoanTypeAdapter(List<ContactGroup> list) {
        SpinnerAdapter groupSpinnerAdapter = new SpinnerAdapter<>(activity, R.layout.spinner_item, list);
        groupSpinnerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        spinnerContactGrp.setAdapter(groupSpinnerAdapter);
        spinnerContactGrp.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position != 0) {
            if (parent.getId() == R.id.spinnerContactGrp) {
                contactgroupId = ((ContactGroup) parent.getSelectedItem()).getId();
                etContacts.setText("");
                btnContactPicker.setClickable(false);
            }
        }
        if (position == 0) {
            contactgroupId = null;
            btnContactPicker.setClickable(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}