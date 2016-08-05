package com.github.denisura.mordor.profile.collection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.SingleFragmentActivity;
import com.github.denisura.mordor.profile.NewProfileActivity;
import com.github.denisura.mordor.profile.ViewProfileActivity;

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


    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(fab)
    public FloatingActionButton mFab;


    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment_collection;
    }

    @Override
    protected Fragment createFragment() {
        long profileId = getIntent().getLongExtra(EXTRA_PROFILE_ID, INVALID_PROFILE_ID);
        return ViewCollectionFragment.newInstance(profileId);
    }


    public static Intent newIntent(Context packageContext) {
        return newIntent(packageContext, INVALID_PROFILE_ID);
    }

    public static Intent newIntent(Context packageContext, long profileId) {
        Intent intent = new Intent(packageContext, ViewCollectionActivity.class);
        intent.putExtra(EXTRA_PROFILE_ID, profileId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        Log.i(LOG_TAG, "onCreate");
    }

    @Override
    public void onProfileSelected(long profileId) {
        Log.i(LOG_TAG, "Selected Profile " + profileId);
        startActivity(ViewProfileActivity.newIntent(this, profileId));
    }


    @OnClick(fab)
    public void onFabCLick(View view) {
        startActivity(NewProfileActivity.newIntent(this));
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
    }
}
