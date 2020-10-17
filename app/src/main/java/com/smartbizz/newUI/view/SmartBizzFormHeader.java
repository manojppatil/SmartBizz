package com.smartbizz.newUI.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.smartbizz.R;

public class SmartBizzFormHeader extends LinearLayout {
    private TextView tvTitle;
    private View viewIndicator;

    public SmartBizzFormHeader(Context context) {
        super(context);
        init(context, null);
    }

    public SmartBizzFormHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_form_header, this);
        tvTitle = view.findViewById(R.id.tvTitle);
        viewIndicator = view.findViewById(R.id.viewIndicator);

        if (attrs != null) {
            String title = "";
            int resource = R.drawable.left_right_corner_selector;
            boolean hideIndicator=false;
            boolean isAcive = false;
            int gravity;
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmartBizzFormHeader, 0, 0);

            if (a.hasValue(R.styleable.SmartBizzFormHeader_title)) {
                title = a.getString(R.styleable.SmartBizzFormHeader_title);
            }
            if (a.hasValue(R.styleable.SmartBizzFormHeader_resource)) {
                resource = a.getResourceId(R.styleable.SmartBizzFormHeader_resource, R.drawable.left_right_corner_selector);
            }
            if (a.hasValue(R.styleable.SmartBizzFormHeader_hideIndicator)) {
                hideIndicator = a.getBoolean(R.styleable.SmartBizzFormHeader_hideIndicator, false);
            }
            if (a.hasValue(R.styleable.SmartBizzFormHeader_isActive)) {
                isAcive = a.getBoolean(R.styleable.SmartBizzFormHeader_isActive, false);
            }

            if (a.hasValue(R.styleable.SmartBizzFormHeader_customGravity)) {
                gravity = a.getInt(R.styleable.SmartBizzFormHeader_customGravity, 0);
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
                switch (gravity) {
                    case 0:
                        params.gravity = Gravity.LEFT;
                        break;
                    case 1:
                        params.gravity = Gravity.RIGHT;
                        break;
                    case 2:
                        params.gravity = Gravity.CENTER_HORIZONTAL;
                        break;
                    case 4:
                        params.gravity = Gravity.CENTER_VERTICAL;
                        break;
                }

                tvTitle.setLayoutParams(params);
            }


            a.recycle();
            tvTitle.setText(title);
            viewIndicator.setBackgroundResource(resource);
            if(hideIndicator){
                viewIndicator.setVisibility(GONE);
            }
            updateStatus(isAcive);
        }
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void updateStatus(boolean isActive) {
        if (isActive) {
            tvTitle.setTextColor(Color.WHITE);
            updateViewIndicator(Color.WHITE);
        } else {
            tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSectionSmallText));
            updateViewIndicator(ContextCompat.getColor(getContext(), R.color.colorSectionSmallText));
        }
    }

    protected void updateViewIndicator(int color) {
        GradientDrawable gradientDrawable = (GradientDrawable) viewIndicator.getBackground();
        gradientDrawable.setColor(color);
    }

}
