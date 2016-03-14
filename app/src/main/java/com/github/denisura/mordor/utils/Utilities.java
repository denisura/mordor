package com.github.denisura.mordor.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.database.ProfileColumns;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utilities {

    private static final String LOG_TAG = Utilities.class.getSimpleName();

    public static String formatLoanToValueBucket(Context context, String value) {

        int index = getDownpaymentCodes().indexOf(value);
        value = getDownpaymentValues().get(index);
        return context.getResources()
                .getString(R.string.format_loan_to_value_bucket, value);
    }

    public static String formatCreditScoreBucket(Context context, String value) {
        int index = getCreditScoreCodes().indexOf(value);
        value = getCreditScoreValues().get(index);
        return context.getResources().getString(R.string.format_credit_score_bucket, value);
    }

    public static String formatLocation(Context context, String value) {

        int index = getStateCodes().indexOf(value);
        value = getStateNames().get(index);

        return context.getResources().getString(R.string.format_location, value);
    }

    public static String formatProgram(Context context, String value) {

        int index = getLoanProgramsCodes().indexOf(value);
        value = getLoanProgramsValues().get(index);

        return context.getResources().getString(R.string.format_program, value);
    }


    public static String formatCurrentRate(Float value) {
        if (value == 0) {
            return "---";
        }
        NumberFormat defaultFormat = NumberFormat.getPercentInstance();
        defaultFormat.setMinimumFractionDigits(3);
        return defaultFormat.format(value / 100);
    }


    public static int getRandomIntInRage(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }


    public static List<String> getStateCodes() {
        List<String> stateCodes = new ArrayList<String>();
        stateCodes.add(0, "");
        stateCodes.add(1, "AL");
        stateCodes.add(2, "AK");
        stateCodes.add(3, "AZ");
        stateCodes.add(4, "AR");
        stateCodes.add(5, "CA");
        stateCodes.add(6, "CO");
        stateCodes.add(7, "CT");
        stateCodes.add(8, "DE");

        //TODO add more states here

        return stateCodes;
    }


    public static List<String> getStateNames() {
        List<String> stateNames = new ArrayList<String>();
        stateNames.add(0, "");
        stateNames.add(1, "Alabama");
        stateNames.add(2, "Alaska");
        stateNames.add(3, "Arizona");
        stateNames.add(4, "Arkansas");
        stateNames.add(5, "California");
        stateNames.add(6, "Colorado");
        stateNames.add(7, "Connecticut");
        stateNames.add(8, "Delaware");

        //TODO add more states here
        return stateNames;
    }


    public static List<String> getDownpaymentCodes() {

        List<String> downpaymentCodes = new ArrayList<String>();
        downpaymentCodes.add(0, "");
        downpaymentCodes.add(1, "VeryHigh");
        downpaymentCodes.add(2, "High");
        downpaymentCodes.add(3, "Normal");
        return downpaymentCodes;
    }


    public static List<String> getDownpaymentValues() {

        List<String> downpaymentValues = new ArrayList<String>();
        downpaymentValues.add(0, "");
        downpaymentValues.add(1, "20% or higher");
        downpaymentValues.add(2, "5% - 20%");
        downpaymentValues.add(3, "Less than 5%");
        return downpaymentValues;
    }


    public static List<String> getCreditScoreCodes() {

        List<String> creditScoreCodes = new ArrayList<String>();
        creditScoreCodes.add(0, "");
        creditScoreCodes.add(1, "VeryHigh");
        creditScoreCodes.add(2, "High");
        creditScoreCodes.add(3, "Low");
        return creditScoreCodes;
    }

    public static List<String> getCreditScoreValues() {

        List<String> creditScoreValues = new ArrayList<String>();
        creditScoreValues.add(0, "");
        creditScoreValues.add(1, "740 - 850");
        creditScoreValues.add(2, "680 - 739");
        creditScoreValues.add(3, "350 - 679");
        return creditScoreValues;
    }


    public static List<String> getLoanProgramsCodes() {

        List<String> programValues = new ArrayList<String>();
        programValues.add(0, "");
        programValues.add(1, "Fixed30Year");
        programValues.add(2, "Fixed20Year");
        programValues.add(3, "Fixed15Year");
        programValues.add(4, "ARM5");
        programValues.add(5, "ARM7");
        return programValues;
    }


    public static List<String> getLoanProgramsValues() {

        List<String> programValues = new ArrayList<String>();
        programValues.add(0, "");
        programValues.add(1, "30 year fixed");
        programValues.add(2, "20 year fixed");
        programValues.add(3, "15 year fixed");
        programValues.add(4, "5/1 ARM");
        programValues.add(5, "7/1 ARM");
        return programValues;
    }


    public static Drawable getTrendIcon(Context context, String value) {

        if (value == null) {
            return null;
        }
        switch (value) {
            case ProfileColumns.TREND_DOWN:
                return context.getResources().getDrawable(R.drawable.ic_trending_down_black_24dp);
            case ProfileColumns.TREND_UP:
                return context.getResources().getDrawable(R.drawable.ic_trending_up_black_24dp);
            case ProfileColumns.TREND_SAME:
                return context.getResources().getDrawable(R.drawable.ic_trending_flat_black_24dp);
            default:
                return null;
        }
    }


    public static int getTrendIconResourceId(String value) {

        if (value == null) {
            return -1;
        }
        switch (value) {
            case ProfileColumns.TREND_DOWN:
                return R.drawable.ic_trending_down_black_24dp;
            case ProfileColumns.TREND_UP:
                return R.drawable.ic_trending_up_black_24dp;
            case ProfileColumns.TREND_SAME:
                return R.drawable.ic_trending_flat_black_24dp;
            default:
                return -1;
        }
    }

    public static int getTrendColor(Context context, String value) {
        if (value == null) {
            return -1;
        }

        switch (value) {
            case ProfileColumns.TREND_DOWN:
                return context.getResources().getColor(android.R.color.holo_red_dark);
            case ProfileColumns.TREND_UP:
                return context.getResources().getColor(android.R.color.holo_blue_dark);
            case ProfileColumns.TREND_SAME:
                return context.getResources().getColor(android.R.color.darker_gray);
            default:
                return context.getResources().getColor(android.R.color.darker_gray);
        }
    }

    public static void setTrendIcon(ImageView imageView, Context context, String value) {
        imageView.setImageDrawable(getTrendIcon(context, value));
        imageView.setColorFilter(getTrendColor(context, value));
        imageView.setContentDescription(value);
    }


    public static void updateWidgets(Context context) {
        Intent dataUpdatedIntent = new Intent(
                context.getResources().getString(R.string.intent_action_data_updated))
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }
}
