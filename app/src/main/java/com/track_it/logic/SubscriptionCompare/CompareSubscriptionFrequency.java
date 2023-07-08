package com.track_it.logic.SubscriptionCompare;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.presentation.util.SetupParameters;

import java.util.List;



//Compare to subscription by their frequencies
public class CompareSubscriptionFrequency implements SubscriptionComparer
{

    private SubscriptionHandler subhandler;


    public CompareSubscriptionFrequency()
    {
        subhandler = SetupParameters.getSubscriptionHandler();
    }
    public int compareSubscriptions(SubscriptionObj left, SubscriptionObj right)
    {
        int valueReturn = 0;

        List<String> allowableFrequencies = subhandler.getFrequencyList();

        int indexLeft = getIndex(allowableFrequencies, left.getPaymentFrequency());
        int indexRight = getIndex(allowableFrequencies, right.getPaymentFrequency());


        return indexLeft - indexRight;
    }

    private int getIndex( List<String> allowableFrequencies, String inputString) {
        int index = 0;

        for (int i = 0; i < allowableFrequencies.size(); i++)
        {
            if ( allowableFrequencies.get(i).equals(inputString ) )
            {
                index = i ;
            }
        }

        return index;
    }


}
