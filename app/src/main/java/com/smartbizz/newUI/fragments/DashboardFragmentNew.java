package com.smartbizz.newUI.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartbizz.Util.Constants;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.Util.PermissionUtil;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.newViews.BrandDesigningActivity;
import com.smartbizz.newUI.newViews.CatalogActivity;
import com.smartbizz.newUI.newViews.CatalogueActivity;
import com.smartbizz.newUI.newViews.DashboardActivity;
import com.smartbizz.newUI.newViews.DesignActivity;
import com.smartbizz.newUI.newViews.EditProfile;
import com.smartbizz.newUI.newViews.PostCardTabActivity;
import com.smartbizz.newUI.newViews.SMSTabActivity;
import com.smartbizz.newUI.newViews.SmsTemplateActivity;
import com.smartbizz.newUI.view.NachInfoBottomSheet;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.smartbizz.MainActivity;
import com.smartbizz.newUI.MainApplication;
import com.smartbizz.R;
import com.smartbizz.newUI.SharedPref;
//import com.whatsboinamob.smartbizz.Sender;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragmentNew extends BaseFragment {

    public static Context context;
    public static Fragment mFragment;
    public static String dealID = "", userName = "", userId = "", student_id = "",
            mobile_no = "", email = "", auth_token = "", lead_id = "";
    private LinearLayout linPromotion, linSMS, linProductCatalog, linDesigner, linSender, linVideoDesigner;
    public static ProgressDialog progressDialog;

    MainApplication mainApplication;
    SharedPref sharedPref;

    public static RecyclerView rvLeads;

    public static View view;

    public DashboardFragmentNew() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        view = inflater.inflate(R.layout.fragment_dashboard_fragment_new, container, false);

        try {
            context = getContext();
            sharedPref = new SharedPref();
            mFragment = new DashboardFragmentNew();
            mainApplication = new MainApplication();

            try {
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
                userName = sharedPreferences.getString("first_name", "User");
                userId = sharedPreferences.getString("logged_id", "");
                mobile_no = sharedPreferences.getString("mobile_no", "");
                email = sharedPreferences.getString("email", "");
                student_id = sharedPreferences.getString("student_id", "");
                auth_token = sharedPreferences.getString("auth_token", "");
                lead_id = sharedPreferences.getString("lead_id", "");

            } catch (Exception e) {
                e.printStackTrace();
            }

            MainActivity.auth_token = auth_token;

            linPromotion = view.findViewById(R.id.linPromotion);
            linSMS = view.findViewById(R.id.linSMS);
            linProductCatalog = view.findViewById(R.id.linProductCatalog);
            linDesigner = view.findViewById(R.id.linDesigner);
            linSender = view.findViewById(R.id.linSender);
            linVideoDesigner = view.findViewById(R.id.linVideoDesigner);
            progressDialog = new ProgressDialog(getActivity());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rvLeads.setLayoutManager(linearLayoutManager);
            rvLeads.setNestedScrollingEnabled(false);

        } catch (Exception e) {

        }

        return view;
    }//-----------------------------------END OF ON CREATE----------------------------------------//


    View.OnClickListener PromotionClkListnr = v -> {
        if (askRequiredPermissions()) {
            startActivity(new Intent(getActivity(), PostCardTabActivity.class));
        }
    };

    View.OnClickListener SmsClkListnr = v -> {
        if (askRequiredPermissions()) {
            startActivity(new Intent(activity, SMSTabActivity.class));
        }
//        callNachIno();
    };

    View.OnClickListener SenderClkListnr = v -> {
        if (askRequiredPermissions()) {
            startActivity(new Intent(activity, SmsTemplateActivity.class));
        }

    };

    View.OnClickListener designerClkListnr = v -> {
        callNachIno();
    };

    View.OnClickListener videoClkListnr = v -> {
        callNachIno();
    };

    View.OnClickListener catalogClkListnr = v -> {

        if (askRequiredPermissions()) {
            startActivity(new Intent(activity, CatalogueActivity.class));
        }
//        callNachIno();
    };

    protected boolean askRequiredPermissions() {
        boolean isAllPermissionsGranted = true;
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            isAllPermissionsGranted = false;
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_CONTACTS},
                    Constants.RequestCode.WRITE_EXTERNAL_STORAGE);
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
//                startActivity(new Intent(getActivity(), PostCardActivity.class));
//                startActivity(new Intent(getActivity(), PostCardTabActivity.class));
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        linBannerDesign.setOnClickListener(BannerDesignClkListnr);
//        linLogoDesign.setOnClickListener(LogoDesignClkListnr);
        linPromotion.setOnClickListener(PromotionClkListnr);
        linSMS.setOnClickListener(SmsClkListnr);
        linSender.setOnClickListener(SenderClkListnr);
        linDesigner.setOnClickListener(designerClkListnr);
        linVideoDesigner.setOnClickListener(videoClkListnr);
        linProductCatalog.setOnClickListener(catalogClkListnr);

        onResume();

        String brandName = PreferenceManager.getString(context, Constants.PrefKeys.BRANDNAME);
        String logo = PreferenceManager.getString(context, Constants.PrefKeys.LOGO);

        if (brandName.equalsIgnoreCase("null") || logo.equalsIgnoreCase("null") || brandName.equalsIgnoreCase("") || logo.equalsIgnoreCase("")) {
            startActivity(new Intent(activity, EditProfile.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void callNachIno() {
        NachInfoBottomSheet nachInfoBottomSheet = new NachInfoBottomSheet();
        nachInfoBottomSheet.show(getChildFragmentManager(), nachInfoBottomSheet.getTag());
    }
}
