package com.github.denisura.mordor;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class TwoPaneFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createMasterFragment();

    protected abstract Fragment createDetailFragment();

    public Fragment mMasterActivityFragment;
    public Fragment mDetailActivityFragment;

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_twopane_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        mMasterActivityFragment = fm.findFragmentById(R.id.master_fragment_container);
        mDetailActivityFragment = fm.findFragmentById(R.id.detail_fragment_container);


        if (mMasterActivityFragment == null) {
            mMasterActivityFragment = createMasterFragment();
            fm.beginTransaction()
                    .add(R.id.master_fragment_container, mMasterActivityFragment)
                    .commit();
        }

        if (mDetailActivityFragment == null) {
            mDetailActivityFragment = createDetailFragment();
            fm.beginTransaction()
                    .add(R.id.detail_fragment_container, mDetailActivityFragment)
                    .commit();
        }
    }
}