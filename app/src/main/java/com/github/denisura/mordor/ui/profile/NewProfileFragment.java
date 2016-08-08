package com.github.denisura.mordor.ui.profile;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.data.database.AppProvider;
import com.github.denisura.mordor.data.database.ProfileColumns;
import com.github.denisura.mordor.data.model.ProfileModel;
import com.github.denisura.mordor.data.sync.ProfileSyncAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.github.denisura.mordor.R.id.credit_score_spinner;
import static com.github.denisura.mordor.R.id.loan_programm_spinner;
import static com.github.denisura.mordor.R.id.loantovalue_spinner;
import static com.github.denisura.mordor.R.id.state_spinner;
import static com.github.denisura.mordor.utils.Utilities.getCreditScoreCodes;
import static com.github.denisura.mordor.utils.Utilities.getCreditScoreValues;
import static com.github.denisura.mordor.utils.Utilities.getDownpaymentCodes;
import static com.github.denisura.mordor.utils.Utilities.getDownpaymentValues;
import static com.github.denisura.mordor.utils.Utilities.getLoanProgramsCodes;
import static com.github.denisura.mordor.utils.Utilities.getLoanProgramsValues;
import static com.github.denisura.mordor.utils.Utilities.getRandomIntInRage;
import static com.github.denisura.mordor.utils.Utilities.getStateCodes;
import static com.github.denisura.mordor.utils.Utilities.getStateNames;


public class NewProfileFragment extends Fragment {

    final static String LOG_TAG = NewProfileFragment.class.getCanonicalName();
    private Callbacks mCallbacks;
    private Unbinder unbinder;


    @BindView(loan_programm_spinner)
    Spinner mLoanProgramSpinner;

    @BindView(credit_score_spinner)
    Spinner mCreditScoreSpinner;

    @BindView(loantovalue_spinner)
    Spinner mLoanToValueSpinner;

    @BindView(state_spinner)
    Spinner mStateSpinner;

    public interface Callbacks {
        void onProfileCreated(long profileId);

        void onProfileCanceled();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static NewProfileFragment newInstance() {
        return new NewProfileFragment();
    }

    public NewProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_profile, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        setupStateSpinner();
        setupLoanProgramSpinner();
        setupLoanToValueSpinner();
        setupCreditScoreSpinner();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.buttonSave)
    public void onSaveCLick() {
        Log.i(LOG_TAG, "Start main Activity");
        long profileId = 0;

        ProfileModel mProfile = new ProfileModel();

        mProfile.setCreditScoreBucket(getCreditScoreCodes().get(mCreditScoreSpinner.getSelectedItemPosition()));
        mProfile.setLoanToValueBucket(getDownpaymentCodes().get(mLoanToValueSpinner.getSelectedItemPosition()));
        mProfile.setLocation(getStateCodes().get(mStateSpinner.getSelectedItemPosition()));
        mProfile.setProgram(getLoanProgramsCodes().get(mLoanProgramSpinner.getSelectedItemPosition()));

        Uri profileUri;

        // Queries the user dictionary and returns results
        Cursor mCursor = getActivity().getContentResolver().query(
                AppProvider.Profiles.CONTENT_URI,   // The content URI of the words table
                null,                        // The columns to return for each row
                ProfileColumns.CREDIT_SCORE_BUCKET + "=? AND "
                        + ProfileColumns.LOAN_TO_VALUE_BUCKET + "=? AND "
                        + ProfileColumns.PROGRAM + "=? AND "
                        + ProfileColumns.STATE + "=? ",
                // Selection criteria
                new String[]{mProfile.getCreditScoreBucket(),
                        mProfile.getLoanToValueBucket(),
                        mProfile.getProgram(),
                        mProfile.getLocation()
                },                     // Selection criteria
                null);                        // The sort order for the returned rows

        if (null == mCursor) {
            Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();

            return;
        } else if (mCursor.getCount() < 1) {
            Log.i(LOG_TAG, "Insert New profile and sync it in ProfileSyncAdapter " + mProfile.getContentValues().toString());
            profileUri = getActivity().getContentResolver().insert(
                    AppProvider.Profiles.CONTENT_URI, mProfile.getContentValues());
            profileId = AppProvider.Profiles.getProfileIdFromUri(profileUri);
            ProfileSyncAdapter.syncNowProfile(getContext(), profileId);
        } else {
            // Insert code here to do something with the results
            mCursor.moveToFirst();
            mProfile = new ProfileModel(mCursor);
            profileId = mProfile.getId();
            Log.i(LOG_TAG, "Found profile in DB FTH");

        }
        mCursor.close();

        mCallbacks.onProfileCreated(profileId);
    }

    @OnClick(R.id.buttonCancel)
    public void onCancelCLick() {
        Log.i(LOG_TAG, "Start main Activity");
        mCallbacks.onProfileCanceled();
    }


    public void setupStateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                getStateNames());
        mStateSpinner.setAdapter(adapter);
        mStateSpinner.setSelection(getRandomIntInRage(1, getStateNames().size()));
    }


    public void setupLoanToValueSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                getDownpaymentValues());
        mLoanToValueSpinner.setAdapter(adapter);
        mLoanToValueSpinner.setSelection(getRandomIntInRage(1, getDownpaymentValues().size()));
    }


    public void setupCreditScoreSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                getCreditScoreValues());
        mCreditScoreSpinner.setAdapter(adapter);
        mCreditScoreSpinner.setSelection(getRandomIntInRage(1, getCreditScoreValues().size()));
    }


    public void setupLoanProgramSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                getLoanProgramsCodes());
        mLoanProgramSpinner.setAdapter(adapter);
        mLoanProgramSpinner.setSelection(getRandomIntInRage(1, getLoanProgramsValues().size()));
    }
}