package com.github.denisura.mordor.ui.profile.collection;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.data.model.ProfileModel;
import com.github.denisura.mordor.ui.profile.collection.helper.ItemTouchHelperViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

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

public class ProfileCollectionItemViewHolder
        extends RecyclerView.ViewHolder
        implements View.OnClickListener, ItemTouchHelperViewHolder {

    private ProfileModel mProfileModel;
    private Context mContext;
    private Callbacks mCallbacks;
    private ProfileCollectionAdapter mAdapter;

    @BindView(creditScoreBucket)
    TextView mCreditScoreBucket;

    @BindView(loanToValueBucket)
    TextView mLoanToValueBucket;

    @BindView(program)
    TextView mProgram;

    @BindView(currentRate)
    TextView mCurrentRate;

    @BindView(location)
    TextView mLocation;

    @BindView(trend_icon)
    ImageView mTrendIcon;

    public ProfileCollectionItemViewHolder(View itemView, ProfileCollectionItemViewHolder.Callbacks callbacks, ProfileCollectionAdapter adapter) {
        super(itemView);
        itemView.setClickable(true);
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mCallbacks = callbacks;
        mAdapter = adapter;
    }

    public void bindProfile(ProfileModel profileModel, boolean isSelected) {
        mProfileModel = profileModel;

        //set value for all text views

        mCreditScoreBucket.setText(formatCreditScoreBucket(mContext, profileModel.getCreditScoreBucket()));
        mLoanToValueBucket.setText(formatLoanToValueBucket(mContext, profileModel.getLoanToValueBucket()));
        mProgram.setText(formatProgram(mContext, profileModel.getProgram()));
        mCurrentRate.setText(formatCurrentRate(profileModel.getCurrentRate()));
        mLocation.setText(formatLocation(mContext, profileModel.getLocation()));
        setTrendIcon(mTrendIcon, mContext, profileModel.getTrend());

        if (isSelected) {
            onItemSelected();
        } else {
            onItemClear();
        }
    }

    @Override
    public void onItemSelected() {
        ((CardView) itemView).setCardBackgroundColor(mContext.getResources().getColor(R.color.cardview_shadow_end_color));
        ((CardView) itemView).setMaxCardElevation(mContext.getResources().getDimension(R.dimen.cardview_default_elevation));
        ((CardView) itemView).setRadius(mContext.getResources().getDimension(R.dimen.cardview_default_radius));
        itemView.setSelected(true);
    }

    @Override
    public void onItemClear() {
        ((CardView) itemView).setCardBackgroundColor(mContext.getResources().getColor(R.color.windowBackground));
        ((CardView) itemView).setMaxCardElevation(mContext.getResources().getDimension(R.dimen.cardview_default_elevation));
        ((CardView) itemView).setRadius(mContext.getResources().getDimension(R.dimen.cardview_default_radius));
        itemView.setSelected(false);
    }

    public interface Callbacks {
        void onProfileSelected(long profileId);
        void onProfileDeleted(long profileId);
    }

    @Override
    public void onClick(View v) {
        mAdapter.mSelected = mProfileModel.getId();
        mCallbacks.onProfileSelected(mProfileModel.getId());
        mAdapter.notifyDataSetChanged();
    }
}

