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
            //studentPersistence = new StudentPersistenceStub();
            subscriptionPersistence = new SubscriptionPersistenceHSQLDB(comp3350.srsys.application.Main.getDBPathName());
        }

        return subscriptionPersistence;
    }


}
