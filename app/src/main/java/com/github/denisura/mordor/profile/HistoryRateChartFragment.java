package com.github.denisura.mordor.profile;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.denisura.mordor.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HistoryRateChartFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
    final static String LOG_TAG = HistoryRateChartFragment.class.getCanonicalName();

    public static final int HISTORY_LOADER = 0;

    private static final String ARG_HISTORY_PROFILE_URI = "history_profile_uri";
    private Unbinder unbinder;

    public static HistoryRateChartFragment newInstance(String profileUri) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_HISTORY_PROFILE_URI, profileUri);
        HistoryRateChartFragment fragment = new HistoryRateChartFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history_rate, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onActivityCreated");
        getLoaderManager().initLoader(HISTORY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                Uri.parse(getArguments().getString(ARG_HISTORY_PROFILE_URI)),
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoaderReset");
        if (null == data) {
            Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
            return;
        } else if (data.getCount() < 1) {
            Toast.makeText(getActivity(), "Couldn't find profile", Toast.LENGTH_SHORT).show();
            return;
        }
        data.moveToFirst();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
    }

}
