package com.github.denisura.mordor;

import android.app.Application;

import com.github.denisura.mordor.data.sync.ProfileSyncAdapter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import net.danlew.android.joda.JodaTimeAndroid;

public class AndroidApplication extends Application {

    public Tracker mTracker;

    // Get the tracker associated with this app
    public void startTracking() {

        // Initialize an Analytics tracker using a Google Analytics property ID.

        // Does the Tracker already exist?
        // If not, create it

        if (mTracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);

            // Get the config data for the tracker
            mTracker = ga.newTracker(R.xml.analytics);

            // Enable tracking of activities
            ga.enableAutoActivityReports(this);
        }
    }


    public Tracker getTracker() {
        // Make sure the tracker exists
        startTracking();

        // Then return the tracker
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        ProfileSyncAdapter.syncNow(this);
    }

}