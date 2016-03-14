package com.github.denisura.mordor.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ProfileSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static ProfileSyncAdapter sProfileSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sProfileSyncAdapter == null) {
                sProfileSyncAdapter = new ProfileSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sProfileSyncAdapter.getSyncAdapterBinder();
    }
}