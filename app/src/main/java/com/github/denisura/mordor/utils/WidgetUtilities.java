package com.github.denisura.mordor.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.denisura.mordor.R;

public class WidgetUtilities {

    private static final String LOG_TAG = Utilities.class.getSimpleName();

    public static void saveProfilePref(Context context, String prefsName, int appWidgetId, long profileId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(prefsName, 0).edit();
        String prefix = context.getResources().getString(R.string.widget_prefs_prefix_profile_id);
        prefs.putLong(prefix + appWidgetId, profileId);
        prefs.apply();
    }

    public static long loadProfilePref(Context context, String prefsName, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(prefsName, 0);
        String prefix = context.getResources().getString(R.string.widget_prefs_prefix_profile_id);
        return prefs.getLong(prefix + appWidgetId, -1);
    }

    public static void deleteProfilePref(Context context, String prefsName, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(prefsName, 0).edit();
        String prefix = context.getResources().getString(R.string.widget_prefs_prefix_profile_id);
        prefs.remove(prefix + appWidgetId);
        prefs.apply();
    }
}
