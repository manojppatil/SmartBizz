package com.eduvanzapplication.Font;

import android.content.Context;
import android.graphics.Typeface;

import android.support.design.widget.TextInputLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;

import java.lang.reflect.Field;

public class InpLayoutLight extends TextInputLayout {

    public InpLayoutLight(Context context) {
        super(context);
        initFont(context);
    }

    public InpLayoutLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont(context);
    }

    private void initFont(Context context) {
        final Typeface typeface = Typeface.createFromAsset(
                context.getAssets(), "fonts/SourceSansPro-Light.ttf");

        EditText editText = getEditText();
        if (editText != null) {
            editText.setTypeface(typeface);
        }
        try {
            // Retrieve the CollapsingTextHelper Field
            final Field cthf = TextInputLayout.class.getDeclaredField("mCollapsingTextHelper");
            cthf.setAccessible(true);

            // Retrieve an instance of CollapsingTextHelper and its TextPaint
            final Object cth = cthf.get(this);
            final Field tpf = cth.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            // Apply your Typeface to the CollapsingTextHelper TextPaint
            ((TextPaint) tpf.get(cth)).setTypeface(typeface);
        } catch (Exception ignored) {
            // Nothing to do
        }
    }
}
