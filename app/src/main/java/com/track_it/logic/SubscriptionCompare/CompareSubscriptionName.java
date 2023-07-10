package com.track_it.logic.SubscriptionCompare;

import com.track_it.domainobject.SubscriptionObj;

public class CompareSubscriptionName implements SubscriptionCompare
{

    public int compareSubscriptions(SubscriptionObj left, SubscriptionObj right)
    {

        return left.getName().toLowerCase().compareTo(right.getName().toLowerCase());
    }


}
