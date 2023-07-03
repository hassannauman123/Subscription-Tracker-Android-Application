package com.track_it.logic;


interface Frequency
{
    boolean checkMatch(String inputCheck);
    String getFrequencyName();

    int calculateMonthlyPayment(int paymentAmount);

}


public abstract class  FrequencyType implements Frequency
{

        private final String frequencyName;

        public FrequencyType(String setName)
        {
            frequencyName = setName;
        }
        public String getFrequencyName()
        {
            return frequencyName;

        }

        public boolean checkMatch(String inputCheck)
        {
            return frequencyName.equals(inputCheck);
        }

}


class WeeklyFrequency extends FrequencyType
{
    public WeeklyFrequency()
    {
         super("weekly");
    }

    public int calculateMonthlyPayment(int inputPayment)
    {
        return inputPayment * 4;
    }

}


class MonthlyFrequency extends FrequencyType
{
    public MonthlyFrequency()
    {
        super("monthly");
    }


    public int calculateMonthlyPayment(int inputPayment)
    {
        return inputPayment ;
    }

}


class YearlyFrequency extends FrequencyType
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