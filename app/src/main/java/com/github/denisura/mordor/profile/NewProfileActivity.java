package com.github.denisura.mordor.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.SingleFragmentActivity;
import com.github.denisura.mordor.profile.collection.ViewCollectionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewProfileActivity extends SingleFragmentActivity implements NewProfileFragment.Callbacks {

    final static String LOG_TAG = NewProfileActivity.class.getCanonicalName();


    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @Override
    protected Fragment createFragment() {
        return NewProfileFragment.newInstance();
    }

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, NewProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
    }


    @Override
    public void onProfileCreated(long profileId) {
        Log.i(LOG_TAG, "View profile " + profileId);
        startActivity(ViewProfileActivity.newIntent(this, profileId));
        finish();

    }

    @Override
    public void onProfileCanceled() {
        Log.i(LOG_TAG, "Open profile collection");
        startActivity(ViewCollectionActivity.newIntent(this));
        finish();
    }
}
