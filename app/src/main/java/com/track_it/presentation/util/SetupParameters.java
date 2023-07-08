

package com.track_it.presentation.util;

import com.track_it.application.Services;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;

public class SetupParameters
{

    // Parameters to set limits on what a subscription can be
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 30;
    private static final int MAX_PAYMENT_DOLLAR = 9999;
    private static final int MAX_PAYMENT_CENTS = 99;

    private static final String allowableCharactersInName = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 _+*^&%$#@!+=|}]'?<>'"; // Our current list of allowable characters in the name


    private static  SubscriptionPersistence DataBase = Services.getSubscriptionPersistence();  // Database that we will use


    public static void initializeDatabase(SubscriptionPersistence inputDB)
    {
        DataBase = inputDB;
    }

    //Wrapper for the logic layer
    public static SubscriptionHandler getSubscriptionHandler()
    {
        return new SubscriptionHandler(MIN_NAME_LENGTH,MAX_NAME_LENGTH,MAX_PAYMENT_DOLLAR,MAX_PAYMENT_CENTS,allowableCharactersInName, DataBase);
    }





}
