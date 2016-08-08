package com.github.denisura.mordor.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.TwoPaneFragmentActivity;
import com.github.denisura.mordor.ui.profile.collection.ViewCollectionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewProfileActivity extends TwoPaneFragmentActivity {

    final static String LOG_TAG = ViewProfileActivity.class.getCanonicalName();
    final static String EXTRA_PROFILE_ID = LOG_TAG + "_extra_profile_id";
    final static long INVALID_PROFILE_ID = -1;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    public static Intent newIntent(Context packageContext, long profileId) {
        Log.d(LOG_TAG, "Create intent with profileId " + profileId);
        Intent intent = new Intent(packageContext, ViewProfileActivity.class);
        intent.putExtra(EXTRA_PROFILE_ID, profileId);
        return intent;
    }

    @Override
    protected Fragment createMasterFragment() {
        long profileId = getIntent().getLongExtra(EXTRA_PROFILE_ID, INVALID_PROFILE_ID);
        Log.d(LOG_TAG, "Create Master fragment for profileId " + profileId);
        return HistoryChartFragment.newInstance(profileId);
    }

    @Override
    protected Fragment createDetailFragment() {
        long profileId = getIntent().getLongExtra(EXTRA_PROFILE_ID, INVALID_PROFILE_ID);
        Log.d(LOG_TAG, "Create Detail fragment for profileId " + profileId);
        return ProfileDetailsFragment.newInstance(profileId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(LOG_TAG, "onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        mMasterActivityFragment = createMasterFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.master_fragment_container, mMasterActivityFragment)
                .commit();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onHomeClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onHomeClicked() {
        long profileId = getIntent().getLongExtra(EXTRA_PROFILE_ID, INVALID_PROFILE_ID);
        startActivity(ViewCollectionActivity.newIntent(this, profileId));
        finish();
    }

}