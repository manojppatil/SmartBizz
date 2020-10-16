package com.smartbizz.newUI.newViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartbizz.R;

public class CatalogueActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public static Context context;
    public Activity activity;
    RecyclerView catalog_recycler;
    ImageView catalog_img;
    TextView catalog_title;
    FloatingActionButton add_catalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        context = this;
        this.activity = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Catalog");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.white));
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));


        catalog_recycler = findViewById(R.id.catalog_recycler);
        catalog_img = findViewById(R.id.catalogimage);
        catalog_title = findViewById(R.id.catalog_title);
        add_catalog = findViewById(R.id.add_catalog);
        add_catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogueActivity.this, AddCatelogueItem.class);
                startActivity(intent);
            }
        });
    }
}