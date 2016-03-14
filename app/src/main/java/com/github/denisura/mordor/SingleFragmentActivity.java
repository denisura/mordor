package com.github.denisura.mordor;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    public Fragment mActivityFragment;

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        mActivityFragment = fm.findFragmentById(R.id.fragment_container);

        if (mActivityFragment == null) {
            mActivityFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, mActivityFragment)
                    .commit();
        }
    }
}