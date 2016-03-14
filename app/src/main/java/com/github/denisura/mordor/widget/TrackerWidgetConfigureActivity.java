package com.github.denisura.mordor.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.SingleFragmentActivity;
import com.github.denisura.mordor.profile.collection.ProfileCollectionItemViewHolder;
import com.github.denisura.mordor.profile.collection.ViewCollectionFragment;
import com.github.denisura.mordor.utils.WidgetUtilities;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.github.denisura.mordor.utils.Utilities.updateWidgets;


public class TrackerWidgetConfigureActivity extends SingleFragmentActivity
        implements ProfileCollectionItemViewHolder.Callbacks {

    final static String LOG_TAG = TrackerWidgetConfigureActivity.class.getCanonicalName();


    public static final String PREFS_NAME = "com.github.denisura.mordor.widget.TrackerWidget";
    public static final String PREF_PREFIX_KEY = "appwidget_";

    final static String EXTRA_PROFILE_ID = LOG_TAG + "_extra_profile_id";
    final static long INVALID_PROFILE_ID = -1;


    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;


    @Bind(R.id.toolbar)
    public Toolbar mToolbar;

    public TrackerWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onProfileSelected(long profileId) {
        Log.i("TrackerWidget", "Selected Profile " + profileId);


        final Context context = TrackerWidgetConfigureActivity.this;

        WidgetUtilities.saveProfilePref(context, PREFS_NAME, mAppWidgetId, profileId);


//        // It is the responsibility of the configuration activity to update the app widget
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        updateWidgets(context);
        //TrackerWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);






        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }


    @Override
    protected Fragment createFragment() {
        long profileId = getIntent().getLongExtra(EXTRA_PROFILE_ID, INVALID_PROFILE_ID);
        return ViewCollectionFragment.newInstance(profileId);

    }

    @Override
    public void onCreate(Bundle icicle) {
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        super.onCreate(icicle);
        ButterKnife.bind(this);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }
}

