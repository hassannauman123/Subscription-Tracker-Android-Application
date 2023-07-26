package com.track_it.logic.frequencies;


public class YearlyFrequency extends FrequencyBase {

    public YearlyFrequency() {
        super("yearly");
    }


    @Override
    public int daysBetweenPayment() {
        return DAYS_PER_YEAR;
    }

}