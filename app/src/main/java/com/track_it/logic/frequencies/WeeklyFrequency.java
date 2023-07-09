package com.track_it.logic.frequencies;

public class WeeklyFrequency extends FrequencyBase
{

    public WeeklyFrequency() //constructor method
    {

        super("weekly");
    }

    public int calculateMonthlyPayment(int inputPayment)
    {
        return inputPayment * WEEKS_PER_MONTH;
    }


    public int daysBetweenPayment()
    {
        return DAYS_PER_YEAR / ( WEEKS_PER_YEAR); // Average number of days between payment
    }


}