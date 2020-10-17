package com.smartbizz.newUI.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.smartbizz.R;
import com.smartbizz.Util.CommonUtil;
import com.smartbizz.Util.Constants;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.newViews.Contact;
import com.smartbizz.newUI.newViews.ContactsPickerActivity;
import com.smartbizz.newUI.view.SenderIdBottomSheet;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class CreateGroupFragment extends Fragment {
    View view;
    private EditText etContactGrpTitle, etContactGrp;
    ImageView btnContactGrpPicker;
    Button btnCreateGrp;
    TextView tvContactGrpPicker;
    final int CONTACT_GRP_REQUEST = 1001;
    protected String[] permissionsStorage = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_group, container, false);

        etContactGrp = view.findViewById(R.id.etContactGrp);
        btnContactGrpPicker = view.findViewById(R.id.btnContactGrpPicker);
        etContactGrpTitle = view.findViewById(R.id.etContactGrpTitle);
        btnCreateGrp = view.findViewById(R.id.btnCreateGrp);
        tvContactGrpPicker = view.findViewById(R.id.tvContactGrpPicker);

        btnContactGrpPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferenceManager.getString(getActivity(), Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(getActivity(), Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
                    callsenderIdBottomSheet();
                    return;
                }
                if (askPermissionStorage()) {
//                    intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                    startActivityForResult(intent, 2);

                    Intent intent = new Intent(getActivity(), ContactsPickerActivity.class);
                    startActivityForResult(intent, CONTACT_GRP_REQUEST);
                }
            }
        });


        btnCreateGrp.setOnClickListener(view -> {
            if (PreferenceManager.getString(getActivity(), Constants.PrefKeys.SENDERID).equalsIgnoreCase("null") || PreferenceManager.getString(getActivity(), Constants.PrefKeys.SENDERID).equalsIgnoreCase("null")) {
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

            DialogUtil.showProgressDialog(getActivity());
            NetworkManager.getInstance(getActivity()).createContactGrp(getActivity(), etContactGrpTitle.getText().toString(), etContactGrp.getTag().toString(), response -> {
                DialogUtil.dismissProgressDialog();
                if (response.isSuccess()) {
                    etContactGrpTitle.setText("");
                    etContactGrp.setText("");
                    makeToast(response.getMessage());
//                    getContactGrp();
                } else {
                    makeToast(response.getMessage());
                }
            });
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    protected void makeToast(String message) {
        CommonUtil.makeToast(getActivity(), message);
    }

    private void callsenderIdBottomSheet() {
        SenderIdBottomSheet senderIdBottomSheet = new SenderIdBottomSheet();
        senderIdBottomSheet.show(getChildFragmentManager(), senderIdBottomSheet.getTag());
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

    public boolean checkPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || (ActivityCompat.checkSelfPermission(getContext(), permission) == PackageManager
                .PERMISSION_GRANTED);
    }

}