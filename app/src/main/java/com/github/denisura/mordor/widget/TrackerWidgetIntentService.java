package com.github.denisura.mordor.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.database.AppProvider;
import com.github.denisura.mordor.model.ProfileModel;
import com.github.denisura.mordor.profile.ViewProfileActivity;
import com.github.denisura.mordor.utils.WidgetUtilities;

import static com.github.denisura.mordor.utils.Utilities.formatCurrentRate;
import static com.github.denisura.mordor.utils.Utilities.getTrendIconResourceId;


public class TrackerWidgetIntentService extends IntentService {
    public TrackerWidgetIntentService() {
        super("TrackerWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, TrackerWidgetProvider.class));

        // Perform this loop procedure for each Tracker widget
        for (int appWidgetId : appWidgetIds) {

            long profileId = WidgetUtilities.loadProfilePref(this, TrackerWidgetConfigureActivity.PREFS_NAME, appWidgetId);

            Log.i("TrackerWidget", "Load Profile Id " + profileId);

            if (profileId < 0) {
                continue;
            }

            // Get tracker data from the ContentProvider
            Uri profileUri = AppProvider.Profiles.withId(profileId);
            Cursor data = getContentResolver().query(profileUri, null, null,
                    null, null);
            if (data == null) {
                continue;
            }
            if (!data.moveToFirst()) {
                data.close();
                continue;
            }

            ProfileModel profileModel = new ProfileModel(data);

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.tracker_widget);

            views.setTextViewText(R.id.widget_current_rate, formatCurrentRate(profileModel.getCurrentRate()));
            views.setTextViewText(R.id.widget_program, profileModel.getProgram());

            int trendIcobResourceId = getTrendIconResourceId(profileModel.getTrend());
            if (trendIcobResourceId > 0) {
                views.setTextViewCompoundDrawables(R.id.widget_current_rate, 0, 0, trendIcobResourceId, 0);
            }

            Intent launchIntent = ViewProfileActivity.newIntent(this, profileId);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, appWidgetId, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
