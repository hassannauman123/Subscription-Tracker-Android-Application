

package com.track_it.presentation.util;

import com.track_it.application.Services;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;

//This is wrapper class for the logic layer.
// It will return a subscription Handler with appropriate set parameters and database.
// Will return the same SubscriptionHandler each time (single item) if the database has not been changed.

public class SetupParameters
{

    // Parameters to set limits on what a subscription can be
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 30;
    private static final int MAX_PAYMENT_DOLLAR = 9999;
    private static final int MAX_PAYMENT_CENTS = 99;

    private static final String allowableCharactersInName = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 -!@#$%^&*()_+=|{=}[]':?<>',."; // Our current list of allowable characters in the name


    private static  SubscriptionPersistence DataBase = Services.getSubscriptionPersistence();  // Database that we will use
    private static SubscriptionHandler subHandler = null ; // subscription handler


    public static void initializeDatabase(SubscriptionPersistence inputDB)
    {
        //Create a new subscription handler if the database has changed
        DataBase = inputDB;
        subHandler = new SubscriptionHandler(MIN_NAME_LENGTH,MAX_NAME_LENGTH,MAX_PAYMENT_DOLLAR,MAX_PAYMENT_CENTS,allowableCharactersInName, DataBase);

    }

    //Wrapper for the logic layer
    public static SubscriptionHandler getSubscriptionHandler()
    {

        if ( subHandler == null) // Only create one instance of the subscription handler, and return it each time
        {
            subHandler = new SubscriptionHandler(MIN_NAME_LENGTH,MAX_NAME_LENGTH,MAX_PAYMENT_DOLLAR,MAX_PAYMENT_CENTS,allowableCharactersInName, DataBase);
        }
        return subHandler;
    }





}
