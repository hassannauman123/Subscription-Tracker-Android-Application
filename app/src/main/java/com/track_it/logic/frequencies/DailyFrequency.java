package com.track_it.logic.frequencies;

public class DailyFrequency extends FrequencyBase
{



    public DailyFrequency()
    {
        super("daily");
    }


    @Override
    public int calculateMonthlyPayment(int inputPayment)
    {
        return inputPayment * DAYS_PER_YEAR / MONTHS_PER_YEAR ;
    }

    @Override
    public int daysBetweenPayment()
    {
        return 1;
    }

}
