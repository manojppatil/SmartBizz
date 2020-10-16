package com.smartbizz.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import com.smartbizz.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;


public class Globle {
    public static void setInstance(Globle instance) {
        Globle.instance = instance;
    }

    private static Globle instance;

    public static final String status = "1"; //For Success
    public static final String payment_platform = "2"; //(1-web, 2-app)
    public static final String payment_partner = "1"; //(1-paytm, 2-atom)

    public static synchronized Globle getInstance() {
        if (instance == null) {
            instance = new Globle();
        }
        return instance;
    }

    public static AlertDialog dialog(Context context, String messege, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle(title);
        builder.setMessage(messege);
        //  builder.setPositiveButton("Ok", null);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.getWindow().getAttributes();
        return builder.create();
    }

}
