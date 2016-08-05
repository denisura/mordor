package com.github.denisura.mordor.profile;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.database.AppProvider;
import com.github.denisura.mordor.model.ProfileModel;
import com.github.denisura.mordor.model.Sample;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.github.denisura.mordor.R.id.creditScoreBucket;
import static com.github.denisura.mordor.R.id.currentRate;
import static com.github.denisura.mordor.R.id.lineChart;
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


public class ViewProfileFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        OnChartGestureListener, OnChartValueSelectedListener {


    final static String LOG_TAG = ViewProfileFragment.class.getCanonicalName();

    public static final int PROFILE_LOADER = 0;
    public static final int HISTORY_LOADER = 1;

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

    @BindView(lineChart)
    LineChart mChart;


    private Context mContext;


    public static ViewProfileFragment newInstance(long profileId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROFILE_ID, profileId);
        ViewProfileFragment fragment = new ViewProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public ViewProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_profile, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mContext = getActivity();

        mChart.invalidate();
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        mChart.setPinchZoom(false);
        mChart.getLegend().setEnabled(false);

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
        getLoaderManager().initLoader(PROFILE_LOADER, null, this);
        getLoaderManager().initLoader(HISTORY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        long profileId = getArguments().getLong(ARG_PROFILE_ID);


        switch (id) {
            case PROFILE_LOADER:
                Uri profileUri = AppProvider.Profiles.withId(profileId);
                return new CursorLoader(getActivity(),
                        profileUri,
                        null,
                        null,
                        null,
                        null);

            case HISTORY_LOADER:
                mChart.setNoDataTextDescription("Data is loading ...");
                Uri historyeUri = AppProvider.History.withId(profileId);

                return new CursorLoader(getActivity(),
                        historyeUri,
                        null,
                        null,
                        null,
                        null);
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        if (null == data) {
            Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (loader.getId()) {
            case PROFILE_LOADER:
                handleProfileData(data);
                break;
            case HISTORY_LOADER:
                handleHistoryData(data);
                break;
            default:
                Log.v(LOG_TAG, " In onLoadFinished Unknown loader id loader ID #" + loader.getId());
        }

    }

    private void handleProfileData(Cursor data) {
        if (data.getCount() < 1) {
            Toast.makeText(getActivity(), "Couldn't find profile", Toast.LENGTH_SHORT).show();
            return;
        }

        data.moveToFirst();

        ProfileModel profileModel = new ProfileModel(data);
        mCreditScoreBucket.setText(formatCreditScoreBucket(mContext, profileModel.getCreditScoreBucket()));
        mLoanToValueBucket.setText(formatLoanToValueBucket(mContext, profileModel.getLoanToValueBucket()));
        mProgram.setText(formatProgram(mContext, profileModel.getProgram()));
        mCurrentRate.setText(formatCurrentRate(profileModel.getCurrentRate()));
        mLocation.setText(formatLocation(mContext, profileModel.getLocation()));
        setTrendIcon(mTrendIcon, getActivity(), profileModel.getTrend());

    }


    private void handleHistoryData(Cursor data) {

        Log.d(LOG_TAG, "handleHistoryData data.count:" + data.getCount());
        if (data.getCount() < 1) {
            mChart.setNoDataTextDescription("No historical data is available at this moment.");
            return;
        }
        mChart.invalidate();

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        int entryId = 0;


        while (data.moveToNext()) {

            Sample sample = new Sample(data);

            String sampleTime = sample.getTime();
            DateTime dateTime = DateTime.parse(sampleTime);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd HH:mm");
            xVals.add(fmt.print(dateTime));

            float val = sample.getRate();
            yVals.add(new Entry(val, entryId));

            entryId++;
        }


        LineDataSet set;

//        if (mChart.getData() != null &&
//                mChart.getData().getDataSetCount() > 0) {
//            set = (LineDataSet) mChart.getData().getDataSetByIndex(0);
//            set.setYVals(yVals);
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
//            return;
//        }


         set = new LineDataSet(yVals, "");

        // set the line to be drawn like this "- - - - - -"
        set.enableDashedLine(10f, 5f, 0f);
        set.enableDashedHighlightLine(10f, 5f, 0f);
        set.setColor(Color.BLACK);
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(1f);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(false);
        set.setValueTextSize(9f);
        set.setDrawFilled(false);
        set.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set);

        // create a data object with the datasets
        LineData lineData = new LineData(xVals, dataSets);

        // set data
        mChart.setData(lineData);
        mChart.animateX(1000, Easing.EasingOption.EaseInOutQuart);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");

        switch (loader.getId()) {
            case HISTORY_LOADER:
                if (mChart != null) {
                    mChart.invalidate();
                }
                break;
            case PROFILE_LOADER:

                break;
            default:
                Log.v(LOG_TAG, " In onLoadFinished Unknown loader id loader ID #" + loader.getId());
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleXIndex() + ", high: " + mChart.getHighestVisibleXIndex());
        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

}