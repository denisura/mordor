package com.github.denisura.mordor.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.utils.WidgetUtilities;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link TrackerWidgetConfigureActivity TrackerWidgetConfigureActivity}
 */
public class TrackerWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, TrackerWidgetIntentService.class));
    }


    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, TrackerWidgetIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (context.getResources().getString(R.string.intent_action_data_updated).equals(intent.getAction())) {
            context.startService(new Intent(context, TrackerWidgetIntentService.class));
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WidgetUtilities.deleteProfilePref(context, TrackerWidgetConfigureActivity.PREFS_NAME, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

