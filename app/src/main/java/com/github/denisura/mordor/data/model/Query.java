package com.github.denisura.mordor.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Query {

    @SerializedName("creditScoreBucket")
    @Expose
    private String creditScoreBucket;
    @SerializedName("loanAmountBucket")
    @Expose
    private String loanAmountBucket;
    @SerializedName("loanToValueBucket")
    @Expose
    private String loanToValueBucket;
    @SerializedName("program")
    @Expose
    private String program;
    @SerializedName("refinance")
    @Expose
    private Boolean refinance;
    @SerializedName("stateAbbreviation")
    @Expose
    private String stateAbbreviation;

    /**
     * @return The creditScoreBucket
     */
    public String getCreditScoreBucket() {
        return creditScoreBucket;
    }

    /**
     * @param creditScoreBucket The creditScoreBucket
     */
    public void setCreditScoreBucket(String creditScoreBucket) {
        this.creditScoreBucket = creditScoreBucket;
    }

    /**
     * @return The loanAmountBucket
     */
    public String getLoanAmountBucket() {
        return loanAmountBucket;
    }

    /**
     * @param loanAmountBucket The loanAmountBucket
     */
    public void setLoanAmountBucket(String loanAmountBucket) {
        this.loanAmountBucket = loanAmountBucket;
    }

    /**
     * @return The loanToValueBucket
     */
    public String getLoanToValueBucket() {
        return loanToValueBucket;
    }

    /**
     * @param loanToValueBucket The loanToValueBucket
     */
    public void setLoanToValueBucket(String loanToValueBucket) {
        this.loanToValueBucket = loanToValueBucket;
    }

    /**
     * @return The program
     */
    public String getProgram() {
        return program;
    }

    /**
     * @param program The program
     */
    public void setProgram(String program) {
        this.program = program;
    }

    /**
     * @return The refinance
     */
    public Boolean getRefinance() {
        return refinance;
    }

    /**
     * @param refinance The refinance
     */
    public void setRefinance(Boolean refinance) {
        this.refinance = refinance;
    }

    /**
     * @return The stateAbbreviation
     */
    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    /**
     * @param stateAbbreviation The stateAbbreviation
     */
    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }


}