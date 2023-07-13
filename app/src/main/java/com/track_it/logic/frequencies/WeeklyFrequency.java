package com.track_it.logic.frequencies;

public class WeeklyFrequency extends FrequencyBase
{

    public WeeklyFrequency() //constructor method
    {

        super("weekly");
    }




    @Override
    public int daysBetweenPayment()
    {
        return DAYS_PER_WEEK; // Average number of days between payment
    }


}