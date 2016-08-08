package com.github.denisura.mordor.ui.profile;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.data.database.AppProvider;
import com.github.denisura.mordor.data.model.ProfileModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.github.denisura.mordor.R.id.creditScoreBucket;
import static com.github.denisura.mordor.R.id.currentRate;
import static com.github.denisura.mordor.R.id.loanToValueBucket;
import static com.github.denisura.mordor.R.id.location;
import static com.github.denisura.mordor.R.id.program;
import static com.github.denisura.mordor.R.id.trend_icon;
import static com.github.denisura.mordor.utils.Utilities.formatCreditScoreBucket;
import static com.github.denisura.mordor.utils.Utilities.formatCurrentRate;
import static com.github.denisura.mordor.utils.Utilities.formatLoanToValueBucket;
import static com.github.denisura.mordor.utils.Utilities.formatLocation;
import static com.github.denisura.mordor.utils.Utilities.formatProgram;
import static com.github.denisura.mordor.utils.Utilities.setTrendIcon;


public class ProfileDetailsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    final static String LOG_TAG = ProfileDetailsFragment.class.getCanonicalName();

    public static final int LOADER = 0;
    private static final String ARG_PROFILE_ID = "profile_id";

    private Unbinder unbinder;

    @BindView(creditScoreBucket)
    TextView mCreditScoreBucket;

    @BindView(loanToValueBucket)
    TextView mLoanToValueBucket;

    @BindView(program)
    TextView mProgram;

    @BindView(currentRate)
    TextView mCurrentRate;

    @BindView(trend_icon)
    ImageView mTrendIcon;

    @BindView(location)
    TextView mLocation;

    private Context mContext;


    public static ProfileDetailsFragment newInstance(long profileId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROFILE_ID, profileId);
        ProfileDetailsFragment fragment = new ProfileDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public ProfileDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cardview_profile_view, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mContext = getActivity();
        Log.d(LOG_TAG, "onCreateView");
        return rootView;
    }


    @Override
    public void onDestroyView() {
        Log.d(LOG_TAG, "onDestroyView");
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");
        getLoaderManager().initLoader(LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long profileId = getArguments().getLong(ARG_PROFILE_ID);
        Log.d(LOG_TAG, "onCreateLoader profileId " + profileId);
        Uri profileUri = AppProvider.Profiles.withId(profileId);
        return new CursorLoader(getActivity(),
                profileUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        if (null == data) {
            Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getCount() < 1) {
            Toast.makeText(getActivity(), "Couldn't find profile", Toast.LENGTH_SHORT).show();
            return;
        }
        data.moveToFirst();
        handleProfileData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void handleProfileData(Cursor data) {
        ProfileModel profileModel = new ProfileModel(data);
        mCreditScoreBucket.setText(formatCreditScoreBucket(mContext, profileModel.getCreditScoreBucket()));
        mLoanToValueBucket.setText(formatLoanToValueBucket(mContext, profileModel.getLoanToValueBucket()));
        mProgram.setText(formatProgram(mContext, profileModel.getProgram()));
        mCurrentRate.setText(formatCurrentRate(profileModel.getCurrentRate()));
        mLocation.setText(formatLocation(mContext, profileModel.getLocation()));
        setTrendIcon(mTrendIcon, getActivity(), profileModel.getTrend());
    }
}