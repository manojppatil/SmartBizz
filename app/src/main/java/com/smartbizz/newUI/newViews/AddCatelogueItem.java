package com.smartbizz.newUI.newViews;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.smartbizz.R;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.Util.FileUtil;
import com.smartbizz.newUI.network.NetworkManager;

import java.io.IOException;

public class AddCatelogueItem extends BaseActivity {

    private Toolbar toolbar;
    public static Context context;
    public Activity activity;
    public static AppCompatActivity mActivity;
    private ImageView itemimage;
    private EditText itemname, itemprice, itemdesc, itemlink, itemcode;
    Button add;
    private String uploadFilePath = "";
    public int REQUEST_CAMERA = 0, SELECT_FILE = 1, SELECT_DOC = 2;
    public String userChoosenTask;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_catelogue_item);


        context = this;
        this.activity = this;
        mActivity = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.white));
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        itemimage = findViewById(R.id.catalog_itemimage);
        itemimage.setOnClickListener(view -> selectImage());
        itemname = findViewById(R.id.catalog_itemname);
        itemprice = findViewById(R.id.catalog_itemprice);
        itemdesc = findViewById(R.id.catalog_itemdesc);
        itemlink = findViewById(R.id.catalog_itemlink);
        itemcode = findViewById(R.id.catalog_itemcode);
        add = findViewById(R.id.add_catalogitem);
        add.setOnClickListener(view -> {
            saveCatalogData(uploadFilePath);
            finish();
        });
        progressBar = findViewById(R.id.progressBar_addcatalog);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Logo!");
        builder.setItems(items, (dialog, item) -> {
            boolean result = true;

            if (items[item].equals("Choose from Gallery")) {
                userChoosenTask = "Choose from Gallery";
                if (result)
                    galleryIntent();

            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
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

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                Uri selectedFileUri = data.getData();
                itemimage.setImageBitmap(bm);
                uploadFilePath = FileUtil.getPath(activity, data.getData());
//                uploadFilePath = PathFile.getPath(context, selectedFileUri);
                Log.e("TAG", "onSelectFromGalleryResult: " + uploadFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void saveCatalogData(String filePath) {
        if (TextUtils.isEmpty(itemname.getText().toString().trim())) {
            makeToast("Please Enter Item name");
            return;
        }
        if (TextUtils.isEmpty(itemdesc.getText().toString().trim())) {
            makeToast("Please enter Description");
            return;
        }
        DialogUtil.showProgressDialog(activity);
        NetworkManager.getInstance(activity).add_catalog(activity, itemname.getText().toString().trim(), itemprice.getText().toString().trim(), itemdesc.getText().toString().trim(), itemlink.getText().toString().trim(),itemcode.getText().toString().trim(), filePath, response -> {
            DialogUtil.dismissProgressDialog();
            if (response.isSuccess()) {
//                setProfileApiCall();
            } else {
                makeToast(response.getMessage());
            }
        });
    }
}