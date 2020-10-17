package com.smartbizz.newUI.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.smartbizz.R;


public class SmartBizzToolBarTitleSubTitle extends LinearLayout{
    private TextView tvSecondaryText;

    public SmartBizzToolBarTitleSubTitle(Context context) {
        super(context);
        init(context, null);
    }

    public SmartBizzToolBarTitleSubTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SmartBizzToolBarTitleSubTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.toolbar_title_sub_title, this);
        TextView tvPrimaryText = view.findViewById(R.id.tvPrimaryText);
        tvSecondaryText = view.findViewById(R.id.tvSecondaryText);
        RelativeLayout toolbarContainer = view.findViewById(R.id.toolbarContainer);

        if (attrs != null) {
            String primaryText = "", secondaryText = "";
            int  backgroundColor = ContextCompat.getColor(context,R.color.colorPrimary);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmartBizzToolBarTitleSubTitle, 0, 0);

            if (a.hasValue(R.styleable.SmartBizzToolBarTitleSubTitle_primaryText)) {
                primaryText = a.getString(R.styleable.SmartBizzToolBarTitleSubTitle_primaryText);
            }

            if (a.hasValue(R.styleable.SmartBizzToolBarTitleSubTitle_secondaryText)) {
                secondaryText = a.getString(R.styleable.SmartBizzToolBarTitleSubTitle_secondaryText);
            }

            if (a.hasValue(R.styleable.SmartBizzToolBarTitleSubTitle_layoutBackgroundColor)) {
                backgroundColor= a.getResourceId(R.styleable.SmartBizzToolBarTitleSubTitle_layoutBackgroundColor, ContextCompat.getColor(context,R.color.colorPrimary));
            }

            if(!TextUtils.isEmpty(primaryText)) {
                tvPrimaryText.setText(primaryText);
            }else{
                tvPrimaryText.setVisibility(GONE);
            }
            if(!TextUtils.isEmpty(secondaryText)) {
                tvSecondaryText.setText(secondaryText);
            }else{
                tvSecondaryText.setVisibility(GONE);
            }
            toolbarContainer.setBackgroundColor(backgroundColor);
            a.recycle();
        }
    }

    public void setSecondaryText(String text){
        tvSecondaryText.setText(text);
    }
}

