package com.track_it.logic.frequencies;

public abstract class  FrequencyBase implements Frequency
{

    private final String frequencyName;

    public FrequencyBase(String setName)
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