package com.track_it.logic.SubscriptionCompare;

import com.track_it.domainobject.SubscriptionObj;

public


class CompareSubscriptionPayment implements SubscriptionCompare
{
    public int compareSubscriptions(SubscriptionObj left, SubscriptionObj right)
    {


        return left.getTotalPaymentInCents() - right.getTotalPaymentInCents();

    }

}
