package com.track_it.logic.comparators;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.application.SetupParameters;

import java.util.Comparator;


//Compare two subscriptions by their frequencies
public class CompareSubscriptionFrequency implements Comparator<SubscriptionObj>
{

    private SubscriptionHandler subhandler;


    public CompareSubscriptionFrequency()
    {

        subhandler = SetupParameters.getSubscriptionHandler();
    }





    @Override
    public int compare(SubscriptionObj left, SubscriptionObj right)
    {
        int leftDaysBetweenPayment = subhandler.getFrequencyObject(left).daysBetweenPayment();
        int rightDaysBetweenPayment = subhandler.getFrequencyObject(right).daysBetweenPayment();

        return leftDaysBetweenPayment  - rightDaysBetweenPayment;

    }


}
