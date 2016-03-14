package com.github.denisura.mordor.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.SingleFragmentActivity;
import com.github.denisura.mordor.profile.collection.ViewCollectionActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewProfileActivity extends SingleFragmentActivity {

    final static String LOG_TAG = ViewProfileActivity.class.getCanonicalName();
    final static String EXTRA_PROFILE_ID = LOG_TAG + "_extra_profile_id";
    final static long INVALID_PROFILE_ID = -1;

    @Bind(R.id.toolbar)
    public Toolbar mToolbar;

    @Override
    protected Fragment createFragment() {
        long profileId = getIntent().getLongExtra(EXTRA_PROFILE_ID, INVALID_PROFILE_ID);
        Log.d(LOG_TAG, "Create fragment for profileId " + profileId);
        return ViewProfileFragment.newInstance(profileId);
    }

    public static Intent newIntent(Context packageContext, long profileId) {
        Log.d(LOG_TAG, "Create intent with profileId " + profileId);
        Intent intent = new Intent(packageContext, ViewProfileActivity.class);
        intent.putExtra(EXTRA_PROFILE_ID, profileId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(LOG_TAG, "onCreate");
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