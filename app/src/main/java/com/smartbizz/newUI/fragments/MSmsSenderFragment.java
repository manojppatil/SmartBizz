package com.smartbizz.newUI.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;

import com.smartbizz.R;
import com.smartbizz.Util.CommonUtil;
import com.smartbizz.Util.Constants;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.Util.FileUtil;
import com.smartbizz.Util.GenericTextWatcher;
import com.smartbizz.Util.PermissionUtil;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.newUI.adapter.SpinnerAdapter;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.newViews.Contact;
import com.smartbizz.newUI.newViews.ContactsPickerActivity;
import com.smartbizz.newUI.newViews.SmsTemplateActivity;
import com.smartbizz.newUI.pojo.Category;
import com.smartbizz.newUI.pojo.ContactGroup;
import com.smartbizz.newUI.view.SMSSuccessBottomSheet;
import com.smartbizz.newUI.view.SenderIdBottomSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MSmsSenderFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    View view;
    final int CONTACT_PICK_REQUEST = 1000;
    final int CONTACT_GRP_REQUEST = 1001;
    public static final int REQUEST = 112;
    private EditText etMesage, etContactGrpTitle, etContacts, etContactGrp;
    private TextView txtNoData, txtCharCount;
    private TextView btnContactPicker;
    private Button btnSubmit, btnSchedulelater, btnCreateGrp;
    private String contactgroupId = null;
    AppCompatSpinner sender_id;
    LinearLayout layout_templates;
    TextView btnFilePicker;
    public int SELECT_FILE = 1;
    private String uploadFilePath = "";
    private AppCompatSpinner spinnerContactGrp;
    private int mYear, mMonth, mDay, mHour, mMinute;
    HashMap scheduledata = new HashMap();

    protected String[] permissionsStorage = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_m_sms_sender, container, false);
        etMesage = view.findViewById(R.id.etMesage);
        txtCharCount = view.findViewById(R.id.txtCharCount);
        txtNoData = view.findViewById(R.id.txtNoData);
        etContacts = view.findViewById(R.id.etContacts);
        btnContactPicker = view.findViewById(R.id.btnContactPicker);
        btnFilePicker = view.findViewById(R.id.btnFilePicker);

        spinnerContactGrp = view.findViewById(R.id.spinnerContactGrp);

        etContactGrp = view.findViewById(R.id.etContactGrp);
//        btnContactGrpPicker = view.findViewById(R.id.btnContactGrpPicker);
        etContactGrpTitle = view.findViewById(R.id.etContactGrpTitle);
        sender_id = view.findViewById(R.id.ed_senderid);

        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSchedulelater = view.findViewById(R.id.schedule_later);
        layout_templates = view.findViewById(R.id.layout_template);
        layout_templates.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), SmsTemplateActivity.class);
            startActivity(i);
            getActivity().overridePendingTransition(0, 0);
        });

        String strtext = getArguments().getString("msg");
        etMesage.setText(strtext);

        btnContactPicker.setOnClickListener(MSmsSenderFragment.this);
//        btnContactGrpPicker.setOnClickListener(this);
        btnSchedulelater.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnFilePicker.setOnClickListener(this);
        RegisterListner();
        spinnerContactGrp.setOnItemSelectedListener(this);
        getContactGrp();
        sender_id.setOnItemSelectedListener(this);
        getSenderId();

//        if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
//        } else {
//            sender_id.setAdapter(PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID));
//        }

        return view;
    }

    private void RegisterListner() {
        TextWatcher pinCodeTextWatcher = new GenericTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                int msgcount = 1;
                if (s.length() >= 150) {
                    txtCharCount.setText(count + "");
//                    makeToast("You have reached max character ");
//                    CommonUtil.hideKeyboard(activity, etMesage);
                    msgcount++;
                }
                txtCharCount.setText(s.length() + "/" + msgcount + "");
            }
        };

        etMesage.addTextChangedListener(pinCodeTextWatcher);
    }

    public static SMSSenderFragment addfrag(int val, Category category) {
        SMSSenderFragment fragment = new SMSSenderFragment();
        return fragment;
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

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }

    }

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


            case R.id.btnSubmit:
                if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                    return;
                }
                if (TextUtils.isEmpty(etMesage.getText().toString())) {
                    makeToast("Please add SMS Content");
                    return;
                }
                if (etMesage != null && etMesage != null) {

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
                } else if (uploadFilePath != null) {
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
                }
            case R.id.btnFilePicker:
                if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                    return;
                }
                galleryIntent();
                break;

            case R.id.schedule_later:
                if (PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(activity, Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                }
                calldatepicker();
                return;
        }

    }

    private void calldatepicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String schd_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        scheduledata.put("schedule_date", schd_date);
                        calltimepicker();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void calltimepicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

//                        txtTime.setText(hourOfDay + ":" + minute);
                        String schd_time = hourOfDay + ":" + minute;
                        scheduledata.put("schedule_time", schd_time);
                        scheduleconfirm();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void scheduleconfirm() {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.confirmschedule_dialog);

        TextView senderid = (TextView) dialog.findViewById(R.id.sch_senderid);
        senderid.setText(sender_id.getSelectedItem().toString());
        TextView message = (TextView) dialog.findViewById(R.id.sch_msg);
        message.setText(etMesage.getText().toString());
        TextView schdate = (TextView) dialog.findViewById(R.id.sch_date);
        schdate.setText(scheduledata.get("schedule_date").toString());
        TextView schtime = (TextView) dialog.findViewById(R.id.sch_time);
        schtime.setText(scheduledata.get("schedule_time").toString());

        Button okButton = (Button) dialog.findViewById(R.id.confirm_btn);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Scheduling data...", Toast.LENGTH_SHORT).show();
            }
        });


        dialog.show();
    }


    private void galleryIntent() {
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

    private void getSenderId() {
        DialogUtil.showProgressDialog(activity);
        NetworkManager.getInstance(activity).getSenderId(activity, response -> {
            DialogUtil.dismissProgressDialog();
            if (response.isSuccess()) {
                JSONObject jsonObject = response.getResponse();
                if (jsonObject != null) {
                    JSONArray results = jsonObject.optJSONArray(ApiConstants.Keys.RESULT);
                    if (results != null) {
                        List<String> senderidlist = new ArrayList<>();
//                        senderidlist.add(new String("-1", "Select Group"));
                        int size = results.length();
                        int selectedPosition = 0;
                        for (int i = 0; i < size; i++) {
                            String senderid = null;
                            try {
                                senderid = String.valueOf(results.optJSONObject(i).get("sender_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            ContactGroup loanTypes = new ContactGroup(results.optJSONObject(i));
                            senderidlist.add(senderid);
//                            if (!TextUtils.isEmpty(loanTypeId) && loanTypes.getId().equalsIgnoreCase(loanTypeId)) {
//                                selectedPosition = i+1;
//                            }
                        }
                        SpinnerAdapter groupSpinnerAdapter = new SpinnerAdapter<>(activity, R.layout.spinner_item, senderidlist);
                        groupSpinnerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
                        sender_id.setAdapter(groupSpinnerAdapter);
                        sender_id.setSelection(selectedPosition);
                    }
                }

            } else {
                makeToast(response.getMessage());
            }
        });
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
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