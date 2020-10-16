package com.smartbizz.newUI.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.smartbizz.R;
import com.smartbizz.newUI.newViews.BaseActivity;

public class GalleryCustomHomeToolBar extends LinearLayout implements View.OnClickListener {
    private Context mContext;

    public GalleryCustomHomeToolBar(Context context) {
        super(context);
        init(context, null);
    }

    public GalleryCustomHomeToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GalleryCustomHomeToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_gallery_toolbar, this);

        if (attrs != null) {
            mContext = context;
            boolean hideSupport = false;
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmartBizzCustomToolBar, 0, 0);

            if (a.hasValue(R.styleable.SmartBizzCustomToolBar_hideContactUs)) {
                hideSupport = a.getBoolean(R.styleable.SmartBizzCustomToolBar_hideContactUs, false);
            }

            a.recycle();
        }
    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
//        BaseActivity baseActivity = getActivity();
//        if(baseActivity != null) {
//            baseActivity.callContactUs();
//        }
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

