package com.track_it.application;

import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.SubscriptionTagPersistence;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;
import com.track_it.persistence.hsqldb.SubscriptionTagPersistenceHSQLDB;


public class Services {

    private static SubscriptionPersistence subscriptionPersistence = null;
    private static SubscriptionTagPersistence subscriptionTagPersistence = null;


    public static synchronized SubscriptionPersistence getSubscriptionPersistence() {
        if (subscriptionPersistence == null) {
            subscriptionPersistence = new SubscriptionPersistenceHSQLDB(Main.getDBPathName());
        }

        return subscriptionPersistence;
    }

    public static synchronized SubscriptionTagPersistence getSubscriptionTagPersistence() {
        if (subscriptionTagPersistence == null) {
            subscriptionTagPersistence = new SubscriptionTagPersistenceHSQLDB(Main.getDBPathName());
        }

        return subscriptionTagPersistence;
    }


}
