package com.github.denisura.mordor.data.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Local {

    @SerializedName("currentRate")
    @Expose
    private CurrentRate currentRate;
    @SerializedName("query")
    @Expose
    private Query query;
    @SerializedName("samples")
    @Expose
    private List<Sample> samples = new ArrayList<Sample>();

    /**
     *
     * @return
     * The currentRate
     */
    public CurrentRate getCurrentRate() {
        return currentRate;
    }

    /**
     *
     * @param currentRate
     * The currentRate
     */
    public void setCurrentRate(CurrentRate currentRate) {
        this.currentRate = currentRate;
    }

    /**
     *
     * @return
     * The query
     */
    public Query getQuery() {
        return query;
    }

    /**
     *
     * @param query
     * The query
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     *
     * @return
     * The samples
     */
    public List<Sample> getSamples() {
        return samples;
    }

    /**
     *
     * @param samples
     * The samples
     */
    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }


}