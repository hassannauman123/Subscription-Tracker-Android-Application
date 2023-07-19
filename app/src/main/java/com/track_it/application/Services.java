package com.track_it.application;

import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;


public class Services
{

    private static SubscriptionPersistence subscriptionPersistence = null;


    public static synchronized SubscriptionPersistence getSubscriptionPersistence()
    {
        if (subscriptionPersistence == null)
        {
             subscriptionPersistence = new SubscriptionPersistenceHSQLDB(Main.getDBPathName());
        }

        return subscriptionPersistence;
    }


}
