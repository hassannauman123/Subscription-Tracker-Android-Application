package com.track_it.persistence;
import com.track_it.domainObject.*;
//import com.track_it.logic.exception.*;  // Add this back later, android is acting really strange right now

import java.util.ArrayList;




public class DataBase {


    // A temporary static Arraylist to hold subscriptions. Used only for early iterations of project
    private static ArrayList<SubscriptionObj> subscriptionDB  = new ArrayList<SubscriptionObj>(); // Create an ArrayList object


    // Used to generate uniqueID's
    private static int dataBaseCount = 0;


    // When we actually have a database, the uniqueID will be the index into the SQL table
    private int getUniqueId()
    {
         return dataBaseCount;
    }

    // Add a subscription to the data base
    // When we actually have a database this method will take apart the inputSubscription object, and use it to create insert statements for
    // the sql dataBase.
    // Method will (eventually)  throw exceptions if something goes wrong with inserting into database.
    private void addToDataBase( SubscriptionObj inputSubscription)
    {
        if ( dataBaseCount >= Integer.MAX_VALUE) // A preview of what an exception might look like
        {
            //throw new DataBaseFullException("You literally added " +  Integer.MAX_VALUE  + " number of subscriptions, and now the database is full");
        }

        inputSubscription.setID(dataBaseCount); // The database layer will ultimately determine the unique id, and as it will be used as primary index
        subscriptionDB.add(inputSubscription);
        dataBaseCount++;


    }



    // I image that eventually we will have a sql server, and the subscription primary key will be the uniqueID
    private boolean subscription( int inputID)
    {
        boolean removed = false;

        for (int i =0 ; i < subscriptionDB.size();i++)
        {
            if (subscriptionDB.get(i).getID() == inputID)
            {
                subscriptionDB.remove(i);
                removed = true;
            }
        }

        return removed;
    }






}
