package com.github.denisura.mordor.ui.profile;

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
import android.widget.Toast;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.data.database.AppProvider;
import com.github.denisura.mordor.data.model.Sample;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.github.denisura.mordor.R.id.adView;
import static com.github.denisura.mordor.R.id.lineChart;

public class HistoryChartFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        OnChartGestureListener, OnChartValueSelectedListener {

    final static String LOG_TAG = HistoryChartFragment.class.getCanonicalName();

    public static final int LOADER = 0;
    private static final String ARG_PROFILE_ID = "profile_id";
    private Unbinder unbinder;

    @BindView(lineChart)
    LineChart mChart;

    @BindView(adView)
    AdView mAdView;

    public static HistoryChartFragment newInstance(long profileId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROFILE_ID, profileId);
        HistoryChartFragment fragment = new HistoryChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_history_chart, container, false);
        unbinder = ButterKnife.bind(this, rootView);

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

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

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
        Log.d(LOG_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        long profileId = getArguments().getLong(ARG_PROFILE_ID);

        Log.d(LOG_TAG, "onCreateLoader profileId " + profileId);
        mChart.setNoDataTextDescription("Data is loading ...");
        Uri historyeUri = AppProvider.History.withId(profileId);

        return new CursorLoader(getActivity(),
                historyeUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        if (null == data) {
            Toast.makeText(getActivity(), getContext().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getCount() < 1) {
            mChart.setNoDataTextDescription(getContext().getString(R.string.history_chart_no_data));
            return;
        }
        handleHistoryData(data);
    }

    private void handleHistoryData(Cursor data) {

        Log.d(LOG_TAG, "handleHistoryData data.count:" + data.getCount());
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
        mChart.invalidate();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        if (mChart != null) {
            mChart.invalidate();
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