package com.track_it.logic.SubscriptionCompare;

import com.track_it.domainobject.SubscriptionObj;

import java.util.List;

public


class CompareSubscriptionPayment implements  SubscriptionComparer
{
    public int compareSubscriptions(SubscriptionObj left, SubscriptionObj right)
    {


        return left.getTotalPaymentInCents() - right.getTotalPaymentInCents();

    }

}
