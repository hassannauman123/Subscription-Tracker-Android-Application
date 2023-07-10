package com.track_it.logic.SubscriptionCompare;

import com.track_it.domainobject.SubscriptionObj;

import java.util.Comparator;


//Compare two subscriptions by their names
public class CompareSubscriptionName implements Comparator <SubscriptionObj>
{

    @Override

    public int compare(SubscriptionObj left, SubscriptionObj right)
    {

        return left.getName().toLowerCase().compareTo(right.getName().toLowerCase());
    }


}
