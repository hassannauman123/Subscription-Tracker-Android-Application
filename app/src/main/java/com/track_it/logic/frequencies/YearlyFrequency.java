package com.track_it.logic.frequencies;


public class YearlyFrequency extends FrequencyBase
{
    public YearlyFrequency()
    {
        super("yearly");
    }

    public int calculateMonthlyPayment(int inputPayment)
    {
        return inputPayment / 12 ;
    }

}