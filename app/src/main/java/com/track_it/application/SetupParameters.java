

package com.track_it.application;

import com.track_it.application.Services;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.SubscriptionTagHandler;
import com.track_it.logic.frequencies.BiWeekly;
import com.track_it.logic.frequencies.DailyFrequency;
import com.track_it.logic.frequencies.Frequency;
import com.track_it.logic.frequencies.MonthlyFrequency;
import com.track_it.logic.frequencies.WeeklyFrequency;
import com.track_it.logic.frequencies.YearlyFrequency;
import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.SubscriptionTagPersistence;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;

import java.util.ArrayList;
import java.util.List;

//This is wrapper class for the logic layer.
// It will return a subscription Handler with appropriate set parameters and database.
// Will return the same SubscriptionHandler each time (single item) if the database has not been changed.

public class SetupParameters {

    // Parameters to set limits on what a subscription can be
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 30;
    private static final int MAX_PAYMENT_IN_CENTS = 999999;
    private static final int MIN_PAYMENT_IN_CENTS = 0;
    private static final int MAX_TAGS = 5;

    private static final String allowableCharactersInName = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 -!@#$%^&*()_+=|{=}[]':?<>',."; // Our current list of allowable characters in the name


    private static  final List<Frequency> allowableFrequencies =  InitFrequency() ; // will hold allowable frequencies
    private static SubscriptionPersistence subscriptionPersistenceDatabase = Services.getSubscriptionPersistence();  // Database that we will use
    private static SubscriptionTagPersistence subscriptionTagPersistenceDatabase = Services.getSubscriptionTagPersistence();  // Database that we will use

    private static SubscriptionHandler subHandler = null; // subscription handler

    private static SubscriptionTagHandler tagHandler = null; // subscription handler

    private static String TAG_SPLIT = " ";
    private static int TAG_MIN_LENGTH = 1;
    private static int TAG_MAX_LENGTH = 20;



    public static void initializeDatabase(SubscriptionPersistence inputDB)
    {
        //Create a new subscription handler if the database has changed
        subscriptionPersistenceDatabase = inputDB;
        subHandler = new SubscriptionHandler(MIN_NAME_LENGTH, MAX_NAME_LENGTH, MIN_PAYMENT_IN_CENTS, MAX_PAYMENT_IN_CENTS, allowableCharactersInName,MAX_TAGS, allowableFrequencies, subscriptionPersistenceDatabase);
        tagHandler = new SubscriptionTagHandler(TAG_SPLIT,TAG_MIN_LENGTH,TAG_MAX_LENGTH, subscriptionTagPersistenceDatabase );

    }

    //Wrapper for the logic layer
    public static SubscriptionHandler getSubscriptionHandler() {

        if (subHandler == null) // Only create one instance of the subscription handler, and return it each time
        {
            subHandler = new SubscriptionHandler(MIN_NAME_LENGTH, MAX_NAME_LENGTH, MIN_PAYMENT_IN_CENTS, MAX_PAYMENT_IN_CENTS, allowableCharactersInName, MAX_TAGS, allowableFrequencies, subscriptionPersistenceDatabase);
        }
        return subHandler;
    }


    //Initialize the frequency list
    private static List<Frequency> InitFrequency() {

        ArrayList<Frequency> returnList = new ArrayList<Frequency>();


        returnList.add(new DailyFrequency());
        returnList.add(new WeeklyFrequency());
        returnList.add(new BiWeekly());
        returnList.add(new MonthlyFrequency());
        returnList.add(new YearlyFrequency());

        return returnList;

    }

    //Wrapper for the logic layer
    public static SubscriptionTagHandler getTagHandler()
    {

        if (tagHandler == null) // Only create one instance of the subscription handler, and return it each time
        {
            tagHandler = new SubscriptionTagHandler(TAG_SPLIT,TAG_MIN_LENGTH,TAG_MAX_LENGTH, subscriptionTagPersistenceDatabase );
        }
        return tagHandler;
    }


}
