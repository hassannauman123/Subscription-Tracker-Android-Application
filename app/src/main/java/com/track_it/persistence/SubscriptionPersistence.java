package com.track_it.persistence;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;

import java.util.List;


//This is the interface for a subscription Persistence database
public  interface  SubscriptionPersistence
{
    List<SubscriptionObj> getAllSubscriptions(); // Get all subscriptions in database

    //Edit a subscription by targeting a subscription by its ID, and having all other parameters changed
    void editSubscriptionByID(int subscriptionID, SubscriptionObj newSubscriptionDetails);

     void addSubscriptionToDB(SubscriptionObj subscriptionToAdd);    // Add a subscription to database  - *subscriptionToAdd ID will be set by this method if no exception thrown

    // Remove a subscription by ID
    void removeSubscriptionByID(int subscriptionIDToRemove);

    //Get a subscription object returned from the database by subscriptionID
    SubscriptionObj getSubscriptionByID(int subscriptionID);


}
