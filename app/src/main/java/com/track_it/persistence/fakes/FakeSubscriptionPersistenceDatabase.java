package com.track_it.persistence.fakes;
import com.track_it.domainobject.*;
import com.track_it.logic.exceptions.DatabaseException;
import com.track_it.logic.exceptions.DatabaseSubNotFoundException;
import com.track_it.persistence.SubscriptionPersistence;

import java.util.ArrayList;
import java.util.List;


//This is a fake database class that implements the SubscriptionPersistence interface.
public class FakeSubscriptionPersistenceDatabase implements SubscriptionPersistence
{




    // A static Arraylist to hold subscriptions.
    private static ArrayList<SubscriptionObj> subscriptionDB  = new ArrayList<SubscriptionObj>(); // Create a static ArrayList that hold subscription Objects

    private static int databaseCount = 0; // A unique number for the Subscription ID's (Do not ever reduce this number, even when deleting from database)


    // This is used to generate a unique number internally.
    private int getUniqueId()
    {
         return databaseCount; // simple method for now
    }


    @Override
    // Add a subscription to the database.
    public void addSubscriptionToDB( SubscriptionObj inputSubscription)
    {

        inputSubscription.setID(databaseCount);   // Set id
        subscriptionDB.add(inputSubscription); // ADD to database
        databaseCount++;
     }




    // Gets all the subscriptions in the database
    @Override
    public List<SubscriptionObj> getAllSubscriptions()
    {

        ArrayList<SubscriptionObj> returnListOfSubscriptions = new ArrayList<SubscriptionObj>();

        //Go through the DataBase, and create fill the return arrayList with all the subscriptions in the database
        for ( int i =0 ; i < subscriptionDB.size(); i++)
        {
            SubscriptionObj copyOfSubscription = subscriptionDB.get(i).copy(); // Copy the sub so the calling function can't illegally modify our fake database
            returnListOfSubscriptions.add(copyOfSubscription);

        }

        return returnListOfSubscriptions;

    }



    // simply edits a subscription by the ID,
    @Override
    public void editSubscriptionByID (int subscriptionID, SubscriptionObj newDetails) throws DatabaseException
    {
        boolean found = false;
        SubscriptionObj subscriptonToUpdate = null;


        //Find the subscription to edit
        for (int i =0 ; i < subscriptionDB.size(); i++ )
        {
           if (  subscriptionDB.get(i).getID()  == subscriptionID)
           {
                subscriptonToUpdate = subscriptionDB.get(i);
                found = true;
                break;
           }
        }

        // If the subscription was found,  edit it with the new details
        if ( found && subscriptonToUpdate != null)
        {

            subscriptonToUpdate.setName(newDetails.getName());
            subscriptonToUpdate.setPayment(newDetails.getTotalPaymentInCents());
            subscriptonToUpdate.setPaymentFrequency(newDetails.getPaymentFrequency());
        }
        else // Else the subscription was not found, throw an error
        {
            throw new DatabaseSubNotFoundException("Subscription not found in database!");
        }
    }




    // Gets and returns a subscription from the database given the subscriptionID
    @Override
    public SubscriptionObj getSubscriptionByID(int subscriptionID) throws DatabaseException
    {

        SubscriptionObj returnSubscription = null;

        for (int i =0 ; i < subscriptionDB.size();i++)
        {
            if (subscriptionDB.get(i).getID() == subscriptionID)
            {
                returnSubscription = subscriptionDB.get(i).copy();

            }
        }

        if (returnSubscription == null )
        {
            throw new DatabaseSubNotFoundException("Subscription not found in database!");

        }

        return returnSubscription;
    }



    // Tries to remove a subscription with the id of subcriptionID from the database- will throw an exception if it can't be deleted
    @Override
    public void  removeSubscriptionByID( int subcriptionID)
    {
        boolean removed = false;

        for (int i =0 ; i < subscriptionDB.size();i++)
        {
            if (subscriptionDB.get(i).getID() == subcriptionID)
            {
                subscriptionDB.remove(i);
                removed = true;
            }
        }

        if ( !removed)
        {
            throw new DatabaseSubNotFoundException("Cannot delete subscription,\n subscription not found in Database!");
        }
    }

}
