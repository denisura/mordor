package com.github.denisura.mordor.splash;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.database.AppProvider;
import com.github.denisura.mordor.profile.NewProfileActivity;
import com.github.denisura.mordor.profile.collection.ViewCollectionActivity;
import com.github.denisura.mordor.sync.ProfileSyncAdapter;

public class SplashActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    final static String LOG_TAG = SplashActivity.class.getCanonicalName();

    private static final int PROFILE_LOADER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

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