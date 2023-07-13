

package com.track_it.presentation.util;

import com.track_it.application.Services;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.frequencies.BiWeekly;
import com.track_it.logic.frequencies.DailyFrequency;
import com.track_it.logic.frequencies.Frequency;
import com.track_it.logic.frequencies.MonthlyFrequency;
import com.track_it.logic.frequencies.WeeklyFrequency;
import com.track_it.logic.frequencies.YearlyFrequency;
import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;

import java.util.ArrayList;
import java.util.List;

//This is wrapper class for the logic layer.
// It will return a subscription Handler with appropriate set parameters and database.
// Will return the same SubscriptionHandler each time (single item) if the database has not been changed.

public class SetupParameters
{

    // Parameters to set limits on what a subscription can be
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 30;
    private static final int MAX_PAYMENT_IN_CENTS = 999999;
    private static final int MIN_PAYMENT_IN_CENTS = 0;

    private static final String allowableCharactersInName = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 -!@#$%^&*()_+=|{=}[]':?<>',."; // Our current list of allowable characters in the name


    private static  List<Frequency> allowableFrequencies  = null; // will hold allowable frequencies
    private static  SubscriptionPersistence DataBase = Services.getSubscriptionPersistence();  // Database that we will use
    private static SubscriptionHandler subHandler = null ; // subscription handler


    public static void initializeDatabase(SubscriptionPersistence inputDB)
    {
        InitFrequency();
        //Create a new subscription handler if the database has changed
        DataBase = inputDB;
        subHandler = new SubscriptionHandler(MIN_NAME_LENGTH,MAX_NAME_LENGTH,MIN_PAYMENT_IN_CENTS,MAX_PAYMENT_IN_CENTS,allowableCharactersInName, allowableFrequencies,DataBase);

    }

    //Wrapper for the logic layer
    public static SubscriptionHandler getSubscriptionHandler()
    {

        InitFrequency();
        if ( subHandler == null) // Only create one instance of the subscription handler, and return it each time
        {
            subHandler = new SubscriptionHandler(MIN_NAME_LENGTH,MAX_NAME_LENGTH,MIN_PAYMENT_IN_CENTS,MAX_PAYMENT_IN_CENTS,allowableCharactersInName, allowableFrequencies, DataBase);
        }
        return subHandler;
    }


    //Initialize the frequency list
    private static void InitFrequency()
    {

        if ( allowableFrequencies == null)
        {
            allowableFrequencies = new ArrayList<Frequency>();
            allowableFrequencies.add(new DailyFrequency());
            allowableFrequencies.add(new WeeklyFrequency());
            allowableFrequencies.add(new BiWeekly());
            allowableFrequencies.add(new MonthlyFrequency());
            allowableFrequencies.add(new YearlyFrequency());
        }
    }





}
