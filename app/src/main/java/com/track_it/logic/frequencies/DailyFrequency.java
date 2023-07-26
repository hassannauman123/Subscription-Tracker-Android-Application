package com.track_it.logic.frequencies;

public class DailyFrequency extends FrequencyBase {


    public DailyFrequency() {
        super("daily");
    }


    @Override
    public int daysBetweenPayment() {
        return 1;
    }

}
