package com.smartbizz.newUI.newViews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.smartbizz.Util.Constants;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.R;
import com.smartbizz.newUI.adapter.CategoryTypeAdapter;
import com.smartbizz.newUI.fragments.PostCardFragment;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.pojo.CategoryType;
import com.smartbizz.newUI.pojo.MBeats;
import com.smartbizz.newUI.view.SmartBizzCustomToolBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.smartbizz.newUI.newViews.EditImageActivity.FILE_PROVIDER_AUTHORITY;

public class PostCardActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private Toolbar toolbar;
    public static Context context;
    public Activity activity;
    AppCompatActivity mActivity;
    public Fragment fragment = null;

    public RecyclerView recyclerView;

    Spinner spinnerCategory;
    public static Bitmap bitmapLogo = null;
    public String loanTypeId = Constants.EMPTY;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard);

        context = this;
        mActivity = this;
        this.activity = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar = findViewById(R.id.toolbar);

        spinnerCategory = findViewById(R.id.spinnerCategory);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Share Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.white));
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        spinnerCategory.setOnItemSelectedListener(this);
        getCategory();

//        fragment = PostCardFragment.newInstance();
//        replaceFragment(fragment);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.frameLayoutbeat, destFragment);
        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PostCardActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public Uri buildFileProviderUri(@NonNull Uri uri) {
        return FileProvider.getUriForFile(PostCardActivity.this,
                FILE_PROVIDER_AUTHORITY,
                new File(uri.getPath()));
    }

    private void getCategory() {
        DialogUtil.showProgressDialog(activity);
        NetworkManager.getInstance(activity).getCategory(activity, response -> {
            DialogUtil.dismissProgressDialog();
            if (response.isSuccess()) {
                JSONObject jsonObject = response.getResponse();
                if (jsonObject != null) {
                    JSONObject results = jsonObject.optJSONObject(ApiConstants.Keys.RESULT);

                    JSONArray category = results.optJSONArray("category");
                    if (category != null) {
                        List<CategoryType> loanTypeList = new ArrayList<>();
                        loanTypeList.add(new CategoryType("-1", "Select Category"));
                        int size = category.length();
                        int selectedPosition = 0;
                        for (int i = 0; i < size; i++) {
                            CategoryType loanTypes = new CategoryType(category.optJSONObject(i));
                            loanTypeList.add(loanTypes);
                            if (!TextUtils.isEmpty(loanTypeId) && loanTypes.getId().equalsIgnoreCase(loanTypeId)) {
                                selectedPosition = i+1;
                            }
                        }
                        setLoanTypeAdapter(loanTypeList);
                        spinnerCategory.setSelection(selectedPosition);
                    }
                }

            } else {
                makeToast(response.getMessage());
            }
        });
    }

    private void setLoanTypeAdapter(List<CategoryType> list) {
        //Rented/Owned Adapter
        CategoryTypeAdapter loanTypeSpinnerAdapter = new CategoryTypeAdapter<>(activity, R.layout.spinner_item, list);
        loanTypeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        spinnerCategory.setAdapter(loanTypeSpinnerAdapter);
        spinnerCategory.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        if (position != 0) {
            if (parent.getId() == R.id.spinnerCategory) {
//            loanType = String.valueOf(position);
                loanTypeId = ((CategoryType) parent.getSelectedItem()).getId();
            }
        }

        fragment = PostCardFragment.newInstance(loanTypeId);
        replaceFragment(fragment);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
