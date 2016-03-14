package com.github.denisura.mordor.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.database.AppProvider;
import com.github.denisura.mordor.model.ProfileModel;
import com.github.denisura.mordor.network.RateHistoryDataAPI;

public class ProfileSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String LOG_TAG = ProfileSyncAdapter.class.getSimpleName();
    public static final String ACTION_DATA_UPDATED =
            "com.example.android.sunshine.app.ACTION_DATA_UPDATED";
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int PROFILE_NOTIFICATION_ID = 3004;


    private final static String SYNC_EXTRAS_PROFILE_ID = "profile_id";


    public ProfileSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        Context context = getContext();

        long profileId = extras.getLong(SYNC_EXTRAS_PROFILE_ID);
        if (profileId > 0) {


            // Queries the user dictionary and returns results
            Cursor mCursor = context.getContentResolver().query(
                    AppProvider.Profiles.withId(profileId),
                    null,
                    null,
                    null,
                    null);

            if (null == mCursor) {
                Toast.makeText(context, "Cannot load profile", Toast.LENGTH_SHORT).show();

                return;
            } else if (mCursor.getCount() < 1) {
                Toast.makeText(context, "Profile cannot be found", Toast.LENGTH_SHORT).show();
                mCursor.close();
                return;
            }
            mCursor.moveToFirst();
            ProfileModel profile = new ProfileModel(mCursor);

            mCursor.close();
            new RateHistoryDataAPI(getContext()).fetchProfileHistory(profile);
            return;
        }

        // TODO sync all profiles

        Log.d(LOG_TAG, "Starting sync");


    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
        syncNow(context);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        ProfileSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.app_content_authority), true);
    }



    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.app_content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    public static void syncNow(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.app_content_authority), bundle);
    }


    public static void syncNowProfile(Context context, long profileId) {
        Log.e(LOG_TAG, "syncNowProfile Profile ID:" + profileId);

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putLong(SYNC_EXTRAS_PROFILE_ID, profileId);

        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.app_content_authority), bundle);
        Log.e(LOG_TAG, "requestSync");
    }

}