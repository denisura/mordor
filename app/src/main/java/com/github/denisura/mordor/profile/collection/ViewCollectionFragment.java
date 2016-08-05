package com.github.denisura.mordor.profile.collection;

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

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.database.AppProvider;
import com.github.denisura.mordor.model.ProfileModel;
import com.github.denisura.mordor.profile.collection.helper.SimpleItemTouchHelperCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ViewCollectionFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    final static String LOG_TAG = ViewCollectionFragment.class.getCanonicalName();

    public static final int PROFILES_LOADER = 0;

    private static final String ARG_PROFILE_ID = "profile_id";

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

        mLayoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.col_number));

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ProfileCollectionAdapter(getContext(), null, mCallbacks);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

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
        getLoaderManager().initLoader(PROFILES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
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

        int position = 0;

        if (getArguments().getLong(ARG_PROFILE_ID) > 0) {
            if (data != null) {
                while (data.moveToNext()) {
                    ProfileModel profileModel = new ProfileModel(data);
                    if (profileModel.getId() == getArguments().getLong(ARG_PROFILE_ID)) {
                        break;
                    }
                    position++;
                }
            }
        }
        mRecyclerView.getLayoutManager().scrollToPosition(position);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(LOG_TAG, "onLoaderReset");
        if (mAdapter != null) {
            mAdapter.changeCursor(null);
        }
    }
}