package com.smartbizz.newUI.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class SmartBizzButton extends AppCompatButton {
    public SmartBizzButton(Context context) {
        super(context);
    }

    public SmartBizzButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDisable(){
        setAlpha(0.5f);
        setEnabled(false);
    }

    public void setEnable(){
        setAlpha(1f);
        setEnabled(true);
    }
}
