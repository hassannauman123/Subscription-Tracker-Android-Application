package com.track_it.logic.SubscriptionCompare;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.exceptions.SubscriptionException;
import com.track_it.presentation.util.SetupParameters;

import java.util.List;



//Compare two subscriptions by their frequencies
public class CompareSubscriptionFrequency implements SubscriptionComparer
{

    private SubscriptionHandler subhandler;


    public CompareSubscriptionFrequency()
    {
        subhandler = SetupParameters.getSubscriptionHandler();
    }
    public int compareSubscriptions(SubscriptionObj left, SubscriptionObj right)
    {

        int leftDaysBetweenPayment = 0;
        int rightDaysBetweenPayment = 0;
        try {
              leftDaysBetweenPayment = subhandler.getFrequencyObject(left).daysBetweenPayment();
              rightDaysBetweenPayment = subhandler.getFrequencyObject(right).daysBetweenPayment();
        }
        catch  (SubscriptionException e)
        {
            leftDaysBetweenPayment = 0;
            leftDaysBetweenPayment = 0;
        }

        return leftDaysBetweenPayment  - rightDaysBetweenPayment;

    }


}
