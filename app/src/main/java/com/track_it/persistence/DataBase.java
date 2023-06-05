package com.track_it.persistence;
import com.track_it.domainObject.*;
import com.track_it.exception.*;
import com.track_it.logic.SubscriptionHandler;
import java.util.ArrayList;

// DataBase handler
//
// Currently our data base consists of a single ArrayList of database object



public class DataBase {


    // A temporary static Arraylist to hold subscriptions. Used only for early iterations of project
    private static ArrayList<SubscriptionObj> subscriptionDB  = new ArrayList<SubscriptionObj>(); // Create an ArrayList object (our current database


    // This is currently used to generate uniqueID's (will change once we have sql database)
    private static int dataBaseCount = 0; // Do NOT EVER REDUCE THIS VALUE (even when deleting, as it might no longer be a unique number


    // This is used to generate a unique number internally.
    // When we actually have a database, the uniqueID will be the index into the SQL table
    private int getUniqueId()
    {
         return dataBaseCount; // simple method for now
    }


    // Add a subscription to the dataBase.
    // When we actually have a database this method will take a part the inputSubscription object, and use it to create insert statements for
    // the sql dataBase.
    // Method will throw exceptions if something goes wrong with inserting into database.
    // This will set the subscriptionID of the inputSubscription object. The calling function can use that to get the ID of the newly added subscription.
    public void addSubscriptionDataBase( SubscriptionObj inputSubscription)
    {
        if ( dataBaseCount >= Integer.MAX_VALUE) // A preview of what an exception might look like
        {
            // If you are really dedicated, you can insert MAX_VALUE subscriptions into the database, and see if this error throws correctly
            throw new DataBaseFullException("You literally added " +  Integer.MAX_VALUE  + " number of subscriptions, and now the database is full");
        }

        inputSubscription.setID(dataBaseCount); // The database layer will ultimately determine the unique id, as it will be used as primary index for the sql tables
        subscriptionDB.add(inputSubscription);
        dataBaseCount++;
    }


    // A temporary fake Data filler.
    // Fill the dataBase with 10 fake subs
    public static void fillFakeData()
    {

        String FrequencyList[] = SubscriptionHandler.getFrequencyList();
        int numFrequency = SubscriptionHandler.getNumFrequencies();
        SubscriptionHandler subHandler = new SubscriptionHandler();

        for (int i =0 ; i < 10; i++)
        {


            try {
                String inputName = "Rand name " + i;
                String frequency = FrequencyList[i % numFrequency];
                int payment = (int) (Math.random() * 10000 + 1);
                SubscriptionObj currSub = new SubscriptionObj(inputName, payment,frequency);
                subHandler.addSubscription(currSub);

            }
            catch (Exception e)
            {
                System.out.println("ERROR WITH MAKING FAKE DATA!!: " +   e);
                assert(true); // Just make the app crash for now
            }
        }

    }

    // Gets all the subscriptions in the database
    public ArrayList<SubscriptionObj> queryGetAllSubs()
    {

        ArrayList<SubscriptionObj> returnListOfSubscriptions = new ArrayList<SubscriptionObj>();

        //Go through the DataBase, and create fill the arrayList with all the subscriptions the database
        for ( int i =0 ; i < subscriptionDB.size(); i++)
        {
            SubscriptionObj copyOfSubscription = subscriptionDB.get(i).copy(); // Copy the sub so the calling function can't illegally modify our fake dataBase
            returnListOfSubscriptions.add(copyOfSubscription);

        }

        return returnListOfSubscriptions;

    }


    // This will look a lot different once we have a real database
    // For now simply edits a subscription
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



    // Tries to remove a subscription with the id of subcriptionID from the database- will throw exception if it can't be deleted
    // I image that eventually we will have a sql server, and the subscription primary key for the sql table will be the subcriptionID
    // Do Not reduce Count!
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
