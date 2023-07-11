package com.track_it.persistence;

import com.track_it.domainobject.SubscriptionObj;

import java.util.List;


//This is the interface for a subscription Persistence database
public  interface  SubscriptionPersistence
{
    List<SubscriptionObj> getAllSubscriptions(); // Get all subscriptions in database

    //Edit a subscription by targeting a subscription by its ID, and having all other parameters changed
    void editSubscriptionByID(int subscriptionID, SubscriptionObj newSubscriptionDetails);

    // Add a subscription to database  - *subscriptionToAdd ID will be set by this method if not exception throw
    void addSubscriptionToDB(SubscriptionObj subscriptionToAdd);

    // Remove a subscription by ID
    void removeSubscriptionByID(int subscriptionIDToRemove);

    SubscriptionObj getSubscriptionByID(int subscriptionID);
}
