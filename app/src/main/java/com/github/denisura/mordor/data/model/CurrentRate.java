package com.github.denisura.mordor.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CurrentRate {

    @SerializedName("rate")
    @Expose
    private Float rate;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("volume")
    @Expose
    private Integer volume;

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