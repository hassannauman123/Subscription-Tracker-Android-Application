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

}