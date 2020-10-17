package com.smartbizz.newUI.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.smartbizz.R;
import com.smartbizz.newUI.newViews.BaseActivity;

public class SmartBizzCustomToolBar extends LinearLayout implements View.OnClickListener {
    private Context mContext;

    public SmartBizzCustomToolBar(Context context) {
        super(context);
        init(context, null);
    }

    public SmartBizzCustomToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SmartBizzCustomToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_toolbar, this);
        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
//        TextView tvPrimaryText = view.findViewById(R.id.tvPrimaryText);
//        TextView tvSecondaryText = view.findViewById(R.id.tvSecondaryText);
//        TextView tvContactUs = view.findViewById(R.id.tvContactUs);
//        tvContactUs.setOnClickListener(this);

        if (attrs != null) {
            mContext = context;
            String primaryText = "", secondaryText = "";
            boolean hideSupport = false;
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmartBizzCustomToolBar, 0, 0);

            if (a.hasValue(R.styleable.SmartBizzCustomToolBar_primaryText)) {
                primaryText = a.getString(R.styleable.SmartBizzCustomToolBar_primaryText);
            }

            if (a.hasValue(R.styleable.SmartBizzCustomToolBar_secondaryText)) {
                secondaryText = a.getString(R.styleable.SmartBizzCustomToolBar_secondaryText);
            }
            if (a.hasValue(R.styleable.SmartBizzCustomToolBar_hideContactUs)) {
                hideSupport = a.getBoolean(R.styleable.SmartBizzCustomToolBar_hideContactUs, false);
            }

            a.recycle();
//            tvPrimaryText.setText(primaryText);
//            tvSecondaryText.setText(secondaryText);
//            tvContactUs.setVisibility(hideSupport ? GONE : VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        BaseActivity baseActivity = getActivity();
        if(baseActivity != null) {
//            baseActivity.callContactUs();
        }
    }

    private BaseActivity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (BaseActivity) context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}

