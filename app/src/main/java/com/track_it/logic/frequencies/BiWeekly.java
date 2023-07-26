package com.track_it.logic.frequencies;

public class BiWeekly extends FrequencyBase {
    public BiWeekly() {
        super("bi-weekly");
    }


    @Override
    public int daysBetweenPayment() {
        return DAYS_PER_WEEK * 2;

    }

}
