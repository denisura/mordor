package com.github.denisura.mordor;

import android.app.Application;

import com.github.denisura.mordor.sync.ProfileSyncAdapter;

import net.danlew.android.joda.JodaTimeAndroid;

public class AndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        ProfileSyncAdapter.syncNow(this);
    }

}