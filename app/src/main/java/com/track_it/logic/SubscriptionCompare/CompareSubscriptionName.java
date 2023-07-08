package com.track_it.logic.SubscriptionCompare;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionCompare.SubscriptionComparer;

public class CompareSubscriptionName implements SubscriptionComparer
{

    public int compareSubscriptions(SubscriptionObj left, SubscriptionObj right)
    {

        return left.getName().compareTo(right.getName());
    }


}
