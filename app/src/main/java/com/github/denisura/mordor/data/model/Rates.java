package com.github.denisura.mordor.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rates {

    @SerializedName("local")
    @Expose
    private Local local;

    /**
     *
     * @return
     * The local
     */
    public Local getLocal() {
        return local;
    }

    /**
     *
     * @param local
     * The local
     */
    public void setLocal(Local local) {
        this.local = local;
    }


}