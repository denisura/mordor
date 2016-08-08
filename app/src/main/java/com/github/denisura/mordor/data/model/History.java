package com.github.denisura.mordor.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class History {

    @SerializedName("rates")
    @Expose
    private Rates rates;

    /**
     * @return The rates
     */
    public Rates getRates() {
        return rates;
    }

    /**
     * @param rates The rates
     */
    public void setRates(Rates rates) {
        this.rates = rates;
    }

}