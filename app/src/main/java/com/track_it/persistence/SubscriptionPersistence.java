package com.track_it.persistence;

import com.track_it.domainobject.SubscriptionObj;

import java.util.List;


public  interface  SubscriptionPersistence
{
    List<SubscriptionObj> getAllSubscriptions(); //default sorted by date in ascending order
    void editSubscriptionByID(int subscriptionID, SubscriptionObj newSubscriptionDetails);

    void addSubscriptionDataBase(SubscriptionObj subscriptionToAdd);

    void removeSubscriptionByID(int subscriptionIDToRemove);

    SubscriptionObj getSubscriptionByID(int subscriptionID);
}
