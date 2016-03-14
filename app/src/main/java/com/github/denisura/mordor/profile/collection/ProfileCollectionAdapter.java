package com.github.denisura.mordor.profile.collection;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.denisura.mordor.CursorRecyclerViewAdapter;
import com.github.denisura.mordor.R;
import com.github.denisura.mordor.database.AppProvider;
import com.github.denisura.mordor.model.ProfileModel;
import com.github.denisura.mordor.profile.collection.helper.ItemTouchHelperAdapter;

public class ProfileCollectionAdapter
        extends CursorRecyclerViewAdapter<ProfileCollectionItemViewHolder>
        implements ItemTouchHelperAdapter {

    static final String LOG_TAG = ProfileCollectionAdapter.class.getCanonicalName();

    private ProfileCollectionItemViewHolder.Callbacks mCallbacks;
    private Context mContext;


    public ProfileCollectionAdapter(Context context, Cursor cursor, ProfileCollectionItemViewHolder.Callbacks callbacks) {
        super(context, cursor);
        mCallbacks = callbacks;
        mContext = context;
    }

    @Override
    public ProfileCollectionItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            int layoutId = R.layout.cardview_profile;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
//            view.setFocusable(true);

            return new ProfileCollectionItemViewHolder(view, mCallbacks);

        }
        throw new RuntimeException("Not bound to RecyclerView");
    }

    @Override
    public void onBindViewHolder(ProfileCollectionItemViewHolder viewHolder, Cursor cursor) {
        ProfileModel profileModel = new ProfileModel(cursor);
        viewHolder.bindProfile(profileModel);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        //mItems.remove(position);
        Log.d(LOG_TAG, "Removed item at position " + position + " Item ID " + getItemId(position));
        ContentResolver cr = mContext.getContentResolver();
        cr.delete(AppProvider.Profiles.withId(getItemId(position)), null, null);
        cr.delete(AppProvider.History.withId(getItemId(position)), null, null);
        notifyItemRemoved(position);
    }
}