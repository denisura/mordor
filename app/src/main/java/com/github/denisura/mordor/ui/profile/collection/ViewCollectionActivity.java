package com.github.denisura.mordor.ui.profile.collection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.denisura.mordor.AndroidApplication;
import com.github.denisura.mordor.R;
import com.github.denisura.mordor.SingleFragmentActivity;
import com.github.denisura.mordor.ui.profile.HistoryChartFragment;
import com.github.denisura.mordor.ui.profile.NewProfileActivity;
import com.github.denisura.mordor.ui.profile.ViewProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.denisura.mordor.R.id.fab;


public class ViewCollectionActivity
        extends SingleFragmentActivity
        implements ProfileCollectionItemViewHolder.Callbacks {

    final static String LOG_TAG = ViewCollectionActivity.class.getCanonicalName();
    final static String EXTRA_PROFILE_ID = LOG_TAG + "_extra_profile_id";
    final static long INVALID_PROFILE_ID = -1;
    private static final String SELECTED_PROFILE_ID = "selected_profile_id";

    static long mProfileId = INVALID_PROFILE_ID;


    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(fab)
    public FloatingActionButton mFab;


    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected Fragment createFragment() {
        return ViewCollectionFragment.newInstance(mProfileId);
    }

    public static Intent newIntent(Context packageContext) {
        return newIntent(packageContext, INVALID_PROFILE_ID);
    }

    public static Intent newIntent(Context packageContext, long profileId) {
        Log.d(LOG_TAG, "Create intent with profileId " + profileId);
        Intent intent = new Intent(packageContext, ViewCollectionActivity.class);
        intent.putExtra(EXTRA_PROFILE_ID, profileId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AndroidApplication) getApplication()).startTracking();
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (savedInstanceState != null) {
            mProfileId = savedInstanceState.getLong(SELECTED_PROFILE_ID);
            Log.d("opa", "Set adapter select from savedInstance to " + mProfileId);
        }

        Log.i(LOG_TAG, "onCreate");
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mProfileId = intent.getLongExtra(EXTRA_PROFILE_ID, INVALID_PROFILE_ID);
        Log.i(LOG_TAG, "onNewIntent mProfileId " + mProfileId);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mProfileId > 0) {
            updateProfileDetails(mProfileId);
            updateProfileList(mProfileId);
        }
        Log.i(LOG_TAG, "onResume Selected Profile " + mProfileId);
    }

    @Override
    public void onProfileSelected(long profileId) {
        mProfileId = profileId;
        Log.i(LOG_TAG, "Selected Profile " + profileId);
        if (findViewById(R.id.fragment_detail_container) == null) {
            startActivity(ViewProfileActivity.newIntent(this, profileId));
        } else {
            Fragment historyChart = HistoryChartFragment.newInstance(profileId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_container, historyChart)
                    .commit();
        }
    }

    @Override
    public void onProfileDeleted(long profileId) {
            updateProfileDetails(profileId);
    }

    public void updateProfileDetails(long profileId) {
        mProfileId = profileId;
        if (findViewById(R.id.fragment_detail_container) != null) {
            if (profileId > 0) {
                Fragment historyChart = HistoryChartFragment.newInstance(profileId);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_container, historyChart)
                        .commit();
            } else {
                Fragment emptyView = new BlankFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_container, emptyView)
                        .commit();
            }
        }
    }

    public void updateProfileList(long profileId) {
        if (findViewById(R.id.fragment_detail_container) != null) {
            if (profileId > 0) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, createFragment())
                        .commit();
            }
        }
    }

    @OnClick(fab)
    public void onFabCLick(View view) {
        startActivity(NewProfileActivity.newIntent(this));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "onSaveInstanceState mProfileId " + mProfileId);
        outState.putLong(SELECTED_PROFILE_ID, mProfileId);
        super.onSaveInstanceState(outState);
    }
}