package com.github.denisura.mordor.data.model;


import android.content.ContentValues;
import android.database.Cursor;

import com.github.denisura.mordor.data.database.ProfileColumns;

public class ProfileModel {

    private long id;
    private String creditScoreBucket;
    private String loanToValueBucket;
    private String program;
    private String location;
    private String loanCategory;
    private String trend;
    private Float currentRate;

    public ProfileModel() {

    }


    public ProfileModel(Cursor cursor) {

        int idx_id = cursor.getColumnIndex(ProfileColumns._ID);
        int idx_credit_score_bucket = cursor.getColumnIndex(ProfileColumns.CREDIT_SCORE_BUCKET);
        int idx_loat_to_value_bucket = cursor.getColumnIndex(ProfileColumns.LOAN_TO_VALUE_BUCKET);
        int idx_program = cursor.getColumnIndex(ProfileColumns.PROGRAM);
        int idx_location = cursor.getColumnIndex(ProfileColumns.STATE);
        int idx_loan_category = cursor.getColumnIndex(ProfileColumns.LOAN_CATEGORY);
        int idx_trend = cursor.getColumnIndex(ProfileColumns.TREND);
        int idx_current_rate = cursor.getColumnIndex(ProfileColumns.CURRENT_RATE);

        setId(cursor.getLong(idx_id));
        setCreditScoreBucket(cursor.getString(idx_credit_score_bucket));
        setLoanToValueBucket(cursor.getString(idx_loat_to_value_bucket));
        setProgram(cursor.getString(idx_program));
        setLocation(cursor.getString(idx_location));
        setLoanCategory(cursor.getString(idx_loan_category));
        setTrend(cursor.getString(idx_trend));
        setCurrentRate(cursor.getFloat(idx_current_rate));
    }

    public ContentValues getContentValues() {
        // Gets a new ContentValues object
        ContentValues v = new ContentValues();

        if (getId() > 0) {
            v.put(ProfileColumns._ID, getId());
        }

        v.put(ProfileColumns.CREDIT_SCORE_BUCKET, getCreditScoreBucket());
        v.put(ProfileColumns.LOAN_TO_VALUE_BUCKET, getLoanToValueBucket());
        v.put(ProfileColumns.PROGRAM, getProgram());
        v.put(ProfileColumns.STATE, getLocation());
        v.put(ProfileColumns.LOAN_CATEGORY, getLoanCategory());
        v.put(ProfileColumns.TREND, getTrend());
        v.put(ProfileColumns.CURRENT_RATE, getCurrentRate());
        return v;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreditScoreBucket() {
        return creditScoreBucket;
    }

    public void setCreditScoreBucket(String creditScoreBucket) {
        this.creditScoreBucket = creditScoreBucket;
    }

    public String getLoanToValueBucket() {
        return loanToValueBucket;
    }

    public void setLoanToValueBucket(String loanToValueBucket) {
        this.loanToValueBucket = loanToValueBucket;
    }

    public String getProgram() {

        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLoanCategory() {

        if (loanCategory == null) {
            return ProfileColumns.LOAN_CATEGORY_PURCHASE;
        }

        return loanCategory;
    }

    public void setLoanCategory(String loanCategory) {
        this.loanCategory = loanCategory;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public Float getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(Float currentRate) {
        this.currentRate = currentRate;
    }
}
