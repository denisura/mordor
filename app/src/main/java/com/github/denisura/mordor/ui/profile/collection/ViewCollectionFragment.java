package com.github.denisura.mordor.ui.profile.collection;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.data.database.AppProvider;
import com.github.denisura.mordor.data.model.ProfileModel;
import com.github.denisura.mordor.ui.profile.collection.helper.SimpleItemTouchHelperCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ViewCollectionFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    final static String LOG_TAG = ViewCollectionFragment.class.getCanonicalName();

    public static final int PROFILES_LOADER = 0;

    private static final String ARG_PROFILE_ID = "profile_id";
    private int mPosition = ListView.INVALID_POSITION;

    private ProfileCollectionAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProfileCollectionItemViewHolder.Callbacks mCallbacks;
    private Unbinder unbinder;

    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (ProfileCollectionItemViewHolder.Callbacks) context;
    }


    public static ViewCollectionFragment newInstance(long profileId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROFILE_ID, profileId);
        ViewCollectionFragment fragment = new ViewCollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_collection, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        Log.d(LOG_TAG, "Create View for profile " + getArguments().getLong(ARG_PROFILE_ID));

        mLayoutManager = new GridLayoutManager(
                getContext(),
                getResources().getInteger(R.integer.col_number),
                getResources().getInteger(R.integer.collection_orientation),
                false);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ProfileCollectionAdapter(getContext(), null, mCallbacks);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        if (savedInstanceState == null) {
            mAdapter.mSelected = getArguments().getLong(ARG_PROFILE_ID);
            Log.d("opa", "Set adapter select from argument to " + mAdapter.mSelected);
        }
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");
        getLoaderManager().initLoader(PROFILES_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG, "onCreateLoader");
        return new CursorLoader(getActivity(),
                AppProvider.Profiles.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(LOG_TAG, "onLoadFinished");

        mAdapter.swapCursor(data);

        if (data != null) {
            if (mAdapter.mSelected > 0) {
                while (data.moveToNext()) {
                    ProfileModel profileModel = new ProfileModel(data);
                    if (profileModel.getId() == mAdapter.mSelected) {
                        break;
                    }
                    mPosition++;
                }
            } else {
                data.moveToFirst();
                ProfileModel profileModel = new ProfileModel(data);
                mAdapter.mSelected = profileModel.getId();
                mPosition = 0;
            }
        }

        mRecyclerView.getLayoutManager().scrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(LOG_TAG, "onLoaderReset");
        if (mAdapter != null) {
            mAdapter.changeCursor(null);
        }
    }


}