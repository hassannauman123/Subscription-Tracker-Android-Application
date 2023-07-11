package com.track_it.logic.frequencies;

public class MonthlyFrequency extends FrequencyBase
{
    public MonthlyFrequency()
    {
        super("monthly");
    }



    @Override
    public int calculateMonthlyPayment(int inputPayment)
    {
        return inputPayment ;
    }



    @Override
    public int daysBetweenPayment()
    {
        return DAYS_PER_YEAR / MONTHS_PER_YEAR; // Average number of days between payment
    }

}