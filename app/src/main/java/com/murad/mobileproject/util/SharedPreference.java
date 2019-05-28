package com.murad.mobileproject.util;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreference {
    private static SharedPreference sharedPreference;

    SharedPreferences preference;
    SharedPreferences.Editor editor;
    Context context;

    public SharedPreference(Context context) {
        this.context = context;
        preference = context.getSharedPreferences("myNotes", 0);
        editor = preference.edit();
    }

    public static SharedPreference getInstance(Context context) {
        if (sharedPreference == null)
            sharedPreference = new SharedPreference(context);
        return sharedPreference;
    }

    public String getNotes() {
        return preference.getString("notes", "");
    }

    public void setNotes(String token) {
        editor.putString("notes", token).commit();
    }


}