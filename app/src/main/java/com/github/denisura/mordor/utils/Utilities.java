package com.github.denisura.mordor.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.github.denisura.mordor.R;
import com.github.denisura.mordor.data.database.ProfileColumns;

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
        stateCodes.add(9, "FL");
        stateCodes.add(10, "GA");
        stateCodes.add(11, "HI");
        stateCodes.add(12, "ID");
        stateCodes.add(13, "IL");
        stateCodes.add(14, "IN");
        stateCodes.add(15, "IA");
        stateCodes.add(16, "KS");
        stateCodes.add(17, "KY");
        stateCodes.add(18, "LA");
        stateCodes.add(19, "ME");
        stateCodes.add(20, "MD");
        stateCodes.add(21, "MA");
        stateCodes.add(22, "MI");
        stateCodes.add(23, "MN");
        stateCodes.add(24, "MS");
        stateCodes.add(25, "MO");
        stateCodes.add(26, "MT");
        stateCodes.add(27, "NE");
        stateCodes.add(28, "NV");
        stateCodes.add(29, "NH");
        stateCodes.add(30, "NJ");
        stateCodes.add(31, "NM");
        stateCodes.add(32, "NY");
        stateCodes.add(33, "NC");
        stateCodes.add(34, "ND");
        stateCodes.add(35, "OH");
        stateCodes.add(36, "OK");
        stateCodes.add(37, "OR");
        stateCodes.add(38, "PA");
        stateCodes.add(39, "RI");
        stateCodes.add(40, "SC");
        stateCodes.add(41, "SD");
        stateCodes.add(42, "TN");
        stateCodes.add(43, "TX");
        stateCodes.add(44, "UT");
        stateCodes.add(45, "VT");
        stateCodes.add(46, "VA");
        stateCodes.add(47, "WA");
        stateCodes.add(48, "WV");
        stateCodes.add(49, "WI");
        stateCodes.add(50, "WY");

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
        stateNames.add(9, "Florida");
        stateNames.add(10, "Georgia");
        stateNames.add(11, "Hawaii");
        stateNames.add(12, "Idaho");
        stateNames.add(13, "Illinois");
        stateNames.add(14, "Indiana");
        stateNames.add(15, "Iowa");
        stateNames.add(16, "Kansas");
        stateNames.add(17, "Kentucky");
        stateNames.add(18, "Louisiana");
        stateNames.add(19, "Maine");
        stateNames.add(20, "Maryland");
        stateNames.add(21, "Massachusetts");
        stateNames.add(22, "Michigan");
        stateNames.add(23, "Minnesota");
        stateNames.add(24, "Mississippi");
        stateNames.add(25, "Missouri");
        stateNames.add(26, "Montana");
        stateNames.add(27, "Nebraska");
        stateNames.add(28, "Nevada");
        stateNames.add(29, "New Hampshire");
        stateNames.add(30, "New Jersey");
        stateNames.add(31, "New Mexico");
        stateNames.add(32, "New York");
        stateNames.add(33, "North Carolina");
        stateNames.add(34, "North Dakota");
        stateNames.add(35, "Ohio");
        stateNames.add(36, "Oklahoma");
        stateNames.add(37, "Oregon");
        stateNames.add(38, "Pennsylvania");
        stateNames.add(39, "Rhode Island");
        stateNames.add(40, "South Carolina");
        stateNames.add(41, "South Dakota");
        stateNames.add(42, "Tennessee");
        stateNames.add(43, "Texas");
        stateNames.add(44, "Utah");
        stateNames.add(45, "Vermont");
        stateNames.add(46, "Virginia");
        stateNames.add(47, "Washington");
        stateNames.add(48, "West Virginia");
        stateNames.add(49, "Wisconsin");
        stateNames.add(50, "Wyoming");

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
