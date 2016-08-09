package com.github.denisura.mordor.ui.splash;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.github.denisura.mordor.AndroidApplication;
import com.github.denisura.mordor.R;
import com.github.denisura.mordor.data.database.AppProvider;
import com.github.denisura.mordor.ui.profile.NewProfileActivity;
import com.github.denisura.mordor.ui.profile.collection.ViewCollectionActivity;
import com.github.denisura.mordor.data.sync.ProfileSyncAdapter;

public class SplashActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    final static String LOG_TAG = SplashActivity.class.getCanonicalName();

    private static final int PROFILE_LOADER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ((AndroidApplication) getApplication()).startTracking();

        ProfileSyncAdapter.initializeSyncAdapter(this);
        getSupportLoaderManager().initLoader(PROFILE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                AppProvider.Profiles.CONTENT_URI,
                null,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(LOG_TAG, "onLoadFinished size " + data.getCount());
        Intent intent;

        if (data.getCount() > 0) {
            Log.i(LOG_TAG, "start MainActivity");
            intent = ViewCollectionActivity.newIntent(this);

        } else {
            Log.i(LOG_TAG, "start NewProfileActivity");
            intent = NewProfileActivity.newIntent(this);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(LOG_TAG, "onLoaderReset");
    }
}