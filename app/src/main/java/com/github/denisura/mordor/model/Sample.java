package com.github.denisura.mordor.model;

import android.database.Cursor;

import com.github.denisura.mordor.database.HistoryColumns;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.ParseException;

public class Sample {

    @SerializedName("rate")
    @Expose
    private Float rate;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("volume")
    @Expose
    private Integer volume;


    private Long profileId;


    public Sample() {

    }


    public Sample(Cursor cursor) {

        int idx_profile_id = cursor.getColumnIndex(HistoryColumns.PROFILE_ID);
        int idx_rate = cursor.getColumnIndex(HistoryColumns.RATE);
        int idx_time = cursor.getColumnIndex(HistoryColumns.TIME);


        setProfileId(cursor.getLong(idx_profile_id));
        setRate(cursor.getFloat(idx_rate));


        long unixSec = cursor.getLong(idx_time);
        DateTime dateTime = new DateTime(unixSec * 1000L);
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        String time = fmt.print(dateTime);

        setTime(time);
    }


    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }


    /**
     * @return The rate
     */
    public Float getRate() {
        return rate;
    }

    /**
     * @param rate The rate
     */
    public void setRate(Float rate) {
        this.rate = rate;
    }

    /**
     * @return The time
     */
    public String getTime() {
        return time;
    }

    /**
     * @return The time
     */
    public long getTimeMIllies() throws ParseException {
        DateTime dt = new DateTime(time);
        return dt.getMillis() / 1000;
    }


    /**
     * @param time The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return The volume
     */
    public Integer getVolume() {
        return volume;
    }

    /**
     * @param volume The volume
     */
    public void setVolume(Integer volume) {
        this.volume = volume;
    }


}