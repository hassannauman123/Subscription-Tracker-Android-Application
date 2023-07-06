package com.track_it.logic.frequencies;

public class DailyFrequency extends FrequencyBase
{

    public DailyFrequency()
    {
        super("daily");
    }

    public int calculateMonthlyPayment(int inputPayment)
    {
        return inputPayment * DAYS_PER_YEAR / MONTHS_PER_YEAR ;
    }

}
