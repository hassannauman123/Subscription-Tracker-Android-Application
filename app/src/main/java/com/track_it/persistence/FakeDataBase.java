package com.track_it.persistence;
import com.track_it.domainobject.*;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.exceptions.DataBaseException;
import com.track_it.logic.exceptions.DataBaseSubNotFoundException;

import java.util.ArrayList;
import java.util.List;


//This is a fakeDatabase class that implements the SubscriptionPersistence interface.

public class FakeDataBase implements SubscriptionPersistence
{


    // A static Arraylist to hold subscriptions.
    private static ArrayList<SubscriptionObj> subscriptionDB  = new ArrayList<SubscriptionObj>(); // Create a static ArrayList that hold subscription Objects


    private static int dataBaseCount = 0; // A unique number for the Subscription ID's (Do not ever reduce this number, even when deleting from database)


    // This is used to generate a unique number internally.
    private int getUniqueId()
    {
         return dataBaseCount; // simple method for now
    }


    // Add a subscription to the dataBase.
    public void addSubscriptionToDB( SubscriptionObj inputSubscription)
    {

        inputSubscription.setID(dataBaseCount);   // Set id
        subscriptionDB.add(inputSubscription); // ADD to database
        dataBaseCount++;
    }


   //Fill database with fake data
    public static void fillFakeData(final SubscriptionHandler subHandler)
    {

        // Create 10 subs, with random data
        List<String> FrequencyList = subHandler.getFrequencyList();
        int numFrequency = subHandler.getNumFrequencies();

        for (int i =0 ; i < 10; i++)
        {

            try {
                String inputName = "Rand name For subscription" + i;
                String frequency = FrequencyList.get(i % numFrequency);
                int payment = (int) (Math.random() * subHandler.getMaxPaymentCentsTotal() + 1);
                SubscriptionObj currSub = new SubscriptionObj(inputName, payment,frequency);
                subHandler.addSubscription(currSub);

            }
            catch (Exception e)
            {
                System.out.println("ERROR WITH MAKING FAKE DATA!!: " +   e.getMessage());
                assert(false); // Just make the app crash for now
            }
        }


        //Create one long name of max length
        try {

            int payment = 22444;
            String inputName = "long name: ";
            for (int i = inputName.length(); i < subHandler.getMaxNameLength(); i++)
            {
                inputName = inputName + "1";
            }
            String frequency = FrequencyList.get(0);
            SubscriptionObj currSub = new SubscriptionObj(inputName, payment, frequency);
            subHandler.addSubscription(currSub);
        }
        catch (Exception e)
        {
            System.out.println("ERROR WITH MAKING FAKE DATA!!: " +   e.getMessage());
            assert(false); // Just make the app crash for now
        }
    }


    // Gets all the subscriptions in the database
    public List<SubscriptionObj> getAllSubscriptions()
    {

        ArrayList<SubscriptionObj> returnListOfSubscriptions = new ArrayList<SubscriptionObj>();

        //Go through the DataBase, and create fill the return arrayList with all the subscriptions in the database
        for ( int i =0 ; i < subscriptionDB.size(); i++)
        {
            SubscriptionObj copyOfSubscription = subscriptionDB.get(i).copy(); // Copy the sub so the calling function can't illegally modify our fake dataBase
            returnListOfSubscriptions.add(copyOfSubscription);

        }

        return returnListOfSubscriptions;

    }


    // simply edits a subscription by the ID
    public void editSubscriptionByID (int subscriptionID, SubscriptionObj newDetails) throws DataBaseException
    {
        boolean found = false;
        SubscriptionObj subscriptonToUpdate = null;


        for (int i =0 ; i < subscriptionDB.size(); i++ )
        {
           if (  subscriptionDB.get(i).getID()  == subscriptionID)
           {
                subscriptonToUpdate = subscriptionDB.get(i);
                 found = true;
                break;
           }
        }

        if ( found && subscriptonToUpdate != null)
        {

            subscriptonToUpdate.setName(newDetails.getName());
            subscriptonToUpdate.setPayment(newDetails.getTotalPaymentInCents());
            subscriptonToUpdate.setPaymentFrequency(newDetails.getPaymentFrequency());
        }
        else
        {
            throw new DataBaseSubNotFoundException("Subscription not found in dataBase!");
        }
    }




    // Gets and returns a subscription from the dataBase given the subscriptionID
    public SubscriptionObj getSubscriptionByID(int subscriptionID) throws DataBaseException
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
            throw new DataBaseSubNotFoundException("Subscription not found in dataBase!");

        }

        return returnSubscription;
    }



    // Tries to remove a subscription with the id of subcriptionID from the database- will throw an exception if it can't be deleted
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

        if ( removed == false)
        {
            throw new DataBaseSubNotFoundException("Cannot delete subscription,\n subscription not found in Database!");
        }
    }

}
