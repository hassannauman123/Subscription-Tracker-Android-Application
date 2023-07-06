package com.track_it.persistence;

import com.track_it.domainobject.SubscriptionObj;

import java.util.List;


public  interface  SubscriptionPersistence
{
    List<SubscriptionObj> getAllSubscriptions(); // Get all subscriptions in database

    //Edit a subscription by targeting a subscription by its ID, and having all other parameters changed
    void editSubscriptionByID(int subscriptionID, SubscriptionObj newSubscriptionDetails);

    // Add a subscription to database *subscriptionToAdd ID will be set by this
    void addSubscriptionToDB(SubscriptionObj subscriptionToAdd);

    // Remove a subscription by ID
    void removeSubscriptionByID(int subscriptionIDToRemove);

    SubscriptionObj getSubscriptionByID(int subscriptionID);
}
