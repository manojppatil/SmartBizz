package com.smartbizz.newUI.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.smartbizz.Util.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.reflect.Field;

public class BaseBottomSheet extends BottomSheetDialogFragment {
    protected Dialog dialog;
    protected Activity activity;

    protected void disableDragging(BottomSheetDialog bottomSheetDialog) {
        try {
            activity = getActivity();
            Field behaviorField = bottomSheetDialog.getClass().getDeclaredField("behavior");
            behaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(bottomSheetDialog);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        } catch (NoSuchFieldException e) {
//            MyLogger.error(e);
        } catch (IllegalAccessException e) {
//            MyLogger.error(e);
        }
    }

    protected void disableDraggingFullScreen(BottomSheetDialog bottomSheetDialog) {
        try {
            activity = getActivity();
            Field behaviorField = bottomSheetDialog.getClass().getDeclaredField("behavior");
            behaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(bottomSheetDialog);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
            behavior.setPeekHeight(new ScreenUtils(activity).getHeight());
        } catch (NoSuchFieldException e) {
//            MyLogger.error(e);
        } catch (IllegalAccessException e) {
//            MyLogger.error(e);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void makeToast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    protected void dismissBottomSheet(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

}
