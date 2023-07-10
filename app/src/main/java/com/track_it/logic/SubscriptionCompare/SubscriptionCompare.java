package com.track_it.logic.SubscriptionCompare;

import com.track_it.domainobject.SubscriptionObj;

public interface SubscriptionCompare
{
    int compareSubscriptions(SubscriptionObj left,SubscriptionObj right); // return < -1 if left smaller, 0 if equal, and 1 if they are the same
}
