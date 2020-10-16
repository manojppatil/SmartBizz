package com.smartbizz;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

//import com.smartbizz.Util.FontsOverride;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Application {

    public static String TAG = "SMARTBIZZ LOG";


    public static String auth_token ="" ;
    @Override
    public void onCreate() {
        super.onCreate();
//        setFont();
//      generateHashkey();
    }

}


