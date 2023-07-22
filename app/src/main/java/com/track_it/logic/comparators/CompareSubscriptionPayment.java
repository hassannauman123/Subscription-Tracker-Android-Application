package com.track_it.logic.comparators;

import com.track_it.domainobject.SubscriptionObj;

import java.util.Comparator;


// Compare two subscription objects by Payment amount
public class CompareSubscriptionPayment  implements Comparator<SubscriptionObj>
{


    @Override

    public int compare(SubscriptionObj left, SubscriptionObj right)
    {
        return left.getTotalPaymentInCents() - right.getTotalPaymentInCents();
    }

}
