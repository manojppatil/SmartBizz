package com.smartbizz.newUI;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vijay on 4/10/19.
 */

public class SharedPref {
    public  static  final String PREF_NAME="UserData";
    public  static  final String PREF_Login="Login";

    public SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public  void setLoginDone(Context context, boolean value) {

        getPref(context).edit().putBoolean(PREF_Login, value).apply();
    }
    public  boolean getLoginDone(Context context) {

        return getPref(context).getBoolean(PREF_Login, false);
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.commit();
    }
}
